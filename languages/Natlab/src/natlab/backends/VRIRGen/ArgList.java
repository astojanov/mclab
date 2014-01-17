package natlab.backends.VRIRGen;

import java.util.ArrayList;

public class ArgList {
	private ArrayList<Arg> list;

	public ArgList() {
		list = new ArrayList<Arg>();
	}

	public ArgList(ArrayList<Arg> list) {
		this.list = list;
	}

	public StringBuffer toXML() {
		StringBuffer sb = new StringBuffer();
		sb.append(HelperClass.toXML("arglist"));
		for (Arg arg : list) {
			sb.append(arg.toXML());
		}
		sb.append(HelperClass.toXML("/arglist"));
		return sb;
	}
}
