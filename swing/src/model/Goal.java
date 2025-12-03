package model;

/**
 * SpendEX — Goal Model
 * Represents a savings goal with encapsulation
 */
public class Goal {
    private String name;
    private double targetAmount;
    private double currentAmount;

    // Constructor
    public Goal(String name, double targetAmount) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = 0;
    }

    // Getters with encapsulation
    public String getName() {
        return name;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public double getProgress() {
        return (currentAmount / targetAmount) * 100;
    }

    // Setters with validation
    public void setName(String name) {
        this.name = name;
    }

    public void setTargetAmount(double targetAmount) {
        if (targetAmount <= 0) {
            throw new IllegalArgumentException("Target amount must be greater than 0");
        }
        this.targetAmount = targetAmount;
    }

    public void addAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.currentAmount += amount;
    }

    public boolean isCompleted() {
        return currentAmount >= targetAmount;
    }

    @Override
    public String toString() {
        return String.format("%s: AED %.2f / AED %.2f (%.1f%%)", 
            name, currentAmount, targetAmount, getProgress());
    }
}
