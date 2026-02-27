package com.finmanager.bdd;

import com.finmanager.model.Category;
import com.finmanager.model.Expense;
import com.finmanager.service.CategoryService;
import com.finmanager.service.ExpenseService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Phase3StepDefinitions {
    private final CategoryService categoryService = CategoryService.getInstance();
    private final ExpenseService expenseService = ExpenseService.getInstance();
    
    private String lastErrorMessage;
    private Long lastCategoryId;

    @Given("the application is running")
    public void applicationRunning() {
        // Handled by Before hook in Phase1StepDefinitions
    }

    @Given("I am logged in")
    public void loggedIn() {
        // Dummy step for now
    }

    @Given("the following categories exist:")
    public void categoriesExist(List<Map<String, String>> data) {
        for (Map<String, String> row : data) {
            Category c = new Category(row.get("name"), row.get("type"), row.get("color"));
            categoryService.createCategory(c);
        }
    }

    @When("I open Category Management")
    public void openCategoryManagement() {
        // In backend tests, this is a no-op or state reset
    }

    @Then("I should see a category named {string} with color {string}")
    public void verifyCategoryColor(String name, String color) {
        Category c = findCategoryByName(name);
        Assert.assertNotNull("Category " + name + " should exist", c);
        Assert.assertEquals(color, c.getColor());
    }

    @Then("the type for {string} should be {string}")
    public void verifyCategoryType(String name, String type) {
        Category c = findCategoryByName(name);
        Assert.assertNotNull("Category " + name + " should exist", c);
        Assert.assertEquals(type, c.getType());
    }

    @When("I create a new category:")
    public void createCategory(List<Map<String, String>> data) {
        Map<String, String> row = data.get(0);
        Category c = new Category(row.get("name"), row.get("type"), row.get("color"));
        lastCategoryId = categoryService.createCategory(c);
    }

    @Then("the category {string} should be visible in the list")
    public void categoryVisible(String name) {
        Category c = findCategoryByName(name);
        Assert.assertNotNull("Category " + name + " should be visible", c);
    }

    @Then("its type should be {string}")
    public void verifyLastCategoryType(String type) {
        Category c = categoryService.getCategoryById(lastCategoryId);
        Assert.assertEquals(type, c.getType());
    }

    @Given("a category {string} exists with type {string} and color {string}")
    public void categoryExists(String name, String type, String color) {
        Category c = new Category(name, type, color);
        categoryService.createCategory(c);
    }

    @When("I edit the category {string} to:")
    public void editCategory(String oldName, List<Map<String, String>> data) {
        Category c = findCategoryByName(oldName);
        Assert.assertNotNull(c);
        Map<String, String> row = data.get(0);
        c.setName(row.get("name"));
        c.setType(row.get("type"));
        c.setColor(row.get("color"));
        categoryService.updateCategory(c);
        lastCategoryId = c.getId();
    }

    @Then("the category {string} should be visible")
    public void categoryShouldBeVisible(String name) {
        Assert.assertNotNull(findCategoryByName(name));
    }

    @Then("the category {string} should not be visible")
    public void categoryShouldNotBeVisible(String name) {
        Assert.assertNull(findCategoryByName(name));
    }

    @Then("its color should be {string}")
    public void verifyLastCategoryColor(String color) {
        Category c = categoryService.getCategoryById(lastCategoryId);
        Assert.assertEquals(color, c.getColor());
    }

    @Given("a category {string} exists with no expenses")
    public void categoryExistsNoExpenses(String name) {
        Category c = new Category(name, "MANDATORY", "#000000");
        categoryService.createCategory(c);
    }

    @When("I delete the category {string}")
    public void deleteCategory(String name) throws SQLException {
        Category c = findCategoryByName(name);
        Assert.assertNotNull(c);
        categoryService.deleteCategory(c.getId());
    }

    @Then("the category {string} should be removed from the list")
    public void categoryRemoved(String name) {
        Assert.assertNull(findCategoryByName(name));
    }

    @Given("a category {string} exists")
    public void categoryExistsSimple(String name) {
        Category c = new Category(name, "MANDATORY", "#000000");
        categoryService.createCategory(c);
    }

    @Given("it has {int} expenses recorded")
    public void addExpensesToCategory(int count) {
        List<Category> all = categoryService.getAllCategories();
        Category last = all.get(all.size() - 1);
        for (int i = 0; i < count; i++) {
            expenseService.createExpense(new Expense(LocalDate.now(), 10.0, last.getId(), "Test " + i));
        }
    }

    @When("I try to delete the category {string}")
    public void tryDeleteCategory(String name) {
        Category c = findCategoryByName(name);
        Assert.assertNotNull(c);
        try {
            categoryService.deleteCategory(c.getId());
            lastErrorMessage = null;
        } catch (SQLException e) {
            lastErrorMessage = e.getMessage();
        }
    }

    @Then("I should see an error message {string}")
    public void verifyError(String expected) {
        Assert.assertNotNull("Error message should not be null", lastErrorMessage);
        Assert.assertTrue("Error message should contain: " + expected, lastErrorMessage.contains(expected));
    }

    @Then("the category {string} should still be in the list")
    public void categoryStillPresent(String name) {
        Assert.assertNotNull(findCategoryByName(name));
    }

    private Category findCategoryByName(String name) {
        return categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
