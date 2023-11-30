package omok.model;

import javax.swing.*;
import java.awt.*;

/**
 * The type Board panel.
 * @author Fernando Muñoz
 */
public class BoardPanel extends JPanel {
    /**
     * The constant BOARD_SIZE.
     */
    public static final int BOARD_SIZE = 15;
    /**
     * The constant CELL_SIZE.
     */
    public static final int CELL_SIZE = 30;
    /**
     * The constant PADDING.
     */
    public static final int PADDING = 20;
    private final Board board;
    private static final int STONE_RADIUS = CELL_SIZE / 2;

    /**
     * Instantiates a new Board panel.
     *
     * @param board the board
     */
    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension((BOARD_SIZE * CELL_SIZE) + (2 * PADDING), (BOARD_SIZE * CELL_SIZE) + (2 * PADDING)));
    }

    /**
     * Gets cell width.
     *
     * @return the cell width
     */
    public int getCellWidth() {
        int totalPadding = 2 * PADDING;
        return (getWidth() - totalPadding) / board.getSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawBoard(g);
        drawStones(g);
        highlightStones(g);
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.gray);
        for (int i = 0; i < BOARD_SIZE; i++) {
            g.drawLine(PADDING, PADDING + i * CELL_SIZE, PADDING + (BOARD_SIZE - 1) * CELL_SIZE, PADDING + i * CELL_SIZE);
            g.drawLine(PADDING + i * CELL_SIZE, PADDING, PADDING + i * CELL_SIZE, PADDING + (BOARD_SIZE - 1) * CELL_SIZE);
        }
    }

    private void drawStones(Graphics g) {
        Place[][] intersections = board.getIntersections();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Place place = intersections[i][j];
                if (place.isOccupied()) {
                    int x = PADDING + i * CELL_SIZE - STONE_RADIUS;
                    int y = PADDING + j * CELL_SIZE - STONE_RADIUS;
                    Player player = place.getPlayer();

                    g.setColor(player.getColor());
                    g.fillOval(x, y, 2 * STONE_RADIUS, 2 * STONE_RADIUS);
                }
            }
        }
    }

    private void highlightStones(Graphics g) {
        Place[][] intersections = board.getWinningStones();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Place place = intersections[i][j];
                if (place.isOccupied()) {
                    int x = PADDING + i * CELL_SIZE - STONE_RADIUS;
                    int y = PADDING + j * CELL_SIZE - STONE_RADIUS;
                    Player player = place.getPlayer();

                    g.setColor(Color.BLACK);
                    g.drawOval(x, y, 2 * STONE_RADIUS, 2 * STONE_RADIUS);

                    g.setColor(Color.GREEN);
                    g.fillOval(x, y, 2 * STONE_RADIUS, 2 * STONE_RADIUS);
                }
            }
        }
    }
}
