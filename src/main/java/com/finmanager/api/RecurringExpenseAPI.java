package com.finmanager.api;

import com.finmanager.model.RecurringExpense;
import com.finmanager.service.RecurringExpenseService;
import com.google.gson.Gson;

import java.util.List;

public class RecurringExpenseAPI {
    private final RecurringExpenseService recurringService;
    private final Gson gson;

    public RecurringExpenseAPI() {
        this.recurringService = RecurringExpenseService.getInstance();
        this.gson = new Gson();
    }

    public String getAllRecurringExpenses() {
        List<RecurringExpense> expenses = recurringService.getAllRecurringExpenses();
        return gson.toJson(expenses);
    }

    public String createRecurringExpense(String json) {
        try {
            RecurringExpense expense = gson.fromJson(json, RecurringExpense.class);
            Long id = recurringService.createRecurringExpense(expense);
            expense.setId(id);
            return gson.toJson(expense);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to create recurring expense: " + e.getMessage()));
        }
    }

    public String updateRecurringExpense(String json) {
        try {
            RecurringExpense expense = gson.fromJson(json, RecurringExpense.class);
            recurringService.updateRecurringExpense(expense);
            return gson.toJson(expense);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to update recurring expense: " + e.getMessage()));
        }
    }

    public String deleteRecurringExpense(Long id) {
        try {
            recurringService.deleteRecurringExpense(id);
            return gson.toJson(new CategoryAPI.ApiResponse("Recurring expense deleted successfully"));
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to delete recurring expense: " + e.getMessage()));
        }
    }
}
