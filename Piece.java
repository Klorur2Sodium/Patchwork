package fr.uge.patchwork;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

/**
 * This class stores the information about a piece,
 * it also handles the creation of a piece from a String
 * and its rotations/ inversions.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class Piece extends GraphicalObject {
	private boolean[][] _body; // 1bis
	private byte _cost; //-128 + 127
	private byte _buttons;
	private byte _moves;
	private byte xSize;
	private byte ySize;
	private Color _color;
	

	/**
	 * Getter for the cost of the piece
	 * 
	 * @return cost of the piece
	 */
	public int getCost() {
		return _cost;
	}

	/**
	 * Getter for the number of buttons on the piece
	 * 
	 * @return number of buttons on the piece
	 */
	public int getButtons() {
		return _buttons;
	}

	/**
	 * Getter for the number of moves of the piece
	 * 
	 * @return number of moves of the piece
	 */
	public int getMoves() {
		return _moves;
	}

	/**
	 * Getter for the cost of the piece
	 * 
	 * @return cost of the piece
	 */
	public int getXSize() {
		return xSize;
	}

	/**
	 * Getter for the cost of the piece
	 * 
	 * @return cost of the piece
	 */
	public int getYSize() {
		return ySize;
	}
	
	/**
	 * Returns the value in the body of the piece at 
	 * the coordinates (x, y)
	 * 
	 * @param x : x coordinates
	 * @param y : y coordinates
	 * @return value at the coordinates (x, y)
	 */
	public boolean getBodyValue(int x, int y) {
		return _body[y][x];
	}
	
	/**
	 * Returns the total number of body parts that the piece contains.
	 * @return number of body parts
	 */
	public int getNumberOfBodyParts() {
		var nbBodyParts = 0;
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				if (_body[i][j]) {
					nbBodyParts++;
				}
			}
		}
		return nbBodyParts;
	}
	
	/**
	 * Creates a new piece with the stats of the current piece,
	 * but with an empty 2 dimensional array initialized with the
	 * given x and y
	 * 
	 * @param x : width of the piece
	 * @param y : height of the piece
	 * @return a new piece
	 */
	private Piece newPiece(byte x, byte y) {
		var temp = new Piece();
		temp._cost = _cost;
		temp._moves = _moves;
		temp._buttons = _buttons;
		temp.xSize = (byte) x;
		temp.ySize = (byte) y;
		temp._body = new boolean[x][y];
		temp._color = _color;
		return temp;
	}
	
	private Color chooseRandomColor() {
		Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA,
				Color.CYAN, Color.GRAY, Color.ORANGE, Color.PINK, Color.YELLOW,
				};
		int rand = (int)( Math.random() * colors.length);
		return colors[rand];
	}
	
	/**
	 * Initializes this piece with the given line
	 * 
	 * @param line : a line
	 */
	public void parseLine(String line) {
		Objects.requireNonNull(line);
		var splitLine = line.split(":");

		parseBody(splitLine[0]);
		_cost = (byte) Integer.parseInt(splitLine[1]);
		_moves = (byte) Integer.parseInt(splitLine[2]);
		_buttons = (byte) Integer.parseInt(splitLine[3]);
		_color = chooseRandomColor();
	}

	/**
	 * Checks if the piece can fit in a quiltboard of the given size
	 * at the (x,y) coordinates 
	 * 
	 * @param x : x coordinates
	 * @param y : y coordinates
	 * @param size : size of the quiltboard
	 * @return boolean
	 */
	public boolean fitArea(int x, int y, int size) {
		if (xSize + x > size) {
			return false;
		}
		if (ySize + y > size) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a rotated version of this piece
	 * 
	 * @return rotated piece
	 */
	public Piece flip() {
		// rotates the piece counter clockwise
		var temp = newPiece(ySize, xSize);
		for (int i = xSize - 1; i >= 0; i--) {
			for (int j = 0; j < ySize; j++) {
				temp._body[j][i] = _body[xSize - 1 - i][j];
			}
		}
		return temp;
	}

	/**
	 * Returns a mirrored version of this piece 
	 * 
	 * @return mirrored piece
	 */
	public Piece reverse() {
		// left becomes right right becomes left
		var temp = newPiece(xSize, ySize);
		for (int i = 0; i < ySize; i++) {
			for (int j = xSize - 1; j >= 0; j--) {
				temp._body[xSize - j - 1][i] = _body[j][i];
			}
		}
		return temp;
	}
	
	/**
	 * Returns the String representing the body of this piece in Ascii
	 * to display it.
	 * 
	 * @return Ascii representation of the piece
	 */
	public String bodyString() {
		var builder = new StringBuilder();
		for (int i = 0; i < ySize; i++) {
			for (int j = 0; j < xSize; j++) {
				builder.append(_body[j][i] ? "x" : " ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	/**
	 * Finds the number of spaces needed between the description of 
	 * each piece so everything is aligned.
	 * 
	 * @param type : type of the stat (cost, move, etc...)
	 * @return String
	 */
	public String spacesCaption(String type) {
		var builder = new StringBuilder();
		int lenCaption = (_cost < 10)? Constants.SMALL_COMMENT.getValue() : Constants.BIG_COMMENT.getValue();
		if (!type.equals("cost") && lenCaption == Constants.BIG_COMMENT.getValue()) {
			builder.append(" ");
		}
		if (xSize > lenCaption) {
			for (int i = 0; i <= xSize - lenCaption; i++) {
				builder.append(" ");
			}
		}
		return builder.toString() + "  ";
	}
	
	/**
	 * Initializes the body by parsing the given line
	 * 
	 * @param line : contains the information about the body
	 */
	private void parseBody(String line) {
		var splitLine = line.split(",");
		ySize = (byte) splitLine.length;
		xSize = (byte) splitLine[0].length();
		_body = new boolean[xSize][ySize];
		initPiece(line);
	}

	/**
	 * Fill the body thanks to the given line
	 *  
	 * @param line
	 */
	private void initPiece(String line) {
		char[] decomposition = line.toCharArray();
		int x = 0, y = 0;
		for (var elm : decomposition) {
			switch (elm) {
				case '1' -> {
					_body[x][y] = true;
					x++;
				}
				case '0' -> {
					_body[x][y] = false;
					x++;
				}
				case ',' -> {
					if (xSize != x) {
						throw new IllegalArgumentException("invalid piece");
					}
					y++;
					x = 0;
				}
			}
		}
	}

	/**
	 * Finds the number of spaces needed between the pieces in the buying phase
	 * so everything is aligned.
	 * 
	 * @param type
	 * @return String
	 */
	private String spacesBody() {
		var space = new StringBuilder();
		int lenCaption;
		lenCaption = (_cost < 10)? 5 : 6;
		if (xSize < lenCaption) {
			for (int i = 0; i < lenCaption - xSize; i++) {
				space.append(" ");
			}
		}
		return space.toString() + "  ";
	}
	
	@Override
	public void SetGraphicalProperties(float x, float y, float w, float h) {
		if(w != h) {
			// Erreur ou
			h = w;
		}
		super.SetGraphicalProperties(x, y, w, h);
	}
	
	/**
	 * Returns a string representing the given line in Ascii
	 * 
	 * @param line : a line
	 * @return Ascii representation of the line
	 */
	public String bodyLine(int line) {
		var builder = new StringBuilder();
		if (line >= ySize) {
			for (int i = 0; i < xSize; i++) {
				builder.append(" ");
			}
		} else {
			for (int i = 0; i < xSize; i++) {
				builder.append(_body[i][line] ? "x" : " ");
			}
		}

		return builder.toString() + spacesBody();
	}

	/**
	 * The function draws the piece on the coordinates (x, y)
	 * @param graphics
	 * @param x
	 * @param y
	 */
	@Override
	protected void onDraw(Graphics2D graphics) {
		for (var i = 0; i < xSize; i++) {
			for (var j = 0; j < ySize; j++) {
				if (_body[i][j]) {
					var square = new Rectangle2D.Float(topLeftX + i * width, topLeftY + j* width, width, width);
					graphics.setColor(_color);
    				graphics.fill(square);
    				graphics.setColor(Color.BLACK);
    				graphics.draw(square);
				}
			}
		}
	}

	/**
	 * Draws a square containing the piece informations
	 * @param context
	 * @param height
	 * @param width
	 */
	public void drawInformations(Graphics2D graphics, float height, float width) {
		var side = height/3*2;
		var h = height/6;
		var w = width/2-side/2;
		var startWritting = w + side/3*2;
		float x = w;
		float y = h;
		var grid = new Rectangle2D.Float(x, y, side, side);
		graphics.setColor(Color.LIGHT_GRAY);
        graphics.fill(grid);
		graphics.setStroke(new BasicStroke(5));
		graphics.setColor(Color.BLACK);
        graphics.draw(grid);
        
        graphics.setFont(new Font("default", Font.BOLD, 25));
        x+= 10; y+= 50;
        graphics.drawString("Piece informations", x, y);
        x = startWritting; y = h + side/2;
        graphics.drawString("Cost : " + _cost, x, y);
        y+= 50;
        graphics.drawString("Moves : " + _moves, x, y);
        y+= 50;
        graphics.drawString("Buttons : " + _buttons, x, y);
		var res = Math.min(proportionalSize(true), proportionalSize(false));
		
		var xPos = w + 10;
		var yPos = h + side/3;
		for (var i = 0; i < xSize; i++) {
			for (var j = 0; j < ySize; j++) {
				if (_body[i][j]) {
					var square = new Rectangle2D.Float(xPos + i * res, yPos + j* res, res, res);
					graphics.setColor(_color);
    				graphics.fill(square);
    				graphics.setColor(Color.BLACK);
    				graphics.draw(square);
				}
			}
		}
	}
	
	/**
	 * returns the size of a cube using the reference 100px for 3 cubes
	 * @param xCoordinate
	 * @return
	 */
	private int proportionalSize(boolean xCoordinate) {
		int cubeCount = 3;
		int size = 100;
		int res;
		if (xCoordinate) {
			res = xSize * size / cubeCount;
		} else {
			res = ySize * size / cubeCount;
		}
		if (res > 100) {
			return res-100;
		}
		return res;
	}
}
