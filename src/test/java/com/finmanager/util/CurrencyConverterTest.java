package com.finmanager.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class CurrencyConverterTest {

    @Test
    public void testSameCurrencyConversion() {
        Double result = CurrencyConverter.convert(100.0, "USD", "USD");
        assertEquals(100.0, result, 0.01);
    }

    @Test
    public void testUSDToEUR() {
        Double result = CurrencyConverter.convert(100.0, "USD", "EUR");
        assertTrue(result > 0);
    }

    @Test
    public void testConvertToBase() {
        Double result = CurrencyConverter.convertToBase(100.0, "USD");
        assertEquals(100.0, result, 0.01);
    }

    @Test
    public void testIsCurrencySupported() {
        assertTrue(CurrencyConverter.isCurrencySupported("USD"));
        assertTrue(CurrencyConverter.isCurrencySupported("EUR"));
    }

    @Test
    public void testGetExchangeRate() {
        Double rate = CurrencyConverter.getExchangeRate("USD");
        assertEquals(1.0, rate, 0.01);
    }

    @Test
    public void testUpdateExchangeRate() {
        CurrencyConverter.updateExchangeRate("USD", 1.05);
        Double rate = CurrencyConverter.getExchangeRate("USD");
        assertEquals(1.05, rate, 0.01);
    }
}
