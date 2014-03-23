package natlab.backends.javascript.codegen;

import java.util.*;
import natlab.backends.javascript.jsast.*;

public class JSFixVars {
    /**
     * Get all the names of variables on the lhs of an assignment.
     * @return a set of strings of the variable names.
     */
    public static Set<String> getVars(ASTNode node) {
        Set<String> vars = new HashSet<>();
        for (int i = 0; i < node.getNumChild(); ++i) {
            ASTNode child = node.getChild(i);

            if (child instanceof ExprAssign) {
                ExprAssign assignment = (ExprAssign) child;
                if (assignment.getLHS() instanceof ExprVar)
                    vars.add(((ExprVar) assignment.getLHS()).getName());
            }
            vars.addAll(getVars(child));
        }
        return vars;
    }
}
