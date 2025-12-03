# SpendEX — Smart Calendar Finance Assistant

A pure Java Swing desktop application for personal budget management with automatic daily budget adjustment based on upcoming events.

## Quick Start

```bash
cd swing
javac -d bin $(find src -type f -name "*.java")
java -cp bin MainApp
```

## Features

- ✅ **Add Expenses** - Track spending by category
- ✅ **Add Income** - Record earnings
- ✅ **Add Events** - Plan future costs with budget impact
- ✅ **Adjusted Budget** - Daily limit auto-adjusts for upcoming events
- ✅ **Summary Dashboard** - Complete spending breakdown & warnings
- ✅ **Professional UI** - 5 pages with emoji headers, purple/black theme

## Architecture

**18 Java Classes** organized in packages:
- `model/` - Expense, Income, Event, Goal
- `manager/` - FinanceTracker, CalendarManager, CategoryManager
- `logic/` - BudgetAdjuster, BudgetForecast, BudgetCalculable interface
- `ui/` - HomePageUI, AddExpenseUI, AddIncomeUI, AddEventUI, SummaryUI
- `exceptions/` - InvalidAmountException, InvalidDateException

## Technical Details

- **Technology**: Pure Java 8+ with Swing (zero dependencies)
- **Currency**: AED (UAE Dirham)
- **Design**: Full OOP with Encapsulation, Abstraction, Composition
- **Data**: User-input only (no defaults or hardcoded values)
- **Color Scheme**: Black #0D0D0D, Purple #6A0DAD, White #FFFFFF

## Project Structure

```
SpenDEX/
├── swing/                   # Main Swing Application
│   ├── src/                 # 18 Java classes
│   │   ├── MainApp.java
│   │   ├── model/           # Data models
│   │   ├── manager/         # Business logic
│   │   ├── logic/           # Calculations
│   │   ├── ui/              # 5 GUI pages
│   │   └── exceptions/      # Custom exceptions
│   ├── bin/                 # Compiled classes (auto-generated)
│   ├── build.sh            # Build script
│   └── README.md           # Quick start guide
├── src/                     # Legacy web app (optional)
├── web/                     # Legacy web frontend (optional)
└── compile.sh              # Legacy build script
```

**Budget**
- `GET /api/budget` - Get info
- `PUT /api/budget` - Update

**Events**
- `GET /api/events`
- `POST /api/events`
- `DELETE /api/events/{id}`

## System Requirements

- Java 8+
- Port 8000 (configurable in Main.java)
- Modern browser

## Building From Scratch

```bash
# 1. Compile using Java builder
javac Build.java
java Build

# 2. Run server
java -cp bin app.Main

# 3. Access
# Open http://localhost:8000
```

## Code Overview

| Class | Purpose |
|-------|---------|
| Main.java | Server initialization & routing |
| BudgetManager.java | Transaction & budget management |
| StaticFileHandler.java | Serve HTML/CSS/JS |
| TransactionHandler.java | Transaction API |
| BudgetHandler.java | Budget API |
| EventHandler.java | Events API |

## No Dependencies

Uses only Java standard library:
- `com.sun.net.httpserver` - HTTP server
- `java.time` - Dates  
- `java.util` - Collections
- `java.io` - File I/O

## Data Storage

All data is in-memory (ArrayLists):
- Persists only during runtime
- Ideal for single-user/testing
- Add database integration if needed

## Cleanup (Optional)

To remove unnecessary files and folders:

```bash
bash cleanup.sh
# Or manually remove 15+ unused files
# See FILES_TO_REMOVE.txt for details
```

## Production Ready

✅ Compiled & tested  
✅ CORS enabled  
✅ Error handling  
✅ Proper shutdown hooks  
✅ Simple architecture  

---

**Version**: 1.0.0  
**License**: ISC  
**Author**: SpenDEX Team

