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
		} while(!chosenVersion.equals("d") && !chosenVersion.equals("a"));
		
		switch(chosenVersion) {
		case "d": 
			playingPhase(scanner, "time_board", "load_phase1"); // a changer 
			break;
		case "a":
			playingPhase(scanner, "time_board", "load_Normal"); // a changer mauvais plateau
			break;
		}
	}
	
	private static void init(String boardFile, String pieceFile, TimeBoard timeBoard, Scanner scanner, PieceHandler pieceHandler, PlayerHandler players) {
		initTimeBoard(boardFile, timeBoard, scanner);
		initPieceHandler(pieceFile, pieceHandler, scanner);
		players.recoverNames(scanner);
		timeBoard.initPlayerPawns(players, 2);

		displayGame(timeBoard, pieceHandler); // prints the captions
	}
	
	
	private static void playingPhase(Scanner scanner, String boardFile, String pieceFile) {
		var timeBoard = new TimeBoard();
		var pieces = PieceHandler.Handler();
		var players = new PlayerHandler();
		int playerChoice;
		Player currentPlayer;
		
		init(boardFile, pieceFile, timeBoard, scanner, pieces, players);
		
		while (!timeBoard.checkEndOfGame(players.getPlayers().size())) {
			players.updateCurrentPlayer();
			currentPlayer = players.getCurrentPlayer();
			
			playerChoice = currentPlayer.buyingPhase(scanner, pieces);

			if (playerChoice == 0) {
				currentPlayer.skipTurn(players.distanceBetweenPlayers() + 1, timeBoard);
			} else {
				currentPlayer.buyPiece(pieces.getPiece(playerChoice - 1), scanner, timeBoard);
				pieces.remove(pieces.getPiece(playerChoice - 1));
				pieces.moveNeutralPawn(playerChoice - 1);
			}
			
			timeBoard.demarcateTurns();
			timeBoard.displayTimeBoard(false);
			pieces.display(false);
		}
		
		players.displayWinner();
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
