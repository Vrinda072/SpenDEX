#!/bin/bash
# Cleanup script - removes unnecessary files from SpenDEX
# Removes ALL Python files, Node.js files, and build artifacts

cd /workspaces/SpenDEX

echo "🧹 SpenDEX Cleanup - Removing unnecessary files..."
echo ""

# Remove ALL Python files (*.py)
echo "Removing Python files..."
rm -f *.py build.py quick_compile.py run_server.py

# Remove Python-related build scripts
echo "Removing Python-related scripts..."
rm -f build_and_run.sh download_gson.sh get_gson.sh quick_build.sh run.sh setup_deps.sh start.sh

# Remove old migration scripts
echo "Removing legacy scripts..."
rm -f move_files.sh

# Remove Node.js files
echo "Removing Node.js files..."
rm -f package.json package-lock.json
rm -rf node_modules/

# Remove external library directory
echo "Removing lib/ directory..."
rm -rf lib/

# Remove old snapshots
echo "Removing .snapshots/ directory..."
rm -rf .snapshots/

# Remove unnecessary documentation
echo "Removing old documentation..."
rm -f BUILD_INSTRUCTIONS.md SETUP_COMPLETE.md

echo ""
echo "✅ Cleanup complete!"
echo ""
echo "📁 Remaining files in root:"
ls -1 | grep -v "^src$\|^web$\|^bin$\|^\." || true
echo ""
echo "✓ Project is now pure Java with zero dependencies"
echo "✓ All Python files removed"
echo "✓ All Node.js files removed"
