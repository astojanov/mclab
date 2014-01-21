package natlab.backends.VRIRGen;

import ast.Expr;
import ast.FPLiteralExpr;
import ast.IntLiteralExpr;
import ast.MatrixExpr;
import ast.NameExpr;
import ast.ParameterizedExpr;
import ast.RangeExpr;
import ast.StringLiteralExpr;

public class ExprCaseHandler {
	public static void handleParameterizedExpr(ParameterizedExpr node,
			VrirXmlGen gen) {
		if (gen.getRemainingVars().contains(node.getVarName())) {

			ExprCaseHandler.handleIndexExpr(node, gen);
		} else {

			// Operator
			if (OperatorMapper.isOperator(node.getVarName())) {
				// Binary operator
				ExprCaseHandler.handleOpExpr(node, gen,
						OperatorMapper.get(node.getVarName()));
				// ExprCaseHandler.handlePlusExpr(node, this);

			}
			// Function Call
			else {
				ExprCaseHandler.handleFunCallExpr(node, gen);

			}

		}
	}

	public static void handleNameExpr(NameExpr node, VrirXmlGen gen) {
		if (HelperClass.isVar(gen, node.getName().getID())) {

			if (!gen.getSymTab().contains(node.getName().getID())) {

				VType vtype = HelperClass.generateVType(gen.getAnalysis(),
						gen.getIndex(), node.getName());
				gen.getSymTab().putSymbol(vtype, node.getName().getID());
			}
			if (gen.getSymbol(node.getName().getID()) != null) {
				gen.appendToPrettyCode(toXMLHead("name",
						gen.getSymbol(node.getName().getID()).getId(), "id"));

			}
		}

		else {
			handleFunCallExpr(node, gen);
		}

		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleOpExpr(ParameterizedExpr node, VrirXmlGen gen,
			String name) {
		if (node.getArgList().getNumChild() == 2) {
			handleBinExpr(node, gen, name);
		} else if (node.getArgList().getNumChild() == 1) {
			handleUnaryExpr(node, gen, name);

		}
	}

	public static void handleMatrixExpr(MatrixExpr node, VrirXmlGen gen) {
		if (node.getRows().getNumChild() > 1) {
			System.out.println("Multiple rows now supported");
			System.exit(1);
		}
		// for a single element
		if (node.getRow(0).getElementList().getNumChild() == 1) {
			node.getRow(0).getElement(0).analyze(gen);

		} else {
			// tuple type for multiple elements

			// gen.appendToPrettyCode(toXMLHead("tuple", "1", "ndims"));
			gen.appendToPrettyCode(toXMLHead("tuple"));
			gen.appendToPrettyCode(HelperClass.toXML("elems"));
			for (Expr expr : node.getRow(0).getElementList()) {

				expr.analyze(gen);

			}
			gen.appendToPrettyCode(HelperClass.toXML("/elems"));
			gen.appendToPrettyCode(toXMLTail());
		}
	}

	public static void handleUnaryExpr(ParameterizedExpr node, VrirXmlGen gen,
			String name) {
		gen.appendToPrettyCode(toXMLHead(name));
		// TODO : Get unary expression type
		gen.appendToPrettyCode(HelperClass.getExprType(node, gen).toXML());
		gen.appendToPrettyCode(HelperClass.toXML("base"));
		node.getArg(0).analyze(gen);
		gen.appendToPrettyCode(HelperClass.toXML("/base"));
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleBinExpr(ParameterizedExpr node, VrirXmlGen gen,
			String name) {

		gen.appendToPrettyCode(toXMLHead(name));

		gen.appendToPrettyCode(HelperClass.getExprType(node, gen).toXML());
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

		if (node.hasIncr()) {

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
		VType vt = HelperClass.getExprType(expr, gen);
		if (vt == null) {
			throw new NullPointerException(
					"Could not generate vtype for const expression");
		}
		gen.appendToPrettyCode(vt.toXML());
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleFpLiteralExpr(FPLiteralExpr expr, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("const", expr.getValue().getValue()
				.toString(), "value"));
		VType vt = HelperClass.getExprType(expr, gen);
		if (vt == null) {
			throw new NullPointerException(
					"Could not generate vtype for const expression");
		}
		gen.appendToPrettyCode(vt.toXML());
		// TODO: 2 types of complex expressions : complex and real . Make
		// changes for that.

		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleFunCallExpr(ParameterizedExpr expr, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("fncall", expr.getVarName(), "fnname"));

		gen.appendToPrettyCode(HelperClass.toXML("args"));
		for (Expr args : expr.getArgList()) {
			args.analyze(gen);
		}
		gen.appendToPrettyCode(HelperClass.toXML("/args"));
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleFunCallExpr(NameExpr node, VrirXmlGen gen) {

		gen.appendToPrettyCode(toXMLHead(node.getName().getID(),
				gen.getSymbol(node.getName().getID()).getId(), "id"));
	}

	// TODO: Revisit . Problem with indices
	public static void handleIndexExpr(ParameterizedExpr expr, VrirXmlGen gen) {
		Symbol sym;

		if ((sym = gen.getSymbol(expr.getVarName())) == null) {
			VType vtype = HelperClass.getExprType(expr, gen);
			if (vtype == null) {
				throw new NullPointerException(
						"No Vtype found for paratemerized expression "
								+ expr.getVarName());
			}
			gen.addToSymTab(vtype, expr.getVarName());
			sym = gen.getSymbol(expr.getVarName());
		}
		if (sym == null) {
			throw new NullPointerException("Symbol not found in symbol table ");
		}
		gen.appendToPrettyCode(toXMLHead("index", "false", "flattened",
				"false", "copyslice",
				Integer.toString(gen.getSymbol(expr.getVarName()).getId()),
				"arrayid"));
		// gen.appendToPrettyCode(HelperClass.toXML("base"));
		// expr.getChild(0).analyze(gen);
		// gen.appendToPrettyCode(HelperClass.toXML("/base"));
		gen.appendToPrettyCode(sym.getVtype().toXML());
		gen.appendToPrettyCode(HelperClass.toXML("indices"));
		// TODO : change to handle index expression after talking with Rahul

		for (Expr args : expr.getArgList()) {
			gen.appendToPrettyCode(HelperClass
					.toXML("index boundscheck=\"1\" negative=\"0\""));

			args.analyze(gen);
			gen.appendToPrettyCode(HelperClass.toXML("/index"));
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

	// public static StringBuffer toXMLHead(String name, String id, String
	// field) {
	// return new StringBuffer("<expr " + field + "=\"" + id + "\" name =\""
	// + name + "\">\n");
	// }
	public static StringBuffer toXMLHead(String name, String... fields) {
		if ((fields.length) % 2 != 0) {
			System.out.println("Number of arguments should be even ");
			return null;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("expr ");
		for (int i = 0; i < fields.length; i += 2) {
			sb.append(fields[i + 1] + "=\"" + fields[i] + "\" ");
		}
		sb.append("name =\"" + name + "\"");
		return new StringBuffer(HelperClass.toXML(sb.toString()));

	}

	public static StringBuffer toXMLTail() {
		return new StringBuffer("</expr>\n");
	}

}
