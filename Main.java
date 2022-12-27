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
	    private Ellipse2D.Float ellipse = new Ellipse2D.Float(0, 0, 0, 0);
	    
	    void draw(ApplicationContext context, float x, float y) {
	      context.renderFrame(graphics -> {
	        // recouvrir le rond d'avant 
	        graphics.setColor(Color.LIGHT_GRAY);
	        graphics.fill(ellipse);
	        
	        // show a new ellipse at the position of the pointer
	        graphics.setColor(Color.MAGENTA);
	        ellipse = new Ellipse2D.Float(x - 20, y - 20, 40, 40);
	        graphics.fill(ellipse);
	      });
	    }
	  }
	  
	  public static void prout(String[] args) {
		  // couleur du fond au demarage
	    Application.run(Color.LIGHT_GRAY, context -> {
	      
	      // get the size of the screen
	      ScreenInfo screenInfo = context.getScreenInfo();
	      float width = screenInfo.getWidth();
	      float height = screenInfo.getHeight();
	      System.out.println("size of the screen (" + width + " x " + height + ")");
	      
	      context.renderFrame(graphics -> {
	    	  // couleur du fond pendant le programme
	        graphics.setColor(Color.LIGHT_GRAY);
	        graphics.fill(new  Rectangle2D.Float(0, 0, width, height));
	      });
	      
	      Area area = new Area();
	      for(;;) {
	        Event event = context.pollOrWaitEvent(10);
	        if (event == null) {  // no event
	          continue;
	        }
	        Action action = event.getAction();
	        if (action == Action.KEY_PRESSED || action == Action.KEY_RELEASED) {
	          System.out.println("abort abort !");
	          context.exit(0);
	          return;
	        }
	        System.out.println(event);
	        Point2D.Float location = event.getLocation();
	        area.draw(context, location.x, location.y);
	      }
	    });
	  }
}
