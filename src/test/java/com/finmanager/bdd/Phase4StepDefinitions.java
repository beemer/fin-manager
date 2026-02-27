package com.finmanager.bdd;

import com.finmanager.model.Category;
import com.finmanager.model.Expense;
import com.finmanager.service.AnalyticsService;
import com.finmanager.service.CategoryService;
import com.finmanager.service.ExpenseService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class Phase4StepDefinitions {
    private final CategoryService categoryService = CategoryService.getInstance();
    private final ExpenseService expenseService = ExpenseService.getInstance();
    private final AnalyticsService analyticsService = AnalyticsService.getInstance();
    
    private Map<String, Object> lastBreakdown;
    private Map<Integer, Double> lastTrend;

    @Given("the following expenses exist for {string}:")
    public void expensesExistForMonth(String monthStr, List<Map<String, String>> data) {
        YearMonth ym = YearMonth.parse(monthStr);
        for (Map<String, String> row : data) {
            Category c = findCategoryByName(row.get("category"));
            Expense e = new Expense(ym.atDay(1), Double.parseDouble(row.get("amount")), c.getId(), row.get("description"));
            expenseService.createExpense(e);
        }
    }

    @Given("the following expenses exist:")
    public void expensesExist(List<Map<String, String>> data) {
        for (Map<String, String> row : data) {
            Category c = findCategoryByName(row.get("category"));
            Expense e = new Expense(LocalDate.parse(row.get("date")), Double.parseDouble(row.get("amount")), c.getId(), "Test");
            expenseService.createExpense(e);
        }
    }

    @When("I view the analytics for {string}")
    public void viewAnalytics(String monthStr) {
        YearMonth ym = YearMonth.parse(monthStr);
        lastBreakdown = analyticsService.getCategoryBreakdown(ym);
    }

    @Then("the category breakdown should include:")
    public void verifyBreakdown(List<Map<String, String>> data) {
        Map<String, Double> breakdown = (Map<String, Double>) lastBreakdown.get("breakdown");
        Map<String, String> colors = (Map<String, String>) lastBreakdown.get("colors");
        
        for (Map<String, String> row : data) {
            String name = row.get("category");
            Double total = Double.parseDouble(row.get("total"));
            String color = row.get("color");
            
            Assert.assertTrue("Breakdown should contain " + name, breakdown.containsKey(name));
            Assert.assertEquals(total, breakdown.get(name), 0.01);
            Assert.assertEquals(color, colors.get(name));
        }
    }

    @Then("{string} should not be in the breakdown")
    public void verifyNotInBreakdown(String name) {
        Map<String, Double> breakdown = (Map<String, Double>) lastBreakdown.get("breakdown");
        Assert.assertFalse("Breakdown should not contain " + name, breakdown.containsKey(name));
    }

    @When("I view the monthly trend for {string}")
    public void viewTrend(String yearStr) {
        lastTrend = analyticsService.getMonthlyTrend(Integer.parseInt(yearStr));
    }

    @Then("the trend should show for {string} the amount {double}")
    public void verifyTrend(String monthKey, Double amount) {
        Integer key = Integer.parseInt(monthKey);
        Assert.assertTrue("Trend should contain month " + monthKey, lastTrend.containsKey(key));
        Assert.assertEquals(amount, lastTrend.get(key), 0.01);
    }

    private Category findCategoryByName(String name) {
        return categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Category not found: " + name));
    }
}
