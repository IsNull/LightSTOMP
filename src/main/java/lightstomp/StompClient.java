package lightstomp;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;

/**
 * Created by paba on 11/17/14.
 */
public class StompClient {

    private final STOMPSocket socket;

    public StompClient(String url){
        WebSocketClient client = new WebSocketClient();
        socket = new STOMPSocket();
        try {
            client.start();
            URI echoUri = new URI(url);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            System.out.printf("Connecting to : %s%n", echoUri);
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
            socket.sendString("");
        }
    }

    public void subscribe(String channel, MessageListener listener){
        // TODO
    }

}
