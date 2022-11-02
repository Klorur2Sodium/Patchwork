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
	private final ArrayList<Piece> pieces = new ArrayList<Piece>();


	public ArrayList<Piece> getPieces() {
		/* c'est peut etre mieux que la methode et la liste n'aient pas le memme nom */
		return pieces;
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
	
	public void add(Piece p) {
		Objects.requireNonNull(p);
		pieces.add(p);
	}
	
	private int[][] parseBody(String bodyString) {
		var lenBodyString = bodyString.length();
		var sizeArray = (int)Math.sqrt(lenBodyString);
		
		if (Math.pow(sizeArray, 2) != lenBodyString) {
			throw new IllegalArgumentException();
		}
		
		var body = new int[sizeArray][sizeArray];
		for (int i = 0; i < lenBodyString; i++) {
			body[i/sizeArray][i%sizeArray] = bodyString.charAt(i) - '0';
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
		try(var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				pieces.add(parseLine(line));
			}
		}
	}
	
	// Methods for the Ascii/demo version
	public void loadPiecesDemo() {
		var nbEachPiece = 20;
		var pieceBody = new int[][] {{1,1}, {1,1}};
		
		var piece1 = new Piece(pieceBody, 3, 4, 1);
		var piece2 = new Piece(pieceBody, 2, 2, 0);
		
		for (int i = 0; i < nbEachPiece; i++) {
			add(piece1);
			add(piece2);
		}
		
		Collections.shuffle(pieces);
	}
	
  //Graphic Methods - Ascii version
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
	
	
	public void displayPiecesBody() {
		for (int i = 0; i < getMaxSize(); i++) {
			for (var piece : pieces) {
				var bodyLen = piece.body().length;
				
				if (i < bodyLen) {
					printLine(piece, i, bodyLen);
				} else {
					printSpaces(bodyLen);
				}
				System.out.print("    ");
			}
			System.out.println();
		}
	}
	
	private void displayPiecesCost() {
		for (var piece : pieces) {
			System.out.print("c: " + piece.cost());
			printSpaces(piece.body().length);
		}
		System.out.println();
	}
	
	private void displayPiecesNbMove() {
		for (var piece : pieces) {
			System.out.print("m: " + piece.nbMove());
			printSpaces(piece.body().length);
		}
		System.out.println();
	}
	
	private void displayPiecesNbButton() {
		for (var piece : pieces) {
			System.out.print("b: " + piece.nbButton());
			printSpaces(piece.body().length);
		}
		System.out.println("\n");
	}
	
	private void displaySelectablePiecesNumber() {
		var numberOfSelectablePiece = 3;
		
		for (int i = 0; i < numberOfSelectablePiece; i++) {
			System.out.print("(" + (i+1) + ") ");
			printSpaces(pieces.get(i).body().length);
		}
		System.out.println();
	}
	
	public void displayPiecesStats() {
		displayPiecesCost();
		displayPiecesNbMove();
		displayPiecesNbButton();
	}
	
	public void displayCaption() {
		System.out.println(
			"Caption :\n"
			+ "  c : cost of the piece\n"
			+ "  m : number of moves the player has to do\n"
			+ "  b : number of buttons on the piece\n"
		);
	}
	
	public void displayBuyingPhase() {
		displayCaption();
		displaySelectablePiecesNumber();
		displayPiecesBody();
		displayPiecesStats();
	}
}







