package ch.epfl.imhof.painting;

/**
 * Classe immuable représentant le style des polylignes.
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Mikael Morales Gonzalez (234747)
 *
 */
public final class LineStyle {

    private final Color color;
    private final float width;
    private final float[] dashingPattern;
    private final LineCap lineCap;
    private final LineJoin lineJoin;

    /**
     * Constructeur publique de la classe LineStyle prenant 5 attributs en
     * paramètre.
     * 
     * @param color
     *            (Color) : la couleur du trait.
     * @param width
     *            (float) : la largeur du trait.
     * @param dashingPattern
     *            (float[]) : alternance des sections opaques et transparentes.
     * @param lineCap
     *            (LineCap) : la terminaison des lignes.
     * @param lineJoin
     *            (LineJoin) : la joignure des segments.
     */
    public LineStyle(float width, Color color, LineCap lineCap,
            LineJoin lineJoin, float... dashingPattern) {
        if (width < 0)
            throw new IllegalArgumentException();
        for (float f : dashingPattern) {
            if (f <= 0.0)
                throw new IllegalArgumentException();
        }
        this.color = color;
        this.width = width;
        this.dashingPattern = dashingPattern.clone();
        this.lineCap = lineCap;
        this.lineJoin = lineJoin;
    }

    /**
     * Constructeur secondaire qui ne prend en arguments que la largeur et la
     * couleur du trait, et qui appelle le constructeur principal en lui passant
     * des valeurs par défaut pour les autres paramètres, qui sont : butt pour
     * la terminaison des lignes, miter pour la jointure des segments, la
     * séquence vide pour l'alternance des segments opaques et transparents.
     * 
     * @param width
     *            la largeur du trait
     * @param color
     *            la couleur du trait
     */
    public LineStyle(float width, Color color) {
        this(width, color, LineCap.BUTT, LineJoin.MITER);
    }

    /**
     * getter retournant la couleur du trait
     * 
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     * getter retournant la largeur du trait
     * 
     * @return width
     */
    public float getWidth() {
        return width;
    }

    /**
     * Getter retournant l'alternance des sections opaques et transparentes
     * 
     * @return dashingPattern
     */
    public float[] getAlternation() {
        return dashingPattern;
    }

    /**
     * Getter retournant la terminaison des lignes
     * 
     * @return lineCap
     */
    public LineCap getTermination() {
        return lineCap;
    }

    /**
     * Getter retournant la jointure des segments
     * 
     * @return lineJoin
     */
    public LineJoin getJoint() {
        return lineJoin;
    }

    /**
     * Méthode permettant d'obtenir un style identique à celui auquel on
     * l'applique, sauf pour le paramètre width
     * 
     * @param aWidth
     * @return LineStyle
     */
    public LineStyle withWidth(float aWidth) {
        return new LineStyle(aWidth, this.color, this.lineCap, this.lineJoin,
                this.dashingPattern);
    }

    /**
     * méthode permettant d'obtenir un style identique à celui auquel on
     * l'applique, sauf pour le paramètre color
     * 
     * @param aColor
     * @return LineStyle
     */
    public LineStyle withColor(Color aColor) {
        return new LineStyle(this.width, aColor, this.lineCap, this.lineJoin,
                this.dashingPattern);
    }

    /**
     * méthode permettant d'obtenir un style identique à celui auquel on
     * l'applique, sauf pour le paramètre lineCap
     * 
     * @param lineCap
     * @return LineStyle
     */
    public LineStyle withLineCap(LineCap lineCap) {
        return new LineStyle(this.width, this.color, lineCap, this.lineJoin,
                this.dashingPattern);
    }

    /**
     * méthode permettant d'obtenir un style identique à celui auquel on
     * l'applique, sauf pour le paramètre lineJoin
     * 
     * @param lineJoin
     * @return LineStyle
     */
    public LineStyle withLineJoin(LineJoin lineJoin) {
        return new LineStyle(this.width, this.color, this.lineCap, lineJoin,
                this.dashingPattern);
    }

    /**
     * méthode permettant d'obtenir un style identique à celui auquel on
     * l'applique, sauf pour le paramètre dashingPattern
     * 
     * @param dashingPattern
     * @return LineStyle
     */
    public LineStyle withDashingPattern(float[] dashingPattern) {
        return new LineStyle(this.width, this.color, this.lineCap,
                this.lineJoin, dashingPattern);
    }

    /**
     * Enumération représentant les terminaisons des lignes.
     * 
     * @author Charles Parzy Turlat (250628)
     * @author Mikael Morales Gonzalez (234747)
     *
     */
    public enum LineCap {
        BUTT, ROUND, SQUARE
    }

    /**
     * Enumération représentant les jointures des segments.
     * 
     * @author Charles Parzy Turlat (250628)
     * @author Mikael Morales Gonzalez (234747)
     *
     */
    public enum LineJoin {
        BEVEL, MITER, ROUND
    }
}
