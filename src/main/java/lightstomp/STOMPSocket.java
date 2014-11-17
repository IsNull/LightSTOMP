package lightstomp;


import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
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
        System.out.printf("Got connected to WS: %s%n", session);
        this.session = session;
        try {
            stompConnect("admin","admin"); // TODO

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        System.out.printf("Got msg: %s%n", msg);
    }

    public Future<Void> sendAsync(StompRequest request){
        if(session != null){
            System.out.println("Sending request: " + request.toString());
           return session.getRemote().sendStringByFuture(request.toString());
        }
        return null;
    }
    public void sendAndWait(StompRequest request) throws IOException{
        if(session != null){
            System.out.println("Sending request: " + request.toString());

            session.getRemote().sendString(request.toString());
        }
    }

    public boolean isConnected() {
        return session != null && session.isOpen();
    }

    private void stompConnect(String user, String password) {
        StompRequest request = new StompRequest(CommandType.CONNECT)
                .withHeader("accept-version", "1.0,1.1,2.0")
                //.withHeader("host", "stomp.github.org") // TODO !!
                .withHeader("login", user)
                .withHeader("passcode", password);
        try {
            sendAndWait(request);
        } catch (IOException e) {
            e.printStackTrace(); // TODO
        }
    }
}