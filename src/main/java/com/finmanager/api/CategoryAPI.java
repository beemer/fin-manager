package com.finmanager.api;

import com.finmanager.model.Category;
import com.finmanager.service.CategoryService;
import com.finmanager.util.Logger;
import com.finmanager.util.GsonUtil;
import com.google.gson.Gson;

import java.util.List;

public class CategoryAPI {
    private final CategoryService categoryService;
    private final Gson gson;

    public CategoryAPI() {
        this.categoryService = CategoryService.getInstance();
        this.gson = GsonUtil.getInstance();
    }

    public String getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return gson.toJson(categories);
    }

    public String getCategoryById(Long id) {
        Category category = categoryService.getCategoryById(id);
        return gson.toJson(category);
    }

    public String createCategory(String json) {
        try {
            Category category = gson.fromJson(json, Category.class);
            Long id = categoryService.createCategory(category);
            category.setId(id);
            return gson.toJson(category);
        } catch (Exception e) {
            return gson.toJson(new ApiError("Failed to create category: " + e.getMessage()));
        }
    }

    public String updateCategory(String json) {
        try {
            Category category = gson.fromJson(json, Category.class);
            categoryService.updateCategory(category);
            return gson.toJson(category);
        } catch (Exception e) {
            return gson.toJson(new ApiError("Failed to update category: " + e.getMessage()));
        }
    }

    public String deleteCategory(Long id) {
        try {
            categoryService.deleteCategory(id);
            return gson.toJson(new ApiResponse("Category deleted successfully"));
        } catch (Exception e) {
            Logger.error(CategoryAPI.class, "Failed to delete category: " + id, e);
            return gson.toJson(new ApiError(e.getMessage()));
        }
    }

    public static class ApiResponse {
        public String message;

        public ApiResponse(String message) {
            this.message = message;
        }
    }

    public static class ApiError {
        public String error;

        public ApiError(String error) {
            this.error = error;
        }
    }
}
