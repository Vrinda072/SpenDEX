package managers;

import model.CalendarEvent;
import model.Category;
import model.Transaction;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SpendingLimitCalculator {
    private final BudgetManager budgetManager;
    private static final int FORECAST_DAYS = 30;
    private static final double SAVINGS_TARGET_PERCENTAGE = 0.2; // 20% of income as savings goal

    public SpendingLimitCalculator(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    public double calculateRecommendedDailyLimit() {
        double remainingBudget = budgetManager.getRemainingBudget();
        int daysRemaining = LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth() + 1;
        
        if (daysRemaining <= 0) {
            return 0;
        }

        // Adjust for upcoming mandatory events
        double upcomingEventCosts = calculateUpcomingEventCosts(7); // Next 7 days
        remainingBudget -= upcomingEventCosts;
        remainingBudget = Math.max(0, remainingBudget);

        // Consider savings target
        double savingsTarget = calculateSavingsTarget();
        remainingBudget = Math.max(0, remainingBudget - savingsTarget);

        // Adjust based on historical spending patterns
        double spendingAdjustment = calculateSpendingAdjustment();
        remainingBudget *= (1 + spendingAdjustment);

        return remainingBudget / daysRemaining;
    }

    private double calculateUpcomingEventCosts(int daysAhead) {
        return budgetManager.getUpcomingEvents(daysAhead).stream()
                .filter(CalendarEvent::isMandatory)
                .mapToDouble(CalendarEvent::getCost)
                .sum();
    }

    private double calculateSavingsTarget() {
        double totalIncome = budgetManager.getTotalEarned();
        return totalIncome * SAVINGS_TARGET_PERCENTAGE;
    }

    private double calculateSpendingAdjustment() {
        // Analyze past spending patterns to adjust future limits
        Map<LocalDate, Double> dailySpending = budgetManager.getDailySpending();
        if (dailySpending.isEmpty()) {
            return 0;
        }

        double averageSpending = dailySpending.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        double currentDailyLimit = budgetManager.getDailySpendingLimit();
        
        if (currentDailyLimit == 0) {
            return 0;
        }

        // If spending is consistently below limit, we can be more aggressive
        // If spending is consistently above, we need to be more conservative
        return (currentDailyLimit - averageSpending) / currentDailyLimit * 0.2; // 20% adjustment factor
    }

    public Map<Category, Double> getRecommendedCategoryBudgets() {
        Map<Category, Double> recommendedBudgets = new EnumMap<>(Category.class);
        Map<Category, Double> currentSpending = budgetManager.getSpendingByCategory();
        double totalSpent = currentSpending.values().stream().mapToDouble(Double::doubleValue).sum();
        
        if (totalSpent == 0) {
            // If no spending data, distribute remaining budget equally
            double equalShare = budgetManager.getRemainingBudget() / Category.values().length;
            for (Category category : Category.values()) {
                recommendedBudgets.put(category, equalShare);
            }
        } else {
            // Distribute remaining budget based on current spending patterns
            double remainingBudget = budgetManager.getRemainingBudget();
            for (Category category : Category.values()) {
                double percentage = currentSpending.get(category) / totalSpent;
                recommendedBudgets.put(category, remainingBudget * percentage);
            }
        }
        
        return recommendedBudgets;
    }

    public double getProjectedEndOfMonthBalance() {
        double currentBalance = budgetManager.getRemainingBudget();
        double dailyLimit = calculateRecommendedDailyLimit();
        int daysRemaining = LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth() + 1;
        
        return currentBalance - (dailyLimit * daysRemaining);
    }
}
