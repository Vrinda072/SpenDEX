package model;

import java.time.LocalDate;

/**
 * SpendEX — Expense Model
 * Represents a single expense entry with encapsulation
 */
public class Expense {
    private String description;
    private double amount;
    private String category;
    private LocalDate date;

    // Constructor
    public Expense(String description, double amount, String category, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Getters with encapsulation
    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    // Setters with validation
    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%s - AED %.2f (%s) on %s", 
            description, amount, category, date);
    }
}
