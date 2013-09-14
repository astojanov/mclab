package natlab.backends.VRIRGen;

import ast.NameExpr;
import ast.PlusExpr;

public class ExprCaseHandler {
	public static void handleNameExpr(NameExpr node, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead(node.getName().getID(),
				gen.getSymbol(node.getName().getID()).getId()));
		gen.appendToPrettyCode(gen.getSymbol(node.getName().getID()).getVtype()
				.toXML());
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handlePlusExpr(PlusExpr node, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("plus"));
		gen.appendToPrettyCode("<vtype name=float64 >\n </vtype>");
		gen.appendToPrettyCode("<rhs>");
		node.getRHS().analyze(gen);
		gen.appendToPrettyCode("</rhs>");
		gen.appendToPrettyCode("<lhs>");
		node.getLHS().analyze(gen);
		gen.appendToPrettyCode("</lhs>");
		gen.appendToPrettyCode(toXMLTail());
	}

	public static StringBuffer toXMLHead(String name) {
		return new StringBuffer("<expr name=\"" + name + "\">\n");
	}

	public static StringBuffer toXMLHead(String name, int id) {
		return new StringBuffer("<expr id=\"" + id + "\" name=\"" + name
				+ "\">\n");
	}

	public static StringBuffer toXMLTail() {
		return new StringBuffer("</expr>\n");
	}
}
