import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
    	var scanner = new Scanner(System.in);
        var timeBoard = new TimeBoard();
        var pieceHandler = PieceHandler.Handler();
        String names = recoverNames(scanner);
        var namesSplit = names.split(",");
        Player[] players = {new Player(namesSplit[0], "Blue"), new Player(namesSplit[0], "Red")};
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

    private static String recoverNames(Scanner scan) {
    	String name1, name2;
    	do {
    		System.out.println("Player 1 please enter your name : ");
    		name1 = scan.next();
    		System.out.println("Player 2 please enter your name : ");
    		name2 = scan.next();
    	} while (name1.equals(name2));
    	return name1 + "," + name2;
    }
    
    private static void game(TimeBoard t, PieceHandler p) {
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

