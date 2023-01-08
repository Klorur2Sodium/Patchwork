package fr.uge.patchwork;

public class GraphicProperties {
	private final float _height;
	private final float _width;
	private final float _topLeftX;
	private final float _topLeftY;
	
	public GraphicProperties(float height, float width, float topLeftX, float topLeftY) {
		_height = height;
		_width = width;
		_topLeftX = topLeftX;
		_topLeftY = topLeftY;
	}
	
	public float getHeight() {
		return _height;
	}
	
	public float getWidth() {
		return _width;
	}
	
	public float getTopLeftX() {
		return _topLeftX;
	}
	
	public float getTopLeftY() {
		return _topLeftY;
	}
}
