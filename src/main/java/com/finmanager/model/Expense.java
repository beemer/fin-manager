package com.finmanager.model;

import java.time.LocalDate;
import java.util.Objects;

public class Expense {
    private Long id;
    private LocalDate date;
    private Double amount;
    private Long categoryId;
    private String description;
    private boolean isRecurringInstance;

    public Expense() {}

    public Expense(LocalDate date, Double amount, Long categoryId, String description) {
        this.date = date;
        this.amount = amount;
        this.categoryId = categoryId;
        this.description = description;
        this.isRecurringInstance = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isRecurringInstance() { return isRecurringInstance; }
    public void setRecurringInstance(boolean recurringInstance) { isRecurringInstance = recurringInstance; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Objects.equals(id, expense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Expense{date=%s, amount=%.2f, categoryId=%d}", date, amount, categoryId);
    }
}
