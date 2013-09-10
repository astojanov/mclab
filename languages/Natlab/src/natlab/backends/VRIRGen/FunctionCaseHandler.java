package natlab.backends.VRIRGen;

import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import ast.Function;
import ast.Name;

public class FunctionCaseHandler {
	public static void handleArgs(Function node, VrirXmlGen gen) {

		ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis = gen
				.getAnalysis();

		for (int i = 0; i < node.getInputParams().getNumChild(); i++) {
			Name param = node.getInputParam(i);

			VType vtype = HelperClass.generateVType(analysis, gen.getIndex(),
					node, param, i);
			System.out.println(vtype.toXML());
		}

	}
}
