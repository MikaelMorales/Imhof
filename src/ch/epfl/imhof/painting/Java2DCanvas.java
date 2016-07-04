package ch.epfl.imhof.painting;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Mise en oeuvre concrète de l'interface Canvas. Permet de dessiner des
 * polygones et des polylignes dans une image discrète à l'aide d'une partie de
 * la bibliothèque Java2D.
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Morales Gonzalez Mikael (234747)
 */
public class Java2DCanvas implements Canvas {

    private final Function<Point, Point> coordinateChange;
    private final BufferedImage image;
    private final Graphics2D ctx;

    /**
     * Constructeur de la classe Java2DCanvas. On initialise l'image discrète,
     * le calcul du changement de repère, ainsi que l'interface graphique.
     * 
     * @param pbl
     *            Point coins bas-gauche
     * @param ptr
     *            Point coins haut-droite
     * @param width
     *            largeur de l'image de la toile en pixels
     * @param height
     *            hauteur de l'image de la toile en pixels
     * @param resolution
     *            de l'image de la toile, en points par pouce
     * @param backgroundColor
     */
    public Java2DCanvas(Point pbl, Point ptr, int width, int height,
            double resolution, Color backgroundColor) {
        if (width < 0 || height < 0)
            throw new IllegalArgumentException(
                    "Height and width must be bigger than zero !");
        double dilatation = resolution / 72d;
        // Création de l'image discrète
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Changement de repère en tenant compte de la dilatation
        coordinateChange = Point.alignedCoordinateChange(pbl, new Point(0.0,
                height / dilatation), ptr, new Point(width / dilatation, 0.0));
        // On définit l'interface graphique
        ctx = image.createGraphics();
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        ctx.setColor(backgroundColor.convert());
        ctx.fillRect(0, 0, width, height);
        ctx.scale(dilatation, dilatation);
    }

    /**
     * @return l'image discrète.
     */
    public BufferedImage image() {
        return image;
    }

    /**
     * Permet de dessiner sur la toile une polyligne donnée avec un style de
     * ligne donné.
     * 
     * @param poly
     *            (Polyline)
     * @param style
     *            (LineStyle)
     */
    @Override
    public void drawPolyLine(PolyLine poly, LineStyle style) {
        // Définit le type de trait ainsi que sa couleur
        BasicStroke stroke = setStroke(style);
        ctx.setStroke(stroke);
        ctx.setColor(style.getColor().convert());

        // Dessine la polyline
        Path2D path = new Path2D.Double();
        Point firstPoint = coordinateChange.apply(poly.firstPoint());
        path.moveTo(firstPoint.x(), firstPoint.y());
        for (Point p : poly.points()) {
            if (p != poly.firstPoint()) {
                Point newPoint = coordinateChange.apply(p);
                path.lineTo(newPoint.x(), newPoint.y());
            }
        }
        if (poly.isClosed())
            path.closePath();
        ctx.draw(path);

    }

    /**
     * Permet de dessiner sur la toile un polygone donné avec une couleur
     * donnée.
     * 
     * @param poly
     *            (Polygon)
     * @param color
     *            (Color)
     */
    @Override
    public void drawPolygon(Polygon poly, Color color) {
        ctx.setColor(color.convert());

        // Transforme enveloppe en chemin et le transforme en surface
        Path2D path = new Path2D.Double();
        Point firstPoint = coordinateChange.apply(poly.shell().firstPoint());
        path.moveTo(firstPoint.x(), firstPoint.y());
        for (Point p : poly.shell().points()) {
            if (p != poly.shell().firstPoint()) {
                Point newPoint = coordinateChange.apply(p);
                path.lineTo(newPoint.x(), newPoint.y());
            }
        }
        path.closePath();
        Area area = new Area(path);
        List<Area> holes = holesToArea(poly);
        // Soustrait les troues à la surface
        for (Area e : holes) {
            area.subtract(e);
        }
        ctx.fill(area);

    }

    /**
     * Transforme chaque troues du Polygon donné en surface (Area), et retourne
     * ces surfaces sous forme de liste.
     * 
     * @param poly
     *            (Polygon)
     * @return La liste de troues du polygone transformés surface.
     */
    private List<Area> holesToArea(Polygon poly) {
        List<Area> listHoles = new ArrayList<Area>();
        for (ClosedPolyLine c : poly.holes()) {
            Path2D path = new Path2D.Double();
            Point firstPoint = coordinateChange.apply(c.firstPoint());
            path.moveTo(firstPoint.x(), firstPoint.y());
            for (Point p : c.points()) {
                if (p != c.firstPoint()) {
                    Point newPoint = coordinateChange.apply(p);
                    path.lineTo(newPoint.x(), newPoint.y());
                }
            }
            path.closePath();
            Area hole = new Area(path);
            listHoles.add(hole);
        }
        return listHoles;
    }

    /**
     * Définit le style de trait utilisé dans drawPolyLine, en vérifiant si
     * l'alternance des sections opaques et transparentes du style donnée est
     * vide ou pas.
     * 
     * @param style
     *            (LineStyle)
     * @return trait (BasicStroke)
     */
    private BasicStroke setStroke(LineStyle style) {
        BasicStroke stroke;
        if (style.getAlternation().length == 0) {
            stroke = new BasicStroke(style.getWidth(), style.getTermination()
                    .ordinal(), style.getJoint().ordinal(), 10.0f, null, 0f);

        } else {
            stroke = new BasicStroke(style.getWidth(), style.getTermination()
                    .ordinal(), style.getJoint().ordinal(), 10.0f,
                    style.getAlternation(), 0f);

        }
        return stroke;
    }
}