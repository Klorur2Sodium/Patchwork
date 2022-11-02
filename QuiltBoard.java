
public class QuiltBoard {
	private int[][] grid;
	private int nbButtons;
	
	private static final int GRID_SIZE = 9;
	
	public QuiltBoard() {
		grid = new int[GRID_SIZE][GRID_SIZE];
		nbButtons = 0;
	}
	
	public int[][] grid() {
		return grid;
	}
	
	public int nbButtons() {
		return nbButtons;
	}
	
	private boolean isPlaceable(Piece piece, int x, int y) {
		var sizePiece = piece.body().length;
		
		for (int i = 0; i < sizePiece; i++) {
			for (int j = 0; j < sizePiece; j++) {
				if (i + x >= GRID_SIZE || j + y >= GRID_SIZE || 
					 (grid[i+x][j+y] == 1 && piece.body()[i][j] == 1)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean addPieceAutomatically(Piece piece) {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				if (addPiece(piece, i, j)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean addPiece(Piece piece, int x, int y) {
		var sizePiece = piece.body().length;
		if (x < 0 || y < 0) {
			return false;
		}

		if (isPlaceable(piece, x, y)) {
			nbButtons += piece.nbButton();
			for (int i = 0; i < sizePiece; i++) {
				for (int j = 0; j < sizePiece; j++) {
					if (piece.body()[i][j] == 1) {
						grid[i+x][j+y] = 1;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	// Graphic Methods - Ascii version
	public void displayGrid() {
		System.out.println(
			"    1 2 3 4 5 6 7 8 9\n"
		+ "  +------------------+"
		);
		for (int i = 0; i < GRID_SIZE; i++) {
			System.out.print((i+1) + " |");
			for (int j = 0; j < GRID_SIZE; j++) {
				if (grid[i][j] == 0) {
					System.out.print("  ");
				} else {
					System.out.print(" x");
				}
			}
			System.out.println('|');
		}
		System.out.println("  +------------------+");
	}
}









