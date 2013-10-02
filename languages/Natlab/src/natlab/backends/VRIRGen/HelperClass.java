package natlab.backends.VRIRGen;

import ast.Function;
import ast.Name;
import ast.ParameterizedExpr;
import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
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
		if (node.getVarName().trim().equalsIgnoreCase("plus")) {
			return true;
		}
		if (node.getVarName().trim().equalsIgnoreCase("minus")) {
			return true;
		}
		if (node.getVarName().trim().equalsIgnoreCase("mtimes")) {
			return true;
		}
		if (node.getVarName().trim().equalsIgnoreCase("times")) {
			return true;
		}
		if (node.getVarName().trim().equalsIgnoreCase("mrdiv")) {
			return true;
		}
		if (node.getVarName().trim().equalsIgnoreCase("rdiv")) {
			return true;
		}
		
		return false;
	}
}
