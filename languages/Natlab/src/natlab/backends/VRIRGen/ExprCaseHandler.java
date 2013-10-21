package natlab.backends.VRIRGen;

import ast.Expr;
import ast.FPLiteralExpr;
import ast.IntLiteralExpr;
import ast.NameExpr;
import ast.ParameterizedExpr;
import ast.RangeExpr;
import ast.StringLiteralExpr;

public class ExprCaseHandler {
	public static void handleNameExpr(NameExpr node, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead(node.getName().getID(),
				gen.getSymbol(node.getName().getID()).getId(), "id"));
		gen.appendToPrettyCode(gen.getSymbol(node.getName().getID()).getVtype()
				.toXML());
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleOpExpr(ParameterizedExpr node, VrirXmlGen gen,
			String name) {
		if (node.getArgList().getNumChild() == 2) {
			handleBinExpr(node, gen, name);
		}
		if (node.getArgList().getNumChild() == 1) {
			handleUnaryExpr(node, gen, name);

		}
	}

	public static void handleUnaryExpr(ParameterizedExpr node, VrirXmlGen gen,
			String name) {
		gen.appendToPrettyCode(toXMLHead(name));
		// TODO : Get unary expression type
		gen.appendToPrettyCode(HelperClass.getUnaryExprType(node, gen).toXML());
		node.getArg(0).analyze(gen);
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleBinExpr(ParameterizedExpr node, VrirXmlGen gen,
			String name) {

		gen.appendToPrettyCode(toXMLHead(name));

		gen.appendToPrettyCode(HelperClass.getBinExprType(node, gen).toXML());
		gen.appendToPrettyCode("<rhs>\n");
		node.getArg(1).analyze(gen);

		gen.appendToPrettyCode("</rhs>\n");
		gen.appendToPrettyCode("<lhs>\n");
		node.getArg(0).analyze(gen);
		gen.appendToPrettyCode("</lhs>\n");
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleRangeExpr(RangeExpr node, VrirXmlGen gen) {

		gen.appendToPrettyCode(HelperClass.toXML("start"));
		node.getLower().analyze(gen);
		gen.appendToPrettyCode(HelperClass.toXML("/start"));

		if (node.getIncr() != null) {
			gen.appendToPrettyCode(HelperClass.toXML("step"));
			node.getIncr().analyze(gen);
			gen.appendToPrettyCode(HelperClass.toXML("/step"));
		}
		gen.appendToPrettyCode(HelperClass.toXML("stop"));
		node.getUpper().analyze(gen);
		gen.appendToPrettyCode(HelperClass.toXML("/stop"));
	}

	public static void handleIntLiteralExpr(IntLiteralExpr expr, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("const", expr.getValue().getValue()
				.intValue(), "value"));
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleFpLiteralExpr(FPLiteralExpr expr, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("const", expr.getValue().getValue()
				.toString(), "value"));
		// TODO: 2 types of complex expressions : complex and real . Make
		// changes for that.

		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleFunCallExpr(ParameterizedExpr expr, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("FuncallExpr"));
		gen.appendToPrettyCode(HelperClass.toXML("name"));
		expr.getChild(0).analyze(gen);
		gen.appendToPrettyCode(HelperClass.toXML("/name"));
		gen.appendToPrettyCode(HelperClass.toXML("args"));
		for (Expr args : expr.getArgList()) {
			args.analyze(gen);
		}
		gen.appendToPrettyCode(HelperClass.toXML("/args"));
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleArrayIndexExpr(ParameterizedExpr expr,
			VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("ArrayIndexExpr"));
		gen.appendToPrettyCode(HelperClass.toXML("base"));
		expr.getChild(0).analyze(gen);
		gen.appendToPrettyCode(HelperClass.toXML("/base"));
		gen.appendToPrettyCode(HelperClass.toXML("indices"));
		// TODO : change to handle index expression after talking with Rahul
		for (Expr args : expr.getArgList()) {
			args.analyze(gen);
		}
		gen.appendToPrettyCode(HelperClass.toXML("/indices"));
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleStringLiteralExpr(StringLiteralExpr expr,
			VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("const", expr.getValue(), "value"));
		gen.appendToPrettyCode(toXMLTail());
	}

	public static StringBuffer toXMLHead(String name) {
		return new StringBuffer("<expr name=\"" + name + "\">\n");
	}

	public static StringBuffer toXMLHead(String name, int id, String field) {
		return new StringBuffer("<expr " + field + "=\"" + id + "\" name =\""
				+ name + "\">\n");
	}

	public static StringBuffer toXMLHead(String name, String id, String field) {
		return new StringBuffer("<expr " + field + "=\"" + id + "\" name =\""
				+ name + "\">\n");
	}

	public static StringBuffer toXMLTail() {
		return new StringBuffer("</expr>\n");
	}

}