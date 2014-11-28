package lightstomp;

/**
 * Provides events concerning the STOMP connection and protocol.
 *
 */
public interface ISTOMPListener {

    /**
     * Occurs when a STOMP connection could be created.
     */
    void connectionSuccess(StompClient connection);

    /**
     * Occurs when the connection could NOT be established.
     */
    void connectionFailed();

    /**
     * Occurs when the previously working connection is lost.
     */
    void disconnected();

}
