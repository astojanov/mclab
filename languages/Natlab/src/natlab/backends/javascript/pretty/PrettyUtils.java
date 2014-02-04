package natlab.backends.javascript.pretty;

public class PrettyUtils {
    public static String pad(int width) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < width; ++i)
            sb.append(' ');
        return sb.toString();
    }

}
