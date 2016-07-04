package ch.epfl.imhof;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;

public class SwissPainterTest {

    public static void main(String[] args) throws IOException {

        Painter painter = SwissPainter.painter();
        String fileName = SwissPainterTest.class.getClass().getResource("/lausanne.osm.gz").getFile();
//        String fileName = SwissPainterTest.class.getClass().getResource("/interlaken.osm.gz").getFile();

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
//        Point bl = new Point(628590, 168210);
//        Point tr = new Point(635660, 172870);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 1600, 1060, 150,
                Color.WHITE);

        // Dessin de la carte et stockage dans un fichier
        painter.drawMap(lc, canvas);
        ImageIO.write(canvas.image(), "png", new File("lausanne123123.png"));
//        ImageIO.write(canvas.image(), "png", new File("interlaken.png"));

    }
}

