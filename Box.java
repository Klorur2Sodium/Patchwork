import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Box {
	private final char _status;
	private ArrayList<Player> _players = new ArrayList<>();

	public Box(char status) {
		Objects.requireNonNull(status);

		switch (status) {
		case '|' -> _status = status;
		case '0' -> _status = status;
		case 'x' -> _status = status;
		default -> throw new IllegalArgumentException("status must be equal to |, 0 or x");
		}
	}
	
	public List<Player> getPlayers() {
		return _players;
	}

	public void add(Player player) {
		Objects.requireNonNull(player);
		_players.add(player);
	}

	public void remove(Player player) {
		Objects.requireNonNull(player);
		_players.remove(player);
	}

	public Player getPlayer() {
		int playersSize = _players.size();

		if (playersSize > 0) {
			return _players.get(playersSize - 1);
		}
		return null;
	}

	public void boxEvent(Player player) {
		Objects.requireNonNull(player);

		switch (_status) {
		case '0':
			player.payEvent();
			break;
		case 'x':
			var patch = new Piece();
			patch.parseLine("1:0:0:0");
			System.out.println(patch);
			player.placingPhase(patch, new Scanner(System.in));
			break;
		case '|':
			return;
		}
	}

	@Override
	public String toString() {
		return "" + _status;
	}
}
