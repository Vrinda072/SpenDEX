#!/bin/bash
cd /workspaces/SpenDEX
mkdir -p bin
javac -d bin $(find src -type f -name "*.java") 2>&1
echo "Build complete"
