package lightstomp;

import java.util.Map;

/**
 * Listener for STOMP messages
 */
public interface MessageListener {
    /**
     * Occurs when a STOMP message is received by the client.
     * @param message
     */
    void messageReceived(String message);

    /**
     * Occurs when a STOMP message is received by the client.
     * @param message
     * @param headers MessageHeaders
     */
	void messageReceived(String message, Map<String, String> headers);
}
