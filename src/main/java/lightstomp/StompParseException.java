package lightstomp;

/**
 * Exception which is thrown when a STOMP frame could not be parsed.
 */
public class StompParseException extends Exception {

    public StompParseException(String s) {
        super(s);
    }

    public StompParseException(String s, Throwable t) {
        super(s, t);
    }
}
