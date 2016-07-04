package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * Un point à la surface de la Terre, en coordonnées cartésiennes
 *
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 */
public final class Point {
    private final double x;
    private final double y;

    /**
     * Construit un point selon un paramètre x et y données
     * 
     * @param x
     *            , un double
     * @param y
     *            , un double
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retourne la coordonnée x du point
     * 
     * @return x
     */
    public double x() {
        return x;
    }

    /**
     * Retourne la coordonnée y du point
     * 
     * @return y
     */
    public double y() {
        return y;
    }
    /**
     * Retourne une fonction qui s'applique a un point de l'ancien repère, et le
     * projette dans le nouveau repère.
     * 
     * @param p1Old point 1 dans l'ancien repère
     * @param p1New point 1 dans le nouveau repère
     * @param p2Old point 2 dans l'ancien repère
     * @param p2New point 2 dans le nouveau repère
     * @return
     */
    public static Function<Point, Point> alignedCoordinateChange(Point p1Old,
            Point p1New, Point p2Old, Point p2New) {
                return p -> {
                    if(p1Old.x() == p2Old.x || p1Old.y == p2Old.y) 
                        throw new IllegalArgumentException();
                
                     double a = factorDilatation(p1Old.x, p1New.x, p2Old.x, p2New.x);
                     double c = Translation(p1Old.x, p1New.x, a);
                     double b = factorDilatation(p1Old.y, p1New.y, p2Old.y, p2New.y);
                     double d = Translation(p1Old.y, p1New.y, b);
                     
                     return new Point(a*p.x + c, b*p.y+d);
                };
    }
    
    /**
     * 
     * @param x1 coordonnée x ou y du point 1 dans l'ancien repère
     * @param newX1 coordonnée x ou y du point 1 dans le nouveau repère
     * @param x2 coordonnée x ou y du point 2 dans l'ancien repère
     * @param newX2 coordonnée x ou y du point 2 dans le nouveau repère
     * @return Calcule le facteur de dilation en x ou y de la projection d'un Point de l'ancien
     * repère au nouveau.
     */
    private static double factorDilatation(double x1, double newX1, double x2, double newX2) {
        return (newX1-newX2)/(x1-x2);
    }
    
    /**
     * @param x1 coordonnée x ou y du point 1 dans l'ancien repère
     * @param newX1 coordonnée x ou y du point 1 dans le nouveau repère
     * @param a facteur de dilatation en x ou y.
     * @return la valeur de la translation en x ou y, de l'ancien au nouveau repère.
     */
    private static double Translation(double x1, double newX1, double a) {
        return newX1-(a*x1);
    }
}
