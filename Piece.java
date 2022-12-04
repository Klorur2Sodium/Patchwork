
public class Piece {
	private boolean[][] _body; // 1bis
	private byte _cost; //-128 + 127
	private byte _buttons;
	private byte _moves;
	private byte xSize;
	private byte ySize;

	public int getCost() {
		return _cost;
	}

	public int getButtons() {
		return _buttons;
	}

	public int getMoves() {
		return _moves;
	}

	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public boolean getBodyValue(int x, int y) {
		return _body[y][x];
	}
	
	public void parseLine(String line) {
		var splitLine = line.split(":");

		parseBody(splitLine[0]);
		_cost = (byte) Integer.parseInt(splitLine[1]);
		_moves = (byte) Integer.parseInt(splitLine[2]);
		_buttons = (byte) Integer.parseInt(splitLine[3]);
	}

	public boolean fitArea(int x, int y, int size) {
		if (xSize + x > size) {
			return false;
		}
		if (ySize + y > size) {
			return false;
		}
		return true;
	}

	public Piece flip() {
		// turns the piece to the left
		var temp = newPiece(ySize, xSize);
		for (int i = xSize - 1; i >= 0; i--) {
			for (int j = 0; j < ySize; j++) {
				temp._body[j][i] = _body[xSize - 1 - i][j];
			}
		}
		return temp;
	}

	public Piece reverse() {
		// left becomes right right becomes left
		var temp = newPiece(xSize, ySize);
		for (int i = 0; i < ySize; i++) {
			for (int j = xSize - 1; j >= 0; j--) {
				temp._body[xSize - j - 1][i] = _body[j][i];
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

	public String spacesCaption(String type) {
		var builder = new StringBuilder();
		int lenCaption = (_cost < 10)? 5 : 6;
		if (!type.equals("cost") && lenCaption == 6) {
			builder.append(" ");
		}
		if (xSize > lenCaption) {
			for (int i = 0; i <= xSize - lenCaption; i++) {
				builder.append(" ");
			}
		}
		return builder.toString() + "  ";
	}
	
	private void parseBody(String line) {
		var splitLine = line.split(",");
		ySize = (byte) splitLine.length;
		xSize = (byte) splitLine[0].length();
		_body = new boolean[xSize][ySize];
		initPiece(line);
	}

	private void initPiece(String line) {
		char[] decomposition = line.toCharArray();
		int x = 0, y = 0;
		for (var elm : decomposition) {
			switch (elm) {
				case '1' -> {
					_body[x][y] = true;
					x++;
				}
				case '0' -> {
					_body[x][y] = false;
					x++;
				}
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

	private Piece newPiece(int x, int y) {
		var temp = new Piece();
		temp._cost = _cost;
		temp._moves = _moves;
		temp._buttons = _buttons;
		temp.xSize = (byte) x;
		temp.ySize = (byte) y;
		temp._body = new boolean[x][y];
		return temp;
	}

	private String spacesBody() {
		var space = new StringBuilder();
		int lenCaption;
		lenCaption = (_cost < 10)? 5 : 6;
		if (xSize < lenCaption) {
			for (int i = 0; i < lenCaption - xSize; i++) {
				space.append(" ");
			}
		}
		return space.toString() + "  ";
	}
	
	public String bodyLine(int x) {
		var builder = new StringBuilder();
		if (x >= ySize) {
			for (int i = 0; i < xSize; i++) {
				builder.append(" ");
			}
		} else {
			for (int i = 0; i < xSize; i++) {
				builder.append(_body[i][x] ? "x" : " ");
			}
		}

		return builder.toString() + spacesBody();
	}
}
