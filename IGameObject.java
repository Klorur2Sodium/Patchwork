package fr.uge.patchwork;

import java.awt.Graphics2D;

public interface IGameObject {
	void SetGraphicalProperties(float x, float y, float size);
	void SetGraphicalProperties(float x, float y, float width, float height); 
	void draw(Graphics2D graphics);
}
