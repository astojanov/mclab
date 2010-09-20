package natlab.mc4.symbolTable;

public class UnknownFunction extends FunctionType {

    @Override
    public String toString() {
        return "unknown function";
    }

	@Override
	public UnknownFunction copy() {
		return new UnknownFunction();
	}
}
