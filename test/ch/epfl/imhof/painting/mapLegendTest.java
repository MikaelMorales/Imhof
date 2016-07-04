/*
 *	Author:      Morales Gonzalez Mikael
 *	Sciper:		 234747
 *	Date:        22 mai 2015
 */

package ch.epfl.imhof.painting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class mapLegendTest {
    public static void main(String[] args) throws IOException {
        BufferedImage image = MapLegend.createLegend(800, Color.WHITE);
        ImageIO.write(image, "png", new File("LEGEND.png"));
    }
}
