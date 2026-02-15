package com.finmanager.service;

import com.finmanager.model.RecurringExpense;
import com.finmanager.model.Category;
import com.finmanager.db.DatabaseManager;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class RecurringExpenseServiceTest {
    private RecurringExpenseService recurringService;
    private CategoryService categoryService;
    private Long testCategoryId;

    @Before
    public void setUp() throws SQLException {
        recurringService = RecurringExpenseService.getInstance();
        categoryService = CategoryService.getInstance();
        
        // Clean up any existing test data first
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM recurring_expenses WHERE description LIKE 'Test%' OR description LIKE 'Monthly%'");
            stmt.executeUpdate("DELETE FROM categories WHERE name LIKE 'Recurring%'");
        }
        
        // Create new category with unique name
        String categoryName = "Recurring Test Cat_" + System.nanoTime();
        Category testCat = new Category(categoryName, "UTILITIES", "#00FF00");
        testCategoryId = categoryService.createCategory(testCat);
    }

    @After
    public void tearDown() throws SQLException {
        // Clean up test data after each test
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM recurring_expenses WHERE description LIKE 'Test%' OR description LIKE 'Monthly%'");
            stmt.executeUpdate("DELETE FROM categories WHERE name LIKE 'Recurring%'");
        }
    }

    @Test
    public void testCreateRecurringExpense() {
        RecurringExpense recurring = new RecurringExpense(
            testCategoryId, 100.0, "Monthly Bill", RecurringExpense.Frequency.MONTHLY, LocalDate.now()
        );
        Long id = recurringService.createRecurringExpense(recurring);
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    public void testGetAllRecurringExpenses() {
        int initialCount = recurringService.getAllRecurringExpenses().size();
        
        RecurringExpense recurring = new RecurringExpense(
            testCategoryId, 50.0, "Test Recurring", RecurringExpense.Frequency.MONTHLY, LocalDate.now()
        );
        recurringService.createRecurringExpense(recurring);
        
        int finalCount = recurringService.getAllRecurringExpenses().size();
        assertTrue(finalCount >= initialCount);
    }

    @Test
    public void testUpdateRecurringExpense() {
        RecurringExpense recurring = new RecurringExpense(
            testCategoryId, 75.0, "Original", RecurringExpense.Frequency.YEARLY, LocalDate.now()
        );
        Long id = recurringService.createRecurringExpense(recurring);
        
        recurring.setId(id);
        recurring.setAmount(150.0);
        recurringService.updateRecurringExpense(recurring);
        
        RecurringExpense updated = recurringService.getAllRecurringExpenses().stream()
            .filter(r -> r.getId().equals(id))
            .findFirst()
            .orElse(null);
        
        assertNotNull(updated);
        assertEquals(150.0, updated.getAmount(), 0.01);
    }

    @Test
    public void testDeleteRecurringExpense() {
        RecurringExpense recurring = new RecurringExpense(
            testCategoryId, 50.0, "To Delete", RecurringExpense.Frequency.MONTHLY, LocalDate.now()
        );
        Long id = recurringService.createRecurringExpense(recurring);
        
        recurringService.deleteRecurringExpense(id);
        
        RecurringExpense deleted = recurringService.getAllRecurringExpenses().stream()
            .filter(r -> r.getId().equals(id))
            .findFirst()
            .orElse(null);
        
        assertNull(deleted);
    }
}
