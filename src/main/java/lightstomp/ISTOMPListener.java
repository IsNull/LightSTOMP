package lightstomp;

/**
 * Provides events concerning the STOMP connection and protocol.
 *
 */
public interface ISTOMPListener {

    /**
     * Occurs when the STOMP protocol is in CONNECTED state
     */
    void stompConnected();

    /**
     * Occurs when the STOMP protocol is in DISCONNECTED state
     */
    void stompClosed();
}
