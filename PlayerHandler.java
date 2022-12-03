import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerHandler {
	private final ArrayList<Player> players = new ArrayList<Player>();
	private Player currentPlayer;
	private int specialTilesRemaining;
	
	public PlayerHandler() {
		specialTilesRemaining = 1;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean specialTileAvailable() {
		return (specialTilesRemaining > 0) ? true : false;
	}
	
	public void initPlayersAscii(Scanner scanner) {
		String[] colors = {"Blue", "Red"};
		var nbPlayers = 2;
		String name;
		
		for (int i = 0; i < nbPlayers; i++) {
			System.out.println("Player " + (i + 1) + " (" + colors[i] + ") please enter your name :");
			name = scanner.next();
			if (name.length() > 30) {
				System.out.println("The name is too long");
				i--;
			} else {
				players.add(new Player(name, colors[i]));
			}
		}
		currentPlayer = players.get(0);
	}
	
	public void updateCurrentPlayer() {
		for (var player : players) {
			if (player.getPosition() < currentPlayer.getPosition()) {
				currentPlayer = player;
			}
		}
	}
	
	public int distanceBetweenPlayers() {
		int distance = 0;
		
		for (var player : players) {
			if (player.getPosition() > currentPlayer.getPosition()) {
				System.out.println("oskur");
				distance = player.getPosition() - currentPlayer.getPosition();
			}
		}
		return distance;
	}
	
	private Player getVictoriousPlayer() {
		var scorePlayer1 = players.get(0).getScore();
		var scorePlayer2 = players.get(1).getScore();
		return (scorePlayer1 > scorePlayer2) ? players.get(0) : players.get(1);
	}
	
	public void displayWinner() {
		var winner = getVictoriousPlayer();
		System.out.println(winner.getName() + " won with " + winner.getScore() + " points");
		
	}
	
}










