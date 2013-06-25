package natlab.backends.Fortran.codegen_readable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ast.ASTNode;
import ast.List;
import ast.*;
import natlab.tame.tir.TIRCommentStmt;

import nodecases.AbstractNodeCaseHandler;
import natlab.tame.valueanalysis.ValueFlowMap;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.aggrvalue.CellValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.backends.Fortran.codegen_readable.FortranAST_readable.*;
import natlab.backends.Fortran.codegen_readable.astCaseHandler.*;

public class FortranCodeASTGenerator extends AbstractNodeCaseHandler {
	static boolean Debug = false;
	ValueFlowMap<AggrValue<BasicMatrixValue>> currentOutSet;
	public Set<String> remainingVars;
	public FortranMapping fortranMapping;
	public String functionName;
	public ArrayList<String> inArgs;
	public ArrayList<String> outRes;
	public boolean isInSubroutine;
	public HashSet<String> inputHasChanged; // used to back up input argument.
	public HashMap<String, ArrayList<BasicMatrixValue>> forCellArr; // not support nested cell array.
	public ArrayList<String> declaredCell;
	public HashMap<String, BasicMatrixValue> tmpVariables; // generated in Fortran code gen.
	public int indentNum;
	public String standardIndent;
	public int ifWhileForBlockNest;
	public StatementSection stmtSecForIfWhileForBlock;
	public SubProgram subProgram;
	public StringBuffer sb;
	boolean insideArray; // ParameterizedExpr can be array index or function call.
	
	private FortranCodeASTGenerator(
			Function fNode, 
			ValueFlowMap<AggrValue<BasicMatrixValue>> currentOutSet, 
			Set<String> remainingVars) {
		this.currentOutSet = currentOutSet;
		this.remainingVars = remainingVars;
		fortranMapping = new FortranMapping();
		functionName = "";
		inArgs = new ArrayList<String>();
		outRes = new ArrayList<String>();
		isInSubroutine = false;
		inputHasChanged = new HashSet<String>();
		forCellArr = new HashMap<String, ArrayList<BasicMatrixValue>>();
		declaredCell = new ArrayList<String>();
		tmpVariables = new HashMap<String,BasicMatrixValue>();
		indentNum = 0;
		standardIndent = "   ";
		ifWhileForBlockNest = 0;
		stmtSecForIfWhileForBlock = new StatementSection();
		subProgram = new SubProgram();
		sb = new StringBuffer();
		insideArray = false;
		fNode.analyze(this);
	}
	// ******************************ast node override*************************
	@Override
	@SuppressWarnings("rawtypes")
	public void caseASTNode(ASTNode node) {}

	@Override
	public void caseFunction(Function node)	{
		HandleCaseFunction functionStmt = new HandleCaseFunction();
		functionStmt.getFortran(this, node);
    }
	
	@Override
	public void caseAssignStmt(AssignStmt node)	{
		if (ifWhileForBlockNest!=0) {
			FAssignStmt fAssignStmt = new FAssignStmt();
			String indent = "";
			for (int i=0; i<indentNum; i++) {
				indent = indent + this.standardIndent;
			}
			fAssignStmt.setIndent(indent);
			node.getLHS().analyze(this);
			fAssignStmt.setFLHS(sb.toString());
			sb.setLength(0);
			node.getRHS().analyze(this);
			fAssignStmt.setFRHS(sb.toString());
			sb.setLength(0);
			stmtSecForIfWhileForBlock.addStatement(fAssignStmt);			
		}
		else {
			FAssignStmt fAssignStmt = new FAssignStmt();
			String indent = "";
			for (int i=0; i<indentNum; i++) {
				indent = indent + this.standardIndent;
			}
			fAssignStmt.setIndent(indent);
			node.getLHS().analyze(this);
			fAssignStmt.setFLHS(sb.toString());
			sb.setLength(0);
			node.getRHS().analyze(this);
			fAssignStmt.setFRHS(sb.toString());
			sb.setLength(0);
			subProgram.getStatementSection().addStatement(fAssignStmt);			
		}
	}
	
	@Override
	public void caseParameterizedExpr(ParameterizedExpr node) {
		// NameExpr(List), NameExpr is the first child, List is the second child.
		if (Debug) System.out.println("parameterized expr: " 
				+ node+", has "+node.getNumChild()+" children.");
		if (node.getChild(0) instanceof NameExpr) {
			String name = ((NameExpr) node.getChild(0)).getName().getID();
			if (this.remainingVars.contains(name)) {
				if (Debug) System.out.println("this is an array index.");
				node.getChild(0).analyze(this);
				sb.append("(");
				insideArray = true;
				node.getChild(1).analyze(this);
				insideArray = false;
				sb.append(")");
			}
			else {
				if (Debug) System.out.println("this is a function call");
				if (node.getChild(1).getNumChild()==1) {
					if (fortranMapping.isFortranDirectBuiltin(name)) {
						sb.append(" "+fortranMapping
								.getFortranDirectBuiltinMapping(name));
						sb.append("(");
						node.getChild(1).getChild(0).analyze(this);
						sb.append(")");						
					}
					else {
						node.getChild(0).analyze(this);
						sb.append("(");
						node.getChild(1).analyze(this);
						sb.append(")");						
					}
				}
				else if (node.getChild(1).getNumChild()==2) {
					if (fortranMapping.isFortranBinOperator(name)) {
						sb.append("(");
						node.getChild(1).getChild(0).analyze(this);
						sb.append(" "+fortranMapping
								.getFortranBinOpMapping(name)+" ");
						node.getChild(1).getChild(1).analyze(this);
						sb.append(")");						
					}
					else if (fortranMapping.isFortranDirectBuiltin(name)) {
						sb.append(" "+fortranMapping
								.getFortranDirectBuiltinMapping(name));
						sb.append("(");
						node.getChild(1).getChild(0).analyze(this);
						sb.append(" ,");
						node.getChild(1).getChild(1).analyze(this);
						sb.append(")");						
					}
					else {
						node.getChild(0).analyze(this);
						sb.append("(");
						node.getChild(1).getChild(0).analyze(this);
						sb.append(" ,");
						node.getChild(1).getChild(1).analyze(this);
						sb.append(")");
					}
				}
			}
		}
		else {
			System.err.println("can this happen?");
			System.exit(0);
		}
	}
	
