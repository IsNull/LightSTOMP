package lightstomp;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;

/**
 * Implements a simple STOMP over Websocket client in pure Java.
 *
 * Created by paba on 11/17/14.
 */
public class StompOverWSClient {

    private final STOMPSocket socket;

    public StompOverWSClient(String url){
        WebSocketClient client = new WebSocketClient();
        socket = new STOMPSocket();
        try {
            client.start();
            URI echoUri = new URI(url);
            ClientUpgradeRequest request = new ClientUpgradeRequest();

            System.out.printf("Connecting to WS : %s%n", echoUri);
            client.connect(socket, echoUri, request);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String channel, String message){
        if(socket.isConnected()) {
            StompRequest request = new StompRequest(CommandType.SEND, channel).withBody(message);
            try {
                socket.sendAndWait( request );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribe(String channel, MessageListener listener){
        // TODO
    }

}
