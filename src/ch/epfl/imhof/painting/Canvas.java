package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.*;

/**
 * Interface qui représente une toile.
 *	@author Charles Parzy Turlat (250628)
 *	@author Morales Gonzalez Mikael (234747)
 */
public interface Canvas {
    
    /**
     * Permet de dessiner sur la toile une polyligne donnée avec un style de
     * ligne donné.
     * @param poly (Polyline)
     * @param style (LineStyle)
     */
    public void drawPolyLine(PolyLine poly, LineStyle style);
    
    /**
     * Permet de dessiner sur la toile un polygone donné avec une couleur
     * donnée.
     * @param poly (Polygon)
     * @param color (Color)
     */
    public void drawPolygon(Polygon poly, Color color);

}
