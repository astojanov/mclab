/*
 *  Copyright 2014, Vincent Foley-Bourgon, McGill University
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
