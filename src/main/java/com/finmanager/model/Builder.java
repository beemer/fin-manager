package com.finmanager.model;

public class Builder {
    // Simple builder for Category since we're using Java 11
    public static class CategoryBuilder {
        private String name;
        private String type;
        private String color;
        private boolean active = true;

        public CategoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder type(String type) {
            this.type = type;
            return this;
        }

        public CategoryBuilder color(String color) {
            this.color = color;
            return this;
        }

        public CategoryBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Category build() {
            Category category = new Category();
            category.setName(name);
            category.setType(type);
            category.setColor(color);
            category.setActive(active);
            return category;
        }
    }

    public static CategoryBuilder categoryBuilder() {
        return new CategoryBuilder();
    }
}
