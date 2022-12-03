import java.util.Collections;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
	private static void demoVersion(Scanner scanner) {
		var timeBoard = new TimeBoard();
		var pieceHandler = PieceHandler.Handler();
		var playerHandler = new PlayerHandler();
		int playerChoice;
		Player currentPlayer;

		initTimeBoard("load_time_board_demo", timeBoard, scanner);
		initPieceHandler("load_phase1", pieceHandler, scanner);
		playerHandler.initPlayersAscii(scanner);
		timeBoard.initPlayerPawns(playerHandler, 2);

		displayGame(timeBoard, pieceHandler); // prints the captions

		while (!timeBoard.checkEndOfGame(playerHandler.getPlayers().size())) {
			playerHandler.updateCurrentPlayer();
			currentPlayer = playerHandler.getCurrentPlayer();
			
			playerChoice = currentPlayer.buyingPhase(scanner, pieceHandler);

			if (playerChoice == 0) {
				currentPlayer.skipTurn(playerHandler.distanceBetweenPlayers() + 1, timeBoard);
			} else {
				currentPlayer.buyPiece(pieceHandler.getPiece(playerChoice - 1), scanner, timeBoard);
				pieceHandler.remove(pieceHandler.getPiece(playerChoice - 1));
				pieceHandler.moveNeutralPawn(playerChoice - 1);
			}
			
			timeBoard.demarcateTurns();
			timeBoard.displayTimeBoard(false);
			pieceHandler.display(false);
		}
		playerHandler.displayWinner();
	}
	
	private static void completeVersion(Scanner scanner) {
		var timeBoard = new TimeBoard();
		var pieceHandler = PieceHandler.Handler();
		var playerHandler = new PlayerHandler();
		int playerChoice;
		Player currentPlayer;

		initTimeBoard("load_time_board", timeBoard, scanner);
		initPieceHandler("load_Normal", pieceHandler, scanner);
		playerHandler.initPlayersAscii(scanner);
		timeBoard.initPlayerPawns(playerHandler, 2);

		displayGame(timeBoard, pieceHandler); // prints the captions

		while (!timeBoard.checkEndOfGame(playerHandler.getPlayers().size())) {
			playerHandler.updateCurrentPlayer();
			currentPlayer = playerHandler.getCurrentPlayer();
			
			playerChoice = currentPlayer.buyingPhase(scanner, pieceHandler);

			if (playerChoice == 0) {
				currentPlayer.skipTurn(playerHandler.distanceBetweenPlayers() + 1, timeBoard);
			} else {
				currentPlayer.buyPiece(pieceHandler.getPiece(playerChoice - 1), scanner, timeBoard);
				pieceHandler.remove(pieceHandler.getPiece(playerChoice - 1));
			}
			
			timeBoard.demarcateTurns();
			timeBoard.displayTimeBoard(false);
			pieceHandler.display(false);
		}
		playerHandler.displayWinner();
	}
	
	public static void main(String[] args) {
		var scanner = new Scanner(System.in);
		String chosenVersion;
		
		do {
			System.out.println("Enter 'd' to play to the demo ascii version and 'a' for the complete ascii version");
			chosenVersion = scanner.next();
		} while(!chosenVersion.equals("d") && !chosenVersion.equals("a"));
		
		switch(chosenVersion) {
		case "d": 
			demoVersion(scanner);
			break;
		case "a":
			completeVersion(scanner); // pas finis
			break;
		}
	}

	private static void displayGame(TimeBoard t, PieceHandler p) {
		t.displayTimeBoard(true);
		p.display(true);
		System.out.println("\nlet's go !!\n");
	}

	private static void initTimeBoard(String file, TimeBoard t, Scanner scanner) {
		try {
			t.loadTimeBoard(Path.of(file));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
			scanner.close();
			return;
		}
	}

	private static void initPieceHandler(String file, PieceHandler p, Scanner scanner) {
		try {
			p.loadPieces(Path.of(file));
			Collections.shuffle(p.getPieces());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
			scanner.close();
			return;
		}
	}

}
