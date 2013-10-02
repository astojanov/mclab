package natlab.backends.VRIRGen;

import natlab.tame.classes.reference.PrimitiveClassReference;
import natlab.tame.valueanalysis.advancedMatrix.AdvancedMatrixValue;
import natlab.tame.valueanalysis.aggrvalue.AggrValue;
import natlab.tame.valueanalysis.components.shape.Shape;

public class VType {
	public static enum Layout {
		ROW_MAJOR, COLUMN_MAJOR, STRIDE_MAJOR
	}

	Shape<AggrValue<AdvancedMatrixValue>> shape;
	PrimitiveClassReference type;
	Layout layout;
	String complexity;

	VType(Shape<AggrValue<AdvancedMatrixValue>> shape,
			PrimitiveClassReference type, Layout layout, String complexity) {
		this.shape = shape;
		this.type = type;
		this.layout = layout;
		this.complexity = complexity;

	}

	public Shape<AggrValue<AdvancedMatrixValue>> getShape() {
		return shape;
	}

	public void setShape(Shape<AggrValue<AdvancedMatrixValue>> shape) {
		this.shape = shape;
	}

	public PrimitiveClassReference getType() {
		return type;
	}

	public void setType(PrimitiveClassReference type) {
		this.type = type;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	private String getVarType() {
		if (complexity.equalsIgnoreCase("COMPLEX")) {
			return "complex";
		}
		if (type.getName().trim().equalsIgnoreCase("double")) {
			return "float64";
		}
		if (type.getName().trim().equalsIgnoreCase("char")) {
			return "char";
		}
		if (type.getName().trim().equalsIgnoreCase("single")) {
			return "float32";
		}
		if (type.getName().trim().equalsIgnoreCase("int8")) {
			return "int8";
		}
		if (type.getName().trim().equalsIgnoreCase("int16")) {
			return "int16";
		}
		if (type.getName().trim().equalsIgnoreCase("int32")) {
			return "int32";
		}
		if (type.getName().trim().equalsIgnoreCase("int64")) {
			return "int64";
		}
		if (type.getName().trim().equalsIgnoreCase("uint8")) {
			return "uint8";
		}
		if (type.getName().trim().equalsIgnoreCase("uint16")) {
			return "uint16";
		}
		if (type.getName().trim().equalsIgnoreCase("uint32")) {
			return "uint32";
		}
		if (type.getName().trim().equalsIgnoreCase("uint64")) {
			return "uint64";
		}
		if (type.getName().trim().equalsIgnoreCase("logical")) {
			return "Boolean";
		}
		return null;
	}

	private String getLayoutString() {
		switch (getLayout()) {
		case COLUMN_MAJOR:
			return "columnmajor";
		case ROW_MAJOR:
			return "rowmajor";
		case STRIDE_MAJOR:
			return "stridemajor";

		}
		return null;
	}

	private void genScalarXML(StringBuffer vTypeXML) {
		vTypeXML.append("<vtype name= \"" + getVarType() + "\"");
		vTypeXML.append(">\n");
		vTypeXML.append("</vtype>\n");
	}

	private void genArrayXML(StringBuffer vTypeXML) {
		vTypeXML.append("<vtype Layout= \"" + getLayoutString()
				+ "\" name=\"array\" ndims=\""
				+ getShape().getDimensions().size() + "\">\n");
		genScalarXML(vTypeXML);
		vTypeXML.append("</vtype>\n");
	}

	public StringBuffer toXML() {
		StringBuffer vTypeXML = new StringBuffer();

		if (shape.isScalar()) {
			genScalarXML(vTypeXML);

		} else {
			genArrayXML(vTypeXML);
		}

		return vTypeXML;
	}
}
