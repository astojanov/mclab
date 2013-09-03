package natlab.tame.tamerplus.utils;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.ASTNode;
import ast.Expr;
import ast.Name;
import ast.NameExpr;

import natlab.tame.TamerTool;
import natlab.tame.callgraph.Callgraph;
import natlab.tame.callgraph.StaticFunction;
import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.tamerplus.analysis.AnalysisEngine;
import natlab.tame.tamerplus.analysis.DUChain;
import natlab.tame.tamerplus.analysis.UDChain;
import natlab.tame.tamerplus.analysis.UDDUWeb;
import natlab.tame.tamerplus.transformation.TransformationEngine;
import natlab.tame.tamerplus.utils.TamerPlusUtils;
import natlab.tame.tir.TIRAssignLiteralStmt;
import natlab.tame.tir.TIRCallStmt;
import natlab.tame.tir.TIRNode;
import natlab.tame.tir.analysis.TIRAbstractNodeCaseHandler;
import natlab.tame.valueanalysis.simplematrix.SimpleMatrixValue;
import natlab.tame.valueanalysis.simplematrix.SimpleMatrixValueFactory;
import natlab.toolkits.filehandling.GenericFile;
import natlab.toolkits.path.FileEnvironment;
import natlab.tame.valueanalysis.*;


import natlab.tame.callgraph.SimpleFunctionCollection;
import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.ValueAnalysisPrinter;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValueFactory;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValueFactory;
import natlab.tame.valueanalysis.value.Args;
import natlab.tame.valueanalysis.value.ValueFactory;
import natlab.toolkits.filehandling.GenericFile;
import natlab.toolkits.path.FileEnvironment;

public class RenameTypeConflictVars extends TIRAbstractNodeCaseHandler {
	
	
	public static  SimpleFunctionCollection renameConflictVarsInDifferentWebs(
			SimpleFunctionCollection callgraph,
			List<AggrValue<AdvancedMatrixValue>> inputValues) {
		//create an analysis
		//use this analysis and webs generated by this analysis to rename vars in callgraph
		//return this callgraph
		
		ValueFactory<AggrValue<AdvancedMatrixValue>> factory = new AdvancedMatrixValueFactory();
		ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis = 
				new ValueAnalysis<AggrValue<AdvancedMatrixValue>>(
						callgraph, 
						Args.newInstance(inputValues), 
						factory);
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+analysis.toString());
		System.out.println(callgraph.getPrettyPrinted());
		System.out.println("########################################################################################");
		
		
		//Map(table) that stores every variable name as a key to it's web and type 
		HashMap<String, VariableMetadata> varWebTable ;
		int size = analysis.getNodeList().size();
		
		for (int i=0 ; i<size ;i++){
			
			varWebTable = new HashMap<String, VariableMetadata>();
			StaticFunction function = analysis.getNodeList().get(i).getFunction();
			TransformationEngine transformationEngine = TransformationEngine
	        		.forAST(function.getAst());
	        AnalysisEngine analysisEngine = transformationEngine
	        		.getAnalysisEngine();
	        
	        UDDUWeb web = analysisEngine.getUDDUWebAnalysis();
	        DUChain vDUChain = web.getDUChain();
	        UDChain vUDChain = web.getUDChain();
	        Map<String, HashSet<TIRNode>> varUses;
	        Map<String, Set<TIRNode>> varDefs;
	        HashSet<String> varSet = new HashSet<String>();
	
	        //get all statements
	        LinkedList<TIRNode> allStatements = web.getVisitedStmtsLinkedList();
	        //loop through the list, check if it is a definition in vDUChain
	        //loops through only those variable definitions that are used somewhere and are not return variables
	        for (TIRNode statement : allStatements){
	        	
	        	varUses = vDUChain.getUsesMapForDefinitionStmt(statement);
	        	    	
	        	if (null != varUses){
	        		for (String var : varUses.keySet()){
	        			if (!function.getAst().getOutParamSet().contains(var)){//Do not rename return variable
	        				System.out.println("=="+statement.toString()+" defines "+var+"==");
	        				if (!varWebTable.containsKey(var)){//add entry to the table
	        										//get metaData
	        					varWebTable.put(var, getVariableMetadata(analysis, statement, i, var, web));
	        				}
	        				else{//check for renaming 
	        					
	        				}
	        			}
	        		}
	        	}
	        }	

	        for (TIRNode keys : web.getNodeAndColorForDefinition("p").keySet()){
	        	if (keys instanceof TIRAssignLiteralStmt){
	        		System.out.println(((TIRAssignLiteralStmt)keys).getLHS());
	        		Expr xxx = ((TIRAssignLiteralStmt)keys).getLHS() ;
	        		if (((TIRAssignLiteralStmt)keys).getLHS() instanceof NameExpr){
	        			((NameExpr)xxx).setName(new Name("PP"));
	        		}
	        	}
	        }
	        //System.out.println();
			
		}
		
		analysis.getNodeList().get(0).getFunction().getAst().setName("SIMPLESTa");
		System.out.println("~~~~"+callgraph.getAllFunctions().get(0).getAst().getName()+"~~~~");
		return callgraph;
	}

	public static VariableMetadata getVariableMetadata(ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis, TIRNode statement, int i, String var, UDDUWeb web){
		//Implement here
		return null;
	}
	
	@Override
	public void caseASTNode(ASTNode node) {
		// TODO Auto-generated method stub
		
	}

}
