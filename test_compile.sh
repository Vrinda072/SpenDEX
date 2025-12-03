#!/bin/bash
cd /workspaces/SpenDEX
echo "Compiling SpenDEX Java project..."
javac -d bin $(find src -type f -name "*.java") 2>&1
if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo "Starting application..."
    java -cp bin app.Main
else
    echo "✗ Compilation failed!"
fi
