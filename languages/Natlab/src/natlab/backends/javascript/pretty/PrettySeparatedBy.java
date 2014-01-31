package natlab.backends.javascript.pretty;

public class PrettySeparatedBy extends PrettyConcat {
    public PrettySeparatedBy(PrettyBase sep, PrettyBase... parts) {
        this.parts = new PrettyBase[Math.max(0, 2 * parts.length - 1)];
        int i = 0;
        boolean first = true;

        for (PrettyBase part : parts) {
            if (!first)
                this.parts[i++] = sep;
            first = false;
            this.parts[i++] = part;
        }
    }

    public PrettySeparatedBy(String sep, PrettyBase... parts) {
        this(new PrettyText(sep), parts);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
