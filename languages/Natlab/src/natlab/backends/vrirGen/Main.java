package natlab.backends.vrirGen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import natlab.backends.vrirGen.WrapperGenFactory.TargetLang;
//import natlab.backends.vrirGen.vrirCodeGen.CppCodeGen;
import natlab.tame.BasicTamerTool;
import natlab.tame.callgraph.SimpleFunctionCollection;
import natlab.tame.callgraph.StaticFunction;
import natlab.tame.tamerplus.analysis.AnalysisEngine;
import natlab.tame.tamerplus.transformation.TransformationEngine;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.ValueFlowMap;
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

		String fileDir = "adpt/";
		String fileName = "adapt.m";

		String fileIn = fileDir + fileName;
		File file = new File(fileIn);

		GenericFile gFile = GenericFile.create(file.getAbsolutePath());
		String[] inputArgs = null;
		String[] testArgs = Main.getArgs(file);
		if (testArgs != null) {
			inputArgs = testArgs;
		} else if (args.length > 0) {
			inputArgs = args;
		} else {
			throw new NullPointerException("arguments not provided");
		}
		
		FileEnvironment env = new FileEnvironment(gFile); // get path
		SimpleFunctionCollection.convertColonToRange = true;
		BasicTamerTool tool = new BasicTamerTool();
		tool.setDoIntOk(true);
		ValueAnalysis<AggrValue<BasicMatrixValue>> analysis = tool.analyze(
				inputArgs, env);
		
		int size = analysis.getNodeList().size();
		WrapperGenerator wrapper = WrapperGenFactory.getWrapperGen(
				TargetLang.Cpp, analysis.getMainNode().getFunction());
		StringBuffer genXML = new StringBuffer();
		VrirXmlGen.genModuleXMLHead(genXML, fileName.split("\\.")[0]);
		genXML.append(HelperClass.toXMLHead("fns"));
		OperatorMapper.initMap();
		VrirTypeMapper.initTypeMap();
		HashSet<StaticFunction> funcSet = new HashSet<StaticFunction>();
		for (int i = 0; i < size; i++) {
			StringBuffer sb;
			/*
			 * type inference.
			 */
			ValueFlowMap<AggrValue<BasicMatrixValue>> currentOutSet = analysis
					.getNodeList().get(i).getAnalysis().getCurrentOutSet();

			/*
			 * tamer plus analysis.
			 */
			StaticFunction function = analysis.getNodeList().get(i)
					.getFunction();

			System.out.println("Analysis function  " + function.getName());
			if (!funcSet.contains(function)) {

//				if (function.getName().equals(
//						analysis.getMainNode().getFunction().getName())) {
//
//					funcSet.add(function);
//					continue;
//				}
				TransformationEngine transformationEngine = TransformationEngine
						.forAST(function.getAst());

				AnalysisEngine analysisEngine = transformationEngine
						.getAnalysisEngine();
				@SuppressWarnings("rawtypes")
				ASTNode fTree = transformationEngine
						.getTIRToMcSAFIRWithoutTemp().getTransformedTree();
				Set<String> remainingVars = analysisEngine
						.getTemporaryVariablesRemovalAnalysis()
						.getRemainingVariablesNames();

				System.out.println("\ntamer plus analysis result: \n"
						+ fTree.getPrettyPrinted() + "\n");
				try {
					sb = VrirXmlGen.generateVrir((Function) fTree,
							remainingVars, analysis, currentOutSet, i, size,
							analysisEngine);
					genXML.append(sb);
				} catch (RuntimeException e) {
					System.out
							.println("did not work for " + function.getName());
					System.out.println(fTree.getPrettyPrinted());
					e.printStackTrace();
					System.exit(0);
				}
			}
			funcSet.add(function);
		}
		genXML.append(HelperClass.toXMLTail());

		VrirXmlGen.genModuleXMLTail(genXML);
		System.out.println(" print the generated VRIR in XML format  .\n");
		 System.out.println(genXML);
		try {
			BufferedWriter buffer = Files.newBufferedWriter(
					Paths.get(fileName.split("\\.")[0] + ".vrir"),
					Charset.forName("US-ASCII"));
			buffer.write(genXML.toString());
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String[] getArgs(File file) {
		try {
			BufferedReader reader = Files.newBufferedReader(file.toPath());
			String argStr = reader.readLine();
			if (argStr.charAt(0) != '%') {
				return null;
			}
			argStr = argStr.substring(1).trim();
			String[] args = argStr.split(" ");
			return args;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
}
