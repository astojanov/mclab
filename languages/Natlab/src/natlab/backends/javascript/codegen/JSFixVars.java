package natlab.backends.javascript.codegen;

import java.util.*;
import natlab.backends.javascript.jsast.*;

public class JSFixVars {

    /**
     * @return the names of local variables that are assigned to.
     */
    public static Set<String> getVars(ASTNode root) {
        Set<String> locals = getLocals(root);
        Set<String> globals = getGlobals(root);
        locals.removeAll(globals);
        return locals;
    }

    /**
     * Get all the names of variables on the lhs of an assignment.
     * @return a set of strings of the variable names.
     */
    private static Set<String> getLocals(ASTNode node) {
        Set<String> vars = new HashSet<>();
        for (int i = 0; i < node.getNumChild(); ++i) {
            ASTNode child = node.getChild(i);

            if (child instanceof ExprAssign) {
                ExprAssign assignment = (ExprAssign) child;
                if (assignment.getLHS() instanceof ExprVar)
                    vars.add(((ExprVar) assignment.getLHS()).getName());
            }
            vars.addAll(getLocals(child));
        }
        return vars;
    }

    /**
     * Get all the  names of the globals.
     * @return a set of strings of the global names.
     */
    private static Set<String> getGlobals(ASTNode node) {
        Set<String> glos = new HashSet<>();
        for (int i = 0; i < node.getNumChild(); ++i) {
            ASTNode child = node.getChild(i);

            if (child instanceof StmtGlobalDecl) {
                StmtGlobalDecl globalDecl = (StmtGlobalDecl) child;
                for (ExprVar exprVar: globalDecl.getVarList()) {
                    glos.add(exprVar.getName());
                }
            }
            glos.addAll(getGlobals(child));
        }
        return glos;
    }

}
