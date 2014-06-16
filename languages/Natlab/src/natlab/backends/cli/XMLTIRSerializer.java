package natlab.backends.cli;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.tir.TIRAbstractAssignToListStmt;
import natlab.tame.tir.TIRAbstractAssignToVarStmt;
import natlab.tame.tir.TIRArrayGetStmt;
import natlab.tame.tir.TIRArraySetStmt;
import natlab.tame.tir.TIRAssignLiteralStmt;
import natlab.tame.tir.TIRBreakStmt;
import natlab.tame.tir.TIRCallStmt;
import natlab.tame.tir.TIRCommaSeparatedList;
import natlab.tame.tir.TIRCommentStmt;
import natlab.tame.tir.TIRContinueStmt;
import natlab.tame.tir.TIRCopyStmt;
import natlab.tame.tir.TIRForStmt;
import natlab.tame.tir.TIRFunction;
import natlab.tame.tir.TIRIfStmt;
import natlab.tame.tir.TIRReturnStmt;
import natlab.tame.tir.TIRStatementList;
import natlab.tame.tir.TIRStmt;
import natlab.tame.tir.TIRWhileStmt;
import natlab.tame.tir.analysis.TIRAbstractNodeCaseHandler;
import natlab.tame.valueanalysis.IntraproceduralValueAnalysis;
import natlab.tame.valueanalysis.ValueAnalysis;
import natlab.tame.valueanalysis.ValueFlowMap;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.basicmatrix.BasicMatrixValue;
import natlab.tame.valueanalysis.components.constant.Constant;
import natlab.tame.valueanalysis.components.isComplex.isComplexInfo;
import natlab.tame.valueanalysis.components.shape.Shape;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ast.ASTNode;
import ast.ColonExpr;
import ast.Expr;
import ast.FPLiteralExpr;
import ast.IntLiteralExpr;
import ast.LiteralExpr;
import ast.Name;
import ast.NameExpr;
import ast.Stmt;
import ast.StringLiteralExpr;

public final class XMLTIRSerializer extends TIRAbstractNodeCaseHandler
{
	public static final int TRIBOOL_TRUE = 1;
	public static final int TRIBOOL_FALSE = 0;
	public static final int TRIBOOL_MAYBE = -1; // AKA FileNotFound :)

	public static final String VARIABLE_KIND_INPUT = "in";
	public static final String VARIABLE_KIND_OUTPUT = "out";
	public static final String VARIABLE_KIND_INPUTOUTPUT = "inout";
	public static final String VARIABLE_KIND_LOCAL = "local";
	public static final String VARIABLE_KIND_GLOBAL = "global";
	public static final String VARIABLE_KIND_PERSISTENT = "persistent";

	private static class VariableInfo
	{
		public Element element;
		public PrimitiveClassReference primitiveClass;
		public int isComplexTribool;
		// True if the variable is only written to during its declaration (could be declared "final" in Java)
		public boolean isFinal = true;
		// True if known to always be scalar, false otherwise
		public boolean isScalar;
		public Constant constantValue;
	}

	private final ValueAnalysis<AggrValue<BasicMatrixValue>> analysis;
	private final Document document;
	private IntraproceduralValueAnalysis<AggrValue<BasicMatrixValue>> intraproceduralAnalysis;
	private final HashMap<String, VariableInfo> variablesInfo = new HashMap<String, VariableInfo>();
	private Element parentElement;
	private Element variablesElement;

	private XMLTIRSerializer(ValueAnalysis<AggrValue<BasicMatrixValue>> analysis) throws Throwable
	{
		assert analysis != null;

		this.analysis = analysis;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	}

	public static Document toDocument(ValueAnalysis<AggrValue<BasicMatrixValue>> analysis) throws Throwable
	{
		assert analysis != null;

		XMLTIRSerializer serializer = new XMLTIRSerializer(analysis);
		serializer.serialize();
		return serializer.document;
	}

	public static void write(ValueAnalysis<AggrValue<BasicMatrixValue>> analysis, OutputStream stream) throws Throwable
	{
		assert analysis != null;
		assert stream != null;

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", new Integer(2));
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(new DOMSource(toDocument(analysis)), new StreamResult(stream));
	}

