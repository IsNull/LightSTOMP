
import lightstomp.ISTOMPListener;
import lightstomp.StompClient;
import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by paba on 11/17/14.
 */
public class TestStomp {

    private final static Logger LOG = LoggerFactory.getLogger(TestStomp.class);


    public static void main(String[] args) throws URISyntaxException {
        TestStomp test = new TestStomp();
        //test.testWSEcho(); // Working!


        URI uri1 = new URI("ws://echo.websocket.org");
        URI uri2 = new URI("wss://echo.websocket.org");
        URI uri3 = new URI("ws://localhost:8080/ws/rest/echo2");
        //test.testWebSocket(uri3);

        test.testStompEcho("ws://localhost:8080/ws/rest/messages");
    }

    private String raceTrackId = "simulator1";

    private void testStompEcho(String url)  {
        try {
            LOG.info("Connecting to " + url);
            StompClient stompClient = StompClient.connectOverWebSocket(url);

            stompClient.addListener(new ISTOMPListener() {
                @Override
                public void stompConnected() {

                    LOG.info("Stomp connected!");

                    stompClient.subscribe("/topic/echo", message -> {
                        LOG.info("STOMP server sent: " + message);
                    });

                    stompClient.subscribe("/topic/simulators/"+ raceTrackId + "/news", message -> {
                        LOG.info("Got News: " + message);
                    });

                    stompClient.stompSend("/echo", "hello world!");
                }

                @Override
                public void stompClosed() {

                }
            });


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.println("Stomp client running... " +
                "Press any key to quit.");
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
                        session.addMessageHandler((MessageHandler.Whole<String>) message -> LOG.info("Received message: " + message));
                        session.getBasicRemote().sendText(SENT_MESSAGE);
                    } catch (IOException e) {
                        LOG.error("Failed to send message", e);
                    }
                }
            }, cec, testEchoUrl);

            // wait
            try {
                System.in.read();
            } catch (IOException e) {
                LOG.error("", e);
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
    }



}
