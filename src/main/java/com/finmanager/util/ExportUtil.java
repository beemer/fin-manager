package com.finmanager.util;

import com.finmanager.model.Expense;
import com.finmanager.model.Category;
import com.finmanager.model.InvestmentEntry;
import com.finmanager.service.CategoryService;
import com.finmanager.service.ExpenseService;
import com.finmanager.service.InvestmentService;

import java.io.*;
import java.time.YearMonth;
import java.time.Year;
import java.util.List;

public class ExportUtil {
    private static final String DELIMITER = ",";
    private static final String QUOTE = "\"";

    public static void exportExpensesToCSV(String filename, YearMonth yearMonth) {
        ExpenseService expenseService = ExpenseService.getInstance();
        CategoryService categoryService = CategoryService.getInstance();
        List<Expense> expenses = expenseService.getExpensesByMonth(yearMonth);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Date,Category,Amount,Description");
            
            for (Expense expense : expenses) {
                Category category = categoryService.getCategoryById(expense.getCategoryId());
                String categoryName = category != null ? category.getName() : "Unknown";
                
                String line = String.format("%s,%s,%.2f,%s",
                    expense.getDate(),
                    escapeCsvValue(categoryName),
                    expense.getAmount(),
                    escapeCsvValue(expense.getDescription())
                );
                writer.println(line);
            }
        } catch (IOException e) {
            Logger.error(ExportUtil.class, "Failed to export expenses", e);
        }
    }

    public static void exportInvestmentsToCSV(String filename, int year) {
        InvestmentService investmentService = InvestmentService.getInstance();
        List<InvestmentEntry> investments = investmentService.getInvestmentsByYear(Year.of(year));

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Date,Amount,Currency,ExchangeRate,BaseCurrencyAmount,Description");
            
            for (InvestmentEntry entry : investments) {
                String line = String.format("%s,%.2f,%s,%.4f,%.2f,%s",
                    entry.getDate(),
                    entry.getAmount(),
                    entry.getCurrency(),
                    entry.getExchangeRate(),
                    entry.getAmountInBaseCurrency(),
                    escapeCsvValue(entry.getDescription())
                );
                writer.println(line);
            }
        } catch (IOException e) {
            Logger.error(ExportUtil.class, "Failed to export investments", e);
        }
    }

    private static String escapeCsvValue(String value) {
        if (value == null) return "";
        if (value.contains(DELIMITER) || value.contains(QUOTE) || value.contains("\n")) {
            return QUOTE + value.replace(QUOTE, QUOTE + QUOTE) + QUOTE;
        }
        return value;
    }
}
