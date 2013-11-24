package natlab.backends.VRIRGen;

import ast.AssignStmt;
import ast.Expr;
import ast.FPLiteralExpr;
import ast.Function;
import ast.IntLiteralExpr;
import ast.LiteralExpr;
import ast.MatrixExpr;
import ast.Name;
import ast.NameExpr;
import ast.ParameterizedExpr;
import ast.Row;
import natlab.tame.callgraph.StaticFunction;
import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.aggrvalue.CellValue;
import natlab.tame.valueanalysis.components.shape.Shape;
import natlab.tame.valueanalysis.value.Value;

public class HelperClass {
	// public static PrimitiveClassReference getDataType(
	// ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
	// int graphIndex, Function node, String ID, int paramIndx) {
	//
	// AdvancedMatrixValue temp = (AdvancedMatrixValue) (analysis
	// .getNodeList().get(graphIndex).getAnalysis().getArgs()
	// .get(paramIndx));
	//
	// return temp.getMatlabClass();
	//
	// }
	//
	// public static Shape<AggrValue<AdvancedMatrixValue>> getShape(
	// ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
	// int graphIndex, Function node, String ID, int i) {
	// AdvancedMatrixValue temp = (AdvancedMatrixValue) (analysis
	// .getNodeList().get(graphIndex).getAnalysis().getArgs().get(i));
	// return temp.getShape();
	// }
	//
	// public static String generateComplexityInfo(
	// ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
	// int graphIndex, Function node, Name param, int i) {
	// AdvancedMatrixValue temp = (AdvancedMatrixValue) (analysis
	// .getNodeList().get(graphIndex).getAnalysis().getArgs().get(i));
	// return temp.getisComplexInfo().geticType();
	// }

	public static VType generateVType(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			int graphIndex, Function node, Name param, int paramIndx) {

		Value temp = analysis.getNodeList().get(graphIndex).getAnalysis()
				.getArgs().get(paramIndx);
		return generateVType(temp);

	}

	public static VType generateVType(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			int graphIndex, Name node) {

		AggrValue<?> temp = analysis.getNodeList().get(graphIndex)
				.getAnalysis().getCurrentOutSet().get(node.getID())
				.getSingleton();

		return generateVType(temp);

	}

	public static VType generateVType(Value value) {
		if ((Object) value instanceof AdvancedMatrixValue) {
			return new VTypeMatrix(
					(((AdvancedMatrixValue) (Object) value)).getShape(),
					(((AdvancedMatrixValue) (Object) value)).getMatlabClass(),
					VTypeMatrix.Layout.COLUMN_MAJOR,
					(((AdvancedMatrixValue) (Object) value)).getisComplexInfo()
							.geticType());
		} else if ((Object) value instanceof CellValue) {
			VTypeTuple vtypeCell = new VTypeTuple();
			for (Value val : (((CellValue<?>) (Object) value)).getValues()) {
				vtypeCell.addElement(generateVType(val));
			}
			return vtypeCell;
		}
		return null;
	}

	public static boolean isOperator(ParameterizedExpr node) {

		return VrirTypeMapper.contains(node.getVarName());

	}

	public static PrimitiveClassReference getDataType(String name,
			VrirXmlGen gen) {
		AggrValue<AdvancedMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet().get(name)
				.getSingleton();
		if ((Object) val instanceof AdvancedMatrixValue) {

			return (((AdvancedMatrixValue) (Object) val)).getMatlabClass();

		}
		return null;
	}

	public static Shape<AggrValue<AdvancedMatrixValue>> getShape(String name,
			VrirXmlGen gen) {
		AggrValue<AdvancedMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet().get(name)
				.getSingleton();
		if ((Object) val instanceof AdvancedMatrixValue) {

			return (((AdvancedMatrixValue) (Object) val)).getShape();

		}
		return null;
	}

	public static PrimitiveClassReference getDataType(NameExpr node,
			VrirXmlGen gen) {
		AggrValue<AdvancedMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet()
				.get(((NameExpr) node).getName().getID()).getSingleton();
		if ((Object) val instanceof AdvancedMatrixValue) {

			return (((AdvancedMatrixValue) (Object) val)).getMatlabClass();

		} else {
			System.out
					.println("Analyses other than cell value and Advanced matrix value not supported. are currently not supported   ");

		}
		return null;
	}

	public static Shape<AggrValue<AdvancedMatrixValue>> getShape(NameExpr node,
			VrirXmlGen gen) {
		AggrValue<AdvancedMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet()
				.get(((NameExpr) node).getName().getID()).getSingleton();
		if ((Object) val instanceof AdvancedMatrixValue) {

			return (((AdvancedMatrixValue) (Object) val)).getShape();

		}
		return null;
	}

	public static String generateComplexityInfo(NameExpr node, VrirXmlGen gen) {
		AggrValue<AdvancedMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet()
				.get(((NameExpr) node).getName().getID()).getSingleton();
		if ((Object) val instanceof AdvancedMatrixValue) {

			return (((AdvancedMatrixValue) (Object) val)).getisComplexInfo()
					.geticType();

		}
		return null;
	}

	public static String generateComplexityInfo(LiteralExpr lit, VrirXmlGen gen) {
		if (lit instanceof FPLiteralExpr) {
			((IntLiteralExpr) lit).getValue().isImaginary();
		}
		return null;
	}

	// public static String generateComplexityInfo(String name, VrirXmlGen gen)
	// {
	// AggrValue<AdvancedMatrixValue> val = gen.getAnalysis().getNodeList()
	// .get(gen.getIndex()).getAnalysis().getCurrentOutSet().get(name)
	// .getSingleton();
	// if ((Object) val instanceof AdvancedMatrixValue) {
	//
	// return (((AdvancedMatrixValue) (Object) val)).getisComplexInfo()
	// .geticType();
	//
	// }
	// return null;
	// }

