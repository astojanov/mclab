package natlab.backends.javascript.pretty;

public class PrettyParenthesized extends PrettyBase {
    private PrettyWrapped container;


    public PrettyParenthesized(PrettyBase p) {
        container = new PrettyWrapped("(", ")", p);
    }


    @Override
    public String show() {
        return container.show();
    }

}
