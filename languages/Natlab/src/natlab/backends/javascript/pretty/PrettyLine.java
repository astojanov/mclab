package natlab.backends.javascript.pretty;

public class PrettyLine extends PrettyBase {
    private static PrettyLine singleton = null;


    public static PrettyLine getInstance() {
        if (singleton == null) {
            singleton = new PrettyLine();
            return singleton;
        }
        return singleton;
    }


    private PrettyLine() {
    }


    public String toString() {
        // Gives a platform-independent newline.
        return String.format("%n");
    }
}
