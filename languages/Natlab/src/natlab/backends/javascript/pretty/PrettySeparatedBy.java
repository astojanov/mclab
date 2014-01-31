package natlab.backends.javascript.pretty;

public class PrettySeparatedBy extends PrettyConcat {
    public PrettySeparatedBy(String sep, PrettyBase... parts) {
        this.parts = new PrettyBase[Math.max(0, 2 * parts.length - 1)];
        int i = 0;
        boolean first = true;

        for (PrettyBase part : parts) {
            if (!first)
                this.parts[i++] = new PrettyText(sep);
            first = false;
            this.parts[i++] = part;
        }
    }


    @Override
    public String toString() {
        return super.toString();
    }

}
