import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		var board = new GameBoard();
        board.init();
        System.out.println(board.toString());
		
		/* var scanner = new Scanner(System.in);
		var pieceHandler = new PieceHandler();
		var players = List.of(new Player("player1", true, "blue"), new Player("player2", false, "red"));
		var currentPlayer = players.get(0);
		
		pieceHandler.loadPiecesDemo();
		
		while (true) {
			pieceHandler.displayBuyingPhase();
			currentPlayer.selectPiece(scanner);
		}
		
		try {
			pieceHandler.loadPieces(Path.of("src/load_pieces"));
		} catch (IOException e) {
    	System.err.println(e.getMessage());
	    System.exit(1);
	    return;
		}
		
		
		pieceHandler.displayPiecesBody();
		pieceHandler.displayPiecesStats();
		
		for (int i = 0; i < 15; i++) {
			currentPlayer.quiltboard().addPieceAutomatically(pieceHandler.pieces().get(0));
			pieceHandler.pieces().remove(0);
			currentPlayer.quiltboard().displayGrid();
		}
		System.out.println(currentPlayer.quiltboard().nbButtons());
		 */
	}
	
} 