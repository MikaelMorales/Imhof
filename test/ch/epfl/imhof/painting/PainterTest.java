package ch.epfl.imhof.painting;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;

public class PainterTest {

    public static void main(String[] args) throws IOException {
        Predicate<Attributed<?>> isLake = Filters.tagged("natural", "water");
        Painter lakesPainter = Painter.polygon(Color.BLUE).when(isLake);

        Predicate<Attributed<?>> isBuilding = Filters.tagged("building");
        Painter buildingsPainter = Painter.polygon(Color.BLACK)
                .when(isBuilding);

        Painter painter = buildingsPainter.above(lakesPainter);
        String fileName = PainterTest.class.getClass().getResource("/lausanne.osm.gz").getFile();
        OSMMap osmmap = null;
        try {
            osmmap = OSMMapReader.readOSMFile(fileName, true);
        } catch (SAXException e) {
            e.printStackTrace();
        } 
                                                                  
        OSMToGeoTransformer projection = new OSMToGeoTransformer(
                new CH1903Projection());
        Map lc = projection.transform(osmmap);
        // La toile
        Point bl = new Point(532510, 150590);
        Point tr = new Point(539570, 155260);      
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 800, 530, 72,
                Color.WHITE);

        // Dessin de la carte et stockage dans un fichier
        painter.drawMap(lc, canvas);
        ImageIO.write(canvas.image(), "png", new File("loz.png"));
    }
}
