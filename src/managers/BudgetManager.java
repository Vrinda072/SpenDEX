package managers;

import model.Transaction;
import model.CalendarEvent;
import model.Category;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetManager {
    private final List<Transaction> transactions;
    private final List<CalendarEvent> events;
    private double monthlyBudget;
    private final Map<Category, Double> categoryBudgets;

    public BudgetManager(double monthlyBudget) {
        this.transactions = new ArrayList<>();
        this.events = new ArrayList<>();
        this.monthlyBudget = Math.max(0, monthlyBudget);
        this.categoryBudgets = new EnumMap<>(Category.class);
        initializeCategoryBudgets();
    }

    private void initializeCategoryBudgets() {
        for (Category category : Category.values()) {
            categoryBudgets.put(category, 0.0);
        }
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    public void addEvent(CalendarEvent event) {
        if (event != null) {
            events.add(event);
        }
    }

    public double getTotalSpent() {
        return transactions.stream()
                .filter(t -> t.getType().equals("EXPENSE"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalEarned() {
        return transactions.stream()
                .filter(t -> t.getType().equals("INCOME"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getRemainingBudget() {
        return monthlyBudget - getTotalSpent();
    }

    public Map<Category, Double> getSpendingByCategory() {
        Map<Category, Double> spendingByCategory = new EnumMap<>(Category.class);
        for (Category category : Category.values()) {
            spendingByCategory.put(category, 0.0);
        }

        transactions.stream()
                .filter(t -> t.getType().equals("EXPENSE"))
                .forEach(t -> {
                    Category category = t.getCategory();
                    spendingByCategory.put(category, spendingByCategory.get(category) + t.getAmount());
                });

        return spendingByCategory;
    }

    public double getDailySpendingLimit() {
        int daysInMonth = LocalDate.now().lengthOfMonth();
        int daysRemaining = daysInMonth - LocalDate.now().getDayOfMonth() + 1;
        return getRemainingBudget() / daysRemaining;
    }

    public List<CalendarEvent> getUpcomingEvents(int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(daysAhead);

        return events.stream()
                .filter(event -> !event.getDate().isBefore(today) && !event.getDate().isAfter(futureDate))
                .sorted(Comparator.comparing(CalendarEvent::getDate))
                .collect(Collectors.toList());
    }

    public Map<LocalDate, Double> getDailySpending() {
        return transactions.stream()
                .filter(t -> t.getType().equals("EXPENSE"))
                .collect(Collectors.groupingBy(
                        Transaction::getDate,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public void setMonthlyBudget(double budget) {
        this.monthlyBudget = Math.max(0, budget);
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setCategoryBudget(Category category, double budget) {
        if (category != null && budget >= 0) {
            categoryBudgets.put(category, budget);
        }
    }

    public double getCategoryBudget(Category category) {
        return categoryBudgets.getOrDefault(category, 0.0);
    }

    public List<Category> getOverBudgetCategories() {
        Map<Category, Double> spending = getSpendingByCategory();
        return categoryBudgets.entrySet().stream()
                .filter(entry -> spending.get(entry.getKey()) > entry.getValue() && entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
