package natlab.backends.VRIRGen;

import java.util.ArrayList;

public class VTypeCell extends VType {
	ArrayList<VType> elementList;

	public VTypeCell() {
		// TODO Auto-generated constructor stub
		elementList = new ArrayList<VType>();
	}

	public void addElement(VType vtype) {
		elementList.add(vtype);
	}

	public ArrayList<VType> getElementList() {
		return elementList;
	}

	public void setElementList(ArrayList<VType> elementList) {
		this.elementList = elementList;
	}

	public StringBuffer toXML() {
		StringBuffer sb = new StringBuffer();
		sb.append(HelperClass.toXML("TupleType"));
		sb.append(HelperClass.toXML("elems"));
		for (VType vtype : elementList) {
			sb.append(vtype.toXML());
		}
		sb.append(HelperClass.toXML("/elems"));
		sb.append(HelperClass.toXML("/TupleType"));
		return sb;
	}
}
