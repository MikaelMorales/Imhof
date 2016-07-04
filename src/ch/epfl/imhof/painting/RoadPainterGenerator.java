package ch.epfl.imhof.painting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Classe non instanciable représentant un peintre routier.
 *
 * @author Charles Parzy Turlat (250628)
 * @author Morales Gonzalez Mikael (234747)
 */
public class RoadPainterGenerator {

    private RoadPainterGenerator() {
    };

    /**
     * 
     * @param spec
     *            (RoadSpec)
     * @return Retourne le peintre pour le réseau routier (RoadSpec)
     *         correspondant en superposant les différents peintres de routes
     *         selon un ordre défini.
     */
    public static Painter painterForRoads(RoadSpec... spec) {
        if (spec.length == 0) {
            throw new IllegalArgumentException();
        }
        List<Painter> list = Arrays.asList(
                abovePainter(RoadSpec::insideBridge, spec),
                abovePainter(RoadSpec::edgeBridge, spec),
                abovePainter(RoadSpec::insideRoad, spec),
                abovePainter(RoadSpec::edgeRoad, spec),
                abovePainter(RoadSpec::tunnel, spec));

        Painter p = list.stream().reduce(Painter::above).get();
        return p.layered();
    }

    /**
     * Retourne un peinte en superposant tous les peintres dans le réseau
     * routier qui satisfont la Function.
     * 
     * @param name
     *            (Function<RoadSpec, Painter> )
     * @param spec
     *            (RoadSpec)
     * @return Painter
     */
    private static Painter abovePainter(Function<RoadSpec, Painter> name,
            RoadSpec... spec) {
        Painter p = name.apply(spec[0]);
        for (int i = 1; i < spec.length; i++) {
            p = p.above(name.apply(spec[i]));
        }
        return p;
    }

    /**
     * Classe représentant un réseau routier.
     *
     * @author Charles Parzy Turlat (250628)
     * @author Morales Gonzalez Mikael (234747)
     */
    public static class RoadSpec {

        private final Predicate<Attributed<?>> e;
        private final LineStyle insideBridge;
        private final LineStyle edgeBridge;
        private final LineStyle tunnel;

        /**
         * Constructeur du réseau routier.
         * 
         * @param e
         *            Predicate<Attributed<?>>
         * @param LineStyle
         *            (style interieur pont)
         * @param LineStyle
         *            (style bordure pont)
         * @param LineStyle
         *            (style tunnel)
         */
        public RoadSpec(Predicate<Attributed<?>> e, float wi, Color ci,
                float wc, Color cc) {
            this.e = e;
            insideBridge = new LineStyle(wi, ci, LineCap.ROUND, LineJoin.ROUND);
            edgeBridge = new LineStyle(wi + 2 * wc, cc, LineCap.BUTT,
                    LineJoin.ROUND);
            tunnel = new LineStyle(wi / 2, cc, LineCap.BUTT, LineJoin.ROUND,
                    2 * wi, 2 * wi);

        }

        /**
         * @return un peintre d'intérieur de pont si celui ci satisfait le
         *         prédicat.
         */
        public Painter insideBridge() {
            
            Painter p = Painter.line(insideBridge).when(
                    e.and(Filters.tagged("bridge")));
            return p;
        }

        /**
         * @return un peintre de bordure de pont si celui ci satisfait le
         *         prédicat.
         */
        public Painter edgeBridge() {
            Painter p = Painter.line(edgeBridge).when(
                    e.and(Filters.tagged("bridge")));
            return p;
        }

        /**
         * @return un peintre d'intérieur de route si celui ci satisfait le
         *         prédicat.
         */
        public Painter insideRoad() {
            Painter p = Painter.line(insideBridge).when(
                    e.and(Filters.tagged("bridge").or(Filters.tagged("tunnel"))
                            .negate()));
            return p;
        }

        /**
         * @return un peintre de bordure de route si celui ci satisfait le
         *         prédicat.
         */
        public Painter edgeRoad() {
            Painter p = Painter.line(edgeBridge.withLineCap(LineCap.ROUND))
                    .when(e.and(Filters.tagged("bridge")
                            .or(Filters.tagged("tunnel")).negate()));
            return p;
        }

        /**
         * @return un peintre de tunnel si celui ci satisfait le prédicat.
         */
        public Painter tunnel() {
            Painter p = Painter.line(tunnel).when(
                    e.and(Filters.tagged("tunnel")));
            return p;
        }
    }
}
