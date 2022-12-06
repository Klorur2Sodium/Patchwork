package fr.uge.patchwork;

import java.util.Objects;

/**
 * This class stores the information about the pawn of a player,
 * like its position or its color.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class Pawn {
    private final String _color;
    
    /**
     * Constructs a new Pawn with the given non null color
     * 
     * @param color : color of the pawn
     */
    public Pawn(String color) {
        Objects.requireNonNull(color);
        _color = color;
    }
    
    @Override
    public String toString() {
        return _color.substring(0, 1);
    }
}
