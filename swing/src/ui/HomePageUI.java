package ui;

import javax.swing.*;
import java.awt.*;
import manager.FinanceTracker;
import manager.CalendarManager;
import manager.CategoryManager;
import logic.BudgetAdjuster;
import logic.BudgetForecast;

/**
 * SpendEX — Home Page UI
 * Main navigation page for SpendEX application
 */
public class HomePageUI extends JFrame {
    private FinanceTracker financeTracker;
    private CalendarManager calendarManager;
    private CategoryManager categoryManager;
    private BudgetAdjuster budgetAdjuster;
    private BudgetForecast budgetForecast;

    // Color scheme
    private static final Color BG_COLOR = new Color(13, 13, 13);
    private static final Color PANEL_COLOR = new Color(31, 31, 31);
    private static final Color BTN_COLOR = new Color(106, 13, 173);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);

    // Constructor
    public HomePageUI(FinanceTracker financeTracker, CalendarManager calendarManager,
                      CategoryManager categoryManager, BudgetAdjuster budgetAdjuster,
                      BudgetForecast budgetForecast) {
        this.financeTracker = financeTracker;
        this.calendarManager = calendarManager;
        this.categoryManager = categoryManager;
        this.budgetAdjuster = budgetAdjuster;
        this.budgetForecast = budgetForecast;

        initializeUI();
    }

    // Initialize UI components
    private void initializeUI() {
        setTitle("SpendEX — Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PANEL_COLOR);
        headerPanel.setPreferredSize(new Dimension(550, 100));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BTN_COLOR));

        JLabel titleLabel = new JLabel("💰 SpendEX");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(BTN_COLOR);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel subtitleLabel = new JLabel("Smart Calendar Finance Assistant");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(PANEL_COLOR);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);
        titlePanel.add(Box.createVerticalStrut(10));

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 60, 25, 60));

        JButton addExpenseBtn = createButton("➕ Add Expense");
        JButton addIncomeBtn = createButton("💵 Add Income");
        JButton addEventBtn = createButton("📅 Add Event");
        JButton viewBudgetBtn = createButton("📊 View Adjusted Budget");
        JButton viewSummaryBtn = createButton("📈 View Summary");

        // Add button listeners
        addExpenseBtn.addActionListener(e -> openAddExpenseUI());
        addIncomeBtn.addActionListener(e -> openAddIncomeUI());
        addEventBtn.addActionListener(e -> openAddEventUI());
        viewBudgetBtn.addActionListener(e -> openViewBudgetUI());
        viewSummaryBtn.addActionListener(e -> openSummaryUI());

        buttonPanel.add(addExpenseBtn);
        buttonPanel.add(addIncomeBtn);
        buttonPanel.add(addEventBtn);
        buttonPanel.add(viewBudgetBtn);
        buttonPanel.add(viewSummaryBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    // Create styled button
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BTN_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(BTN_COLOR, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    // Open Add Expense UI
    private void openAddExpenseUI() {
        new AddExpenseUI(financeTracker, categoryManager);
    }

    // Open Add Income UI
    private void openAddIncomeUI() {
        new AddIncomeUI(financeTracker);
    }

    // Open Add Event UI
    private void openAddEventUI() {
        new AddEventUI(calendarManager, categoryManager);
    }

    // Open View Budget UI
    private void openViewBudgetUI() {
        // Simple dialog showing adjusted budget
        JOptionPane.showMessageDialog(this,
            String.format("Adjusted Daily Budget: AED %.2f",
                budgetAdjuster.getAdjustedDailyBudget()),
            "SpendEX — Budget",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Open Summary UI
    private void openSummaryUI() {
        new SummaryUI(financeTracker, calendarManager, budgetAdjuster, budgetForecast);
    }
}
