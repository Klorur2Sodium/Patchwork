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
    private final Constants _color;
    
    /**
     * Constructs a new Pawn with the given non null color
     * 
     * @param color : color of the pawn
     */
    public Pawn(String color) {
        Objects.requireNonNull(color);
        switch(color) {
	        case("Blue") -> _color = Constants.BLUE;
	        case("Red") -> _color = Constants.RED;
	        default -> _color = Constants.GREEN;
        }
    }
    
    @Override
    public String toString() {
    	switch(_color) {
	    	case BLUE : return "B";
	    	case RED : return "R";
	    	case GREEN : return "G";
	    	default:
	    		return " ";
    	}
    }
}
