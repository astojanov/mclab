package natlab.backends.javascript.codegen;

import java.util.HashMap;
import java.util.Map;

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

        // Add input parameters.
        fn.setFunctionName(new FunctionName(node.getName()));
        for (ast.Name param: node.getInputParamList())
            fn.addParam(new Variable(param.getID()));

        // Add body statements.
        StmtBlock stmts = new StmtBlock();
        for (ast.Stmt stmt: node.getStmtList()) {
            stmts.addStmt(genStmt(stmt));
        }
        
        // In MATLAB, results are returned to the caller by assigning
        // into out parameters.  We accumulate them into a list, and 
        // return an array containing the names of the out parameters
        // or just the name in case there is only one.
        List<Expr> returnNames = new List<>();
        for (ast.Name outParam: node.getOutputParamList()) {
            returnNames.add(new ExprVar(new Variable(outParam.getID())));
        }
        
        switch (returnNames.getNumChild()) {
        case 0: 
            break;
        case 1: 
            stmts.addStmt(new StmtReturn(new Opt<Expr>(returnNames.getChild(0))));
            break;
        default: 
            stmts.addStmt(new StmtReturn(new Opt<Expr>(new ExprArray(returnNames))));
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
        if (stmt instanceof ast.WhileStmt) return genWhileStmt((ast.WhileStmt) stmt);
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
        if (lhs instanceof ast.ParameterizedExpr) {
            ast.ParameterizedExpr lhsPe = (ast.ParameterizedExpr) lhs;
            String lhsName = lhsPe.getNodeString();
            Expr rhs = genExpr(stmt.getRHS());
            return new StmtExpr(new ExprAssign(new Variable(lhsName), rhs));
        }
        throw new UnsupportedOperationException(
                String.format("genAssignStmt does not support arrays with multiple bindings: %d. %s [%s]",
                        stmt.getStartLine(),
                        stmt.getPrettyPrinted(),
                        lhs.getClass().getName())
                );
    }
    
    public static StmtWhile genWhileStmt(ast.WhileStmt stmt) {
        StmtWhile wstmt = new StmtWhile();
        wstmt.setCond(genExpr(stmt.getExpr()));
        List<Stmt> body = new List<>();
        for (ast.Stmt bodyStmt: stmt.getStmts()) {
            body.add(genStmt(bodyStmt));
        }
        wstmt.setBody(new StmtBlock(body));
        return wstmt;
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

    // TODO: Replace function calls like plus() and mtimes() with
    //       JavaScript operators when operands are scalars.
    public static Expr genParametrizedExpr(ast.ParameterizedExpr expr) {
        ExprCall call = new ExprCall();
        String funName = expr.getVarName();
        call.setFunctionName(new FunctionName(funName));
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
