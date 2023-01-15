package fr.uge.patchwork;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;


/**
 * This class stores the information about a quilt board. it also handles adding
 * a piece to it and displaying the grid.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class QuiltBoard extends GraphicalObject {
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
		
	@Override
	protected void onDraw(Graphics2D graphics) {
		var infos = getInfos(topLeftX + width, topLeftY, topLeftY + height);
		var grid = new Rectangle2D.Float(infos.get(0), infos.get(2), infos.get(1), infos.get(1));
		float cubeSize = infos.get(1) / _size; 
		String s = "Recovered buttons : ";
		int x = 0, y = 0;
        for (int i = 0; i < _size; i++) {
			for (int j = 0; j < _size; j++) {
				var cube = new Rectangle2D.Float(infos.get(0) + j*cubeSize , infos.get(2) + i*cubeSize, cubeSize, cubeSize);
		        drawPieceCube(graphics, j, i ,cube);
		        y = j;
			}
			x = i;
		}
        graphics.setStroke(new BasicStroke(5));
        graphics.draw(grid);
        graphics.drawString(s + _buttons, infos.get(0) + x*cubeSize + s.length(), infos.get(1) + (y+1)*cubeSize + cubeSize/3);
	}
	
	/**
	 * The function draws a piece on the QuiltBoard on the (x, y) coordinates
	 * @param graphics 
	 * @param piece the piece to draw
	 * @param x 
	 * @param y
	 */
	public void drawPiece(Graphics2D graphics, Piece piece, int x, int y) {
		var infos = getInfos(topLeftX + width, topLeftY, topLeftY + height);
		float cubeSize = infos.get(1) / _size; 
		piece.SetGraphicalProperties(infos.get(0) + x * cubeSize, infos.get(2) + y*cubeSize, cubeSize);
		piece.draw(graphics);
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
	 * The function return 3 informations where to start the drawing 
	 * and the size of the square that contains it
	 * @param bottomX
	 * @param topY
	 * @param bottomY
	 * @return the List of those informations
	 */
	private List<Float> getInfos(float bottomX, float topY, float bottomY) {
		float tY = topY + 50; // to have the smallest space on the top and bottom
		float size = bottomY - 50 - (tY);
		float tX = (bottomX - size) / 2;
		return List.of(tX, size, tY);
	}
	
	/**
	 * The function draws a pink cube on the quiltBoard or/and just a square
	 * at the (i, j) coordinates
	 * @param graphics
	 * @param i
	 * @param j
	 * @param cube the representation of the cubre
	 */
	private void drawPieceCube(Graphics2D graphics, int i, int j, Rectangle2D cube) {
		if (_grid[j][i]) {
        	graphics.setColor(Color.MAGENTA);
        	graphics.fill(cube);
        	graphics.setColor(Color.BLACK);
        }
        graphics.draw(cube);
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
