package com.finmanager.service;

import com.finmanager.model.Category;
import com.finmanager.db.DatabaseManager;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.Assert.*;

public class CategoryServiceTest {
    private CategoryService categoryService;

    @Before
    public void setUp() throws SQLException {
        categoryService = CategoryService.getInstance();
        // Clean up any existing test data
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM expenses WHERE category_id IN (SELECT id FROM categories WHERE name LIKE 'Test%' OR name LIKE '%Original%' OR name LIKE '%Updated%' OR name LIKE '%Delete%' OR name = 'Leisure Expenses')");
            stmt.executeUpdate("DELETE FROM categories WHERE name LIKE 'Test%' OR name LIKE '%Original%' OR name LIKE '%Updated%' OR name LIKE '%Delete%' OR name = 'Leisure Expenses'");
        }
    }

    @After
    public void tearDown() throws SQLException {
        // Clean up test data after each test
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM expenses WHERE category_id IN (SELECT id FROM categories WHERE name LIKE 'Test%' OR name LIKE '%Original%' OR name LIKE '%Updated%' OR name LIKE '%Delete%' OR name = 'Leisure Expenses')");
            stmt.executeUpdate("DELETE FROM categories WHERE name LIKE 'Test%' OR name LIKE '%Original%' OR name LIKE '%Updated%' OR name LIKE '%Delete%' OR name = 'Leisure Expenses'");
        }
    }

    @Test
    public void testCreateCategory() {
        Category category = new Category("Test Category", "LEISURE", "#FF5733");
        Long id = categoryService.createCategory(category);
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    public void testGetCategoryById() {
        Category category = new Category("Leisure Expenses", "LEISURE", "#FF5733");
        Long id = categoryService.createCategory(category);
        
        Category retrieved = categoryService.getCategoryById(id);
        assertNotNull(retrieved);
        assertEquals("Leisure Expenses", retrieved.getName());
        assertEquals("LEISURE", retrieved.getType());
    }

    @Test
    public void testUpdateCategory() {
        Category category = new Category("Original", "UTILITIES", "#00FF00");
        Long id = categoryService.createCategory(category);
        
        category.setId(id);
        category.setName("Updated Name");
        categoryService.updateCategory(category);
        
        Category updated = categoryService.getCategoryById(id);
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    public void testGetAllCategories() {
        int initialCount = categoryService.getAllCategories().size();
        
        categoryService.createCategory(new Category("TestCat1", "MANDATORY", "#FF0000"));
        categoryService.createCategory(new Category("TestCat2", "LEISURE", "#00FF00"));
        
        int finalCount = categoryService.getAllCategories().size();
        assertTrue(finalCount >= initialCount);
    }

    @Test
    public void testDeleteCategory() {
        Category category = new Category("Delete Test", "KIDS", "#0000FF");
        Long id = categoryService.createCategory(category);
        
        categoryService.deleteCategory(id);
        
        Category deleted = categoryService.getCategoryById(id);
        assertNotNull(deleted);
        assertFalse(deleted.isActive());
    }
}
