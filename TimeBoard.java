import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimeBoard {
	private final ArrayList<Box> board;

	public TimeBoard() {
		board = new ArrayList<Box>();
	}

	public ArrayList<Box> board() {
		return board;
	}

	private void add(Box box) {
		Objects.requireNonNull(box);
		board.add(box);
	}

	public void initPlayerPawns(List<Player> players) {
		Objects.requireNonNull(players);

		for (int i = players.size() - 1; i >= 0; i--) {
			board.get(0).add(players.get(i));
		}
	}

	private void parseLine(String line) {
		var boxesStatus = line.split(",");

		for (var boxStatus : boxesStatus) {
			add(new Box(boxStatus.charAt(0)));
		}
	}

	public void loadTimeBoard(Path path) throws IOException {
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			String[] boxesStatus;
			while ((line = reader.readLine()) != null) {
				parseLine(line);
			}
		}
	}
	
	public void movePawn(Player player) {
		board.get(player.pawn().pos()).remove(player);
		player.pawn().move();
		board.get(player.pawn().pos()).add(player);
	}
	
	public Player currentPlayer() {
		Player currentPlayer = null;
		var index = 0;
		
		while (currentPlayer == null) {
			currentPlayer = board.get(index).getPlayer();
			index++;
		}
		
		return currentPlayer;
	}

	/* Graphic method - Ascii Version */
	private void displayCaption() {
		System.out.println("Caption :\n" + "  0 : button\n" + "  x : patch\n" + "  B, R : pawn of a player\n");
	}
	
	public void demarcateTurns() {
		System.out.println("\n--------------- NEXT TURN ---------------\n");
	}

	public void displayTimeBoard() {
		displayCaption();
		System.out.println(toString());
	}

	private String printNTimes(String line, int n) {
		var builder = new StringBuilder();
		for (int i = 0; i < n; i++) {
			builder.append(line);
		}
		builder.append("\n");

		return builder.toString();
	}

	@Override
	public String toString() {
		var builder = new StringBuilder();

		builder.append(printNTimes(" _____", board.size()));
		builder.append(printNTimes("|     ", board.size()));

		for (var box : board) {
			var asciiPawn = " ";
			if (box.getPlayer() != null) {
				asciiPawn = box.getPlayer().pawn().asciiPawn();
			}
			builder.append(box.status()).append("  ").append(asciiPawn).append("  ");
		}
		builder.append("\n");
		builder.append(printNTimes("|_____", board.size()));

		return builder.toString();
	}

}
