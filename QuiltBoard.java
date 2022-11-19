
public class QuiltBoard {
    private boolean[][] _grid;
    private int _buttons;

    private final int GRID_SIZE = 9;

    public QuiltBoard() {
		_grid = new boolean[GRID_SIZE][GRID_SIZE];
		_buttons = 0;
	}


    public int getButtons() {
        return _buttons;
    }

    private void addButtons(int nbButtons) {
        if (nbButtons < 0) {
            throw new IllegalArgumentException("The number of buttons must be > 0");
        }
        _buttons += nbButtons;
    } 

    private boolean isPlacebale(Piece piece, int x, int y) {
        if (!piece.fitArea(x, y, GRID_SIZE)) {
            return false;
        }
        int sizeX = piece.getXSize();
        int sizeY = piece.getYSize();
        for(var i = y; i < sizeY; i++) {
            for (var j = x; j < sizeX; j++) {
                if (_grid[i][j] && piece.getBodyValue(i-y, j-x)) {
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
        int xsize = piece.getXSize();
        int ysize = piece.getYSize();
        for (int i = 0; i < ysize; i++) {
            for (int j = 0; j < xsize; j++) {
                _grid[i + y][j + x] = piece.getBodyValue(i, j);
            }
        }
        return true;
    }

    public void addPieceAutomatically(Piece piece) {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				if (addPiece(piece, i, j)) {
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
				builder.append(_grid[i][j] ? " x" : "  ");
			}
            builder.append("|\n");
		}
        builder.append("  +------------------+\n");
		System.out.println(builder.toString());
    }

}
