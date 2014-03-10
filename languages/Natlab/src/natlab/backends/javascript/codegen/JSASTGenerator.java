package natlab.backends.javascript.codegen;

import natlab.backends.javascript.jsast.*;
import natlab.tame.tir.TIRFunction;

public class JSASTGenerator {
    /**
     * Convert a TIRFunction node to a jsast.Function
     * @param node
     * @return
     */
    public static Function gen(TIRFunction node) {
        Function fn = new Function();

        fn.setFunctionName(new FunctionName(node.getName()));
        for (ast.Name param: node.getInputParamList())
            fn.addParam(new Variable(param.getID()));

        StmtBlock stmts = new StmtBlock();
        for (ast.Stmt stmt: node.getStmtList()) {
            stmts.addStmt(genStmt(stmt));
        }
        fn.setStmtBlock(stmts);

        return fn;
    }
    
    
    /**
     * Main dispatching method for statements. It would be cleaner if we could
     * use Java's dynamic dispatch mechanism, but it would require us to modify every
     * Tamer/McSAF class, which we don't want to do.
     * @param stmt The IR statement to convert.
     * @return The JS statement.
     */
    public static Stmt genStmt(ast.Stmt stmt) {
        if (stmt instanceof ast.AssignStmt) return genAssignStmt((ast.AssignStmt) stmt);
        return new StmtNull();
    }
    
    public static StmtExpr genAssignStmt(ast.AssignStmt stmt) {
        ast.Expr lhs = stmt.getLHS();
        
        if (lhs instanceof ast.NameExpr) {
            String lhsName = lhs.getVarName();
            Expr rhs = genExpr(stmt.getRHS());
            return new StmtExpr(new ExprAssign(new Variable(lhsName), rhs));
        }
        if (lhs instanceof ast.MatrixExpr) {
            ast.MatrixExpr lhsMat = (ast.MatrixExpr) lhs;
            if (lhs.getNumChild() == 1) {
                String lhsName = ((ast.NameExpr) lhsMat.getRow(0).getElement(0)).getName().getID();
                Expr rhs = genExpr(stmt.getRHS());
                return new StmtExpr(new ExprAssign(new Variable(lhsName), rhs));
            }
            
        }
        throw new UnsupportedOperationException(
                String.format("genStmt does not support arrays with multiple bindings: %d. %s",
                        stmt.getStartLine(),
                        stmt.getPrettyPrinted())
                );
    }

    
    /**
     * Main dispatching method for expressions.  Like with statements, we use instanceof
     * to dispatch to the correct method, since we don't want to modify the class of all
     * the node types we handle.
     * @param expr The IR expression to convert.
     * @return The JS expression.
     */
    public static Expr genExpr(ast.Expr expr) {
        if (expr instanceof ast.IntLiteralExpr) return genIntLiteralExpr((ast.IntLiteralExpr) expr);
        else if (expr instanceof ast.FPLiteralExpr) return genFPLiteralExpr((ast.FPLiteralExpr) expr);
        else if (expr instanceof ast.StringLiteralExpr) return genStringLiteralExpr((ast.StringLiteralExpr) expr);
        else if (expr instanceof ast.NameExpr) return genNameExpr((ast.NameExpr) expr);
        else if (expr instanceof ast.ParameterizedExpr) return genParametrizedExpr((ast.ParameterizedExpr) expr);
        throw new UnsupportedOperationException(
                String.format("Expr node not supported. %d. %s [%s]", 
                        expr.getStartLine(), 
                        expr.getPrettyPrinted(), 
                        expr.getClass().getName())
                );

    }

    public static ExprInt genIntLiteralExpr(ast.IntLiteralExpr expr) {
        return new ExprInt(Integer.parseInt(expr.getValue().getText()));
    }
    
    public static ExprNum genFPLiteralExpr(ast.FPLiteralExpr expr) {
        return new ExprNum(Double.parseDouble(expr.getValue().getText()));
    }
    
    public static ExprVar genNameExpr(ast.NameExpr expr) {
        return new ExprVar(new Variable(expr.getName().getID()));
    }
    
    public static ExprCall genParametrizedExpr(ast.ParameterizedExpr expr) {
        ExprCall call = new ExprCall();
        call.setFunctionName(new FunctionName(expr.getVarName()));
        for (ast.Expr arg: expr.getArgList()) {
            call.addArgument(genExpr(arg));
        }
        return call;
    }
    
    
    // TODO: Should we do the actual string escaping here? 
    public static ExprString genStringLiteralExpr(ast.StringLiteralExpr expr) {
        return new ExprString(expr.getValue());
    }
    
    public static ASTNode<ASTNode> gen(ast.ASTNode node) {
        throw new UnsupportedOperationException("node type not supported: " + node.getPrettyPrinted());
    }
}
