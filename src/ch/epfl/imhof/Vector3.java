package ch.epfl.imhof;

/**
 * Classe immuable représentant un vecteur tridimensionnel,
 *	@author Charles Parzy Turlat (250628)
 *	@author Morales Gonzalez Mikael (234747)
 */
public final class Vector3 {
    
    private final double x;
    private final double y;
    private final double z;
    
    /**
     * Constructeur d'un vecteur à 3 dimensions (x,y,z)
     * @param x double
     * @param y double
     * @param z double
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * @return la norme d'un vecteur.
     */
    public double norm() {
        return Math.sqrt(x*x+y*y+z*z);
    }
    
    /**
     * @return une version noramlisée du vecteur.
     */
    public Vector3 normalized() {
        double norme = norm();
        return new Vector3(x/norme, y/norme, z/norme);
    }
    
    /**
     * @param vect (Vector3)
     * @return retourne le produit scalaire entre les deux vecteurs.
     */
    public double scalarProduct(Vector3 vect) {
        return x*vect.x + y*vect.y + z*vect.z;
    }
}
