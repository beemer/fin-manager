package com.finmanager.service;

import com.finmanager.db.DatabaseManager;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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

    @After
    public void tearDown() throws SQLException {
        // Clean up test data
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM expenses WHERE description = 'Test Analytics'");
        }
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
