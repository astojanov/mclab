package natlab.tame.tir;

import ast.*;
import natlab.tame.tir.analysis.TIRNodeCaseHandler;

/**
 * statements of the form
 * u = t
 */
public class TIRCopyStmt extends TIRAbstractAssignToVarStmt {
    private static final long serialVersionUID = 1L;

    public TIRCopyStmt(Name lhs, Name rhs) {
        super(lhs);
        this.setRHS(new NameExpr(rhs));
    }
    
    @Override
    public void irAnalyize(TIRNodeCaseHandler irHandler) {
        irHandler.caseTIRCopyStmt(this);
    }
    
    public Name getSourceName(){
        return ((NameExpr)(getRHS())).getName();
    }

}
