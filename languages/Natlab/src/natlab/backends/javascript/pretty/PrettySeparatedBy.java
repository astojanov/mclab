package natlab.backends.javascript.pretty;


public class PrettySeparatedBy extends PrettyBase {
	private PrettyConcat container;
	
	public PrettySeparatedBy(String sep, PrettyBase... parts) {
		if (parts.length <= 1) {
			container = new PrettyConcat(parts);
		}
		else {
			PrettyBase[] separatedParts = new PrettyBase[2 * parts.length - 1];
			PrettyText prettySep = new PrettyText(sep);
			boolean first = true;
			int i = 0;
			
			for (PrettyBase part: parts) {
				if (!first) separatedParts[i++] = prettySep;
				first = false;
				separatedParts[i++] = part;
			}
			container = new PrettyConcat(separatedParts);
		}
	}
	
	@Override
	public String show() {
		return container.show();
	}

}
