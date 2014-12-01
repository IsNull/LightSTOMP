package lightstomp.stompSocket;

import lightstomp.StompFrame;

/**
 * Created by paba on 11/21/14.
 */
public interface ISocketListener {

    /**
     * Occurs when this socket has successfully connected.
     */
    void connected();

    /**
     * Occurs when a STOMP Frame has arrived
     * @param frame
     */
    void onStompFrameReceived(StompFrame frame);

    /**
     * Occurs when the socket has been closed
     */
    void closed(String reason);

    /**
     * Occrus when no connection to the socket could be etablished.
     */
    void connectionFailed(Throwable e);

}
