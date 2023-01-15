package fr.uge.patchwork;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Menu extends GraphicalObject {
	
	@Override
	protected void onDraw(Graphics2D graphics) {
		int yPadding = 50;
		var x = topLeftX + 10;
		var y = topLeftY + 25;
		graphics.setFont(new Font("default", Font.BOLD, 25));
		int menuWidth = getStringWidth(graphics, "Menu");
		var cube = new Rectangle2D.Float(topLeftX, topLeftY, topLeftX + width, height);
		graphics.draw(cube);
		graphics.drawString("Menu", (topLeftX + width - menuWidth) / 2, y);
		y += yPadding;
		graphics.setFont(new Font("default", Font.BOLD, 12));
		displayMenuEntry(graphics, x, y, "Display piece", "CTRL + P");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Next piece", "right arrow");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Prev piece", "left arrow");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Select", "CTRL + S");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Quit Display", "s");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Skip turn", "space");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Quit game", "CTRL + Q");
	}
	
	public void pieceMenu(Graphics2D graphics) {
		int yPadding = 50;
		var x = topLeftX + 10;
		var y = topLeftY + 25;
		graphics.setFont(new Font("default", Font.BOLD, 25));
		int menuWidth = getStringWidth(graphics, "Menu");
		var cube = new Rectangle2D.Float(topLeftX, topLeftY, topLeftX + width, height);
		graphics.setColor(Color.GRAY);
		graphics.fill(cube);
		graphics.setColor(Color.BLACK);
		graphics.draw(cube);
		graphics.drawString("Menu", (topLeftX + width - menuWidth) / 2, y);
		y += yPadding;
		graphics.setFont(new Font("default", Font.BOLD, 12));
		displayMenuEntry(graphics, x, y, "Flip", "F");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Reverse", "R");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Quit", "Q");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Save", "S");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Move", "Arrows");
	}
	
	public void drawMenu(Graphics2D graphics) {
		int yPadding = 50;
		var x = topLeftX + 10;
		var y = topLeftY + 25;
		graphics.setFont(new Font("default", Font.BOLD, 25));
		int menuWidth = getStringWidth(graphics, "Menu");
		var cube = new Rectangle2D.Float(topLeftX, topLeftY, topLeftX + width, height);
		graphics.setColor(Color.GRAY);
		graphics.fill(cube);
		graphics.setColor(Color.BLACK);
		graphics.draw(cube);
		graphics.drawString("Menu", (topLeftX + width - menuWidth) / 2, y);
		y += yPadding;
		graphics.setFont(new Font("default", Font.BOLD, 12));
		displayMenuEntry(graphics, x, y, "Select", "SPACE");
		y += yPadding;
		displayMenuEntry(graphics, x, y, "Move", "Arrows");
	}
	
	private int getStringWidth(Graphics2D graphics, String text) {
		return graphics.getFontMetrics().stringWidth(text);
	}
	
	private void displayMenuEntry(Graphics2D graphics, float x, float y, String text, String menu) {
		graphics.drawString(text, x, y);
		var menuOffset = topLeftX + width - getStringWidth(graphics, menu);
		graphics.drawString(menu, menuOffset, y);
	}
}
