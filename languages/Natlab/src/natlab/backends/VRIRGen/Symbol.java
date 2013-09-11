package natlab.backends.VRIRGen;

public class Symbol {
	VType vtype;
	String name;
	int id;

	public Symbol(VType vtype, String name, int id) {
		this.vtype = vtype;
		this.name = name;
		this.id = id;
	}
}
