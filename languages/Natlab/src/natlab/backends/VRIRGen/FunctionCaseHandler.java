package natlab.backends.VRIRGen;

import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import ast.Function;
import ast.Name;

public class FunctionCaseHandler {
	public static void handleHeader(Function node, VrirXmlGen gen) {
		gen.appendToPrettyCode("<function name=\"" + node.getName()
				+ "\"\n <vtype name=\"func\"> \n");
		handleArgs(node, gen);
		gen.appendToPrettyCode("</vtype>\n");
	}

	public static void handleArgs(Function node, VrirXmlGen gen) {

		ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis = gen
				.getAnalysis();
		gen.appendToPrettyCode("<intypes>\n");

		for (int i = 0; i < node.getInputParams().getNumChild(); i++) {
			Name param = node.getInputParam(i);

			VType vtype = HelperClass.generateVType(analysis, gen.getIndex(),
					node, param, i);
			gen.appendToPrettyCode(vtype.toXML());
		}
		gen.appendToPrettyCode("</intypes>\n");
		gen.appendToPrettyCode("<outtypes>\n");
		for (int i = 0; i < node.getOutputParams().getNumChild(); i++) {
			Name param = node.getOutputParam(i);
			VType vtype = HelperClass.generateVType(analysis, gen.getIndex(),
					node, param, i);
			gen.appendToPrettyCode(vtype.toXML());
		}
		gen.appendToPrettyCode("</outtypes>\n");

	}

	public static void handleTail(Function node, VrirXmlGen gen) {
		gen.appendToPrettyCode("</function>\n");
	}
}
