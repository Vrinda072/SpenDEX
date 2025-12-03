package manager;

import model.Expense;
import model.Income;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * SpendEX — FinanceTracker
 * Tracks all expenses and income for SpendEX
 */
public class FinanceTracker {
    private List<Expense> expenses;
    private List<Income> incomes;

    // Constructor
    public FinanceTracker() {
        this.expenses = new ArrayList<>();
        this.incomes = new ArrayList<>();
    }

    // Add expense
    public void addExpense(Expense expense) {
        if (expense != null) {
            expenses.add(expense);
        }
    }

    // Add income
    public void addIncome(Income income) {
        if (income != null) {
            incomes.add(income);
        }
    }

    // Get all expenses
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    // Get all incomes
    public List<Income> getAllIncomes() {
        return new ArrayList<>(incomes);
    }

    // Get current month total expenses
    public double getCurrentMonthExpenses() {
        YearMonth current = YearMonth.now();
        double total = 0;
        
        for (Expense expense : expenses) {
            if (YearMonth.from(expense.getDate()).equals(current)) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    // Get current month total income
    public double getCurrentMonthIncome() {
        YearMonth current = YearMonth.now();
        double total = 0;
        
        for (Income income : incomes) {
            if (YearMonth.from(income.getDate()).equals(current)) {
                total += income.getAmount();
            }
        }
        return total;
    }

    // Get expenses by category
    public double getExpensesByCategory(String category) {
        double total = 0;
        for (Expense expense : expenses) {
            if (expense.getCategory().equals(category)) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    // Get total expenses for current month by category
    public double getCurrentMonthExpensesByCategory(String category) {
        YearMonth current = YearMonth.now();
        double total = 0;
        
        for (Expense expense : expenses) {
            if (expense.getCategory().equals(category) &&
                YearMonth.from(expense.getDate()).equals(current)) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    // Get today's expenses
    public double getTodayExpenses() {
        LocalDate today = LocalDate.now();
        double total = 0;
        
        for (Expense expense : expenses) {
            if (expense.getDate().equals(today)) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    // Remove expense
    public void removeExpense(Expense expense) {
        expenses.remove(expense);
    }

    // Remove income
    public void removeIncome(Income income) {
        incomes.remove(income);
    }
}
