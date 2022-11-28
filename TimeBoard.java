import java.util.ArrayList;
import java.util.Objects;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TimeBoard {
//    private final ArrayList<Box> _board1 = new ArrayList<Box>();
    private final ArrayList<Character> _board = new ArrayList<>();

//    private void add1(Box box) {
//		Objects.requireNonNull(box);
//		_board.add(box);
//	}
    
    private void add(Character status) {
    	Objects.requireNonNull(status);
		_board.add(status);
    }

//    private void parseLine1(String line) {
//		var boxesStatus = line.split(",");
//
//		for (var boxStatus : boxesStatus) {
//			add(new Box(boxStatus.charAt(0)));
//		}
//	}
    
    private void parseLine(String line) {
    	var boxesStatus = line.split(",");
    	
    	for (var boxStatus : boxesStatus) {
    		add(boxStatus.charAt(0));
    	}
    }

    public void loadTimeBoard(Path path) throws IOException {
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				parseLine(line);
			}
		}
	}

//	public void initPlayerPawns(Player[] players, int size) {
//		Objects.requireNonNull(players);
//
//		for (int i = size - 1; i >= 0; i--) {
//			_board.get(0).add(players[i]);
//		}
//	}

    /* Graphic method - Ascii Version */
	private void displayCaption() {
		System.out.println("Caption :\n" + "  0 : button\n" + "  x : patch\n" + "  B, R : pawn of a player\n");
	}
	
	public void demarcateTurns() {
		System.out.println("\n--------------- NEXT TURN ---------------\n");
	}

	public void displayTimeBoard(boolean captions) {
		if (captions) {
			displayCaption();
		}
		display(new Player("init", "Blue"), new Player("init", "Red"));
	}

	private String printNTimes(String line, int n) {
		var builder = new StringBuilder();
		for (int i = 0; i < n; i++) {
			builder.append(line);
		}
		builder.append("\n");

		return builder.toString();
	}
	
//	public void display1() {
//		var builder = new StringBuilder();
//
//		builder.append(printNTimes(" _____", _board.size()));
//		builder.append(printNTimes("|     ", _board.size()));
//
//		for (var box : _board) {
//			String asciiPawn = " ";
//			if (box.getPlayer() != null) {
//				asciiPawn = box.getPlayer().getPawn().toString();
//			}
//			builder.append(box.toString()).append("  ").append(asciiPawn).append("  ");
//		}
//		builder.append("\n");
//		builder.append(printNTimes("|_____", _board.size()));
//
//		System.out.println(builder.toString());
//	}
	
	public void display(Player one, Player two) {
		var builder = new StringBuilder();
		builder.append(printNTimes(" _____", _board.size()));
		builder.append(printNTimes("|     ", _board.size()));
		
		for (int i = 0; i < _board.size(); i++) {
			String asciiPawn = " ";
			if (one.getPosition() == i) {
				asciiPawn = one.getPawn().toString();
			} else if (two.getPosition() == i) {
				asciiPawn = two.getPawn().toString();
			}
			builder.append(_board.get(i)).append("  ").append(asciiPawn).append("  ");
		}
		builder.append("\n");
		builder.append(printNTimes("|_____", _board.size()));

		System.out.println(builder.toString());
	}

}
