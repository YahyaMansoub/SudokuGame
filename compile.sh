#!/bin/bash
# Compile Sudoku Game

echo "Compiling Sudoku Game..."
javac -d bin src/game/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo "Run the game with: ./run.sh"
else
    echo "✗ Compilation failed!"
    exit 1
fi
