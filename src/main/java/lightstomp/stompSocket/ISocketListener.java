package lightstomp.stompSocket;

import lightstomp.StompFrame;

/**
 * Created by paba on 11/21/14.
 */
public interface ISocketListener {
    void connected();
    void onStompFrameReceived(StompFrame frame);
    void disconnected();
}
