import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
    	var scanner = new Scanner(System.in);
        var timeBoard = new TimeBoard();
        var pieceHandler = PieceHandler.Handler();
        // missing  : check if the names aren't the same
        Player[] players = {new Player(getName(scanner, 1), "Blue"), new Player(getName(scanner, 2), "Red")};
        var current = 0;
        var other = 1;
        int playerChoice = 1;

        initTimeBoard("load_time_board_demo", timeBoard, scanner);
        initPieceHandler("load_pieces", pieceHandler, scanner);
//        timeBoard.initPlayerPawns(players, 2);

        game(timeBoard, pieceHandler); // prints the captions 
        
        while (pieceHandler.getSize() > 0) { // needs to be modify
			playerChoice = players[current].buyingPhase(scanner);

			if (playerChoice == 0) {
				if (players[0].getPosition() != players[1].getPosition()) {
					players[current].skipTurn(players[other].getPosition() - players[current].getPosition());
				}
			} else if (players[current].byPiece(pieceHandler.getPiece(playerChoice - 1), scanner)) {
				pieceHandler.remove(pieceHandler.getPiece(playerChoice - 1));
			}
			
			current = currentPlayer(players, current);
            other = otherPlayer(current);
			timeBoard.demarcateTurns();
			
			
			timeBoard.displayTimeBoard(false);
			pieceHandler.display(false);
        }
    }
    
    private static void game(TimeBoard t, PieceHandler p) {
    	t.displayTimeBoard(true);
    	p.display(true);
    	System.out.println("\nlet's go !!\n");
    }
    
    private static String getName(Scanner scan, int number) {
    	System.out.println("please enter player" + number + "'s name ");
    	return scan.next();
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

    private static int currentPlayer(Player[] players, int current) {
        if (players[0].getPosition() > players[1].getPosition()) {
            return 1;
        }
        if (players[0].getPosition() < players[1].getPosition()) {
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

