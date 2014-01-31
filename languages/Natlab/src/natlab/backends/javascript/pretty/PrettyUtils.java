package natlab.backends.javascript.pretty;

import java.util.Stack;

public class PrettyUtils {
    private static int INDENT = 4;
    
    public static String indent(int level) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < INDENT*level; ++i)
            sb.append(' ');
        return sb.toString();
    }
        
    public static String display(PrettyBase root) {
        Stack<Pair> worklist = new Stack<>();
        worklist.push(new Pair(root, 0));
        return display(worklist, new StringBuffer(), 0);
    }
    

    private static String display(Stack<Pair> worklist,
            StringBuffer sb, int currCol) {
        if (worklist.isEmpty())
            return sb.toString();
        
        Pair pair = worklist.pop();
        PrettyBase node = pair.prettyNode;
        int currLevel = pair.level;
        
        if (node instanceof PrettyLine) {
            currCol = 0;
            sb.append('\n');
        }
        else if (node instanceof PrettyText) {
            String s = pad(currLevel*INDENT - currCol) + node.toString();
            currCol += s.length();
            sb.append(s);
        }
        else if (node instanceof PrettyConcat) {
            PrettyConcat pc = (PrettyConcat) node;
            for (int i = pc.parts.length - 1; i >= 0; --i) {
                worklist.push(new Pair(pc.parts[i], currLevel));
            }
        }
        else if (node instanceof PrettyIndent) {
            PrettyIndent pi = (PrettyIndent) node;
            worklist.push(new Pair(pi.child, currLevel+1));
        }
        
        return display(worklist, sb, currCol);
    }
    
    private static String pad(int width) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < width; ++i)
            sb.append(' ');
        return sb.toString();
    }

}