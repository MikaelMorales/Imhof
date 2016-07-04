package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente une carte OpenStreetMap, donc un ensemble de chemins et de
 * relations.
 * 
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 */
public final class OSMMap {
    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    /**
     * Construit une carte OSM avec les chemins et les relations donnés.
     * 
     * @param ways (OSMWay)
     * @param relations (OSMRelation)
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList<OSMWay>(ways));
        this.relations = Collections
                .unmodifiableList(new ArrayList<OSMRelation>(relations));
    }

    /**
     * Retourne la liste des chemins de la carte.
     * 
     * @return List<OSMWay>
     */
    public List<OSMWay> ways() {
        return ways;
    }

    /**
     * Retourne la liste des relations de la carte.
     * 
     * @return List<OSMRelation>
     */
    public List<OSMRelation> relations() {
        return relations;
    }

    /**
     * Bâtisseur de la classe OSMMap, permet de construit une OSMMap en
     * plusieurs étapes. Elle stocke les noeuds afin de permettre aux
     * utilisateurs d'y accéder.
     * 
     * @author Charles Parzy Turlat (250628)
     * @author Morales Gonzalez Mikael (234747)
     */
    public final static class Builder {
        private final Map<Long, OSMNode> node = new HashMap<>();
        private final Map<Long, OSMWay> ways = new HashMap<>();
        private final Map<Long, OSMRelation> relations = new HashMap<>();

        /**
         * Ajoute le noeud donné au batisseur.
         * 
         * @param OSMNode newNode
         */
        public void addNode(OSMNode newNode) {
            node.put(newNode.id(), newNode);
        }

        /**
         * Retourne le noeud dont l'identifiant unique est égal à celui donné
         * en paramètre ou null si ce noeud n'a pas été ajouté au bâtisseur.
         * 
         * @param long id
         * @return OSMNode
         */
        public OSMNode nodeForId(long id) {
            return node.get(id);
        }

        /**
         * Ajoute le chemin donné à la carte en cours de construction.
         * 
         * @param OSMWay newWay
         */
        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }

        /**
         * Retourne le chemin dont l'identifiant unique est égal à celui donné
         * ou null si ce chemin n'a pas été ajouté au bâtisseur.
         * 
         * @param long id
         * @return OSMWay
         */
        public OSMWay wayForId(long id) {
            return ways.get(id);
        }

        /**
         * Ajoute la relation donnée à la carte en cours de construction.
         * 
         * @param OSMRelation newRelation
         */
        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }

        /**
         * Retourne la relation dont l'identifiant unique est égal à celui
         * donné ou null si cette relation n'a pas été ajoutée au bâtisseur.
         * 
         * @param long id
         * @return relation.get(id)
         */
        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }

        /**
         * Construit une carte OSM avec les chemins et les relations ajoutés
         * jusqu'à présent.
         * 
         * @return OSMMap
         */
        public OSMMap build() {
            return new OSMMap(ways.values(), relations.values());
        }

    }
}
