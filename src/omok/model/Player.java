package omok.model;

import java.awt.*;

/**
 * The type Player.
 * @author Fernando Muñoz
 */
public class Player {
    private final String name;
    private final Color color;

    /**
     * Instantiates a new Player.
     *
     * @param name  the name
     * @param color the color
     */
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }
}
