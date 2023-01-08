package fr.uge.patchwork;

import java.util.Scanner;


/**
 * Main class
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class Main {
	public static void main(String[] args) {
		var game = GameBuilder.getVersionSelector(new Scanner(System.in))
			.chooseVersion()
			.addPlayers()
			.build();
		
		//game.playingPhase(new Scanner(System.in));
		game.play();
	}
}
