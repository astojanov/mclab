package natlab.backends.javascript.codegen;

import java.util.Stack;

import natlab.backends.javascript.jsast.*;

import natlab.tame.tir.TIRAbstractAssignStmt;
import natlab.tame.tir.TIRFunction;
import natlab.tame.tir.analysis.TIRAbstractNodeCaseHandler;

public class JSCodeGen extends TIRAbstractNodeCaseHandler {
    
    private Program program;
    public Stack<StmtBlock> currentBlock = new Stack<>();

    
    public JSCodeGen(Program program) {
        this.program = program;
    }
    
    
    @Override
    public void caseASTNode(ast.ASTNode node) {
        System.err.println("UNHANDLED NODE");
    }

    @Override public void caseTIRFunction(TIRFunction node) {
        GenHandlerFunction.handle(node, program, this);
    }
    
    @Override public void caseTIRAbstractAssignStmt(TIRAbstractAssignStmt stmt) {
        GenHandlerAssignStmt.handle(stmt, currentBlock.peek(), this);
    }
}
