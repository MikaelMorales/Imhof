package ch.epfl.imhof.painting;

/**
 * Représente une couleur décrite par ses trois composantes rouge, verte et
 * bleue. Chacune des ces composantes est un nombre réel compris entre 0 et 1.
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Morales Gonzalez Mikael (234747)
 */
public final class Color {
    private final double r, g, b;

    /**
     * Constructeur privée qui crée une couleur selon les trois composante
     * rouge, verte et bleue. Lance une exception si celles-ci ne sont pas
     * comprise entre 0 et 1.
     * 
     * @param r
     *            composante rouge
     * @param g
     *            composante green
     * @param b
     *            composante blue
     */
    private Color(double r, double g, double b) {
        if (!(0.0 <= r && r <= 1.0))
            throw new IllegalArgumentException("invalid red component: " + r);
        if (!(0.0 <= g && g <= 1.0))
            throw new IllegalArgumentException("invalid green component: " + g);
        if (!(0.0 <= b && b <= 1.0))
            throw new IllegalArgumentException("invalid blue component: " + b);
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * La couleur « rouge » (pur).
     */
    public final static Color RED = new Color(1, 0, 0);

    /**
     * La couleur « vert » (pur).
     */
    public final static Color GREEN = new Color(0, 1, 0);

    /**
     * La couleur « bleu » (pur).
     */
    public final static Color BLUE = new Color(0, 0, 1);

    /**
     * La couleur « noir ».
     */
    public final static Color BLACK = new Color(0, 0, 0);

    /**
     * La couleur « blanc ».
     */
    public final static Color WHITE = new Color(1, 1, 1);

    /**
     * @param valeur
     * @return Retourne la valeur grise dont les trois composante sont égales à
     *         la valeur donnée en argument.
     */
    public static Color gray(double valeur) {
        return new Color(valeur, valeur, valeur);
    }

    /**
     * 
     * @param r
     *            composante rouge
     * @param g
     *            composante green
     * @param b
     *            composante blue
     * @return Retourne la couleur en faisant appelle au constructeur à l'aide
     *         des 3 composantes données en argument.
     */
    public static Color rgb(double r, double g, double b) {
        return new Color(r, g, b);
    }

    /**
     * Retourne la couleur en faisant appelle au constructeur. La composante
     * rouge est comprise entre les bits 23 à 16 de l'entier donnée en argument,
     * la composante vert entre les bits 15 à 8 et la composante bleue entre les
     * bits 7 à 0.
     * 
     * @param couleur
     * @return
     */
    public static Color rgb(int couleur) {
        double b = (couleur & 0xFF) / 255d;
        double g = ((couleur >> 8) & 0xFF) / 255d;
        double r = ((couleur >> 16) & 0xFF) / 255d;
        return new Color(r, g, b);
    }

    /**
     * @return Getter qui retourne la composante g.
     */
    public double g() {
        return g;
    }

    /**
     * @return Getter qui retourne la composante r.
     */
    public double r() {
        return r;
    }

    /**
     * @return Getter qui retourne la composante b.
     */
    public double b() {
        return b;
    }

    /**
     * @param Color
     *            c
     * @return mutliplie deux couleurs en multipliant leurs composantes
     *         individuellement.
     */
    public Color multiply(Color c) {
        return new Color(r * c.r, g * c.g, b * c.b);
    }

    /**
     * @return Convertie une Color en couleur de l'API Java, c'est-à-dire une
     *         instance de la classe java.awt.Color.
     */
    public java.awt.Color convert() {
        return new java.awt.Color((float) r, (float) g, (float) b);
    }

}
