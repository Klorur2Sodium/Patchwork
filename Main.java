import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
	private void demoVersion() {
		
	}
	
	public static void main(String[] args) {
		var timeBoard = new TimeBoard();
		var scanner = new Scanner(System.in);
		var pieceHandler = new PieceHandler();
		var players = List.of(new Player("player1", "Blue"), new Player("player2", "Red"));
		var currentPlayer = players.get(0);
		int playerChoice, nbSpecialTile = 1;

		try {
			timeBoard.loadTimeBoard(Path.of("src/load_time_board_demo"));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
			scanner.close();
			return;
		}

		pieceHandler.loadPiecesDemo();
		timeBoard.initPlayerPawns(players);

		while (true) {
			timeBoard.displayTimeBoard();
			pieceHandler.displayBuyingPhase();

			playerChoice = currentPlayer.buyingPhase(scanner);

			if (playerChoice == 0) {
				currentPlayer.skipTurn(timeBoard);
			} else if (currentPlayer.checkEarnPiece(pieceHandler.getPiece(playerChoice - 1))) {
				var piece = pieceHandler.getPiece(playerChoice - 1);
				currentPlayer.placingPhaseDemo(piece, scanner);
				currentPlayer.move(timeBoard, piece.nbMove());
				pieceHandler.pieces().remove(piece);
			}
			currentPlayer = timeBoard.currentPlayer();
			timeBoard.demarcateTurns();
		}
	}

}