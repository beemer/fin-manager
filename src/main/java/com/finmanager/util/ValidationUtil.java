package com.finmanager.util;

import java.time.LocalDate;

public class ValidationUtil {

    public static boolean isValidAmount(Double amount) {
        return amount != null && amount > 0;
    }

    public static boolean isValidCategoryName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 50;
    }

    public static boolean isValidDescription(String description) {
        return description == null || description.length() <= 500;
    }

    public static boolean isValidDate(LocalDate date) {
        return date != null && !date.isBefore(LocalDate.of(2020, 1, 1)) && !date.isAfter(LocalDate.now());
    }

    public static boolean isValidColor(String color) {
        if (color == null || !color.startsWith("#")) return false;
        return color.matches("#[0-9A-Fa-f]{6}");
    }

    public static boolean isValidCurrency(String currency) {
        return currency != null && currency.matches("[A-Z]{3}");
    }

    public static boolean isValidExchangeRate(Double rate) {
        return rate != null && rate > 0;
    }

    public static boolean isValidYear(int year) {
        return year >= 2020 && year <= LocalDate.now().getYear();
    }

    public static String sanitizeInput(String input) {
        if (input == null) return null;
        return input.trim().replaceAll("[<>\"'%;()&+]", "");
    }
}
