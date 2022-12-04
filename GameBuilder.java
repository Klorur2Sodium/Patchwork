import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Scanner;

/**
 * The class stores an array of players the version chosen by the players and a scanner
 * This class allows the construction of the the class Game.
 * It implements IGameVersionSelector, IGamePlayerSelector, IGameBuilder to force the user to 
 * initialize the game in the right order.
 * 
 * @author FRAIZE Victor
 * @author COUSSON Sophie
 *
 */

public class GameBuilder implements IGameVersionSelector, IGamePlayerSelector, IGameBuilder {
	
	private Player[] _players;
	private String _chosenVersion;
	private final Scanner _scanner;
	
	/**
	* The method creates a new GameBuilder
	* It allows to create a gameBuilder (only one can be created).
	*
	* @param scann
	* @return IGameVersionSelector
	*/
	public static IGameVersionSelector getVersionSelector(Scanner scan) {
		return new GameBuilder(scan);
	}
	
	/**
	* The method is the constructor of the class it takes a scanner and puts it on _scanner
	*
	* @param scan
	*/
	private GameBuilder(Scanner scan) {
		_scanner = scan;
	}
	
	/**
	 * The method returns the version chosen by the players
	 * 
	 * @return String 
	 */
	public String getVersion() {
		return _chosenVersion;
	}
	
	/**
	 * The method asks the player to choose a version of the game and allows to go to the next
	 * step (choosing the players)
	 * 
	 * @return IGamePlayerSelector
	 */
	public IGamePlayerSelector chooseVersion() {
		do {
			System.out.println("Enter 'd' to play to the demo ascii version and 'a' for the complete ascii version");
			_chosenVersion = _scanner.next();
		} while(!_chosenVersion.equals("d") && !_chosenVersion.equals("a"));
				
		return this;
	}
	
	
	/**
	 * The method adds player to the players' array and allows to go to the next
	 * step (building the game)
	 * 
	 * @return IGameBuilder
	 */
	public IGameBuilder addPlayers() {
		
		_players = new Player[2];
		
		String name1, name2;
		do {
			System.out.println("Player1 (Blue) please enter your name :");
			name1 = _scanner.next();
			System.out.println("Player2 (Red) please enter your name :");
			name2 = _scanner.next();
			if (name1.length() > 30 || name2.length() > 30) {
				System.out.println("Too long");
				name1 = name2;
			}
		} while (name1.equals(name2));
		_players[0] = new Player(name1, "Blue");
		_players[1] = new Player(name2, "Red");
		
		return this;
	}
	
	/**
	 * The method builds a new game
	 * 
	 * @return Game
	 */
	public Game build() {
		var timeBoard = new TimeBoard();
		var pieces = PieceHandler.Handler();
		var players = new PlayerHandler(_players, _chosenVersion.equals("a"));
		
		var boardFile = _chosenVersion.equals("a") ? "load_time_board" : "load_time_board_demo";
		var pieceFile = _chosenVersion.equals("a") ? "load_Normal" : "load_phase1";
		
		init(boardFile, pieceFile, timeBoard, pieces, players);
		
		return new Game(timeBoard, players, pieces, _chosenVersion); 
	}
	
	/**
	 * The method uses two files to initializes the timeBoard and the pieceHandler 
	 * 
	 * @param boardFile
	 * @param pieceFile
	 * @param timeBoard
	 * @param pieceHandler
	 * @param players
	 */
	private void init(String boardFile, String pieceFile, TimeBoard timeBoard, PieceHandler pieceHandler, PlayerHandler players) {
		initTimeBoard(boardFile, timeBoard);
		initPieceHandler(pieceFile, pieceHandler);
		timeBoard.initPlayerPawns(players, 2);
	}
	
	/**
	 * The method initializes the timeBoard by reading each line of the file given 
	 * 
	 * @param file
	 * @param timeBoard
	 */
	private void initTimeBoard(String file, TimeBoard timeBoard) {
		try {
			timeBoard.loadTimeBoard(Path.of(file));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
			_scanner.close();
			return;
		}
	}

	/**
	 * The method initializes the pieceHandler by reading each line of the file given
	 * 
	 * @param file
	 * @param pieceHandler
	 */
	private void initPieceHandler(String file, PieceHandler pieceHandler) {
		try {
			pieceHandler.loadPieces(Path.of(file));
			Collections.shuffle(pieceHandler.getPieces());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
			_scanner.close();
			return;
		}
	}
}
	
