# Sudoku Game

A fully functional Sudoku game with a graphical user interface built in Java using Swing.

## Features

- **Intuitive GUI**: Clean 9x9 grid with visual 3x3 box separators
- **Input Validation**: Real-time checking of Sudoku rules (rows, columns, boxes)
- **Interactive Gameplay**: 
  - Click cells and enter numbers 1-9
  - Fixed puzzle cells are highlighted and uneditable
  - Invalid moves are highlighted in red
- **Game Controls**:
  - **Reset**: Clear all your entries and start over
  - **Check Solution**: Verify if your solution is correct
  - **Show Solution**: Display the complete answer
  - **New Game**: Start a fresh puzzle
- **Visual Feedback**: Color-coded cells for fixed numbers, user entries, and errors

## Project Structure

```
SudokuGame/
├── README.md           # This file
├── src/                # Source code
│   └── game/
│       ├── SudokuBoard.java    # Game logic and board state
│       └── SudokuGUI.java      # Graphical interface
├── bin/                # Compiled classes
├── compile.sh          # Compilation script
└── run.sh              # Run script
```

## How to Compile

```bash
./compile.sh
```

Or manually:
```bash
javac -d bin src/game/*.java
```

## How to Run

```bash
./run.sh
```

Or manually:
```bash
java -cp bin game.SudokuGUI
```

## How to Play

1. Launch the game using the run script
2. Click on any white (editable) cell
3. Type a number from 1-9
4. Press Enter or click another cell
5. The game will validate your move:
   - ✅ Valid moves are accepted
   - ❌ Invalid moves show an error message
6. Fill all cells correctly to win!
7. Use the buttons at the bottom to reset, check, or solve the puzzle

## Requirements

- Java Development Kit (JDK) 8 or higher
- No external libraries required (uses Java Swing)

## Game Rules

Standard Sudoku rules apply:
- Each row must contain digits 1-9 without repetition
- Each column must contain digits 1-9 without repetition
- Each 3x3 box must contain digits 1-9 without repetition

## Development

The game consists of two main components:
- **SudokuBoard**: Handles game logic, validation, and state management
- **SudokuGUI**: Provides the graphical interface and user interaction

Feel free to extend the game with additional features like:
- Difficulty levels
- Timer and scoring
- Hint system
- Puzzle generator
- Save/load functionality