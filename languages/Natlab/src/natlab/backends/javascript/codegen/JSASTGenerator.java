package natlab.backends.javascript.codegen;


import natlab.backends.javascript.jsast.*;
import natlab.tame.tir.*;
import natlab.toolkits.rewrite.TempFactory;

public class JSASTGenerator {
    public static Function genFunction(TIRFunction tirFunc) {
        Function fn = new Function();

        // Add input parameters.
        fn.setFunctionName(new FunctionName(tirFunc.getName()));
        for (ast.Name param: tirFunc.getInputParamList())
            fn.addParam(new Variable(param.getID()));
        
        // Add body statements.
        StmtBlock stmts = new StmtBlock();
        for (ast.Stmt astStmt: tirFunc.getStmts()) {
            TIRStmt tirStmt = (TIRStmt) astStmt;
            stmts.addStmt(genStmt(tirStmt));
        }
        
        Stmt returnStmt = makeStmtReturn(tirFunc);
        if (returnStmt != null)
            stmts.addStmt(returnStmt);
        
        fn.setStmtBlock(stmts);

        return fn;
        
    }
    
    
    private static Stmt makeStmtReturn(ast.Function astFunc) {
        // In MATLAB, results are returned to the caller by assigning
        // into out parameters.  We accumulate them into a list, and 
        // return an array containing the names of the out parameters
        // or just the name in case there is only one.
        List<Expr> returnNames = new List<>();
        for (ast.Name outParam: astFunc.getOutputParamList()) {
            returnNames.add(new ExprVar(outParam.getID()));
        }
        
        switch (returnNames.getNumChild()) {
        case 0: 
            return null;
        case 1: 
            return new StmtReturn(new Opt<Expr>(returnNames.getChild(0)));
        default: 
            return new StmtReturn(new Opt<Expr>(new ExprArray(returnNames)));
        }
    }
    
