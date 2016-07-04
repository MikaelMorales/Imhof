package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Mikael Morales Gonzalez (234747)
 * 
 *         Interface fonctionnelle représentant un Modèle Numérique de Terrain
 *         (MNT).
 *
 */
public interface DigitalElevationModel extends AutoCloseable {
    /**
     * Retourne le vecteur normal à la Terre au point passé en paramètre.
     * 
     * @param p
     *            (PointGeo)
     * @return Vector3
     */
    public Vector3 normalAt(PointGeo p);
}
