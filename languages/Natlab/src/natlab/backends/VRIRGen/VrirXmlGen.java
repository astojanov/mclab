package natlab.backends.VRIRGen;

import java.util.Set;

import ast.ASTNode;
import ast.AndExpr;
import ast.ArrayTransposeExpr;
import ast.AssignStmt;
import ast.Attribute;
import ast.BinaryExpr;
import ast.Body;
import ast.BreakStmt;
import ast.CSLExpr;
import ast.CellArrayExpr;
import ast.CellIndexExpr;
import ast.CheckScalarStmt;
import ast.ClassBody;
import ast.ClassDef;
import ast.ClassEvents;
import ast.ColonExpr;
import ast.CompilationUnits;
import ast.ContinueStmt;
import ast.DefaultCaseBlock;
import ast.DotExpr;
import ast.EDivExpr;
import ast.ELDivExpr;
import ast.EPowExpr;
import ast.EQExpr;
import ast.ETimesExpr;
import ast.ElseBlock;
import ast.EmptyProgram;
import ast.EmptyStmt;
import ast.EndCallExpr;
import ast.EndExpr;
import ast.Event;
import ast.Expr;
import ast.ExprStmt;
import ast.FPLiteralExpr;
import ast.ForStmt;
import ast.Function;
import ast.FunctionHandleExpr;
import ast.FunctionList;
import ast.FunctionOrSignatureOrPropertyAccessOrStmt;
import ast.GEExpr;
import ast.GTExpr;
import ast.GlobalStmt;
import ast.HelpComment;
import ast.IfBlock;
import ast.IfStmt;
import ast.IntLiteralExpr;
import ast.LEExpr;
import ast.LTExpr;
import ast.LValueExpr;
import ast.LambdaExpr;
import ast.List;
import ast.LiteralExpr;
import ast.MDivExpr;
import ast.MLDivExpr;
import ast.MPowExpr;
import ast.MTimesExpr;
import ast.MTransposeExpr;
import ast.MatrixExpr;
import ast.Methods;
import ast.MinusExpr;
import ast.MultiLineHelpComment;
import ast.NEExpr;
import ast.Name;
import ast.NameExpr;
import ast.NotExpr;
import ast.OneLineHelpComment;
import ast.OrExpr;
import ast.ParameterizedExpr;
import ast.PersistentStmt;
import ast.PlusExpr;
import ast.Program;
import ast.Properties;
import ast.Property;
import ast.PropertyAccess;
import ast.RangeExpr;
import ast.ReturnStmt;
import ast.Row;
import ast.Script;
import ast.ShellCommandStmt;
import ast.ShortCircuitAndExpr;
import ast.ShortCircuitOrExpr;
import ast.Signature;
import ast.Stmt;
import ast.StringLiteralExpr;
import ast.SuperClass;
import ast.SuperClassMethodExpr;
import ast.SwitchCaseBlock;
import ast.SwitchStmt;
import ast.TryStmt;
import ast.UMinusExpr;
import ast.UPlusExpr;
import ast.UnaryExpr;
import ast.WhileStmt;
import natlab.tame.tamerplus.analysis.AnalysisEngine;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.ValueFlowMap;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import nodecases.natlab.NatlabAbstractNodeCaseHandler;

public class VrirXmlGen extends NatlabAbstractNodeCaseHandler {

	private StringBuffer prettyPrintedCode = null;
	// private StringBuffer bodyCode;
	private SymbolTable symTab;
	private Set<String> remainingVars;
	private ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis;
	private ValueFlowMap<AggrValue<AdvancedMatrixValue>> currentOutSet;
	private int size;
	private int index;
	// private int indent = 1;
	private AnalysisEngine analysisEngine;

	// public int getIndent() {
	// return indent;
	// }
	//
	// public void setIndent(int indent) {
	// this.indent = indent;
	// }
	//
	// public void incIndent() {
	// indent++;
	// }
	//
	// public void decIndent() {
	// indent--;
	// }

