import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PieceHandler {
	private static final PieceHandler _instance = new PieceHandler();

	private final ArrayList<Piece> _pieces;
	private int _piecesDisplayed;
	private int _posNeutralPawn;
	
	private final static int selectableNumber = 3; 

	private PieceHandler() {
		_pieces = new ArrayList<>();
		_piecesDisplayed = 12;
		_posNeutralPawn = 0;
	}

	public static PieceHandler Handler() {
		return _instance;
	}

	public int getSize() {
		return _pieces.size();
	}

	public List<Piece> getPieces() {
		return _pieces;
	}

	private int getRealIndex(int index) {
		return index < _pieces.size() ? index : index - _pieces.size();
	}

	public boolean add(Piece p) {
		Objects.requireNonNull(p);
		return _pieces.add(p);
	}

	public void remove(Object p) {
		Objects.requireNonNull(p);
		_pieces.remove(p);
	}

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
	
	public void moveNeutralPawn(int nbMove) {
		if (nbMove < 0) {
			throw new IllegalArgumentException("The neutral pawn must only move forward");
		}
		_posNeutralPawn = getRealIndex(_posNeutralPawn + nbMove);
	}

	public Piece getPiece(int index) {
		if (index >= _pieces.size()) {
			throw new ArrayIndexOutOfBoundsException("Index must be strictly smaller than the size of pieces");
		}

		index = getRealIndex(index + _posNeutralPawn);
		return _pieces.get(index);
	}
	
	
	public void display(boolean captions) {
		var boardBuilder = new AsciiPieceDisplayer();
		boardBuilder.display(captions);
	}

	
	
	
	private class AsciiPieceDisplayer {
		

		private String Caption() {
			var builder = new StringBuilder();
			builder.append("Caption :\n");
			builder.append("  c : cost of the piece\n");
			builder.append("  m : number of moves the player has to do\n");
			builder.append("  b : number of buttons on the piece\n");
			return builder.toString();
		}
		
		private String displaySelectablePiecesNumber() {
			var builder = new StringBuilder();

			for (int i = 0; i < selectableNumber && i < _pieces.size(); i++) {
				builder.append("(").append(i + 1).append(")");
				builder.append(_pieces.get(i).spacesCaption());
			}
			builder.append("\n");
			return builder.toString();
		}
		
		private String bodyString() {
			var builder = new StringBuilder();
			int index;
			
			for (int line = 0; line < getBiggestPiece(); line++) {
				for (int j = 0; j < _piecesDisplayed; j++) {
					index = getRealIndex(j + _posNeutralPawn);
					builder.append(_pieces.get(index).bodyLine(line));
				}
				builder.append("\n");
			}
			return builder.toString();
		}
		
		/**
		 * Returns the biggest height of the pieces in the _piecesDisplayed first
		 * pieces
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

		private String costString(int index) {
			return "c : " + _pieces.get(index).getCost() + _pieces.get(index).spacesCaption();
		}

		private String movesString(int index) {
			return "m : " + _pieces.get(index).getMoves() + _pieces.get(index).spacesCaption();
		}

		private String buttonString(int index) {
			return "b : " + _pieces.get(index).getButtons() + _pieces.get(index).spacesCaption();
		}

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

		public void display(boolean captions) {
			if (captions) {
				System.out.println(Caption());
			}
			System.out.println(displaySelectablePiecesNumber());
			displayPieces();

		}
	}
}
