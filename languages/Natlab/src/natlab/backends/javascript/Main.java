package natlab.backends.javascript;

import natlab.backends.javascript.ast.*;

public class Main {

    public static void main(String[] args) {
        Program p = new Program();
        p.addFunction(new Function(new Opt<FuncName>(), new List<FormalParam>(), null));
        List<FormalParam> params = new List<>();
        params.add(new FormalParam("x"));
        params.add(new FormalParam("y"));
        p.addFunction(new Function(
                new Opt<FuncName>(new FuncName("function2")), 
                params,
                null));
        System.out.println(p.pp().show());
    }

}
