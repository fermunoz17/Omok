package omok.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OmokGame extends JFrame {
    private Board board;
    private BoardPanel boardPanel;
    private JButton humanGameButton;
    private JButton cpuGameButton;

    private JButton startButton;

    public OmokGame() {
        setupInitialUI();
    }

    private void setupInitialUI() {
        setTitle("Omok Game Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        humanGameButton = new JButton("Human vs. Human");
        cpuGameButton = new JButton("Human vs. CPU");
        startButton = new JButton("Start Game");

        humanGameButton.addActionListener(this::startHumanGame);
        cpuGameButton.addActionListener(this::startCpuGame);



        add(humanGameButton);
        add(cpuGameButton);

        pack();
        setVisible(true);
    }

    private void setupMenuBar(int gameType) {
        JMenuBar menuBar = new JMenuBar();

        // Create a "Game" menu
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G); // Alt + G will activate the menu

        // Create a "Restart Game" item
        JMenuItem restartMenuItem = new JMenuItem("Restart Game");
        restartMenuItem.addActionListener(e -> restartGame(gameType));
        restartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        restartMenuItem.setMnemonic(KeyEvent.VK_R); // Alt + R will activate the item

        // Create a "Quit Game" item
        JMenuItem quitMenuItem = new JMenuItem("Quit Game");
        quitMenuItem.addActionListener(e -> quitGame());
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        quitMenuItem.setMnemonic(KeyEvent.VK_Q); // Alt + Q will activate the item

        // Add items to the "Game" menu
        gameMenu.add(restartMenuItem);
        gameMenu.add(quitMenuItem);

        // Add the "Game" menu to the menu bar
        menuBar.add(gameMenu);

        setJMenuBar(menuBar);
    }


    private void startCpuGame(ActionEvent e) {
        board = new Board(); // Reset or initialize the board
        boardPanel = new BoardPanel(board);
        setupGameUI();

        setupMenuBar(2); // Pass game type as 2 (human vs. CPU)

        Player humanPlayer = new Player("Human", Color.BLACK);
        Player cpuPlayer = new Player("CPU", Color.WHITE);
        board.setCurrentPlayer(humanPlayer);  // Human starts first

        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (board.getCurrentPlayer().equals(humanPlayer) && !board.isGameOver()) {
                    int cellWidth = boardPanel.getCellWidth();
                    int col = (e.getX() - BoardPanel.PADDING) / cellWidth;
                    int row = (e.getY() - BoardPanel.PADDING) / cellWidth;

                    if (row >= 0 && row < board.getSize() && col >= 0 && col < board.getSize()) {
                        if (board.placeStone(col, row, humanPlayer)) {
                            boardPanel.repaint();
                            if (board.checkForWin(col, row)) {
                                handleGameOver(humanPlayer, 2);
                            } else {
                                board.setCurrentPlayer(cpuPlayer);
                                handleCpuMove(cpuPlayer, humanPlayer); // It is now CPU's turn
                            }
                        }
                    }
                }
            }
        });
    }

    private void handleCpuMove(Player cpuPlayer, Player humanPlayer) {
        Point cpuMove = findCpuMove(cpuPlayer, humanPlayer);
        if (board.placeStone(cpuMove.x, cpuMove.y, cpuPlayer)) {
            boardPanel.repaint();
            if (board.checkForWin(cpuMove.x, cpuMove.y)) {
                handleGameOver(cpuPlayer, 2);
            } else {
                board.setCurrentPlayer(humanPlayer); // Switch to human's turn
            }
        }
    }

    private Point findCpuMove(Player cpuPlayer, Player humanPlayer) {
        // First, check if the human player has a potential win with 4 stones in a row
        Place[][] intersections = board.getIntersections(); // Access the intersections array
        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                if (intersections[x][y].getPlayer() == null) {
                    // Check if placing a stone here creates a potential win for the human player
                    intersections[x][y].setPlayer(humanPlayer); // Temporarily place a human's stone
                    if (board.checkForWin(x, y)) {
                        intersections[x][y].setPlayer(null); // Remove the temporary stone
                        return new Point(x, y); // Block the human's potential win
                    }
                    intersections[x][y].setPlayer(null); // Remove the temporary stone
                }
            }
        }

        // If no immediate threat from the human player, prioritize placing a stone in the center
        int center = board.getSize() / 2;
        if (intersections[center][center].getPlayer() == null) {
            return new Point(center, center); // Place CPU's stone in the center
        }

        // If the center is occupied, place a stone in the first available empty spot
        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                if (intersections[x][y].getPlayer() == null) {
                    return new Point(x, y); // Place CPU's stone in the first available spot
                }
            }
        }

        return null; // No valid move found (shouldn't happen unless the board is full)
    }

    private void startHumanGame(ActionEvent e) {
        board = new Board();
        boardPanel = new BoardPanel(board);
        final Player[] currentPlayer = {board.getCurrentPlayer()}; // Declare currentPlayer as a final array

        setupMenuBar(1); // Pass game type as 1 (human vs. human)

        setupGameUI();

        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cellWidth = boardPanel.getCellWidth();
                int offsetX = e.getX() - BoardPanel.PADDING;
                int offsetY = e.getY() - BoardPanel.PADDING;

                if (offsetX >= 0 && offsetY >= 0) {
                    int col = offsetX / cellWidth;
                    int row = offsetY / cellWidth;

                    if (row < board.getSize() && col < board.getSize()) {
                        if (board.placeStone(col, row, currentPlayer[0])) { // Access currentPlayer from the array
                            boardPanel.repaint();
                            if (board.checkForWin(col, row)) {
                                handleGameOver(currentPlayer[0], 1);
                            }
                            currentPlayer[0] = board.getCurrentPlayer(); // Update currentPlayer
                        }
                    }
                }
            }
        });
    }

    private void handleGameOver(Player winner, int gameType) {
        Object[] options = {"Quit Game", "Restart Game", "Choose Mode"};
        int choice = JOptionPane.showOptionDialog(
                boardPanel,
                "Game Over. " + winner.getName() + " wins!\nWhat would you like to do?",
                "Game Over",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        switch (choice) {
            case JOptionPane.YES_OPTION:
                System.exit(0);
                break;
            case JOptionPane.NO_OPTION:
                restartGame(gameType);
                break;
            case JOptionPane.CANCEL_OPTION:
                setupInitialUI();
                break;
            default:
                break;
        }
    }
    private void restartGame(int gameType) {
        int confirm = JOptionPane.showConfirmDialog(
                boardPanel,
                "Are you sure you want to start a new game?",
                "Confirm Restart",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (gameType == 1) {
                startHumanGame(null);
            } else if (gameType == 2) {
                startCpuGame(null);
            }
        }
    }

    private void quitGame() {
        int confirm = JOptionPane.showConfirmDialog(
                boardPanel,
                "Are you sure you want to quit the game?",
                "Confirm Quit",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0); // Exit the game
        }
    }


    private void setupGameUI() {
        setTitle("Omok Game");
        getContentPane().removeAll();
        getContentPane().add(boardPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OmokGame::new);
    }
}
