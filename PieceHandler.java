package fr.uge.patchwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class stores all the pieces in a list as well as
 * the position of the neutral pawn. 
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class PieceHandler extends GraphicalObject {
	private static final PieceHandler _instance = new PieceHandler();

	private final ArrayList<Piece> _pieces;
	private int _piecesDisplayed;
	private int _posNeutralPawn;
	private boolean _display;
	
	private final static int selectableNumber = 3; 

	/**
	 * Constructs a new PieceHandler.
	 */
	private PieceHandler() {
		_pieces = new ArrayList<>();
		_piecesDisplayed = 12;
		_posNeutralPawn = 0;
	}

	/**
	 * Calls the constructor.
	 * 
	 * @return the new PieceHandler
	 */
	public static PieceHandler Handler() {
		return _instance;
	}

	/**
	 * Getter for the size of the list pieces.
	 * 
	 * @return size
	 */
	public int getSize() {
		return _pieces.size();
	}

	/**
	 * Getter for the list pieces.
	 * 
	 * @return pieces
	 */
	public List<Piece> getPieces() {
		return _pieces;
	}

	/**
	 * If the given index is greater or equal to the size of the list returns the index
	 * minus the size
	 * 
	 * @param index : index you want to test
	 * @return real Index
	 */
	private int getRealIndex(int index) {
		return (index >= _pieces.size()) ? index - _pieces.size() : index;
	}

	/**
	 * Returns the piece at the given index.
	 * 
	 * @param index : index of the piece
	 * @return the Piece
	 */
	public Piece getPiece(int index) {
		if (index >= _pieces.size()) {
			throw new ArrayIndexOutOfBoundsException("Index must be strictly smaller than the size of pieces");
		}

		index = getRealIndex(index + _posNeutralPawn);
		return _pieces.get(index);
	}
	
	public boolean getDisplay() {
		return _display;
	}
	
	public void setDisplay(boolean value) {
		_display = value;
	}
	
	/**
	 * Adds the given piece to the list of pieces.
	 * 
	 * @param p : the piece you want to add
	 * @return yes if successfully add, false if not
	 */
	public boolean add(Piece p) {
		Objects.requireNonNull(p);
		return _pieces.add(p);
	}

	/**
	 * Removes the non null given piece. 
	 * 
	 * @param p : the piece you want to remove
	 */
	public void remove(Object p) {
		Objects.requireNonNull(p);
		_pieces.remove(p);
	}

	/**
	 * Initializes the list of pieces by parsing the lines
	 * of a file.
	 * 
	 * @param path : path to the file 
	 * @throws IOException : if file not find
	 */
	public void loadPieces(Path path) throws IOException {
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				var p = new Piece();
				p.parseLine(line);
				add(p);
			}
		}
	}
	
	/**
	 * Increases the position of the neutral pawn by the given number of moves
	 * 
	 * @param nbMove : number of moves
	 */
	public void moveNeutralPawn(int nbMove) {
		if (nbMove < 0) {
			throw new IllegalArgumentException("The neutral pawn must only move forward");
		}
		_posNeutralPawn = getRealIndex(_posNeutralPawn + nbMove);
	}
	
	/**
	 * Displays all the pieces and their informations.
	 * 
	 * @param captions : display the captions or not
	 */
	public void display(boolean captions) {
		var boardBuilder = new AsciiPieceDisplayer();
		boardBuilder.display(captions);
	}
	
	/**
	 * the function draws a line from the end of the quiltboard to the end of the window
	 * @param context
	 * @param x
	 * @param height
	 */
	private void drawDelimitation(Graphics2D graphics, float x, float height) {
		var line = new Line2D.Float(x, Constants.BOX_SIZE.getValue(), x, height - 5);
			graphics.setStroke(new BasicStroke(5));
    		graphics.setColor(Color.BLACK);
    		graphics.draw(line);
	}
	
	/**
	 * The function draws the PieceHandler
	 * @param context
	 * @param topX
	 * @param width
	 * @param height
	 */
	@Override
	protected void onDraw(Graphics2D graphics) {
		float x = topLeftX;
		float y = Constants.BOX_SIZE.getValue() + 100;
		float biggestY = 0;
		drawDelimitation(graphics, x - 10, height);
		graphics.setStroke(new BasicStroke(2));
		for (int i = 0; i < _piecesDisplayed && i < _posNeutralPawn + _pieces.size(); i++) {
			var index = getRealIndex(i + _posNeutralPawn);
			var piece = _pieces.get(index);
			if (y + piece.getYSize() >=  height) {
				return;
			}
			if (piece.getXSize() * Constants.PIECE_SQUARE.getValue() + x >= width) {
				x = topLeftX;
				y += biggestY + 50;
				biggestY = 0;
			}
			piece.SetGraphicalProperties(x, y, Constants.PIECE_SQUARE.getValue());
			piece.draw(graphics);
			x += piece.getXSize() * Constants.PIECE_SQUARE.getValue() + 50;
			biggestY = (piece.getYSize() * Constants.PIECE_SQUARE.getValue() > biggestY)? piece.getYSize()  * Constants.PIECE_SQUARE.getValue() : biggestY; 
		}
	}
	
	public void action(Graphics2D graphics, float height, float width, int number) {
		if (_piecesDisplayed < number) {
			return;
		}
		var index = getRealIndex(number + _posNeutralPawn);
		_pieces.get(index).drawInformations(graphics, height, width);
	}
	
	/**
	 * This class handles the graphic Ascii methods
	 */
	private class AsciiPieceDisplayer {
		/**
		 * Displays all the pieces and their informations.
		 * 
		 * @param captions : display the captions or not
		 */
		public void display(boolean captions) {
			if (captions) {
				System.out.println(Caption());
			}
			System.out.println(displaySelectablePiecesNumber());
			displayPieces();

		}
		
		/**
		 * Returns a String that contains the captions.
		 * 
		 * @return the captions
		 */
		private String Caption() {
			var builder = new StringBuilder();
			builder.append("Caption :\n");
			builder.append("  c : cost of the piece\n");
			builder.append("  m : number of moves the player has to do\n");
			builder.append("  b : number of buttons on the piece\n");
			return builder.toString();
		}
		
		/**
		 * Returns a String with the numbers that number the first 3 pieces.
		 * 
		 * @return 
		 */
		private String displaySelectablePiecesNumber() {
			var builder = new StringBuilder();

			for (int i = 0; i < selectableNumber && i < _pieces.size(); i++) {
				builder.append("(").append(i + 1).append(")");
				builder.append("    ");
			}
			builder.append("\n");
			return builder.toString();
		}
		
		/**
		 * Returns a string containing a line of all the pieces we want to display
		 * 
		 * @return String
		 */
		private String bodyString() {
			var builder = new StringBuilder();
			int index;
			
			for (int line = 0; line < getBiggestPiece(); line++) {
				for (int j = 0; j < _piecesDisplayed && j < _pieces.size(); j++) {
					index = getRealIndex(j + _posNeutralPawn);
					builder.append(_pieces.get(index).bodyLine(line));
				}
				builder.append("\n");
			}
			return builder.toString();
		}
		
		/**
		 * Gets the biggest height out of the 12 first pieces.
		 * 
		 * @return biggest height
		 */
		private int getBiggestPiece() {
			int pieceHeight;
			var maxHeight = _pieces.get(_posNeutralPawn).getYSize();
			for (int i = 1; i < _piecesDisplayed && i < _pieces.size(); i++) {
				pieceHeight = _pieces.get(getRealIndex(i + _posNeutralPawn)).getYSize();
				if (maxHeight < pieceHeight) {
					maxHeight = pieceHeight;
				}
			}
			return maxHeight;
		}

		/**
		 * Returns the cost of the piece and some spaces for esthetic reasons
		 * 
		 * @param index : index of the piece
		 * @return cost
		 */
		private String costString(int index) {
			return "c : " + _pieces.get(index).getCost() + _pieces.get(index).spacesCaption("cost");
		}

		/**
		 * Returns the moves of the piece and some spaces for esthetic reasons
		 * 
		 * @param index : index of the piece
		 * @return moves
		 */
		private String movesString(int index) {
			return "m : " + _pieces.get(index).getMoves() + _pieces.get(index).spacesCaption("move");
		}

		/**
		 * Returns the buttons of the piece and some spaces for esthetic reasons
		 * 
		 * @param index : index of the piece
		 * @return buttons
		 */
		private String buttonString(int index) {
			return "b : " + _pieces.get(index).getButtons() + _pieces.get(index).spacesCaption("button");
		}

		/**
		 * Displays the first 12 pieces if possible and all their informations
		 */
		private void displayPieces() {
			var cost = new StringBuilder();
			var moves = new StringBuilder();
			var button = new StringBuilder();
			int index;

			for (int i = _posNeutralPawn; i < _posNeutralPawn + _piecesDisplayed && i < _pieces.size(); i++) {
				index = getRealIndex(i);
				cost.append(costString(index));
				moves.append(movesString(index));
				button.append(buttonString(index));
			}
			System.out.println(bodyString());
			System.out.println(cost.toString());
			System.out.println(moves.toString());
			System.out.println(button.toString());
		}

	}
}
