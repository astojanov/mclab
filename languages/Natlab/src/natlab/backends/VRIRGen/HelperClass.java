package natlab.backends.VRIRGen;

import java.util.ArrayList;

import ast.Function;
import ast.Name;
import ast.NameExpr;
import ast.ParameterizedExpr;
import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.ValueSet;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.components.shape.DimValue;
import natlab.tame.valueanalysis.components.shape.Shape;

public class HelperClass {
	public static PrimitiveClassReference getDataType(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			int graphIndex, Function node, String ID, int i) {

		AdvancedMatrixValue temp = (AdvancedMatrixValue) (analysis
				.getNodeList().get(graphIndex).getAnalysis().getArgs().get(i));

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
			int graphIndex, Function node, Name param, int i) {
		PrimitiveClassReference paramType = HelperClass.getDataType(analysis,
				graphIndex, node, param.getID(), i);
		Shape<AggrValue<AdvancedMatrixValue>> shape = HelperClass.getShape(
				analysis, graphIndex, node, param.getID(), i);
		String complexity = generateComplexityInfo(analysis, graphIndex, node,
				param, i);
		return new VType(shape, paramType, VType.Layout.COLUMN_MAJOR,
				complexity);
	}

	public static VType generateVType(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			int graphIndex, Name node) {
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

	// public static VType genBinExprType(ParameterizedExpr node, VrirXmlGen
	// gen) {
	// if (node.getArg(0) instanceof NameExpr
	// && node.getArg(1) instanceof NameExpr) {
	// AggrValue<?> lhsVal = gen.getAnalysis().getNodeList()
	// .get(gen.getIndex()).getAnalysis().getCurrentOutSet()
	// .get((((NameExpr) node.getArg(0)).getName().getID()))
	// .getSingleton();
	//
	// AggrValue<?> rhsVal = gen.getAnalysis().getNodeList()
	// .get(gen.getIndex()).getAnalysis().getCurrentOutSet()
	// .get((((NameExpr) node.getArg(1)).getName().getID()))
	// .getSingleton();
	// if ((Object) lhsVal instanceof AdvancedMatrixValue
	// && (Object) rhsVal instanceof AdvancedMatrixValue) {
	// // ((AdvancedMatrixValue) (Object) lhsVal);
	//
	// // ((AdvancedMatrixValue) (Object) rhsVal);
	// Shape<AggrValue<AdvancedMatrixValue>> outShape = getOutputShape(
	// ((AdvancedMatrixValue) (Object) lhsVal).getShape(),
	// ((AdvancedMatrixValue) (Object) rhsVal).getShape());
	//
	// }
	//
	// } else {
	//
	// }
	//
	// return null;
	// }

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
