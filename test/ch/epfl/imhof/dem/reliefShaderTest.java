package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public class reliefShaderTest {

    public static void main(String[] args) throws IOException {

        // Vecteur lumière
        final Vector3 vector = new Vector3(-1, 1, 1).normalized();  
//        
//        final double rayon = 0;
//        
        Projection project = new CH1903Projection();
        Point bl = project.project(new PointGeo(Math.toRadians(7.1), Math.toRadians(46.6)));
        Point tr = project.project(new PointGeo(Math.toRadians(7.2), Math.toRadians(46.7)));

        
        File file = new File(reliefShaderTest.class.getClass().getResource("/N46E007.hgt")
                .getPath());
        HGTDigitalElevationModel model = new HGTDigitalElevationModel(file);

        // Création de l'image discrète
 
        ReliefShader relief = new ReliefShader(project, model, vector);
        BufferedImage imageBlur = relief.shadedRelief(bl, tr, 800, 800,
                0);
        // Dessin de la carte et stockage dans un fichier
        ImageIO.write(imageBlur, "png", new File("relief.png"));
        model.close();
    }

}
