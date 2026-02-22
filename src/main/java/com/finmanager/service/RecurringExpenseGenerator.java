package com.finmanager.service;

import com.finmanager.model.RecurringExpense;
import com.finmanager.model.Expense;
import com.finmanager.db.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * Generates expense instances from recurring expense definitions.
 * Handles MONTHLY and YEARLY frequencies with proper date boundary validation.
 */
public class RecurringExpenseGenerator {
    private static RecurringExpenseGenerator instance;
    private final RecurringExpenseService recurringExpenseService;
    private final ExpenseService expenseService;

    private RecurringExpenseGenerator() {
        this.recurringExpenseService = RecurringExpenseService.getInstance();
        this.expenseService = ExpenseService.getInstance();
    }

    public static RecurringExpenseGenerator getInstance() {
        if (instance == null) {
            instance = new RecurringExpenseGenerator();
        }
        return instance;
    }

    /**
     * Generate instances for a single recurring expense.
     * @param recurringId ID of the recurring expense
     * @param upToDate Generate instances up to this date (inclusive)
     * @return Number of instances generated
     */
    public int generateForRecurring(Long recurringId, LocalDate upToDate) {
        int count = 0;
        
        // Get the recurring expense with last_generated_date
        List<RecurringExpense> allRecurring = recurringExpenseService.getAllRecurringExpenses();
        RecurringExpense recurring = allRecurring.stream()
            .filter(r -> r.getId().equals(recurringId))
            .findFirst()
            .orElse(null);
        
        if (recurring == null || !recurring.isActive()) {
            return 0;
        }
        
        // Determine starting point for generation
        LocalDate generationStart = (recurring.getLastGeneratedDate() == null) 
            ? recurring.getStartDate()
            : recurring.getLastGeneratedDate().plusDays(1);
        
        if (generationStart.isAfter(upToDate)) {
            return 0; // Already generated all instances
        }
        
        // Respect end date boundary
        LocalDate generationEnd = (recurring.getEndDate() != null && recurring.getEndDate().isBefore(upToDate))
            ? recurring.getEndDate()
            : upToDate;
        
        // Generate instances based on frequency
        if (recurring.getFrequency() == RecurringExpense.Frequency.MONTHLY) {
            count = generateMonthlyInstances(recurring, generationStart, generationEnd);
        } else if (recurring.getFrequency() == RecurringExpense.Frequency.YEARLY) {
            count = generateYearlyInstances(recurring, generationStart, generationEnd);
        }
        
        // Update last_generated_date
        if (count > 0) {
            updateLastGeneratedDate(recurringId, generationEnd);
        }
        
        return count;
    }

    /**
     * Generate instances for all active recurring expenses.
     * @param upToDate Generate instances up to this date (inclusive)
     * @return Total number of instances generated
     */
    public int generateAllRecurring(LocalDate upToDate) {
        int totalGenerated = 0;
        List<RecurringExpense> allRecurring = recurringExpenseService.getAllRecurringExpenses();
        
        for (RecurringExpense recurring : allRecurring) {
            if (recurring.isActive()) {
                totalGenerated += generateForRecurring(recurring.getId(), upToDate);
            }
        }
        
        return totalGenerated;
    }

    /**
     * Generate instances for today.
     * @return Number of instances generated
     */
    public int generateForToday() {
        return generateAllRecurring(LocalDate.now());
    }

    private int generateMonthlyInstances(RecurringExpense recurring, LocalDate start, LocalDate end) {
        int count = 0;
        YearMonth current = YearMonth.from(start);
        YearMonth endYearMonth = YearMonth.from(end);
        
        while (!current.isAfter(endYearMonth)) {
            LocalDate instanceDate = current.atDay(1);
            
            // Make sure we're within the generation range
            if (!instanceDate.isBefore(start) && !instanceDate.isAfter(end)) {
                // Check if instance already exists (avoid duplicates)
                if (!expenseInstanceExists(recurring.getId(), instanceDate)) {
                    Expense instance = new Expense(
                        instanceDate,
                        recurring.getAmount(),
                        recurring.getCategoryId(),
                        recurring.getDescription()
                    );
                    instance.setRecurringId(recurring.getId());
                    instance.setRecurringInstance(true);
                    
                    if (expenseService.createExpense(instance) != null) {
                        count++;
                    }
                }
            }
            
            current = current.plusMonths(1);
        }
        
        return count;
    }

    private int generateYearlyInstances(RecurringExpense recurring, LocalDate start, LocalDate end) {
        int count = 0;
        int startYear = start.getYear();
        int endYear = end.getYear();
        
        for (int year = startYear; year <= endYear; year++) {
            LocalDate instanceDate = LocalDate.of(year, 1, 1);
            
            // Make sure we're within the generation range and boundaries
            if (!instanceDate.isBefore(start) && !instanceDate.isAfter(end) && 
                !instanceDate.isBefore(recurring.getStartDate()) &&
                (recurring.getEndDate() == null || !instanceDate.isAfter(recurring.getEndDate()))) {
                
                // Check if instance already exists (avoid duplicates)
                if (!expenseInstanceExists(recurring.getId(), instanceDate)) {
                    Expense instance = new Expense(
                        instanceDate,
                        recurring.getAmount(),
                        recurring.getCategoryId(),
                        recurring.getDescription()
                    );
                    instance.setRecurringId(recurring.getId());
                    instance.setRecurringInstance(true);
                    
                    if (expenseService.createExpense(instance) != null) {
                        count++;
                    }
                }
            }
        }
        
        return count;
    }

    private boolean expenseInstanceExists(Long recurringId, LocalDate date) {
        String sql = "SELECT COUNT(*) as cnt FROM expenses WHERE recurring_id = ? AND date = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, recurringId);
            pstmt.setString(2, date.toString());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    private void updateLastGeneratedDate(Long recurringId, LocalDate date) {
        String sql = "UPDATE recurring_expenses SET last_generated_date = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, date.toString());
            pstmt.setLong(2, recurringId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
