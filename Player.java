import java.util.Objects;
import java.util.Scanner;

public class Player {
	private QuiltBoard quiltBoard;
	private int buttons;
	private final String name;
	private int wage;
	private Pawn pawn;
	boolean specialTile;

	public Player(String name, String color) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(color);
		this.name = name;
		wage = 0;
		buttons = 5;
		pawn = new Pawn(color);
		specialTile = false;

		quiltBoard = new QuiltBoard();
	}

	public Pawn pawn() {
		return pawn;
	}

	public int buttons() {
		return buttons;
	}

	public QuiltBoard quiltboard() {
		return quiltBoard;
	}

	public boolean checkEarnPiece(Piece piece) {
		if (buttons >= piece.cost()) {
			return true;
		}
		return false;
	}

	public void pay() {
		buttons += wage;
	}

	public void earnButtons(int nbButtons) {
		if (nbButtons <= 0) {
			throw new IllegalArgumentException("The player must gain a positive amount of buttons");
		}
		buttons += nbButtons;
	}

	public void skipTurn(TimeBoard timeBoard) {
		Objects.requireNonNull(timeBoard);

		while (this == timeBoard.currentPlayer()) {
			move(timeBoard, 1);
			earnButtons(1);
		}
	}

	public void move(TimeBoard timeBoard, int nbMove) {
		Objects.requireNonNull(timeBoard);

		for (int i = 0; i < nbMove; i++) {
			timeBoard.movePawn(this);
			timeBoard.board().get(this.pawn().pos()).boxEvent(this);
		}
	}

	// Methods - Ascii version
	public int buyingPhase(Scanner scanner) {
		int playerChoice;
		do {
			try {
				System.out.println(name + "'s turn :\n" + "You currently have " + buttons + " buttons \n"
						+ "Enter 1, 2 or 3 to select the according piece or enter 0 if you don't want to buy any pieces");
				playerChoice = Integer.parseInt(scanner.next());
			} catch (NumberFormatException e) {
				playerChoice = -1;
			}
		} while (playerChoice < 0 || playerChoice > 3);

		return playerChoice;
	}
	
	public void placingPhaseDemo(Piece piece, Scanner scanner) {
		String userInput;
		buttons -= piece.cost();
		wage += piece.nbButton();
		
		do {
			System.out.println("Do you want to place your piece automaticly y/n");
			userInput = scanner.next();
		} while(userInput.equals("y") && userInput.equals("n"));
		if (userInput.equals("y")) {
			quiltBoard.addPieceAutomatically(piece);
			quiltBoard.displayGrid();
		} else {
			placingPhase(piece, scanner);
		}
	}

	public void placingPhase(Piece piece, Scanner scanner) {
		int x, y;

		quiltBoard.displayGrid();
		piece.displayPiece();
		do {
			try {
				System.out.println("Enter the x coordinate of the top right corner of your piece in the quiltboard");
				x = Integer.parseInt(scanner.next());
				System.out.println("Enter the y coordinate of the top right corner of your piece in the quiltboard");
				y = Integer.parseInt(scanner.next());
			} catch (NumberFormatException e) {
				x = -1;
				y = -1;
			}
		} while (!quiltBoard.addPiece(piece, x - 1, y - 1));
		quiltBoard.displayGrid();
	}

	public void displayPayEvent() {
		System.out.println("You ran on a button and got " + wage + " button(s)");
		pay();
	}

	@Override
	public String toString() {
		return "name : " + name + ", wages : " + wage + ", buttons : " + buttons + ", " + pawn.toString();
	}
}
