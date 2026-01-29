import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuGUI extends JFrame {
    private SudokuBoard board;
    private JTextField[][] cells;
    private JButton resetButton, solveButton, checkButton, newGameButton;
    private JLabel statusLabel;
    private static final Color FIXED_COLOR = new Color(200, 200, 200);
    private static final Color EDITABLE_COLOR = Color.WHITE;
    private static final Color SELECTED_COLOR = new Color(173, 216, 230);
    private static final Color ERROR_COLOR = new Color(255, 200, 200);
    
    public SudokuGUI() {
        board = new SudokuBoard();
        cells = new JTextField[9][9];
        
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create grid panel
        JPanel gridPanel = createGridPanel();
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(9, 9, 0, 0));
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = createCell(row, col);
                gridPanel.add(cells[row][col]);
            }
        }
        
        updateGrid();
        return gridPanel;
    }
    
    private JTextField createCell(int row, int col) {
        JTextField cell = new JTextField();
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setFont(new Font("Arial", Font.BOLD, 24));
        cell.setPreferredSize(new Dimension(60, 60));
        
        // Add thick borders for 3x3 boxes
        int top = (row % 3 == 0) ? 2 : 1;
        int left = (col % 3 == 0) ? 2 : 1;
        int bottom = (row == 8) ? 2 : 1;
        int right = (col == 8) ? 2 : 1;
        cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
        
        // Add input validation
        cell.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                    return;
                }
                if (c < '1' || c > '9') {
                    e.consume();
                }
                if (cell.getText().length() >= 1) {
                    e.consume();
                }
            }
        });
        
        // Handle cell updates
        cell.addActionListener(e -> updateCell(row, col));
        cell.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateCell(row, col);
            }
            
            @Override
            public void focusGained(FocusEvent e) {
                if (!board.isFixed(row, col)) {
                    cell.setBackground(SELECTED_COLOR);
                }
            }
        });
        
        return cell;
    }
    
    private void updateCell(int row, int col) {
        JTextField cell = cells[row][col];
        String text = cell.getText().trim();
        
        if (board.isFixed(row, col)) {
            updateGrid();
            return;
        }
        
        if (text.isEmpty()) {
            board.makeMove(row, col, 0);
            cell.setBackground(EDITABLE_COLOR);
        } else {
            int num = Integer.parseInt(text);
            if (board.makeMove(row, col, num)) {
                cell.setBackground(EDITABLE_COLOR);
                statusLabel.setText("Move accepted");
                statusLabel.setForeground(Color.BLACK);
                
                if (board.isSolved()) {
                    showWinDialog();
                }
            } else {
                cell.setBackground(ERROR_COLOR);
                statusLabel.setText("Invalid move! This number conflicts with Sudoku rules.");
                statusLabel.setForeground(Color.RED);
            }
        }
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 10));
        
        // Status label
        statusLabel = new JLabel("Fill in the empty cells to solve the puzzle!");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        buttonPanel.add(statusLabel, BorderLayout.NORTH);
        
        // Buttons
        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.addActionListener(e -> resetGame());
        
        checkButton = new JButton("Check Solution");
        checkButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkButton.addActionListener(e -> checkSolution());
        
        solveButton = new JButton("Show Solution");
        solveButton.setFont(new Font("Arial", Font.BOLD, 14));
        solveButton.addActionListener(e -> showSolution());
        
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 14));
        newGameButton.addActionListener(e -> newGame());
        
        buttonsRow.add(resetButton);
        buttonsRow.add(checkButton);
        buttonsRow.add(solveButton);
        buttonsRow.add(newGameButton);
        
        buttonPanel.add(buttonsRow, BorderLayout.CENTER);
        
        return buttonPanel;
    }
    
    private void updateGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = board.getValue(row, col);
                JTextField cell = cells[row][col];
                
                if (value == 0) {
                    cell.setText("");
                } else {
                    cell.setText(String.valueOf(value));
                }
                
                if (board.isFixed(row, col)) {
                    cell.setBackground(FIXED_COLOR);
                    cell.setEditable(false);
                    cell.setForeground(Color.BLACK);
                    cell.setFont(new Font("Arial", Font.BOLD, 24));
                } else {
                    cell.setBackground(EDITABLE_COLOR);
                    cell.setEditable(true);
                    cell.setForeground(new Color(0, 0, 200));
                    cell.setFont(new Font("Arial", Font.PLAIN, 24));
                }
            }
        }
    }
    
    private void resetGame() {
        board.reset();
        updateGrid();
        statusLabel.setText("Game reset! Try again.");
        statusLabel.setForeground(Color.BLACK);
    }
    
    private void checkSolution() {
        if (board.isSolved()) {
            showWinDialog();
        } else {
            boolean allFilled = true;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (board.getValue(i, j) == 0) {
                        allFilled = false;
                        break;
                    }
                }
            }
            if (allFilled) {
                JOptionPane.showMessageDialog(this,
                    "The puzzle is complete but incorrect. Keep trying!",
                    "Not Quite",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Keep going! The puzzle is not complete yet.",
                    "In Progress",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void showSolution() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to see the solution?",
            "Show Solution",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            board.showSolution();
            updateGrid();
            statusLabel.setText("Solution displayed!");
            statusLabel.setForeground(new Color(0, 128, 0));
        }
    }
    
    private void newGame() {
        int result = JOptionPane.showConfirmDialog(this,
            "Start a new game? Current progress will be lost.",
            "New Game",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            board = new SudokuBoard();
            updateGrid();
            statusLabel.setText("New game started! Good luck!");
            statusLabel.setForeground(Color.BLACK);
        }
    }
    
    private void showWinDialog() {
        JOptionPane.showMessageDialog(this,
            "Congratulations! You solved the puzzle!",
            "Victory!",
            JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText("Puzzle solved! Click 'New Game' to play again.");
        statusLabel.setForeground(new Color(0, 128, 0));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            SudokuGUI game = new SudokuGUI();
            game.setVisible(true);
        });
    }
}
