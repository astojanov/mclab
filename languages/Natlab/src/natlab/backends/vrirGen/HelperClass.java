package natlab.backends.vrirGen;

import java.util.ArrayList;

import ast.AssignStmt;
import ast.ColonExpr;
import ast.Expr;
import ast.FPLiteralExpr;
import ast.Function;
import ast.List;
import ast.LiteralExpr;
import ast.MatrixExpr;
import ast.Name;
import ast.NameExpr;
import ast.ParameterizedExpr;
import ast.Row;
import natlab.tame.builtin.Builtin;
import natlab.tame.callgraph.StaticFunction;
import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.aggrvalue.CellValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.tame.valueanalysis.components.shape.DimValue;
import natlab.tame.valueanalysis.components.shape.Shape;
import natlab.tame.valueanalysis.value.Value;

public class HelperClass {

	public static VType generateVType(
			ValueAnalysis<AggrValue<BasicMatrixValue>> analysis,
			int graphIndex, Function node, Name param, int paramIndx) {

		@SuppressWarnings("rawtypes")
		Value temp = analysis.getNodeList().get(graphIndex).getAnalysis()
				.getArgs().get(paramIndx);
		if (temp == null) {
			throw new NullPointerException("type information for "
					+ param.getID() + " not found");
		}

		return generateVType(temp);

	}

	public static VType generateVType(
			ValueAnalysis<AggrValue<BasicMatrixValue>> analysis,
			int graphIndex, String name) {

		AggrValue<?> temp = analysis.getNodeList().get(graphIndex)
				.getAnalysis().getCurrentOutSet().get(name).getSingleton();

		return generateVType(temp);

	}

	public static VType generateVType(@SuppressWarnings("rawtypes") Value value) {

		if ((Object) value instanceof BasicMatrixValue) {
			// System.out.println("matlab class"
			// + value.getMatlabClass().getName());
			String complexity = HelperClass
					.getVrComplexity((((BasicMatrixValue) (Object) value))
							.getisComplexInfo().geticType());
			return new VTypeMatrix(
					(((BasicMatrixValue) (Object) value)).getShape(),
					(((BasicMatrixValue) (Object) value)).getMatlabClass(),
					VTypeMatrix.Layout.COLUMN_MAJOR, complexity);
		} else if ((Object) value instanceof CellValue) {

			VTypeTuple vtypeTuple = new VTypeTuple();
			for (Value<?> val : (((CellValue<?>) (Object) value)).getValues()) {
				vtypeTuple.addElement(generateVType(val));
			}

			return vtypeTuple;
		}
		return null;
	}

	public static boolean isOperator(ParameterizedExpr node) {

		return VrirTypeMapper.contains(node.getVarName());

	}

	public static PrimitiveClassReference getDataType(String name,
			VrirXmlGen gen) {
		AggrValue<BasicMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet().get(name)
				.getSingleton();
		if ((Object) val instanceof BasicMatrixValue) {

			return (((BasicMatrixValue) (Object) val)).getMatlabClass();

		}
		return null;
	}

	public static Shape<AggrValue<BasicMatrixValue>> getShape(String name,
			VrirXmlGen gen) {
		AggrValue<BasicMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet().get(name)
				.getSingleton();
		if ((Object) val instanceof BasicMatrixValue) {

			return (((BasicMatrixValue) (Object) val)).getShape();

		}
		return null;
	}

	public static PrimitiveClassReference getDataType(NameExpr node,
			VrirXmlGen gen) {
		AggrValue<BasicMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet()
				.get(((NameExpr) node).getName().getID()).getSingleton();
		if ((Object) val instanceof BasicMatrixValue) {

			return (((BasicMatrixValue) (Object) val)).getMatlabClass();

		} else {
			throw new NullPointerException(
					"Analyses other than cell value and Advanced matrix value not supported. are currently not supported   ");

		}
	}

	public static String getVrComplexity(String complexity) {
		if (complexity.equals("REAL")) {
			return "real";
		} else if (complexity.equals("COMPLEX")) {
			return "complex";
		} else {
			System.out.println("value is neither complex nor real "
					+ complexity + " .Returning may complex ");
			return "maycomplex";
		}

	}

