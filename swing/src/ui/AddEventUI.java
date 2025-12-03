package ui;

import javax.swing.*;
import java.awt.*;
import manager.CalendarManager;
import manager.CategoryManager;
import model.Event;
import exceptions.InvalidAmountException;
import exceptions.InvalidDateException;
import java.time.LocalDate;

/**
 * SpendEX — Add Event UI
 * Form to add upcoming events that impact SpendEX budget
 */
public class AddEventUI extends JFrame {
    private CalendarManager calendarManager;
    private CategoryManager categoryManager;

    // Color scheme
    private static final Color BG_COLOR = new Color(13, 13, 13);
    private static final Color PANEL_COLOR = new Color(31, 31, 31);
    private static final Color BTN_COLOR = new Color(106, 13, 173);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);

    // UI Components
    private JTextField titleField;
    private JTextField costField;
    private JTextField dateField;
    private JComboBox<String> categoryCombo;

    // Constructor
    public AddEventUI(CalendarManager calendarManager, CategoryManager categoryManager) {
        this.calendarManager = calendarManager;
        this.categoryManager = categoryManager;
        initializeUI();
    }

    // Initialize UI
    private void initializeUI() {
        setTitle("SpendEX — Add Event");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 400);
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
        JLabel titleLabel = new JLabel("📅 Add Event");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(BTN_COLOR);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(BG_COLOR);
        formPanel.setLayout(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLbl = new JLabel("Event Title:");
        titleLbl.setForeground(TEXT_COLOR);
        titleField = createTextField();
        formPanel.add(titleLbl);
        formPanel.add(titleField);

        // Expected Cost
        JLabel costLabel = new JLabel("Expected Cost (AED):");
        costLabel.setForeground(TEXT_COLOR);
        costField = createTextField();
        formPanel.add(costLabel);
        formPanel.add(costField);

        // Date
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setForeground(TEXT_COLOR);
        dateField = createTextField();
        dateField.setToolTipText("Example: " + LocalDate.now().toString());
        formPanel.add(dateLabel);
        formPanel.add(dateField);

        // Category
        JLabel catLabel = new JLabel("Category:");
        catLabel.setForeground(TEXT_COLOR);
        categoryCombo = new JComboBox<>(categoryManager.getDefaultCategories());
        categoryCombo.setBackground(PANEL_COLOR);
        categoryCombo.setForeground(TEXT_COLOR);
        formPanel.add(catLabel);
        formPanel.add(categoryCombo);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton saveBtn = createButton("Save Event");
        JButton cancelBtn = createButton("Cancel");

        saveBtn.addActionListener(e -> saveEvent());
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

    // Save event with validation
    private void saveEvent() {
        try {
            String title = titleField.getText().trim();
            String costStr = costField.getText().trim();
            String dateStr = dateField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();

            // Validation
            if (title.isEmpty()) {
                throw new IllegalArgumentException("Event title cannot be empty");
            }

            double cost;
            try {
                cost = Double.parseDouble(costStr);
            } catch (NumberFormatException e) {
                throw new InvalidAmountException("Cost must be a valid number");
            }

            if (cost <= 0) {
                throw new InvalidAmountException("Cost must be greater than 0");
            }

            LocalDate date;
            try {
                date = LocalDate.parse(dateStr);
            } catch (Exception e) {
                throw new InvalidDateException("Date must be in format YYYY-MM-DD");
            }

            // Create and save event
            Event event = new Event(title, cost, date, category);
            calendarManager.addEvent(event);

            // Print to console as requested
            System.out.println(String.format(
                "Event \"%s\" reduces your next 7-day SpendEX daily budget by AED %.2f.",
                title, cost));

            JOptionPane.showMessageDialog(this,
                String.format("Event saved: %s - AED %.2f\n\n" +
                    "Event \"%s\" reduces your next 7-day SpendEX daily budget by AED %.2f.",
                    title, cost, title, cost),
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
