package ch.epfl.imhof.painting;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Classe qui permet de mettre deux image l'une
 * à côté de l'autre.
 *	@author Charles Parzy Turlat (250628)
 *	@author Morales Gonzalez Mikael (234747)
 */
public final class MergeImages {

    /**
     * Constructeur privé de classe MergeImages
     */
    private MergeImages(){}
    
    /**
     * @param image1 (BufferedImage)
     * @param image2 (BufferedImage)
     * @return Nouvelle BufferedImage en mettant l'image1 à gauche de
     * l'image2
     */
    public static BufferedImage merge(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth() + image2.getWidth();
        int height = Math.max(image1.getHeight(), image2.getHeight());
        
        BufferedImage mergeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gtx = mergeImage.createGraphics();
        gtx.setColor(Color.WHITE.convert());
        gtx.fillRect(0, 0, width, height);
        
        gtx.drawImage(image1, 0, 0, null);
        gtx.drawImage(image2, image1.getWidth(), 0, null);
        
        return mergeImage;
    }
}
