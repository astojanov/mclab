package natlab.backends.VRIRGen;

import ast.AssignStmt;
import ast.Expr;
import ast.Function;
import ast.LiteralExpr;
import ast.MatrixExpr;
import ast.Name;
import ast.NameExpr;
import ast.ParameterizedExpr;
import ast.Row;
import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.tame.valueanalysis.components.shape.Shape;

public class HelperClass {
	public static PrimitiveClassReference getDataType(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			int graphIndex, Function node, String ID, int paramIndx) {

		System.out.println("Function name  "
				+ node.getName()
				+ "   number of arguments "
				+ analysis.getNodeList().get(graphIndex).getAnalysis()
						.getArgs().size() + "  paramIndx     " + paramIndx);
		AdvancedMatrixValue temp = (AdvancedMatrixValue) (analysis
				.getNodeList().get(graphIndex).getAnalysis().getArgs()
				.get(paramIndx));

		return temp.getMatlabClass();

	}

	public static Shape<AggrValue<AdvancedMatrixValue>> getShape(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			int graphIndex, Function node, String ID, int i) {
		AdvancedMatrixValue temp = (AdvancedMatrixValue) (analysis
				.getNodeList().get(graphIndex).getAnalysis().getArgs().get(i));
		return temp.getShape();
	}

	public static String generateComplexityInfo(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			int graphIndex, Function node, Name param, int i) {
		AdvancedMatrixValue temp = (AdvancedMatrixValue) (analysis
				.getNodeList().get(graphIndex).getAnalysis().getArgs().get(i));
		return temp.getisComplexInfo().geticType();
	}

	public static VType generateVType(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			int graphIndex, Function node, Name param, int paramIndx) {
		// System.out.println("paramindx in gen vtype " + paramIndx);
		PrimitiveClassReference paramType = HelperClass.getDataType(analysis,
				graphIndex, node, param.getID(), paramIndx);
		Shape<AggrValue<AdvancedMatrixValue>> shape = HelperClass.getShape(
				analysis, graphIndex, node, param.getID(), paramIndx);
		String complexity = generateComplexityInfo(analysis, graphIndex, node,
				param, paramIndx);
		return new VType(shape, paramType, VType.Layout.COLUMN_MAJOR,
				complexity);
	}

	public static VType generateVType(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			int graphIndex, Name node) {
		// if (analysis.getNodeList().get(graphIndex).getAnalysis()
		// .getCurrentOutSet().get(node.getID()) == null) {
		// System.out.println("node where error  " + node.getID());
		// }
		AggrValue<?> temp = analysis.getNodeList().get(graphIndex)
				.getAnalysis().getCurrentOutSet().get(node.getID())
				.getSingleton();

		if ((Object) temp instanceof AdvancedMatrixValue) {
			return new VType(
					(((AdvancedMatrixValue) (Object) temp)).getShape(),
					(((AdvancedMatrixValue) (Object) temp)).getMatlabClass(),
					VType.Layout.COLUMN_MAJOR,
					(((AdvancedMatrixValue) (Object) temp)).getisComplexInfo()
							.geticType());
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

		return null;
	}

	public static String generateComplexityInfo(String name, VrirXmlGen gen) {
		AggrValue<AdvancedMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet().get(name)
				.getSingleton();
		if ((Object) val instanceof AdvancedMatrixValue) {

			return (((AdvancedMatrixValue) (Object) val)).getisComplexInfo()
					.geticType();

		}
		return null;
	}

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
			PrimitiveClassReference type = HelperClass.getDataType(
					tempName.getID(), gen);
			Shape<AggrValue<AdvancedMatrixValue>> shape = HelperClass.getShape(
					tempName.getID(), gen);
			String complexity = HelperClass.generateComplexityInfo(
					tempName.getID(), gen);
			return new VType(shape, type, VType.Layout.COLUMN_MAJOR, complexity);

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
			PrimitiveClassReference type = HelperClass.getDataType(
					tempName.getID(), gen);
			Shape<AggrValue<AdvancedMatrixValue>> shape = HelperClass.getShape(
					tempName.getID(), gen);
			String complexity = HelperClass.generateComplexityInfo(
					tempName.getID(), gen);
			return new VType(shape, type, VType.Layout.COLUMN_MAJOR, complexity);

		}
		return null;
	}

	public static VType getLhsType(MatrixExpr lhsExpr, VrirXmlGen gen) {

		if (((MatrixExpr) lhsExpr).getNumRow() > 1) {
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
		PrimitiveClassReference type = HelperClass.getDataType(
				(NameExpr) lhsExpr, gen);
		Shape<AggrValue<AdvancedMatrixValue>> shape = HelperClass.getShape(
				(NameExpr) lhsExpr, gen);
		return new VType(shape, type, VType.Layout.COLUMN_MAJOR,
				HelperClass.generateComplexityInfo((NameExpr) lhsExpr, gen));

	}

	public static VType getLhsType(ParameterizedExpr lhsExpr, VrirXmlGen gen) {
		PrimitiveClassReference type = HelperClass.getDataType(
				((ParameterizedExpr) lhsExpr).getVarName(), gen);
		Shape<AggrValue<AdvancedMatrixValue>> shape = HelperClass.getShape(
				((ParameterizedExpr) lhsExpr).getVarName(), gen);

		return new VType(shape, type, VType.Layout.COLUMN_MAJOR,
				HelperClass.generateComplexityInfo(lhsExpr.getVarName(), gen));

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
	// public static Shape<AggrValue<AdvancedMatrixValue>> getOutputShape(
	// Shape<AggrValue<AdvancedMatrixValue>> lhsShape,
	// Shape<AggrValue<AdvancedMatrixValue>> rhsShape) {
	// int i;
	// ArrayList<DimValue> outList = new ArrayList<DimValue>();
	// for (i = 0; i < lhsShape.getDimensions().size()
	// && i < rhsShape.getDimensions().size(); i++) {
	// outList.add(lhsShape.getDimensions().get(i).getIntValue() > rhsShape
	// .getDimensions().get(i).getIntValue() ? lhsShape
	// .getDimensions().get(i) : rhsShape.getDimensions().get(i));
	// }
	// for (int j = i; j < lhsShape.getDimensions().size(); j++) {
	// outList.add(lhsShape.getDimensions().get(j));
	// }
	// for (int j = i; j < rhsShape.getDimensions().size(); j++) {
	// outList.add(rhsShape.getDimensions().get(j));
	// }
	// return new Shape<AggrValue<AdvancedMatrixValue>>(outList);
	// }
	//
	// public static String getOutputType(String lhsType, String rhsType) {
	// return null;
	//
	// }
}
