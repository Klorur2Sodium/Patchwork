package fr.uge.patchwork;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Scanner;

public sealed interface IOpponent permits Player, Automa {
	//Getter or "just return a value" type of methods
	public abstract Pawn getPawn();
	public abstract int getPosition();
	public abstract int getScore();
	public abstract String getName();
	public int getButton();
	
	// Setter
	public void setPosition(int position);
	public void setQuiltBoard(QuiltBoard board);
	public void setButtons(int nbButton, boolean add);

	//main methods of a turn
	public abstract Piece recoverPiece(Piece piece, TimeBoard _timeBoard);
	public abstract boolean canBuyPiece(Piece piece);
	public abstract void addSpecialTile();
	public abstract Piece skipTurn(int nbMoves, TimeBoard timeBoard);
	public abstract boolean updateSpeTile();
	
	
	//graphic methods
	public abstract void SetGraphicalProperties(float topLeftX, float topLeftY, float width, float height);
	public abstract void draw(Graphics2D graphics);
	 
	//Functions implemented by player and not by automa
	public default QuiltBoard getQuiltboard() {
		return new QuiltBoard();
	}
	
	public default void buyPiece(Piece piece, Scanner scanner, TimeBoard _timeBoard, Constants _chosenVersion) {
		return;
	}
	
	public default boolean addPieceToGrid(Piece piece, int y, int x) {
		return false;
	}
	
	public default void skipTurn(Scanner scanner, int nbMoves, TimeBoard timeBoard, Constants version) {
		return;
	}
	
	public default String display() {
		return "";
	}

	public default Constants buyingPhase(Scanner scanner, PieceHandler _pieceHandler) {
		return null;
	}
	
	//Functions implemented by automa and not by player
	public default Piece buyingPhase(ArrayList<Piece> pieces, int playerPos, int automaPos, Card currentCard, TimeBoard timeBoard) {
		return null;
	}
	
	public default int automaChoice(ArrayList<Piece> pieces, Piece piece) {
		return -1;
	}
}