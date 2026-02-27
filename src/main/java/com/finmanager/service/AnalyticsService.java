package com.finmanager.service;

import com.finmanager.model.Category;
import com.finmanager.model.Expense;

import java.time.YearMonth;
import java.util.*;

public class AnalyticsService {
    private static AnalyticsService instance;
    private final CategoryService categoryService;
    private final ExpenseService expenseService;

    private AnalyticsService() {
        this.categoryService = CategoryService.getInstance();
        this.expenseService = ExpenseService.getInstance();
    }

    public static AnalyticsService getInstance() {
        if (instance == null) {
            instance = new AnalyticsService();
        }
        return instance;
    }

    public Map<String, Object> getCategoryBreakdown(YearMonth yearMonth) {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Double> breakdown = new LinkedHashMap<>();
        Map<String, String> colors = new HashMap<>();
        
        List<Category> categories = categoryService.getAllCategories();
        for (Category category : categories) {
            Double total = expenseService.getTotalExpensesByCategoryAndMonth(category.getId(), yearMonth);
            if (total > 0) {
                breakdown.put(category.getName(), total);
                colors.put(category.getName(), category.getColor());
            }
        }
        
        response.put("breakdown", breakdown);
        response.put("colors", colors);
        return response;
    }

    public Map<String, Double> getYearlyTrendByCategory(int year) {
        Map<String, Double> trend = new LinkedHashMap<>();
        
        List<Category> categories = categoryService.getAllCategories();
        for (Category category : categories) {
            Double yearTotal = 0.0;
            for (int month = 1; month <= 12; month++) {
                YearMonth ym = YearMonth.of(year, month);
                yearTotal += expenseService.getTotalExpensesByCategoryAndMonth(category.getId(), ym);
            }
            if (yearTotal > 0) {
                trend.put(category.getName(), yearTotal);
            }
        }
        
        return trend;
    }

    public Double getTotalByCategory(Long categoryId, YearMonth yearMonth) {
        return expenseService.getTotalExpensesByCategoryAndMonth(categoryId, yearMonth);
    }

    public List<Expense> getExpensesByCategory(Long categoryId, YearMonth yearMonth) {
        return expenseService.getExpensesByCategory(categoryId, yearMonth);
    }

    public Double getMonthlyTotal(YearMonth yearMonth) {
        return expenseService.getTotalExpensesByMonth(yearMonth);
    }

    public Map<Integer, Double> getMonthlyTrend(int year) {
        Map<Integer, Double> trend = new LinkedHashMap<>();
        
        for (int month = 1; month <= 12; month++) {
            YearMonth ym = YearMonth.of(year, month);
            Double total = expenseService.getTotalExpensesByMonth(ym);
            trend.put(month, total);
        }
        
        return trend;
    }

    public Map<Integer, Map<String, Double>> getYearlyComparison(int year1, int year2) {
        Map<Integer, Map<String, Double>> comparison = new LinkedHashMap<>();
        comparison.put(year1, getYearlyTrendByCategory(year1));
        comparison.put(year2, getYearlyTrendByCategory(year2));
        return comparison;
    }
}
