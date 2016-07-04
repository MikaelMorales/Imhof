package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Grid;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.MapLegend;
import ch.epfl.imhof.painting.MergeImages;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

/**
 * Main class du projet permettant de générer une carte avec des reliefs et des
 * ombres.
 *
 * @author Charles Parzy Turlat (250628)
 * @author Morales Gonzalez Mikael (234747)
 */
public class Main {

    /**
     * Méthode main exécutée afin de générer la carte.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 8)
            throw new IllegalStateException("8 arguments are needed !");

        // Attribution des variables
        String osmPath = args[0];
        String hgtPath = args[1];
        double longitudeBL = 0;
        double latitudeBL = 0;
        double longitudeTR = 0;
        double latitudeTR = 0;
        int resolution = 0;
        try {
            longitudeBL = Double.parseDouble(args[2]);
            latitudeBL = Double.parseDouble(args[3]);
            longitudeTR = Double.parseDouble(args[4]);
            latitudeTR = Double.parseDouble(args[5]);
            resolution = Integer.parseInt(args[6]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Latitudes/Longitudes and the resolution must be numbers !");
        }
        String nom = args[7];
        
        /*Etablir les valeurs des constantes:
         *Vecteur lumière
         *Resolution en pixel par mètres, 1 dpi = 1/0.0254 pixel/m
         *Rayon de 1.7mm en pixels
         */
        
        final Vector3 vector = new Vector3(-1, 1, 1);
        final double resPixel = resolution / 0.0254;    
        final double rayon = 1.7 * resPixel / 1000d;

        // Projection CH1903Projection
        Projection project = new CH1903Projection();

        // Point bas-gauche et haut-droit en coordonnées projetées
        Point bl = project.project(new PointGeo(Math.toRadians(longitudeBL),
                Math.toRadians(latitudeBL)));
        Point tr = project.project(new PointGeo(Math.toRadians(longitudeTR),
                Math.toRadians(latitudeTR)));

        // Calcul de la largeur et de la hauteur
        final int height = (int)Math.round((Earth.RADIUS / 25000d)
                * (Math.toRadians(latitudeTR) - Math.toRadians(latitudeBL))
                * resPixel);

        final int width = (int)Math.round(height * (tr.x() - bl.x()) / (tr.y() - bl.y()));

        // Map du fichier
        Painter painter = SwissPainter.painter();
        OSMMap osmmap = null;
        try {
            osmmap = OSMMapReader.readOSMFile(osmPath, true);
        } catch (SAXException e) {
            e.printStackTrace();
        }

        OSMToGeoTransformer projection = new OSMToGeoTransformer(
                new CH1903Projection());
        Map map = projection.transform(osmmap);

        // Création du canvas
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, resolution,
                Color.WHITE);
        painter.drawMap(map, canvas);
        BufferedImage image = canvas.image();

        // Définit le HGTDigitalElevation et fait les reliefs
        HGTDigitalElevationModel model = new HGTDigitalElevationModel(new File(
                hgtPath));
        ReliefShader relief = new ReliefShader(project, model, vector);
        BufferedImage imageBlur = relief.shadedRelief(bl, tr, width, height, rayon);

        // On combine les deux images en une seul image.
        BufferedImage finalImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color couleur1 = Color.rgb(image.getRGB(x, y));
                Color couleur2 = Color.rgb(imageBlur.getRGB(x, y));
                Color finalColor = couleur1.multiply(couleur2);
                finalImage.setRGB(x, y, finalColor.convert().getRGB());
            }
        }
        
        //Dessine le quadrillage
        double gridWidth = 500.0;
        Point br = project.project(new PointGeo(Math.toRadians(longitudeTR), Math.toRadians(latitudeBL)));
        Grid.drawGrid(finalImage, bl, br, gridWidth);
        
        //Dessine la legend
        BufferedImage legend = MapLegend.createLegend(finalImage.getHeight(), Color.gray(0.93));
        finalImage = MergeImages.merge(legend, finalImage);

        // Stockage de la carte dans un fichier et close model.
        ImageIO.write(finalImage, "png", new File(nom));
        model.close();
    }
}
