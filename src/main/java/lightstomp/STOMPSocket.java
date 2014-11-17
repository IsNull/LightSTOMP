package lightstomp;


import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Basic Echo Client Socket
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class STOMPSocket {


    @SuppressWarnings("unused")
    private Session session;

    public STOMPSocket() {

    }


    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        this.session = null;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.printf("Got connect: %s%n", session);
        this.session = session;
        try {
            Future<Void> fut;
            fut = sendString("Hello");
            fut.get(2, TimeUnit.SECONDS);
            fut = sendString("Thanks for the conversation.");
            fut.get(2, TimeUnit.SECONDS);
            session.close(StatusCode.NORMAL, "I'm done");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        System.out.printf("Got msg: %s%n", msg);
    }

    public Future<Void> sendString(String msg){
        if(session != null){
           return session.getRemote().sendStringByFuture("Hello");
        }
        return null;
    }

    public boolean isConnected() {
        return session != null && session.isOpen();
    }
}