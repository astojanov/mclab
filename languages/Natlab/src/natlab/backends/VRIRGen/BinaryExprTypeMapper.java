package natlab.backends.VRIRGen;

import natlab.tame.classes.reference.PrimitiveClassReference;

public class BinaryExprTypeMapper {
	public static PrimitiveClassReference getOutputType(String lhs, String rhs) {
		// For all Double types
		if (lhs.trim().equalsIgnoreCase("double")
				&& rhs.trim().equalsIgnoreCase("double")) {
			return PrimitiveClassReference.DOUBLE;
		} else if (lhs.trim().equalsIgnoreCase("double")
				&& rhs.trim().equalsIgnoreCase("single")
				|| lhs.trim().equalsIgnoreCase("single")
				&& rhs.trim().equalsIgnoreCase("double")) {
			return PrimitiveClassReference.SINGLE;

			// for all Single types
		} else if (lhs.trim().equalsIgnoreCase("single")
				&& rhs.trim().equalsIgnoreCase("int8")
				|| lhs.trim().equalsIgnoreCase("int8")
				&& rhs.trim().equalsIgnoreCase("single")) {
			return PrimitiveClassReference.INT8;
		} else if (lhs.trim().equalsIgnoreCase("single")
				&& rhs.trim().equalsIgnoreCase("int16")
				|| lhs.trim().equalsIgnoreCase("int16")
				&& rhs.trim().equalsIgnoreCase("single")) {
			return PrimitiveClassReference.INT16;
		} else if (lhs.trim().equalsIgnoreCase("int32")
				&& rhs.trim().equalsIgnoreCase("single")
				|| lhs.trim().equalsIgnoreCase("single")
				&& rhs.trim().equalsIgnoreCase("int32")) {
			return PrimitiveClassReference.INT32;
		} else if (lhs.trim().equalsIgnoreCase("single")
				&& rhs.trim().equalsIgnoreCase("int64")
				|| lhs.trim().equalsIgnoreCase("int64")
				&& rhs.trim().equalsIgnoreCase("single")) {
			return PrimitiveClassReference.INT64;
		} else if (lhs.trim().equalsIgnoreCase("single")
				&& rhs.trim().equalsIgnoreCase("char")
				|| lhs.trim().equalsIgnoreCase("char")
				&& rhs.trim().equalsIgnoreCase("single")) {
			return PrimitiveClassReference.CHAR;
		} else if (lhs.trim().equalsIgnoreCase("single")
				&& rhs.trim().equalsIgnoreCase("single")) {
			return PrimitiveClassReference.SINGLE;
		}
		
		return null;
	}
}
