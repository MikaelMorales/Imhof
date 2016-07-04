package ch.epfl.imhof.osm;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;

/**
 * Classe non instanciable, qui permet de construire une carte OpenStreetMap à
 * partir de données stockées dans un fichier au format OSM.
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Morales Gonzalez Mikael (234747)
 */
public final class OSMMapReader {
    /**
     * Constructeur privé et vide
     */
    private OSMMapReader() {
    }

    /**
     * Lit la carte OSM contenue dans le fichier fileName, en le décompressant
     * si unGZip est vrai. Lève l'exception SAXException en cas d'erreur dans le
     * format du fichier XML contenant la carte, ou l'exception IOException en
     * cas d'autre erreur d'entrée/sortie.
     * 
     * @param String
     *            fileName
     * @param boolean unGZip
     * @return OSMMap
     * @throws IOException
     * @throws SAXException
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip)
            throws IOException, SAXException {

        OSMMap.Builder map = new OSMMap.Builder();

        XMLReader content = XMLReaderFactory.createXMLReader();
        content.setContentHandler(new DefaultHandler() {

            OSMNode.Builder builderNode;
            OSMWay.Builder builderWay;
            OSMRelation.Builder builderRelation;
            String currentType;
            private static final String way = "way";
            private static final String node = "node";
            private static final String relation = "relation";
            private static final String member = "member";
            private static final String nd = "nd";
            private static final String ID = "id";
            private static final String REF = "ref";

            @Override
            public void startElement(String uri, String lName, String qName,
                    Attributes atts) throws SAXException {
                switch (qName) {

                case (node): {
                    long id = Long.parseLong(atts.getValue(ID));
                    double latitude = Double.parseDouble(atts.getValue("lat"));
                    latitude = Math.toRadians(latitude);
                    double longitude = Double.parseDouble(atts.getValue("lon"));
                    longitude = Math.toRadians(longitude);
                    builderNode = new OSMNode.Builder(id, new PointGeo(
                            longitude, latitude));
                    currentType = node;
                    break;
                }
                case (way): {
                    long id = Long.parseLong(atts.getValue(ID));
                    builderWay = new OSMWay.Builder(id);
                    currentType = way;
                    break;
                }
                case (nd): {
                    long ref = Long.parseLong(atts.getValue(REF));
                    builderWay.addNode(map.nodeForId(ref));
                    if (map.nodeForId(ref) == null)
                        builderWay.setIncomplete();
                    break;
                }
                case (relation): {
                    long id = Long.parseLong(atts.getValue(ID));
                    builderRelation = new OSMRelation.Builder(id);
                    currentType = relation;
                    break;
                }
                case (member): {
                    long ref = Long.parseLong(atts.getValue(REF));
                    String role = atts.getValue("role");
                    switch (atts.getValue("type")) {
                    case (way): {
                        OSMRelation.Member.Type type = OSMRelation.Member.Type.WAY;
                        builderRelation.addMember(type, role, map.wayForId(ref));
                        if (map.wayForId(ref) == null)
                            builderRelation.setIncomplete();
                        break;
                    }
                    case (node): {
                        OSMRelation.Member.Type type = OSMRelation.Member.Type.NODE;
                        builderRelation.addMember(type, role,
                                map.nodeForId(ref));
                        if (map.nodeForId(ref) == null)
                            builderRelation.setIncomplete();
                        break;
                    }
                    case (relation): {
                        OSMRelation.Member.Type type = OSMRelation.Member.Type.RELATION;
                        builderRelation.addMember(type, role,
                                map.relationForId(ref));
                        if (map.relationForId(ref) == null)
                            builderRelation.setIncomplete();
                        break;
                    }
                    default:
                        throw new SAXException();
                    }
                   break;
                }
                case ("tag"): {
                    String k = atts.getValue("k");
                    String v = atts.getValue("v");
                    switch (currentType) {
                    case (node): {
                        builderNode.setAttribute(k, v);
                        break;
                    }
                    case (way): {
                        builderWay.setAttribute(k, v);
                        break;
                    }
                    case (relation): {
                        builderRelation.setAttribute(k, v);
                        break;
                    }
                    default:
                        throw new SAXException();
                    }
                    break;
                }
                }
            }

            @Override
            public void endElement(String uri, String lName, String qName) {
                switch (qName) {

                case (node): {
                    if (!builderNode.isIncomplete())
                        map.addNode(builderNode.build());
                    break;
                }
                case (way): {
                    if (!builderWay.isIncomplete())
                        map.addWay(builderWay.build());
                    break;
                }
                case (relation): {
                    if (!builderRelation.isIncomplete())
                        map.addRelation(builderRelation.build());
                    break;
                }
                }
            }
        });
        try (InputStream i = getInputStream(unGZip, fileName)) {
            content.parse(new InputSource(i));
        }
        return map.build();
    }

    /**
     * Retourne un InputStream ou GZIPUnputStream en fonction de la valeur du
     * boolean donné.
     * 
     * @param boolean unGZip
     * @param String
     *            fileName
     * @return InputStream
     * @throws IOException
     */
    private static InputStream getInputStream(boolean unGZip, String fileName)
            throws IOException {
        if (!unGZip)
            return new BufferedInputStream(new FileInputStream(fileName));
        return new GZIPInputStream(new BufferedInputStream(new FileInputStream(
                fileName)));
    }

}
