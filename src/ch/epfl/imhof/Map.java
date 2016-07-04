package ch.epfl.imhof;

import ch.epfl.imhof.geometry.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
/**
 * 
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 * 
 * Map publique, finale et immuable
 * représente une carte projetée,
 * composée d'entités géométriques attribuées
 * 
 */
public final class Map {
    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;
    
    /**
     * constructeur public 
     * construit une carte à partir des listes
     * de polylignes et polygones attribués donnés
     * 
     * @param polyLines une liste de polylignes attribuées
     * @param polygons une liste de polygon attribués
     */
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections.unmodifiableList(new ArrayList<Attributed<PolyLine>>(polyLines));
        this.polygons = Collections.unmodifiableList(new ArrayList<Attributed<Polygon>>(polygons));
    }
  
    /**
     * Getter qui retourne la liste des polylignes attribuées de la carte.
     *
     * @return polyLines 
     */
    public List<Attributed<PolyLine>> polyLines() {
        return polyLines;
    }
    
    /**
     * Getter qui retourne la liste des polygones attribués de la carte.
     * 
     * @return polygons
     */
    public List<Attributed<Polygon>> polygons() {
        return polygons;
    }
    
    /**
     * 
     * @author Mikael Morales Gonzalez (234747)
     * @author Charles Parzy Turlat (250628)
     * 
     * Bâtisseur de la classe Map, imbriqué statiquement dans la classe Map
     * Permet de construire une Map en plusieurs étapes.
     */
    public static final class Builder {
        private final List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        private final List<Attributed<Polygon>> polygons = new ArrayList<>();
        
        /**
         * Ajoute une polyligne attribuée à la carte en cours de construction.
         * @param newPolyLine (une polyligne attribuée)
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
           polyLines.add(newPolyLine);
        }
        
        /**
         * Ajoute un polygone attribué à la carte en cours de construction.
         * 
         * @param newPolygon 
         *              un polygon attribué
         */
        public void addPolygon(Attributed<Polygon> newPolygon) {
            polygons.add(newPolygon);
        }
        
        /**
         * Construit une carte avec les polylignes 
         * et polygones ajoutés jusqu'à présent au bâtisseur.
         *
         * @return new Map(polyLines, polygons) 
         *                      une nouvelle carte
         */
        public Map build() {
            return new Map(polyLines, polygons);
        }
    }
}
