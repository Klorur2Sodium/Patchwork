import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TimeBoard {
	private final ArrayList<Box> _board = new ArrayList<Box>();
	
	public List<Box> getBoard() {
		return _board;
	}
	
	public int getSize() {
		return _board.size();
	}
	
	public void loadTimeBoard(Path path) throws IOException {
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				parseLine(line);
			}
		}
	}
	
	public void initPlayerPawns(PlayerHandler playerHandler, int size) {
		Objects.requireNonNull(playerHandler);

		for (int i = size - 1; i >= 0; i--) {
			_board.get(0).add(playerHandler.getPlayerIndex(i));
		}
	}
	
	private void add(Box box) {
		Objects.requireNonNull(box);
		_board.add(box);
	}

	private void parseLine(String line) {
		var boxesStatus = line.split(",");

		for (var boxStatus : boxesStatus) {
			add(new Box(boxStatus.charAt(0)));
		}
	}

	

	/* Graphic method - Ascii Version */
	
	public void demarcateTurns() {
		System.out.println("\n--------------- NEXT TURN ---------------\n");
	}
	
	public void displayTimeBoard(boolean captions) {
		if (captions) {
			displayCaption();
		}
		display();
	}

	public void display() {
		var builder = new StringBuilder();

		builder.append(printNTimes(" _____", _board.size()));
		builder.append(printNTimes("|     ", _board.size()));

		for (var box : _board) {
			String asciiPawn = " ";
			if (box.getPlayer() != null) {
				asciiPawn = box.getPlayer().getPawn().toString();
			}
			builder.append(box.toString()).append("  ").append(asciiPawn).append("  ");
		}
		builder.append("\n");
		builder.append(printNTimes("|_____", _board.size()));

		System.out.println(builder.toString());
	}
	
	private void displayCaption() {
		System.out.println("Caption :\n" + "  0 : button\n" + "  x : patch\n" + "  B, R : pawn of a player\n");
	}
	
	private String printNTimes(String line, int n) {
		var builder = new StringBuilder();
		for (int i = 0; i < n; i++) {
			builder.append(line);
		}
		builder.append("\n");

		return builder.toString();
	}
}
