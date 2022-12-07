package fr.uge.patchwork;

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
					_grid[i + y][j + x] = true;
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
		builder.append(numberLine() + "\n").append("  +------------------+\n");
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
	 * Checks if the quilt board possesses a seven by seven square
	 * completely filled 
	 * 
	 * @return true if yes, false if not
	 */
	public boolean checkSpecialTile() {
		var lenLig = 0;
		for (int col = 0; col < 3; col++) {
			for (int lig = 0; lig < 9; lig++) {
				if (_grid[col][lig]) {
					lenLig++;
				} else {
					lenLig = 0;
				}
				if (lenLig == 7 && checkSpace(lig - 6, col)) {
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
		for (int i = 1; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
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