    /**
     * Main dispatching method for statements. It would be cleaner if we could
     * use Java's dynamic dispatch mechanism, but it would require us to modify every
     * Tamer/McSAF class, which we don't want to do.
     * @param stmt The IR statement to convert.
     * @return The JS statement.
     */    
    public static Stmt genStmt(TIRStmt tirStmt) {
        if (tirStmt instanceof TIRAbstractAssignToVarStmt) return genAssignToVarStmt((TIRAbstractAssignToVarStmt) tirStmt);
        else if (tirStmt instanceof TIRAbstractAssignToListStmt) return genAssignToListStmt((TIRAbstractAssignToListStmt) tirStmt);
        else if (tirStmt instanceof TIRArraySetStmt) return genArraySetStmt((TIRArraySetStmt) tirStmt);
        else if (tirStmt instanceof TIRWhileStmt) return genWhileStmt((TIRWhileStmt) tirStmt);
        else if (tirStmt instanceof TIRForStmt) return genForStmt((TIRForStmt) tirStmt);
        else if (tirStmt instanceof TIRIfStmt) return genIfStmt((TIRIfStmt) tirStmt);
        else if (tirStmt instanceof TIRCommentStmt) return genCommentStmt((TIRCommentStmt) tirStmt);
        else if (tirStmt instanceof TIRContinueStmt) return genContinueStmt();
        else if (tirStmt instanceof TIRBreakStmt) return genBreakStmt();
        else if (tirStmt instanceof TIRReturnStmt) return genReturnStmt((TIRReturnStmt) tirStmt);

        throw new UnsupportedOperationException(
                String.format("Statement not supported: %d. %s [%s]",
                        ((ast.Stmt) tirStmt).getStartLine(),
                        ((ast.Stmt) tirStmt).getPrettyPrinted(),
                        ((ast.Stmt) tirStmt).getClass().getName())
                );
    }
    
    
    public static Stmt genStmtList(TIRStatementList tirStmts) {
        StmtBlock stmts = new StmtBlock();
        for (int i = 0; i < tirStmts.getNumChild(); ++i) {
            TIRStmt currStmt = (TIRStmt) tirStmts.getChild(i);
            stmts.addStmt(genStmt(currStmt));
        }
        return stmts;
    }
    
    
    /**
     * A helper function that extracts the lhs name of an assignment.
     * Because it's a TIRAbstractAssignToVarStmt object, we are guaranteed
     * that there is exactly one element in the .getLValues() set.
     * @param tirStmt Extract the lhs from this statement.
     * @return the string of the name of the lhs.
     */
    private static String extractLHSName(TIRAbstractAssignToVarStmt tirStmt) {
        String lhs = null;
        for (String name: tirStmt.getLValues()) lhs = name;
        return lhs;
    }
    
    
    /**
     * MATLAB assignments of the form:
     *   x = <lit>
     *   x = y
     * where x and y are variables.
     * @param tirStmt the statement to process.
     * @return A JS assignment statement (ExprAssign wrapped in a StmtExpr).
     */
    public static Stmt genAssignToVarStmt(TIRAbstractAssignToVarStmt tirStmt) {
        String lhs = extractLHSName(tirStmt);
        ast.Expr rhs = tirStmt.getRHS();
        return new StmtExpr(new ExprAssign(new ExprVar(lhs), genExpr(rhs)));
    }
    
    
    /**
     * MATLAB assignments of the form:
     *   [x1, x2, ..., xn] = f(a1, a2, ..., an)
     * Currently, in JavaScript we assign the result of f to a
     * temporary variable and manually extract the elements one
     * by one into the correct variable names.  
     * @param tirStmt
     * @return A statement block (without braces) containing the call + assignments.
     */
    public static Stmt genAssignToListStmt(TIRAbstractAssignToListStmt tirStmt) {
        StmtBlockNoBraces stmts = new StmtBlockNoBraces();
        Expr call =
                tirStmt instanceof TIRCallStmt
                ? genCallExpr((ast.ParameterizedExpr) tirStmt.getRHS())
                : genArrayGetExpr((ast.ParameterizedExpr) tirStmt.getRHS());
        
        switch (tirStmt.getNumTargets()) {
        case 0:
            stmts.addStmt(new StmtExpr(call));
            break;
            
        case 1:
            stmts.addStmt(new StmtExpr(new ExprAssign(
                    new ExprVar(tirStmt.getTargetName().getID()),
                    call)));
            break;
            
        default:
            ExprVar tempVar = new ExprVar(TempFactory.genFreshTempString());
            stmts.addStmt(new StmtExpr(new ExprAssign(tempVar, call)));
            int i = 0;
            for (ast.Expr target: tirStmt.getTargets()) {
                stmts.addStmt(new StmtExpr(new ExprAssign(
                        new ExprVar(((ast.NameExpr) target).getName().getID()), 
                        new ExprPropertyGet(tempVar, new ExprInt(i)))));
                i++;
            }
            
        }
        
        return stmts;
    }
    
    
    /**
     * A helper function; MATLAB is 1-index, JavaScript is 0-indexed.  We use this 
     * method to transform an indexing expression into a subtraction by 1. 
     * @param expr the indexing expression.
     * @return expr - 1
     */
    private static Expr indexedBy(Expr expr) {
        return new ExprBinaryOp("-", expr, new ExprInt(1));
    }
    
    
    /**
     * MATLAB assignments of the form:
     *   m(i1, i2, ..., in) = x
     * @param tirStmt
     * @return m[i1-1][i2-1]...[in-1] = x
     */
    public static Stmt genArraySetStmt(TIRArraySetStmt tirStmt) {
        String lhs = tirStmt.getArrayName().getID();
        String rhs = tirStmt.getValueName().getID();
        TIRCommaSeparatedList indices = tirStmt.getIndizes();
        ExprPropertyGet prop = new ExprPropertyGet(new ExprVar(lhs), null);
        for (int i = 0; i < indices.getNumChild(); ++i) {
            if (i == 0)
                prop.setProperty(indexedBy(genExpr(indices.getChild(i))));
            else {
                prop = new ExprPropertyGet(
                        prop, 
                        indexedBy(genExpr(indices.getChild(i))));
            }
        }
        return new StmtExpr(new ExprAssign(prop, new ExprVar(rhs)));
    }
    
    
    /**
     * Transformation of a MATLAB while loop.
     * @param tirWhile the while loop to transform
     * @return a StmtWhile node
     */
    public static Stmt genWhileStmt(TIRWhileStmt tirWhile) {
        StmtBlock body = new StmtBlock();
        for (ast.Stmt stmt: tirWhile.getStmtList()) {
            body.addStmt(genStmt((TIRStmt) stmt));
        }
        
        return new StmtWhile(
                genExpr(tirWhile.getExpr()),
                body);
    }
    
    
    /**
     * Transformation of a MATLAB for loop.  Tamer ensures
     * that the loops always have the form:
     *   for i = low:inc:high
     *     ...
     *   end
     * @param tirFor the for loop to transform
     * @return a StmtFor node
     */
    public static Stmt genForStmt(TIRForStmt tirFor) {
        StmtBlock body = new StmtBlock();
        for (ast.Stmt stmt: tirFor.getStmtList()) {
            body.addStmt(genStmt((TIRStmt) stmt));
        }
        ExprVar iterVar = new ExprVar(tirFor.getLoopVarName().getID());
        ExprVar lowerBound = new ExprVar(tirFor.getLowerName().getID());
        ExprVar upperBound = new ExprVar(tirFor.getUpperName().getID());
        String increment = tirFor.hasIncr() ? tirFor.getIncName().getID() : null;
        
        Expr incr = increment == null ? new ExprInt(1) : new ExprVar(increment);
        
        return new StmtFor(
                new StmtVarDecl(iterVar, new Opt<Expr>(lowerBound)),
                new ExprBinaryOp("<=", iterVar, upperBound),
                new ExprAssign(iterVar, new ExprBinaryOp("+", iterVar, incr)),
                body
                );
    }
    
    
    public static Stmt genIfStmt(TIRIfStmt tirIf) {
        Expr condVar = new ExprVar(tirIf.getConditionVarName().getID());
        Stmt ifBlock = genStmtList(tirIf.getIfStatements());
        Opt<Stmt> elseBlock =
                tirIf.hasElseBlock()
                ? new Opt<Stmt>(genStmtList(tirIf.getElseStatements()))
                : new Opt<Stmt>();
        return new StmtIfThenElse(condVar, ifBlock, elseBlock);
    }
    
    

