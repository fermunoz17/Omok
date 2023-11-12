package omok.model;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BoardPanel extends JPanel {
    public static final int BOARD_SIZE = 15;
    public static final int CELL_SIZE = 30;
    public static final int PADDING = 20;
    private final Board board;

    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension((BOARD_SIZE * CELL_SIZE) + (2 * PADDING), (BOARD_SIZE * CELL_SIZE) + (2 * PADDING)));
    }

    public int getCellWidth() {
        int totalPadding = 2 * PADDING;
        return (getWidth() - totalPadding) / board.getSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawStones(g);
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
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
                    int x = PADDING + i * CELL_SIZE - CELL_SIZE / 2;
                    int y = PADDING + j * CELL_SIZE - CELL_SIZE / 2;
                    Player player = place.getPlayer();
                    g.setColor(player.getColor());
                    g.fillOval(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

//    private void highlightWinningRow(Graphics g) {
//        List<Place> winningRow = board.getWinningRow();
//        if (winningRow != null) {
//            g.setColor(Color.RED); // You can change the color as needed
//            for (Place place : winningRow) {
//                int x = PADDING + place.getX() * CELL_SIZE;
//                int y = PADDING + place.getY() * CELL_SIZE;
//                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
//            }
//        }
//    }
}
