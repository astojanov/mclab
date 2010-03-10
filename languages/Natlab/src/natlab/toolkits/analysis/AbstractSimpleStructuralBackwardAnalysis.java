package natlab.toolkits.analysis;

import ast.*;
import java.util.*;

/**
 * A simple abstract implementation of a
 * AbstractStructuralBackwardAnalysis. This class provides some simple
 * implementations of methods such as processBreaks and caseBreakStmt.
 *
 * @see AbstractStructuralBackwardAnalysis
 */
public abstract class AbstractSimpleStructuralBackwardAnalysis<A extends FlowSet> extends AbstractStructuralBackwardAnalysis<A>
{

    public AbstractSimpleStructuralBackwardAnalysis(ASTNode tree){
        super( tree );
    }


    public void caseBreakStmt( BreakStmt node )
    {
        LoopFlowsets<A> loop = loopStack.peek();
        currentOutSet = newInitialFlow();
        copy( loop.getBreakInFlow(), currentOutSet );
    }
    public void caseContinueStmt( ContinueStmt node )
    {
        LoopFlowsets<A> loop = loopStack.peek();
        currentOutSet = newInitialFlow();
        copy( loop.getBreakInFlow(), currentOutSet );
    }
    public void caseCondition( Expr condExpr )
    {
        caseExpr( condExpr );
    }
    public void caseLoopVarAsCondition( AssignStmt node )
    {
        caseAssignStmt( node );
    }
    public void caseLoopVarAsInit( AssignStmt node )
    {
        caseAssignStmt( node );
    }
    public void caseLoopVarAsUpdate( AssignStmt node )
    {
        caseAssignStmt( node );
    }
        
}