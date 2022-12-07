package fr.uge.patchwork;

import java.util.Objects;

/**
 * This class stores the information about a piece,
 * it also handles the creation of a piece from a String
 * and its rotations/ inversions.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class Piece {
	private boolean[][] _body; // 1bis
	private byte _cost; //-128 + 127
	private byte _buttons;
	private byte _moves;
	private byte xSize;
	private byte ySize;
	
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
		return temp;
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
}
