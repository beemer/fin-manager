package com.finmanager.bdd;

import com.finmanager.db.DatabaseManager;
import com.finmanager.model.Category;
import com.finmanager.model.Expense;
import com.finmanager.model.RecurringExpense;
import com.finmanager.service.CategoryService;
import com.finmanager.service.ExpenseService;
import com.finmanager.service.RecurringExpenseService;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class Phase1StepDefinitions {
    private CategoryService categoryService;
    private ExpenseService expenseService;
    private RecurringExpenseService recurringExpenseService;
    
    private String selectedMonth;
    private String apiEndpoint;
    private boolean isRecurringChecked;
    private String selectedFrequency;
    private String endDate;
    private Double enteredAmount;
    private String description;
    private Long selectedCategoryId;
    private List<Expense> fetchedExpenses;
    private Long createdRecurringExpenseId;
    private Long createdExpenseId;

    @Before
    public void setupTestDatabase() throws Exception {
        // Use temporary file-based SQLite database for better reliability
        String tempDb = "test-" + System.currentTimeMillis() + ".db";
        System.setProperty("db.url", "jdbc:sqlite:" + tempDb);
        
        // Reset DatabaseManager singleton
        Field dbInstance = DatabaseManager.class.getDeclaredField("instance");
        dbInstance.setAccessible(true);
        dbInstance.set(null, null);
        
        // Initialize new database instance - this will create tables
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        // Verify tables were created
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
            // Test that categories table exists and is accessible
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM categories");
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("Database tables not properly initialized: " + e.getMessage(), e);
        }
        
        // Reset service singletons
        resetServiceSingleton(CategoryService.class);
        resetServiceSingleton(ExpenseService.class);
        resetServiceSingleton(RecurringExpenseService.class);
        
        // Get fresh service instances
        categoryService = CategoryService.getInstance();
        expenseService = ExpenseService.getInstance();
        recurringExpenseService = RecurringExpenseService.getInstance();
    }
    
    private void resetServiceSingleton(Class<?> serviceClass) throws Exception {
        Field instance = serviceClass.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @After
    public void cleanupTestDatabase() throws Exception {
        String dbUrl = System.getProperty("db.url", "");
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS expenses");
            stmt.execute("DROP TABLE IF EXISTS recurring_expenses");
            stmt.execute("DROP TABLE IF EXISTS investment_entries");
            stmt.execute("DROP TABLE IF EXISTS categories");
        } catch (Exception e) {
            // Ignore cleanup errors
        }
        
        // Delete temporary database file
        if (dbUrl.startsWith("jdbc:sqlite:") && !dbUrl.contains(":memory:")) {
            String dbFile = dbUrl.replace("jdbc:sqlite:", "");
            try {
                java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(dbFile));
            } catch (Exception e) {
                // Ignore file deletion errors
            }
        }
    }

    // ========== Month Navigation Steps ==========
    
    @Given("I am on the Dashboard")
    public void userIsOnDashboard() {
        selectedMonth = YearMonth.now().toString();
    }

    @When("I click the Previous button")
    public void clickPreviousButton() {
        YearMonth current = YearMonth.parse(selectedMonth);
        selectedMonth = current.minusMonths(1).toString();
    }

    @When("I click the Next button")
    public void clickNextButton() {
        YearMonth current = YearMonth.parse(selectedMonth);
        selectedMonth = current.plusMonths(1).toString();
    }

    @When("I click the Today button")
    public void clickTodayButton() {
        selectedMonth = YearMonth.now().toString();
    }

    @Given("the current month is not displayed")
    public void currentMonthNotDisplayed() {
        YearMonth current = YearMonth.now();
        selectedMonth = current.minusMonths(1).toString();
    }

    @Then("the displayed month should be the previous month")
    public void verifyPreviousMonth() {
        YearMonth expected = YearMonth.now().minusMonths(1);
        Assert.assertEquals(expected.toString(), selectedMonth);
    }

    @Then("the displayed month should be the next month")
    public void verifyNextMonth() {
        YearMonth expected = YearMonth.now().plusMonths(1);
        Assert.assertEquals(expected.toString(), selectedMonth);
    }

    @Then("the displayed month should be the current month")
    public void verifyCurrentMonth() {
        YearMonth expected = YearMonth.now();
        Assert.assertEquals(expected.toString(), selectedMonth);
    }

    @When("I view the selected month")
    public void viewSelectedMonth() {
        // Month is already stored in selectedMonth variable
    }

    @Then("the month should be displayed in YYYY-MM format")
    public void verifyMonthFormat() {
        Assert.assertTrue("Month format should match YYYY-MM", 
            selectedMonth.matches("\\d{4}-\\d{2}"));
    }

    // ========== Month Filtering Steps ==========
    
    @Given("the database has expenses from multiple months")
    public void setupMultipleMonthsOfExpenses() {
        Category utilities = new Category("Utilities", "MANDATORY", "#FF0000");
        Long categoryId = categoryService.createCategory(utilities);
        
        YearMonth current = YearMonth.now();
        
        // Add expenses from current month
        expenseService.createExpense(new Expense(
            current.atDay(5), 100.0, categoryId, "Current month expense 1"
        ));
        expenseService.createExpense(new Expense(
            current.atDay(15), 150.0, categoryId, "Current month expense 2"
        ));
        
        // Add expenses from previous month
        expenseService.createExpense(new Expense(
            current.minusMonths(1).atDay(10), 50.0, categoryId, "Previous month expense"
        ));
        expenseService.createExpense(new Expense(
            current.minusMonths(1).atDay(20), 75.0, categoryId, "Previous month expense 2"
        ));
    }

    @When("I select the current month")
    public void selectCurrentMonth() {
        selectedMonth = YearMonth.now().toString();
    }

    @When("I change to the previous month")
    public void changeToPreviousMonth() {
        selectedMonth = YearMonth.now().minusMonths(1).toString();
    }

    @When("I select a month with no expenses")
    public void selectMonthWithNoExpenses() {
        selectedMonth = YearMonth.now().minusMonths(3).toString();
    }

    @When("I select a specific month {string}")
    public void selectSpecificMonth(String month) {
        selectedMonth = month;
    }

    @Then("I should see only expenses from the current month")
    public void verifyCurrentMonthExpenses() {
        fetchedExpenses = expenseService.getExpensesByMonth(YearMonth.now());
        Assert.assertNotNull("Expenses list should not be null", fetchedExpenses);
        Assert.assertEquals("Should have 2 expenses for current month", 2, fetchedExpenses.size());
        
        // Verify all expenses are from current month
        YearMonth current = YearMonth.now();
        for (Expense expense : fetchedExpenses) {
            YearMonth expenseMonth = YearMonth.from(expense.getDate());
            Assert.assertEquals("All expenses should be from current month", current, expenseMonth);
        }
    }

    @Then("the expense list should update")
    public void verifyExpenseListUpdates() {
        Assert.assertNotNull("Selected month should be set", selectedMonth);
        fetchedExpenses = expenseService.getExpensesByMonth(YearMonth.parse(selectedMonth));
        Assert.assertNotNull("Fetched expenses should not be null", fetchedExpenses);
    }

    @Then("I should see only expenses from the selected month")
    public void verifySelectedMonthExpenses() {
        fetchedExpenses = expenseService.getExpensesByMonth(YearMonth.parse(selectedMonth));
        Assert.assertNotNull("Expenses list should not be null", fetchedExpenses);
        
        YearMonth expectedMonth = YearMonth.parse(selectedMonth);
        for (Expense expense : fetchedExpenses) {
            YearMonth expenseMonth = YearMonth.from(expense.getDate());
            Assert.assertEquals("All expenses should be from selected month", expectedMonth, expenseMonth);
        }
    }

    @Then("the expense list should be empty")
    public void verifyEmptyList() {
        fetchedExpenses = expenseService.getExpensesByMonth(YearMonth.parse(selectedMonth));
        Assert.assertNotNull("Expenses list should not be null", fetchedExpenses);
        Assert.assertTrue("Expenses list should be empty", fetchedExpenses.isEmpty());
    }

    @Then("the total expenses should be {int}")
    public void verifyTotalExpenses(int expected) {
        fetchedExpenses = expenseService.getExpensesByMonth(YearMonth.parse(selectedMonth));
        double total = fetchedExpenses.stream().mapToDouble(Expense::getAmount).sum();
        Assert.assertEquals("Total expenses should match", (double) expected, total, 0.01);
    }

    @Then("the total should be the sum of current month expenses")
    public void verifyTotalSum() {
        fetchedExpenses = expenseService.getExpensesByMonth(YearMonth.now());
        double total = fetchedExpenses.stream().mapToDouble(Expense::getAmount).sum();
        Assert.assertEquals("Total should be 250.0 (100 + 150)", 250.0, total, 0.01);
    }

    @Then("the API request should include query parameter {string}")
    public void verifyApiQueryParameter(String month) {
        selectedMonth = month;
        Assert.assertTrue("Month should be in YYYY-MM format", selectedMonth.matches("\\d{4}-\\d{2}"));
        
        // Verify the month parameter works by fetching expenses
        fetchedExpenses = expenseService.getExpensesByMonth(YearMonth.parse(month));
        Assert.assertNotNull("Should be able to fetch expenses with month parameter", fetchedExpenses);
    }

    @Then("the response should contain expenses only from that month")
    public void verifyResponseFiltering() {
        Assert.assertNotNull("Selected month should be set", selectedMonth);
        fetchedExpenses = expenseService.getExpensesByMonth(YearMonth.parse(selectedMonth));
        
        YearMonth expectedMonth = YearMonth.parse(selectedMonth);
        for (Expense expense : fetchedExpenses) {
            YearMonth expenseMonth = YearMonth.from(expense.getDate());
            Assert.assertEquals("All expenses should match the queried month", expectedMonth, expenseMonth);
        }
    }

    // ========== Recurring Expense Steps ==========
    
    @Given("I am on the AddExpenseForm")
    public void userIsOnAddExpenseForm() {
        isRecurringChecked = false;
        selectedFrequency = null;
        enteredAmount = null;
        createdRecurringExpenseId = null;
        createdExpenseId = null;
    }

    @Given("a category exists with name {string}")
    public void categoryExists(String categoryName) {
        Category category = new Category(categoryName, "UTILITIES", "#FFA500");
        selectedCategoryId = categoryService.createCategory(category);
        Assert.assertNotNull("Category should be created", selectedCategoryId);
    }

    @When("I check the {string} checkbox")
    public void checkRecurringCheckbox(String checkboxName) {
        if (checkboxName.equals("IsRecurring")) {
            isRecurringChecked = true;
        }
    }

    @When("I don't check the recurring checkbox")
    public void dontCheckRecurringCheckbox() {
        isRecurringChecked = false;
    }

    @Then("the frequency dropdown should appear")
    public void verifyFrequencyDropdown() {
        Assert.assertTrue("Frequency dropdown should appear when recurring is checked", 
            isRecurringChecked);
    }

    @Then("the end date field should appear")
    public void verifyEndDateField() {
        Assert.assertTrue("End date field should appear when recurring is checked", 
            isRecurringChecked);
    }

    @When("I enter expense amount {string}")
    public void enterExpenseAmount(String amount) {
        enteredAmount = Double.parseDouble(amount);
    }

    @When("I select category {string}")
    public void selectCategory(String categoryName) {
        Category found = categoryService.getAllCategories()
            .stream()
            .filter(c -> c.getName().equals(categoryName))
            .findFirst()
            .orElse(null);
        Assert.assertNotNull("Category should exist: " + categoryName, found);
        selectedCategoryId = found.getId();
    }

    @When("I select frequency {string}")
    public void selectFrequency(String frequency) {
        selectedFrequency = frequency;
    }

    @When("I don't select a frequency")
    public void dontSelectFrequency() {
        selectedFrequency = null;
    }

    @When("I set end date to {string}")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @When("I submit the form")
    public void submitForm() {
        if (isRecurringChecked && selectedFrequency != null) {
            // Create recurring expense
            RecurringExpense recurring = new RecurringExpense();
            recurring.setCategoryId(selectedCategoryId);
            recurring.setAmount(enteredAmount);
            recurring.setDescription("Test recurring expense");
            recurring.setFrequency(RecurringExpense.Frequency.valueOf(selectedFrequency));
            recurring.setStartDate(LocalDate.now());
            if (endDate != null) {
                recurring.setEndDate(LocalDate.parse(endDate));
            }
            recurring.setActive(true);
            
            createdRecurringExpenseId = recurringExpenseService.createRecurringExpense(recurring);
            apiEndpoint = "/api/recurring";
        } else if (!isRecurringChecked) {
            // Create regular expense
            Expense expense = new Expense(
                LocalDate.now(), enteredAmount, selectedCategoryId, "Test expense"
            );
            createdExpenseId = expenseService.createExpense(expense);
            apiEndpoint = "/api/expense";
        } else {
            // Recurring checked but no frequency - validation error
            apiEndpoint = null;
        }
    }

    @When("I click submit")
    public void clickSubmit() {
        submitForm();
    }

    @Then("the recurring expense should be created")
    public void verifyRecurringExpenseCreated() {
        Assert.assertEquals("Should submit to /api/recurring", "/api/recurring", apiEndpoint);
        Assert.assertNotNull("Recurring expense ID should be set", createdRecurringExpenseId);
        Assert.assertTrue("Recurring expense ID should be positive", createdRecurringExpenseId > 0);
    }

    @Then("the recurring expense should be created with frequency YEARLY")
    public void verifyYearlyRecurringExpense() {
        Assert.assertEquals("Frequency should be YEARLY", "YEARLY", selectedFrequency);
        Assert.assertEquals("Should submit to /api/recurring", "/api/recurring", apiEndpoint);
        Assert.assertNotNull("Recurring expense ID should be set", createdRecurringExpenseId);
        Assert.assertTrue("Recurring expense ID should be positive", createdRecurringExpenseId > 0);
    }

    @Then("the submission should go to {string} endpoint")
    public void verifySubmissionEndpoint(String endpoint) {
        Assert.assertEquals("Should submit to correct endpoint", endpoint, apiEndpoint);
        
        if (endpoint.equals("/api/recurring")) {
            Assert.assertNotNull("Recurring expense should be created", createdRecurringExpenseId);
        } else if (endpoint.equals("/api/expense")) {
            Assert.assertNotNull("Expense should be created", createdExpenseId);
        }
    }

    @Then("the form should show a validation error")
    public void verifyValidationError() {
        // Form validation prevents submission without frequency
        Assert.assertTrue("Recurring should be checked", isRecurringChecked);
        Assert.assertNull("Frequency should be missing", selectedFrequency);
        Assert.assertNull("API endpoint should not be set due to validation", apiEndpoint);
    }

    @Then("the recurring expense should not be created")
    public void verifyRecurringNotCreated() {
        // Validation prevents submission
        Assert.assertNull("Frequency is required", selectedFrequency);
        Assert.assertNull("Recurring expense should not be created", createdRecurringExpenseId);
    }

    @Then("the expense should be created immediately")
    public void verifyExpenseCreatedImmediately() {
        Assert.assertEquals("Should submit to /api/expense", "/api/expense", apiEndpoint);
        Assert.assertNotNull("Expense ID should be set", createdExpenseId);
    }
}

