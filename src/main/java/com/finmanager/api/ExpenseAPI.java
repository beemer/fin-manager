package com.finmanager.api;

import com.finmanager.model.Expense;
import com.finmanager.service.ExpenseService;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class ExpenseAPI {
    private final ExpenseService expenseService;
    private final Gson gson;

    public ExpenseAPI() {
        this.expenseService = ExpenseService.getInstance();
        this.gson = new Gson();
    }

    public String getExpensesByMonth(String yearMonth) {
        try {
            YearMonth ym = YearMonth.parse(yearMonth);
            List<Expense> expenses = expenseService.getExpensesByMonth(ym);
            return gson.toJson(expenses);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Invalid month format: " + yearMonth));
        }
    }

    public String getExpensesByDateRange(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<Expense> expenses = expenseService.getExpensesByDateRange(start, end);
            return gson.toJson(expenses);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Invalid date format"));
        }
    }

    public String createExpense(String json) {
        try {
            Expense expense = gson.fromJson(json, Expense.class);
            Long id = expenseService.createExpense(expense);
            expense.setId(id);
            return gson.toJson(expense);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to create expense: " + e.getMessage()));
        }
    }

    public String updateExpense(String json) {
        try {
            Expense expense = gson.fromJson(json, Expense.class);
            expenseService.updateExpense(expense);
            return gson.toJson(expense);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to update expense: " + e.getMessage()));
        }
    }

    public String deleteExpense(Long id) {
        try {
            expenseService.deleteExpense(id);
            return gson.toJson(new CategoryAPI.ApiResponse("Expense deleted successfully"));
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to delete expense: " + e.getMessage()));
        }
    }

    public String getTotalExpensesByMonth(String yearMonth) {
        try {
            YearMonth ym = YearMonth.parse(yearMonth);
            Double total = expenseService.getTotalExpensesByMonth(ym);
            return gson.toJson(new TotalResponse(total));
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Invalid month format"));
        }
    }

    public static class TotalResponse {
        public Double total;

        public TotalResponse(Double total) {
            this.total = total;
        }
    }
}
