package natlab.backends.Fortran.codegen.ASTcaseHandler;

import java.util.ArrayList;
import java.util.List;

import natlab.tame.valueanalysis.components.shape.DimValue;
import natlab.backends.Fortran.codegen.FortranAST.*;

public class ArraySetIndexingTransformation {

	public static RigorousIndexingTransformation getTransformedIndex(
			String lhsArrayName, 
			String valueName, 
			List<DimValue> lhsArrayDimension, 
			ArrayList<String> lhsIndex) {
		RigorousIndexingTransformation transformedArraySet = new RigorousIndexingTransformation();
		StringBuffer sb = new StringBuffer();
		sb.append("call array_set");
		sb.append(lhsArrayDimension.size());
		for (int i=0; i<lhsIndex.size(); i++) {
			String[] vectorIndex = lhsIndex.get(i).split(":");
			if (lhsIndex.get(i).equals(":")) {
				sb.append("c");
			}
			else if (vectorIndex.length==2) {
				sb.append("v");
			}
			else {
				sb.append("s");
			}
		}
		sb.append("(");
		sb.append(lhsArrayName+", ");
		for (int i=0; i<lhsArrayDimension.size(); i++) {
			sb.append(lhsArrayDimension.get(i)+", ");
		}
		for (int i=0; i<lhsIndex.size(); i++) {
			String[] vectorIndex = lhsIndex.get(i).split(":");
			if (lhsIndex.get(i).equals(":")) {
				// do nothing.
			}
			else if (vectorIndex.length==2) {
				sb.append(vectorIndex[0]+", "+vectorIndex[1]+", ");
			}
			else {
				sb.append(lhsIndex.get(i)+", ");
			}
		}
		sb.append(valueName+");");
		transformedArraySet.setFunctionCall(sb.toString());
		return transformedArraySet;
	}
}
