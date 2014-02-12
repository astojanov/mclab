package natlab.backends.vrirGen;

import java.util.ArrayList;

import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import ast.Function;
import ast.Name;

public class FunctionCaseHandler {
	public static void handleHeader(Function node, VrirXmlGen gen) {
		gen.appendToPrettyCode(HelperClass.toXML("function name=\""
				+ node.getName() + "\" "));
		handleParams(node, gen);
		handleArgs(node, gen);

	}

	public static void handleFuncType(Function node, VrirXmlGen gen) {

		ValueAnalysis<AggrValue<AdvancedMatrixValue>> analysis = gen
				.getAnalysis();
		// gen.appendToPrettyCode("<intypes>\n");
		ArrayList<VType> inParamType = new ArrayList<VType>();
		ArrayList<VType> outParamType = new ArrayList<VType>();
		for (int i = 0; i < node.getInputParams().getNumChild(); i++) {

			Name param = node.getInputParam(i);

			VType vtype = HelperClass.generateVType(analysis, gen.getIndex(),
					node, param, i);

			if (vtype == null) {
				throw new NullPointerException("VType is null");
			}
			inParamType.add(vtype);
			gen.addToSymTab(vtype, param.getID());

		}

		for (int i = 0; i < node.getOutputParams().getNumChild(); i++) {
			Name param = node.getOutputParam(i);

			VType vtype = HelperClass.generateVType(gen.getAnalysis(),
					gen.getIndex(), param.getID());

			/*
			 * if (vtype == null) { System.out.println("vtype null for param  "
			 * + param.getID()); }
			 */
			// VType vtype = HelperClass.generateVType(analysis, gen.getIndex(),
			// node, param, i);
			// TODO: temporary. Should not be null
			if (vtype == null) {
				throw new NullPointerException("VType is null");
			}
			inParamType.add(vtype);
			gen.addToSymTab(vtype, param.getID());

		}
		VTypeFunction vtf = new VTypeFunction(inParamType, outParamType);
		gen.appendToPrettyCode(vtf.toXML());

	}

	public static void handleParams(Function node, VrirXmlGen gen) {
		handleFuncType(node, gen);

	}

	public static void handleArgs(Function node, VrirXmlGen gen) {
		gen.appendToPrettyCode(new ArgList(HelperClass.generateArgList(
				node.getInputParamList(), node.getOutputParamList(), gen))
				.toXML());
	}

	public static void handleTail(Function node, VrirXmlGen gen) {
		gen.appendToPrettyCode("</function>\n");
	}
}
