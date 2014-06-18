package natlab.tame.tir;

import ast.Name;
import ast.NameExpr;

abstract public class TIRAbstractAssignFromVarStmt extends TIRAbstractAssignStmt {
    private static final long serialVersionUID = 1L;

    public TIRAbstractAssignFromVarStmt(Name rhs) {
        super();
        setRHS(new NameExpr(rhs));
    }

    /**
     * Return the lhs name
     */
    abstract public Name getName();

    /**
     * returns the rhs name
     */
    public Name getValueName(){
        return ((NameExpr)getRHS()).getName();
    }

    /**
     * Return indices
     */
    abstract public TIRCommaSeparatedList getIndices();


}
