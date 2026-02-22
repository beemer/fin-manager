package com.finmanager.service;

import com.finmanager.model.RecurringExpense;
import com.finmanager.model.Expense;
import com.finmanager.db.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class RecurringExpenseService {
    private static RecurringExpenseService instance;

    private RecurringExpenseService() {}

    public static RecurringExpenseService getInstance() {
        if (instance == null) {
            instance = new RecurringExpenseService();
        }
        return instance;
    }

    public List<RecurringExpense> getAllRecurringExpenses() {
        List<RecurringExpense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM recurring_expenses WHERE active = 1 ORDER BY category_id";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                expenses.add(mapResultSetToRecurringExpense(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public Long createRecurringExpense(RecurringExpense expense) {
        String sql = "INSERT INTO recurring_expenses(category_id, amount, description, frequency, start_date, end_date, active) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, expense.getCategoryId());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getDescription());
            pstmt.setString(4, expense.getFrequency().toString());
            pstmt.setString(5, expense.getStartDate().toString());
            pstmt.setString(6, expense.getEndDate() != null ? expense.getEndDate().toString() : null);
            pstmt.setBoolean(7, expense.isActive());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateRecurringExpense(RecurringExpense expense) {
        String sql = "UPDATE recurring_expenses SET category_id = ?, amount = ?, description = ?, frequency = ?, " +
                     "start_date = ?, end_date = ?, active = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, expense.getCategoryId());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getDescription());
            pstmt.setString(4, expense.getFrequency().toString());
            pstmt.setString(5, expense.getStartDate().toString());
            pstmt.setString(6, expense.getEndDate() != null ? expense.getEndDate().toString() : null);
            pstmt.setBoolean(7, expense.isActive());
            pstmt.setLong(8, expense.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecurringExpense(Long id) {
        String sql = "UPDATE recurring_expenses SET active = 0 WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollForwardRecurringExpenses(YearMonth yearMonth) {
        LocalDate monthStart = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();

        List<RecurringExpense> recurring = getAllRecurringExpenses();
        ExpenseService expenseService = ExpenseService.getInstance();

        for (RecurringExpense rec : recurring) {
            if (!rec.isActive()) continue;
            if (rec.getStartDate().isAfter(monthEnd)) continue;
            if (rec.getEndDate() != null && rec.getEndDate().isBefore(monthStart)) continue;

            if (rec.getFrequency() == RecurringExpense.Frequency.MONTHLY) {
                Expense exp = new Expense(monthStart, rec.getAmount(), rec.getCategoryId(), rec.getDescription());
                exp.setRecurringInstance(true);
                expenseService.createExpense(exp);
            } else if (rec.getFrequency() == RecurringExpense.Frequency.YEARLY) {
                LocalDate jan1 = LocalDate.of(monthStart.getYear(), 1, 1);
                if (monthStart.getMonthValue() == 1) {
                    Expense exp = new Expense(jan1, rec.getAmount(), rec.getCategoryId(), rec.getDescription());
                    exp.setRecurringInstance(true);
                    expenseService.createExpense(exp);
                }
            }
        }
    }

    private RecurringExpense mapResultSetToRecurringExpense(ResultSet rs) throws SQLException {
        RecurringExpense expense = new RecurringExpense();
        expense.setId(rs.getLong("id"));
        expense.setCategoryId(rs.getLong("category_id"));
        expense.setAmount(rs.getDouble("amount"));
        expense.setDescription(rs.getString("description"));
        expense.setFrequency(RecurringExpense.Frequency.valueOf(rs.getString("frequency")));
        expense.setStartDate(LocalDate.parse(rs.getString("start_date")));
        
        String endDate = rs.getString("end_date");
        if (endDate != null) {
            expense.setEndDate(LocalDate.parse(endDate));
        }
        
        String lastGenerated = rs.getString("last_generated_date");
        if (lastGenerated != null) {
            expense.setLastGeneratedDate(LocalDate.parse(lastGenerated));
        }
        
        expense.setActive(rs.getBoolean("active"));
        return expense;
    }
}
