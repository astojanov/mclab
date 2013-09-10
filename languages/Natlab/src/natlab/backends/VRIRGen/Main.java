package natlab.backends.VRIRGen;

import java.util.Set;

import natlab.backends.Fortran.codegen_readable.FortranCodeASTGenerator;
import natlab.tame.AdvancedTamerTool;
import natlab.tame.BasicTamerTool;
import natlab.tame.callgraph.StaticFunction;
import natlab.tame.tamerplus.analysis.AnalysisEngine;
import natlab.tame.tamerplus.transformation.TransformationEngine;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.ValueFlowMap;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.toolkits.filehandling.GenericFile;
import natlab.toolkits.path.FileEnvironment;
import ast.ASTNode;
import ast.Function;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/**
		 * This main method is just for testing, doesn't follow the convention
		 * when passing a file to a program, please replace "fileDir and fileIn"
		 * below with your real testing file directory and its name, and you can
		 * pass the type info of the input argument to the program, currently,
		 * the type info is composed like double&3*3&REAL.
		 */
		String fileDir = "/home/sameer/mclab/";
		String fileIn = fileDir + "simple.m";
		GenericFile gFile = GenericFile.create(fileIn);
		FileEnvironment env = new FileEnvironment(gFile); // get path
															// environment obj
		AdvancedTamerTool tool = new AdvancedTamerTool();
		ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis = tool.analyze(
				args, env);
		if (analysis == null) {
			System.out.println("problem from the start");

		}
		int size = analysis.getNodeList().size();

		for (int i = 0; i < size; i++) {
			/*
			 * type inference.
			 */
			ValueFlowMap<AggrValue<AdvancedMatrixValue>> currentOutSet = analysis
					.getNodeList().get(i).getAnalysis().getCurrentOutSet();
			// System.err.println(currentOutSet);
			/*
			 * tamer plus analysis.
			 */
			StaticFunction function = analysis.getNodeList().get(i)
					.getFunction();
			// TamerPlusUtils.debugMode();
			// System.out.println("tamer pretty print: \n"+function.getAst().getPrettyPrinted());
			TransformationEngine transformationEngine = TransformationEngine
					.forAST(function.getAst());
			AnalysisEngine analysisEngine = transformationEngine
					.getAnalysisEngine();
			@SuppressWarnings("rawtypes")
			ASTNode fTree = transformationEngine.getTIRToMcSAFIRWithoutTemp()
					.getTransformedTree();
			Set<String> remainingVars = analysisEngine
					.getTemporaryVariablesRemovalAnalysis()
					.getRemainingVariablesNames();
			System.out.println("\ntamer plus analysis result: \n"
					+ fTree.getPrettyPrinted() + "\n");
			// System.err.println("remaining variables: \n"+remainingVars);
			/*
			 * Fortran code generation.
			 */
			System.err
					.println("pretty print the generated VRIR in XML format  .\n");
			StringBuffer sb;

			sb = VrirXmlGen.generateVrir((Function) fTree, remainingVars,
					analysis, currentOutSet, i, size);

			System.err.println(sb);
		}

	}

}
