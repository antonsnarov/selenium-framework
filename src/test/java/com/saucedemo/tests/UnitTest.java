package com.saucedemo.tests;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class UnitTest extends BaseTest {

    @Test(groups = "unit")
    public void testBasicMath() {
        System.out.println("Running basic math test");
        assertEquals(5 + 3, 8, "Addition should work");
        assertEquals(10 - 4, 6, "Subtraction should work");
        System.out.println("Basic math test passed");
    }

    @Test(groups = "unit")
    public void testStringManipulation() {
        System.out.println("Running string manipulation test");
        String str = "TestNG";
        assertNotNull(str, "String should not be null");
        assertTrue(str.length() > 0, "String should have length > 0");
        assertEquals(str.toLowerCase(), "testng", "toLowerCase should work");
        System.out.println("String manipulation test passed");
    }

    @Test(groups = "unit")
    public void testCollections() {
        System.out.println("Running collections test");
        java.util.List<String> list = java.util.Arrays.asList("a", "b", "c");
        assertEquals(list.size(), 3, "List should have 3 elements");
        assertTrue(list.contains("b"), "List should contain 'b'");
        System.out.println("Collections test passed");
    }
}
