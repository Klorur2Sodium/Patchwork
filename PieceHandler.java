import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * This class stores the pieces that are still arround the time board.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class PieceHandler {
	private final ArrayList<Piece> pieces;
	private int neutralPawn;
	private int numPiecesDisplay;
	
	public PieceHandler() {
		pieces = new ArrayList<Piece>();
		neutralPawn = 0;
		numPiecesDisplay = 10;
	}

	public ArrayList<Piece> pieces() {
		return pieces;
	}
	
	private int getRealIndex(int index) {
		if (index >= pieces.size()) {
			return index - pieces.size();
		}
		return index;
	}
	
	public void updateNumPiecesDisplay() {
		if (numPiecesDisplay < 10) {
			numPiecesDisplay = pieces.size();
		}
	}

	public void add(Piece p) {
		Objects.requireNonNull(p);
		pieces.add(p);
	}
	
	public Piece getPiece(int index) {
		if (index > pieces.size()) {
			throw new ArrayIndexOutOfBoundsException("Index must be strictly smaller than the size of pieces");
		}
		
		index = getRealIndex(index + neutralPawn);
		return pieces.get(index);
	}

	private int[][] parseBody(String bodyString) {
		var lenBodyString = bodyString.length();
		var sizeArray = (int) Math.sqrt(lenBodyString);

		if (Math.pow(sizeArray, 2) != lenBodyString) {
			throw new IllegalArgumentException();
		}

		var body = new int[sizeArray][sizeArray];
		for (int i = 0; i < lenBodyString; i++) {
			body[i / sizeArray][i % sizeArray] = bodyString.charAt(i) - '0';
		}
		return body;
	}

	private Piece parseLine(String line) {
		var splitLine = line.split(":");

		var body = parseBody(splitLine[0]);
		var cost = Integer.parseInt(splitLine[1]);
		var nbMove = Integer.parseInt(splitLine[2]);
		var nbButton = Integer.parseInt(splitLine[3]);

		return new Piece(body, cost, nbMove, nbButton);
	}

	public void loadPieces(Path path) throws IOException {
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				pieces.add(parseLine(line));
			}
		}
	}

	// Methods for the Ascii/demo version
	public void loadPiecesDemo() {
		var nbEachPiece = 20;
		var pieceBody = new int[][] { { 1, 1 }, { 1, 1 } };

		var piece1 = new Piece(pieceBody, 3, 4, 1);
		var piece2 = new Piece(pieceBody, 2, 2, 0);

		for (int i = 0; i < nbEachPiece; i++) {
			add(piece1);
			add(piece2);
		}

		Collections.shuffle(pieces);
	}

	// Graphic Methods - Ascii version
	private void printLine(Piece piece, int index, int bodyLength) {
		for (int i = 0; i < bodyLength && i < bodyLength; i++) {
			if (piece.body()[index][i] == 0) {
				System.out.print(' ');
			} else {
				System.out.print('x');
			}
		}
	}

	private void printSpaces(int nbSpaces) {
		for (int i = 0; i < nbSpaces; i++) {
			System.out.print(' ');
		}
	}
	
	public int getMaxSize() {
		var maxSize = 0;

		for (var piece : pieces) {
			if (piece.body().length > maxSize) {
				maxSize = piece.body().length;
			}
		}
		return maxSize;
	}

	public void displayPiecesBody() {
		int index;
		for (int i = 0; i < getMaxSize(); i++) {
			for (int j = neutralPawn; j < neutralPawn + numPiecesDisplay; j++) {
				index = getRealIndex(j);
				var bodyLen = pieces.get(index).body().length;

				if (i < bodyLen) {
					printLine(pieces.get(index), i, bodyLen);
				} else {
					printSpaces(bodyLen);
				}
				System.out.print("    ");
			}
			System.out.println();
		}
	}

	private void displayPiecesCost() {
		int index;
		for (int i = neutralPawn; i < neutralPawn + numPiecesDisplay; i++) {
			index = getRealIndex(i);
			System.out.print("c: " + pieces.get(index).cost());
			printSpaces(pieces.get(index).body().length);
		}
		System.out.println();
	}

	private void displayPiecesNbMove() {
		int index;
		for (int i = neutralPawn; i < neutralPawn + numPiecesDisplay; i++) {
			index = getRealIndex(i);
			System.out.print("m: " + pieces.get(index).nbMove());
			printSpaces(pieces.get(index).body().length);
		}
		System.out.println();
	}

	private void displayPiecesNbButton() {
		int index;
		for (int i = neutralPawn; i < neutralPawn + numPiecesDisplay; i++) {
			index = getRealIndex(i);
			System.out.print("b: " + pieces.get(index).nbButton());
			printSpaces(pieces.get(index).body().length);
		}
		System.out.println("\n");
	}

	private void displaySelectablePiecesNumber() {
		var numberOfSelectablePiece = 3;

		for (int i = 0; i < numberOfSelectablePiece; i++) {
			System.out.print("(" + (i + 1) + ") ");
			printSpaces(pieces.get(i).body().length);
		}
		System.out.println();
	}

	private void displayPiecesStats() {
		displayPiecesCost();
		displayPiecesNbMove();
		displayPiecesNbButton();
	}

	private void displayCaption() {
		System.out.println(
				"Caption :\n" + 
				"  c : cost of the piece\n" + 
				"  m : number of moves the player has to do\n" + 
				"  b : number of buttons on the piece\n");
	}

	public void displayBuyingPhase() {
		displayCaption();
		displaySelectablePiecesNumber();
		displayPiecesBody();
		displayPiecesStats();
	}
}
