package natlab.backends.VRIRGen;

import ast.AssignStmt;
import ast.EmptyStmt;
import ast.ForStmt;
import ast.IfBlock;
import ast.IfStmt;
import ast.List;
import ast.NameExpr;
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

	public static void handleForStmt(ForStmt node, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("forstmt"));
		gen.appendToPrettyCode("<domain>");
		node.getAssignStmt().getRHS().analyze(gen);
		gen.appendToPrettyCode("</domain>");
		gen.appendToPrettyCode("<itervars>");
		if (node.getAssignStmt().getLHS() instanceof NameExpr) {
			Symbol sym = gen.getSymbol(((NameExpr) ((Object) node
					.getAssignStmt().getLHS())).getVarName());
			if (sym == null) {
				VType vtype = HelperClass.generateVType(gen.getAnalysis(), gen
						.getIndex(), ((NameExpr) ((Object) node.getAssignStmt()
						.getLHS())).getName());
				gen.addToSymTab(vtype, ((NameExpr) ((Object) node
						.getAssignStmt().getLHS())).getName().getVarName());
			}
			sym = gen.getSymbol(((NameExpr) ((Object) node.getAssignStmt()
					.getLHS())).getVarName());

			gen.appendToPrettyCode(sym.toXML());
		}

		gen.appendToPrettyCode("</itervars>");
		gen.appendToPrettyCode(toXMLHead("stmtlist"));
		for (Stmt stmt : node.getStmtList()) {
			stmt.analyze(gen);
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
