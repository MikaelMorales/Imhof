package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entité géométrique permettant de représenter les bâtiments, les lacs, les
 * forêts, etc..
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Mikael Morales Gonzalez (234747)
 *
 */
public final class Polygon {
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;

    /**
     * Construit un polygone avec l'enveloppe (shell) et les trous donnés
     * (holes).
     * 
     * @param shell
     * @param holes
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<>(holes));
    }

    /**
     * Construit un polygone avec l'enveloppe donnée (shell) mais sans trous.
     * 
     * @param shell
     */
    public Polygon(ClosedPolyLine shell) {
        this(shell, Collections.emptyList());
    }

    /**
     * Méthode qui retourne l'attribut shell du polygone, c'est-à-dire
     * l'enveloppe du polygone.
     * 
     * @return shell
     */
    public ClosedPolyLine shell() {
        return shell;
    }

    /**
     * Méthode qui retourne l'attribut holes du polygone, c'est-à-dire les trous
     * du polygone.
     * 
     * @return holes
     */
    public List<ClosedPolyLine> holes() {
        return holes;
    }
}
