import java.util.Objects;
import java.util.Scanner;

public class Player {
	private QuiltBoard _quiltBoard;
	private int _buttonsCount;
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
		_buttonsCount = 5;
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
	
	public int getScore() {
		var partialScore = _buttonsCount - _quiltBoard.getEmpty() * 2;
		return (_specialTile) ? partialScore : partialScore + 7;
	}

	public boolean canBuyPiece(Piece piece) {
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
	
	public void addSpecialTile() {
		_specialTile = true;
	}

	public boolean addPieceToGrid(Piece p, int x, int y) {
		return _quiltBoard.addPiece(p, x, y);
	}

	public void skipTurn(Scanner scanner, int nbMoves, TimeBoard timeBoard, String version) {
		Objects.requireNonNull(timeBoard);
		if (nbMoves < 0) {
			throw new IllegalArgumentException("the player can't move back");
		}

		earnButtons(nbMoves);
		move(scanner, nbMoves, timeBoard, version);
	}

	public void payEvent() {
		earnButtons(_wage);
	}

	public void buyPiece(Piece piece, Scanner scanner, TimeBoard timeBoard, String version) {
		Objects.requireNonNull(piece);
		Objects.requireNonNull(scanner);
		Objects.requireNonNull(timeBoard);
		if (!canBuyPiece(piece)) {
			return;
		}
		
		if (version.equals("d")) {
			automaticPlacing(piece, scanner, version);
		} else {
			placingPhase(piece, scanner, version);
		}
		_buttonsCount -= piece.getCost();
		_wage += piece.getButtons();
		move(scanner, piece.getMoves(), timeBoard, version);
	}

	private void move(Scanner scanner, int nbMoves, TimeBoard timeBoard, String version) {
		Box currentBox;
		timeBoard.getBoard().get(_position).remove(this);
		for (int i = 0; i < nbMoves && _position < timeBoard.getBoard().size() - 1; i++) {
			_position++;
			currentBox = timeBoard.getBoard().get(_position);
			currentBox.boxEvent(this, scanner, version);
		}
		timeBoard.getBoard().get(_position).add(this);
	}

	// Methods - Ascii version
	public int buyingPhase(Scanner scanner, PieceHandler p) {
		int playerChoice;
		do {
			try {
				System.out.println(_name + "'s turn :\n" + "You currently have " + _buttonsCount + " buttons \n"
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

	private Piece flipPiece(Scanner scan, Piece p) {
		String res;
		do {
			System.out.println("Do you want to flip the piece");
			System.out.println("Enter f if you want to flip it or r if you want to rotate it and s if you want to stop");
			res = scan.next();
			switch (res) {
			case "f" -> p = p.flip();
			case "r" -> p = p.reverse();
			}
			System.out.println(p.bodyString());
		} while (!res.equals("s"));
		return p;
	}
	
	public void automaticPlacing(Piece piece, Scanner scanner, String version) {
		String userInput;
		
		do {
			System.out.println("Do you want to place your piece automaticly y/n");
			userInput = scanner.next();
		} while(userInput.equals("y") && userInput.equals("n"));
		if (userInput.equals("y")) {
			_quiltBoard.addPieceAutomatically(piece);
			_quiltBoard.display();
		} else {
			placingPhase(piece, scanner, version);
		}
	}

	public void placingPhase(Piece piece, Scanner scanner, String version) {
		int x, y;

		_quiltBoard.display();
		System.out.println(piece.bodyString());
		if (version.equals("a")) {
			piece = flipPiece(scanner, piece);
		}
		
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

	@Override
	public String toString() {
		var builder= new StringBuilder();
		builder.append(_name);
		builder.append("buttons").append(_buttonsCount).append("\n");
		builder.append("Wage").append(_wage).append("\n");
		return builder.toString();
	}
}
