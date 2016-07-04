package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe générique dont le paramètre de type, N, représente le type
 * des nœuds du graphe représente un graphe non orienté.
 * 
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 * @param <N>
 * 
 */
public final class Graph<N> {

    private final Map<N, Set<N>> neighbors;

    /**
     * Construit un graphe non orienté avec la table des voisins donnée
     * 
     * @param neighbors
     *            Map dont les clés sont des noeuds et les valeurs, l'ensemble
     *            de noeux voisins
     */
    public Graph(Map<N, Set<N>> neighbors) {
        Map<N, Set<N>> myMap = new HashMap<>();
        for (Map.Entry<N, Set<N>> key : neighbors.entrySet()) {
            myMap.put(key.getKey(), Collections.unmodifiableSet(new HashSet<N>(key.getValue())));
        }
        this.neighbors = Collections.unmodifiableMap(new HashMap<N, Set<N>>(myMap));
    }

    /**
     * Retourne l'ensemble des nœuds du graphe c'est-à-dire l'ensemble des clés.
     * 
     * @return neighbors.keySet()
     */
    public Set<N> nodes() {
        return neighbors.keySet();
    }

    /**
     * Retourne l'ensemble des nœuds voisins du nœud donné c'est-à-dire la
     * valeur associée à une clé.
     * 
     * @param node
     * @return neighbors.get(node)
     * @throws IllegalArgumentException
     *             Lève l'exception IllegalArgumentException si le nœud donné ne
     *             fait pas partie du graphe
     */
    public Set<N> neighborsOf(N node) throws IllegalArgumentException {
        if (!(nodes().contains(node))) {
            throw new IllegalArgumentException();
        }
        return neighbors.get(node);
    }

    /**
     * Builder imbriqué statiquement dans la classe Graph bâtisseur de la classe
     * Graph
     * 
     * @author Mikael Morales Gonzalez (234747)
     * @author Charles Parzy Turlat (250628)
     */
    public final static class Builder<N> {

        private final Map<N, Set<N>> mySet = new HashMap<N, Set<N>>();

        /**
         * ajoute le nœud donné au graphe en cours de construction, s'il n'en
         * faisait pas déjà partie
         * 
         * @param n
         *            un noeux à ajouter
         */
        public void addNode(N n) {
            mySet.putIfAbsent(n, new HashSet<N>());
        }

        /**
         * ajoute le premier nœud à l'ensemble des voisins du second, et vice
         * versa
         * 
         * @param n1
         *            un noeux ajouté à l'ensemble des voisins de n2
         * @param n2
         *            un noeux ajouté à l'ensemble des voisins de n1
         * 
         * @throws IllegalArgumentException
         *             Lève l'exception IllegalArgumentException si l'un des
         *             nœuds n'appartient pas au graphe en cours de construction
         */
        public void addEdge(N n1, N n2) {
            if (mySet.containsKey(n1) && mySet.containsKey(n2)) {
                mySet.get(n1).add(n2);
                mySet.get(n2).add(n1);
            } else {
                throw new IllegalArgumentException();
            }
        }

        /**
         * construit le graphe composé des nœuds et arêtes ajoutés jusqu'à
         * présent au bâtisseur
         * 
         * @return Graph<N>(neighbors) un graph
         */
        public Graph<N> build() {
            return new Graph<N>(mySet);
        }
    }
}
