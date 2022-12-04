import java.util.Objects;

public class PlayerHandler {
	private final Player[] _players;
	private int _current;
	private boolean _specialTilesRemaining;
	
	public PlayerHandler(Player[] players, boolean special) {
		Objects.requireNonNull(players);
		_players = players;
		_specialTilesRemaining = special;
		_current = 0;
	}
	
	public Player getPlayerIndex(int i) {
		if (i < 0 || i > 1) {
			throw new IllegalArgumentException("there is only two players");
		}
		return _players[i];
	}
	
	public Player getCurrent() {
		return _players[_current];
	}
	
	public boolean specialTileRemaining() {
		return _specialTilesRemaining;
	}
	
	public void updateSpecialTile() {
		if (specialTileRemaining()) {
			if (_players[_current].getQuiltboard().checkSpecialTile()) {
				_players[_current].addSpecialTile();
				_specialTilesRemaining = false;
			}
		}
	}
	
	public void updateCurrentPlayer() {
        if (_players[0].getPosition() > _players[1].getPosition()) {
            _current =  1;
        } else if (_players[0].getPosition() < _players[1].getPosition()) {
            _current =  0;
        }
    }
	
	public int distanceBetweenPlayers() {
		return Math.abs(_players[0].getPosition() - _players[1].getPosition());
	}
	
	public boolean checkEndOfGame(int boardSize) {
		return _players[0].getPosition() == _players[1].getPosition() && _players[0].getPosition() == (boardSize - 1);
	}
	
	private Player getVictoriousPlayer() {
		return (_players[0].getScore() > _players[1].getScore()) ? _players[0] : _players[1];
	}
	
	public void displayWinner() {
		var winner = getVictoriousPlayer();
		System.out.println(winner.getName() + " won with " + winner.getScore() + " points");
	}
	
}










