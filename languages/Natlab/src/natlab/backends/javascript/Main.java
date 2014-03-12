package natlab.backends.javascript;

import natlab.backends.javascript.codegen.JSASTGenerator;
import natlab.backends.javascript.jsast.Program;
import natlab.backends.javascript.pretty.Pretty;
import natlab.tame.BasicTamerTool;
import natlab.tame.tir.TIRFunction;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.toolkits.filehandling.GenericFile;
import natlab.toolkits.path.FileEnvironment;

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
        if (!gfile.exists()) {
            System.err.printf("Error: file '%s' does not exist.%n", gfile.getName());
            System.exit(1);
        }
        
        FileEnvironment fenv = new FileEnvironment(gfile);
        
        BasicTamerTool tool = new BasicTamerTool();
        ValueAnalysis<AggrValue<BasicMatrixValue>> analysis = tool.analyze(shapeDesc, fenv);
    
        System.out.println("=================================");
        Program program = new Program();
        
        int numFunctions = analysis.getNodeList().size();
        for (int i = 0; i < numFunctions; ++i) {
            TIRFunction matlabFunction = analysis.getNodeList().get(i).getAnalysis().getTree();
            program.addFunction(JSASTGenerator.genFunction(matlabFunction));
        }
        
        System.out.println(Pretty.display(program.pp()));
    }
}