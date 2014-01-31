package natlab.backends.javascript.pretty;

public class PrettyWrapped extends PrettyBase {
    private PrettyConcat container;


    public PrettyWrapped(String opener, String closer, PrettyBase p) {
        container = new PrettyConcat(new PrettyText(opener), p, new PrettyText(
                closer));
    }


    public String show() {
        return container.show();
    }

}
