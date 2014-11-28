package lightstomp.stompSocket;

import lightstomp.StompFrame;

/**
 * Created by paba on 11/21/14.
 */
public interface IStompSocket {

    /**
     * Returns true if this socket is connected
     * @return
     */
    boolean isConnected();

    /**
     *
     */
    void connect(ISocketListener listener);

    /**
     * Send the given Frame
     * @param request
     */
    void sendFrame(StompFrame request);

    /**
     * Get the host to which this socket is connected
     * @return
     */
    String getHost();

}
