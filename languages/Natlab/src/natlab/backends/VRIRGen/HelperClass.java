package natlab.backends.VRIRGen;

import ast.Function;
import ast.Name;
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

	public static VType generateVType(
			ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis,
			int graphIndex, Function node, Name param, int i) {
		PrimitiveClassReference paramType = HelperClass.getDataType(analysis,
				graphIndex, node, param.getID(), i);
		Shape<AggrValue<AdvancedMatrixValue>> shape = HelperClass.getShape(
				analysis, graphIndex, node, param.getID(), i);
		return new VType(shape, paramType, VType.Layout.COLUMN_MAJOR);
	}
}
