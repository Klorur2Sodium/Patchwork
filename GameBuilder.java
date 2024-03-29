package fr.uge.patchwork;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
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
	
	private IOpponent[] _opponents;
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
		Objects.requireNonNull(scan);
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
	
	/**
	 * Parses the given String in an enum representing the chosen version of the game.
	 * @param choice : the given String.
	 * @return the enum representing the chosen version
	 */
	private Constants readVersion(String choice) {
		return switch(choice) {
			case("a") -> Constants.PHASE2;
			case("d") -> Constants.PHASE1;
			case("g") -> Constants.PHASE3;
			case("s") -> Constants.PHASE4;
			case("o") -> Constants.PHASE5;
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
			System.out.println("      'a' to play to the complete ascii version");
 			System.out.println("      'g' to play to the graphic version with a friend");
 			System.out.println("      's' to play to the graphic version alone");
			System.out.println("      'o' if you want to try an orignial version");
			_chosenVersion = readVersion(_scanner.next());
		} while(_chosenVersion == Constants.DEFAULT);
				
		return this;
	}
	
	/**
	 * Gets the names of the players by asking them and then by storing them in the given array.
	 * @param names : Array containing the names.
	 * @param nbNames : number of wanted names.
	 */
	private void getNames(String[] names, int nbNames) {
 		for (int i = 0; i < nbNames; i++) {
 			System.out.println("Player " + (i+1) + " please enter your name :");
 			names[i] = _scanner.next();
 			if (names[i].length() > 30) {
 				System.out.println("Too long");
 				i--;
 			}
 			if (i >= 1 && names[i].equals(names[i-1])) {
 				System.out.println("Your name must be unique");
 				i--;
 			}
 		}
 	}

	/**
	 * Parses the given String in an enum representing the automa's difficulty.
	 * @param choice : the given String.
	 * @return the enum representing the automa's difficulty.
	 */
 	private Constants readDifficulty(String choice) {
 		return switch(choice) {
 			case("1") -> Constants.INTERN;
 			case("2") -> Constants.APPRENTICE;
 			case("3") -> Constants.FELLOW;
 			case("4") -> Constants.MASTER;
 			case("5") -> Constants.LEGEND;
 			default -> Constants.DEFAULT;
 		};
 	}

 	/**
 	 * Asks and then returns the difficulty of the automa.
 	 * @return automa's difficulty.
 	 */
 	private Constants getDifficulty() {
 		Constants difficulty;
 			do {
 				System.out.println("Enter a number between 1 and 5 to select the difficulty in which you want to play");
 				difficulty = readDifficulty(_scanner.next());
 			} while(difficulty == Constants.DEFAULT);
 		return difficulty;
 	}
	
	/**
	 * The method adds player to the players' array and allows to go to the next
	 * step (building the game)
	 * 
	 * @return IGameBuilder
	 */
	public IGameBuilder addPlayers() {
		
		Constants difficulty;
 		var names = new String[2];
 		_opponents = new IOpponent[2];
 		switch(_chosenVersion) {
 		case PHASE4 -> {
 			getNames(names, 1);
 			difficulty = getDifficulty();
 			_opponents[0] = new Player(names[0], "Blue");
 			_opponents[1] = new Automa(difficulty, 54);
 		  }
 		default -> {
 			getNames(names, 2);
 			_opponents[0] = new Player(names[0], "Blue");
 			_opponents[1] = new Player(names[1], "Red");
 			}
 		}
		return this;
	}
	
	/**
	 * Parses the given String in an enum representing the automa's deck.
	 * @param choice : the given String.
	 * @return the enum representing the automa's deck.
	 */
	private Constants readDeck(String choice) {
 		return switch(choice) {
 			case("n") -> Constants.NORMAL_DECK;
 			case("t") -> Constants.TACTICAL_DECK;
 			default -> Constants.DEFAULT;
 		};
 	}

	/**
 	 * Asks and then returns the deck used by the automa.
 	 * @return automa's deck, DEFAULT if not played in solo.
 	 */
 	private Constants chooseDeck() {
 		Constants chosenDeck;
 		if (_chosenVersion != Constants.PHASE4) {
 			return Constants.DEFAULT;
 		}

 		do {
 			System.out.println("Enter 'n' to play with the normal deck");
 			System.out.println("      't' to play with the tactical deck");
 			chosenDeck = readDeck(_scanner.next());
 		} while(chosenDeck == Constants.DEFAULT);
 		return chosenDeck;
 	}
	
	/**
	 * The method builds a new game.
	 * 
	 * @return Game
	 */
	public Game build() {
		var chosenDeck = chooseDeck();
		var timeBoard = new TimeBoard();
		var pieces = PieceHandler.Handler();
		var players = new OpponentHandler(_opponents, !_chosenVersion.equals("d"));
 		var cards = new CardHandler();
		
 		var boardFile = _chosenVersion == Constants.PHASE1 ? "load_time_board_demo" : "load_time_board";
		var pieceFile = _chosenVersion == Constants.PHASE1 ? "load_phase1" : "load_Normal";
		var cardFile = (chosenDeck == Constants.DEFAULT) ? null : 
			 (chosenDeck == Constants.NORMAL_DECK) ? "load_normal_deck" : "load_tactical_deck" ;
		
		init(boardFile, pieceFile, cardFile, timeBoard, pieces, players, cards, chosenDeck);
		
		return new Game(timeBoard, players, pieces, cards, _chosenVersion); 
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
	private void init(String boardFile, String pieceFile, String cardFile, TimeBoard timeBoard, PieceHandler pieceHandler, 
				OpponentHandler players, CardHandler cards, Constants deck) {
		initTimeBoard(boardFile, timeBoard);
		initPieceHandler(pieceFile, pieceHandler);
		timeBoard.initPlayerPawns(players, 2);
		initCards(cards, cardFile, deck);
	}
	
	/**
	 * Initializes the deck of cards.
	 * @param cards : the deck to initialize.
	 * @param file : path to the file containing the cards.
	 * @param deck : indicates wich deck we use.
	 */
	private void initCards(CardHandler cards, String file, Constants deck) {
 		try {
 			if (file != null) {
 				cards.loadCards(Path.of(file), deck);
 			}
 		} catch (IOException e) {
 			System.err.println(e.getMessage());
 			System.exit(1);
 			_scanner.close();
 			return;
 		}
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
			System.err.println(e.getMessage());
			System.exit(1);
			_scanner.close();
			return;
		}
	}
}
	
