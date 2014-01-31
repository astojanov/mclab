package natlab.backends.javascript.pretty;

public class PrettyText extends PrettyBase {
    private String text;


    public PrettyText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return text;
    }

}
