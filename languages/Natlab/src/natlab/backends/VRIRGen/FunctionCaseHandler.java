package natlab.backends.VRIRGen;

import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import ast.Function;
import ast.Name;

public class FunctionCaseHandler {
	public static void handleHeader(Function node, VrirXmlGen gen) {
		gen.appendToPrettyCode("<function name=\"" + node.getName()
				+ "\">\n <vtype name=\"func\" > \n");
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
			gen.addToSymTab(vtype, param.getID());
			gen.appendToPrettyCode(vtype.toXML());
		}
		gen.appendToPrettyCode("</intypes>\n");
		gen.appendToPrettyCode("<outtypes>\n");
		if (node.getOutputParamList().getNumChild() == 0) {
			gen.appendToPrettyCode("<vtype name=\"Void\">\n</vtype>\n");
		}
		for (int i = 0; i < node.getOutputParams().getNumChild(); i++) {
			Name param = node.getOutputParam(i);

			VType vtype = HelperClass.generateVType(gen.getAnalysis(),
					gen.getIndex(), param);
			/*
			 * if (vtype == null) { System.out.println("vtype null for param  "
			 * + param.getID()); }
			 */
			// VType vtype = HelperClass.generateVType(analysis, gen.getIndex(),
			// node, param, i);
			// TODO: temporary. Should not be null
			if (vtype != null) {
				gen.addToSymTab(vtype, param.getID());
				gen.appendToPrettyCode(vtype.toXML());
			}
		}
		gen.appendToPrettyCode("</outtypes>\n");

	}

	public static void handleTail(Function node, VrirXmlGen gen) {
		gen.appendToPrettyCode("</function>\n");
	}
}
