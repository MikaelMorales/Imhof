package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe ClosedPolyLigne hérite de la classe PolyLine représente une polyligne
 * fermée elle est publique final et immuable.
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Mikael Morales Gonzalez (234747)
 *
 */
public final class ClosedPolyLine extends PolyLine {
    /**
     * Constructeur de la classe ClosedPolyLine
     * 
     * @param points
     *            une liste de point de type Point
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * Version concrète de la méthode abstraite contenue dans la super classe
     * PolyLine.
     * 
     * @return true car la Polyligne est fermée
     */
    public boolean isClosed() {
        return true;
    }

    /**
     * Retourne le modulo de i par rapport à la taille de la liste de points
     * caractérisant la ClosedPolyLine retourne aussi le modulo de
     * nombre négatifs.
     * @param i
     *        un entier (int)
     * @return i mod points().size();
     */
    private int generalise(int i) {
        return (Math.floorMod(i, points().size()));
    }

    /**
     * Retourne l'aire signée (double) selon les formules
     * données.
     * @return aire signée 
     */
    public double aireSignee() {
        double airePlusMoins = 0.0;

        for (int i = 0; i < points().size(); ++i) {
            airePlusMoins += (points().get(i).x())
                    * points().get(generalise(i + 1)).y()
                    - points().get(generalise(i + 1)).x() * points().get(i).y();
        }
        return ((0.5) * airePlusMoins);
    }

    /**
     * Retourne la valeur absolue de l'aire signée (double)
     * @return aire
     */
    public double area() {
        return (Math.abs(aireSignee()));
    }

    /**
     * Retourne true si le Point unPoint est à gauche de la Polyligne fermée
     * false si le Point unPoint n'est pas à gauche de la Polyligne
     * fermée.
     * 
     * @param a
     *            un Point de la Polyligne
     * @param b
     *            un Point de la Polyligne
     * @param unPoint
     *            un Point
     * @return true/false
     */
    private boolean isLeft(Point a, Point b, Point unPoint) {
        // On crée une ClosedPolyLine avec les arguments.
        List<Point> myList = new ArrayList<Point>(3);
        myList.add(a);
        myList.add(b);
        myList.add(unPoint);
        ClosedPolyLine newLine = new ClosedPolyLine(myList);
        // Si aire signée > 0, le point est à gauche, sinon à droite.
        double aire = newLine.aireSignee();
        return aire > 0;
    }

    /**
     * Retourne true si p est à l'interieur de la Polyligne fermée false si p est
     * à l'exterieur de la Polyligne fermée.
     * 
     * @param p
     *            un objet de type Point
     * @return true/false
     */
    public boolean containsPoint(Point p) {
        int compteur = 0;
        for (int i = 0; i < points().size(); ++i) {
            if (points().get(i).y() <= p.y()) {
                if ((points().get(generalise(i + 1)).y() > p.y())
                        && (isLeft(points().get(i),
                                points().get(generalise(i + 1)), p))) {
                    ++compteur;
                }
            } else {
                if ((points().get(generalise(i + 1)).y() <= p.y())
                        && (isLeft(points().get(generalise(i + 1)), points()
                                .get(i), p))) {
                    --compteur;
                }
            }
        }
        return (compteur != 0);
    }
}
