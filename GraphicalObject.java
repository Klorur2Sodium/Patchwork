package fr.uge.patchwork;
import java.awt.Graphics2D;
import java.util.Objects;

/**
 * Represents a graphical object with its size and position on the screen.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public abstract class GraphicalObject implements IGameObject {
	protected float height;
	protected float width;
	protected float topLeftX;
	protected float topLeftY;
	
	private boolean _set;
	
	/**
	 * Initializes a GraphicalObject in a square.
	 * @param x : x coordinates of the GraphicalObject's top left corner.
	 * @param y : y coordinates of the GraphicalObject's top left corner.
	 * @param size : size of the GraphicalObject.
	 */
	public void SetGraphicalProperties(float x, float y, float size) {
		if (x < 0 || y < 0 || size < 0) {
			throw new IllegalArgumentException("Invalid argument");
		}
		SetGraphicalProperties(x, y, size, size);
	}
	
	/**
	 * Initializes a GraphicalObject in a rectangle.
	 * @param x : x coordinates of the GraphicalObject's top left corner.
	 * @param y : y coordinates of the GraphicalObject's top left corner.
	 * @param w : width of the GraphicalObject.
	 * @param h : height of the GraphicalObject.
	 */
	public void SetGraphicalProperties(float x, float y, float w, float h) {
		if (x < 0 || y < 0 || w < 0 || h < 0) {
			throw new IllegalArgumentException("Invalid argument");
		}
		topLeftX = x;
		topLeftY = y;
		height = h;
		width = w;		
		_set = true;
	}
	
	/**
	 * Draw a GraphicalObject by calling its onDraw() function.
	 * @param  graphics : object that calls the graphic methods.
	 */
	public final void draw(Graphics2D graphics) {
		Objects.requireNonNull(graphics);
		if(!_set) {
			// jeter une erreur
		}
		onDraw(graphics);
	}
	
	protected abstract void onDraw(Graphics2D graphics);
}
