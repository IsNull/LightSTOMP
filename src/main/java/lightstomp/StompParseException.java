package lightstomp;

/**
 * Created by paba on 11/21/14.
 */
public class StompParseException extends Exception {

    public StompParseException(String s) {
        super(s);
    }

    public StompParseException(String s, Throwable t) {
        super(s, t);
    }
}