	public void serialize()
	{
		parentElement = document.createElement("CompilationUnit");
		document.appendChild(parentElement);

		HashSet<TIRFunction> visitedFunctions = new HashSet<TIRFunction>();
		int functionCount = analysis.getNodeList().size();
		for (int i = 0; i < functionCount; ++i)
		{
			intraproceduralAnalysis = analysis.getNodeList().get(i).getAnalysis();

			TIRFunction function = intraproceduralAnalysis.getTree();
			if (visitedFunctions.contains(function))
			{
				// We already visited some intraprocedural analysis for this function,
				// assume this one has the same analysis results.
				continue;
			}

			visitedFunctions.add(function);
			caseTIRFunction(function);
		}
	}

	@Override
	public void caseTIRCallStmt(TIRCallStmt node)
	{
		Element element = createChild(parentElement, "Call");
		element.setAttribute("function", node.getFunctionName().getID());
		setCsvAttribute(element, "targets", node.getTargets());
		setCsvAttribute(element, "arguments", node.getArguments());

		updateVariableInfo(node);
	}

	@Override
	public void caseTIRCommentStmt(TIRCommentStmt node) {} // Just ignore comments

	@Override
	public void caseTIRArrayGetStmt(TIRArrayGetStmt node)
	{
		Element element = createChild(parentElement, "ArrayGet");
		element.setAttribute("array", node.getArrayName().getID());
		setCsvAttribute(element, "targets", node.getTargets());
		setCsvAttribute(element, "indices", node.getIndizes());

		updateVariableInfo(node);
	}

	@Override
	public void caseTIRArraySetStmt(TIRArraySetStmt node)
	{
		String arrayName = node.getArrayName().getID();

		Element element = createChild(parentElement, "ArraySet");
		element.setAttribute("array", arrayName);
		element.setAttribute("value", node.getValueName().getID());
		setCsvAttribute(element, "indices", node.getIndizes());

		updateVariableInfo(intraproceduralAnalysis.getOutFlowSets().get(node), arrayName);
	}

	@Override
	public void caseTIRFunction(TIRFunction node)
	{
		variablesInfo.clear();
		variablesElement = null;

		// Create the XML node for the function
		Element element = createChild(parentElement, "Function");
		element.setAttribute("name", node.getName());
		if (node == analysis.getMainNode().getAnalysis().getTree())
			element.setAttribute("entry", "true");

		setNamesAttribute(element, "inputs", node.getInputParams());
		setNamesAttribute(element, "outputs", node.getOutputParams());

		variablesElement = createChild(element, "Variables");

		// Fill out function-wide static information for the input/output variables
		ValueFlowMap<AggrValue<BasicMatrixValue>> valueFlowMap = intraproceduralAnalysis.getOutFlowSets().get(node);

		for (Name name : node.getInputParams())
			updateVariableInfo(valueFlowMap, name.getID());

		for (Name name : node.getOutputParams())
			updateVariableInfo(valueFlowMap, name.getID());

		// Traverse the function body
		createChildrenStatements(createChild(element, "Body"), node.getStmtList());

		// Fill out static information for each of the variables
		for (Map.Entry<String, VariableInfo> entry : variablesInfo.entrySet())
		{
			VariableInfo variableInfo = entry.getValue();
			if (variableInfo.primitiveClass != null)
				variableInfo.element.setAttribute("class", variableInfo.primitiveClass.getName());
			if (variableInfo.isComplexTribool != TRIBOOL_MAYBE)
				variableInfo.element.setAttribute("complex", variableInfo.isComplexTribool == TRIBOOL_TRUE ? "true" : "false");
			if (variableInfo.isScalar)
				variableInfo.element.setAttribute("scalar", "true");
			if (variableInfo.isFinal)
				variableInfo.element.setAttribute("final", "true");
			if (variableInfo.constantValue != null)
			{
				String value = variableInfo.constantValue.toString();
				if (variableInfo.constantValue.getMatlabClass().equals(PrimitiveClassReference.CHAR))
					value = unescapeMatlabString(value); // Escape string constants
				variableInfo.element.setAttribute("constant", value);
			}
		}
	}

	@Override
	public void caseTIRIfStmt(TIRIfStmt node)
	{
		Element element = createChild(parentElement, "If");
		element.setAttribute("condition", node.getConditionVarName().getID());

		createChildrenStatements(element, node.getIfStatements());
		if (node.hasElseBlock() && node.getElseStatements().getNumChild() > 0)
		{
			createChild(element, "Else");
			createChildrenStatements(element, node.getElseStatements());
		}
	}

	@Override
	public void caseTIRForStmt(TIRForStmt node)
	{
		String iteratorName = node.getLoopVarName().getID();

		Element element = createChild(parentElement, "For");
		element.setAttribute("iterator", iteratorName);
		element.setAttribute("from", node.getLowerName().getID());
		element.setAttribute("to", node.getUpperName().getID());
		if (node.hasIncr()) element.setAttribute("step", node.getIncName().getID());

		createChildrenStatements(element, node.getStatements());

		updateVariableInfo(intraproceduralAnalysis.getOutFlowSets().get(node), iteratorName);
	}

