package com.finmanager.bdd;

import com.finmanager.model.Category;
import com.finmanager.model.Expense;
import com.finmanager.model.RecurringExpense;
import com.finmanager.service.CategoryService;
import com.finmanager.service.ExpenseService;
import com.finmanager.service.RecurringExpenseGenerator;
import com.finmanager.service.RecurringExpenseService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class Phase2StepDefinitions {
    private final CategoryService categoryService = CategoryService.getInstance();
    private final RecurringExpenseService recurringExpenseService = RecurringExpenseService.getInstance();
    private final RecurringExpenseGenerator generator = RecurringExpenseGenerator.getInstance();
    private final ExpenseService expenseService = ExpenseService.getInstance();
    
    private Long currentRecurringId;
    private int lastGeneratedCount;
    private int totalGeneratedCount;

    @Given("a monthly recurring expense exists:")
    public void createMonthlyRecurring(Map<String, String> data) {
        createRecurring(data, RecurringExpense.Frequency.MONTHLY);
    }

    @Given("a yearly recurring expense exists:")
    public void createYearlyRecurring(Map<String, String> data) {
        createRecurring(data, RecurringExpense.Frequency.YEARLY);
    }

    private void createRecurring(Map<String, String> data, RecurringExpense.Frequency frequency) {
        Category sub = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals("Subscriptions"))
                .findFirst()
                .orElseGet(() -> {
                    Category c = new Category("Subscriptions", "MANDATORY", "#0000FF");
                    Long id = categoryService.createCategory(c);
                    c.setId(id);
                    return c;
                });

        RecurringExpense rec = new RecurringExpense();
        rec.setCategoryId(sub.getId());
        rec.setDescription(data.get("description"));
        rec.setAmount(Double.parseDouble(data.get("amount")));
        rec.setFrequency(frequency);
        rec.setStartDate(LocalDate.parse(data.get("startDate")));
        if (data.containsKey("endDate")) {
            rec.setEndDate(LocalDate.parse(data.get("endDate")));
        }
        rec.setActive(true);
        currentRecurringId = recurringExpenseService.createRecurringExpense(rec);
    }

    @When("I trigger manual generation up to {string}")
    public void triggerGeneration(String dateStr) {
        LocalDate upTo = LocalDate.parse(dateStr);
        lastGeneratedCount = generator.generateForRecurring(currentRecurringId, upTo);
        totalGeneratedCount += lastGeneratedCount;
    }

    @Then("{int} expense instances should be created")
    public void verifyCreatedCount(int expected) {
        Assert.assertEquals("Generated count should match", expected, lastGeneratedCount);
    }

    @Then("{int} additional expense instances should be created")
    public void verifyAdditionalCreatedCount(int expected) {
        Assert.assertEquals("Additional generated count should match", expected, lastGeneratedCount);
    }

    @Then("the expenses for {string} should include {string}")
    public void verifyExpenseInMonth(String monthStr, String description) {
        YearMonth ym = YearMonth.parse(monthStr);
        List<Expense> expenses = expenseService.getExpensesByMonth(ym);
        boolean found = expenses.stream()
                .anyMatch(e -> e.getDescription().equals(description) && e.isRecurringInstance());
        Assert.assertTrue("Expense '" + description + "' should be found in " + monthStr, found);
    }

    @Then("the expenses for {string} should not include {string}")
    public void verifyExpenseNotInMonth(String monthStr, String description) {
        YearMonth ym = YearMonth.parse(monthStr);
        List<Expense> expenses = expenseService.getExpensesByMonth(ym);
        boolean found = expenses.stream()
                .anyMatch(e -> e.getDescription().equals(description));
        Assert.assertFalse("Expense '" + description + "' should NOT be found in " + monthStr, found);
    }
}
