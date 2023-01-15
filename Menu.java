package fr.uge.patchwork;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

/**
 * This class contains all the methods used to draw the game's menu.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 *
 */
public class Menu extends GraphicalObject {
	
	@Override
	protected void onDraw(Graphics2D graphics) {
		Objects.requireNonNull(graphics);
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
		graphics.drawLine((int)topLeftX, (int)(y + 5), (int)(topLeftX + width), (int)(y + 5));
	}
	
	/**
	 * Displays the menu when a player wants to place a piece on his quilt board.
	 * 
	 * @param graphics : object that calls the graphic methods.
	 */
	public void pieceMenu(Graphics2D graphics) {
		Objects.requireNonNull(graphics);
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
	
	/**
	 * Draws the global menu.
	 * 
	 * @param graphics : object that calls the graphic methods.
	 */
	public void drawMenu(Graphics2D graphics) {
		Objects.requireNonNull(graphics);
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
	
	/**
	 * Returns the width of the given String
	 * @param graphics : object that calls the graphic methods.
	 * @param text the String you want to measure.
	 * @return the width of the given String
	 */
	private int getStringWidth(Graphics2D graphics, String text) {
		return graphics.getFontMetrics().stringWidth(text);
	}
	
	/**
	 * Draws at the given coordinates a command and its key bind.
	 * @param graphics : object that calls the graphic methods.
	 * @param x : x coordinates of the entry.
	 * @param y : y coordinates of the entry.
	 * @param text : command.
	 * @param menu : key bind.
	 */
	private void displayMenuEntry(Graphics2D graphics, float x, float y, String text, String menu) {
		graphics.drawString(text, x, y);
		var menuOffset = topLeftX + width - getStringWidth(graphics, menu);
		graphics.drawString(menu, menuOffset, y);
	}
}
