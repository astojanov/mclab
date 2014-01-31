package natlab.backends.javascript.pretty;

public class PrettyWrapped extends PrettyConcat {
    public PrettyWrapped(String opener, String closer, PrettyBase p) {
        super(new PrettyText(opener), p, new PrettyText(closer));
    }


    public String toString() {
        return super.toString();
    }

}
