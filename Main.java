import java.util.Collections;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path;

public class Main {

	public static void main(String[] args) {
		var scanner = new Scanner(System.in);
		String chosenVersion;

		do {
			System.out.println("Enter 'd' to play to the demo ascii version and 'a' for the complete ascii version");
			chosenVersion = scanner.next();
		} while (!chosenVersion.equals("d") && !chosenVersion.equals("a"));

		switch (chosenVersion) {
		case "d":
			demoVersion(scanner);
			break;
		case "a":
			completeVersion(scanner);
			break;
		}
	}

	private static void init(String boardFile, String pieceFile, TimeBoard timeBoard, Scanner scanner,
		PieceHandler pieceHandler, PlayerHandler players) {
		initTimeBoard(boardFile, timeBoard, scanner);
		initPieceHandler(pieceFile, pieceHandler, scanner);
		players.initPlayersAscii(scanner);
		timeBoard.initPlayerPawns(players, 2);
	}

	private static void demoVersion(Scanner scanner) {
		var timeBoard = new TimeBoard();
		var pieceHandler = PieceHandler.Handler();
		var playerHandler = new PlayerHandler();

		init("load_time_board_demo", "load_phase1", timeBoard, scanner, pieceHandler, playerHandler);
		
		displayGame(timeBoard, pieceHandler);
		playingPhaseDemo(timeBoard, playerHandler, scanner, pieceHandler);
		playerHandler.displayWinner();
	}

	private static void playingPhaseDemo(TimeBoard timeBoard, PlayerHandler playerHandler, Scanner scanner, PieceHandler pieceHandler) {
		int playerChoice;
		Player currentPlayer;
		while (!timeBoard.checkEndOfGame(playerHandler.getPlayers().size())) {
			playerHandler.updateCurrentPlayer();
			currentPlayer = playerHandler.getCurrentPlayer();
			playerChoice = currentPlayer.buyingPhase(scanner, pieceHandler);
			
			if (playerChoice == 0) {
				currentPlayer.skipTurn(playerHandler.distanceBetweenPlayers() + 1, timeBoard, scanner);
			} else {
				currentPlayer.buyPieceDemo(pieceHandler.getPiece(playerChoice - 1), scanner, timeBoard);
				pieceHandler.remove(pieceHandler.getPiece(playerChoice - 1));
				pieceHandler.moveNeutralPawn(playerChoice - 1);
			}
			
			timeBoard.demarcateTurns();
			timeBoard.displayTimeBoard(false);
			pieceHandler.display(false);
		}
	}
	
	private static void completeVersion(Scanner scanner) {
		var timeBoard = new TimeBoard();
		var pieceHandler = PieceHandler.Handler();
		var playerHandler = new PlayerHandler();
		var lastPiece = new Piece();
		lastPiece.parseLine("11:2:1:0");
		
		init("load_time_board", "load_Normal", timeBoard, scanner, pieceHandler, playerHandler);
		pieceHandler.add(lastPiece);
		
		displayGame(timeBoard, pieceHandler);
		playingPhaseComplete(timeBoard, playerHandler, scanner, pieceHandler);
		playerHandler.displayWinner();
	}

	private static void playingPhaseComplete(TimeBoard timeBoard, PlayerHandler playerHandler, Scanner scanner, PieceHandler pieceHandler) {
		int playerChoice;
		Player currentPlayer;
		while (!timeBoard.checkEndOfGame(playerHandler.getPlayers().size())) {
			playerHandler.updateCurrentPlayer();
			currentPlayer = playerHandler.getCurrentPlayer();
			playerChoice = currentPlayer.buyingPhase(scanner, pieceHandler);
			
			if (playerChoice == 0) {
				currentPlayer.skipTurn(playerHandler.distanceBetweenPlayers() + 1, timeBoard, scanner);
			} else {
				currentPlayer.buyPieceComplete(pieceHandler.getPiece(playerChoice - 1), scanner, timeBoard);
				pieceHandler.remove(pieceHandler.getPiece(playerChoice - 1));
				pieceHandler.moveNeutralPawn(playerChoice - 1);
				playerHandler.updateSpecialTile();
			}
			
			timeBoard.demarcateTurns();
			timeBoard.displayTimeBoard(false);
			pieceHandler.display(false);
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
