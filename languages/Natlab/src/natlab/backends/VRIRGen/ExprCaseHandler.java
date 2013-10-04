package natlab.backends.VRIRGen;

import java.util.HashMap;

import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.valueanalysis.ValueSet;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.components.shape.DimValue;
import natlab.tame.valueanalysis.components.shape.Shape;
import ast.AssignStmt;
import ast.Expr;
import ast.FPLiteralExpr;
import ast.IntLiteralExpr;
import ast.MatrixExpr;
import ast.Name;
import ast.NameExpr;
import ast.ParameterizedExpr;
import ast.RangeExpr;
import ast.Row;
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
		gen.appendToPrettyCode("<vtype name=float64>\n</vtype>\n");
		node.getArg(0).analyze(gen);
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleBinExpr(ParameterizedExpr node, VrirXmlGen gen,
			String name) {

		gen.appendToPrettyCode(toXMLHead(name));

		// gen.appendToPrettyCode("<vtype name=float64>\n</vtype>\n");
		HashMap<Expr, Name> map = gen.getAnalysisEngine()
				.getTemporaryVariablesRemovalAnalysis().getExprToTempVarTable();

		if (node.getParent() instanceof AssignStmt) {
			Expr lhsExpr = ((AssignStmt) node.getParent()).getLHS();
			System.out.println("lhs expr type" + lhsExpr.getClass().toString());
			if (lhsExpr instanceof MatrixExpr) {
				if (((MatrixExpr) lhsExpr).getNumRow() > 1) {
					System.out.println("not sure what to do ");
				} else {
					for (Row row : ((MatrixExpr) lhsExpr).getRowList()) {
						if (row.getNumElement() > 1) {
							System.out.println("not sure what to do");

						} else {
							Expr expr = row.getElement(0);
							if (expr instanceof NameExpr) {
								PrimitiveClassReference type = HelperClass
										.getDataType((NameExpr) expr, gen);
								Shape<AggrValue<AdvancedMatrixValue>> shape = HelperClass
										.getShape((NameExpr) expr, gen);
								gen.appendToPrettyCode((new VType(shape, type,
										VType.Layout.COLUMN_MAJOR, HelperClass
												.generateComplexityInfo(
														(NameExpr) expr, gen)))
										.toXML());

							} else if (expr instanceof ParameterizedExpr) {
								PrimitiveClassReference type = HelperClass
										.getDataType(((ParameterizedExpr) expr)
												.getVarName(), gen);
								Shape<AggrValue<AdvancedMatrixValue>> shape = HelperClass
										.getShape(((ParameterizedExpr) expr)
												.getVarName(), gen);

								gen.appendToPrettyCode((new VType(shape, type,
										VType.Layout.COLUMN_MAJOR, HelperClass
												.generateComplexityInfo(
														expr.getVarName(), gen)))
										.toXML());
							} else {
								System.out.println("not sure what to do ");
							}

						}
					}
				}
			}
		}

		gen.appendToPrettyCode("<rhs>\n");
		node.getArg(1).analyze(gen);

		gen.appendToPrettyCode("</rhs>\n");
		gen.appendToPrettyCode("<lhs>\n");
		node.getArg(0).analyze(gen);
		gen.appendToPrettyCode("</lhs>\n");
		gen.appendToPrettyCode(toXMLTail());
	}

	public static void handleRangeExpr(RangeExpr node, VrirXmlGen gen) {
		gen.appendToPrettyCode("<start>");
		node.getLower().analyze(gen);
		gen.appendToPrettyCode("</start>");
		gen.appendToPrettyCode("<step>");
		node.getIncr().analyze(gen);
		gen.appendToPrettyCode("</step>");
		gen.appendToPrettyCode("<stop>");
		node.getUpper().analyze(gen);
		gen.appendToPrettyCode("</stop>");
	}

	public static void handleIntLiteralExpr(IntLiteralExpr expr, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("const", expr.getValue().getValue()
				.intValue(), "value"));
	}

	public static void handleFpLiteralExpr(FPLiteralExpr expr, VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("const", expr.getValue().getValue()
				.toString(), "value"));
	}

	public static void handleStringLiteralExpr(StringLiteralExpr expr,
			VrirXmlGen gen) {
		gen.appendToPrettyCode(toXMLHead("const", expr.getValue(), "value"));
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