	public static Shape<AggrValue<BasicMatrixValue>> getShape(NameExpr node,
			VrirXmlGen gen) {
		AggrValue<BasicMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet()
				.get(((NameExpr) node).getName().getID()).getSingleton();
		if ((Object) val instanceof BasicMatrixValue) {

			return (((BasicMatrixValue) (Object) val)).getShape();

		}
		return null;
	}

	public static String generateComplexityInfo(NameExpr node, VrirXmlGen gen) {
		AggrValue<BasicMatrixValue> val = gen.getAnalysis().getNodeList()
				.get(gen.getIndex()).getAnalysis().getCurrentOutSet()
				.get(((NameExpr) node).getName().getID()).getSingleton();
		if ((Object) val instanceof BasicMatrixValue) {

			return (((BasicMatrixValue) (Object) val)).getisComplexInfo()
					.geticType();

		}
		return null;
	}

	public static boolean generateComplexityInfo(LiteralExpr lit, VrirXmlGen gen) {
		if (lit instanceof FPLiteralExpr) {
			((FPLiteralExpr) lit).getValue().isImaginary();

		}
		return false;
	}

	public static VType getLhsType(MatrixExpr lhsExpr, VrirXmlGen gen) {

		if (((MatrixExpr) lhsExpr).getNumRow() > 1) {
			System.out
					.println("Multiple return types for binary expressions not supported. ");
			System.exit(0);
			// return null;
		}

		for (Row row : ((MatrixExpr) lhsExpr).getRowList()) {
			if (row.getElementList().getNumChild() == 0) {
				return new VoidType();
			}
			System.out.println("number of children" + row.getNumChild());
			if (row.getElementList().getNumChild() == 1) {

				Expr expr = row.getElement(0);

				if (expr instanceof NameExpr) {
					return getLhsType((NameExpr) expr, gen);

				} else if (expr instanceof ParameterizedExpr) {
					return getLhsType((ParameterizedExpr) expr, gen);
				} else {

					return null;
				}

			} else {
				VTypeTuple vtype = new VTypeTuple();
				for (Expr expr : row.getElementList()) {
					if (expr instanceof NameExpr) {
						vtype.addElement(getLhsType((NameExpr) expr, gen));

					} else if (expr instanceof ParameterizedExpr) {
						vtype.addElement(getLhsType((ParameterizedExpr) expr,
								gen));
					}
				}
				return vtype;
			}

		}

		return null;
	}

	public static VType getExprType(Expr expr, VrirXmlGen gen) {

		if (expr instanceof NameExpr
				&& isVar(gen, ((NameExpr) expr).getName().getID())) {

			return generateVType(gen.getAnalysis(), gen.getIndex(),
					((NameExpr) expr).getName().getID());
		}
		if (expr instanceof ParameterizedExpr) {
			Name tempName = (Name) gen.getAnalysisEngine()
					.getTemporaryVariablesRemovalAnalysis()
					.getExprToTempVarTable().get(expr);
			if (tempName != null) {
				AggrValue<?> val = gen.getAnalysis().getNodeList()
						.get(gen.getIndex()).getAnalysis().getCurrentOutSet()
						.get(tempName.getID()).getSingleton();
				return generateVType(val);
			} else if (isVar(gen, ((ParameterizedExpr) expr).getVarName())) {
				return generateVType(gen.getAnalysis(), gen.getIndex(),
						((ParameterizedExpr) expr).getVarName());
			}
		}
		if (expr.getParent() instanceof AssignStmt) {
			Expr lhsExpr = ((AssignStmt) expr.getParent()).getLHS();
			if (lhsExpr instanceof MatrixExpr) {
				return getLhsType((MatrixExpr) lhsExpr, gen);
			} else if (lhsExpr instanceof NameExpr) {
				return getLhsType((NameExpr) lhsExpr, gen);
			} else if (lhsExpr instanceof ParameterizedExpr) {
				return getLhsType((ParameterizedExpr) lhsExpr, gen);
			}
		}

		else {

			Name tempName = (Name) gen.getAnalysisEngine()
					.getTemporaryVariablesRemovalAnalysis()
					.getExprToTempVarTable().get(expr);
			if (tempName == null) {
				throw new NullPointerException(
						"Temporary variable for the expression not found");
			}

			AggrValue<?> val = gen.getAnalysis().getNodeList()
					.get(gen.getIndex()).getAnalysis().getCurrentOutSet()
					.get(tempName.getID()).getSingleton();
			VType vt = generateVType(val);

			return vt;
		}
		return null;
	}

