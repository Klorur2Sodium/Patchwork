package fr.uge.patchwork;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class stores the information about the Automa. It also handles 
 * all its action during the playing phase. Like moving, getting a piece etc...
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public final class Automa extends GraphicalObject implements IOpponent {
	private int _buttonsCount;
	private int _turnBudget;
	private final String _name;
	private int _wage;
	private Pawn _pawn;
	private int _position;
	private boolean _specialTile;
	private int _speTilePos;
	private Constants _difficulty;
	private ArrayList<Piece> _piecesWithButtons;
	
	/**
	 * Constructs a new automa using its difficulty and the size of the time board.
	 * @param difficulty : difficulty of the automa.
	 * @param sizeTimeBoard : number of tile in the time board.
	 */
	public Automa(Constants difficulty, int sizeTimeBoard) {
		Objects.requireNonNull(difficulty);
		if (sizeTimeBoard < 0) {
			throw new IllegalArgumentException("invalid position");
		}
		_buttonsCount = 0;
		_name = "Automa";
		_turnBudget = 0;
		_wage = 0;
		_pawn = new Pawn("DarkGrey");
		_position = 0;
		_specialTile = false;
		_difficulty = difficulty;
		_speTilePos = sizeTimeBoard - difficulty.getValue();
		_piecesWithButtons = new ArrayList<Piece>();
	}
	
	/**
	 * Getter for the position.
	 * @return the position.
	 */
	public int getPosition() {
		return _position;
	}
	
	/**
	 * Getter for the pawn.
	 * @return the pawn.
	 */
	public Pawn getPawn() {
		return _pawn;
	}
	
	/**
	 * Getter for the name.
	 * @return the name.
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Returns the number of buttons the player owns
	 * @return the buttonsCount
	 */
	public int getButton() {
		return _buttonsCount;
	}
	
	/**
	 * The function sets the player position to another position
	 * @param position
	 */
	public void setPosition(int position) {
		if (position < 0) {
			throw new IllegalArgumentException("invalid position");
		}
		_position = position;
	}
	
	/**
	 * The function sets the quiltBoard
	 * @param board
	 */
	public void setQuiltBoard(QuiltBoard board) {
		Objects.requireNonNull(board);
		// faire quelque chose
	}
	
	/**
	 * The function add or retrieve nbButton to the buttonCount of the player
	 * @param nbButton
	 * @param add
	 */
	public void setButtons(int nbButton, boolean add) {
		if (add) {
			_buttonsCount += nbButton;
		} else {
			_buttonsCount -= nbButton;
		}
	}
	
	/**
	 * Add the given positive number to the number of buttons of the
	 * player.
	 *  
	 * @param nbButtons : the number that will be add to the number of buttons
	 */
	public void earnButtons(int nbButtons) {
		if (nbButtons < 0) {
			throw new IllegalArgumentException("The player must gain a positive amount of buttons");
		}
		_buttonsCount += nbButtons;
	}
	
	/**
	 * Returns the total number of buttons on all the pieces that the automa acquired.
	 * @return total number of buttons on all the pieces that the automa acquired.
	 */
	private int getNbButtonsPieces() {
		var nb_buttons = 0;
		for (int i = 0; i < _piecesWithButtons.size(); i++) {
			nb_buttons += _piecesWithButtons.get(i).getButtons();
		}
		return nb_buttons;
	}
	
	/**
	 * Returns the automa' score for the intern difficulty.
	 * @return the score.
	 */
	private int internScore() {
		return (_specialTile) ? 7 : 0;
	}
	
	/**
	 * Returns the automa' score for the apprentice difficulty.
	 * @return the score.
	 */
	private int apprenticeScore() {
		return _buttonsCount + ((_specialTile) ? 7 : 0);
	}
	
	/**
	 * Returns the automa' score for the fellow difficulty.
	 * @return the score.
	 */
	private int fellowScore() {
		return _buttonsCount + _piecesWithButtons.size() + ((_specialTile) ? 7 : 0);
	}
	
	/**
	 * Returns the automa' score for the master difficulty.
	 * @return the score.
	 */
	private int masterScore() {
		return _buttonsCount + getNbButtonsPieces() + ((_specialTile) ? 7 : 0);
	}
	
	/**
	 * Returns the automa' score for the legend difficulty.
	 * @return the score.
	 */
	private int legendScore() {
		return _buttonsCount + _piecesWithButtons.size() + getNbButtonsPieces() + ((_specialTile) ? 7 : 0);
	}
	
	/**
	 * Returns the automa' score.
	 * @return the score.
	 */
	public int getScore() {
		return switch(_difficulty) {
		case INTERN -> internScore();
		case APPRENTICE -> apprenticeScore();
		case FELLOW -> fellowScore();
		case MASTER -> masterScore();
		case LEGEND -> legendScore();
		default -> -1;
		};
	}
	
	/**
	 * Adds a special tile to the automa by setting _specialTile to true.
	 */
	public void addSpecialTile() {
		_specialTile = true;
	}
	
	/**
	 * Pays the player with its wage.
	 */
	public void payEvent() {
		earnButtons(_wage);
	}
	
	/**
	 * the function return a patch if the player is walking on one
	 * @param currentBox : box of the time board
	 * @return piece
	 */
	private Piece updatePatch(Box currentBox) {
		switch (currentBox.getStatus()) {
		case BUTTON:
			payEvent();
			return null;
		case PATCH:
			var patch = new Piece();
			patch.parseLine("1:0:0:0");
			currentBox.emptyStatus();
			return patch;
		default:
			return null;
		}
	}
	
	/**
	 * The function moves the player
	 * @param nbMoves indicate the number of moves
	 * @param timeBoard 
	 * @return a patch if the player walked in one
	 */
	private Piece move(int nbMoves, TimeBoard timeBoard) {
		Box currentBox;
		timeBoard.getBoard().get(_position).remove(this);
		for (int i = 0; i < nbMoves && _position < timeBoard.getBoard().size() - 1; i++) {
			_position++;
			currentBox = timeBoard.getBoard().get(_position);
			updatePatch(currentBox);
		}
		timeBoard.getBoard().get(_position).add(this);
		return null;
	}

	/**
	 * Checks whether or not the player has the money to purchase a piece
	 * 
	 * @param piece : the you want to buy
	 * @return boolean
	 */
	public boolean canBuyPiece(Piece piece) {
		Objects.requireNonNull(piece);
		return _turnBudget >= piece.getCost();
	}

	/**
	 * Handles the skip turn action of the automa.
	 * @param nbMoves : number of moves the automa has to do.
	 * @param timeBoard : TimeBoard object representing the time board
	 * @return a piece
	 */
	public Piece skipTurn(int nbMoves, TimeBoard timeBoard) {
		Objects.requireNonNull(timeBoard);
		if (nbMoves < 0) {
			throw new IllegalArgumentException("the player can't move back");
		}

		earnButtons(nbMoves);
		return move(nbMoves, timeBoard);
	}
	
	/**
	 * Updates the budget and wage of the player by using the given card.
	 * @param card : Card containing the stats to update the automa.
	 */
	private void updateStatsFromCard(Card card) {
		_wage = card.wage();
		_turnBudget = card.turnBudget();
	}
	
	/**
	 * Returns a list of pieces that contains all the pieces that the automa can buy considering
	 * its current budget.
	 * @param pieces : all the selectable pieces.
	 * @return all the buyable pieces.
	 */
	private ArrayList<Piece> buyablePiece(ArrayList<Piece> pieces) {
		var lst = new ArrayList<Piece>();
		for (int i = 0; i < 3; i++) {
			if (_turnBudget >= pieces.get(i).getCost()) {
				lst.add(pieces.get(i));
			}
		}
		return lst;
	}
	
	/**
	 * Gives a piece to the automa and moves it.
	 * @param piece : gained piece.
	 * @param timeBoard : TimeBoard object representing the time board.
	 */
	public Piece recoverPiece(Piece piece, TimeBoard timeBoard) {
		Objects.requireNonNull(piece);
		Objects.requireNonNull(timeBoard);
		move(piece.getMoves(), timeBoard);
		if (piece.getButtons() > 0) {
			_piecesWithButtons.add(piece);
		}
		return null;
	}
	
	/**
	 * Handles the buying phase of the automa.
	 * @param pieces : list of selectable pieces.
	 * @param playerPos : position of the player on the time board.
	 * @param automaPos : position of the automa on the time board.
	 * @param currentCard : Card that the automa is using.
	 * @param timeBoard : TimeBoard object representing the time board.
	 * @return piece
	 */
	public Piece buyingPhase(ArrayList<Piece> pieces, int playerPos, int automaPos, Card currentCard, TimeBoard timeBoard) {
		Objects.requireNonNull(pieces);
		Objects.requireNonNull(currentCard);
		Objects.requireNonNull(timeBoard);
		if (playerPos < 0 || automaPos < 0) {
			throw new IllegalArgumentException("invalid position");
		}
		
		Piece piece = null;
		updateStatsFromCard(currentCard);
		var lst = buyablePiece(pieces);
		
		switch(lst.size()) {
		case 0 -> skipTurn(playerPos - automaPos + 1, timeBoard); 
		case 1 -> {piece = lst.get(0);
							 recoverPiece(piece, timeBoard);
			}
		default -> {piece = currentCard.applyFilters(lst, automaPos, playerPos);
			          recoverPiece(piece, timeBoard);
			}
		}
		return piece;
	}

	/**
	 * Updates the special tile of the automa by checking if it passed a certain point.
	 * This point changes according to the difficulty of the automa.
	 * @return true if yes, false otherwaise.
	 */
	public boolean updateSpeTile() {
		if (_position >= _speTilePos) {
			addSpecialTile();
			return true;
		}
		return false;
	}
	
	/**
	 * Draws the stats of the Automaon the window.
	 * @param graphics : object that calls the graphic methods.
	 */
	protected void onDraw(Graphics2D graphics) {
		var y = topLeftY;
		var yPadding = 20;
		graphics.drawString("Automa's stats : " , topLeftX, y);
		y += yPadding;
		graphics.drawString("Current Score : " + getScore() , topLeftX, y);
		y += yPadding;
		graphics.drawString("Special Tile : " + ((!_specialTile) ? "Don't have it" : "Got it") , topLeftX, y);
	}
	
}