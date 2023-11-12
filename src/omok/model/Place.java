package omok.model;

public class Place {
    private final int x;
    private final int y;
    private Player player;

    public Place(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isOccupied() {
        return player != null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