	public static VType getLhsType(NameExpr lhsExpr, VrirXmlGen gen) {
		AggrValue<?> val = gen.getAnalysis().getNodeList().get(gen.getIndex())
				.getAnalysis().getCurrentOutSet()
				.get(lhsExpr.getName().getID()).getSingleton();

		return generateVType(val);
	}

	public static VType getLhsType(ParameterizedExpr lhsExpr, VrirXmlGen gen) {
		System.out.println("skjdhaskjdahslkdhaskldh" + lhsExpr.getVarName());
		AggrValue<?> val = gen.getAnalysis().getNodeList().get(gen.getIndex())
				.getAnalysis().getCurrentOutSet().get(lhsExpr.getVarName())
				.getSingleton();
		return generateVType(val);

	}

	public static VTypeFunction generateFuncType(VrirXmlGen gen,
			ParameterizedExpr expr) {
		VTypeFunction funcType = new VTypeFunction();
		StaticFunction func = null;

		if (Builtin.getInstance(expr.getVarName()) != null) {
			System.out.println("Function is a builtin");
			VType vtype = getExprType(expr, gen);

			// TODO : What about the case of multiple returns ?
			if (vtype == null) {
				funcType.addOutType(new VoidType());
			} else {
				funcType.addOutType(vtype);
			}
			if (expr.getArgList().getNumChild() == 0) {
				funcType.addInType(new VoidType());
			}

			for (Expr param : expr.getArgList()) {

				System.out.println("null expr" + param.getClass() + "  "
						+ expr.getVarName());
				vtype = getExprType(param, gen);
				if (vtype == null) {

				}
				funcType.addInType(vtype);
			}
			return funcType;
		}
		int i;
		for (i = 0; i < gen.getAnalysis().getNodeList().size(); i++) {
			if (gen.getAnalysis().getNodeList().get(i).getFunction().getName()
					.equalsIgnoreCase(expr.getVarName())) {
				func = gen.getAnalysis().getNodeList().get(i).getFunction();
				break;
			}

		}
		if (func == null) {

			System.err.println("function not found in call graph" + expr);

			return new VTypeFunction(new ArrayList<VType>(),
					new ArrayList<VType>());
		}

		for (Name nm : func.getAst().getInputParamList()) {
			Symbol sym = gen.getSymbol(nm.getID());
			if (sym == null) {
				VType vtype = HelperClass.generateVType(gen.getAnalysis(), i,
						nm.getID());
				gen.addToSymTab(vtype, nm.getID());
				sym = gen.getSymbol(nm.getID());
			}
			funcType.addInType(sym.getVtype());

		}
		if (func.getAst().getOutputParamList().getNumChild() == 0) {
			funcType.addOutType(new VoidType());
		}
		for (Name nm : func.getAst().getOutputParamList()) {
			Symbol sym = gen.getSymbol(nm.getID());
			if (sym == null) {
				VType vtype = HelperClass.generateVType(gen.getAnalysis(), i,
						nm.getID());
				gen.addToSymTab(vtype, nm.getID());
				sym = gen.getSymbol(nm.getID());
			}
			funcType.addOutType(sym.getVtype());

		}

		return funcType;
	}

