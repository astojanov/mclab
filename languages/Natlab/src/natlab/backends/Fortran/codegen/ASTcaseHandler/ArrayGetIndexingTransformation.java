package natlab.backends.Fortran.codegen.ASTcaseHandler;

import java.util.ArrayList;
import java.util.List;

import natlab.tame.valueanalysis.components.shape.DimValue;
import natlab.backends.Fortran.codegen.FortranAST.*;

public class ArrayGetIndexingTransformation {

	public static RigorousIndexingTransformation getTransformedIndex(
			String lhsVariable, 
			String rhsArrayName, 
			List<DimValue> rhsArrayDimension, 
			ArrayList<String> rhsIndex) {
		RigorousIndexingTransformation transformedArrayGet = new RigorousIndexingTransformation();
		StringBuffer sb = new StringBuffer();
		sb.append("call array_get");
		sb.append(rhsArrayDimension.size());
		for (int i=0; i<rhsIndex.size(); i++) {
			String[] vectorIndex = rhsIndex.get(i).split(":");
			if (rhsIndex.get(i).equals(":")) {
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
		sb.append(rhsArrayName+", ");
		for (int i=0; i<rhsArrayDimension.size(); i++) {
			sb.append(rhsArrayDimension.get(i)+", ");
		}
		for (int i=0; i<rhsIndex.size(); i++) {
			String[] vectorIndex = rhsIndex.get(i).split(":");
			if (rhsIndex.get(i).equals(":")) {
				// do nothing.
			}
			else if (vectorIndex.length==2) {
				sb.append(vectorIndex[0]+", "+vectorIndex[1]+", ");
			}
			else {
				sb.append(rhsIndex.get(i)+", ");
			}
		}
		sb.append(lhsVariable+");");
		transformedArrayGet.setFunctionCall(sb.toString());
		return transformedArrayGet;
	}
}
