package model;

import java.time.LocalDate;
import logic.BudgetCalculable;

/**
 * SpendEX — Event Model
 * Represents a future event that impacts budget planning
 * Implements BudgetCalculable for composition
 */
public class Event implements BudgetCalculable {
    private String title;
    private double expectedCost;
    private LocalDate date;
    private String category;

    // Constructor
    public Event(String title, double expectedCost, LocalDate date, String category) {
        this.title = title;
        this.expectedCost = expectedCost;
        this.date = date;
        this.category = category;
    }

    // Getters with encapsulation
    public String getTitle() {
        return title;
    }

    public double getExpectedCost() {
        return expectedCost;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    // Setters with validation
    public void setTitle(String title) {
        this.title = title;
    }

    public void setExpectedCost(double expectedCost) {
        if (expectedCost <= 0) {
            throw new IllegalArgumentException("Expected cost must be greater than 0");
        }
        this.expectedCost = expectedCost;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * SpendEX — BudgetCalculable Implementation
     * Calculates impact on daily budget
     */
    @Override
    public double calculateImpact() {
        return expectedCost;
    }

    @Override
    public String toString() {
        return String.format("%s - AED %.2f (%s) on %s", 
            title, expectedCost, category, date);
    }
}
