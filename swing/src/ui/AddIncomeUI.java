package ui;

import javax.swing.*;
import java.awt.*;
import manager.FinanceTracker;
import model.Income;
import exceptions.InvalidAmountException;
import exceptions.InvalidDateException;
import java.time.LocalDate;

/**
 * SpendEX — Add Income UI
 * Form to add new income entries to SpendEX
 */
public class AddIncomeUI extends JFrame {
    private FinanceTracker financeTracker;

    // Color scheme
    private static final Color BG_COLOR = new Color(13, 13, 13);
    private static final Color PANEL_COLOR = new Color(31, 31, 31);
    private static final Color BTN_COLOR = new Color(106, 13, 173);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);

    // UI Components
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField dateField;

    // Constructor
    public AddIncomeUI(FinanceTracker financeTracker) {
        this.financeTracker = financeTracker;
        initializeUI();
    }

    // Initialize UI
    private void initializeUI() {
        setTitle("SpendEX — Add Income");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setLayout(new BorderLayout());

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(PANEL_COLOR);
        titlePanel.setPreferredSize(new Dimension(450, 60));
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BTN_COLOR));
        JLabel titleLabel = new JLabel("💵 Add Income");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(BTN_COLOR);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(BG_COLOR);
        formPanel.setLayout(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Description
        JLabel descLabel = new JLabel("Description:");
        descLabel.setForeground(TEXT_COLOR);
        descriptionField = createTextField();
        formPanel.add(descLabel);
        formPanel.add(descriptionField);

        // Amount
        JLabel amountLabel = new JLabel("Amount (AED):");
        amountLabel.setForeground(TEXT_COLOR);
        amountField = createTextField();
        formPanel.add(amountLabel);
        formPanel.add(amountField);

        // Date
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setForeground(TEXT_COLOR);
        dateField = createTextField();
        dateField.setToolTipText("Example: " + LocalDate.now().toString());
        formPanel.add(dateLabel);
        formPanel.add(dateField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton saveBtn = createButton("Save Income");
        JButton cancelBtn = createButton("Cancel");

        saveBtn.addActionListener(e -> saveIncome());
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    // Create styled text field
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(PANEL_COLOR);
        field.setForeground(TEXT_COLOR);
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createLineBorder(BTN_COLOR, 1));
        return field;
    }

    // Create styled button
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BTN_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Save income with validation
    private void saveIncome() {
        try {
            String description = descriptionField.getText().trim();
            String amountStr = amountField.getText().trim();
            String dateStr = dateField.getText().trim();

            // Validation
            if (description.isEmpty()) {
                throw new IllegalArgumentException("Description cannot be empty");
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                throw new InvalidAmountException("Amount must be a valid number");
            }

            if (amount <= 0) {
                throw new InvalidAmountException("Amount must be greater than 0");
            }

            LocalDate date;
            try {
                date = LocalDate.parse(dateStr);
            } catch (Exception e) {
                throw new InvalidDateException("Date must be in format YYYY-MM-DD");
            }

            // Create and save income
            Income income = new Income(description, amount, date);
            financeTracker.addIncome(income);

            JOptionPane.showMessageDialog(this,
                String.format("Income saved: %s - AED %.2f", description, amount),
                "SpendEX — Success",
                JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (InvalidAmountException | InvalidDateException e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "SpendEX — Validation Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "SpendEX — Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "SpendEX — Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
