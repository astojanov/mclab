package natlab.backends.VRIRGen;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
	private Map<String, Symbol> symbolMap;

	public SymbolTable() {
		symbolMap = new HashMap<String, Symbol>();
	}

	public Symbol getSymbol(String name) {
		return symbolMap.get(name);
	}

	public void putSymbol(VType vtype, String name, int id) {
		symbolMap.put(name, new Symbol(vtype, name, id));
	}

	public void putSymbol(String name, Symbol sym) {
		symbolMap.put(name, sym);
	}

	public Map<String, Symbol> getSymbolMap() {
		return symbolMap;
	}

	public void setSymbolMap(Map<String, Symbol> symbolMap) {
		this.symbolMap = symbolMap;
	}

}
