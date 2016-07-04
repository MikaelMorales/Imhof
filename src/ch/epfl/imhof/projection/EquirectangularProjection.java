package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Projection "simple" qui consiste a utiliser la latitude et la longitude
 * commme coordonnées cartésiennes.
 * 
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 *
 */
public final class EquirectangularProjection implements Projection {

    /**
     * On considère la latitude et la longitude comme les coordonnées x et y
     * d'un Point.
     * 
     * @param PointGeo
     *            Un point en coordonnées sphériques.
     * @return Point Un point dans un plan.
     */
    @Override
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }

    /**
     * On considère les coordonnées x et y d'un point comme la latitude et la
     * longitude.
     * 
     * @param Point
     *            Un point dans un plan.
     * @return PointGeo Un point en coordonnées sphériques.
     */
    @Override
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
}
