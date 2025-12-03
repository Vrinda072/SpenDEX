# SpendEX — Smart Calendar Finance Assistant

A pure Java Swing desktop application for budget management, expense tracking, and financial forecasting.

## Features

✅ **Add Expenses** - Track daily spending with categories (Food, Travel, Bills, Entertainment, Other)
✅ **Add Income** - Record income entries
✅ **Add Events** - Plan future events that impact budget
✅ **Adjusted Budget** - Automatically recalculates daily budget based on upcoming events
✅ **Budget Summary** - View spending, remaining budget, and category warnings
✅ **Exception Handling** - Custom exceptions for invalid inputs
✅ **AED Currency** - All values in UAE Dirhams (AED)

## Project Structure

```
/swing/src/
├── MainApp.java                 # Entry point
├── exceptions/
│   ├── InvalidAmountException.java
│   └── InvalidDateException.java
├── model/
│   ├── Expense.java
│   ├── Income.java
│   ├── Event.java
│   └── Goal.java
├── manager/
│   ├── FinanceTracker.java
│   ├── CalendarManager.java
│   └── CategoryManager.java
├── logic/
│   ├── BudgetCalculable.java    # Interface
│   ├── BudgetAdjuster.java
│   └── BudgetForecast.java
└── ui/
    ├── HomePageUI.java
    ├── AddExpenseUI.java
    ├── AddIncomeUI.java
    ├── AddEventUI.java
    └── SummaryUI.java
```

## OOP Principles

### Encapsulation
- All model fields are private with getters/setters
- Validation in setters ensures data integrity
- Example: `Expense.setAmount()` validates amount > 0

### Abstraction
- `BudgetCalculable` interface defines contract for budget impact calculation
- `Event` implements `BudgetCalculable` to demonstrate abstraction
- UI classes hide implementation details behind clear interfaces

### Composition
- `HomePageUI` composes and uses `FinanceTracker`, `CalendarManager`, `CategoryManager`
- `BudgetAdjuster` and `BudgetForecast` compose manager objects
- UI pages navigate between screens via composition relationships

### Exception Handling
- Custom exceptions: `InvalidAmountException`, `InvalidDateException`
- All UI forms use try-catch blocks for validation
- Meaningful error messages displayed to users

## Color Scheme (SpendEX Theme)

- **Background**: #0D0D0D (Black)
- **Panels**: #1F1F1F (Dark Grey)
- **Buttons**: #6A0DAD (Purple)
- **Text**: #FFFFFF (White)
- **Warning**: #FFC107 (Yellow)

## How to Run

### Compile
```bash
cd /workspaces/SpenDEX/swing
javac -d bin $(find src -type f -name "*.java")
```

### Run
```bash
java -cp bin MainApp
```

## Pages

### 1. Homepage (HomePageUI)
Navigation hub with buttons for:
- Add Expense
- Add Income
- Add Event
- View Adjusted Budget
- View Summary

### 2. Add Expense (AddExpenseUI)
Form fields:
- Description
- Amount (AED)
- Category (dropdown)
- Date (YYYY-MM-DD)

Validates:
- Description not empty
- Amount > 0
- Valid date format

### 3. Add Income (AddIncomeUI)
Form fields:
- Description
- Amount (AED)
- Date (YYYY-MM-DD)

### 4. Add Event (AddEventUI)
Form fields:
- Event Title
- Expected Cost (AED)
- Date (YYYY-MM-DD)
- Category

Console output:
```
Event "Trip" reduces your next 7-day SpendEX daily budget by AED 500.00.
```

### 5. Summary (SummaryUI)
Displays:
- Total Spent
- Remaining Budget
- Monthly Budget
- Adjusted Daily Budget
- Recommended Daily Spending
- Upcoming Events (7 days)
- Category Warnings (80%+)

## Default Configuration

- Monthly Budget: AED 5,000
- Categories: Food, Travel, Bills, Entertainment, Other
- Currency: AED only
- All data is user-input based (no random values)

## Exception Handling Examples

```java
// InvalidAmountException
double amount = Double.parseDouble(amountStr);
if (amount <= 0) {
    throw new InvalidAmountException("Amount must be greater than 0");
}

// InvalidDateException
LocalDate date = LocalDate.parse(dateStr);  // Throws if invalid format
```

## Backend Logic

### FinanceTracker
- Tracks all expenses and income
- Calculates monthly totals
- Filters by category and date

### CalendarManager
- Manages upcoming events
- Filters events by date range (7 days)
- Calculates total upcoming costs

### BudgetAdjuster
- Calculates adjusted daily budget based on:
  - Remaining monthly budget
  - Upcoming event costs
  - Days remaining in month
- Checks for category warnings (80%+)

### BudgetForecast
- Forecasts budget for N days
- Warns if events will impact budget
- Recommends daily spending

## Class Sizes

All classes kept under 300 lines:
- HomePageUI: ~200 lines
- AddExpenseUI: ~180 lines
- AddEventUI: ~200 lines
- FinanceTracker: ~150 lines
- BudgetAdjuster: ~120 lines

## Testing Scenario

1. Launch MainApp → Shows Homepage
2. Click "Add Income" → Add AED 5000
3. Click "Add Expense" → Add AED 100 (Food)
4. Click "Add Event" → Add AED 500 event
5. Click "View Summary" → See adjusted budget
6. Console prints: "Event reduces your next 7-day SpendEX daily budget by AED 500.00"

## Developer Notes

- No external libraries - pure Java Swing
- Thread-safe UI initialization on EDT
- Modular design for easy feature additions
- Clear separation of concerns (model, logic, ui)
- All comments reference "SpendEX"

---

**SpendEX** — Smart Calendar Finance Assistant
Pure Java Swing | AED Only | User Input Based
