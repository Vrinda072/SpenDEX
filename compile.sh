#!/bin/bash

# Create necessary directories
mkdir -p bin

# Compile all Java files with Gson in the classpath
find src -name "*.java" > sources.txt
javac -d bin -cp ".:lib/*" @sources.txt

# Clean up
rm sources.txt

echo "Compilation complete. If no errors, you can run the application with:"
echo "java -cp \"bin:lib/*\" Main"
