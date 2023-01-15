package fr.uge.patchwork;

/**
 * Represents the graphic properties of an object like its position on a window.
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class GraphicProperties {
	private final float _height;
	private final float _width;
	private final float _topLeftX;
	private final float _topLeftY;
	
	/**
	 * Constructs a new GraphicProperties object with its height, width and base coordinates.
	 * @param height : height in pixels.
	 * @param width : width in pixels.
	 * @param topLeftX : x coordinates of the top left corner.
	 * @param topLeftY : y coordinates of the top left corner.
	 */
	public GraphicProperties(float height, float width, float topLeftX, float topLeftY) {
		_height = height;
		_width = width;
		_topLeftX = topLeftX;
		_topLeftY = topLeftY;
	}
	
	/**
	 * Getter for the height.
	 * @return the height.
	 */
	public float getHeight() {
		return _height;
	}
	
	/**
	 * Getter for the width.
	 * @return the width.
	 */
	public float getWidth() {
		return _width;
	}
	
	/**
	 * Getter for the x coordinates of the top left corner.
	 * @return x coordinates of the top left corner.
	 */
	public float getTopLeftX() {
		return _topLeftX;
	}
	
	/**
	 * Getter for the y coordinates of the top left corner.
	 * @return y coordinates of the top left corner.
	 */
	public float getTopLeftY() {
		return _topLeftY;
	}
}
