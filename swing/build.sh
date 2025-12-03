#!/bin/bash

# SpendEX Build and Run Script

echo "╔════════════════════════════════════╗"
echo "║  SpendEX — Build & Run             ║"
echo "╚════════════════════════════════════╝"

cd "$(dirname "$0")"

# Create bin directory if it doesn't exist
if [ ! -d bin ]; then
    mkdir -p bin
    echo "✓ Created bin directory"
fi

# Compile
echo ""
echo "Compiling SpendEX..."
javac -d bin $(find src -type f -name "*.java") 2>&1

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo ""
    echo "╔════════════════════════════════════╗"
    echo "║  Launching SpendEX...              ║"
    echo "╚════════════════════════════════════╝"
    echo ""
    
    # Run
    java -cp bin MainApp
else
    echo "✗ Compilation failed!"
    exit 1
fi
