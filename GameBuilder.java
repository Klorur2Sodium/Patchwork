package fr.uge.patchwork;

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
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class GameBuilder implements IGameVersionSelector, IGamePlayerSelector, IGameBuilder {
	
	private Player[] _players;
	private Constants _chosenVersion;
	private final Scanner _scanner;
	
	/**
	* The method creates a new GameBuilder
	* It allows to create a gameBuilder (only one can be created).
	*
	* @param scan : a scanner
	* @return IGameVersionSelector
	*/
	public static IGameVersionSelector getVersionSelector(Scanner scan) {
		return new GameBuilder(scan);
	}
	
	/**
	* The method is the constructor of the class it takes a scanner and puts it on _scanner
	*
	* @param scan : a scanner
	*/
	private GameBuilder(Scanner scan) {
		_scanner = scan;
	}
	
	/**
	 * The method returns the version chosen by the players
	 * 
	 * @return String 
	 */
	public Constants getVersion() {
		return _chosenVersion;
	}
	
	private Constants readVersion(String choice) {
		return switch(choice) {
			case("a") -> Constants.PHASE2;
			case("d") -> Constants.PHASE1;
			case("g") -> Constants.PHASE3;
			default -> Constants.DEFAULT;
		};
	}
	
	/**
	 * The method asks the player to choose a version of the game and allows to go to the next
	 * step (choosing the players)
	 * 
	 * @return IGamePlayerSelector
	 */
	public IGamePlayerSelector chooseVersion() {
		do {
			System.out.println("Enter 'd' to play to the demo ascii version");
			System.out.println("'a' for the complete ascii version");
			System.out.println("'g' for the graphic version");
			_chosenVersion = readVersion(_scanner.next());
		} while(_chosenVersion == Constants.DEFAULT);
				
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
		var players = new PlayerHandler(_players, !_chosenVersion.equals("d"));
		
		var boardFile = _chosenVersion == Constants.PHASE1 ? "load_time_board_demo" :  "load_time_board";
		var pieceFile = _chosenVersion == Constants.PHASE1 ? "load_phase1" : "load_Normal";
		
		init(boardFile, pieceFile, timeBoard, pieces, players);
		
		return new Game(timeBoard, players, pieces, _chosenVersion); 
	}
	
	/**
	 * The method uses two files to initializes the timeBoard and the pieceHandler 
	 * 
	 * @param boardFile : file containing the informations of the TimeBoard
	 * @param pieceFile : file containing the informations of all the pieces
	 * @param timeBoard : the time board
	 * @param pieceHandler : the piece handler
	 * @param players : the list containing the players
	 */
	private void init(String boardFile, String pieceFile, TimeBoard timeBoard, PieceHandler pieceHandler, PlayerHandler players) {
		initTimeBoard(boardFile, timeBoard);
		initPieceHandler(pieceFile, pieceHandler);
		timeBoard.initPlayerPawns(players, 2);
	}
	
	/**
	 * The method initializes the timeBoard by reading each line of the file given 
	 * 
	 * @param file : the file containing the information
	 * @param timeBoard : the time board
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
	 * @param file : the file containing the information
	 * @param pieceHandler : the piece handler
	 */
	private void initPieceHandler(String file, PieceHandler piecHandler) {
		try {
			piecHandler.loadPieces(Path.of(file));
			Collections.shuffle(piecHandler.getPieces());
		} catch (IOException e) {
			System.out.println("pitié");
			System.err.println(e.getMessage());
			System.exit(1);
			_scanner.close();
			return;
		}
	}
}
	
