package fr.uge.patchwork;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Interface that permits the player's class and the automa's class.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public sealed interface IOpponent permits Player, Automa {
	// Getter or "just return a value" type of methods
	
	/**
	 * Getter for the pawn of the opponent
	 * @return pawn of the opponent
	 */
	public abstract Pawn getPawn();
	
	/**
	 * Getter for the position of the opponent
	 * @return pawn of the opponent
	 */
	public abstract int getPosition();
	
	/**
	 * Getter for the score of the opponent
	 * @return pawn of the opponent
	 */
	public abstract int getScore();
	
	/**
	 * Getter for the name of the opponent
	 * @return pawn of the opponent
	 */
	public abstract String getName();
	
	/**
	 * Returns the number of buttons the player owns
	 * @return the buttonsCount
	 */
	public abstract int getButton();
	
	// Setter
	
	/**
	 * The function sets the player position to another position
	 * @param position ! new position.
	 */
	public void setPosition(int position);
	
	/**
	 * The function sets the quiltBoard
	 * @param board : 
	 */
	public void setQuiltBoard(QuiltBoard board);
	
	/**
	 * The function adds or retrieves nbButton to the buttonCount of the player
	 * @param nbButton : number of buttons to add/ retrieve
	 * @param add : indicates if we had or retrieve buttons
	 */
	public void setButtons(int nbButton, boolean add);

	// Main methods of a turn
	
	/**
	 * The function buys a piece and and moves the opponent
	 * @param piece the piece recovered
	 * @param _timeBoard the game's board
	 * @return a null or a patch waiting to be placed 
	 */
	public abstract Piece recoverPiece(Piece piece, TimeBoard _timeBoard);
	
	/**
	 * Checks whether or not the opponent has the money to purchase a piece
	 * 
	 * @param piece : the piece you want to buy
	 * @return boolean
	 */
	public abstract boolean canBuyPiece(Piece piece);
	
	/**
	 * Sets the filed _specialTile to True. 
	 */
	public abstract void addSpecialTile();
	
	/**
	 * Handles the action skip of the player during buying phase.
	 * 
	 * @param nbMoves : number of moves
	 * @param timeBoard : the time board
	 */
	public abstract Piece skipTurn(int nbMoves, TimeBoard timeBoard);
	
	/**
	 * The method adds the special tile to the opponent if can have it
	 * @return boolean
	 */
	public abstract boolean updateSpeTile();
	
	
	// Graphic methods
	
	/**
	 * Initializes a GraphicalObject in a rectangle.
	 * @param topLeftX : x coordinates of the GraphicalObject's top left corner.
	 * @param topLeftY : y coordinates of the GraphicalObject's top left corner.
	 * @param width : width of the GraphicalObject.
	 * @param height : height of the GraphicalObject.
	 */
	public abstract void SetGraphicalProperties(float topLeftX, float topLeftY, float width, float height);
	
	/**
	 * Draw a GraphicalObject by calling its onDraw() function.
	 * @param  graphics : object that calls the graphic methods.
	 */
	public abstract void draw(Graphics2D graphics);
	 
	// Functions implemented by player and not by automa
	
	/**
	 * Getter for the quilt board
	 * @return the quilt board
	 */
	public default QuiltBoard getQuiltboard() {
		return new QuiltBoard();
	}
	
	/**
	 * Handles all the required changes needed when the player buys a piece
	 * like its money, position, quiltboard.
	 * 
	 * @param piece : piece you want to buy
	 * @param scanner : scanner for the placing phase
	 * @param _timeBoard : the timeboard
	 * @param _chosenVersion : given version of the game
	 */
	public default void buyPiece(Piece piece, Scanner scanner, TimeBoard _timeBoard, Constants _chosenVersion) {
		return;
	}
	
	/**
	 * Add the given piece p to the quiltboard of this player
	 * 
	 * @param piece : the piece you want to add
	 * @param x : x coordinate
	 * @param y : y coordinate
	 * @return success of the action
	 */
	public default boolean addPieceToGrid(Piece piece, int y, int x) {
		return false;
	}
	
	/**
	 * Handles the action skip of the player during buying phase.
	 * 
	 * @param scanner : here in case you land on a box with a patch in it
	 * @param nbMoves : number of moves
	 * @param timeBoard : the timeboard
	 * @param version : given version of the game
	 */
	public default void skipTurn(Scanner scanner, int nbMoves, TimeBoard timeBoard, Constants version) {
		return;
	}
	
	/**
	 * The function returns the letter corresponding to the pawn's color
	 * @return a letter
	 */
	public default String display() {
		return "";
	}

	/**
	 * Handles the buying phase
	 * 
	 * @param scanner : a scanner
	 * @param _pieceHandler : the pieceHandler containing all the pieces
	 * @return choice of the player
	 */
	public default Constants buyingPhase(Scanner scanner, PieceHandler _pieceHandler) {
		return null;
	}
	
	//Functions implemented by automa and not by player
	
	/**
	 * Handles the buying phase of the automa.
	 * @param pieces : list of selectable pieces.
	 * @param playerPos : position of the player on the time board.
	 * @param automaPos : position of the automa on the time board.
	 * @param currentCard : Card that the automa is using.
	 * @param timeBoard : TimeBoard object representing the time board.
	 * @return piece
	 */
	public default Piece buyingPhase(ArrayList<Piece> pieces, int playerPos, int automaPos, Card currentCard, TimeBoard timeBoard) {
		return null;
	}
}