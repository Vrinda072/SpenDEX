package logic;

import manager.FinanceTracker;
import manager.CalendarManager;
import java.time.LocalDate;

/**
 * SpendEX — BudgetForecast
 * Forecasts future budget based on spending patterns
 */
public class BudgetForecast {
    private FinanceTracker financeTracker;
    private CalendarManager calendarManager;
    private BudgetAdjuster budgetAdjuster;

    // Constructor
    public BudgetForecast(FinanceTracker financeTracker, CalendarManager calendarManager,
                         BudgetAdjuster budgetAdjuster) {
        this.financeTracker = financeTracker;
        this.calendarManager = calendarManager;
        this.budgetAdjuster = budgetAdjuster;
    }

    // Forecast budget for next N days
    public double forecastBudget(int days) {
        double dailyBudget = budgetAdjuster.getAdjustedDailyBudget();
        return dailyBudget * days;
    }

    // Forecast if user will exceed budget
    public boolean willExceedBudget(double additionalSpending) {
        double remaining = budgetAdjuster.getRemainingBudget();
        return additionalSpending > remaining;
    }

    // Get recommended daily spending
    public double getRecommendedDailySpending() {
        LocalDate today = LocalDate.now();
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        
        int daysRemaining = (int) java.time.temporal.ChronoUnit.DAYS
            .between(today, endOfMonth) + 1;
        
        // Average spending per day for safe budget
        return budgetAdjuster.getRemainingBudget() / daysRemaining;
    }

    // Get total upcoming event costs
    public double getUpcomingEventsCost() {
        return calendarManager.getTotalUpcomingCosts(LocalDate.now());
    }

    // Forecast if upcoming events will impact budget
    public boolean willEventsImpactBudget() {
        double upcomingCosts = getUpcomingEventsCost();
        double remaining = budgetAdjuster.getRemainingBudget();
        return upcomingCosts > (remaining * 0.5); // More than 50% of remaining
    }
}
