package natlab.backends.VRIRGen;

import ast.AssignStmt;
import ast.BreakStmt;
import ast.ContinueStmt;
import ast.ForStmt;
import ast.IfBlock;
import ast.IfStmt;
import ast.NameExpr;
import ast.Stmt;
import ast.WhileStmt;

public class StmtCaseHandler {
	public static void handleAssignStmt(AssignStmt node, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("assignstmt"));
		gen.appendToPrettyCode(HelperClass.toXML("targets"));

		node.getLHS().analyze(gen);
		gen.appendToPrettyCode(HelperClass.toXML("/targets"));
		gen.appendToPrettyCode(HelperClass.toXML("rhs"));

		node.getRHS().analyze(gen);
		gen.appendToPrettyCode(HelperClass.toXML("/rhs"));
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleWhileStmt(WhileStmt node, VrirXmlGen gen) {

		gen.appendToPrettyCode(toXMLHead("whileStmt"));
		gen.appendToPrettyCode(HelperClass.toXML("test"));
		node.getExpr().analyze(gen);
		gen.appendToPrettyCode(HelperClass.toXML("/test"));
		gen.appendToPrettyCode(toXMLHead("stmtList"));
		for (int i = 0; i < node.getStmtList().getNumChild(); i++) {

			node.getStmt(i).analyze(gen);
		}
		gen.appendToPrettyCode(toXMLTail());
		gen.appendToPrettyCode(toXMLTail());

	}

	public static void handleBreakStmt(BreakStmt node, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("BreakStmt"));
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleContinueStmt(ContinueStmt node, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("ContStmt"));
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleForStmt(ForStmt node, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("forstmt"));
		gen.appendToPrettyCode(HelperClass.toXML("domain"));

		node.getAssignStmt().getRHS().analyze(gen);
		gen.appendToPrettyCode(HelperClass.toXML("/domain"));
		gen.appendToPrettyCode(HelperClass.toXML("itervars"));

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

		gen.appendToPrettyCode(HelperClass.toXML("/itervars"));
		gen.appendToPrettyCode(toListXMLHead(false));

		for (Stmt stmt : node.getStmtList()) {
			stmt.analyze(gen);
		}

		gen.appendToPrettyCode(toListXMLTail());
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleIfStmt(IfStmt node, VrirXmlGen gen) {
		for (IfBlock ifblock : node.getIfBlockList()) {

			gen.appendToPrettyCode(toXMLHead("ifstmt"));
			ifblock.getCondition().analyze(gen);
			gen.appendToPrettyCode(HelperClass.toXML("if"));
			gen.appendToPrettyCode(toXMLHead("stmtlist"));
			for (Stmt stmt : ifblock.getStmtList()) {

				stmt.analyze(gen);
			}
			gen.appendToPrettyCode(toXMLTail());
			gen.appendToPrettyCode(HelperClass.toXML("/if"));

		}
		if (node.hasElseBlock()) {
			gen.appendToPrettyCode(HelperClass.toXML("else"));
			for (Stmt stmt : node.getElseBlock().getStmtList()) {
				stmt.analyze(gen);
			}
			gen.appendToPrettyCode(HelperClass.toXML("/else"));
		}
		gen.appendToPrettyCode(toXMLTail());
	}

	public static StringBuffer toListXMLHead(boolean onGpu) {
		StringBuffer buff = new StringBuffer();
		buff.append(HelperClass.toXML("StmtList onGpu="
				+ Boolean.toString(onGpu)));
		buff.append(HelperClass.toXML("stmts"));
		return buff;
	}

	public static StringBuffer toListXMLTail() {
		return new StringBuffer(HelperClass.toXML("/stmts") + toXMLTail());
	}

	public static StringBuffer toXMLHead(String name) {
		return new StringBuffer(HelperClass.toXML("stmt name=\"" + name + "\""));
	}

	public static StringBuffer toXMLTail() {
		return new StringBuffer(HelperClass.toXML("/stmt"));
	}
}
