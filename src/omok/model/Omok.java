package omok.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Omok extends JFrame {
    private Board omok;
    private BoardPanel gameBoard;
    private Player p1;
    private Player p2;
    private Player currentPlayer;
    private JRadioButton humanButton;
    private JRadioButton computerButton;

    public Omok() {
        super("Omok Game");
        initializeGame();
        GameStartScreen();
    }

    private void initializeGame() {
        omok = new Board();
        p1 = new Player("Mike", Color.black);
        p2 = new Player("Andre", Color.white);
        currentPlayer = p1;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 700));
        setLocationRelativeTo(null);
    }

    private void GameStartScreen() {
        var panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Select opponent: "));

        humanButton = new JRadioButton("Human");
        computerButton = new JRadioButton("Computer");
        humanButton.setSelected(true);


        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(humanButton);
        buttonGroup.add(computerButton);

        JButton playButton = new JButton("Play");
        panel.add(humanButton);
        panel.add(computerButton);
        panel.add(playButton);

        playButton.addActionListener(this::startGame);

        setContentPane(panel);
        pack();
        setVisible(true);
    }

    private void startGame(ActionEvent e) {
        if (humanButton.isSelected()) {
            startHumanGame();
        } else if (computerButton.isSelected()) {
            startComputerGame();
        }
    }

    private void startHumanGame() {
        gameBoard = new BoardPanel(omok);
        setContentPane(gameBoard);
        pack();
        setVisible(true);
    }

    private void startComputerGame() {
        // Computer game logic
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Omok::new);
    }
}
