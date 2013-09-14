package natlab.backends.VRIRGen;

import ast.AssignStmt;
import ast.EmptyStmt;
import ast.List;

public class StmtCaseHandler {
	public static void handleAssignStmt(AssignStmt node, VrirXmlGen gen) {
		toXMLHead("assignstmt", gen);
		gen.appendToPrettyCode("<lhs>\n");
		System.out.println("LHS" + node.getLHS().getClass().toString());
		node.getLHS().analyze(gen);
		gen.appendToPrettyCode("</lhs>\n");
		gen.appendToPrettyCode("<rhs>\n");
		System.out.println("RHS" + node.getRHS().getClass().toString());
		node.getRHS().analyze(gen);
		gen.appendToPrettyCode("</rhs>\n");
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
