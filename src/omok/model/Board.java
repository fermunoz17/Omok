package omok.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size = 15; // Assuming a 15x15 board
    private final Place[][] intersections = new Place[size][size];
    private Player currentPlayer;
    private Player player1;
    private Player player2;
    private boolean gameOver;
    private List<Place> winningStones;


    public Board() {
        initializePlayers();
        initializeBoard();
        currentPlayer = player1; // Player 1 starts the game
    }

    private void initializePlayers() {
        player1 = new Player("Player 1", Color.BLACK);
        player2 = new Player("Player 2", Color.WHITE);
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                intersections[i][j] = new Place(i, j);
            }
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpponentPlayer() {
        return (currentPlayer == player1) ? player2 : player1;
    }

    public Place[][] getIntersections() {
        return intersections;
    }
    public boolean placeStone(int x, int y, Player cpuPlayer) {
        if (isWithinBounds(x, y) && intersections[x][y].getPlayer() == null && !gameOver) {
            intersections[x][y].setPlayer(currentPlayer);
            if (checkForWin(x, y)) {
                gameOver = true;
            } else {
                togglePlayer();
            }
            return true;
        }
        return false; // Spot is already taken, out of bounds, or game over
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    private void togglePlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public boolean checkForWin(int placedX, int placedY) {
        Player player = intersections[placedX][placedY].getPlayer();
        if (player == null) {
            return false;
        }

        if (countStonesInDirection(placedX, placedY, 1, 0, player) >= 5 || // Horizontal
                countStonesInDirection(placedX, placedY, 0, 1, player) >= 5 || // Vertical
                countStonesInDirection(placedX, placedY, 1, 1, player) >= 5 || // Diagonal \
                countStonesInDirection(placedX, placedY, 1, -1, player) >= 5) { // Diagonal /

            return true;
        }

        return false;
    }


    int countStonesInDirection(int x, int y, int dx, int dy, Player player) {
        int count = 1; // Start with 1 for the stone at (x, y)
        int nx = x + dx;
        int ny = y + dy;

        while (isWithinBounds(nx, ny) && intersections[nx][ny].getPlayer() == player) {
            count++;
            nx += dx;
            ny += dy;
        }

        nx = x - dx;
        ny = y - dy;

        while (isWithinBounds(nx, ny) && intersections[nx][ny].getPlayer() == player) {
            count++;
            nx -= dx;
            ny -= dy;
        }

        return count;
    }

    private List<Place> getWinningRow(int x, int y) {
        List<Place> row = new ArrayList<>();
        Player player = intersections[x][y].getPlayer();

        // Horizontal
        if (countStonesInDirection(x, y, 1, 0, player) >= 5) {
            for (int i = x - 4; i <= x + 4; i++) {
                row.add(intersections[i][y]);
            }
        }
        // Vertical
        else if (countStonesInDirection(x, y, 0, 1, player) >= 5) {
            for (int j = y - 4; j <= y + 4; j++) {
                row.add(intersections[x][j]);
            }
        }
        // Diagonal \
        else if (countStonesInDirection(x, y, 1, 1, player) >= 5) {
            for (int i = x - 4, j = y - 4; i <= x + 4; i++, j++) {
                row.add(intersections[i][j]);
            }
        }
        // Diagonal /
        else if (countStonesInDirection(x, y, 1, -1, player) >= 5) {
            for (int i = x - 4, j = y + 4; i <= x + 4; i++, j--) {
                row.add(intersections[i][j]);
            }
        }

        return row;
    }

    public boolean isGameOver() {
        return gameOver;
    }


    public int getSize() {
        return size;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public List<Place> getWinningStones() {
        return winningStones;
    }
}