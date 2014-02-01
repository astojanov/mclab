package natlab.backends.javascript.pretty;

public class PrettyBlock extends PrettyWrapped {
    public PrettyBlock(PrettyBase p) {
        super(new PrettyConcat(new PrettyText("{"), PrettyLine.getInstance()),
                new PrettyConcat(PrettyLine.getInstance(), new PrettyText("}")),
                new PrettySeparatedBy(PrettyLine.getInstance(), p));
    }
    
    public String toString() {
        return super.toString();
    }
}
