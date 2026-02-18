package com.finmanager.api;

import com.finmanager.service.AnalyticsService;
import com.finmanager.util.Logger;
import com.finmanager.util.GsonUtil;
import com.google.gson.Gson;

import java.time.YearMonth;
import java.util.Map;

public class AnalyticsAPI {
    private final AnalyticsService analyticsService;
    private final Gson gson;

    public AnalyticsAPI() {
        this.analyticsService = AnalyticsService.getInstance();
        this.gson = GsonUtil.getInstance();
    }

    public String getCategoryBreakdown(String yearMonth) {
        try {
            YearMonth ym = YearMonth.parse(yearMonth);
            Map<String, Double> breakdown = analyticsService.getCategoryBreakdown(ym);
            return gson.toJson(breakdown);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Invalid month format"));
        }
    }

    public String getYearlyTrendByCategory(int year) {
        try {
            Map<String, Double> trend = analyticsService.getYearlyTrendByCategory(year);
            return gson.toJson(trend);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to get yearly trend: " + e.getMessage()));
        }
    }

    public String getMonthlyTotal(String yearMonth) {
        try {
            YearMonth ym = YearMonth.parse(yearMonth);
            Double total = analyticsService.getMonthlyTotal(ym);
            return gson.toJson(new MonthlyTotalResponse(total));
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Invalid month format"));
        }
    }

    public String getMonthlyTrend(int year) {
        try {
            Map<Integer, Double> trend = analyticsService.getMonthlyTrend(year);
            return gson.toJson(trend);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to get monthly trend: " + e.getMessage()));
        }
    }

    public String getYearlyComparison(int year1, int year2) {
        try {
            Map<Integer, Map<String, Double>> comparison = analyticsService.getYearlyComparison(year1, year2);
            return gson.toJson(comparison);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to get yearly comparison: " + e.getMessage()));
        }
    }

    public static class MonthlyTotalResponse {
        public Double total;

        public MonthlyTotalResponse(Double total) {
            this.total = total;
        }
    }
}
