package model;

import java.time.LocalDate;

public class Expense implements Transaction {
    private final double amount;
    private final String title;
    private final LocalDate date;
    private final Category category;

    public Expense(double amount, String title, LocalDate date, Category category) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive");
        }
        this.amount = amount;
        this.title = title != null ? title : "";
        this.date = date != null ? date : LocalDate.now();
        this.category = category != null ? category : Category.OTHER;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public String getType() {
        return "EXPENSE";
    }
}
