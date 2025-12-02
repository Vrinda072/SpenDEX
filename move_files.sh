#!/bin/bash

# Create necessary directories
mkdir -p src/main/java/com/spendex/{model,services,managers,web}

# Move and update package declarations
for file in src/*.java; do
    if [ -f "$file" ]; then
        # For files directly in src/
        sed -i '' 's/^package /package com.spendex./' "$file"
        mv "$file" "src/main/java/com/spendex/$(basename $file)"
    fi
done

for dir in src/*/; do
    if [ -d "$dir" ]; then
        dir_name=$(basename "$dir")
        for file in "$dir"*.java; do
            if [ -f "$file" ]; then
                # For files in subdirectories
                sed -i '' "s/^package ${dir_name}/package com.spendex.${dir_name}/" "$file"
                mv "$file" "src/main/java/com/spendex/${dir_name}/$(basename $file)"
            fi
        done
    fi
done

echo "Files moved and package declarations updated successfully!"
