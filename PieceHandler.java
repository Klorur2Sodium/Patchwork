import java.util.ArrayList;
import java.util.Objects;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PieceHandler {
	private static final PieceHandler _instance = new PieceHandler();
	
    private final ArrayList<Piece> _pieces;
    private int _piecesDisplayed;
    private int _posNeutralPawn;

    private PieceHandler() {
        _pieces = new ArrayList<>();
        _piecesDisplayed = 10;
        _posNeutralPawn = 0;
    }
    
    public static PieceHandler Handler() { return _instance; }
    
    public int getSize() {return _pieces.size();}

    private int getRealIndex(int index) {
    	return index < _pieces.size()
    		? index
    		: index - _pieces.size();
    	
//		if (index >= _pieces.size()) {
//			return index - _pieces.size();
//		}
//		return index;
	}

    public boolean add(Piece p) {
        Objects.requireNonNull(p);
        return _pieces.add(p);
    }

    public void remove(Object p) {
        Objects.requireNonNull(p);
        _pieces.remove(p);
    }

    public void loadPieces(Path path) throws IOException {
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
                var p = new Piece();
                p.parseLine(line);
				_pieces.add(p);
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
    
    public void display() {
    	var boardBuilder = new BoardBuilder();
    	boardBuilder.display();
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
                builder.append("(").append(1 + 1).append(")");
                builder.append(spacesString(_pieces.get(i).getXSize()));
    		}
    		builder.append("\n");
            return builder.toString();
    	}
    	
    	private String bodyString(int index) {
            return _pieces.get(index).bodyString() + "   ";
        }
    	
    	private String costString(int index) {
            return "c :" + _pieces.get(index).getCost() + spacesString(_pieces.get(index).getXSize());
        }

        private String movesString(int index) {
            return "m :" + _pieces.get(index).getMoves() + spacesString(_pieces.get(index).getXSize());	
        }

        private String buttonString(int index) {
            return "b:" + _pieces.get(index).getButtons() + spacesString(_pieces.get(index).getXSize());
    	}
        
        private void displayPieces() {
        	var body = new StringBuilder();
        	var cost = new StringBuilder();
        	var moves = new StringBuilder();
        	var button = new StringBuilder();
        	int index;
        	
        	for (int i = _posNeutralPawn; i < _posNeutralPawn + _piecesDisplayed && i < _pieces.size(); i++) {
        		index = getRealIndex(i);
        		body.append(bodyString(index));
        		cost.append(costString(index));
        		moves.append(movesString(index));
    			button.append(buttonString(index));
    		}
        	System.out.println(body.toString());
        	System.out.println(cost.toString());
        	System.out.println(moves.toString());
        	System.out.println(button.toString());
        }
    	
    	
    	
    	public void display() {
    		System.out.println(displayCaption());
    		System.out.println(displaySelectablePiecesNumber());
    		displayPieces();
    		
    	}
    }
}
