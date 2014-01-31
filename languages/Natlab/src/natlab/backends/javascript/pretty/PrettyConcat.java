package natlab.backends.javascript.pretty;

public class PrettyConcat extends PrettyBase {
    public PrettyBase[] parts;


    public PrettyConcat(PrettyBase... parts) {
        this.parts = parts;
    }


    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        boolean first = true;
        for (PrettyBase part : parts) {
            s.append(part.toString());
        }
        return s.toString();
    }
}