	VrirXmlGen(Function functionNode, Set<String> remainVars,
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			ValueFlowMap<AggrValue<AdvancedMatrixValue>> currentOutSet,
			int size, int index, String moduleName,
			AnalysisEngine analysisEngine) {
		prettyPrintedCode = new StringBuffer();
		remainingVars = remainVars;
		this.analysis = analysis;
		this.currentOutSet = currentOutSet;
		this.size = size;
		this.index = index;
		symTab = new SymbolTable();
		// indent = 0;
		this.analysisEngine = analysisEngine;
		genModuleXMLHead(moduleName);
		functionNode.analyze(this);
		genModuleXMLTail();
	}

	public void genModuleXMLHead(String moduleName) {
		this.appendToPrettyCode("<module name=\"" + moduleName + "\">\n");
	}

	public void genModuleXMLTail() {
		this.appendToPrettyCode("</module>\n");
	}

	public void addToSymTab(VType vtype, String name) {
		symTab.putSymbol(vtype, name);
	}

	public Symbol getSymbol(String name) {
		return symTab.getSymbol(name);
	}

	public void appendToPrettyCode(StringBuffer buff) {
		prettyPrintedCode.append(buff);
	}

	public void appendToPrettyCode(String buff) {
		prettyPrintedCode.append(buff);
	}

	public Set<String> getRemainingVars() {
		return remainingVars;
	}

	public void setRemainingVars(Set<String> remainingVars) {
		this.remainingVars = remainingVars;
	}

	public ValueAnalysis<AggrValue<AdvancedMatrixValue>> getAnalysis() {
		return analysis;
	}

	public void setAnalysis(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis) {
		this.analysis = analysis;
	}

	public ValueFlowMap<AggrValue<AdvancedMatrixValue>> getCurrentOutSet() {
		return currentOutSet;
	}

