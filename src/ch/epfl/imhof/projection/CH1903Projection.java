package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * On transforme un Point CH1903 en WGS 84, en utilisant les formules revisitées
 * de swisstopo.
 * 
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 *
 */
public final class CH1903Projection implements Projection {
    /**
     * On projette un point en coordonnées sphériques en coordonnées
     * cartésiennes. (En modifiant tout d'abord les attributs de PointGeo de
     * radian à degrées).
     * 
     * @param PointGeo
     *            Un point en coordonnées sphériques.
     * @return Point Un point dans un plan.
     */
    @Override
    public Point project(PointGeo point) {
        double longitude = Math.toDegrees(point.longitude());
        double latitude = Math.toDegrees(point.latitude());
        double rapport = (1.0 / 10000d);
        // Longitude = Lambda et Latitude = Phi
        double lambda1 = rapport * (longitude * 3600 - 26782.5);
        double phi1 = rapport * (latitude * 3600 - 169028.66);
        double phi1Carre = phi1*phi1;
        double lambda1Carre = lambda1*lambda1;
        double x = 600072.37 + 211455.93 * lambda1 - 10938.51 * lambda1 * phi1
                - 0.36 * lambda1 * phi1Carre - 44.54
                * (lambda1*lambda1Carre);

        double y = 200147.07 + 308807.95 * phi1 + 3745.25
                * lambda1Carre + 76.63 * phi1Carre - 194.56
                * lambda1Carre * phi1 + 119.79 * (phi1Carre*phi1);

        return new Point(x, y);
    }

    /**
     * On "dé-projette" un point en coordonnées cartésiennes en cordonnées
     * sphériques (L'inverse de la méthode project). (En modifiant les variables
     * x et y en radian).
     * 
     * @param Point
     *            Un point dans un plan.
     * @return PointGeo Un point en coordonnées sphériques.
     */
    @Override
    public PointGeo inverse(Point point) {
        double x = point.x();
        double y = point.y();
        double x1 = (x - 600000) / 1000000d;
        double y1 = (y - 200000) / 1000000d;
        double y1Carre = y1*y1;
        double x1Carre = x1*x1;
        double rapport = (100 / 36d);
        double lambda0 = 2.6779094 + 4.728982 * x1 + 0.791484 * x1 * y1
                + 0.1306 * x1 * y1Carre - 0.0436 * (x1Carre*x1);
        double phi0 = 16.9023892 + 3.238272 * y1 - 0.270978 * x1Carre
                - 0.002528 * y1Carre - 0.0447 * x1Carre * y1
                - 0.0140 * (y1Carre*y1);
        double lambda = lambda0 * rapport;
        double phi = phi0 * rapport;

        return new PointGeo(Math.toRadians(lambda), Math.toRadians(phi));
    }

}
