package ch.epfl.imhof.painting;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.SwissPainter;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Classe permettant de d'insérer une légende sur
 * la carte.
 *	@author Charles Parzy Turlat (250628)
 *	@author Morales Gonzalez Mikael (234747)
 */
public final class MapLegend {

	private final static int START_POLYLINE = 30;
	private final static int END_POLYLINE = START_POLYLINE + 50;
	private final static int HEIGHT_POLYGON = 30;
	private final static int ADJUSTMENT = (int)(HEIGHT_POLYGON/4d);
	private final static int LEGEND_WIDTH = 600;
	private final static int LEGEND_RESOLUTION = 600;
	private final static int TEXT_X_COORDINATE  = 90;
	
	/**
	 * Constructeur privé de la classe
	 */
	private MapLegend() {}

	/**
	 * Méthode retournant une nouvelle image sur laquelle est dessinée la légende.
	 * Elle écrit les noms des légendes.
	 * @param height (int) : la hauteur de l'image.
	 * @param backgroundColor (Color) : la couleur d'arrière fond de l'image.
	 * @return BufferedImage
	 */
	public static BufferedImage createLegend(int height,
			Color backgroundColor) {
		BufferedImage image = drawLegend(LEGEND_WIDTH, height, backgroundColor);
		Graphics2D g = image.createGraphics();
		
		int nbPixel = (int) (height / 8d);
		int delta = (int) (nbPixel / 4d) + ADJUSTMENT;
		
		List<String> s = Arrays.asList(" : Autoroutes",
				" : Routes principales", " : Routes secondaires",
				" : Voies ferrées", " : Forêts", " : Lacs et bassins",
				" : Bâtiments");
		g.setColor(Color.BLACK.convert());
		g.setFont(new Font(Font.SANS_SERIF, 50, 70));

		g.drawString("Légende", (int)(image.getWidth()/4d), 2*delta);
		
		Font f = new Font(Font.SANS_SERIF, 30, 45);
		g.setFont(f);
		
		
		for (int i = 0; i < 4; ++i) 
			g.drawString(s.get(i), TEXT_X_COORDINATE, (i + 1) * nbPixel + delta);

		for(int i = 4; i < 7; ++i )
			g.drawString(s.get(i), TEXT_X_COORDINATE, (int)((i+1) * nbPixel + delta + HEIGHT_POLYGON/2d));
		
		return image;
	}
	
	/**
	 * Méthode dessinant les icônes de la légende sur une nouvelle image.
	 * @param width (int) : la largeur de l'image.
	 * @param height (int) : la hauteur de l'image.
	 * @param backgroundColor (Color) : la couleur de l'image.
	 * @return BufferedImage
	 */
	private static BufferedImage drawLegend(int width, int height,
			Color backgroundColor) {
		Painter painter = SwissPainter.painter();
		Java2DCanvas c = new Java2DCanvas(new Point(0, height), new Point(
				width, 0), width, height, LEGEND_RESOLUTION, backgroundColor);
		Map myMap = initMapLegend(height);
		painter.drawMap(myMap, c);
		return c.image();
	}

	/**
	 * Méthode créant une map permettant 
	 * @param height (int) : la hauteur de l'image sur laquelle la map sera dessinée.
	 * @return Map
	 */
	private static Map initMapLegend(int height) {
		Map.Builder mB = new Map.Builder();

		int nbPixel = (int) (height / 8d);
		int delta = (int) (nbPixel / 4d);
		
		// Ajoute Autoroute à la map.
		mB.addPolyLine(createOpenPolyLine(new Point(START_POLYLINE, nbPixel
				+ delta), new Point(END_POLYLINE, nbPixel + delta),
				createMapAttributes("highway", "motorway")));

		// Ajoute Route principale à la map.
		mB.addPolyLine(createOpenPolyLine(new Point(START_POLYLINE, 2 * nbPixel
				+ delta), new Point(END_POLYLINE, 2 * nbPixel + delta),
				createMapAttributes("highway", "primary")));

		// Ajoute Route secondaire à la map.
		mB.addPolyLine(createOpenPolyLine(new Point(START_POLYLINE, 3 * nbPixel
				+ delta), new Point(END_POLYLINE, 3 * nbPixel + delta),
				createMapAttributes("highway", "secondary")));

		// Ajoute Voie ferrée à la map.
		mB.addPolyLine(createOpenPolyLine(new Point(START_POLYLINE, 4 * nbPixel
				+ delta), new Point(END_POLYLINE, 4 * nbPixel + delta),
				createMapAttributes("railway", "rail")));

		// Ajoute Foret à la map.
		mB.addPolygon(createPolygon(new Point(START_POLYLINE, 5 * nbPixel
				+ delta), new Point(END_POLYLINE, 5 * nbPixel + delta),
				createMapAttributes("natural", "wood")));

		// Ajoute lac et bassin à la map.
		mB.addPolygon(createPolygon(new Point(START_POLYLINE, 6 * nbPixel
				+ delta), new Point(END_POLYLINE, 6 * nbPixel + delta),
				createMapAttributes("natural", "water")));

		// Ajoute building à la map.
		mB.addPolygon(createPolygon(new Point(START_POLYLINE, 7 * nbPixel
				+ delta), new Point(END_POLYLINE, 7 * nbPixel + delta),
				createMapAttributes("building", "yes")));

		return mB.build();
	}

	/**
	 * Méthode créant une polyligne attribuée. 
	 * @param startPoint (Point) le point de départ.
	 * @param endPoint (Point) le point d'arrivée.
	 * @param m (Map) table associative des attributs de la polyligne.
	 * @return Attributed<PolyLine>
	 */
	private static Attributed<PolyLine> createOpenPolyLine(Point startPoint,
			Point endPoint, java.util.Map<String, String> m) {
		PolyLine.Builder p = new PolyLine.Builder();
		p.addPoint(startPoint);
		p.addPoint(endPoint);
		Attributes att = new Attributes(m);
		return new Attributed<PolyLine>(p.buildOpen(), att);
	}

	/**
	 * Méthode créant un polygon attribué.
	 * @param startPoint (Point) le point de départ
	 * @param endPoint (Point) le point d'arrivé.
	 * @param m (Map) la table associative des attrbuts du polygone.
	 * @return Attributed<Polygon>
	 */
	private static Attributed<Polygon> createPolygon(Point startPoint,
			Point endPoint, java.util.Map<String, String> m) {
		PolyLine.Builder c = new PolyLine.Builder();
		c.addPoint(startPoint);
		c.addPoint(endPoint);
		c.addPoint(new Point(endPoint.x(), endPoint.y() + HEIGHT_POLYGON));
		c.addPoint(new Point(startPoint.x(), startPoint.y() + HEIGHT_POLYGON));
		Attributes att = new Attributes(m);
		return new Attributed<Polygon>(new Polygon(c.buildClosed()), att);
	}

	/**
	 * Méthode créant une table associative d'attributs.
	 * @param key (String) la clé.
	 * @param value (String) la valeur.
	 * @return Map<String,String>
	 */
	private static java.util.Map<String, String> createMapAttributes(
			String key, String value) {
		java.util.Map<String, String> m = new HashMap<>();
		m.put(key, value);
		return m;
	}
}
