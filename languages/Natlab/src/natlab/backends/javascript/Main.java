package natlab.backends.javascript;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
        System.err.println("Usage: java -cp MatJuice.jar natlab.backends.javascript.Main <shape> <matlab file> <output file>");
        System.exit(1);
    }
    
    public static void main(String[] args) {
        if (args.length < 3) usage();
        
        String[] shapeDesc = { args[0] };
        String   matlabFile = args[1];
        String   javascriptFile = args[2];
        
        GenericFile gfile = GenericFile.create(matlabFile);
        if (!gfile.exists()) {
            System.err.printf("Error: file '%s' does not exist.%n", gfile.getName());
            System.exit(1);
        }
        
        FileEnvironment fenv = new FileEnvironment(gfile);
        ValueAnalysis<AggrValue<BasicMatrixValue>> analysis = BasicTamerTool.analyze(shapeDesc, fenv);

        Program program = new Program();
  
        Set<String> processedFunctions = new HashSet<>();
        int numFunctions = analysis.getNodeList().size();
        for (int i = 0; i < numFunctions; ++i) {
            TIRFunction matlabFunction = analysis.getNodeList().get(i).getAnalysis().getTree();
            if (!processedFunctions.contains(matlabFunction.getName())) {
                processedFunctions.add(matlabFunction.getName());
                program.addFunction(JSASTGenerator.genFunction(matlabFunction));
            }
        }
        
        FileWriter out = null;
        BufferedReader libReader = null;
        try {
            out = new FileWriter(javascriptFile);
            libReader = new BufferedReader(new FileReader("src/natlab/backends/javascript/lib/lib.js"));
            String line;
            while ((line = libReader.readLine()) != null) {
                out.write(String.format("%s%n", line));
            }
            out.write(String.format("%n%n// BEGINNING OF PROGRAM%n%n"));
            out.write(Pretty.display(program.pp()));
            
        }
        catch (IOException exc) {
            System.err.println("Error: cannot write to " + javascriptFile);
        }
        finally {
            try {
                out.close();
                libReader.close();
            }
            catch (IOException e) {}
        }
    }
}