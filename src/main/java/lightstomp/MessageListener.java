package lightstomp;

/**
 * Listener for STOMP messages
 */
public interface MessageListener {
    /**
     * Occurs when a STOMP message is received by the client.
     * @param message
     */
    void messageReceived(String message);
}
