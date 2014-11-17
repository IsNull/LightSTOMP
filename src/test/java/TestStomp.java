import lightstomp.MessageListener;
import lightstomp.SimpleEchoSocket;
import lightstomp.StompOverWSClient;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Created by paba on 11/17/14.
 */
public class TestStomp {

    public static void main(String[] args) {
        TestStomp test = new TestStomp();
        //test.testWSEcho(); // Working!

        test.testStompEcho();

    }

    private void testStompEcho(){
        StompOverWSClient stompClient = new StompOverWSClient("ws://localhost:8080/ws/rest/messages");

        stompClient.subscribe("/topic/echo", new MessageListener() {
            @Override
            public void messageReceived(String message) {
                System.out.println("STOMP server sent: " + message);
            }
        });

        stompClient.send("/echo", "hello world!");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testWSEcho(){
        String destUri = "ws://echo.websocket.org";
        WebSocketClient client = new WebSocketClient();
        SimpleEchoSocket socket = new SimpleEchoSocket();
        try {
            client.start();
            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            System.out.printf("Connecting to : %s%n", echoUri);
            socket.awaitClose(5, TimeUnit.SECONDS);
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

}
