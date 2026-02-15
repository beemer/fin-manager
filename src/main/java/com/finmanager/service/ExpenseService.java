package com.finmanager.service;

import com.finmanager.model.Expense;
import com.finmanager.db.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class ExpenseService {
    private static ExpenseService instance;

    private ExpenseService() {}

    public static ExpenseService getInstance() {
        if (instance == null) {
            instance = new ExpenseService();
        }
        return instance;
    }

    public List<Expense> getExpensesByMonth(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return getExpensesByDateRange(startDate, endDate);
    }

    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE date >= ? AND date <= ? ORDER BY date DESC";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(mapResultSetToExpense(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public List<Expense> getExpensesByCategory(Long categoryId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        String sql = "SELECT * FROM expenses WHERE category_id = ? AND date >= ? AND date <= ? ORDER BY date DESC";

        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, categoryId);
            pstmt.setString(2, startDate.toString());
            pstmt.setString(3, endDate.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(mapResultSetToExpense(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public Long createExpense(Expense expense) {
        String sql = "INSERT INTO expenses(date, amount, category_id, description, is_recurring_instance) " +
                     "VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, expense.getDate().toString());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setLong(3, expense.getCategoryId());
            pstmt.setString(4, expense.getDescription());
            pstmt.setBoolean(5, expense.isRecurringInstance());
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

    public void updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET date = ?, amount = ?, category_id = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, expense.getDate().toString());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setLong(3, expense.getCategoryId());
            pstmt.setString(4, expense.getDescription());
            pstmt.setLong(5, expense.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExpense(Long id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Double getTotalExpensesByMonth(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return getTotalExpensesByDateRange(startDate, endDate);
    }

    public Double getTotalExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM expenses WHERE date >= ? AND date <= ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Double getTotalExpensesByCategoryAndMonth(Long categoryId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM expenses WHERE category_id = ? AND date >= ? AND date <= ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, categoryId);
            pstmt.setString(2, startDate.toString());
            pstmt.setString(3, endDate.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private Expense mapResultSetToExpense(ResultSet rs) throws SQLException {
        Expense expense = new Expense();
        expense.setId(rs.getLong("id"));
        expense.setDate(LocalDate.parse(rs.getString("date")));
        expense.setAmount(rs.getDouble("amount"));
        expense.setCategoryId(rs.getLong("category_id"));
        expense.setDescription(rs.getString("description"));
        expense.setRecurringInstance(rs.getBoolean("is_recurring_instance"));
        return expense;
    }
}