	public static VTypeFunction generateFuncType(VrirXmlGen gen, NameExpr expr) {
		VTypeFunction funcType = new VTypeFunction();
		StaticFunction func = null;
		int i;
		if (Builtin.getInstance(expr.getVarName()) != null) {
			System.out.println("Function is a builtin " + expr.getVarName());

			VType vtype = getExprType(expr, gen);
			if (vtype == null) {
				funcType.addOutType(new VoidType());
			} else {
				// TODO : What about the case of multiple returns ?
				funcType.addOutType(vtype);
			}
			// for (Expr param : expr.getArgList()) {
			// funcType.addInType(getExprType(param, gen));
			// }
			funcType.addInType(new VoidType());
			return funcType;

		}
		for (i = 0; i < gen.getAnalysis().getNodeList().size(); i++) {
			if (gen.getAnalysis().getNodeList().get(i).getFunction().getName()
					.equalsIgnoreCase(expr.getVarName())) {
				func = gen.getAnalysis().getNodeList().get(i).getFunction();
				break;
			}

		}
		if (func == null) {

			System.err.println("function not found in call graph" + expr);
			return new VTypeFunction(new ArrayList<VType>(),
					new ArrayList<VType>());
		}

		for (Name nm : func.getAst().getInputParamList()) {
			Symbol sym = gen.getSymbol(nm.getID());
			if (sym == null) {
				VType vtype = HelperClass.generateVType(gen.getAnalysis(), i,
						nm.getID());
				gen.addToSymTab(vtype, nm.getID());
				sym = gen.getSymbol(nm.getID());
			}
			funcType.addInType(sym.getVtype());

		}
		if (func.getAst().getOutputParamList().getNumChild() == 0) {
			funcType.addOutType(new VoidType());
		}
		for (Name nm : func.getAst().getOutputParamList()) {
			Symbol sym = gen.getSymbol(nm.getID());
			if (sym == null) {
				VType vtype = HelperClass.generateVType(gen.getAnalysis(), i,
						nm.getID());
				gen.addToSymTab(vtype, nm.getID());
				sym = gen.getSymbol(nm.getID());
			}
			funcType.addOutType(sym.getVtype());

		}

		return funcType;
	}

	public static ArrayList<Arg> generateArgList(List<Name> inParamList,
			List<Name> outParamList, VrirXmlGen gen) {
		ArrayList<Arg> argList = new ArrayList<Arg>();
		for (int i = 0; i < inParamList.getNumChild(); i++) {
			Symbol sym = gen.getSymbol(inParamList.getChild(i).getID());
			if (sym == null) {
				throw new NullPointerException("Symbol not found");
			}
			argList.add(new Arg(sym.getId(), false));
		}

		// for (int i = 0; i < outParamList.getNumChild(); i++) {
		// Symbol sym = gen.getSymbol(outParamList.getChild(i).getID());
		// if (sym == null) {
		// throw new NullPointerException("Symbol not found");
		// }
		// argList.add(new Arg(sym.getId(), false));
		// }

		return argList;
	}

	public static boolean isVar(VrirXmlGen gen, String name) {
		return gen.getRemainingVars().contains(name);
	}

	public static String toXML(String str) {
		return "<" + str + ">\n";
	}

	public static boolean isAllocFunc(String name) {
		if (name.equalsIgnoreCase("zeros") || name.equalsIgnoreCase("ones")) {
			return true;
		}
		return false;
	}

	public static boolean isLibFunc(String name) {
		return LibFuncMapper.containsFunc(name);
	}

	public static boolean isScalar(ParameterizedExpr expr, VrirXmlGen gen) {
		VType vt = getExprType(expr, gen);
		boolean flag = true;

		if (vt instanceof VTypeMatrix) {
			if (((VTypeMatrix) vt).getShape().isScalar()) {
				return true;
			}
			for (DimValue val : ((VTypeMatrix) vt).getShape().getDimensions()) {
				if ((!val.equalsOne()) && val.hasIntValue()) {
					return false;
				}
				if (!val.hasIntValue()) {
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			return true;
		}
		for (Expr arg : expr.getArgList()) {
			if (arg instanceof ColonExpr) {
				return false;
			}
			if (arg instanceof ParameterizedExpr) {
				if (arg.getVarName().equals("colon")) {
					return false;
				}
				if (!isScalar((ParameterizedExpr) arg, gen)) {
					return false;
				}
			}
			vt = getExprType(arg, gen);
			if (vt instanceof VTypeMatrix) {
				if (!isScalar(((VTypeMatrix) vt).getShape().getDimensions())) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	public static boolean isScalar(java.util.List<DimValue> dimensions) {
		for (DimValue dim : dimensions) {
			if (!dim.equalsOne()) {
				return false;
			}
		}
		return true;
	}
}
