import java.util.Scanner;


public class SudokuGame {
     public static void main(String[] args) {
        SudokuBoard board = new SudokuBoard();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to Terminal Sudoku!");
        System.out.println("Instructions:");
        System.out.println("1. Enter row, column, and value separated by spaces");
        System.out.println("2. Use values 1-9 for numbers, 0 to erase");
        System.out.println("3. Type '0 0 0' to quit");
        System.out.println("4. Fixed numbers (starting clues) cannot be changed");
        System.out.println("5. Box boundaries are shown with | and -");
        
        while (true) {
            board.printBoard();
            
            if (board.isSolved()) {
                System.out.println("Congratulations! You solved the puzzle!");
                break;
            }
            
            System.out.print("\nEnter row(1-9), col(1-9), num(0-9): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            int num = scanner.nextInt();
            
            if (row == 0 && col == 0 && num == 0) {
                System.out.println("Quitting game...");
                break;
            }
            
            if (row < 1 || row > 9 || col < 1 || col > 9 || num < 0 || num > 9) {
                System.out.println("Invalid input! Values must be between 1-9 (0 to erase)");
                continue;
            }
            
            if (board.makeMove(row - 1, col - 1, num)) {
                System.out.println("Move accepted");
            } else {
                System.out.println("Invalid move! Try again.");
            }
        }
        scanner.close();
    }
}
