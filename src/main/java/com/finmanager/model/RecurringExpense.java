package com.finmanager.model;

import java.time.LocalDate;

public class RecurringExpense {
    public enum Frequency {
        MONTHLY, YEARLY
    }

    private Long id;
    private Long categoryId;
    private Double amount;
    private String description;
    private Frequency frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate lastGeneratedDate;
    private boolean active;

    public RecurringExpense() {}

    public RecurringExpense(Long categoryId, Double amount, String description, 
                          Frequency frequency, LocalDate startDate) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.frequency = frequency;
        this.startDate = startDate;
        this.active = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Frequency getFrequency() { return frequency; }
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDate getLastGeneratedDate() { return lastGeneratedDate; }
    public void setLastGeneratedDate(LocalDate lastGeneratedDate) { this.lastGeneratedDate = lastGeneratedDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("RecurringExpense{categoryId=%d, amount=%.2f, frequency=%s}", 
                           categoryId, amount, frequency);
    }
}
