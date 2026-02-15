package com.finmanager.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationUtilTest {

    @Test
    public void testValidAmount() {
        assertTrue(ValidationUtil.isValidAmount(100.0));
        assertTrue(ValidationUtil.isValidAmount(0.01));
        assertFalse(ValidationUtil.isValidAmount(-10.0));
        assertFalse(ValidationUtil.isValidAmount(0.0));
    }

    @Test
    public void testValidCategoryName() {
        assertTrue(ValidationUtil.isValidCategoryName("Groceries"));
        assertTrue(ValidationUtil.isValidCategoryName("Entertainment"));
        assertFalse(ValidationUtil.isValidCategoryName(null));
        assertFalse(ValidationUtil.isValidCategoryName(""));
    }

    @Test
    public void testValidColor() {
        assertTrue(ValidationUtil.isValidColor("#FF5733"));
        assertTrue(ValidationUtil.isValidColor("#000000"));
        assertFalse(ValidationUtil.isValidColor("FF5733"));
        assertFalse(ValidationUtil.isValidColor("#GGGGGG"));
    }

    @Test
    public void testValidCurrency() {
        assertTrue(ValidationUtil.isValidCurrency("USD"));
        assertTrue(ValidationUtil.isValidCurrency("EUR"));
        assertFalse(ValidationUtil.isValidCurrency("USDA"));
        assertFalse(ValidationUtil.isValidCurrency("US"));
    }

    @Test
    public void testSanitizeInput() {
        String input = "Hello<World>";
        String result = ValidationUtil.sanitizeInput(input);
        assertEquals("HelloWorld", result);
    }
}
