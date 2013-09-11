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

	public void putSymbol(String name, Symbol sym) {
		symbolMap.put(name, sym);
	}
}
