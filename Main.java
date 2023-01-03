package fr.uge.patchwork;

import java.util.Scanner;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.ScreenInfo;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

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
		game.draw();
	}
	static class Area {
}
