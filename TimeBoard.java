package fr.uge.patchwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class stores the information about the TimeBoard. It also handles its
 * representation in Ascii and its initialization from a file.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class TimeBoard extends GraphicalObject {
	private final ArrayList<Box> _board = new ArrayList<Box>();

	/**
	 * Return the ArrayList in which the informations about the board are stored.
	 * 
	 * @return List representing the board
	 */
	public List<Box> getBoard() {
		return _board;
	}

	/**
	 * Return the size of the board
	 * 
	 * @return size
	 */
	public int getSize() {
		return _board.size();
	}

	/**
	 * Initializes the board by parsing the given file
	 * 
	 * @param path : path of the file
	 * @throws IOException : if file not found
	 */
	public void loadTimeBoard(Path path) throws IOException {
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				parseLine(line);
			}
		}
	}

	/**
	 * Initializes the players at position 0 in the board
	 * 
	 * @param playerHandler : player handler
	 * @param size : size
	 */
	public void initPlayerPawns(PlayerHandler playerHandler, int size) {
		Objects.requireNonNull(playerHandler);

		for (int i = size - 1; i >= 0; i--) {
			_board.get(0).add(playerHandler.getPlayerIndex(i));
		}
	}

	/**
	 * Appends a non null box at the end of the board
	 * 
	 * @param box
	 */
	private void add(Box box) {
		Objects.requireNonNull(box);
		_board.add(box);
	}

	/**
	 * Initializes all the status of the boxes in the board by parsing the given
	 * line.
	 * 
	 * @param line
	 */
	private void parseLine(String line) {
		var boxesStatus = line.split(",");

		for (var boxStatus : boxesStatus) {
			add(new Box(boxStatus.charAt(0)));
		}
	}

	/* Graphic method - Ascii Version */

	/**
	 * Demarcate each turn for esthetic reasons
	 */
	public void demarcateTurns() {
		System.out.println("\n--------------- NEXT TURN ---------------\n");
	}

	/**
	 * Displays the TimeBoard in Ascii art with or without the captions depending on
	 * the given boolean captions.
	 * 
	 * @param captions : must be true if you want the captions
	 * @param posCurrent : current position
	 */
	public void displayTimeBoard(boolean captions, int posCurrent) {
		if (captions) {
			displayCaption();
		}
		display(posCurrent);
	}
	
	
	/**
	 * The function returns the number of boxes it's necessary to print
	 * @param posCurrent
	 * @return the number of boxes
	 */
	private int getRemainningSize(int posCurrent) {
		return (_board.size() - posCurrent > Constants.WINDOW_SIZE.getValue()) ? Constants.WINDOW_SIZE.getValue() : _board.size() - posCurrent;
	}

	/**
	 * Displays the TimeBoard in Ascii art
	 * 
	 * @param posCurrent : current position
	 */
	public void display(int posCurrent) {
		var builder = new StringBuilder();
		int size = getRemainningSize(posCurrent);

		builder.append(printNTimes(" _____", size));
		builder.append(printNTimes("|     ", size));

		for (int i = posCurrent; i < size; i++) {
			String asciiPawn = " ";
			if (_board.get(i).getPlayer() != null) {
				asciiPawn = _board.get(i).getPlayer().display(); // modification
			}
			builder.append(_board.get(i).toString()).append("  ").append(asciiPawn).append("  ");
		}
		builder.append("\n");
		builder.append(printNTimes("|_____", size));

		System.out.println(builder.toString());
	}

	/**
	 * Displays the captions
	 */
	private void displayCaption() {
		System.out.println("Caption :\n" + "  0 : button\n" + "  x : patch\n" + "  B, R : pawn of a player\n");
	}
	
	/**
	 * Returns the pattern line stored n times in a string.
	 * 
	 * @param line : the pattern you want to duplicate
	 * @param n    : the number of times you want to duplicate the pattern
	 * @return a String in which there is n times the given line
	 */
	private String printNTimes(String line, int n) {
		var builder = new StringBuilder();
		for (int i = 0; i < n; i++) {
			builder.append(line);
		}
		builder.append("\n");

		return builder.toString();
	}
	
	/**
	 * the functions draw a box line that begins on (x, y)
	 * @param context
	 * @param x
	 * @param y
	 */
	private void drawLine(Graphics2D graphics, float x, float y) {
		var line = new Line2D.Float(x, y, x, y + Constants.BOX_SIZE.getValue());
		graphics.setStroke(new BasicStroke(5));
		graphics.setColor(Color.BLACK);
		graphics.draw(line);
	}
	
	/**
	 * The function draws the TimeBoard
	 * @param context
	 * @param width
	 * @param posCurrent position of the current player 
	 */
	@Override
	protected void onDraw(Graphics2D graphics) {
		int box = Constants.BOX_SIZE.getValue();
		float boxWidth = width / Constants.WINDOW_SIZE.getValue();
		var firstPlayerIdx = IntStream.range(0,_board.size())
				.filter(i -> _board.get(i).hasPlayer())
				.findFirst()
				.orElse(-1);
		
		int size = getRemainningSize(firstPlayerIdx);
		Line2D line = new Line2D.Float(5, box, width - 5, box);
		graphics.setStroke(new BasicStroke(5));
		graphics.setColor(Color.BLACK);
		graphics.draw(line);
		for (var i = 0; i < size; i++) {
			drawLine(graphics, i * boxWidth, 0);
			if (_board.get(i + firstPlayerIdx).hasPlayer()) {
				var pawn = _board.get(i + firstPlayerIdx).getPlayer().getPawn();
				pawn.SetGraphicalProperties(i * boxWidth + boxWidth / 2, box/2, 20);
				pawn.draw(graphics);
			}
			_board.get(i+firstPlayerIdx).SetGraphicalProperties(i*boxWidth, box/2, 10);
			_board.get(i+firstPlayerIdx).draw(graphics);
		}
	}
	
}
