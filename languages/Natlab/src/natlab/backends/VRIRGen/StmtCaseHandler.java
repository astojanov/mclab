package natlab.backends.VRIRGen;

import ast.AssignStmt;

public class StmtCaseHandler {
	public static void handleAssgnStmt(AssignStmt node, VrirXmlGen gen) {
		toXMLHead("assgnstmt", gen);
		node.getLHS().analyze(gen);
		node.getRHS().analyze(gen);
		toXMLTail(gen);
	}

	public static void toXMLHead(String name, VrirXmlGen gen) {
		gen.appendToPrettyCode("<stmt name=\"" + name + "\">\n");
	}

	public static void toXMLTail(VrirXmlGen gen) {
		gen.appendToPrettyCode("</stmt>\n");
	}
}
