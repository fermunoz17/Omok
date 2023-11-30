package omok.model;

import javax.swing.SwingUtilities;

/**
 * Main class to run the Omok game.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(OmokGame::new);
        new ChatServer().start();

    }
}
