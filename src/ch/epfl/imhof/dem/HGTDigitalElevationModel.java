package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.regex.Pattern;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Mikael Morales Gonzalez (234747)
 * 
 *         Classe représentant un Modèle Numérique de Terrain(MNT), implémentant
 *         DigitalElevationModel.
 *
 */
public class HGTDigitalElevationModel implements DigitalElevationModel {
    private ShortBuffer buffer;
    // On stocke in pour pouvoir le fermer dans la méthode close().
    private final FileInputStream in;
    private final double delta;
    private final int lineSize;
    private final String name;

    /**
     * Constructeur de la classe.
     * 
     * @param file
     *            (File) désigne le fichier HGT contenant le MNT.
     * @throws IOException
     */
    public HGTDigitalElevationModel(File file) throws IOException {
        double sqrtFile = (int) Math.sqrt(file.length() / 2d);
        name = file.getName();
        if ((2 * sqrtFile * sqrtFile) != file.length() || !nameIsOK())
            throw new IllegalArgumentException();
        in = new FileInputStream(file);
        buffer = in.getChannel().map(MapMode.READ_ONLY, 0, file.length())
                .asShortBuffer();
        lineSize = (int) (Math.sqrt(file.length() / 2d));
        delta = Math.toRadians(1 / (double) lineSize);

    }

    /**
     * Méthode retournant la latitude en degrés du point sud-ouest.
     * 
     * @return double lat.
     */
    private double lat() {
        double lat = Double.parseDouble(name.substring(1, 3));
        if (name.charAt(0) == 'S')
            lat *= -1;
        return lat;
    }

    /**
     * Méthode retournant la longitude en degrés du point sud-ouest.
     * 
     * @return double lon.
     */
    private double lon() {
        double lon = Double.parseDouble(name.substring(4, 7));
        if (name.charAt(3) == 'W')
            lon *= -1;
        return lon;
    }

    /**
     * Méthode qui vérifie que le nom du fichier HGT correspont à la bonne
     * structure.
     * 
     * @return boolean
     */
    private boolean nameIsOK() {
        return name.length() == 11
                && Pattern.matches("^[SN]\\d{2}[EW]\\d{3}\\.hgt$", name);
    }

    /**
     * Méthode fermant de flow d'entré.
     */
    @Override
    public void close() throws IOException {
        in.close();
        buffer = null;
    }

    /**
     * Méthode calculant le vecteur normal de la Terre au point passé en
     * paramètre.
     * 
     * @param p
     *            (PointGeo) point sur la Terre auquel on calcule le vecteur
     *            normal.
     * @return (Vector3) : le vecteur normal.
     */
    @Override
    public Vector3 normalAt(PointGeo p) {
        if (check(p))
            throw new IllegalArgumentException();

        double lat = Math.toRadians(lat());
        double lon = Math.toRadians(lon());

        int i = (int) ((p.longitude() - lon) / delta);
        int j = (int) ((p.latitude() - lat) / delta);
        double s = Earth.RADIUS * delta;
        double deltaA = buffer.get(getIJ(i + 1, j)) - buffer.get(getIJ(i, j));
        double deltaB = buffer.get(getIJ(i, j + 1)) - buffer.get(getIJ(i, j));
        double deltaC = buffer.get(getIJ(i, j + 1))
                - buffer.get(getIJ(i + 1, j + 1));
        double deltaD = buffer.get(getIJ(i + 1, j))
                - buffer.get(getIJ(i + 1, j + 1));
        double x = 1 / 2d * s * (deltaC - deltaA);
        double y = 1 / 2d * s * (deltaD - deltaB);
        double z = s * s;
        return new Vector3(x, y, z);
    }

    /**
     * Méthode calculant la position de l'altitude d'un point situé aux
     * coordonnée i et j dans le fichier HGT sur le repère dont l'origine est le
     * point sud-ouest.
     * 
     * @param i
     *            (int) coordonnée i.
     * @param j
     *            (int) coordonnée j.
     * @return int : la position.
     */
    private int getIJ(int i, int j) {
        return i + (lineSize - j - 1) * lineSize;

    }

    /**
     * Méthode vérifiant que le point passé en paramètre fait partie du fichier
     * HGT.
     * 
     * @param p
     *            (PointGeo)
     * @return boolean.
     */
    private boolean check(PointGeo p) {
        return p.latitude() < Math.toRadians(lat())
                || p.latitude() > Math.toRadians(lat() + 1)
                || p.longitude() < Math.toRadians(lon())
                || p.longitude() > Math.toRadians(lon() + 1);
    }
}
