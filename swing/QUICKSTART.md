# SpendEX — Quick Start Guide

## Installation & Setup

### Prerequisites
- Java 8 or higher
- Terminal/Command Prompt

### Step 1: Navigate to Project
```bash
cd /workspaces/SpenDEX/swing
```

### Step 2: Compile
```bash
javac -d bin $(find src -type f -name "*.java")
```

Or use the build script:
```bash
bash build.sh
```

### Step 3: Run
```bash
java -cp bin MainApp
```

## First Use

1. **Homepage Opens** → Click "Add Expense"
2. **Add First Expense**
   - Description: "Lunch"
   - Amount: 50
   - Category: Food
   - Date: Today's date (auto-filled)
   - Click "Save Expense"

3. **Add Income**
   - Click "Add Income" on homepage
   - Description: "Salary"
   - Amount: 5000
   - Date: Today's date
   - Click "Save Income"

4. **Add Event**
   - Click "Add Event" on homepage
   - Title: "Trip to Dubai"
   - Cost: 500
   - Date: 2025-12-15
   - Category: Travel
   - Click "Save Event"
   - Check console: Event message printed

5. **View Summary**
   - Click "View Summary"
   - See your adjusted daily budget
   - See category warnings if any
   - See upcoming events impact

## Features Overview

### Add Expense
- Track daily spending
- Categorize (Food, Travel, Bills, Entertainment, Other)
- Auto-validation of amount and date
- Shows success message

### Add Income
- Record earnings
- Simple form (description, amount, date)
- Full validation

### Add Event
- Plan future costs
- Automatically adjusts daily budget
- Prints to console: "Event reduces your next 7-day SpendEX daily budget by AED X.XX"

### View Adjusted Budget
- Shows current daily spending limit
- Adjusts based on:
  - Remaining monthly budget
  - Upcoming events (7 days)
  - Days remaining in month

### View Summary
- **Monthly Summary**: Total spent, remaining, budget
- **Daily Budget**: Adjusted rate, recommended spending
- **Upcoming Events**: Next 7 days total costs
- **Category Warnings**: Shows if 80%+ spent

## Tips

1. **Date Format**: Always use YYYY-MM-DD (e.g., 2025-12-02)
2. **Currency**: All values in AED (no need to type AED)
3. **Categories**: 5 default categories, can be extended
4. **Budget**: Default monthly budget is AED 5,000
5. **Validation**: Invalid inputs show error messages

## Keyboard Shortcuts

- Press `Enter` in text fields to save (if focused on button)
- Click `Cancel` to close dialogs without saving
- `X` button to close windows

## Default Budget Settings

- **Monthly Budget**: AED 5,000
- **Period**: 1 calendar month
- **Daily Budget Calc**: (Remaining - Upcoming Events) / Days Remaining
- **Warning Threshold**: 80% of category budget

## Example Workflow

### Scenario: Plan a Trip

1. **Add Income** (Salary)
   - Description: "Monthly Salary"
   - Amount: 5000
   - Date: 2025-12-01

2. **Add Monthly Expenses**
   - Expense 1: "Groceries" - 200 AED (Food)
   - Expense 2: "Gas" - 150 AED (Travel)
   - Expense 3: "Electricity" - 300 AED (Bills)

3. **Add Event** (Trip planning)
   - Title: "Dubai Trip"
   - Cost: 1000 AED
   - Date: 2025-12-15 (10 days away)
   - Category: Travel

4. **View Summary**
   - Spent so far: 650 AED
   - Remaining: 4350 AED
   - Upcoming events: 1000 AED
   - Adjusted for next 7 days: 4350 - 1000 = 3350 / 20 days = **167.50 AED/day**

5. **Console Output**
   ```
   Event "Dubai Trip" reduces your next 7-day SpendEX daily budget by AED 1000.00.
   ```

## Troubleshooting

### "Class not found" Error
- Make sure you're in `/workspaces/SpenDEX/swing` directory
- Check that `bin` directory was created
- Re-compile: `javac -d bin $(find src -type f -name "*.java")`

### "Invalid format" Error
- Check date format: must be YYYY-MM-DD
- Check amount: must be a number (e.g., 50.00, not "50 AED")
- No special characters in description

### Window won't open
- Check Java version: `java -version`
- Needs Java 8+
- Try running as admin if permission issues

## File Structure

```
swing/
├── src/               # Source code
│   ├── MainApp.java
│   ├── exceptions/
│   ├── model/
│   ├── manager/
│   ├── logic/
│   └── ui/
├── bin/              # Compiled classes (created after javac)
├── build.sh          # Build script
├── README.md         # Full documentation
└── IMPLEMENTATION_SUMMARY.md  # Technical details
```

## More Information

For detailed technical information, see:
- `README.md` - Full feature documentation
- `IMPLEMENTATION_SUMMARY.md` - OOP design & architecture

---

**SpendEX** — Smart Calendar Finance Assistant
Pure Java Swing | Ready to Run ✓
