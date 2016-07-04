package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.projection.Projection;

/**
 * Représente un convertisseur de données OSM en carte.
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Morales Gonzalez Mikael (234747)
 */
public final class OSMToGeoTransformer {
    private Projection projection;

    /**
     * Construit un convertisseur OSM en géométrie qui utilise la projection
     * donnée.
     * 
     * @param projection
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }

    /**
     * Retourne la liste de chemins ayant le role passé en paramètre(inner ou
     * outer)
     * 
     * @param relation
     *            OSMRelation
     * @param role
     *            String indiquant le role attendu
     * @return liste de OSMWay ayant le même role que celui passé en paramètre
     */
    private List<OSMWay> WaysWithRole(OSMRelation relation, String role) {
        List<OSMWay> chemins = new ArrayList<OSMWay>();
        if (relation.hasAttribute("type")
                && "multipolygon".equals(relation.attributeValue("type"))) {
            for (OSMRelation.Member m : relation.members()) {
                if (m.role().equals(role)
                        && OSMRelation.Member.Type.WAY.equals(m.type())) {
                    chemins.add(((OSMWay) m.member()));
                }
            }
        }
        return chemins;
    }

    /**
     * Construit le graph à partir de la liste de chemins et des noeuds
     * contenus. Remplit le graph avec les noeuds des chemins et associe à ces
     * noeuds leurs voisins.
     * 
     * @param une
     *            liste de OSMWay
     * @return Graph<OSMNode>
     */
    private Graph<OSMNode> constructGraph(List<OSMWay> list) {
        Graph.Builder<OSMNode> graph = new Graph.Builder<>();
        // Initialise le graph avec tous les noeuds de list. 
        for (OSMWay ways : list) {
            for (OSMNode n : ways.nodes()) {
                graph.addNode(n);
            }
        }
        // Ajoute les voisins aux noeuds déjà présent dans le graph.
        for (OSMWay s : list) {
            Iterator<OSMNode> iterateur = s.nodes().iterator();
            OSMNode node1 = iterateur.next();
            while (iterateur.hasNext()) {
                OSMNode node2 = iterateur.next();
                graph.addEdge(node1, node2);
                node1 = node2;
            }
        }
        return graph.build();
    }

