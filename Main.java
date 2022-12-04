import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		// La classe main est techique point de demarage
		
		var game = GameBuilder.getVersionSelector(new Scanner(System.in))
			.chooseVersion()
			.addPlayers()
			.build();
		
		game.playingPhase(new Scanner(System.in));
	}
}
