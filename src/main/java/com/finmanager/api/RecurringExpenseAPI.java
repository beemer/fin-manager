package com.finmanager.api;

import com.finmanager.model.RecurringExpense;
import com.finmanager.service.RecurringExpenseService;
import com.finmanager.service.RecurringExpenseGenerator;
import com.finmanager.util.Logger;
import com.finmanager.util.GsonUtil;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecurringExpenseAPI {
    private final RecurringExpenseService recurringService;
    private final RecurringExpenseGenerator generator;
    private final Gson gson;

    public RecurringExpenseAPI() {
        this.recurringService = RecurringExpenseService.getInstance();
        this.generator = RecurringExpenseGenerator.getInstance();
        this.gson = GsonUtil.getInstance();
    }

    public String getAllRecurringExpenses() {
        Logger.debug(RecurringExpenseAPI.class, "getAllRecurringExpenses() called");
        try {
            List<RecurringExpense> expenses = recurringService.getAllRecurringExpenses();
            Logger.debug(RecurringExpenseAPI.class, "  → Found " + expenses.size() + " recurring expenses");
            return gson.toJson(expenses);
        } catch (Exception e) {
            Logger.error(RecurringExpenseAPI.class, "Error fetching recurring expenses", e);
            return gson.toJson(new CategoryAPI.ApiError("Failed to fetch recurring expenses: " + e.getMessage()));
        }
    }

    public String createRecurringExpense(String json) {
        Logger.debug(RecurringExpenseAPI.class, "createRecurringExpense() called");
        try {
            RecurringExpense expense = gson.fromJson(json, RecurringExpense.class);
            Long id = recurringService.createRecurringExpense(expense);
            expense.setId(id);
            Logger.debug(RecurringExpenseAPI.class, "  → Created recurring expense with id: " + id);
            return gson.toJson(expense);
        } catch (Exception e) {
            Logger.error(RecurringExpenseAPI.class, "Error creating recurring expense", e);
            return gson.toJson(new CategoryAPI.ApiError("Failed to create recurring expense: " + e.getMessage()));
        }
    }

    public String updateRecurringExpense(String json) {
        Logger.debug(RecurringExpenseAPI.class, "updateRecurringExpense() called");
        try {
            RecurringExpense expense = gson.fromJson(json, RecurringExpense.class);
            recurringService.updateRecurringExpense(expense);
            Logger.debug(RecurringExpenseAPI.class, "  → Updated recurring expense with id: " + expense.getId());
            return gson.toJson(expense);
        } catch (Exception e) {
            Logger.error(RecurringExpenseAPI.class, "Error updating recurring expense", e);
            return gson.toJson(new CategoryAPI.ApiError("Failed to update recurring expense: " + e.getMessage()));
        }
    }

    public String deleteRecurringExpense(Long id) {
        Logger.debug(RecurringExpenseAPI.class, "deleteRecurringExpense() called for id: " + id);
        try {
            recurringService.deleteRecurringExpense(id);
            Logger.debug(RecurringExpenseAPI.class, "  → Deleted recurring expense with id: " + id);
            return gson.toJson(new CategoryAPI.ApiResponse("Recurring expense deleted successfully"));
        } catch (Exception e) {
            Logger.error(RecurringExpenseAPI.class, "Error deleting recurring expense", e);
            return gson.toJson(new CategoryAPI.ApiError("Failed to delete recurring expense: " + e.getMessage()));
        }
    }

    public String generateForRecurring(Long id) {
        Logger.debug(RecurringExpenseAPI.class, "generateForRecurring() called for id: " + id);
        try {
            int generated = generator.generateForRecurring(id, LocalDate.now());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Generated " + generated + " expense instances");
            response.put("count", generated);
            response.put("recurringId", id);
            Logger.debug(RecurringExpenseAPI.class, "  → Generated " + generated + " instances for recurring expense " + id);
            return gson.toJson(response);
        } catch (Exception e) {
            Logger.error(RecurringExpenseAPI.class, "Error generating recurring expenses", e);
            return gson.toJson(new CategoryAPI.ApiError("Failed to generate recurring expenses: " + e.getMessage()));
        }
    }

    public String generateAll() {
        Logger.debug(RecurringExpenseAPI.class, "generateAll() called");
        try {
            int generated = generator.generateAllRecurring(LocalDate.now());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Generated " + generated + " expense instances");
            response.put("count", generated);
            response.put("timestamp", LocalDate.now().toString());
            Logger.debug(RecurringExpenseAPI.class, "  → Generated " + generated + " instances total");
            return gson.toJson(response);
        } catch (Exception e) {
            Logger.error(RecurringExpenseAPI.class, "Error generating all recurring expenses", e);
            return gson.toJson(new CategoryAPI.ApiError("Failed to generate recurring expenses: " + e.getMessage()));
        }
    }
}
