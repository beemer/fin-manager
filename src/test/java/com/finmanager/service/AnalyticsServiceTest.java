package com.finmanager.service;

import com.finmanager.model.Category;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

public class AnalyticsServiceTest {
    private AnalyticsService analyticsService;
    private ExpenseService expenseService;
    private CategoryService categoryService;

    @Before
    public void setUp() {
        analyticsService = AnalyticsService.getInstance();
        expenseService = ExpenseService.getInstance();
        categoryService = CategoryService.getInstance();
    }

    @Test
    public void testGetCategoryBreakdown() {
        YearMonth month = YearMonth.now();
        
        Map<String, Double> breakdown = analyticsService.getCategoryBreakdown(month);
        assertNotNull(breakdown);
    }

    @Test
    public void testGetMonthlyTotal() {
        YearMonth month = YearMonth.now();
        
        Double total = analyticsService.getMonthlyTotal(month);
        assertNotNull(total);
        assertTrue(total >= 0);
    }

    @Test
    public void testGetYearlyTrendByCategory() {
        int year = YearMonth.now().getYear();
        
        Map<String, Double> trend = analyticsService.getYearlyTrendByCategory(year);
        assertNotNull(trend);
    }

    @Test
    public void testGetMonthlyTrend() {
        int year = YearMonth.now().getYear();
        
        Map<Integer, Double> trend = analyticsService.getMonthlyTrend(year);
        assertNotNull(trend);
        assertEquals(12, trend.size());
    }

    @Test
    public void testGetYearlyComparison() {
        int year1 = 2023;
        int year2 = 2024;
        
        Map<Integer, Map<String, Double>> comparison = analyticsService.getYearlyComparison(year1, year2);
        assertNotNull(comparison);
        assertEquals(2, comparison.size());
    }
}
