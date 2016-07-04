package ch.epfl.imhof.painting;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe dessinant le quadrillage à l'échelle sur la carte
 *	@author Charles Parzy Turlat (250628)
 *	@author Morales Gonzalez Mikael (234747)
 */
public final class Grid {
    
    /**
     * Constructeur privé de la classe.
     */
    private Grid(){};

    /**
     * Méthode dessinant un quadrillage sur la carte dont les carreaux ont un
     * coté de 500 mètres.
     * 
     * @param image
     *            (BufferedImage) : l'image sur laquelle on dessine le
     *            quadrillage.
     * @param style
     *            (LineStyle) : un style de ligne pour les lignes du
     *            quadrillage.
     * @param resolution
     *            (int) la résolution de l'image.
     */
    public static void drawGrid(BufferedImage image, Point bl, Point br,
            double width) {
        BasicStroke stroke = new BasicStroke(2f);
        Graphics2D g = image.createGraphics();
        g.setStroke(stroke);
        g.setColor(new java.awt.Color(0, 0, 0, 0.5f));
        Projection projection = new CH1903Projection();
        // On projette bl et tr.
        PointGeo bottomLeft = projection.inverse(bl);
        PointGeo bottomRight = projection.inverse(br);
        // Formule d'Haversine.
        // On calcule la distance en mètre en les points en bas à droite et à
        // gauche.
        double deltaLat = bottomRight.latitude() - bottomLeft.latitude();
        double deltaLon = bottomRight.longitude() - bottomLeft.longitude();
        double a = Math.pow(Math.sin(deltaLat / 2d), 2)
                + Math.cos(bottomLeft.latitude())
                * Math.cos(bottomRight.latitude())
                * Math.pow(Math.sin(deltaLon / 2d), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = Earth.RADIUS * c;
        // On définit le nombre de pixel par mètre sur l'image.
        double scale = image.getWidth() / d;
        // On définit l'intervalle pour lequel on dessine un trait.
        int r = (int) (width * scale);

        // On dessine les lignes horizontales.
        for (int y = image.getHeight(); y > 0; y -= r) {
            g.drawLine(0, y, image.getWidth(), y);
        }

        // On dessine les lignes verticales.
        for (int x = 0; x < image.getWidth(); x += r) {
            g.drawLine(x, 0, x, image.getHeight());
        }

        drawScale(image, r, g, width);
    }

    /**
     * Méthode dessinant une échelle sur la carte.
     * 
     * @param image
     *            (BufferedImage) : image sur laquelle on dessine l'échelle.
     * @param r
     *            (int) : largeur d'un carré. Ce sera la taille de l'échelle.
     * @param g
     *            (Graphics2D) : contexte graphique.
     * @param width
     *            (double) : largeur en mètres représentée par l'échelle sur la
     *            carte.
     */
    private static void drawScale(BufferedImage image, int r, Graphics2D g,
            double width) {
        g.setColor(java.awt.Color.black);
        g.fillRect(2 * r, image.getHeight() - 100, (int) (r / 2d), 15);
        g.setColor(java.awt.Color.white);
        g.fillRect((int) (2.5 * r), image.getHeight() - 100, (int) (r / 2d), 15);
        Font f = new Font("scale", 60, 40);
        String s = (int)width + " m";
        g.setFont(f);
        g.setColor(Color.BLACK.convert());
        g.drawString(s, 2 * r, image.getHeight() - 110);
    }

}
