package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;
/**
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Mikael Morales Gonzalez (234747)
 *
 * Classe permettant de dessiner un relief ombré coloré. 
 */
public final class ReliefShader {

	private final Projection projection;
	private final HGTDigitalElevationModel model;
	private final Vector3 vector;
	
	/**
	 * Constructeur de la classe ReliefShader.
	 * @param projection Projection
	 * @param model HGTDigitalElevationModel
	 * @param vector Vector3
	 */
	public ReliefShader(Projection projection, HGTDigitalElevationModel model, Vector3 vector) {
		this.projection = projection;
		this.model = model;
		this.vector = vector.normalized();
	}
	
	/**
	 * Méthode retournant une image ombré coloré.
	 * @param bl Point en bas à gauche du relief. 
	 * @param tr Point en haut à droite du relief.
	 * @param width largeur de l'image.
	 * @param height hauteur de l'image.
	 * @param radius le rayon de floutage.
	 * @return l'image représentant les reliefs.
	 */
	public BufferedImage shadedRelief(Point bl, Point tr, int width, int height, double radius) {
		if(radius == 0) {
			Function<Point, Point> f = Point.alignedCoordinateChange(new Point(0.0, height), bl, new Point(width, 0.0), tr);
			return brutShadow(width, height, f);
		}else {
			int r = (int)Math.ceil(radius);
			Function<Point, Point> f = Point.alignedCoordinateChange(new Point(r, height + r), bl, new Point(width + r, r), tr);
			BufferedImage image = brutShadow(width + 2*r, height + 2*r, f);
			float[] tab = ker(radius);
			image = blur(new Kernel(tab.length,1,tab),image);
			image = blur(new Kernel(1, tab.length, tab), image);
			return image.getSubimage(r, r, width, height);
			}
	}
	
	/**
	 * Méthode dessinant un relief ombré brut (sans floutage).
	 * @param width largeur en pixels.
	 * @param height hauteur en pixels.
	 * @param function fonction permettant de réaliser le changement de variable.
	 * @return BufferedImage
	 */
	private BufferedImage brutShadow(int width, int height, Function<Point, Point> function) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < width ; ++x) {
			for(int y = 0; y < height; ++y) {
				Point point = function.apply(new Point(x,y));
				Vector3 v = model.normalAt(projection.inverse(point));
				double cosTheta = v.scalarProduct(vector)/(v.norm()*vector.norm());
				double rg = 1/2d * (cosTheta + 1);
				double b = 1/2d * (0.7 * cosTheta + 1);
				Color c =  Color.rgb(rg, rg, b);
				image.setRGB(x, y, c.convert().getRGB());
			}
		}
		return image;
	}
	 /**
	  * Méthode calculant le tableau du noyau du flou gaussien.
	  * @param r rayon de floutage.
	  * @return float[]
	  */
	private float[] ker(double r) {
        double sigma = r/3d;
        int n = (int)(2*Math.ceil(r) + 1);
        int centerPosition = (int)Math.ceil(n/2d);
        Function<Integer, Float> f = x -> (float)(Math.exp(-(x*x)/(2*sigma*sigma)));
        float sum = 0;
        float[] tab = new float[n];
        
        for(int x=0; x<n; x++) {
            int i = x - centerPosition;
            sum += f.apply(i);
            tab[x] = f.apply(i);
        }
        //Normalise le tableau
        for(int i=0; i<n; i++) {
            tab[i] = tab[i]/sum;
        }
        return tab;
    }
	
	/**
	 * Méthode retournant un image floutée. 
	 * @param k le noyau du flou gaussien.
	 * @param image l'image à flouter.
	 * @return BufferedImage.
	 */
	private BufferedImage blur(Kernel k, BufferedImage image) {
		ConvolveOp c = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
		return c.filter(image, null);
	}
}
