package com.finmanager.service;

import com.finmanager.model.Expense;
import com.finmanager.model.Category;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpenseServiceTest {
    private ExpenseService expenseService;
    private CategoryService categoryService;
    private Long testCategoryId;

    @Before
    public void setUp() {
        expenseService = ExpenseService.getInstance();
        categoryService = CategoryService.getInstance();
        
        Category testCat = new Category("Test Expense Category", "LEISURE", "#FF5733");
        testCategoryId = categoryService.createCategory(testCat);
    }

    @Test
    public void testCreateExpense() {
        Expense expense = new Expense(LocalDate.now(), 50.0, testCategoryId, "Test expense");
        Long id = expenseService.createExpense(expense);
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    public void testGetExpensesByMonth() {
        YearMonth month = YearMonth.now();
        expenseService.createExpense(new Expense(month.atDay(1), 100.0, testCategoryId, "Exp1"));
        expenseService.createExpense(new Expense(month.atDay(15), 50.0, testCategoryId, "Exp2"));
        
        var expenses = expenseService.getExpensesByMonth(month);
        assertTrue(expenses.size() >= 2);
    }

    @Test
    public void testTotalExpensesByMonth() {
        YearMonth month = YearMonth.now();
        expenseService.createExpense(new Expense(month.atDay(1), 100.0, testCategoryId, "Exp1"));
        expenseService.createExpense(new Expense(month.atDay(15), 50.0, testCategoryId, "Exp2"));
        
        Double total = expenseService.getTotalExpensesByMonth(month);
        assertTrue(total >= 150.0);
    }

    @Test
    public void testUpdateExpense() {
        Expense expense = new Expense(LocalDate.now(), 75.0, testCategoryId, "Original");
        Long id = expenseService.createExpense(expense);
        
        expense.setId(id);
        expense.setAmount(100.0);
        expense.setDescription("Updated");
        expenseService.updateExpense(expense);
        
        Expense updated = expenseService.getExpensesByMonth(YearMonth.now()).stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .orElse(null);
        
        assertNotNull(updated);
        assertEquals(100.0, updated.getAmount(), 0.01);
    }

    @Test
    public void testDeleteExpense() {
        Expense expense = new Expense(LocalDate.now(), 50.0, testCategoryId, "To Delete");
        Long id = expenseService.createExpense(expense);
        
        expenseService.deleteExpense(id);
        
        var expenses = expenseService.getExpensesByMonth(YearMonth.now());
        boolean exists = expenses.stream().anyMatch(e -> e.getId().equals(id));
        assertFalse(exists);
    }

    @Test
    public void testGetExpensesByCategory() {
        expenseService.createExpense(new Expense(LocalDate.now(), 100.0, testCategoryId, "Cat Test"));
        
        var expenses = expenseService.getExpensesByCategory(testCategoryId, YearMonth.now());
        assertTrue(expenses.size() >= 1);
    }
}
