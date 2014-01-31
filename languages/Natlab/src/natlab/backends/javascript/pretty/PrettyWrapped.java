package natlab.backends.javascript.pretty;

public class PrettyWrapped extends PrettyConcat {
    public PrettyWrapped(String opener, String closer, PrettyBase p) {
        this(new PrettyText(opener), new PrettyText(closer), p);
    }  
    
    public PrettyWrapped(PrettyBase opener, PrettyBase closer, PrettyBase p) {
        super(opener, p, closer);
    }


    public String toString() {
        return super.toString();
    }

}
