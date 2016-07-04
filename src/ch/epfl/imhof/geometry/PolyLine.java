package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entité géométrique utilisée pour dessiner les cartes Elle représente les
 * rivières, les lignes de chemin de fer,etc..
 * 
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 *
 */
public abstract class PolyLine {
    private final List<Point> sommets;

    /**
     * Méthode qui construit une polyligne avec les sommets donnés. Elle lève
     * l'exception IllegalArgumentException si la liste des sommets est vide.
     * 
     * @param points (liste de points)
     */
    public PolyLine(List<Point> points) {
        if (points.isEmpty()) {
            throw new IllegalArgumentException(
                    "La liste des sommets est vide !");
        } else {
            sommets = Collections.unmodifiableList(new ArrayList<>(points));
        }
    }

    /**
     * Méthode abstraite redéfinie dans les sous-classes.
     */
    public abstract boolean isClosed();

    /**
     * 
     * @return sommets (La liste des sommets de la PolyLine)
     */
    public List<Point> points() {
        return sommets;
    }

    /**
     * 
     * @return sommets.get(0) (Premier éléments de la liste des sommets de la
     *         PolyLine)
     */
    public Point firstPoint() {
        return sommets.get(0);
    }

    /**
     * Bâtisseur qui sert aux deux sous-classes de PolyLinge. Permet de
     * construire une polyline en plusieurs étapes.
     * 
     * @author Mikael Morales Gonzalez (234747)
     * @author Charles Parzy Turlat (250628)
     *
     */
    public final static class Builder {
        private final List<Point> points = new ArrayList<>();

        /**
         * Ajoute un point donné à la fin de la liste des sommets de la
         * polyline en cours de construction.
         * 
         * @param newPoint
         *            (un Point)
         */
        public void addPoint(Point newPoint) {
            points.add(newPoint);
        }

        /**
         * Construit une PolyLine ouverte avec les points ajoutés jusqu'à
         * présent.
         * 
         * @return OpenPolyLine
         */
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(points);
        }

        /**
         * Construit une PolyLine fermée avec les points ajoutés jusqu'à
         * présent.
         * 
         * @return ClosedPolyLine
         */
        public ClosedPolyLine buildClosed() {
            return new ClosedPolyLine(points);
        }
    }
}
