package game;

import java.util.HashSet;
import java.util.Set;

public class SudokuBoard {
    private int[][] board;
    private boolean[][] fixed;
    private int[][] solution;


    public SudokuBoard(){
        board = new int[9][9];
        fixed = new boolean[9][9];
        solution = new int[9][9];
        initializeBoard();
    }


    private void initializeBoard(){
        int[][] puzzle = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        int[][] fullSolution = {
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        for(int i=0; i<9;i++){
            for(int j=0; j<9;j++){
                board[i][j]=puzzle[i][j];
                solution[i][j]=fullSolution[i][j];
                if(board[i][j]!=0){
                    fixed[i][j]=true;
                }
            }
        }
    }
    public void printBoard(){
        System.out.println("\n  1 2 3   4 5 6   7 8 9");
        System.out.println(" +-------+-------+-------+");

        for(int i=0;i<9;i++){
            System.out.print((i+1)+ "|");
            for(int j=0;j<9;j++){
                if(board[i][j]==0){
                    System.out.print(". ");
                } else{
                    System.out.print(board[i][j] + " ");
                }
                if (j == 2 || j == 5) {
                    System.out.print("| ");
                }
            }
            System.out.println("|");
            if (i == 2 || i == 5) {
                System.out.println(" +-------+-------+-------+");
            }
        }
        System.out.println(" +-------+-------+-------+");
        
    }

     public boolean makeMove(int row, int col, int num) {
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            return false;
        }
        
        if (fixed[row][col]) {
            return false;
        }
        
        if (num != 0 && !isValidPlacement(row, col, num)) {
            return false;
        }
        
        board[row][col] = num;
        return true;
    }
    
    private boolean isValidPlacement(int row, int col, int num) {
        // Check row
        for (int j = 0; j < 9; j++) {
            if (j != col && board[row][j] == num) {
                return false;
            }
        }
        
        // Check column
        for (int i = 0; i < 9; i++) {
            if (i != row && board[i][col] == num) {
                return false;
            }
        }
        
        // Check 3x3 box
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;
        
        for (int i = boxRowStart; i < boxRowStart + 3; i++) {
            for (int j = boxColStart; j < boxColStart + 3; j++) {
                if ((i != row || j != col) && board[i][j] == num) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public boolean isSolved() {
        // Check all cells filled
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        
        // Check all rows valid
        for (int i = 0; i < 9; i++) {
            if (!isRowValid(i)) return false;
        }
        
        // Check all columns valid
        for (int j = 0; j < 9; j++) {
            if (!isColValid(j)) return false;
        }
        
        // Check all boxes valid
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                if (!isBoxValid(i, j)) return false;
            }
        }
        
        return true;
    }
    
    private boolean isRowValid(int row) {
        Set<Integer> seen = new HashSet<>();
        for (int j = 0; j < 9; j++) {
            int num = board[row][j];
            if (num != 0 && seen.contains(num)) {
                return false;
            }
            seen.add(num);
        }
        return true;
    }
    
    private boolean isColValid(int col) {
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            int num = board[i][col];
            if (num != 0 && seen.contains(num)) {
                return false;
            }
            seen.add(num);
        }
        return true;
    }
    
    private boolean isBoxValid(int startRow, int startCol) {
        Set<Integer> seen = new HashSet<>();
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                int num = board[i][j];
                if (num != 0 && seen.contains(num)) {
                    return false;
                }
                seen.add(num);
            }
        }
        return true;
    }

    public int getValue(int row, int col) {
        return board[row][col];
    }

    public boolean isFixed(int row, int col) {
        return fixed[row][col];
    }

    public void reset() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!fixed[i][j]) {
                    board[i][j] = 0;
                }
            }
        }
    }

    public void showSolution() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = solution[i][j];
            }
        }
    }

}
