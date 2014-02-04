package natlab.backends.javascript.pretty;

public class PrettyConcat extends PrettyBase {
    public PrettyBase[] parts;


    public PrettyConcat(PrettyBase... parts) {
        this.parts = parts;
    }
}