	@Override
	public void caseTIRAssignLiteralStmt(TIRAssignLiteralStmt node)
	{
		Element element = createChild(parentElement, "Literal");
		element.setAttribute("target", node.getTargetName().getID());

		LiteralExpr literal = node.getRHS();
		if (literal instanceof IntLiteralExpr)
		{
			element.setAttribute("type", "double");
			element.setAttribute("value", ((IntLiteralExpr)literal).getValue().getText());
		}
		else if (literal instanceof FPLiteralExpr)
		{
			element.setAttribute("type", "double");
			element.setAttribute("value", ((FPLiteralExpr)literal).getValue().getText());
		}
		else if (literal instanceof StringLiteralExpr)
		{
			String text = ((StringLiteralExpr)literal).getValue();
			element.setAttribute("type", "char");
			element.setAttribute("value", unescapeMatlabString(text));
		}
		else
		{
			throw new UnsupportedOperationException("Literals of type: " + node.getClass().getName());
		}

		updateVariableInfo(node);
	}

	private static String unescapeMatlabString(String str)
	{
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); )
		{
			if (str.charAt(i) == '\\')
			{
				if (i == str.length() - 1)
				{
					result.append('\\');
					break;
				}

				i++;

				switch (str.charAt(i))
				{
					case '\'': result.append('\''); break;
					case '%': result.append('%'); break;
					case '\\': result.append('\\'); break;
					case 'a': result.append('\u0007'); break;
					case 'b': result.append('\b'); break;
					case 'f': result.append('\f'); break;
					case 'n': result.append('\n'); break;
					case 'r': result.append('\r'); break;
					case 't': result.append('\u000B'); break;

					// TODO: Implement hex & octal escape sequences
					default: result.append(str.charAt(i)); break;
				}

				i++;
			}
			else
			{
				result.append(str.charAt(i));
				i++;
			}
		}

		return result.toString();
	}

	@Override
	public void caseTIRCopyStmt(TIRCopyStmt node)
	{
		Element element = createChild(parentElement, "Copy");
		element.setAttribute("source", node.getSourceName().getID());
		element.setAttribute("target", node.getTargetName().getID());
		updateVariableInfo(node);
	}

	@Override
	public void caseTIRBreakStmt(TIRBreakStmt node)
	{
		createChild(parentElement, "Break");
	}

	@Override
	public void caseTIRContinueStmt(TIRContinueStmt node)
	{
		createChild(parentElement, "Continue");
	}

	@Override
	public void caseTIRReturnStmt(TIRReturnStmt node)
	{
		createChild(parentElement, "Return");
	}

	@Override
	public void caseTIRWhileStmt(TIRWhileStmt node)
	{
		Element element = createChild(parentElement, "While");
		element.setAttribute("condition", node.getCondition().getName().getID());
		createChildrenStatements(element, node.getStatements());
	}

	@Override
	public void caseASTNode(ASTNode node)
	{
		throw new UnsupportedOperationException(
			"Serialization support for nodes of type " + node.getClass().getName() + " not implemented.");
	}

	private void updateVariableInfo(TIRAbstractAssignToVarStmt node)
	{
		updateVariableInfo(
			intraproceduralAnalysis.getOutFlowSets().get(node),
			node.getTargetName().getID());
	}

	private void updateVariableInfo(TIRAbstractAssignToListStmt node)
	{
		ValueFlowMap<AggrValue<BasicMatrixValue>> valueFlowMap = intraproceduralAnalysis.getOutFlowSets().get(node);
		for (Expr targetExpr : node.getTargets())
		{
			updateVariableInfo(
				valueFlowMap,
				((NameExpr)targetExpr).getName().getID());
		}
	}

	private void updateVariableInfo(ValueFlowMap<AggrValue<BasicMatrixValue>> valueFlowMap, String name)
	{
		if (valueFlowMap.isViable())
		{
			for (AggrValue<BasicMatrixValue> aggrValue : valueFlowMap.get(name).values())
				updateVariableInfo(name, (BasicMatrixValue)aggrValue);
		}
		else
		{
			// No type information via this code path
			updateVariableInfo(name, null);
		}
	}

	private void updateVariableInfo(String name, BasicMatrixValue value)
	{
		VariableInfo localInfo = variablesInfo.get(name);
		if (localInfo == null)
		{
			// First time we encounter the variable
			localInfo = new VariableInfo();
			variablesInfo.put(name, localInfo);

			// Create the XML element now so that they are in order of first appearance
			localInfo.element = createChild(variablesElement, "Variable");
			localInfo.element.setAttribute("name", name);

			if (value == null)
			{
				// No static information
				localInfo.isComplexTribool = TRIBOOL_MAYBE;
			}
			else
			{
				localInfo.primitiveClass = value.getMatlabClass();
				localInfo.isComplexTribool = getIsComplexTribool(value);

				Shape<AggrValue<BasicMatrixValue>> shape = value.getShape();
				localInfo.isScalar = shape != null && shape.isScalar();

				localInfo.constantValue = value.getConstant();
			}
		}
		else
		{
			localInfo.isFinal = false;

			// Conservatively merge static information with previously known value
			if (value == null)
			{
				// We just lost any static information we could have
				localInfo.primitiveClass = null;
				localInfo.isComplexTribool = TRIBOOL_MAYBE;
				localInfo.isScalar = false;
				localInfo.constantValue = null;
			}
			else
			{
				// Check if our primitive class is still valid
				if (value.getMatlabClass() != localInfo.primitiveClass)
					localInfo.primitiveClass = null;

				if (localInfo.isComplexTribool != TRIBOOL_MAYBE)
				{
					// Check if our static "is complex" value is still valid
					int isComplexTribool = getIsComplexTribool(value);
					if (isComplexTribool != localInfo.isComplexTribool)
						localInfo.isComplexTribool = TRIBOOL_MAYBE;
				}

				if (localInfo.isScalar)
				{
					// Check if our static "is scalar" value is still valid
					Shape<AggrValue<BasicMatrixValue>> shape = value.getShape();
					if (shape == null || !shape.isScalar())
						localInfo.isScalar = false;
				}

				if (localInfo.constantValue != null)
				{
					// Check if our static constant value is still valid
					Constant constantValue = value.getConstant();
					if (constantValue == null || !constantValue.equals(localInfo.constantValue))
						localInfo.constantValue = null;
				}
			}
		}
	}

	private static int getIsComplexTribool(BasicMatrixValue value)
	{
		if (value != null)
		{
			isComplexInfo<AggrValue<BasicMatrixValue>> isComplexInfo = value.getisComplexInfo();
			if (isComplexInfo != null)
			{
				String complexTypeString = isComplexInfo.toString();
				if ("COMPLEX".equals(complexTypeString)) return TRIBOOL_TRUE;
				if ("REAL".equals(complexTypeString)) return TRIBOOL_FALSE;
			}
		}

		return TRIBOOL_MAYBE;
	}

	private void createChildrenStatements(Element parentElement, TIRStatementList statements)
	{
		Element oldParentElement = this.parentElement;
		this.parentElement = parentElement;

		for (Stmt statement : statements)
			((TIRStmt) statement).tirAnalyze(this);

		this.parentElement = oldParentElement;
	}

	/**
	 * Adds an attribute to an XML element consisting of a comma-separated list of names.
	 * This is a major sin in XML design, but it actually makes the output much more readable.
	 * @param element The element to which to add the attribute.
	 * @param attributeName The name of the attribute to be added.
	 * @param names The names to be converted to a comma separated list as the attribute's value.
	 */
	private void setNamesAttribute(Element element, String attributeName, Iterable<Name> names)
	{
		Iterator<Name> iterator = names.iterator();
		StringBuilder stringBuilder = new StringBuilder();
		while (iterator.hasNext())
		{
			Name name = iterator.next();
			if (name != null)
			{
				if (stringBuilder.length() > 0) stringBuilder.append(',');
				stringBuilder.append(name.getID());
			}
		}

		if (stringBuilder.length() > 0)
			element.setAttribute(attributeName, stringBuilder.toString());
	}

	private void setCsvAttribute(Element element, String attributeName, TIRCommaSeparatedList list)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (Expr expr : list)
		{
			if (stringBuilder.length() > 0) stringBuilder.append(',');
			if (expr instanceof NameExpr) stringBuilder.append(((NameExpr)expr).getName().getID());
			else if (expr instanceof ColonExpr) stringBuilder.append(':');
		}

		if (stringBuilder.length() > 0)
			element.setAttribute(attributeName, stringBuilder.toString());
	}

	private Element createChild(Element parent, String childName)
	{
		Element child = document.createElement(childName);
		parent.appendChild(child);
		return child;
	}
}