	public static VType getBinExprType(ParameterizedExpr node, VrirXmlGen gen) {
		if (node.getParent() instanceof AssignStmt) {
			Expr lhsExpr = ((AssignStmt) node.getParent()).getLHS();
			if (lhsExpr instanceof MatrixExpr) {
				return getLhsType((MatrixExpr) lhsExpr, gen);
			} else if (lhsExpr instanceof NameExpr) {
				return getLhsType((NameExpr) lhsExpr, gen);
			} else if (lhsExpr instanceof ParameterizedExpr) {
				return getLhsType((ParameterizedExpr) lhsExpr, gen);
			}

		} else {

			Name tempName = (Name) gen.getAnalysisEngine()
					.getTemporaryVariablesRemovalAnalysis()
					.getExprToTempVarTable().get(node);

			if (tempName == null) {

				throw new NullPointerException(
						"No equivalent temporary variable exists.");

			}

			AggrValue<?> val = gen.getAnalysis().getNodeList()
					.get(gen.getIndex()).getAnalysis().getCurrentOutSet()
					.get(tempName.getID()).getSingleton();
			return generateVType(val);
		}

		return null;
	}

	public static VType getUnaryExprType(ParameterizedExpr node, VrirXmlGen gen) {
		if (node.getParent() instanceof AssignStmt) {
			Expr lhsExpr = ((AssignStmt) node.getParent()).getLHS();
			if (lhsExpr instanceof MatrixExpr) {
				return getLhsType((MatrixExpr) lhsExpr, gen);
			} else if (lhsExpr instanceof NameExpr) {
				return getLhsType((NameExpr) lhsExpr, gen);
			} else if (lhsExpr instanceof ParameterizedExpr) {
				return getLhsType((ParameterizedExpr) lhsExpr, gen);
			}
		} else {
			Name tempName = (Name) gen.getAnalysisEngine()
					.getTemporaryVariablesRemovalAnalysis()
					.getExprToTempVarTable().get(node);

			AggrValue<?> val = gen.getAnalysis().getNodeList()
					.get(gen.getIndex()).getAnalysis().getCurrentOutSet()
					.get(tempName.getID()).getSingleton();
			return generateVType(val);
		}
		return null;
	}

	public static VType getLhsType(MatrixExpr lhsExpr, VrirXmlGen gen) {

		if (((MatrixExpr) lhsExpr).getNumRow() > 1) {
			System.out
					.println("Multiple return types for binary expressions not supported . ");
			System.exit(0);
			return null;
		}

		for (Row row : ((MatrixExpr) lhsExpr).getRowList()) {
			if (row.getNumElement() > 1) {
				System.out.println("Multiple row case not handled");
				return null;
			} else {
				Expr expr = row.getElement(0);
				if (expr instanceof NameExpr) {
					return getLhsType((NameExpr) expr, gen);

				} else if (expr instanceof ParameterizedExpr) {
					return getLhsType((ParameterizedExpr) expr, gen);
				} else {
					return null;
				}

			}
		}

		return null;
	}

	public static VType getLhsType(NameExpr lhsExpr, VrirXmlGen gen) {
		AggrValue<?> val = gen.getAnalysis().getNodeList().get(gen.getIndex())
				.getAnalysis().getCurrentOutSet()
				.get(lhsExpr.getName().getID()).getSingleton();

		return generateVType(val);
	}

	public static VType getLhsType(ParameterizedExpr lhsExpr, VrirXmlGen gen) {
		AggrValue<?> val = gen.getAnalysis().getNodeList().get(gen.getIndex())
				.getAnalysis().getCurrentOutSet().get(lhsExpr.getVarName())
				.getSingleton();
		return generateVType(val);

	}

	public static VTypeFunction generateFuncType(VrirXmlGen gen, String name) {
		VTypeFunction funcType = new VTypeFunction();
		StaticFunction func = null;
		int i;
		for (i = 0; i < gen.getAnalysis().getNodeList().size(); i++) {
			if (gen.getAnalysis().getNodeList().get(i).getFunction().getName()
					.equalsIgnoreCase(name)) {
				func = gen.getAnalysis().getNodeList().get(i).getFunction();
				break;
			}

		}
		if (func == null) {

			System.err.println("function not found in call graph" + name);
			System.exit(0);
		}

		for (Name nm : func.getAst().getInputParamList()) {
			Symbol sym = gen.getSymbol(nm.getID());
			if (sym == null) {
				VType vtype = HelperClass.generateVType(gen.getAnalysis(), i,
						nm);
				gen.addToSymTab(vtype, nm.getID());
				sym = gen.getSymbol(nm.getID());
			}
			funcType.addInType(sym.getVtype());

		}
		for (Name nm : func.getAst().getOutputParamList()) {
			Symbol sym = gen.getSymbol(nm.getID());
			if (sym == null) {
				VType vtype = HelperClass.generateVType(gen.getAnalysis(), i,
						nm);
				gen.addToSymTab(vtype, nm.getID());
				sym = gen.getSymbol(nm.getID());
			}
			funcType.addOutType(sym.getVtype());

		}

		return funcType;
	}

	public static boolean isVar(VrirXmlGen gen, String name) {
		return gen.getRemainingVars().contains(name);
	}

	public static boolean isVar(VrirXmlGen gen, NameExpr expr) {
		return gen.getRemainingVars().contains(expr.getName().getID());
	}

	public static String toXML(String str) {
		return "<" + str + ">\n";
	}

}