    /**
     * Construit les anneaux (ClosedPolyLine) à partir des noeuds et de leurs
     * voisins contenus dans un graphe non orienté.
     * 
     * @param relation
     *            (OSMRelation)
     * @param role
     *            (String)
     * @return Une liste de polylignes fermées formant les anneaux
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        List<OSMWay> chemins = WaysWithRole(relation, role);
        Graph<OSMNode> graph = constructGraph(chemins);
        List<ClosedPolyLine> rings = new ArrayList<>();
        Set<OSMNode> noeudsVisite = new HashSet<OSMNode>();

        for (OSMNode noeud : graph.nodes()) {
            if (graph.neighborsOf(noeud).size() != 2)
                return Collections.emptyList();
        }

        for (OSMNode noeud : graph.nodes()) {
            if (!noeudsVisite.contains(noeud)) {
                PolyLine.Builder polyline = new PolyLine.Builder();
                polyline.addPoint(projection.project(noeud.position()));
                noeudsVisite.add(noeud);
                OSMNode currentNode = noeud;
                while (true) {
                    List<OSMNode> voisins = new ArrayList<>(
                            graph.neighborsOf(currentNode));
                    if (!noeudsVisite.contains(voisins.get(0))) {
                        currentNode = voisins.get(0);
                        polyline.addPoint(projection.project(currentNode
                                .position()));
                        noeudsVisite.add(currentNode);
                    } else if (!noeudsVisite.contains(voisins.get(1))) {
                        currentNode = voisins.get(1);
                        polyline.addPoint(projection.project(currentNode
                                .position()));
                        noeudsVisite.add(currentNode);
                    } else {
                        break;
                    }
                }
                rings.add(polyline.buildClosed());
            }
        }
        return rings;

    }

    /**
     * Construit les polygones a partir des "anneaux" construits a l'aide de
     * ringsForRole.
     * 
     * @param relation
     * @param attributes
     * @return une liste de polygones attribués
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,
            Attributes attributes) {
        List<ClosedPolyLine> inner = ringsForRole(relation, "inner");
        List<ClosedPolyLine> outer = ringsForRole(relation, "outer");
        List<Attributed<Polygon>> polygon = new ArrayList<>();
        Set<ClosedPolyLine> trouesVisite = new HashSet<ClosedPolyLine>();
        List<ClosedPolyLine> trous = new ArrayList<>();

        // Cr�ation du comparateur, qui utilise les aires pour
        // classer les ClosedPolyLines.
        Comparator<ClosedPolyLine> comparateur = (o1, o2) -> Double.compare(
                o1.area(), o2.area());
        // Tri les deux List<ClosedPolyLine>.
        Collections.sort(inner, comparateur);
        Collections.sort(outer, comparateur);

        // Cr�e les polygones (avec ou sans trous) en fonctions
        // des anneaux inner et outer.
        if (!outer.isEmpty()) {
            for (ClosedPolyLine shell : outer) {
                trous.clear();
                for (ClosedPolyLine hole : inner) {
                    boolean contient = false;
                    for (Point p : hole.points()) {
                        if (shell.containsPoint(p)
                                && !trouesVisite.contains(hole)) {
                            contient = true;
                            trouesVisite.add(hole);
                        }
                    }
                    if (contient) {
                        trous.add(hole);
                    }
                }
                if (!trous.isEmpty()) {
                    polygon.add(new Attributed<Polygon>(new Polygon(shell,
                            trous), attributes));
                } else if (trous.isEmpty()) {
                    polygon.add(new Attributed<Polygon>(new Polygon(shell),
                            attributes));
                }
            }
        }
        return polygon;
    }

    /**
     * Verifie si OSMWay décrit une surface, en controlant si un de ses
     * attributs est dans la liste ou s'il contient l'attribut area.
     * 
     * @param way
     * @return boolean true : OSMWay est fermé et décrit une surface false :
     *         OSMWay ne décrit pas une surface
     */
    private boolean OSMWayIsAnArea(OSMWay way) {
        String[] tab = {"aeroway", "amenity", "building", "harbour",
                "historic", "landuse", "leisure", "man_made", "military",
                "natural", "office", "place", "power", "public_transport",
                "shop", "sport", "tourism", "water", "waterway", "wetland"};
        for (String s : tab) {
            if (way.hasAttribute(s))
                return true;
        }
        if (way.hasAttribute("area")) {
            String att = way.attributeValue("area");
            if (att.equals("yes") || att.equals("1") || att.equals("true"))
                return true;
        }
        return false;
    }

    /**
     * Retourne une closedPolyLine formée à partir des points projetés en
     * coordonnées cartésiennes composant les noeuds du chemin passé en
     * paramètre.
     * 
     * @param OSMWay way
     * @return ClosedPolyLine
     */
    private ClosedPolyLine ringsForWays(OSMWay way) {
        PolyLine.Builder polyline = new PolyLine.Builder();
        List<OSMNode> noeuds = way.nonRepeatingNodes();
        for (OSMNode n : noeuds) {
            polyline.addPoint(projection.project(n.position()));
        }

        return polyline.buildClosed();
    }

    /**
     * Retourne une Polyline.Builder dont ces points sont en 
     * coordonnées cartésiennes(pointGeo projetés).
     * 
     * @param way
     * @return List<Point> points
     */
    private PolyLine.Builder wayToPolyLine(OSMWay way) {
        PolyLine.Builder polyline = new PolyLine.Builder();
        List<OSMNode> noeuds = way.nonRepeatingNodes();
        for (OSMNode n : noeuds) {
           polyline.addPoint(projection.project(n.position()));
        }
        return polyline;
    }

