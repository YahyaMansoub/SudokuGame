package game;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuGUI extends JFrame {
    private SudokuBoard board;
    private JTextField[][] cells;
    private JButton resetButton, solveButton, checkButton, newGameButton;
    private JLabel statusLabel, titleLabel;
    
    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241);  // Indigo
    private static final Color SECONDARY_COLOR = new Color(79, 70, 229); // Dark indigo
    private static final Color ACCENT_COLOR = new Color(139, 92, 246);   // Purple
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);   // Green
    private static final Color ERROR_COLOR = new Color(239, 68, 68);     // Red
    private static final Color WARNING_COLOR = new Color(251, 146, 60);  // Orange
    
    private static final Color BG_COLOR = new Color(248, 250, 252);      // Light gray-blue
    private static final Color FIXED_COLOR = new Color(226, 232, 240);   // Light slate
    private static final Color EDITABLE_COLOR = Color.WHITE;
    private static final Color SELECTED_COLOR = new Color(224, 231, 255); // Light indigo
    private static final Color ERROR_CELL_COLOR = new Color(254, 226, 226); // Light red
    private static final Color GRID_COLOR = new Color(148, 163, 184);    // Slate
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);     // Dark slate
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139); // Medium slate
    
    public SudokuGUI() {
        board = new SudokuBoard();
        cells = new JTextField[9][9];
        
        setTitle("Sudoku - Modern Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 245, 255), 0, h, BG_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create title
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create grid panel with shadow
        JPanel gridContainer = new JPanel(new GridBagLayout());
        gridContainer.setOpaque(false);
        JPanel gridPanel = createGridPanel();
        gridContainer.add(gridPanel);
        mainPanel.add(gridContainer, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        titleLabel = new JLabel("SUDOKU", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JLabel subtitleLabel = new JLabel("Fill the grid with numbers 1-9", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        
        JPanel titleContainer = new JPanel(new BorderLayout(0, 5));
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel, BorderLayout.CENTER);
        titleContainer.add(subtitleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(titleContainer, BorderLayout.CENTER);
        return headerPanel;
    }
    
    private JPanel createGridPanel() {
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(Color.WHITE);
        outerPanel.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            BorderFactory.createLineBorder(GRID_COLOR, 3, true)
        ));
        
        JPanel gridPanel = new JPanel(new GridLayout(9, 9, 0, 0));
        gridPanel.setBackground(Color.WHITE);
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = createCell(row, col);
                gridPanel.add(cells[row][col]);
            }
        }
        
        outerPanel.add(gridPanel);
        updateGrid();
        return outerPanel;
    }
    
    private JTextField createCell(int row, int col) {
        JTextField cell = new JTextField();
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setFont(new Font("Segoe UI", Font.BOLD, 28));
        cell.setPreferredSize(new Dimension(65, 65));
        cell.setCaretColor(PRIMARY_COLOR);
        
        // Add thick borders for 3x3 boxes with modern colors
        int top = (row % 3 == 0) ? 3 : 1;
        int left = (col % 3 == 0) ? 3 : 1;
        int bottom = (row == 8) ? 3 : 1;
        int right = (col == 8) ? 3 : 1;
        cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, GRID_COLOR));
        
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
                    cell.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(top, left, bottom, right, GRID_COLOR),
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2)
                    ));
                }
            }
        });
        
        return cell;
    }
    
    private void updateCell(int row, int col) {
        JTextField cell = cells[row][col];
        String text = cell.getText().trim();
        
        // Get border dimensions
        int top = (row % 3 == 0) ? 3 : 1;
        int left = (col % 3 == 0) ? 3 : 1;
        int bottom = (row == 8) ? 3 : 1;
        int right = (col == 8) ? 3 : 1;
        
        if (board.isFixed(row, col)) {
            updateGrid();
            return;
        }
        
        if (text.isEmpty()) {
            board.makeMove(row, col, 0);
            cell.setBackground(EDITABLE_COLOR);
            cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, GRID_COLOR));
        } else {
            int num = Integer.parseInt(text);
            if (board.makeMove(row, col, num)) {
                cell.setBackground(EDITABLE_COLOR);
                cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, GRID_COLOR));
                statusLabel.setText("Move accepted");
                statusLabel.setForeground(SUCCESS_COLOR);
                
                if (board.isSolved()) {
                    showWinDialog();
                }
            } else {
                cell.setBackground(ERROR_CELL_COLOR);
                cell.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(top, left, bottom, right, GRID_COLOR),
                    BorderFactory.createLineBorder(ERROR_COLOR, 2)
                ));
                statusLabel.setText("Invalid move! This number conflicts with Sudoku rules.");
                statusLabel.setForeground(ERROR_COLOR);
            }
        }
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout(0, 15));
        buttonPanel.setOpaque(false);
        
        // Status label with modern styling
        statusLabel = new JLabel("Fill in the empty cells to solve the puzzle!");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        statusLabel.setForeground(TEXT_SECONDARY);
        buttonPanel.add(statusLabel, BorderLayout.NORTH);
        
        // Buttons with modern design
        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonsRow.setOpaque(false);
        
        resetButton = createModernButton("Reset", WARNING_COLOR);
        resetButton.addActionListener(e -> resetGame());
        
        checkButton = createModernButton("Check", SUCCESS_COLOR);
        checkButton.addActionListener(e -> checkSolution());
        
        solveButton = createModernButton("Solution", ACCENT_COLOR);
        solveButton.addActionListener(e -> showSolution());
        
        newGameButton = createModernButton("New Game", PRIMARY_COLOR);
        newGameButton.addActionListener(e -> newGame());
        
        buttonsRow.add(resetButton);
        buttonsRow.add(checkButton);
        buttonsRow.add(solveButton);
        buttonsRow.add(newGameButton);
        
        buttonPanel.add(buttonsRow, BorderLayout.CENTER);
        
        return buttonPanel;
    }
    
    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(140, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
        });
        
        return button;
    }
    
    private void updateGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = board.getValue(row, col);
                JTextField cell = cells[row][col];
                
                int top = (row % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int bottom = (row == 8) ? 3 : 1;
                int right = (col == 8) ? 3 : 1;
                
                if (value == 0) {
                    cell.setText("");
                } else {
                    cell.setText(String.valueOf(value));
                }
                
                if (board.isFixed(row, col)) {
                    cell.setBackground(FIXED_COLOR);
                    cell.setEditable(false);
                    cell.setForeground(TEXT_PRIMARY);
                    cell.setFont(new Font("Segoe UI", Font.BOLD, 28));
                } else {
                    cell.setBackground(EDITABLE_COLOR);
                    cell.setEditable(true);
                    cell.setForeground(PRIMARY_COLOR);
                    cell.setFont(new Font("Segoe UI", Font.PLAIN, 28));
                }
                
                cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, GRID_COLOR));
            }
        }
    }
    
    private void resetGame() {
        board.reset();
        updateGrid();
        statusLabel.setText("Game reset! Try again.");
        statusLabel.setForeground(TEXT_SECONDARY);
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
            statusLabel.setForeground(ACCENT_COLOR);
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
            statusLabel.setForeground(TEXT_SECONDARY);
        }
    }
    
    private void showWinDialog() {
        JOptionPane.showMessageDialog(this,
            "Congratulations! You solved the puzzle!",
            "Victory!",
            JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText("Puzzle solved! Click 'New Game' to play again.");
        statusLabel.setForeground(SUCCESS_COLOR);
    }
    
    // Custom shadow border for modern look
    static class ShadowBorder extends AbstractBorder {
        private static final Color SHADOW_COLOR = new Color(0, 0, 0, 30);
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw shadow
            for (int i = 0; i < 4; i++) {
                g2d.setColor(new Color(0, 0, 0, 10 - i * 2));
                g2d.drawRoundRect(x + i, y + i, width - 1 - i * 2, height - 1 - i * 2, 8, 8);
            }
            
            g2d.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 4, 4, 4);
        }
        
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = 4;
            return insets;
        }
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
