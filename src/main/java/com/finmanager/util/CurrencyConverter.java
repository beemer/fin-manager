package com.finmanager.util;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
    private static final Map<String, Double> exchangeRates = new HashMap<>();
    private static final String BASE_CURRENCY = "USD";

    static {
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 1.1);
        exchangeRates.put("GBP", 1.27);
        exchangeRates.put("CAD", 0.74);
        exchangeRates.put("AUD", 0.66);
        exchangeRates.put("JPY", 0.0074);
        exchangeRates.put("CHF", 1.1);
        exchangeRates.put("CNY", 0.14);
        exchangeRates.put("INR", 0.012);
        exchangeRates.put("MXN", 0.058);
    }

    public static Double convert(Double amount, String fromCurrency, String toCurrency) {
        if (amount == null || amount == 0) {
            return 0.0;
        }
        
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        Double fromRate = exchangeRates.getOrDefault(fromCurrency, 1.0);
        Double toRate = exchangeRates.getOrDefault(toCurrency, 1.0);

        return amount * (toRate / fromRate);
    }

    public static Double convertToBase(Double amount, String currency) {
        return convert(amount, currency, BASE_CURRENCY);
    }

    public static Double convertFromBase(Double amount, String currency) {
        return convert(amount, BASE_CURRENCY, currency);
    }

    public static void updateExchangeRate(String currency, Double rate) {
        if (rate != null && rate > 0) {
            exchangeRates.put(currency, rate);
        }
    }

    public static Double getExchangeRate(String currency) {
        return exchangeRates.getOrDefault(currency, 1.0);
    }

    public static boolean isCurrencySupported(String currency) {
        return exchangeRates.containsKey(currency);
    }
}
