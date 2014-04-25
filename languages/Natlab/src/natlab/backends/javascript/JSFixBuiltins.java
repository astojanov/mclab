package natlab.backends.javascript;

import natlab.backends.javascript.jsast.*;
import natlab.tame.builtin.Builtin;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;

public class JSFixBuiltins {
    public static void fix(ASTNode node, ValueAnalysis<AggrValue<BasicMatrixValue>> analysis, int index) {
        if (node instanceof ExprCall) {
            ExprCall call = (ExprCall) node;
            if (call.getExpr() instanceof ExprVar) {
                ExprVar var = (ExprVar) call.getExpr();
                
                if (Builtin.getInstance(var.getName()) != null) {
                    String suffix = "";
                    
                    for (Expr e: call.getArguments()) {
                        ExprVar arg = (ExprVar) e;
                        AggrValue<BasicMatrixValue> val = 
                                analysis.getNodeList()
                                .get(index)
                                .getAnalysis()
                                .getCurrentOutSet()
                                .get(arg.getName())
                                .getSingleton();
                        if (val instanceof BasicMatrixValue) {
                            if (((BasicMatrixValue) val).getShape().isScalar())
                                suffix += "S";
                            else
                                suffix += "M";
                        }
                    }
                    
                    var.setName("mc_" + var.getName() + "_" + suffix);                   
                }
            }
        }
        
        for (int i = 0; i < node.getNumChild(); ++i) {
            fix(node.getChild(i), analysis, index);
        }
    }
}
