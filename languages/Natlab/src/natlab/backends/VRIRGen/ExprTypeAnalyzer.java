package natlab.backends.VRIRGen;

import java.util.HashMap;

import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.ValueFlowMap;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import analysis.AbstractDepthFirstAnalysis;
import ast.ASTNode;
import ast.Expr;
import ast.NameExpr;
import ast.ParameterizedExpr;

@SuppressWarnings("rawtypes")
public class ExprTypeAnalyzer extends
		AbstractDepthFirstAnalysis<HashMap<Expr, VType>> {
	private HashMap<Expr, VType> exprType;
	private ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis;
	private ValueFlowMap<AggrValue<AdvancedMatrixValue>> currentOutSet;
	private int graphIndex;

	public HashMap<Expr, VType> newInitialFlow() {
		return new HashMap<Expr, VType>();
	}

	public HashMap<Expr, VType> getExprType() {
		return exprType;
	}

	public void setExprType(HashMap<Expr, VType> exprType) {
		this.exprType = exprType;
	}

	public ExprTypeAnalyzer(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			ValueFlowMap<AggrValue<AdvancedMatrixValue>> currentOutSet,
			int graphIndex) {
		this.analysis = analysis;
		this.currentOutSet = currentOutSet;
		exprType = newInitialFlow();
		this.graphIndex = graphIndex;
	}

	public void caseASTNode(ASTNode node) {
		/*
		 * for (int i = 0; i < node.getNumChild(); i++) {
		 * node.getChild(i).analyze(this); }
		 */
	}

	public void caseParameterizedExpr(ParameterizedExpr node) {
		System.out.println("entered parameterized expr");
		if (OperatorMapper.isOperator(node.getVarName())) {
			if (!exprType.containsKey(node)) {
				if (node.getArgList().getNumChild() == 2) {
					handleBinaryExpr(node);
				} else if (node.getArgList().getNumChild() == 1) {
					handleUnaryExpr(node);
				}
			}
		}
	}

	public void handleBinaryExpr(ParameterizedExpr node) {
		if (!exprType.containsKey(node.getArg(0))) {
			node.getArg(0).analyze(this);
		}
		VType lhsType = exprType.get(node.getArg(0));
		if (!exprType.containsKey(node.getArg(1))) {
			node.getArg(1).analyze(this);
		}
		VType rhsType = exprType.get(node.getArg(1));
		VType outputType = getOutputType(lhsType, rhsType);
		exprType.put(node, outputType);
	}

	public void handleUnaryExpr(ParameterizedExpr node) {
		if (!exprType.containsKey(node.getArg(0))) {
			node.getArg(0).analyze(this);
		}
		exprType.put(node, exprType.get(node.getArg(0)));
	}

	public VType getOutputType(VType lhs, VType rhs) {
		
		return null;
	}

	public void caseNameExpr(NameExpr node) {
		VType vtype = HelperClass.generateVType(analysis, getGraphIndex(),
				node.getName());
		exprType.put(node, vtype);
	}

	public int getGraphIndex() {
		return graphIndex;
	}

	public void setGraphIndex(int graphIndex) {
		this.graphIndex = graphIndex;
	}

}
