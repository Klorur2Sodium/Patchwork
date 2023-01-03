package fr.uge.patchwork;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import fr.umlv.zen5.ApplicationContext;

/**
 * This class stores the information about a quilt board. it also handles adding
 * a piece to it and displaying the grid.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class QuiltBoard {
	private boolean[][] _grid;
	private int _buttons;
	private final int _size;

	/**
	 * Constructs a new QuiltBoard with 0 buttons.
	 */
	public QuiltBoard() {
		_size = Constants.GRID_SIZE.getValue();
		_grid = new boolean[_size][_size];
		_buttons = 0;
	}

	/**
	 * Getter for the number of buttons on the quilt board
	 * 
	 * @return number of buttons
	 */
	public int getButtons() {
		return _buttons;
	}
	
	/**
	 * Returns the number of empty square in the grid
	 * 
	 * @return number of empty square
	 */
	public int getEmpty() {
		var score = 0;
		for (int i = 0; i < _size; i++) {
			for (int j = 0; j < _size; j++) {
				if (!_grid[i][j]) {
					score++;
				}
			}
		}
		return score;
	}

	/**
	 * Checks if the piece is placeable and then places it at the
	 * (x,y) coordinates
	 * 
	 * @param piece : the piece you want to place
	 * @param x : x coordinate
	 * @param y : y coordinate
	 * @return boolean representing success of the placement
	 */
	public boolean addPiece(Piece piece, int x, int y) {
		if (x < 0 || y < 0 || !isPlacebale(piece, x, y)) {
			return false;
		}
		addButtons(piece.getButtons());
		for (int i = 0; i < piece.getYSize(); i++) {
			for (int j = 0; j < piece.getXSize(); j++) {
				if (piece.getBodyValue(i, j)) {
					_grid[j + y][i + x] = true;
				}
			}
		}
		return true;
	}

	/**
	 * Adds the given piece to the quilt board at
	 * the first valid coordinates.
	 * 
	 * @param piece : the piece you want to place
	 */
	public void addPieceAutomatically(Piece piece) {
		for (int i = 0; i < _size; i++) {
			for (int j = 0; j < _size; j++) {
				if (addPiece(piece, j, i)) {
					return;
				}
			}
		}
	}
	
	/**
	 * Creates a String that contains every number to the size
	 * 
	 * @return String
	 */
	public String numberLine() {
		var builder = new StringBuilder();
		for (int i = 1; i <= _size; i++) {
			builder.append(i).append(" ");
		}
		return builder.toString();
	}

	/**
	 * Displays the quilt board in Ascii art.
	 */
	public void display() {
		var builder = new StringBuilder();
		builder.append("    " + numberLine() + "\n").append("  +------------------+\n");
		for (int i = 0; i < _size; i++) {
			builder.append(i + 1).append(" |");

			for (int j = 0; j < _size; j++) {
				builder.append(_grid[i][j] ? " x" : " .");
			}
			builder.append(" |\n");
		}
		builder.append("  +------------------+\n");
		System.out.println(builder.toString());
	}
	
	/**
	 * The function draws the grid on the screen
	 * @param context
	 * @param topX
	 * @param topY
	 * @param size
	 */
	public void draw(ApplicationContext context, float topX, float topY, float size) {
		var grid = new Rectangle2D.Float(topX, topY, size, size);
		float cubeSize = size / _size; 
		context.renderFrame(graphics -> {
			String s = "Recovered buttons : ";
			int x = 0, y = 0;
	        for (int i = 0; i < _size; i++) {
				for (int j = 0; j < _size; j++) {
					var cube = new Rectangle2D.Float(topX + i*cubeSize , topY + j*cubeSize, cubeSize, cubeSize);
			        if (_grid[i][j]) {
			        	graphics.setColor(Color.PINK);
			        	graphics.fill(cube);
			        } else {
			        	graphics.setColor(Color.BLACK);
				        graphics.draw(cube);
			        }
			        y = j;
				}
				x = i;
			}
	        graphics.setStroke(new BasicStroke(5));
	        graphics.draw(grid);
	        graphics.drawString(s + _buttons, topX + x*cubeSize + s.length(), topY + (y+1)*cubeSize + cubeSize/3);
	      });
	}

	/**
	 * Checks if the quilt board possesses a seven by seven square
	 * completely filled 
	 * 
	 * @return true if yes, false if not
	 */
	public boolean checkSpecialTile() {
		var lenLig = 0;
		int tileSize = Constants.SPECIAL_TILE.getValue();
		int boardSize = Constants.GRID_SIZE.getValue();
		for (int col = 0; col <= boardSize - tileSize; col++) {
			for (int lig = 0; lig < boardSize; lig++) {
				if (_grid[col][lig]) {
					lenLig++;
				} else {
					lenLig = 0;
				}
				if (lenLig == tileSize && checkSpace(lig - (tileSize - 1), col)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if a seven by seven square space is completely filled,
	 * starting at the coordinates (lig,col).
	 * 
	 * @param line : starting line
	 * @param col : starting column
	 * @return
	 */
	private boolean checkSpace(int line, int col) {
		int tileSize = Constants.SPECIAL_TILE.getValue();
		for (int i = 1; i < tileSize; i++) {
			for (int j = 0; j < tileSize; j++) {
				if (!_grid[i + col][j + line]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Adds the given number of buttons to the total number of buttons on the
	 * grid.
	 * 
	 * @param nbButtons : number of buttons
	 */
	private void addButtons(int nbButtons) {
		if (nbButtons < 0) {
			throw new IllegalArgumentException("The number of buttons must be positive or equal to zero");
		}
		_buttons += nbButtons;
	}

	/**
	 * Checks if the piece is placeable at the (x,y) coordinates.
	 * 
	 * @param piece : the piece to place
	 * @param x : x coordinates
	 * @param y : y coordinates
	 * @return true if yes, false if not
	 */
	private boolean isPlacebale(Piece piece, int x, int y) {
		if (!piece.fitArea(x, y, _size)) {
			return false;
		}

		for (var i = 0; i < piece.getYSize(); i++) {
			for (var j = 0; j < piece.getXSize(); j++) {
				if (_grid[i + y][j + x] && piece.getBodyValue(i, j)) {
					return false;
				}
			}
		}
		return true;
	}

}
