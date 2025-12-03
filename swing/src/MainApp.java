import manager.FinanceTracker;
import manager.CalendarManager;
import manager.CategoryManager;
import logic.BudgetAdjuster;
import logic.BudgetForecast;
import ui.HomePageUI;

/**
 * SpendEX — Smart Calendar Finance Assistant
 * Main entry point for the SpendEX application
 * 
 * A budgeting assistant that connects:
 * - Calendar of future events
 * - Daily expense/income tracker
 * - Goal-based saving system
 * - Budget forecast system
 * 
 * Automatically adjusts daily spending limit (AED) based on events and spending.
 */
public class MainApp {
    public static void main(String[] args) {
        // Initialize backend managers
        FinanceTracker financeTracker = new FinanceTracker();
        CalendarManager calendarManager = new CalendarManager();
        CategoryManager categoryManager = new CategoryManager();

        // Initialize budget logic with composition
        BudgetAdjuster budgetAdjuster = new BudgetAdjuster(
            5000.0, // Monthly budget of 5000 AED
            financeTracker,
            calendarManager
        );

        BudgetForecast budgetForecast = new BudgetForecast(
            financeTracker,
            calendarManager,
            budgetAdjuster
        );

        // Launch Swing UI on EDT
        javax.swing.SwingUtilities.invokeLater(() -> {
            new HomePageUI(financeTracker, calendarManager, categoryManager,
                          budgetAdjuster, budgetForecast);
        });
    }
}
