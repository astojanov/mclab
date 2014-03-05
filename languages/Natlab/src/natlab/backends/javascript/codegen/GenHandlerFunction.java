package natlab.backends.javascript.codegen;

import natlab.backends.javascript.jsast.Function;
import natlab.backends.javascript.jsast.FunctionName;
import natlab.backends.javascript.jsast.Program;
import natlab.backends.javascript.jsast.StmtBlock;
import natlab.backends.javascript.jsast.StmtNull;
import natlab.backends.javascript.jsast.Variable;
import natlab.tame.tir.TIRFunction;
import natlab.tame.tir.TIRStmt;

public class GenHandlerFunction {
    public static void handle(TIRFunction tirFn, Program parent, JSCodeGen gen) {
        Function fn = new Function();
        fn.setFunctionName(new FunctionName(tirFn.getName()));
        
        for (ast.Name param: tirFn.getInputParams())
            fn.addParam(new Variable(param.getID()));
        
        StmtBlock stmts = new StmtBlock();
        gen.currentBlock.push(stmts);
        for (ast.Stmt stmt: tirFn.getStmts()) {
            ((TIRStmt) stmt).tirAnalyze(gen);
        }
        gen.currentBlock.pop();
        fn.setStmtBlock(stmts);
        
        
        parent.addFunction(fn);
    }
    
}
