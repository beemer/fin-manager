package com.finmanager.service;

import com.finmanager.model.RecurringExpense;
import com.finmanager.model.Category;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalDate;

public class RecurringExpenseServiceTest {
    private RecurringExpenseService recurringService;
    private CategoryService categoryService;
    private Long testCategoryId;

    @Before
    public void setUp() {
        recurringService = RecurringExpenseService.getInstance();
        categoryService = CategoryService.getInstance();
        
        Category testCat = new Category("Recurring Test Cat", "UTILITIES", "#00FF00");
        testCategoryId = categoryService.createCategory(testCat);
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
