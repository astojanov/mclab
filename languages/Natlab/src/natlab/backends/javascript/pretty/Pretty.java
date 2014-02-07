package natlab.backends.javascript.pretty;

import java.util.Stack;


public class Pretty {
    public static int INDENT_WIDTH = 4;

    public static PrettyBase EMPTY = text("");
    public static PrettyBase NEWLINE = PrettyLine.getInstance();

    public static PrettyBase text(String s) {
        return new PrettyText(s);
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
                separateBy(NEWLINE, node));
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


    public static PrettyBase argList(PrettyBase... parts) {
        return parenthesized(separateBy(text(", "), parts));
    }


    public static String display(PrettyBase root) {
        int currCol = 0;
        StringBuffer sb = new StringBuffer();
        Stack<Pair> worklist = new Stack<>();
        worklist.push(new Pair(root, 0));

        while (!worklist.isEmpty()) {
            Pair pair = worklist.pop();
            PrettyBase node = pair.prettyNode;
            int currLevel = pair.level;

            if (node instanceof PrettyLine) {
                currCol = 0;
                sb.append('\n');
            }
            else if (node instanceof PrettyText) {
                String s = PrettyUtils.pad(currLevel*INDENT_WIDTH - currCol) + node.toString();
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
        }

        return sb.toString();
    }


}
