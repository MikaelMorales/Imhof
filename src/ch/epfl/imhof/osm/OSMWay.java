package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Représente un chemin OSM et hérite de OSMEntity
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Morales Gonzalez Mikael (234747)
 */
public final class OSMWay extends OSMEntity {
    private final List<OSMNode> nodes;

    /**
     * Construit un chemin étant donnés son identifiant unique, ses noeuds et
     * ses attributs. Lève une exception si la liste de noeuds a moins de deux
     * éléments.
     * 
     * @param long id
     * @param List
     *            <OSMNode> nodes
     * @param Attributes
     *            attributes
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) {
        super(id, attributes);
        if (nodes.size() < 2) {
            throw new IllegalArgumentException(
                    "La liste de noeuds doit possèdé plus de deux éléments");
        }
        this.nodes = Collections
                .unmodifiableList(new ArrayList<OSMNode>(nodes));
    }

    /**
     * Retourne la taille de nodes.
     * 
     * @return nodes.size()
     */
    public int nodesCount() {
        return nodes.size();
    }

    /**
     * Retourne la liste des noeuds (nodes)
     * 
     * @return nodes
     */
    public List<OSMNode> nodes() {
        return nodes;
    }

    /**
     * Retourne la liste des noeuds du chemin sans le dernier, si celui-ci est
     * identique au premier.
     * 
     * @return List<OSMNode>
     */
    public List<OSMNode> nonRepeatingNodes() {
        if (isClosed()) {
            return nodes.subList(0, nodes.size() - 1);
        } else {
            return nodes;
        }
    }

    /**
     * Retourne le premier noeud du chemin.
     * 
     * @return nodes.get(0)
     */
    public OSMNode firstNode() {
        return nodes.get(0);
    }

    /**
     * Retourne le dernier noeud du chemin.
     * 
     * @return nodes.get(dernier)
     */
    public OSMNode lastNode() {
        return nodes.get(nodes.size() - 1);
    }

    /**
     * Retourne vrai ssi le chemin est fermé, c-à-d si le dernier noeud est égal
     * au premier.
     * 
     * @return true/false
     */
    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }

    /**
     * Bâtisseur de la classe OSMWay, permet de construire un chemin OSM en
     * plusieurs étapes. Il hérite du batîsseur de OSMEntity.
     *
     * @author Charles Parzy Turlat (250628)
     * @author Morales Gonzalez Mikael (234747)
     */
    public static final class Builder extends OSMEntity.Builder {
        private final List<OSMNode> nodes;

        /**
         * Construit un batîsseur pour un chemin selon un identifiant donné en
         * paramètre.
         * 
         * @param long id
         */
        public Builder(long id) {
            super(id);
            nodes = new ArrayList<>();
        }

        /**
         * Ajoute un noeud à la fin des noeuds du chemin en cours de
         * construction.
         * 
         * @param OSMNode
         *            newNode
         */
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }

        /**
         * Construit et retourne le chemin avec les noeuds et les attributs
         * ajoutés jusqu'à maintenant. Lève une exception si le chemin est
         * incomplet.
         * 
         * @return objet de type OSMWay
         */
        public OSMWay build() {
            if (isIncomplete()) {
                throw new IllegalStateException();
            } else {
                return new OSMWay(id, nodes, getAttributes());
            }
        }

        /**
         * Redéfinit la méthode isIncomplete de OSMEntity, pour que un chemin
         * possèdant moins de 2 noeuds soit considéré comme incomplet.
         */
        @Override
        public boolean isIncomplete() {
            if (nodes.size() < 2) {
                return true;
            }
            return super.isIncomplete();
        }

    }

}
