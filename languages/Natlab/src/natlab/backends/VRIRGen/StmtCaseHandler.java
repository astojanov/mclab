package natlab.backends.VRIRGen;

import ast.AssignStmt;
import ast.EmptyStmt;
import ast.IfBlock;
import ast.IfStmt;
import ast.List;
import ast.Stmt;
import ast.SwitchStmt;
import ast.WhileStmt;

public class StmtCaseHandler {
	public static void handleAssignStmt(AssignStmt node, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("assignstmt"));
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
		for (IfBlock ifblock : node.getIfBlockList()) {

			gen.appendToPrettyCode(toXMLHead("ifstmt"));
			ifblock.getCondition().analyze(gen);
			gen.appendToPrettyCode("<if>\n");
			gen.appendToPrettyCode(toXMLHead("stmtlist"));
			for (Stmt stmt : ifblock.getStmtList()) {
				System.out.println("statement " + stmt.getClass().toString());
				stmt.analyze(gen);
			}
			gen.appendToPrettyCode(toXMLTail());
			gen.appendToPrettyCode("</if>\n");

		}
		if (node.hasElseBlock()) {
			gen.appendToPrettyCode("<else>\n");
			for (Stmt stmt : node.getElseBlock().getStmtList()) {
				stmt.analyze(gen);
			}
			gen.appendToPrettyCode("</else>\n");
		}
		gen.appendToPrettyCode(toXMLTail());
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
