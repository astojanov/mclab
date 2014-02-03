package natlab.backends.javascript.pretty;

public class Pretty {
    public static PrettyBase EMPTY = text("");
    public static PrettyBase NEWLINE = newline();
    
    public static PrettyBase text(String s) {
        return new PrettyText(s);
    }
    
    
    private static PrettyBase newline() {
        return PrettyLine.getInstance();
    }
    
    
    public static PrettyBase concat(PrettyBase... parts) {
        return new PrettyConcat(parts);
    }
    
    
    public static PrettyBase indent(PrettyBase node) {
        return new PrettyIndent(node);
    }
    
    
    public static PrettyBase wrapped(PrettyBase opener, PrettyBase closer, PrettyBase node) {
        return concat(opener, node, closer);
    }

    
    public static PrettyBase parenthesized(PrettyBase node) {
        return wrapped(text("("), text(")"), node);
    }
    
    
    public static PrettyBase block(PrettyBase node) {
        return wrapped(
                concat(text("{"), NEWLINE),
                concat(NEWLINE, text("}")),
                separateBy(newline(), node));
    }
    
    
    public static PrettyBase separateBy(PrettyBase sep, PrettyBase... parts) {
        PrettyBase[] separatedParts = new PrettyBase[Math.max(0, 2 * parts.length - 1)];
        int i = 0;
        boolean first = true;

        for (PrettyBase part : parts) {
            if (!first)
                separatedParts[i++] = sep;
            first = false;
            separatedParts[i++] = part;
        }
        
        return concat(separatedParts);
    }
    
}
