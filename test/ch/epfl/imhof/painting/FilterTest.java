package ch.epfl.imhof.painting;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Test;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.painting.Filters;

public class FilterTest {
    
    @Test
    public void taggedWithString() {
        Predicate<Attributed<?>> p = Filters.tagged("layer");
        Map<String,String> attributes = new HashMap<>();
        attributes.put("layer", "1");
        Attributes att = new Attributes(attributes);
        Attributed<Integer> attrib = new Attributed<Integer>(new Integer(1), att);
        assertTrue(p.test(attrib));
        
    }
    
    @Test
    public void taggedWithValue() {
        Predicate<Attributed<?>> p = Filters.tagged("layer", "1");
        Map<String,String> attributes = new HashMap<>();
        attributes.put("layer", "1");
        Attributes att = new Attributes(attributes);
        Attributed<Integer> attrib = new Attributed<Integer>(new Integer(1), att);
        assertTrue(p.test(attrib));
    }
    
    @Test
    public void onLayer() {
        Predicate<Attributed<?>> p = Filters.onLayer(2);
        Map<String,String> attributes = new HashMap<>();
        attributes.put("layer", "2");
        Attributes att = new Attributes(attributes);
        Attributed<Integer> attrib = new Attributed<Integer>(new Integer(1), att);
        assertTrue(p.test(attrib));
    }
}
