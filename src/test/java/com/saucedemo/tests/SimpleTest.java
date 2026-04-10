package com.saucedemo.tests;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SimpleTest {

    @Test
    public void testSimpleAssertion() {
        assertEquals(2 + 2, 4, "Basic math should work");
        assertTrue(true, "True should be true");
        System.out.println("Simple test executed successfully!");
    }

    @Test
    public void testStringOperations() {
        String test = "Hello World";
        assertNotNull(test, "String should not be null");
        assertEquals(test.length(), 11, "String length should be 11");
        assertTrue(test.contains("World"), "String should contain 'World'");
    }
}
