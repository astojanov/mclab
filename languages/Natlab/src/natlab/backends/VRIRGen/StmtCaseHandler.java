package natlab.backends.VRIRGen;

import ast.AssignStmt;
import ast.EmptyStmt;
import ast.IfStmt;
import ast.List;
import ast.WhileStmt;

public class StmtCaseHandler {
	public static void handleAssignStmt(AssignStmt node, VrirXmlGen gen) {
		toXMLHead("assignstmt");
		gen.appendToPrettyCode("<lhs>\n");

		node.getLHS().analyze(gen);
		gen.appendToPrettyCode("</lhs>\n");
		gen.appendToPrettyCode("<rhs>\n");

		node.getRHS().analyze(gen);
		gen.appendToPrettyCode("</rhs>\n");
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleWhileStmt(WhileStmt node, VrirXmlGen gen) {

		gen.appendToPrettyCode(toXMLHead("whileStmt"));
		node.getExpr().analyze(gen);
		gen.appendToPrettyCode(toXMLHead("stmtList"));
		for (int i = 0; i < node.getStmtList().getNumChild(); i++) {

			node.getStmt(i).analyze(gen);
		}
		gen.appendToPrettyCode(toXMLTail());
		gen.appendToPrettyCode(toXMLTail());

	}

	public static void handleIfStmt(IfStmt node, VrirXmlGen gen) {
		System.out.println("if block"
				+ node.getIfBlock(0).getClass().toString());
		
	}

	public static void handleList(List node, VrirXmlGen gen) {

	}

	public static StringBuffer toXMLHead(String name) {
		return new StringBuffer("<stmt name=\"" + name + "\">\n");
	}

	public static StringBuffer toXMLTail() {
		return new StringBuffer("</stmt>\n");
	}
}
