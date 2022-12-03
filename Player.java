import java.util.Objects;
import java.util.Scanner;

public class Player {
	private QuiltBoard _quiltBoard;
	private int _buttonsNumber;
	private final String _name;
	private int _wage;
	private Pawn _pawn;
	private int _position;
	private boolean _specialTile;

	public Player(String name, String color) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(color);
		_name = name;
		_wage = 0;
		_buttonsNumber = 5;
		_pawn = new Pawn(color);
		_position = 0;
		_specialTile = false;

		_quiltBoard = new QuiltBoard();
	}

	public String getName() {
		return _name;
	}

	public Pawn getPawn() {
		return _pawn;
	}

	public int getPosition() {
		return _position;
	}

	public QuiltBoard getQuiltboard() {
		return _quiltBoard;
	}

	public int getScoreDemo() {
		return _buttonsNumber + _quiltBoard.getScore(); 
	}
	
	public int getScore() {
		var partialScore = _buttonsNumber + _quiltBoard.getScore();
		return (_specialTile) ? partialScore : partialScore + 7;
	}

	public boolean canBuyPiece(Piece piece) {
		return _buttonsNumber >= piece.getCost();
	}

	public void earnWage() {
		_buttonsNumber += _wage;
	}

	public void earnButtons(int nbButtons) {
		if (nbButtons < 0) {
			throw new IllegalArgumentException("The player must gain a positive amount of buttons");
		}
		_buttonsNumber += nbButtons;
	}

	public boolean addPieceToGrid(Piece p, int x, int y) {
		return _quiltBoard.addPiece(p, x, y);
	}

	public void skipTurn(int nbMoves, TimeBoard timeBoard) {
		Objects.requireNonNull(timeBoard);

		if (nbMoves < 0) {
			throw new IllegalArgumentException("the player can't move back");
		}
		earnButtons(nbMoves);
		move(nbMoves, timeBoard);
	}

	public void payEvent() {
		System.out.println();
		earnButtons(_quiltBoard.getButtons());
	}

	public void buyPiece(Piece p, Scanner scan, TimeBoard timeBoard) {
		Objects.requireNonNull(p);
		Objects.requireNonNull(scan);
		Objects.requireNonNull(timeBoard);

		placingPhaseDemo(p, scan);
		_buttonsNumber -= p.getCost();
		_wage += p.getButtons();
		move(p.getMoves(), timeBoard);
	}

	private void move(int nbMoves, TimeBoard timeBoard) {
		Box currentBox;
		timeBoard.getBoard().get(_position).remove(this);
		for (int i = 0; i < nbMoves && _position < timeBoard.getBoard().size() - 1; i++) {
			currentBox = timeBoard.getBoard().get(_position);
			_position++;
			currentBox.boxEvent(this);
		}
		timeBoard.getBoard().get(_position).add(this);
	}

	// Methods - Ascii version
	public int buyingPhase(Scanner scanner, PieceHandler p) {
		int playerChoice;
		do {
			try {
				System.out.println(_name + "'s turn :\n" + "You currently have " + _buttonsNumber + " buttons \n"
						+ "Enter 1, 2 or 3 to select the according piece or enter 0 if you don't want to buy any pieces");
				playerChoice = Integer.parseInt(scanner.next());
			} catch (NumberFormatException e) {
				playerChoice = -1;
			}
			if (playerChoice == 0) {
				return playerChoice;
			}
		} while (playerChoice < 1 || playerChoice > 3 || !canBuyPiece(p.getPiece(playerChoice - 1)));

		return playerChoice;
	}

	public void placingPhaseDemo(Piece piece, Scanner scanner) {
		String userInput;

		do {
			System.out.println("Do you want to place your piece automaticly y/n");
			userInput = scanner.next();
		} while (userInput.equals("y") && userInput.equals("n"));
		if (userInput.equals("y")) {
			_quiltBoard.addPieceAutomatically(piece);
			_quiltBoard.display();
		} else {
			placingPhase(piece, scanner);
		}
	}

	private Piece flipPiece(Scanner scan, Piece p) {
		String res;
		do {
			System.out.println("Do you want to flip the piece");
			System.out.println("Enter f if you want to flip it or r if you want to rotate it clockwise and s if you want to stop");
			res = scan.next();
			switch (res) {
			case "f" -> p = p.flip();
			case "r" -> p = p.reverse();
			}
			System.out.println(p.bodyString());
		} while (!res.equals("s"));
		return p;
	}

	public void placingPhase(Piece piece, Scanner scanner) {
		int x, y;

		_quiltBoard.display();
		System.out.println(piece.bodyString());
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
		_quiltBoard.display();
	}
	
	public void placingPhaseCompleteAscii(Piece piece, Scanner scanner) {
		piece = flipPiece(scanner, piece);
		placingPhase(piece, scanner);
	}

}
