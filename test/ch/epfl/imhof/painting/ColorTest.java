package ch.epfl.imhof.painting;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class ColorTest {
    
    @Test
    public void createColor() {
        try {
            Color.rgb(1.5, 0, 0);
            Assert.fail("r failed");
        } catch (IllegalArgumentException e) {
        }
        try {
            Color.rgb(1, 1.5, 0);
            Assert.fail("g failed");
        } catch (IllegalArgumentException e) {
        }
        try {
            Color.rgb(0, 0, 1.5);
            Assert.fail("b failed");
        } catch (IllegalArgumentException e) {
        }       
    }
    
    @Test
    public void testColorStaticMethods() {
        //Test RED r, g et b
        assertEquals(1, Color.RED.r(), 0.0);
        assertEquals(0, Color.RED.g(), 0.0);
        assertEquals(0, Color.RED.b(), 0.0);
      //Test Green r, g et b
        assertEquals(0, Color.GREEN.r(), 0.0);
        assertEquals(1, Color.GREEN.g(), 0.0);
        assertEquals(0, Color.GREEN.b(), 0.0);
      //Test BLUE r, g et b
        assertEquals(0, Color.BLUE.r(), 0.0);
        assertEquals(0, Color.BLUE.g(), 0.0);
        assertEquals(1, Color.BLUE.b(), 0.0);
      //Test BLACK r, g et b
        assertEquals(0, Color.BLACK.r(), 0.0);
        assertEquals(0, Color.BLACK.g(), 0.0);
        assertEquals(0, Color.BLACK.b(), 0.0);
      //Test WHITE r, g et b
        assertEquals(1, Color.WHITE.r(), 0.0);
        assertEquals(1, Color.WHITE.g(), 0.0);
        assertEquals(1, Color.WHITE.b(), 0.0);
    }
    
    @Test
    public void grayWorks() {
        Color c = Color.gray(0.5);
      //Test Gray r, g et b
        assertEquals(0.5, c.r(), 0.0);
        assertEquals(0.5, c.g(), 0.0);
        assertEquals(0.5, c.b(), 0.0);
    }
    
    @Test
    public void rgbBinaryWorks() {
        Color c = Color.rgb(134217727); //2^27-1
      //Test de c pour r, g et b
        assertEquals(1, c.r(), 0.0);
        assertEquals(1, c.g(), 0.0);
        assertEquals(1, c.b(), 0.0);
      //Test de c2 pour r,g et b 
        Color c2 = Color.rgb(174762);
        assertEquals(2/255d, c2.r(), 0.0);
        assertEquals(170/255d, c2.g(), 0.0);
        assertEquals(170/255d, c2.b(), 0.0);           
    }
    
    @Test
    public void multiplyWork() {
        Color c = Color.gray(0.5);
        Color c2 = Color.RED;
        Color c3 = c.multiply(c2);
        //Test de c3 pour r,g et b
        assertEquals(0.5, c3.r(), 0.0);
        assertEquals(0, c3.g(), 0.0);
        assertEquals(0, c3.b(), 0.0);    
    }
    
    @Test
    public void convertWork() {
        Color c = Color.RED;
        java.awt.Color c2 = c.convert();
      //Test de c2 pour r,g et b
        assertEquals(255, c2.getRed(), 0.0);
        assertEquals(0, c2.getGreen(), 0.0);
        assertEquals(0, c2.getBlue(), 0.0); 
        
    }
}
