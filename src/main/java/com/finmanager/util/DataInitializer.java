package com.finmanager.util;

import com.finmanager.service.*;
import com.finmanager.model.Category;
import com.finmanager.db.DatabaseManager;

public class DataInitializer {
    public static void initializeSampleData() {
        CategoryService categoryService = CategoryService.getInstance();

        // Check if categories exist
        if (categoryService.getAllCategories().isEmpty()) {
            // Create default categories
            categoryService.createCategory(new Category("Mandatory", "MANDATORY", "#FF0000"));
            categoryService.createCategory(new Category("Leisure", "LEISURE", "#00FF00"));
            categoryService.createCategory(new Category("Utilities", "UTILITIES", "#0000FF"));
            categoryService.createCategory(new Category("Kids", "KIDS", "#FFFF00"));
            categoryService.createCategory(new Category("Investments", "INVESTMENTS", "#FF00FF"));
            
            Logger.getInstance().info("Sample categories created");
        }
    }

    public static void cleanupOldData() {
        DatabaseManager.getInstance().cleanupOldData();
        Logger.getInstance().info("Old data cleaned up (data older than 3 years)");
    }
}
