package natlab.backends.javascript.pretty;

public class PrettyIndent extends PrettyBase {
    public PrettyBase child;
    
    public PrettyIndent(PrettyBase child) {
        this.child = child;
    }
}
