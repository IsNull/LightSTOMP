
import lightstomp.MessageListener;
import lightstomp.StompOverWSClient;
import org.glassfish.tyrus.client.ClientManager;


import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by paba on 11/17/14.
 */
public class TestStomp {

    public static void main(String[] args) {
        TestStomp test = new TestStomp();
        //test.testWSEcho(); // Working!

        //test.test();
        test.testStompEcho();

    }

    private void testStompEcho()  {
        StompOverWSClient stompClient = new StompOverWSClient("ws://echo.websocket.org");

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

    private static CountDownLatch messageLatch;
    private static final String SENT_MESSAGE = "Hello World";

    private void test(){
        try {
            messageLatch = new CountDownLatch(1);

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
                                messageLatch.countDown();
                            }
                        });
                        session.getBasicRemote().sendText(SENT_MESSAGE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, cec, new URI("ws://echo.websocket.org"));
            messageLatch.await(100, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
