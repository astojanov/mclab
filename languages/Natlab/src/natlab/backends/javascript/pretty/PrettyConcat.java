package natlab.backends.javascript.pretty;

public class PrettyConcat extends PrettyBase {
    private PrettyBase[] parts;


    public PrettyConcat(PrettyBase... parts) {
        this.parts = parts;
    }


    @Override
    public String show() {
        StringBuffer s = new StringBuffer();
        boolean first = true;
        for (PrettyBase part : parts) {
            s.append(part.show());
        }
        return s.toString();
    }
}
