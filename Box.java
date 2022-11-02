public class Box {
    private final boolean statut;
    private int value;

    public Box(int v, boolean s) {
        value = v;
        statut = s;
    }

    public boolean getStatut() {
        return statut;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (statut) {
            switch (value) {
                case -1: /* end */
                    return "//";
                case 0: /* beginnig */
                    return "__";
                case 1: /* piece */
                    return "0 ";
            }
        }
        return "x ";
    }
}
