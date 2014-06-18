package natlab.tame.tir;

import natlab.tame.tir.analysis.TIRNodeCaseHandler;
import ast.DotExpr;
import ast.Name;
import ast.NameExpr;

//TODO give cell/array/det set a single parent '... = t' (assign from var?)
// - alternative: remove altogether, and have one complex assign (?)
public class TIRDotSetStmt extends TIRAbstractAssignFromVarStmt{
    private static final long serialVersionUID = 1L;

    public TIRDotSetStmt(Name dot, Name field, Name rhs) {
        super(rhs);
        this.setLHS(new DotExpr(new NameExpr(dot),field));
    }

    public Name getDotName(){
        return ((NameExpr)(((DotExpr)getLHS()).getTarget())).getName();
    }

    public Name getName() {
        return getDotName();
    }

    public Name getFieldName(){
        return ((DotExpr)getLHS()).getField();
    }

    public TIRCommaSeparatedList getIndices() {
        TIRCommaSeparatedList fields = new TIRCommaSeparatedList(new NameExpr(getFieldName()));
        return fields;
    }



    @Override
    public void tirAnalyze(TIRNodeCaseHandler irHandler) {
        irHandler.caseTIRDotSetStmt(this);
    }
}
