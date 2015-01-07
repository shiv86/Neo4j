package com.packtpub.learningcypher.chapter2;

import java.util.regex.Pattern;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Regular expressions test
     */
    public void testApp()
    {        
        assertTrue(Pattern.matches(".*Text.*", "This Text is very good"));
        
        assertTrue(Pattern.matches("Te[sx]t", "Test"));
        assertTrue(Pattern.matches("Te[sx]t", "Text"));
        
        assertTrue(Pattern.matches("Te[sx]*t", "Test"));
        assertTrue(Pattern.matches("Te[sx]*t", "Text"));
        assertTrue(Pattern.matches("Te[sx]*t", "Texxt"));
        assertTrue(Pattern.matches("Te[sx]*t", "Tet"));
        
        assertFalse(Pattern.matches("Te.t", "Tet"));
        assertTrue(Pattern.matches("Te.t", "Text"));
        assertTrue(Pattern.matches("Te.t", "Test"));
        
        assertTrue(Pattern.matches("Te[sx]?t", "Test"));
        assertTrue(Pattern.matches("Te[sx]?t", "Text"));
        assertFalse(Pattern.matches("Te[sx]?t", "Texxt"));
        assertTrue(Pattern.matches("Te[sx]?t", "Tet"));
        
        assertTrue(Pattern.matches("Te(xt|ll)", "Text"));
        assertTrue(Pattern.matches("Te(xt|ll)", "Tell"));
    }
}
