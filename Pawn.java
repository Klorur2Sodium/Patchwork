package fr.uge.patchwork;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.Objects;

import fr.umlv.zen5.ApplicationContext;

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
    
    /**
     * The function return the Color corresponding to the pawn's color
     * to be able to draw it
     * @return Color 
     */
    private Color match() {
    	switch(_color) {
    		case BLUE : return Color.BLUE;
    		case RED : return Color.RED;
    		case GREEN: return Color.GREEN;
    		default : return Color.LIGHT_GRAY;
    	}
    }
    
    
    /**
     * The function draw a pawn on the coordinates (x, y)
     * @param context 
     * @param x
     * @param y
     */
    public void draw(ApplicationContext context, float x, float y) {
    	Ellipse2D.Float ellipse = new Ellipse2D.Float(x - 10, y - 10, 20, 20);
    	context.renderFrame(graphics -> {
    		graphics.setColor(match());
	        graphics.fill(ellipse);
	      });
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
