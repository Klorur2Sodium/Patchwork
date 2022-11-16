import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public record Box(char status, ArrayList<Player> players) {
	public Box {
		Objects.requireNonNull(status);
		Objects.requireNonNull(players);

		if (status != '|' && status != '0' && status != 'x') {
			throw new IllegalArgumentException("status must be equal to |, 0 or x");
		}
	}

	public Box(char status) {
		this(status, new ArrayList<Player>());
	}

	public void add(Player player) {
		Objects.requireNonNull(player);
		players.add(player);
	}

	public void remove(Player player) {
		Objects.requireNonNull(player);
		players.remove(player);
	}
	
	public Player getPlayer() {
		int playersSize = players.size();
		
		if (playersSize > 0) {
			return players.get(playersSize - 1);
		}
		return null;
	}

	public void boxEvent(Player player) {
		Objects.requireNonNull(player);
		
		switch (status) {
		case '0':
			player.displayPayEvent();
			break;
		case 'x':
			var patch = new Piece(new int[][] {{1}}, 0, 0, 0);
			patch.displayPiece();
			player.placingPhase(patch, new Scanner(System.in));
			break;
		case '|':
			return;
		}
	}

}
