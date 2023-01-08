package fr.uge.patchwork;
import java.awt.Graphics2D;

public abstract class GraphicalObject implements IGameObject {
	protected float height;
	protected float width;
	protected float topLeftX;
	protected float topLeftY;
	
	private boolean _set;
	
	public void SetGraphicalProperties(float x, float y, float size) {
		SetGraphicalProperties(x, y, size, size);
	}
	
	public void SetGraphicalProperties(float x, float y, float w, float h) {
		
		topLeftX = x;
		topLeftY = y;
		height = h;
		width = w;		
		_set = true;
	}
	
	public final void draw(Graphics2D graphics) {
		if(!_set) {
			// jeter une erreur
		}
		onDraw(graphics);
	}
	
	protected abstract void onDraw(Graphics2D graphics);
}
