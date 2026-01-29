#!/bin/bash
# Run Sudoku Game

if [ ! -d "bin/game" ]; then
    echo "Game not compiled yet. Compiling now..."
    ./compile.sh
    echo ""
fi

echo "Starting Sudoku Game..."
java -cp bin game.SudokuGUI
