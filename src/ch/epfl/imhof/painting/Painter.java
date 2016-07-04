package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;
/**
 * Interface fonctionnelle Painter représentant un peintre. 
 * @author Charles Parzy Turlat (250628)
 * @author Morales Gonzalez Mikael (234747)
 *
 */
public interface Painter {

	/**
	 * Méthode abstraite.
	 * Dessine la carte sur la toile.
	 * @param map (Map) une carte
	 * @param canvas (Canvas) une toile
	 */
    public void drawMap(Map map, Canvas canvas);

    /**
     * Méthode statique offrant un peintre de base.
     * Retourne un peintre dessinant l'intérieur de tous les polygones de la carte qu'il reéoit en argument
     * avec cette couleur passée en paramètre.
     * @param color (Color) une couleur 
     * @return Painter
     */
    public static Painter polygon(Color color) {
        return (m, t) -> {
            for (Attributed<Polygon> p : m.polygons()) 
                t.drawPolygon(p.value(), color);
        };
    }
    
    /**
     * Méthode statique retournant un peintre de base.
     * Retourne un peintre dessinant toutes les lignes de la carte
     * qu'on lui fournit avec le style donné.
     * @param style (LineStyle)
     * @return Painter
     */
    public static Painter line(LineStyle style) {
        return (m,t) -> {
            for (Attributed<PolyLine> p : m.polyLines()) 
                t.drawPolyLine(p.value(), style);
            for (Attributed<Polygon> polygon : m.polygons()) {
                t.drawPolyLine(polygon.value().shell(), style);
                for (ClosedPolyLine hole : polygon.value().holes()) 
                    t.drawPolyLine(hole, style);
            }
        };
    }

    /**
     * Méthode statique retournant un peintre de base.
     * Retourne un peintre dessinant toutes les lignes de la carte
     * qu'on lui fournit avec le style correspondant
     * @param color (Color) : la couleur du trait.
     * @param width (float) : la largeur du trait.
     * @param dashingPattern (float[]) : alternance des sections opaques et transparentes.
     * @param lineCap (LineCap) : la terminaison des lignes.
     * @param lineJoin (LineJoin) : la joignure des segments.
     * @return Painter
     */
    public static Painter line(float width, Color color, LineCap lineCap, LineJoin lineJoin, float... dashingPattern) {
        return line(new LineStyle(width, color, lineCap, lineJoin, dashingPattern));
    }
    
     /**
      * Méthode statique retournant un peintre de base.
      * Retourne un peintre dessinant toutes les lignes de la carte
      * qu'on lui fournit avec le style correspondant construit à partir des arguments.
      * Utilise le constructeur de LineStyle ne prenant que width et color
      * en argument pour construire le style de ligne.
      * @param width (float) : la largeur du trait.
      * @param color (Color) : la couleur du trait.
      * @return Painter
      */
    public static Painter line(float width, Color color) {
        return line(new LineStyle(width, color));
    }
    
    /**
     * Méthode statique retournant un peintre de base.
     * Dessine les pourtours de l'enveloppe et des
     * trous de tous les polygones de la carte qu'on lui fournit
     * avec le style donné.
     * @param style (LineStyle)
     * @return Painter
     */
    public static Painter outline(LineStyle style) {
        return (m, t) -> {
            for (Attributed<Polygon> polygon : m.polygons()) {
                t.drawPolyLine(polygon.value().shell(), style);
                for (ClosedPolyLine hole : polygon.value().holes()) 
                    t.drawPolyLine(hole, style);
            }
        };
    }
    
    /**
     * Méthode statique retournant un peintre de base.
     * Dessine les pourtours de l'enveloppe et des
     * trous de tous les polygones de la carte qu'on lui fournit.
     * @param color (Color) : la couleur du trait.
     * @param width (float) : la largeur du trait.
     * @param dashingPattern (float[]) : alternance des sections opaques et transparentes.
     * @param lineCap (LineCap) : la terminaison des lignes.
     * @param lineJoin (LineJoin) : la joignure des segments.
     * @return Painter
     */
    public static Painter outline(float width,Color color, LineCap lineCap, LineJoin lineJoin,float... dashingPattern) {
        return outline(new LineStyle(width, color, lineCap, lineJoin,dashingPattern));
    }
    
    /**
     * Méthode statique retournant un peintre de base.
     * Prend en argument que la largeur du trait et la couleur et utilise les valeurs par défaut données 
     * dans le constructeur de LineStyle pour les autres paramètres du style de ligne.
     * Dessine les pourtours de l'enveloppe et des trous de tous les polygones de la carte qu'on lui fournit.
     * @param width (float) : la largeur du trait.
     * @param color (Color) : la couleur du trait.
     * @return Painter
     */
    public static Painter outline(float width, Color color) {
        return outline(new LineStyle(width, color));
    }
    
    /**
     * Méthodes par défault permettant d'obtenir des peintres 
     * dérivés à partir de peintres existants.
     * Retourne un peintre se comportant comme celui auquel on l'applique,
     * si ce n'est qu'il ne considère que les éléments de la carte satisfaisant le prédicat.
     * @param e (Predicate<Attributed<?>>)
     * @return Painter
     */
    public default Painter when(Predicate<Attributed<?>> e) {
        return (m,t) -> { 
           Map.Builder map = new Map.Builder();
           for(Attributed<PolyLine> p : m.polyLines()) {
               if(e.test(p))
                   map.addPolyLine(p);
           }
           for(Attributed<Polygon> p : m.polygons()) {
               if(e.test(p))
                   map.addPolygon(p);
           }
            drawMap(map.build(), t);
        };
    }
    
    /**
     * Prend en argument un autre peintre et retournant un peintre dessinant d'abord la
     * carte produite par ce second peintre puis, par dessus, la carte produite par le premier peintre.
     * @param painter (Painter)
     * @return Painter
     */
    public default Painter above(Painter painter) {
        return (m,t) -> {
            painter.drawMap(m, t);
            drawMap(m, t);
        };
    }
    
    /**
     * Un peintre utilisant l'attribut layer attaché aux entités de la carte pour la dessiner par couches,
     * c-à-d en dessinant d'abord tous les entités de la couche -5, puis celle de la couche -4, et ainsi de
     * suite jusqu'à la couche +5.
     * @return Painter
     */
    public default Painter layered() {
        return (m,t) -> {
            Painter p = when(Filters.onLayer(-5));           
            for(int i=-4; i<= 5; i++) {
               p = when(Filters.onLayer(i)).above(p);
            }
        };
    }
}
