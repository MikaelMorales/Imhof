package ch.epfl.imhof;

/**
 * 
 */
/**
 * Un point à la surface de la Terre, en coordonnées sphériques.
 * 
 * @author Charles Parzy Turlat(250628)
 * @author Mikael Morales Gonzalez (234747)
 */
public final class PointGeo {
    private final double lambda;
    private final double phi;

    /**
     * Construit un point avec la latitude et la longitude données.
     *
     * @param longitude
     *            la longitude du point, en radians
     * @param latitude
     *            la latitude du point, en radians
     * @throws IllegalArgumentException
     *             si la longitude est invalide, c-à-d hors de l'intervalle [-π;
     *             π]
     * @throws IllegalArgumentException
     *             si la latitude est invalide, c-à-d hors de l'intervalle
     *             [-π/2; π/2]
     */
    public PointGeo(double longitude, double latitude) {
        if (longitude < -Math.PI || longitude > Math.PI) {
            throw new IllegalArgumentException(
                    "La longitude doit être entre [-Pi,Pi]");
        }
        if (latitude < (-Math.PI) / 2.0 || latitude > Math.PI / 2.0) {
            throw new IllegalArgumentException(
                    "La latitude doit être entre [-Pi/2,Pi/2]");
        }
        lambda = longitude;
        phi = latitude;
    }

    /**
     * Retourne la valeur de la longitude (lambda)
     * 
     * @return longitude
     */
    public double longitude() {
        return lambda;
    }

    /**
     * Retourne la valeur de la latitude (Phi)
     * 
     * @return latitude
     */
    public double latitude() {
        return phi;
    }
}
