package natlab.backends.javascript;

import natlab.backends.javascript.ast.*;

public class Main {
	
	public static void main(String[] args) {
		Program p = new Program();
		p.addFunction(new NamedFunction("function1", null, null));
		p.addFunction(new NamedFunction("function2", null, null));
		System.out.println(p.pp().show());
	}
	
}
