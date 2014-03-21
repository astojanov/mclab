package natlab.backends.javascript.codegen;

import java.util.*;
import natlab.backends.javascript.jsast.*;

/**
 * Replace statements of the form `x = ...` to
 * `var x = ...` for the first assignment to x.
 */
public class JSFixVars {
    public static void fixVars(ASTNode node, Set<String> declaredVars) {
        JSFixVars inst = new JSFixVars();
        for (int i = 0; i < node.getNumChild(); ++i) {
            ASTNode child = node.getChild(i);

            // child is a node of the form `<something> = ...`
            if (child instanceof StmtExpr
                && child.getChild(0) instanceof ExprAssign) {

                ExprAssign assignment = (ExprAssign) child.getChild(0);

                // the assignment is to a variable.
                if (assignment.getLValue() instanceof ExprVar) {
                    String var = ((ExprVar) assignment.getLValue()).getVariable();
                    if (!declaredVars.contains(var)) {
                        declaredVars.add(var);
                        node.setChild(new StmtVarDecl(new ExprVar(var),
                                                      new Opt<Expr>(assignment.getExpr())),
                                      i);
                    }
                }
            }

            if (child instanceof StmtVarDecl) {
                StmtVarDecl varDecl = (StmtVarDecl) child;
                declaredVars.add(varDecl.getVar().getVariable());
            }

            fixVars(child, declaredVars);
        }
    }
}
