package logic;

import manager.CalendarManager;
import manager.FinanceTracker;
import java.time.LocalDate;

/**
 * SpendEX — BudgetAdjuster
 * Adjusts daily budget based on upcoming events and spending
 */
public class BudgetAdjuster {
    private double monthlyBudget;
    private FinanceTracker financeTracker;
    private CalendarManager calendarManager;

    // Constructor
    public BudgetAdjuster(double monthlyBudget, FinanceTracker financeTracker, 
                         CalendarManager calendarManager) {
        this.monthlyBudget = monthlyBudget;
        this.financeTracker = financeTracker;
        this.calendarManager = calendarManager;
    }

    // Calculate adjusted daily budget
    public double getAdjustedDailyBudget() {
        LocalDate today = LocalDate.now();
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        
        int daysRemaining = (int) java.time.temporal.ChronoUnit.DAYS
            .between(today, endOfMonth) + 1;
        
        // Calculate remaining budget after current month expenses
        double spent = financeTracker.getCurrentMonthExpenses();
        double remaining = monthlyBudget - spent;
        
        // Adjust for upcoming events (next 7 days)
        double upcomingCosts = calendarManager.getTotalUpcomingCosts(today);
        double adjustedRemaining = remaining - upcomingCosts;
        
        // Calculate adjusted daily budget
        double dailyBudget = adjustedRemaining / daysRemaining;
        
        return Math.max(0, dailyBudget); // Never go negative
    }

    // Get monthly budget
    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    // Get remaining budget for month
    public double getRemainingBudget() {
        double spent = financeTracker.getCurrentMonthExpenses();
        return Math.max(0, monthlyBudget - spent);
    }

    // Get spent amount
    public double getSpentAmount() {
        return financeTracker.getCurrentMonthExpenses();
    }

    // Set monthly budget
    public void setMonthlyBudget(double monthlyBudget) {
        if (monthlyBudget <= 0) {
            throw new IllegalArgumentException("Monthly budget must be greater than 0");
        }
        this.monthlyBudget = monthlyBudget;
    }

    // Check if category is over budget (80%+)
    public boolean isCategoryWarning(String category, double categoryBudget) {
        double spent = financeTracker.getCurrentMonthExpensesByCategory(category);
        return spent >= (categoryBudget * 0.8);
    }
}
