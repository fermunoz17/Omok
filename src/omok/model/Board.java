package omok.model;

import java.awt.*;

/**
 * The type Board.
 * @autor Fernando Muñoz
 */
public class Board {
    private final int size = 15; // Assuming a 15x15 board
    private final Place[][] intersections = new Place[size][size];
    private Player currentPlayer;
    private Player player1;
    private Player player2;
    private boolean gameOver;

    /**
     * The Game type.
     */
    public int gameType;
    private final Place[][] winningStones = new Place[size][size];

    /**
     * Instantiates a new Board.
     */
    public Board() {
        initializePlayers();
        initializeBoard();
        resetWinningStones();
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

    private void resetWinningStones() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                winningStones[i][j] = new Place(i, j);
            }
        }
    }

    /**
     * Gets current player.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets opponent player.
     *
     * @return the opponent player
     */
    public Player getOpponentPlayer() {
        return (currentPlayer == player1) ? player2 : player1;
    }

    /**
     * Get intersections place [ ] [ ].
     *
     * @return the place [ ] [ ]
     */
    public Place[][] getIntersections() {
        return intersections;
    }

    /**
     * Get winning stones place [ ] [ ].
     *
     * @return the place [ ] [ ]
     */
    public Place[][] getWinningStones() {
        return winningStones;
    }

    /**
     * Place stone boolean.
     *
     * @param x         the x
     * @param y         the y
     * @param cpuPlayer the cpu player
     * @return the boolean
     */
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

    /**
     * Check for win boolean.
     *
     * @param placedX the placed x
     * @param placedY the placed y
     * @return the boolean
     */
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

    /**
     * Counts the number of consecutive stones in a given direction from a starting point.
     * This method is used to determine if a player has a winning sequence of stones.
     * The search is conducted in two linear directions, forward and backward,
     * starting from the given coordinates (x, y) and moving according to the
     * delta values (dx, dy) provided for each direction.
     * The count includes the stone at the starting position.
     *
     * The winning stones are recorded by marking them in the 'winningStones' array.
     *
     * @param x      the x-coordinate of the starting position
     * @param y      the y-coordinate of the starting position
     * @param dx     the change in x-coordinate for each step in the direction
     * @param dy     the change in y-coordinate for each step in the direction
     * @param player the player whose stones to count
     * @return the total count of consecutive stones in the specified direction
     */
    int countStonesInDirection(int x, int y, int dx, int dy, Player player) {
        int count = 1; // Start with 1 for the stone at (x, y)
        int nx = x + dx;
        int ny = y + dy;
        resetWinningStones();

        // Search in the forward direction
        while (isWithinBounds(nx, ny) && intersections[nx][ny].getPlayer() == player) {
            count++;
            winningStones[x][y] = intersections[x][y];
            winningStones[nx][ny] = intersections[nx][ny];
            nx += dx;
            ny += dy;
        }

        nx = x - dx;
        ny = y - dy;

        // Reset to start position and search in the backward direction
        while (isWithinBounds(nx, ny) && intersections[nx][ny].getPlayer() == player) {
            count++;
            winningStones[x][y] = intersections[x][y];
            winningStones[nx][ny] = intersections[nx][ny];
            nx -= dx;
            ny -= dy;
        }

        return count;
    }

    /**
     * Is game over boolean.
     *
     * @return  gameOver
     */
    public boolean isGameOver() {
        return gameOver;
    }


    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets current player.
     *
     * @param player the player
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * Sets game type.
     *
     * @param gameType the game type
     */
    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    /**
     * Gets game type.
     *
     * @return the game type
     */
    public int getGameType() {
        return gameType;
    }
}