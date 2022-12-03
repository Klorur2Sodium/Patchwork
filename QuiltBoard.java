
public class QuiltBoard {
	private boolean[][] _grid;
	private int _buttons;

	private static final int GRID_SIZE = 9;

	public QuiltBoard() {
		_grid = new boolean[GRID_SIZE][GRID_SIZE];
		_buttons = 0;
	}

	public int getButtons() {
		return _buttons;
	}

	public int getEmpty() {
		var score = 0;
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				if (!_grid[i][j]) {
					score++;
				}
			}
		}
		return score;
	}

	private void addButtons(int nbButtons) {
		if (nbButtons < 0) {
			throw new IllegalArgumentException("The number of buttons must be positive or equal to zero");
		}
		_buttons += nbButtons;
	}

	private boolean isPlacebale(Piece piece, int x, int y) {
		if (!piece.fitArea(x, y, GRID_SIZE)) {
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

	public void addPieceAutomatically(Piece piece) {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				if (addPiece(piece, j, i)) {
					return;
				}
			}
		}
	}

	public void display() {
		var builder = new StringBuilder();
		builder.append("    1 2 3 4 5 6 7 8 9\n").append("  +------------------+\n");
		for (int i = 0; i < GRID_SIZE; i++) {
			builder.append(i + 1).append(" |");

			for (int j = 0; j < GRID_SIZE; j++) {
				builder.append(_grid[i][j] ? " x" : " .");
			}
			builder.append("|\n");
		}
		builder.append("  +------------------+\n");
		System.out.println(builder.toString());
	}

}
