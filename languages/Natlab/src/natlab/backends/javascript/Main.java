package natlab.backends.javascript;

import natlab.backends.javascript.ast.*;
import natlab.backends.javascript.pretty.*;

public class Main {

    public static void main(String[] args) {
        Program p = new Program();

        List<Stmt> nullStmt = new List<>();
        nullStmt.add(new StmtNull());
        p.addFunction(new Function(
                new Opt<FunctionName>(),
                new List<Variable>(),
                nullStmt));

        List<Variable> params = new List<>();
        params.add(new Variable("x"));
        params.add(new Variable("y"));
        List<Stmt> stmts = new List<>();
        List<Expr> funArgs = new List<>();
        funArgs.add(new ExprVar(new Variable("x")));
        funArgs.add(new ExprString("hello"));
        stmts.add(new StmtExpr(new ExprString("use asm.js")));
        stmts.add(new StmtExpr(new ExprAssign(new Variable("x"), new ExprInt(42))));

        StmtBlock thenPart = new StmtBlock();
        thenPart.addStmt(new StmtExpr(new ExprInt(1)));
        thenPart.addStmt(new StmtExpr(new ExprString("two")));
        StmtExpr elsePart = new StmtExpr(new ExprBoolean(false));
        stmts.add(new StmtIfThenElse(
                      new ExprBoolean(true),
                      thenPart,
                      new Opt<Stmt>(elsePart)
                      ));
        stmts.add(
                new StmtReturn(
                        new Opt<Expr>(
                                new ExprCall( new FunctionName("add"), funArgs))));

        p.addFunction(new Function(
                new Opt<FunctionName>(new FunctionName("function2")),
                params,
                stmts));

        System.out.println(Pretty.display(p.pp()));
    }
}
