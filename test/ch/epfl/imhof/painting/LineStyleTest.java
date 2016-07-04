package ch.epfl.imhof.painting;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class LineStyleTest {

    public LineStyle constructor() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;

        return new LineStyle(width, color, lineCap, lineJoin,dashingPattern);
    }

    public LineStyle LineStyleException1() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = -1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;

        return new LineStyle(width, color, lineCap, lineJoin,dashingPattern);    
    }

    public LineStyle LineStyleException2() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, -0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;

        return new LineStyle(width, color, lineCap, lineJoin,dashingPattern);    
    }

    @Test
    public void LineStyleConstructorTest() {
        try {
            LineStyleException1();
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            LineStyleException2();
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void getColorTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;
        LineStyle l = new LineStyle(width, color, lineCap, lineJoin,dashingPattern);

        assertEquals(color, l.getColor());
    }

    @Test
    public void getWidthTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;
        LineStyle l = new LineStyle(width, color, lineCap, lineJoin,dashingPattern);

        assertEquals(width, l.getWidth(), 0.0);
    }

    @Test
    public void getAlternationTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;
        LineStyle l = new LineStyle(width, color, lineCap, lineJoin,dashingPattern);
        assertEquals(dashingPattern.length, l.getAlternation().length);
        for (int i = 0; i < dashingPattern.length; ++i) {
            assertEquals(dashingPattern[i], l.getAlternation()[i], 0.0);
        }
    }

    @Test
    public void getTerminationTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;
        LineStyle l = new LineStyle(width, color, lineCap, lineJoin,dashingPattern);
        assertEquals(LineStyle.LineCap.BUTT, l.getTermination());
    }

    @Test
    public void getJointTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;
        LineStyle l = new LineStyle(width, color, lineCap, lineJoin,dashingPattern);
        assertEquals(LineStyle.LineJoin.BEVEL, l.getJoint());
    }

    @Test
    public void withWidthTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;
        LineStyle l = new LineStyle(width, color, lineCap, lineJoin,dashingPattern);
        LineStyle li = l.withWidth(0.5f);
        assertEquals(0.5f, li.getWidth(), 0.0);
        assertEquals(dashingPattern.length, li.getAlternation().length);
        for (int i = 0; i < dashingPattern.length; ++i) {
            assertEquals(dashingPattern[i], li.getAlternation()[i], 0.0);
        }
        assertEquals(LineStyle.LineCap.BUTT, li.getTermination());
        assertEquals(LineStyle.LineJoin.BEVEL, li.getJoint());
        assertEquals(color, li.getColor());
    }
    
    @Test
    public void withColorTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;
        LineStyle l = new LineStyle(width, color, lineCap, lineJoin,dashingPattern);
        Color c = Color.rgb(0.5, 0.5, 0.5);
        LineStyle li = l.withColor(c);
        assertEquals(width, li.getWidth(), 0.0);
        assertEquals(dashingPattern.length, li.getAlternation().length);
        for (int i = 0; i < dashingPattern.length; ++i) {
            assertEquals(dashingPattern[i], li.getAlternation()[i], 0.0);
        }
        assertEquals(LineStyle.LineCap.BUTT, li.getTermination());
        assertEquals(LineStyle.LineJoin.BEVEL, li.getJoint());
        assertEquals(c, li.getColor());
    }
    
    @Test
    public void withLineCapTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;
        LineStyle l = new LineStyle(width, color, lineCap, lineJoin,dashingPattern);
        LineStyle.LineCap lC = LineStyle.LineCap.ROUND;        
        LineStyle li = l.withLineCap(lC);
        assertEquals(width, li.getWidth(), 0.0);
        assertEquals(dashingPattern.length, li.getAlternation().length);
        for (int i = 0; i < dashingPattern.length; ++i) {
            assertEquals(dashingPattern[i], li.getAlternation()[i], 0.0);
        }
        assertEquals(LineStyle.LineCap.ROUND, li.getTermination());
        assertEquals(LineStyle.LineJoin.BEVEL, li.getJoint());
        assertEquals(color, li.getColor());
    }
    
    @Test
    public void withLineJoinTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;
        LineStyle l = new LineStyle(width, color, lineCap, lineJoin,dashingPattern);
        LineStyle.LineJoin lJ = LineStyle.LineJoin.MITER;
        LineStyle li = l.withLineJoin(lJ);
        assertEquals(width, li.getWidth(), 0.0);
        assertEquals(dashingPattern.length, li.getAlternation().length);
        for (int i = 0; i < dashingPattern.length; ++i) {
            assertEquals(dashingPattern[i], li.getAlternation()[i], 0.0);
        }
        assertEquals(LineStyle.LineCap.BUTT, li.getTermination());
        assertEquals(LineStyle.LineJoin.MITER, li.getJoint());
        assertEquals(color, li.getColor());
    }
    
    @Test
    public void withDashingPatternTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        float[] dashingPattern = { 0.5f, 0.1f, 1.5f, 4.1f, 5.2f };
        LineStyle.LineCap lineCap = LineStyle.LineCap.BUTT;
        LineStyle.LineJoin lineJoin = LineStyle.LineJoin.BEVEL;
        LineStyle l = new LineStyle(width, color, lineCap, lineJoin,dashingPattern);
        float[] dP = {1.0f, 0.7f, 4.5f};
        LineStyle li = l.withDashingPattern(dP);
        assertEquals(width, li.getWidth(), 0.0);
        assertEquals(dP.length, li.getAlternation().length);
        for (int i = 0; i < dP.length; ++i) {
            assertEquals(dP[i], li.getAlternation()[i], 0.0);
        }
        assertEquals(LineStyle.LineCap.BUTT, li.getTermination());
        assertEquals(LineStyle.LineJoin.BEVEL, li.getJoint());
        assertEquals(color, li.getColor());
    }
    
    @Test 
    public void LineStyleLittleConstructorTest() {
        Color color = Color.rgb(0.5, 0.2, 0.6);
        float width = 1.5f;
        LineStyle l = new LineStyle(width, color);
        assertEquals(width, l.getWidth(),0.0);
        assertEquals(color, l.getColor());
        assertEquals(0, l.getAlternation().length);
        assertEquals(LineStyle.LineCap.BUTT, l.getTermination());
        assertEquals(LineStyle.LineJoin.MITER, l.getJoint());
    }
}
