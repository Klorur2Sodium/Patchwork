import java.util.Objects;

public class Pawn {
    private final String _color;

    public Pawn(String color) {
        Objects.requireNonNull(color);
        _color = color;
    }

    @Override
    public String toString() {
        return _color.substring(0, 1);
    }
}
