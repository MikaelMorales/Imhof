package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Une interface projection, dotée de deux méthodes, une qui projette sur le
 * plan un point et une qui "dé-projette" le point du plan.
 * 
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 *
 */
public interface Projection {
    /**
     * Projette sur le plan le point reçu en argument
     * 
     * @param point
     */
    public Point project(PointGeo point);

    /**
     * "Dé-porjette le point du plan reçu en argument
     * 
     * @param point
     */
    public PointGeo inverse(Point point);
}
