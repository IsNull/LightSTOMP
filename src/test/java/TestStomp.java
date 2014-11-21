
import lightstomp.MessageListener;
import lightstomp.StompOverWSClient;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by paba on 11/17/14.
 */
public class TestStomp {

    public static void main(String[] args) throws URISyntaxException {
        TestStomp test = new TestStomp();
        //test.testWSEcho(); // Working!


        URI uri1 = new URI("ws://echo.websocket.org");
        URI uri2 = new URI("wss://echo.websocket.org");
        URI uri3 = new URI("ws://localhost:8080/ws/rest/echo2");
        test.testWebSocket(uri3);
        //test.testStompEcho();

    }

    private void testStompEcho()  {
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

    private static final String SENT_MESSAGE = "Hello World";

    private void testWebSocket(URI testEchoUrl){
        try {
            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();

            ClientManager client = ClientManager.createClient();
            client.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    try {
                        session.addMessageHandler(new MessageHandler.Whole<String>() {

                            @Override
                            public void onMessage(String message) {
                                System.out.println("Received message: "+message);
                            }
                        });
                        session.getBasicRemote().sendText(SENT_MESSAGE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, cec, testEchoUrl);
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
