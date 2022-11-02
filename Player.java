import java.util.Objects;
import java.util.Scanner;

public class Player {
	private QuiltBoard quiltBoard;
	//private int nbButtonOnQuiltBoard;
	private int buttons; /* je crois qu'il demare avce 5 boutons autant le mettre la */

	/* ce que j'avais fais */
	private final String name;
	private int wage; /* les boutons qu'il a sur le plateau c'es en gros sonsalair c'est plus court */
	private Pawn pawn;
	private boolean isPlayerTurn; /* c'est lui qui avance */

	public Player(String n, boolean move, String color) {
		/* ce qsue j'avais fais */
		Objects.requireNonNull(n);
		Objects.requireNonNull(move);
		name = n;
		wage = 0;
		buttons = 5;
		pawn = new Pawn(color);
		isPlayerTurn = move;
		
		/* */
		quiltBoard = new QuiltBoard();
		//nbButtonOnQuiltBoard = 0; /* = wage */
	}

	private boolean check(int value) {
		Objects.requireNonNull(value);
		return value >= 0;
	}

	public void switchTurn() {
		isPlayerTurn = !isPlayerTurn;
	}
	
	public void earnPiece(int nbButtons, int price) {
		if (check(nbButtons) && check(price)) {
			buttons -= price;
			wage += nbButtons;
		}
	}

	public void pay() {
		buttons += wage;
	}
	
	/* pourquoi cette fonction est dans cette classe ? */
	// Methods - Ascii version
	public void selectPiece(Scanner scanner) {
		int userChoice;
		do {
			try {
				System.out.println("Enter 1, 2 or 3 to select the according piece or enter 0 if you don't want to buy any pieces");
				userChoice = Integer.parseInt(scanner.next());
			} catch (NumberFormatException e) {
				userChoice = -1;
			}
		} while (userChoice < 0 || userChoice > 3);
		
		System.out.println("fin");
	}

	public int getButtons() {
		return buttons;
	}
	
	public QuiltBoard getQuiltboard() {
		return quiltBoard;
	}
	
	@Override
    public String toString() {
        return "name : " + name + ", wages : " + wage + ", buttons : " + buttons + ", " + pawn.toString();
    }
}


