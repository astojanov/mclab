package natlab.backends.javascript;

import natlab.backends.javascript.jsast.ExprAssign;
import natlab.backends.javascript.jsast.ExprCall;
import natlab.backends.javascript.jsast.Program;
import natlab.backends.javascript.jsast.StmtBlock;
import natlab.backends.javascript.jsast.StmtExpr;
import natlab.backends.javascript.codegen.JSCodeGen;
import natlab.tame.BasicTamerTool;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.toolkits.filehandling.GenericFile;
import natlab.toolkits.path.FileEnvironment;

import natlab.backends.javascript.jsast.*;
import natlab.backends.javascript.pretty.Pretty;

public class Main {
    private static void usage() {
        System.err.println("Usage: java -cp MatJuice.jar natlab.backends.javascript.Main <shape> <matlab file>");
        System.exit(1);
    }
    
    public static void main(String[] args) {
        if (args.length < 2) usage();
        
        String[] shapeDesc = { args[0] };
        String   matlabFile = args[1];
        
        GenericFile gfile = GenericFile.create(matlabFile);
        FileEnvironment fenv = new FileEnvironment(gfile);
        
        BasicTamerTool tool = new BasicTamerTool();
        ValueAnalysis<AggrValue<BasicMatrixValue>> analysis = tool.analyze(shapeDesc, fenv);
    
        System.out.println("=================================");
        Program program = new Program();
        JSCodeGen gen = new JSCodeGen(program);
        analysis.getNodeList().get(0).getAnalysis().getTree().tirAnalyze(gen);
        System.out.println(Pretty.display(program.pp()));
    }
}    
    
    
//    
//    public static void main(String[] args) {
//        Program p = new Program();
//
//        List<Variable> params = new List<>();
//        params.add(new Variable("x"));
//        params.add(new Variable("y"));
//        
//        StmtBlock stmts = new StmtBlock();
//        stmts.addStmt(new StmtExpr(new ExprString("use asm.js")));
//        stmts.addStmt(new StmtExpr(new ExprAssign(new Variable("x"), new ExprNum(42))));
//        StmtBlock lamStmts = new StmtBlock();
//        lamStmts.addStmt(new StmtExpr(new ExprAssign(new Variable("x"), new ExprNum(21))));
//        stmts.addStmt(new StmtExpr(new ExprAssign(
//                new Variable("f"),
//                new ExprLambda(
//                        new Function(
//                                new Opt<FunctionName>(),
//                                new List<Variable>(),
//                                lamStmts)))));
//        stmts.addStmt(new StmtExpr(new ExprCall(new FunctionName("f"), new List<Expr>())));
//                         
//        
//        p.addFunction(new Function(
//                new Opt<FunctionName>(new FunctionName("foo")),
//                params,
//                stmts));
//
//        System.out.println(Pretty.display(p.pp()));
//    }
// }
