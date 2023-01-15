package fr.uge.patchwork;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Objects;

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
	
	public Automa(Constants difficulty, int sizeTimeBoard) {
		Objects.requireNonNull(difficulty);
		_buttonsCount = 0;
		_name = "Automate";
		_turnBudget = 0;
		_wage = 0;
		_pawn = new Pawn("DarkGrey");
		_position = 0;
		_specialTile = false;
		_difficulty = difficulty;
		_speTilePos = sizeTimeBoard - difficulty.getValue();
		_piecesWithButtons = new ArrayList<Piece>();
	}
	
	public int getPosition() {
		return _position;
	}
	
	public Pawn getPawn() {
		return _pawn;
	}
	
	public String getName() {
		return _name;
	}
	
	/**
	 * returns the number of buttons the player owns
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
	
	private int getNbButtonsPieces() {
		var nb_buttons = 0;
		for (int i = 0; i < _piecesWithButtons.size(); i++) {
			nb_buttons += _piecesWithButtons.get(i).getButtons();
		}
		return nb_buttons;
	}
	
	private int internScore() {
		return (_specialTile) ? 0 : 7;
	}
	
	private int apprenticeScore() {
		return _buttonsCount + ((_specialTile) ? 0 : 7);
	}
	
	private int fellowScore() {
		return _buttonsCount + _piecesWithButtons.size() + ((_specialTile) ? 0 : 7);
	}
	
	private int masterScore() {
		return _buttonsCount + getNbButtonsPieces() + ((_specialTile) ? 0 : 7);
	}
	
	private int legendScore() {
		return _buttonsCount + _piecesWithButtons.size() + getNbButtonsPieces() + ((_specialTile) ? 0 : 7);
	}
	
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
	
	public void addSpecialTile() {
		_specialTile = true;
	}

	protected void onDraw(Graphics2D graphics) {
		
	}
	
	/**
	 * Pays the player with its wage.
	 */
	public void payEvent() {
		earnButtons(_wage);
	}
	
	/**
	 * the function return a patch if the player is walking on one
	 * @param currentBox
	 * @return
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

	public Piece skipTurn(int nbMoves, TimeBoard timeBoard) {
		Objects.requireNonNull(timeBoard);
		if (nbMoves < 0) {
			throw new IllegalArgumentException("the player can't move back");
		}

		earnButtons(nbMoves);
		return move(nbMoves, timeBoard);
	}
	
	private void updateStatsFromCard(Card card) {
		_wage = card.wage();
		_turnBudget = card.turnBudget();
	}
	
	private ArrayList<Piece> buyablePiece(ArrayList<Piece> pieces) {
		var lst = new ArrayList<Piece>();
		for (int i = 0; i < 3; i++) {
			if (_turnBudget >= pieces.get(i).getCost()) {
				lst.add(pieces.get(i));
			}
		}
		return lst;
	}
	
	public Piece recoverPiece(Piece piece, TimeBoard timeBoard) {
		if (piece.getButtons() < 1) {
			return null;
		}
		_piecesWithButtons.add(piece);
		return move(piece.getMoves(), timeBoard);
	}
	
	public Piece buyingPhase(ArrayList<Piece> pieces, int playerPos, int automaPos, Card currentCard, TimeBoard timeBoard) {
		Objects.requireNonNull(pieces);
		Objects.requireNonNull(currentCard);
		Objects.requireNonNull(timeBoard);
		
		Piece piece = null;
		updateStatsFromCard(currentCard);
		var lst = buyablePiece(pieces);
		
		switch(lst.size()) {
		case 0 -> {
			skipTurn(playerPos - automaPos + 1, timeBoard); 
		}
		case 1 -> {
			piece = lst.get(0);
			recoverPiece(piece, timeBoard);
		}
		default -> {
			piece = currentCard.applyFilters(lst, automaPos, playerPos);
			recoverPiece(piece, timeBoard);
		}
		}
		return piece;
	}

	public boolean updateSpeTile() {
		if (_position >= _speTilePos) {
			addSpecialTile();
			return true;
		}
		return false;
	}
	
//	public int automaChoice(ArrayList<Piece> pieces, Piece piece) {
//		for (int i = 0; i < pieces.size(); i++) {
//			if (pieces)
//		}
//	}
}