	public void setCurrentOutSet(
			ValueFlowMap<AggrValue<AdvancedMatrixValue>> currentOutSet) {
		this.currentOutSet = currentOutSet;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setPrettyPrintedCode(StringBuffer prettyPrintedCode) {
		this.prettyPrintedCode = prettyPrintedCode;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void caseASTNode(ASTNode node) {

	}

	@Override
	@SuppressWarnings("rawtypes")
	public void caseList(List node) {

		caseASTNode(node);
	}

	@Override
	public void caseProgram(Program node) {
		caseASTNode(node);
	}

	@Override
	public void caseBody(Body node) {
		caseASTNode(node);
	}

	@Override
	public void caseFunctionOrSignatureOrPropertyAccessOrStmt(
			FunctionOrSignatureOrPropertyAccessOrStmt node) {
		caseASTNode(node);
	}

	@Override
	public void caseHelpComment(HelpComment node) {
		caseASTNode(node);
	}

	@Override
	public void caseExpr(Expr node) {
		caseASTNode(node);
	}

	public void caseCompilationUnits(CompilationUnits node) {
		caseASTNode(node);
	}

	public void caseAttribute(Attribute node) {
		caseASTNode(node);
	}

	public void caseSuperClass(SuperClass node) {
		caseASTNode(node);
	}

	public void caseProperty(Property node) {
		caseASTNode(node);
	}

	public void caseEvent(Event node) {
		caseASTNode(node);
	}

	public void caseName(Name node) {
		caseASTNode(node);
	}

	public void caseSwitchCaseBlock(SwitchCaseBlock node) {

		caseASTNode(node);
	}

	public void caseDefaultCaseBlock(DefaultCaseBlock node) {
		caseASTNode(node);
	}

	public void caseIfBlock(IfBlock node) {
		caseASTNode(node);
	}

	public void caseElseBlock(ElseBlock node) {
		caseASTNode(node);
	}

	public void caseRow(Row node) {
		caseASTNode(node);
	}

	public void caseClassBody(ClassBody node) {
		caseBody(node);
	}

	public void caseStmt(Stmt node) {
		caseFunctionOrSignatureOrPropertyAccessOrStmt(node);
	}

	public void caseLValueExpr(LValueExpr node) {

		caseExpr(node);
	}

	public void caseLiteralExpr(LiteralExpr node) {
		caseExpr(node);
	}

	public void caseUnaryExpr(UnaryExpr node) {
		caseExpr(node);
	}

	public void caseBinaryExpr(BinaryExpr node) {
		caseExpr(node);
	}

	public void caseScript(Script node) {
		caseProgram(node);
	}

	public void caseFunctionList(FunctionList node) {
		caseProgram(node);
	}

	public void caseEmptyProgram(EmptyProgram node) {
		caseProgram(node);
	}

	public void caseClassDef(ClassDef node) {
		caseProgram(node);
	}

	public void caseProperties(Properties node) {
		caseClassBody(node);
	}

	public void caseMethods(Methods node) {
	}

	public void caseSignature(Signature node) {
		caseFunctionOrSignatureOrPropertyAccessOrStmt(node);
	}

	public void casePropertyAccess(PropertyAccess node) {
		caseFunctionOrSignatureOrPropertyAccessOrStmt(node);
	}

	public void caseClassEvents(ClassEvents node) {
		caseClassBody(node);
	}

	public void caseFunction(Function node) {
		// caseFunctionOrSignatureOrPropertyAccessOrStmt(node);
		FunctionCaseHandler.handleHeader(node, this);

		for (Stmt stmt : node.getStmts()) {

			stmt.analyze(this);
		}
		this.appendToPrettyCode(symTab.toXML());

		FunctionCaseHandler.handleTail(node, this);

	}

	public void caseOneLineHelpComment(OneLineHelpComment node) {
		caseHelpComment(node);
	}

	public void caseMultiLineHelpComment(MultiLineHelpComment node) {
		caseHelpComment(node);
	}

	public void caseExprStmt(ExprStmt node) {
		caseStmt(node);
	}

	public void caseAssignStmt(AssignStmt node) {

		StmtCaseHandler.handleAssignStmt(node, this);

		// caseStmt(node);

	}

	public void caseGlobalStmt(GlobalStmt node) {
		caseStmt(node);
	}

	public void casePersistentStmt(PersistentStmt node) {
		caseStmt(node);
	}

	public void caseShellCommandStmt(ShellCommandStmt node) {
		caseStmt(node);
	}

	public void caseBreakStmt(BreakStmt node) {
		StmtCaseHandler.handleBreakStmt(node, this);
		caseStmt(node);
	}

	public void caseContinueStmt(ContinueStmt node) {
		StmtCaseHandler.handleContinueStmt(node, this);
		caseStmt(node);
	}

	public void caseReturnStmt(ReturnStmt node) {

		// TODO: Ask rahul
		caseStmt(node);
	}

	public void caseEmptyStmt(EmptyStmt node) {
		caseStmt(node);
	}

	public void caseForStmt(ForStmt node) {
		StmtCaseHandler.handleForStmt(node, this);
		caseStmt(node);
	}

	public void caseWhileStmt(WhileStmt node) {

		StmtCaseHandler.handleWhileStmt(node, this);
		caseStmt(node);
	}

	public void caseTryStmt(TryStmt node) {
		caseStmt(node);
	}

	public void caseSwitchStmt(SwitchStmt node) {

		caseStmt(node);
	}

	public void caseIfStmt(IfStmt node) {
		StmtCaseHandler.handleIfStmt(node, this);

		caseStmt(node);
	}

	public void caseRangeExpr(RangeExpr node) {
		ExprCaseHandler.handleRangeExpr(node, this);
		caseExpr(node);
	}

	public void caseColonExpr(ColonExpr node) {
		caseExpr(node);
	}

	public void caseEndExpr(EndExpr node) {
		caseExpr(node);
	}

	public void caseNameExpr(NameExpr node) {
		VType vtype = HelperClass.generateVType(analysis, getIndex(),
				node.getName());
		if (!symTab.contains(node.getName().getID())) {
			symTab.putSymbol(vtype, node.getName().getID());
		}
		ExprCaseHandler.handleNameExpr(node, this);
	}

	public void caseParameterizedExpr(ParameterizedExpr node) {

		// Array Index Expression
		if (remainingVars.contains(node.getVarName())) {
			ExprCaseHandler.handleArrayIndexExpr(node, this);
		} else {

			// Operator
			if (OperatorMapper.isOperator(node.getVarName())) {
				// Binary operator
				ExprCaseHandler.handleOpExpr(node, this,
						OperatorMapper.get(node.getVarName()));
				// ExprCaseHandler.handlePlusExpr(node, this);

			}
			// Function Call
			else {
				ExprCaseHandler.handleFunCallExpr(node, this);
			}

		}
		// caseLValueExpr(node);
	}

	public SymbolTable getSymTab() {
		return symTab;
	}

	public void setSymTab(SymbolTable symTab) {
		this.symTab = symTab;
	}

	public void caseCellIndexExpr(CellIndexExpr node) {
		caseLValueExpr(node);
	}

	public void caseDotExpr(DotExpr node) {
		caseLValueExpr(node);
	}

	public void caseMatrixExpr(MatrixExpr node) {
		caseLValueExpr(node);
	}

	public void caseCellArrayExpr(CellArrayExpr node) {
		caseExpr(node);
	}

	public void caseSuperClassMethodExpr(SuperClassMethodExpr node) {
		caseExpr(node);
	}

	public void caseIntLiteralExpr(IntLiteralExpr node) {

		ExprCaseHandler.handleIntLiteralExpr(node, this);
		caseLiteralExpr(node);
	}

	public AnalysisEngine getAnalysisEngine() {
		return analysisEngine;
	}

	public void setAnalysisEngine(AnalysisEngine analysisEngine) {
		this.analysisEngine = analysisEngine;
	}

	public void caseFPLiteralExpr(FPLiteralExpr node) {
		ExprCaseHandler.handleFpLiteralExpr(node, this);
		caseLiteralExpr(node);
	}

	public void caseStringLiteralExpr(StringLiteralExpr node) {
		ExprCaseHandler.handleStringLiteralExpr(node, this);
		caseLiteralExpr(node);
	}

	// public void caseUMinusExpr(UMinusExpr node) {
	// caseUnaryExpr(node);
	// }
	//
	// public void caseUPlusExpr(UPlusExpr node) {
	// caseUnaryExpr(node);
	// }
	//
	// public void caseNotExpr(NotExpr node) {
	// caseUnaryExpr(node);
	// }
	//
	// public void caseMTransposeExpr(MTransposeExpr node) {
	// caseUnaryExpr(node);
	// }
	//
	// public void caseArrayTransposeExpr(ArrayTransposeExpr node) {
	// caseUnaryExpr(node);
	// }
	// public void casePlusExpr(PlusExpr node) {
	//
	// }
	//
	// public void caseMinusExpr(MinusExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseMTimesExpr(MTimesExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseMDivExpr(MDivExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseMLDivExpr(MLDivExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseMPowExpr(MPowExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseETimesExpr(ETimesExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseEDivExpr(EDivExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseELDivExpr(ELDivExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseEPowExpr(EPowExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseAndExpr(AndExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseOrExpr(OrExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseShortCircuitAndExpr(ShortCircuitAndExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseShortCircuitOrExpr(ShortCircuitOrExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseLTExpr(LTExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseGTExpr(GTExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseLEExpr(LEExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseGEExpr(GEExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseEQExpr(EQExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseNEExpr(NEExpr node) {
	// caseBinaryExpr(node);
	// }
	//
	// public void caseFunctionHandleExpr(FunctionHandleExpr node) {
	// caseExpr(node);
	// }
	//
	// public void caseLambdaExpr(LambdaExpr node) {
	// caseExpr(node);
	// }
	//
	// public void caseCSLExpr(CSLExpr node) {
	// caseNameExpr(node);
	// }
	//
	// public void caseEndCallExpr(EndCallExpr node) {
	// caseExpr(node);
	// }
	//
	// public void caseCheckScalarStmt(CheckScalarStmt node) {
	// caseStmt(node);
	// }

	public static StringBuffer generateVrir(Function functionNode,
			Set<String> remainingVars,
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			ValueFlowMap<AggrValue<AdvancedMatrixValue>> currentOutSet,
			int index, int size, String moduleName,
			AnalysisEngine analysisEngine) {
		return (new VrirXmlGen(functionNode, remainingVars, analysis,
				currentOutSet, size, index, moduleName, analysisEngine))
				.getPrettyPrintedCode();
	}

	public StringBuffer getPrettyPrintedCode() {
		return prettyPrintedCode;
	}

}