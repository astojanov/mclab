package natlab.backends.javascript.codegen;


import natlab.backends.javascript.jsast.*;
import natlab.tame.tir.*;
import natlab.toolkits.rewrite.TempFactory;

public class JSASTGenerator {

    /**
     * Entry point for converting a piece of MATLAB code.  We perform
     * a rather straight-forward conversion from MATLAB to JavaScript.
     * @param tirFunc the function to compile.
     * @return a Function node.
     */
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

        // Add a return statement at the end of the function.
        Stmt returnStmt = makeStmtReturn(tirFunc);
        if (returnStmt != null)
            stmts.addStmt(returnStmt);

        fn.setStmtBlock(stmts);

        return fn;

    }

    /**
     * In MATLAB, results are returned to the caller by assigning
     * into out parameters.  We accumulate them into a list, and
     * return an array containing the names of the out parameters
     * or just the name in case there is only one.
     * @param astFunc the function to create a return statement for
     * @return null if the function doesn't have output parameters, a StmtReturn otherwise.
     */
    private static Stmt makeStmtReturn(ast.Function astFunc) {
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
        if (tirStmt instanceof TIRAbstractAssignToListStmt) return genAssignToListStmt((TIRAbstractAssignToListStmt) tirStmt);
        if (tirStmt instanceof TIRArraySetStmt) return genArraySetStmt((TIRArraySetStmt) tirStmt);
        if (tirStmt instanceof TIRWhileStmt) return genWhileStmt((TIRWhileStmt) tirStmt);
        if (tirStmt instanceof TIRForStmt) return genForStmt((TIRForStmt) tirStmt);
        if (tirStmt instanceof TIRIfStmt) return genIfStmt((TIRIfStmt) tirStmt);
        if (tirStmt instanceof TIRCommentStmt) return genCommentStmt((TIRCommentStmt) tirStmt);
        if (tirStmt instanceof TIRContinueStmt) return genContinueStmt();
        if (tirStmt instanceof TIRBreakStmt) return genBreakStmt();
        if (tirStmt instanceof TIRReturnStmt) return genReturnStmt((TIRReturnStmt) tirStmt);

        throw new UnsupportedOperationException(
                String.format("Statement not supported: %d. %s [%s]",
                        ((ast.Stmt) tirStmt).getStartLine(),
                        ((ast.Stmt) tirStmt).getPrettyPrinted(),
                        ((ast.Stmt) tirStmt).getClass().getName())
                );
    }


    /**
     * In Tamer, we have a TIRStatementList that forces its elements
     * to be TIRStmt objects.  This method creates a block of code
     * that contains the translation of those statements.
     * @param tirStmts the statement list
     * @return a StmtBlock
     */
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
     *
     * If the target list is empty, we simply generate a call to the function.
     *
     * If the target list contains a single variable, we assign directly to it.
     *
     * If the target list contains more than one item, we store the result
     * of the function call in a temporary array variable and then extract
     * the individual parts into the target parameters.
     *
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
            new ExprAssign(iterVar, lowerBound),
            new ExprBinaryOp("<=", iterVar, upperBound),
            new ExprAssign(iterVar, new ExprBinaryOp("+", iterVar, incr)),
            body
            );
    }


    /**
     * Compile an if/else/end statement to JavaScript.  In Tamer, there is
     * always an else block, though it may be empty.  If the else block is empty,
     * we won't generate it in our JavaScript.
     * @param tirIf the if/else/end statement
     * @return a StmtIfThenElse node
     */
    public static Stmt genIfStmt(TIRIfStmt tirIf) {
        Expr condVar = new ExprVar(tirIf.getConditionVarName().getID());
        Stmt ifBlock = genStmtList(tirIf.getIfStatements());
        boolean hasElseStatements = tirIf.getElseStatements().getNumChild() > 0;
        Opt<Stmt> elseBlock =
                hasElseStatements
                ? new Opt<Stmt>(genStmtList(tirIf.getElseStatements()))
                : new Opt<Stmt>();
        return new StmtIfThenElse(condVar, ifBlock, elseBlock);
    }


    /**
     * Convert a MATLAB continue to a JavaScript continue.
     * @return
     */
    public static Stmt genContinueStmt() {
        return new StmtContinue();
    }


    /**
     * Convert a MATLAB break to a JavaScript break.
     * @return
     */
    public static Stmt genBreakStmt() {
        return new StmtBreak();
    }


    /**
     * Convert a MATLAB return to a JavaScript break.  In MATLAB, you don't give
     * an expression to return, it exits the current function and the output
     * parameters are returned to the called.  To emulate this behavior in JavaScript,
     * we find the names of the output parameters of the enclosing function and return them
     * explicitly.
     * @param tirReturn
     * @return
     */
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
        if (expr instanceof ast.FPLiteralExpr) return genFPLiteralExpr((ast.FPLiteralExpr) expr);
        if (expr instanceof ast.StringLiteralExpr) return genStringLiteralExpr((ast.StringLiteralExpr) expr);
        if (expr instanceof ast.NameExpr) return genNameExpr((ast.NameExpr) expr);
        if (expr instanceof ast.ParameterizedExpr) return genCallExpr((ast.ParameterizedExpr) expr);
        throw new UnsupportedOperationException(
                String.format("Expr node not supported. %d. %s [%s]",
                        expr.getStartLine(),
                        expr.getPrettyPrinted(),
                        expr.getClass().getName())
                );

    }

    /**
     * Convert an integer literal into JavaScript.
     * @param expr
     * @return
     */
    public static ExprInt genIntLiteralExpr(ast.IntLiteralExpr expr) {
        return new ExprInt(Integer.parseInt(expr.getValue().getText()));
    }


    /**
     * Convert a double literal into JavaScript.
     * @param expr
     * @return
     */
    public static ExprNum genFPLiteralExpr(ast.FPLiteralExpr expr) {
        return new ExprNum(Double.parseDouble(expr.getValue().getText()));
    }



    /**
     * Convert a string literal into JavaScript.
     *
     * TODO: handle escaping.
     * @param expr
     * @return
     */
    public static ExprString genStringLiteralExpr(ast.StringLiteralExpr expr) {
        return new ExprString(expr.getValue());
    }

    public static ExprVar genNameExpr(ast.NameExpr expr) {
        return new ExprVar(expr.getName().getID());
    }

    /**
     * Convert a function call expression into JavaScript.
     * @param expr a ParametrizedExpr where the name corresponds to a
     * function kind.
     * @return The JavaScript function call.
     *
     * TODO: Replace function calls like plus() and mtimes() with
     *       JavaScript operators when operands are scalars.
     */
    public static Expr genCallExpr(ast.ParameterizedExpr expr) {
        ExprCall call = new ExprCall();
        ExprVar funName = new ExprVar(expr.getVarName());
        call.setExpr(funName);
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
}
