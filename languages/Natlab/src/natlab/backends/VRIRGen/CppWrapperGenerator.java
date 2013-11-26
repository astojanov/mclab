package natlab.backends.VRIRGen;

import natlab.tame.callgraph.StaticFunction;

public class CppWrapperGenerator extends WrapperGenerator {



	public StaticFunction getFunc() {
		return func;
	}

	public void setFunc(StaticFunction func) {
		this.func = func;
	}

	public CppWrapperGenerator(StaticFunction fun) {
		// TODO Auto-generated constructor stub
		super(fun);

	}

	@Override
	public void genWrapper() {
		// TODO Auto-generated method stub

	}

}
