import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
    	var scanner = new Scanner(System.in);
        var timeBoard = new TimeBoard();
        var pieceHandler = PieceHandler.Handler();
        int[] positions = {1, 1};
        Player[] players = {new Player("player1", "Blue"), new Player("player2", "Red")};
        var current = 0;
        var other = 1;
        int playerChoice = 1;

        initTimeBoard("load_time_board_demo", timeBoard, scanner);
        initPieceHandler("load_pieces", pieceHandler, scanner);
        timeBoard.initPlayerPawns(players, 2);

        while (pieceHandler.getSize() > 0) {
			timeBoard.displayTimeBoard();
			pieceHandler.display();

			playerChoice = players[current].buyingPhase(scanner);

			if (playerChoice == 0) {
				players[current].skipTurn(positions[current] - positions[other]);
			} else if (players[current].canByPiece(pieceHandler.getPiece(playerChoice - 1))) {
				var piece = pieceHandler.getPiece(playerChoice - 1);
				players[current].placingPhaseDemo(piece, scanner);
                positions[current] = piece.getMoves();
				pieceHandler.remove(piece);
			}
			current = currentPlayer(positions, current);
            other = otherPlayer(current);
			timeBoard.demarcateTurns();
        }
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
		p.mix();
        } catch (IOException e) {
        	System.err.println(e.getMessage());
        	System.exit(1);
        	scanner.close();
        	return;
        }
	}

    private static int currentPlayer(int[] playersPositions, int current) {
        if (playersPositions[0] > playersPositions[1]) {
            return 1;
        }
        if (playersPositions[0] < playersPositions[1]) {
            return 0;
        }
        return current;
    }

    private static int otherPlayer(int current) {
        if (current == 0) {
            return 1;
        }
        return 0;
    }
}
