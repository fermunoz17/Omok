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

    private JToolBar toolBar;
    private JButton restartButton;
    private JButton exitButton;

    public OmokGame() {
        setupInitialUI();
    }

    private void setupInitialUI() {
        setTitle("Omok Game Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        humanGameButton = new JButton("Human vs. Human");
        cpuGameButton = new JButton("Human vs. CPU");

        humanGameButton.addActionListener(this::startHumanGame);
        cpuGameButton.addActionListener(this::startCpuGame);

        buttonPanel.add(humanGameButton);
        buttonPanel.add(cpuGameButton);

        add(buttonPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
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

        setJMenuBar(menuBar); // Set the new menu bar

        // Create a Box container to hold both the MenuBar and the toolbar horizontally
        Box menuAndToolbarBox = Box.createHorizontalBox();
        menuAndToolbarBox.add(menuBar);
        menuAndToolbarBox.add(Box.createHorizontalStrut(10)); // Add spacing
        setupToolBar(menuAndToolbarBox, gameType); // Add the buttons to the toolbar

        // Add the menuAndToolbarBox to the NORTH of the main frame
        add(menuAndToolbarBox, BorderLayout.NORTH);
    }


    private void setupToolBar(Box menuAndToolbarPanel, int gameType) {
        // Create the toolbar and add buttons
        toolBar = new JToolBar();
        restartButton = new JButton("Restart Game");
        exitButton = new JButton("Exit Game");

        restartButton.addActionListener(e -> restartGame(board != null ? board.getGameType() : 0));
        exitButton.addActionListener(e -> quitGame());

        toolBar.add(restartButton);
        toolBar.add(exitButton);

        // Add the toolbar to the menuAndToolbarPanel
        menuAndToolbarPanel.add(toolBar);
    }

    private void startCpuGame(ActionEvent e) {
        board = new Board(); // Reset or initialize the board
        boardPanel = new BoardPanel(board);

        board.setGameType(2);
        setupGameUI();
        setupMenuBar(board.getGameType());
//        setupToolBar(2);

        Player humanPlayer = new Player("Human", Color.darkGray);
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
        board.setGameType(1);
        setupMenuBar(board.getGameType()); // Pass game type as 1 (human vs. human)
        setupGameUI();

        Player humanPlayer1 = new Player("Player 1", Color.darkGray);
        Player humanPlayer2 = new Player("Player 2", Color.WHITE);
        board.setCurrentPlayer(humanPlayer1); // Player 1 starts first

        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!board.isGameOver()) {
                    int cellWidth = boardPanel.getCellWidth();
                    int offsetX = e.getX() - BoardPanel.PADDING;
                    int offsetY = e.getY() - BoardPanel.PADDING;

                    if (offsetX >= 0 && offsetY >= 0) {
                        int col = offsetX / cellWidth;
                        int row = offsetY / cellWidth;

                        if (row < board.getSize() && col < board.getSize()) {
                            Player currentPlayer = board.getCurrentPlayer();
                            if (board.placeStone(col, row, currentPlayer)) {
                                boardPanel.repaint();
                                if (board.checkForWin(col, row)) {
                                    handleGameOver(currentPlayer, 1);
                                } else {
                                    // Switch to the other player
                                    board.setCurrentPlayer(currentPlayer.equals(humanPlayer1) ? humanPlayer2 : humanPlayer1);
                                }
                            }
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
                // Create a custom dialog to cover the entire screen
                JDialog chooseModeDialog = new JDialog(this, "Choose Mode", Dialog.ModalityType.APPLICATION_MODAL);
                chooseModeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Create a panel for the options
                JPanel optionsPanel = new JPanel();
                optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
                optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                // Add buttons to the panel
                JButton humanVsHumanButton = new JButton("Human vs. Human");
                JButton humanVsCpuButton = new JButton("Human vs. CPU");
                JButton cancelButton = new JButton("Cancel");

                // Add action listeners to the buttons
                humanVsHumanButton.addActionListener(e -> {
                    chooseModeDialog.dispose(); // Close the dialog
                    startHumanGame(null); // Start a new human vs. human game
                });
                humanVsCpuButton.addActionListener(e -> {
                    chooseModeDialog.dispose(); // Close the dialog
                    startCpuGame(null); // Start a new human vs. CPU game
                });
                cancelButton.addActionListener(e -> chooseModeDialog.dispose()); // Close the dialog

                // Add buttons to the panel
                optionsPanel.add(humanVsHumanButton);
                optionsPanel.add(humanVsCpuButton);
                optionsPanel.add(cancelButton);

                // Add the panel to the dialog
                chooseModeDialog.add(optionsPanel);

                // Set the dialog size to cover the entire screen
//                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//                chooseModeDialog.setSize(screenSize);
                chooseModeDialog.setLocationRelativeTo(null); // Center the dialog
                chooseModeDialog.setVisible(true);
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
