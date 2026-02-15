package com.finmanager.service;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class AnalyticsServiceIntegrationTest {
    private AnalyticsService analyticsService;
    private CategoryService categoryService;
    private ExpenseService expenseService;

    @Before
    public void setUp() {
        analyticsService = AnalyticsService.getInstance();
        categoryService = CategoryService.getInstance();
        expenseService = ExpenseService.getInstance();
    }

    @Test
    public void testAnalyticsWithMultipleExpenses() {
        java.time.YearMonth month = java.time.YearMonth.now();
        
        var categories = categoryService.getAllCategories();
        if (!categories.isEmpty()) {
            var category = categories.get(0);
            expenseService.createExpense(new com.finmanager.model.Expense(
                java.time.LocalDate.now(), 100.0, category.getId(), "Test Analytics"
            ));

            var breakdown = analyticsService.getCategoryBreakdown(month);
            assertNotNull(breakdown);
            
            var total = analyticsService.getMonthlyTotal(month);
            assertTrue(total >= 100.0);
        }
    }
}
