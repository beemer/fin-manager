package com.finmanager.service;

import com.finmanager.model.InvestmentEntry;
import com.finmanager.db.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

public class InvestmentService {
    private static InvestmentService instance;
    private static final Long INVESTMENTS_CATEGORY_ID = 5L;

    private InvestmentService() {}

    public static InvestmentService getInstance() {
        if (instance == null) {
            instance = new InvestmentService();
        }
        return instance;
    }

    public List<InvestmentEntry> getAllInvestments() {
        List<InvestmentEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM investment_entries ORDER BY date DESC";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                entries.add(mapResultSetToInvestmentEntry(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public List<InvestmentEntry> getInvestmentsByYear(Year year) {
        LocalDate startDate = LocalDate.of(year.getValue(), 1, 1);
        LocalDate endDate = LocalDate.of(year.getValue(), 12, 31);
        String sql = "SELECT * FROM investment_entries WHERE date >= ? AND date <= ? ORDER BY date DESC";

        List<InvestmentEntry> entries = new ArrayList<>();
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entries.add(mapResultSetToInvestmentEntry(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public Long createInvestmentEntry(InvestmentEntry entry) {
        String sql = "INSERT INTO investment_entries(date, amount, currency, exchange_rate, description, is_recurring) " +
                     "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, entry.getDate().toString());
            pstmt.setDouble(2, entry.getAmount());
            pstmt.setString(3, entry.getCurrency());
            pstmt.setDouble(4, entry.getExchangeRate());
            pstmt.setString(5, entry.getDescription());
            pstmt.setBoolean(6, entry.isRecurring());
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

    public void updateInvestmentEntry(InvestmentEntry entry) {
        String sql = "UPDATE investment_entries SET date = ?, amount = ?, currency = ?, exchange_rate = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entry.getDate().toString());
            pstmt.setDouble(2, entry.getAmount());
            pstmt.setString(3, entry.getCurrency());
            pstmt.setDouble(4, entry.getExchangeRate());
            pstmt.setString(5, entry.getDescription());
            pstmt.setLong(6, entry.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteInvestmentEntry(Long id) {
        String sql = "DELETE FROM investment_entries WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Double getTotalInvestmentsByYear(Year year) {
        LocalDate startDate = LocalDate.of(year.getValue(), 1, 1);
        LocalDate endDate = LocalDate.of(year.getValue(), 12, 31);
        String sql = "SELECT COALESCE(SUM(amount * exchange_rate), 0) as total FROM investment_entries WHERE date >= ? AND date <= ?";

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

    public Double calculateCAGR(Double startValue, Double endValue, int years) {
        if (startValue <= 0 || years <= 0) return 0.0;
        return (Math.pow(endValue / startValue, 1.0 / years) - 1) * 100;
    }

    public Double projectInvestmentValue(Double currentValue, Double annualContribution, Double expectedAnnualReturn, int years) {
        double projectedValue = currentValue;
        for (int i = 0; i < years; i++) {
            projectedValue = (projectedValue + annualContribution) * (1 + expectedAnnualReturn / 100);
        }
        return projectedValue;
    }

    private InvestmentEntry mapResultSetToInvestmentEntry(ResultSet rs) throws SQLException {
        InvestmentEntry entry = new InvestmentEntry();
        entry.setId(rs.getLong("id"));
        entry.setDate(LocalDate.parse(rs.getString("date")));
        entry.setAmount(rs.getDouble("amount"));
        entry.setCurrency(rs.getString("currency"));
        entry.setExchangeRate(rs.getDouble("exchange_rate"));
        entry.setDescription(rs.getString("description"));
        entry.setRecurring(rs.getBoolean("is_recurring"));
        return entry;
    }
}
