#!/bin/bash

# Create necessary directories
mkdir -p bin

# Compile all Java files (no external dependencies needed!)
echo "Compiling SpenDEX Java project..."
javac -d bin $(find src -type f -name "*.java")

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo ""
    echo "To run the server:"
    echo "  java -cp bin app.Main"
else
    echo "✗ Compilation failed!"
    exit 1
fi
