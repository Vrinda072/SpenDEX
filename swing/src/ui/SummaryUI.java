package ui;

import javax.swing.*;
import java.awt.*;
import manager.FinanceTracker;
import manager.CalendarManager;
import logic.BudgetAdjuster;
import logic.BudgetForecast;
import model.Expense;

/**
 * SpendEX — Summary / Budget Page UI
 * Displays comprehensive budget and spending summary
 */
public class SummaryUI extends JFrame {
    private FinanceTracker financeTracker;
    private CalendarManager calendarManager;
    private BudgetAdjuster budgetAdjuster;
    private BudgetForecast budgetForecast;

    // Color scheme
    private static final Color BG_COLOR = new Color(13, 13, 13);
    private static final Color PANEL_COLOR = new Color(31, 31, 31);
    private static final Color BTN_COLOR = new Color(106, 13, 173);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);

    // Constructor
    public SummaryUI(FinanceTracker financeTracker, CalendarManager calendarManager,
                     BudgetAdjuster budgetAdjuster, BudgetForecast budgetForecast) {
        this.financeTracker = financeTracker;
        this.calendarManager = calendarManager;
        this.budgetAdjuster = budgetAdjuster;
        this.budgetForecast = budgetForecast;

        initializeUI();
    }

    // Initialize UI
    private void initializeUI() {
        setTitle("SpendEX — Summary");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 850);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setLayout(new BorderLayout());

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(PANEL_COLOR);
        titlePanel.setPreferredSize(new Dimension(700, 60));
        JLabel titleLabel = new JLabel("📊 SpendEX — Summary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(BTN_COLOR);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel);

        // Summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setBackground(BG_COLOR);
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add sections
        summaryPanel.add(createSummarySection());
        summaryPanel.add(Box.createVerticalStrut(15));
        summaryPanel.add(createBudgetSection());
        summaryPanel.add(Box.createVerticalStrut(15));
        summaryPanel.add(createEventSection());
        summaryPanel.add(Box.createVerticalStrut(15));
        summaryPanel.add(createWarningsSection());

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(summaryPanel);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setBorder(null);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    // Create summary section
    private JPanel createSummarySection() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BTN_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("💰 MONTHLY SUMMARY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(BTN_COLOR);

        double spent = budgetAdjuster.getSpentAmount();
        double remaining = budgetAdjuster.getRemainingBudget();
        double budget = budgetAdjuster.getMonthlyBudget();

        JLabel spentLabel = createDataLabel(String.format("Total Spent: AED %.2f", spent));
        JLabel remainingLabel = createDataLabel(String.format("Remaining: AED %.2f", remaining));
        JLabel budgetLabel = createDataLabel(String.format("Monthly Budget: AED %.2f", budget));

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(spentLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(remainingLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(budgetLabel);

        return panel;
    }

    // Create budget section
    private JPanel createBudgetSection() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BTN_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("📈 ADJUSTED DAILY BUDGET");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(BTN_COLOR);

        double dailyBudget = budgetAdjuster.getAdjustedDailyBudget();
        double recommended = budgetForecast.getRecommendedDailySpending();

        JLabel dailyLabel = createDataLabel(
            String.format("Adjusted Daily Budget: AED %.2f", dailyBudget));
        JLabel recommendedLabel = createDataLabel(
            String.format("Recommended Daily Spending: AED %.2f", recommended));

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(dailyLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(recommendedLabel);

        return panel;
    }

    // Create event section
    private JPanel createEventSection() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BTN_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("📅 UPCOMING EVENTS (Next 7 Days)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(BTN_COLOR);

        double upcomingCosts = budgetForecast.getUpcomingEventsCost();
        JLabel costLabel = createDataLabel(
            String.format("Total Upcoming Costs: AED %.2f", upcomingCosts));

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(costLabel);

        return panel;
    }

    // Create warnings section
    private JPanel createWarningsSection() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WARNING_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("⚠️ SPENDING BY CATEGORY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(WARNING_COLOR);

        String[] categories = {"Food", "Travel", "Bills", "Entertainment", "Other"};
        
        boolean hasSpending = false;
        for (String category : categories) {
            double spent = financeTracker.getCurrentMonthExpensesByCategory(category);
            if (spent > 0) {
                JLabel categoryLabel = createDataLabel(
                    String.format("%s: AED %.2f", category, spent));
                panel.add(categoryLabel);
                panel.add(Box.createVerticalStrut(6));
                hasSpending = true;
            }
        }

        if (!hasSpending) {
            JLabel noSpendingLabel = createDataLabel("No spending recorded yet!");
            noSpendingLabel.setForeground(new Color(76, 175, 80));
            panel.add(noSpendingLabel);
        }

        panel.add(Box.createVerticalStrut(5));
        panel.add(titleLabel);
        return panel;
    }

    // Create data label
    private JLabel createDataLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    // Create warning label
    private JLabel createWarningLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(WARNING_COLOR);
        return label;
    }
}
