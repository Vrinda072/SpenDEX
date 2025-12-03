# SpenDEX - Budget Management System

Pure Java web application for tracking expenses and managing budgets. **Zero external dependencies.**

## Quick Start

```bash
# Compile (pure Java)
javac Build.java && java Build

# Or run directly
java -cp bin app.Main
```

## Features

- ✅ Track income & expenses
- ✅ Budget management by category  
- ✅ Daily spending limits
- ✅ Calendar event integration
- ✅ Responsive web UI
- ✅ In-memory data storage

## Project Structure

```
SpenDEX/
├── src/                     # Java source (14 files)
│   ├── Main.java
│   ├── managers/            # Business logic
│   ├── model/               # Data models
│   ├── services/            # Utilities
│   └── web/                 # HTTP handlers
├── web/                     # Frontend assets
│   ├── *.html               # 6 HTML pages
│   ├── css/
│   └── js/
├── bin/                     # Compiled output (auto-generated)
├── compile.sh              # Build script
├── start                   # Run script
└── README.md               # This file
```

## API Endpoints

**Transactions**
- `GET /api/transactions` - Get all
- `POST /api/transactions` - Add
- `DELETE /api/transactions/{id}` - Delete

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

