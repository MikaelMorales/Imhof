package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Sous-classe de PolyLine qui représente une PolyLine ouverte.
 * 
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 */
public final class OpenPolyLine extends PolyLine {
    /**
     * Construit une PolyLine ouverte en faisant appel aux super-constructeur.
     * 
     * @param points
     *            (liste de sommets)
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * Redéfinition de la méthode de PolyLine qui retourne false, car une
     * PolyLine ouverte n'est jamais fermée.
     */
    @Override
    public boolean isClosed() {
        return false;
    }

}
