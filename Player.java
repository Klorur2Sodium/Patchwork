import java.util.Objects;
import java.util.Scanner;

public class Player {
    private QuiltBoard _quiltBoard;
    private int _buttonsCount;
	private final String _name;
	private int _wage;
	private Pawn _pawn;
	private int _position;

    public Player(String name, String color) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(color);
		_name = name;
		_wage = 0;
		_buttonsCount = 5;
		_pawn = new Pawn(color);
		_position = 0;

		_quiltBoard = new QuiltBoard();
	}

	public String getName() {return _name;}
	public Pawn getPawn() {return _pawn;}
	public int getPosition() {return _position;}

	
    public boolean canByPiece(Piece piece) {
        return _buttonsCount >= piece.getCost();
    }

    public void earnWage() {
        _buttonsCount += _wage;
    }

    public void earnButtons(int nbButtons) {
		if (nbButtons < 0) {
			throw new IllegalArgumentException("The player must gain a positive amount of buttons");
		}
		_buttonsCount += nbButtons;
	}

	public boolean addPieceToGrid(Piece p, int x, int y) {
		return _quiltBoard.addPiece(p, x, y);
	}

	public void skipTurn(int nbMoves) {
		if (nbMoves < 0) {
			throw new IllegalArgumentException("the player can't move back");
		}
		earnButtons(nbMoves);
		_position += nbMoves;
	}
	
	public boolean byPiece(Piece p, Scanner scan) {
		if (!canByPiece(p)) {
			return false;
		}
		placingPhaseDemo(p, scan);
		_position += p.getMoves();
		_buttonsCount -= p.getCost();
		_wage += p.getButtons();
		return true;
	}

	// Methods - Ascii version
	public int buyingPhase(Scanner scanner) {
		int playerChoice;
		do {
			try {
				System.out.println(_name + "'s turn :\n" + "You currently have " + _buttonsCount + " buttons \n"
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
		
		do {
			System.out.println("Do you want to place your piece automaticly y/n");
			userInput = scanner.next();
		} while(userInput.equals("y") && userInput.equals("n"));
		if (userInput.equals("y")) {
			_quiltBoard.addPieceAutomatically(piece);
			_quiltBoard.display();
		} else {
			placingPhase(piece, scanner);
		}
	}

	public void placingPhase(Piece piece, Scanner scanner) {
		int x, y;

		System.out.println(_quiltBoard);
		System.out.println(piece);
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
		} while (!_quiltBoard.addPiece(piece, x - 1, y - 1));
		System.out.println(_quiltBoard);
	}
	
	public void temp(Piece p, int x, int y) {
		_quiltBoard.addPiece(p, x - 1, y - 1);
	}
}