    public static Stmt genContinueStmt() {
        return new StmtContinue();
    }

    
    public static Stmt genBreakStmt() {
        return new StmtBreak();
    }
    
    
    public static Stmt genReturnStmt(TIRReturnStmt tirReturn) {
        ast.ASTNode curr = (ast.ASTNode) tirReturn;
        while (!(curr instanceof ast.Function)) 
            curr = curr.getParent();
        ast.Function astFunc = (ast.Function) curr;
        
        Stmt returnStmt = makeStmtReturn(astFunc);
        return returnStmt;
    }
    
    
    public static Stmt genCommentStmt(TIRCommentStmt tirComment) {
        if (tirComment.hasComments())
            return new StmtComment(tirComment.getNodeString());
        else
            return new StmtEmpty();
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
        else if (expr instanceof ast.ParameterizedExpr) return genCallExpr((ast.ParameterizedExpr) expr);
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
        return new ExprVar(expr.getName().getID());
    }

    // TODO: Replace function calls like plus() and mtimes() with
    //       JavaScript operators when operands are scalars.
    public static Expr genCallExpr(ast.ParameterizedExpr expr) {
        ExprCall call = new ExprCall();
        String funName = expr.getVarName();
        call.setFunctionName(new FunctionName(funName));
        for (ast.Expr arg: expr.getArgList()) {
            call.addArgument(genExpr(arg));
        }
        return call;
    }
    
    
    public static Expr genArrayGetExpr(ast.ParameterizedExpr expr) {
        ExprPropertyGet access = new ExprPropertyGet();
        String arrName = expr.getVarName();
        access.setExpr(new ExprVar(arrName));
        int i = 0;
        for (ast.Expr arg: expr.getArgList()) {
            if (i == 0)
                access.setProperty(indexedBy(genExpr(arg)));
            else
                access = new ExprPropertyGet(access, indexedBy(genExpr(arg)));
            i++;
        }
        return access;
    }
    
    
    // TODO: Should we do the actual string escaping here? 
    public static ExprString genStringLiteralExpr(ast.StringLiteralExpr expr) {
        return new ExprString(expr.getValue());
    }
}
