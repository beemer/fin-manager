package com.finmanager.model;

import java.time.LocalDate;

public class InvestmentEntry {
    private Long id;
    private LocalDate date;
    private Double amount;
    private String currency;
    private Double exchangeRate;
    private String description;
    private boolean isRecurring;

    public InvestmentEntry() {}

    public InvestmentEntry(LocalDate date, Double amount, String currency, String description) {
        this.date = date;
        this.amount = amount;
        this.currency = currency;
        this.exchangeRate = 1.0;
        this.description = description;
        this.isRecurring = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Double getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(Double exchangeRate) { this.exchangeRate = exchangeRate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }

    public Double getAmountInBaseCurrency() {
        return amount * exchangeRate;
    }

    @Override
    public String toString() {
        return String.format("InvestmentEntry{date=%s, amount=%.2f, currency=%s}", date, amount, currency);
    }
}