    /**
     * Convertit une « carte » OSM en une carte g�om�trique projet�e
     * 
     * @param Une carte OpenStreetMap
     * @return Une carte projetée (Map)
     */
    public Map transform(OSMMap map) {
        List<OSMWay> chemins = map.ways();
        List<OSMRelation> relations = map.relations();
        List<Attributed<Polygon>> listPolygon = new ArrayList<Attributed<Polygon>>();
        List<Attributed<PolyLine>> listPolyLine = new ArrayList<Attributed<PolyLine>>();

        for (OSMWay c : chemins) {
            if (c.isClosed() && OSMWayIsAnArea(c) && polygonIsNotEmpty(c.attributes())) {
                listPolygon.add(new Attributed<Polygon>(new Polygon(ringsForWays(c)), filterPolygon(c.attributes())));
            } if (c.isClosed() && !OSMWayIsAnArea(c) && polyLineIsNotEmpty(c.attributes())) {
                	listPolyLine.add(new Attributed<PolyLine>((wayToPolyLine(c).buildClosed()), filterPolyLine(c.attributes())));
            } if (!c.isClosed() && polyLineIsNotEmpty(c.attributes())) {
                	listPolyLine.add(new Attributed<PolyLine>((wayToPolyLine(c).buildOpen()), filterPolyLine(c.attributes())));
                }            
        }

        for (OSMRelation r : relations) {
        	if(polygonIsNotEmpty(r.attributes()))
        	    listPolygon.addAll(assemblePolygon(r, filterPolygon(r.attributes())));
        }
        return new Map(listPolyLine, listPolygon);
    }

    /**
     * Filtre les attributs des polygones afin de garder ceux les plus utiles au
     * dessin des cartes.
     * 
     * @param list
     *            une liste de polygones attribués dont les attributs n'ont pas
     *            été filtrés.
     * @return une liste de polygones attribués dont les attribus ont été
     *         filtrés.
     */
    private Attributes filterPolygon(Attributes attributes) {
        Set<String> keysToKeep = new HashSet<String>(Arrays.asList("building",
                "landuse", "layer", "leisure", "natural", "waterway"));
        Attributes att = attributes.keepOnlyKeys(keysToKeep);
        return att;
    }

    /**
     * Filtre les attributs des PolyLine afin de garder ceux utiles au dessin
     * des cartes.
     * 
     * @param list
     *            de polylignes attribuées dont les attributs n'ont pas été
     *            filtrés.
     * @return une liste de polylignes attribuées dont les attributs ont été
     *         filtrés.
     */
    private Attributes filterPolyLine(Attributes attributes) {
        Set<String> keysToKeep = new HashSet<String>(
                Arrays.asList("bridge", "highway", "layer", "man_made",
                        "railway", "tunnel", "waterway"));

        Attributes att = attributes.keepOnlyKeys(keysToKeep);
        return att;
    }
    
    /**
     * @param a (Attributes)
     * @return vérifie si lorsqu'on filtre les attributs, ceux-ci ne seront
     * pas vide.
     */
    private boolean polygonIsNotEmpty(Attributes a) {
    	 String[] keysToKeep = {"building",
               "landuse", "layer", "leisure", "natural", "waterway"};
    	 for(String keys : keysToKeep) {
    		 if(a.contains(keys))
    		     return true;
    	 }
    	 return false;
    }
    
    /**
     * @param a (Attributes)
     * @return vérifie si lorsqu'on filtre les attributs, ceux-ci ne seront
     * pas vides
     */
    private boolean polyLineIsNotEmpty(Attributes a) {
    	String[] keysToKeep = {"bridge", "highway", "layer", "man_made",
              "railway", "tunnel", "waterway"};
    	for (String keys : keysToKeep) {
    		if(a.contains(keys))
    		    return true;
    	}
    	return false;
    }
    
    
 }
