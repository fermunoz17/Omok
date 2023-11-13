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

    private int turnNumber = 1;
    private JLabel turnLabel;



    public OmokGame() {
        setupInitialUI();
    }
    private void setupInitialUI() {
        getContentPane().removeAll(); // When setupInitialUI is called after a game or during a game through the game menu
        setTitle("Omok Game Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        humanGameButton = new JButton("Human vs. Human");
        cpuGameButton = new JButton("Human vs. CPU");
        startButton = new JButton("Start Game");

        // Set the gameType when buttons are clicked
        humanGameButton.addActionListener(e -> setGameType(1));
        cpuGameButton.addActionListener(e -> setGameType(2));

        // Start the game when the Start button is clicked
        startButton.addActionListener(e -> startGame());

        buttonPanel.add(humanGameButton);
        buttonPanel.add(cpuGameButton);
        buttonPanel.add(startButton);

        add(buttonPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private int selectedGameType = 0; // Initialize with 0 (no game selected)

    private void setGameType(int gameType) {
        selectedGameType = gameType;
    }

    private void startGame() {
        turnNumber = 1;
        if (selectedGameType == 1) {
            startHumanGame(null);
        } else if (selectedGameType == 2) {
            startCpuGame(null);
        } else {
            // Show an error message if no game type is selected
            JOptionPane.showMessageDialog(this, "Please select a game type first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

        // Create a "Chose Game" item
        JMenuItem choseGame = new JMenuItem("Chose Game Mode");
        choseGame.addActionListener(e -> setupInitialUI());
        choseGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
        choseGame.setMnemonic(KeyEvent.VK_G); // Alt + Q will activate the item

        // Create an "About" item
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> aboutPopUp());
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        about.setMnemonic(KeyEvent.VK_A); // Alt + Q will activate the item

        // Create a "Rules" item
        JMenuItem rules = new JMenuItem("Rules");
        rules.addActionListener(e -> rules());
        rules.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
        rules.setMnemonic(KeyEvent.VK_U); // Alt + Q will activate the item


        // Create a "Quit Game" item
        JMenuItem quitMenuItem = new JMenuItem("Quit Game");
        quitMenuItem.addActionListener(e -> quitGame());
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        quitMenuItem.setMnemonic(KeyEvent.VK_Q); // Alt + Q will activate the item

        // Add items to the "Game" menu
        gameMenu.add(restartMenuItem);
        gameMenu.add(choseGame);
        gameMenu.add(rules);
        gameMenu.add(about);
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

    private void aboutPopUp() {
        JDialog aboutDialog = new JDialog(this, "About Omok Game", true);

        String aboutMessage = "Omok Game\n\n" +
                "Version: 1.0\n" +
                "Author: [Your Name]\n\n" +
                "Description:\n" +
                "Omok Game is a classic two-player strategy board game where " +
                "the objective is to be the first to get five of your own stones in a row, either horizontally, vertically, or diagonally, on a 15x15 grid. " +
                "This game offers two exciting game modes: Human vs. Human and Human vs. CPU.";

        JTextArea textArea = new JTextArea(aboutMessage);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        textArea.setFont(UIManager.getFont("Label.font"));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);

        aboutDialog.add(scrollPane);

        aboutDialog.pack();
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setVisible(true);
    }


    private void rules() {
        JDialog rulesDialog = new JDialog(this, "About Omok Game", true);

        String rules = "Objective: Be the first player to get five of your own stones in a row, either horizontally, vertically, or diagonally, on a 15x15 grid.\n" +
                "\n" +
                "Game Setup:\n" +
                "\n" +
                "Omok is typically played on a 15x15 grid.\n" +
                "Two players take turns: one uses black stones, and the other uses white stones.\n" +
                "Gameplay:\n" +
                "\n" +
                "Players take turns placing one of their stones on an empty intersection point on the grid.\n" +
                "The first player to create a continuous line of five of their stones in a row (horizontally, vertically, or diagonally) wins the game.";

        JTextArea textArea = new JTextArea(rules);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        textArea.setFont(UIManager.getFont("Label.font"));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);

        rulesDialog.add(scrollPane);

        rulesDialog.pack();
        rulesDialog.setLocationRelativeTo(this);
        rulesDialog.setVisible(true);
    }

    private void setupToolBar(Box menuAndToolbarPanel, int gameType) {
        // Create the toolbar and add buttons
        toolBar = new JToolBar();

        // Create the "Restart Game" button with an icon
        restartButton = new JButton();
        ImageIcon restartIcon = new ImageIcon("restart.png"); // Specify the correct path to your restart icon
        Image restartIconImage = restartIcon.getImage();
        Image scaledRestartIconImage = restartIconImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledRestartIcon = new ImageIcon(scaledRestartIconImage);
        restartButton.setIcon(scaledRestartIcon);
        restartButton.setToolTipText("Restart Game"); // Optional tooltip text

        // Create the "Exit Game" button with an icon
        exitButton = new JButton();
        ImageIcon exitIcon = new ImageIcon("exit.png"); // Specify the correct path to your exit icon
        Image exitIconImage = exitIcon.getImage();
        Image scaledExitIconImage = exitIconImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledExitIcon = new ImageIcon(scaledExitIconImage);
        exitButton.setIcon(scaledExitIcon);
        exitButton.setToolTipText("Exit Game"); // Optional tooltip text

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
                                displayTurn();
                                board.setCurrentPlayer(cpuPlayer);
                                handleCpuMove(cpuPlayer, humanPlayer); // It is now CPU's turn
                            }
                        }
                    }
                }
            }
        });
    }

    private void displayTurn() {
        String currentPlayerName = board.getCurrentPlayer().getName();

        // Set the text for the turnLabel
        turnLabel.setText("Turn " + turnNumber + ": " + currentPlayerName + "'s Turn");
        turnNumber++; // Increment the turn number
    }

    private void handleCpuMove(Player cpuPlayer, Player humanPlayer) {
        Point cpuMove = findCpuMove(cpuPlayer, humanPlayer);
        if (board.placeStone(cpuMove.x, cpuMove.y, cpuPlayer)) {
            boardPanel.repaint();
            if (board.checkForWin(cpuMove.x, cpuMove.y)) {
                handleGameOver(cpuPlayer, 2);
            } else {
                turnNumber++;
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
        setupGameUI();
        setupMenuBar(board.getGameType());

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
                                    displayTurn(); // Display the current turn
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

        // Create and add the turnLabel to the SOUTH of the main frame
        turnLabel = new JLabel();
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(turnLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OmokGame::new);
    }
}
