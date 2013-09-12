package natlab.backends.VRIRGen;

import ast.AssignStmt;
import ast.EmptyStmt;
import ast.List;

public class StmtCaseHandler {
	public static void handleAssignStmt(AssignStmt node, VrirXmlGen gen) {
		toXMLHead("assgnstmt", gen);
		node.getLHS().analyze(gen);
		node.getRHS().analyze(gen);
		toXMLTail(gen);
	}

	public static void handleList(List node, VrirXmlGen gen) {
		toXMLHead("stmtlist", gen);
		for (int i = 0; i < node.getNumChild(); i++) {
			if (!(node.getChild(i) instanceof EmptyStmt)) {
				node.getChild(i).analyze(gen);
			}
		}
		toXMLTail(gen);
	}

	public static void toXMLHead(String name, VrirXmlGen gen) {
		gen.appendToPrettyCode("<stmt name=\"" + name + "\">\n");
	}

	public static void toXMLTail(VrirXmlGen gen) {
		gen.appendToPrettyCode("</stmt>\n");
	}
}
