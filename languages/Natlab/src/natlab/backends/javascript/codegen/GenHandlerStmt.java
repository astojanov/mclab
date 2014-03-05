package natlab.backends.javascript.codegen;

import natlab.backends.javascript.jsast.Function;

public class GenHandlerStmt {
    
    public ast.Stmt stmt;
    public Function fn;
    
    public GenHandlerStmt(ast.Stmt stmt, Function fn) {
        this.stmt = stmt;
        this.fn = fn;
    }

}
