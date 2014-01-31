package natlab.backends.javascript.pretty;


public class Pair {
    public PrettyBase prettyNode;
    public int level;
    
    public Pair(PrettyBase node, int level) {
        this.prettyNode = node;
        this.level = level;
    }
}
