package omok.model;

/**
 * The type Place.
 * @author Fernando Muñoz
 */
public class Place {
    private final int x;
    private final int y;
    private Player player;

    /**
     * Instantiates a new Place.
     *
     * @param x the x
     * @param y the y
     */
    public Place(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Is occupied boolean.
     *
     * @return the boolean
     */
    public boolean isOccupied() {
        return player != null;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets player.
     *
     * @param player the player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

}
