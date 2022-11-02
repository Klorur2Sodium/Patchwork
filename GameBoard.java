import java.util.ArrayList;
import java.util.Objects;

public class GameBoard {
    private final ArrayList<Box> board = new ArrayList<Box>();
    private final int size = 9*9;

    private boolean isSpecialValue(int i) {
        int [] arr = {9, 18, 27, 36, 45, 54, 63, 72,79};
        for (var j = 0; j < arr.length; j++) {
            if (i == arr[j]) {
                return true;
            }
        }
        return false;
    }

    private void add(Box b){
        Objects.requireNonNull(b);
        board.add(b);
    }

    public void init() {
        /* value = 0 means starting spot */
        add(new Box(0, true));
        add(new Box(0, true));
        for (var i = 2; i < size-2; i++) {
            if (isSpecialValue(i)) {
                /* value = 1 means button */
                var b = new Box(1, true);
                add(b);
            } else {
                var b = new Box(0, false);
                add(b);
            }
        }
        /* value = -1 means ending spot */
        add(new Box(-1, true));
        add(new Box(-1, true));
    }


    @Override
    public String toString() {
        /* pour le moment je laisse comme ca */
        var buffer = new StringBuilder();
        for (var i = 0; i < size; i++) {
            buffer.append(board.get(i));
        }
        return buffer.toString();
    }
}
