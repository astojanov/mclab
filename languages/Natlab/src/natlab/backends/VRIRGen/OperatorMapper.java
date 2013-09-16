package natlab.backends.VRIRGen;

import java.util.HashMap;
import java.util.Map;

public class OperatorMapper {
	static Map<String, String> opMap;

	public static void initMap() {
		opMap = new HashMap<String, String>();
		opMap.put("plus", "plus");
		opMap.put("minus", "minus");
		opMap.put("mtimes", "mmult");
		opMap.put("times", "mult");
		opMap.put("mrdivide", "div");
		// TODO: need to check all divisions and then add them
		// TODO: check equivalents for Unary operators
		opMap.put("uplus", "");
		opMap.put("uminus", "negate");
		opMap.put("not", "not");
	}

	public static boolean isOperator(String name) {
		return opMap.containsKey(name);
	}

	public static void add(String key, String value) {
		opMap.put(key, value);
	}

	public static void remove(String key, String value) {
		opMap.remove(key);
	}

	public static String get(String key) {
		return opMap.get(key);
	}

}