	@Override
	public void caseName(Name node) {
		System.err.println(node.getID());
	}
	
	@Override
	public void caseNameExpr(NameExpr node) {
		// System.out.println("nameExpr:" + node.getName().getID());
		if (this.remainingVars.contains(node.getName().getID())) {
			if (Debug) System.out.println(node.getName().getID()+" is a variable.");
			sb.append(node.getName().getID());
		}
		else {
			if (Debug) System.out.println(node.getName().getID()+" is a function name.");
			sb.append(node.getName().getID());
		}
	}
	
	@Override
	public void caseLiteralExpr(LiteralExpr node) {
		if (Debug) System.out.println("liter: "+node.getPrettyPrinted());
		sb.append(node.getPrettyPrinted());
	}
	
	@Override
	public void caseIntLiteralExpr(IntLiteralExpr node) {
		if (Debug) System.out.print("intLiter: "+node.getPrettyPrinted());
		sb.append(node.getPrettyPrinted());
	}
	
	@Override
	public void caseForStmt(ForStmt node) {
		HandleCaseForStmt forStmt = new HandleCaseForStmt();
		if(ifWhileForBlockNest!=0) 
			stmtSecForIfWhileForBlock.addStatement(forStmt.getFortran(this, node));
		else 
			subProgram.getStatementSection().addStatement(forStmt.getFortran(this, node));
    }
	
	@Override
	public void caseRangeExpr(RangeExpr node) {
		if (node.getNumChild()==3) {
			if (Debug) System.out.println("has increment.");
			node.getChild(0).analyze(this);
			sb.append(", ");
			node.getChild(2).analyze(this);
			sb.append(", ");
			node.getChild(1).getChild(0).analyze(this);
		}
	}
	
	@Override
	public void caseIfStmt(IfStmt node) {
		HandleCaseIfStmt ifStmt = new HandleCaseIfStmt();
		if (ifWhileForBlockNest!=0) 
			stmtSecForIfWhileForBlock.addStatement(ifStmt.getFortran(this, node));
		else 
			subProgram.getStatementSection().addStatement(ifStmt.getFortran(this, node));
	}
	
	@Override
	public void caseWhileStmt(WhileStmt node) {
		HandleCaseWhileStmt whileStmt = new HandleCaseWhileStmt();
		if (ifWhileForBlockNest!=0) 
			stmtSecForIfWhileForBlock.addStatement(whileStmt.getFortran(this, node));
		else 
			subProgram.getStatementSection().addStatement(whileStmt.getFortran(this, node));
	}
	
	@Override
	public void caseMatrixExpr(MatrixExpr node) {
		for (int i=0; i<node.getNumChild(); i++) {
			node.getChild(i).analyze(this);
		}
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void caseList(List node) {
		for (int i=0; i<node.getNumChild(); i++) {
			// System.out.println(node.getChild(i));
			if (!(node.getChild(i) instanceof EmptyStmt)) {
				node.getChild(i).analyze(this);
				if (insideArray && i < node.getNumChild()-1) 
					sb.append(", ");
			}
		}
	}
	
	@Override
	public void caseRow(Row node) {
		node.getElementList().analyze(this);
	}
	
	// ******************************helper methods****************************
	public static SubProgram FortranProgramGen(
			Function fNode, 
			ValueFlowMap<AggrValue<BasicMatrixValue>> currentOutSet, 
			Set<String> remainingVars) {
		return new FortranCodeASTGenerator(fNode, currentOutSet, remainingVars).subProgram;
	}

	public void iterateStatements(ast.List<ast.Stmt> stmts) {
		for (ast.Stmt stmt : stmts) {
			if (!(stmt instanceof TIRCommentStmt))
				stmt.analyze(this);
		}
	}
	
	public ValueFlowMap<AggrValue<BasicMatrixValue>> getCurrentOutSet() {
		return currentOutSet;
	}
	
	public BasicMatrixValue getMatrixValue(String variable) {
		if (variable.indexOf("_copy")!=-1) {
			int index = variable.indexOf("_copy");
			String originalVar = variable.substring(0, index);
			return (BasicMatrixValue) currentOutSet.get(originalVar).getSingleton();
		}
		return (BasicMatrixValue) currentOutSet.get(variable).getSingleton();
	}
	
	public boolean isCell(String variable) {
		if (currentOutSet.get(variable).getSingleton() instanceof CellValue) {
			return true;
		}
		else return false;
	}
	
	public boolean hasSingleton(String variable) {
		if (currentOutSet.get(variable).getSingleton()==null) return false;
		return true;
	}
}
