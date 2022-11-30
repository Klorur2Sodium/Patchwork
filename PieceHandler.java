import java.util.ArrayList;
import java.util.Objects;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.Math;

public class PieceHandler {
	private static final PieceHandler _instance = new PieceHandler();
	
    private final ArrayList<Piece> _pieces;
    private int _biggestPiece;
    private int _piecesDisplayed;
    private int _posNeutralPawn;

    private PieceHandler() {
        _pieces = new ArrayList<>();
        _piecesDisplayed = 10;
        _posNeutralPawn = 0;
        _biggestPiece = 0;
    }
    
    public static PieceHandler Handler() { return _instance; }
    
    public int getSize() {return _pieces.size();}

    private int getRealIndex(int index) {
    	return index < _pieces.size()
    		? index
    		: index - _pieces.size();
	}

    public boolean add(Piece p) {
        Objects.requireNonNull(p);
        if (_biggestPiece < p.getYSize()) {
        	_biggestPiece = p.getYSize();
        }
        return _pieces.add(p);
    }

    public void remove(Object p) {
        Objects.requireNonNull(p);
        _pieces.remove(p);
    }

    public void mix() {
    	int changementNumber = 10;
    	for (int i = 0; i < changementNumber; i++) {
    		var index = Math.random() * _pieces.size();
    		var elem = _pieces.get((int)index);
    		_pieces.remove(elem);
    		_pieces.add(elem);
    	}
    }
    
    public void loadPieces(Path path) throws IOException {
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
                var p = new Piece();
                p.parseLine(line);
				add(p);
			}
		}
	}

    public Piece getPiece(int index) {
		if (index >= _pieces.size()) {
			throw new ArrayIndexOutOfBoundsException("Index must be strictly smaller than the size of pieces");
		}
		
		index = getRealIndex(index + _posNeutralPawn);
		return _pieces.get(index);
	}
    
    public void display(boolean captions) {
    	var boardBuilder = new BoardBuilder();
    	boardBuilder.display(captions);
    }
    
    private class BoardBuilder {
    	
    	private String spacesString(int nbSpaces) {
    	        var builder = new StringBuilder();
    			for (int i = 0; i < nbSpaces; i++) {
    				builder.append(' ');
    			}
    	        return builder.toString();
    	}
    	
    	private String displayCaption() {
            var builder = new StringBuilder();
            builder.append("Caption :\n");
            builder.append("  c : cost of the piece\n");
            builder.append("  m : number of moves the player has to do\n");
            builder.append("  b : number of buttons on the piece\n");
    		return builder.toString();
    	}
    	
    	private String displaySelectablePiecesNumber() {
            var builder = new StringBuilder();
    		var numberOfSelectablePiece = 3;

    		for (int i = 0; i < numberOfSelectablePiece && i < _pieces.size(); i++) {
                builder.append("(").append(i + 1).append(")");
                builder.append(spacesString(_pieces.get(i).getXSize()));
    		}
    		builder.append("\n");
            return builder.toString();
    	}
    	
    	private String bodyString() {
    		var builder = new StringBuilder();
    		for (int i = 0; i < _biggestPiece; i++) {
    			for (int j = 0; j < _pieces.size(); j++) {
    				builder.append(_pieces.get(j).bodyLine(i)).append("  ");
        		}
    			builder.append("\n");
    		}
            return builder.toString();
        }
    	
    	private String costString(int index) {
            return "c :" + _pieces.get(index).getCost() + spacesString(_pieces.get(index).getXSize());
        }

        private String movesString(int index) {
            return "m :" + _pieces.get(index).getMoves() + spacesString(_pieces.get(index).getXSize());	
        }

        private String buttonString(int index) {
            return "b :" + _pieces.get(index).getButtons() + spacesString(_pieces.get(index).getXSize());
    	}
        
        private void displayPieces() {
        	var cost = new StringBuilder();
        	var moves = new StringBuilder();
        	var button = new StringBuilder();
        	int index;
        	
        	for (int i = _posNeutralPawn; i < _posNeutralPawn + _piecesDisplayed && i < _pieces.size(); i++) {
        		index = getRealIndex(i);
        		cost.append(costString(index));
        		moves.append(movesString(index));
    			button.append(buttonString(index));
    		}
        	System.out.println(bodyString());
        	System.out.println(cost.toString());
        	System.out.println(moves.toString());
        	System.out.println(button.toString());
        }
    	
    	
    	
    	public void display(boolean captions) {
    		if (captions) {
    			System.out.println(displayCaption());
    		}
    		System.out.println(displaySelectablePiecesNumber());
    		displayPieces();
    		
    	}
    }
}
