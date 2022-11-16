import java.util.Objects;

public class Pawn {
	/* les pions des joueurs et le pions blanc */
	private final String _color;
	private int _pos;

	public Pawn(String color) {
		Objects.requireNonNull(color, "Color missing");
		
		_color = color;
		_pos = 0;
	}

	public int pos() {
		return _pos;
	}
	
	public void move() {
		_pos++;
	}
	
	public String asciiPawn() {
		return _color.substring(0, 1);
	}

	@Override
	public String toString() {
		return "color : " + _color + ", position :" + _pos;
	}

}