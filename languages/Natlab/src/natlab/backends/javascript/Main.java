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
                new FunctionBody(nullStmt)));
        
        List<Variable> params = new List<>();
        params.add(new Variable("x"));
        params.add(new Variable("y"));
        List<Stmt> stmts = new List<>();
        List<Expr> funArgs = new List<>();
        funArgs.add(new ExprVar(new Variable("x")));
        funArgs.add(new ExprString("hello"));
        stmts.add(new StmtExpr(new ExprString("use asm.js")));
        stmts.add(new StmtExpr(new ExprAssign(new Variable("x"), new ExprInt(42))));
        stmts.add(
                new StmtReturn(
                        new Opt<Expr>(
                                new ExprCall( new FunctionName("add"), funArgs))));
        
        p.addFunction(new Function(
                new Opt<FunctionName>(new FunctionName("function2")), 
                params,
                new FunctionBody(stmts)));
        
        System.out.println(PrettyUtils.display(p.pp()));
    }
}
