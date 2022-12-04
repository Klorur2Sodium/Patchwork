import java.util.Objects;
import java.util.Scanner;

public class Game {
	private final TimeBoard _timeBoard;
	private final PlayerHandler _playerHandler;
	private final PieceHandler _pieceHandler;
	private final String _chosenVersion;
	
	public Game(TimeBoard timeBoard, PlayerHandler playerHandler, PieceHandler pieceHandler, String version) {
		Objects.requireNonNull(timeBoard);
		Objects.requireNonNull(playerHandler);
		Objects.requireNonNull(pieceHandler);
		Objects.requireNonNull(version);
		
		_timeBoard = timeBoard;
		_playerHandler = playerHandler;
		_pieceHandler = pieceHandler;
		_chosenVersion = version;
	}
		
	public void playingPhase(Scanner scanner) {		
		
		displayGame();
		
		int playerChoice;
		while (!_playerHandler.checkEndOfGame(_timeBoard.getSize())) {
			_playerHandler.updateCurrentPlayer();
			playerChoice = _playerHandler.getCurrent().buyingPhase(scanner, _pieceHandler);

			if (playerChoice == 0) {
				_playerHandler.getCurrent().skipTurn(scanner, _playerHandler.distanceBetweenPlayers() + 1, _timeBoard, _chosenVersion);
			} else {
				_playerHandler.getCurrent().buyPiece(_pieceHandler.getPiece(playerChoice - 1), scanner, _timeBoard, _chosenVersion);
				_pieceHandler.remove(_pieceHandler.getPiece(playerChoice - 1));
				_pieceHandler.moveNeutralPawn(playerChoice - 1);
				if (_chosenVersion.equals("a")) {
					_playerHandler.updateSpecialTile();
				}	
			}
			_timeBoard.demarcateTurns();
			_timeBoard.displayTimeBoard(false);
			_pieceHandler.display(false);
		}
		
		_playerHandler.displayWinner();
	}
	
	private void displayGame() {
		_timeBoard.displayTimeBoard(true);
		_pieceHandler.display(true);
		System.out.println("\nlet's go !!\n");
	}
}
