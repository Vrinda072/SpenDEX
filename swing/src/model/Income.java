package model;

import java.time.LocalDate;

/**
 * SpendEX — Income Model
 * Represents a single income entry with encapsulation
 */
public class Income {
    private String description;
    private double amount;
    private LocalDate date;

    // Constructor
    public Income(String description, double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    // Getters with encapsulation
    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%s - AED %.2f on %s", 
            description, amount, date);
    }
}
