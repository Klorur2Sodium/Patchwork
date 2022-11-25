
public class Piece {
    private boolean[][] _body; // 1bis
    private int _cost;
    private int _buttons;
    private int _moves;
    private int xSize; 
    private int ySize;

    public int getCost() {return _cost;}
    public int getButtons() {return _buttons;}
    public int getMoves() {return _moves;}
    public int getXSize() {return xSize;}
    public int getYSize() {return ySize;}

    public boolean getBodyValue(int x, int y) {
        return _body[y][x];
    }
    
    private void initPiece(String line) {
    	char[] decomposition = line.toCharArray();
    	int x, y;
    	x = 0;
    	y = 0;
    	for (var elm : decomposition) {
            switch (elm) {
                case '1' -> {_body[x][y] = true; x++;}
                case '0' -> {_body[x][y] = false; x++;}
                case ',' -> {
                	if (xSize != x) {
                		throw new IllegalArgumentException("invalid piece");
                	}
                	y++;
                	x = 0;
                	}
            }
        }
    }
    
    private void parseBody(String line) {
    	var splitLine = line.split(",");
    	ySize = splitLine.length;
    	xSize = splitLine[0].length();
    	_body = new boolean[xSize][ySize];
    	initPiece(line);
    }

    public void parseLine(String line) {
		var splitLine = line.split(":");

	    parseBody(splitLine[0]);
		_cost = Integer.parseInt(splitLine[1]);
		_moves = Integer.parseInt(splitLine[2]);
		_buttons = Integer.parseInt(splitLine[3]);
	}

    public boolean fitArea(int x, int y, int size) {
        if (xSize + x >= size) {
            return false;
        }
        if (ySize + y >= size) {
            return false;
        }
        return true;
    }
    
    public Piece flip() {
    	var temp = new Piece();
    	temp._cost = _cost;
    	temp._moves = _moves;
    	temp._buttons = _buttons;
    	temp.xSize = ySize;
    	temp.ySize = xSize;
    	temp._body = new boolean[ySize][xSize];
    	for (int i = xSize-1; i >= 0; i--) {
    		for (int j = 0; j < ySize; j++) {
    			temp._body[j][i] = _body[xSize - 1 - i][j];
    		}
    	}
    	return temp;
    }
    
    public String bodyString() {
    	var builder = new StringBuilder();
        
        for (int i = 0; i < ySize; i++) {
        	for (int j = 0; j < xSize; j++) {
        		builder.append(_body[j][i] ? "x" : " ");
        	}
        	builder.append("\n");
        }
        return builder.toString();
    }
    
    public String bodyLine(int x) {
    	var builder = new StringBuilder();
    	if (x >= xSize) {
    		for (int i = 0; i < xSize; i++) {
    			builder.append(" ");
    		}
    		return builder.toString() + "  ";
    	}
    	
    	for (int i = 0; i < xSize; i++) {
    		builder.append(_body[i][x] ? "x" : " ");
    	}
    	return builder.toString() + "  ";
    }
}
