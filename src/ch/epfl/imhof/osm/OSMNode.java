package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * La classe OSMNode représente un noeud OpenStreetMap caractérisé par un
 * identifiant, une position (en coordonnées sphériques) et des attribus. Cette
 * classe hérite de la classe OSMEntity
 * 
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 */
public final class OSMNode extends OSMEntity {
    private final PointGeo position;

    /**
     * Construit une entité OSMNode.
     * 
     * @param id
     *            (long) l'identifiant
     * @param position
     *            (PointGeo) les coordonnées du noeuds en coordonnées sphériques
     * @param attributes
     *            (Attributes) les attributs caractérisant le noeud
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        this.position = position;
    }

    /**
     * Getter retournant les coordonnées sphériques du noeud
     * 
     * @return position (PointGeo)
     */
    public PointGeo position() {
        return position;
    }

    /**
     * Builder de la classe OSMNode, hérite du builder de la classe OSMEntity.
     * 
     * @author Mikael Morales Gonzalez (234747)
     * @author Charles Parzy Turlat (250628)
     */
    public static final class Builder extends OSMEntity.Builder {
        private final PointGeo position;

        /**
         * Construit un bâtisseur pour un nœud ayant l'identifiant et la
         * position donnés.
         * 
         * @param id
         *            (long) identifiant
         * @param position
         *            (PointGeo) Position en coordonnées sphériques du futur
         *            point
         */
        public Builder(long id, PointGeo position) {
            super(id);
            this.position = position;
        }

        /**
         * Construit une instance de OSMNode, c'est-à-dire un noeud à partir des
         * attributs passés au constructeur.
         * 
         * @return new OSMNode(id, position, attributes.build())
         */
        public OSMNode build() {
            if (isIncomplete()) {
                throw new IllegalStateException();
            }
            return new OSMNode(id, position, getAttributes());
        }
    }
}
