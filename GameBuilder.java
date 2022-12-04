import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Scanner;

public class GameBuilder implements IGameVersionSelector, IGamePlayerSelector, IGameBuilder {
	
	private Player[] _players;
	private String _chosenVersion;
	private final Scanner _scanner;
	
	public static IGameVersionSelector getVersionSelector(Scanner scan) {
		return new GameBuilder(scan);
	}
	
	private GameBuilder(Scanner scan) {
		_scanner = scan;
	}
	
	public String getVersion() {
		return _chosenVersion;
	}
	
	public IGamePlayerSelector chooseVersion() {
		
		do {
			System.out.println("Enter 'd' to play to the demo ascii version and 'a' for the complete ascii version");
			_chosenVersion = _scanner.next();
		} while(!_chosenVersion.equals("d") && !_chosenVersion.equals("a"));
				
		return this;
	}
	
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
	
	public Game build() {
		var timeBoard = new TimeBoard();
		var pieces = PieceHandler.Handler();
		var players = new PlayerHandler(_players, _chosenVersion.equals("a"));
		
		var boardFile = _chosenVersion.equals("a") ? "load_time_board" : "load_time_board_demo";
		var pieceFile = _chosenVersion.equals("a") ? "load_Normal" : "load_phase1";
		
		init(boardFile, pieceFile, timeBoard, pieces, players);
		
		return new Game(timeBoard, players, pieces, _chosenVersion); 
	}
	
	private void init(String boardFile, String pieceFile, TimeBoard timeBoard, PieceHandler pieceHandler, PlayerHandler players) {
		initTimeBoard(boardFile, timeBoard);
		initPieceHandler(pieceFile, pieceHandler);
		timeBoard.initPlayerPawns(players, 2);
	}
	
	private void initTimeBoard(String file, TimeBoard t) {
		try {
			t.loadTimeBoard(Path.of(file));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
			_scanner.close();
			return;
		}
	}

	private void initPieceHandler(String file, PieceHandler p) {
		try {
			p.loadPieces(Path.of(file));
			Collections.shuffle(p.getPieces());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
			_scanner.close();
			return;
		}
	}
}
	
