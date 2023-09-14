
import lightstomp.ISTOMPListener;
import lightstomp.MessageListener;
import lightstomp.StompClient;
import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

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

        //test.testStompEcho(uri1.toString());
        //test.testWebSocket(uri1);
    }

    private String raceTrackId = "simulator1";

    private void testStompEcho(String url)  {
            LOG.info("Connecting to " + url);
            StompClient.connectOverWebSocket(url, new ISTOMPListener() {
                @Override
                public void connectionSuccess(StompClient connection) {
                    LOG.info("Stomp connected!");

                    connection.subscribe("/topic/echo", new MessageListener() {
						@Override
						public void messageReceived(String message, Map<String, String> headers) {
						}
						@Override
						public void messageReceived(String message) {
							LOG.info("STOMP server sent: " + message);							
						}
					});

                    connection.subscribe("/topic/simulators/"+ raceTrackId + "/news", new MessageListener() {
						@Override
						public void messageReceived(String message, Map<String, String> headers) {
						}
						@Override
						public void messageReceived(String message) {
							LOG.info("Got News: " + message);					
						}
					});

                    connection.stompSend("/stomp/msg", "hello world!");
                }

                @Override
                public void connectionFailed(Throwable e) {
                    LOG.error("Could not connect!", e);
                }

                @Override
                public void disconnected(String reason) {
                    LOG.error("Lost connection: " + reason);
                }
            });

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

            LOG.info("Connecting to " + testEchoUrl);

            client.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    try {
                        session.addMessageHandler(new MessageHandler.Whole<String>(){
                            @Override
                            public void onMessage(String s) {
                                LOG.info("Received message: " + s);
                            }
                        });

                        session.getBasicRemote().sendText(SENT_MESSAGE);
                    } catch (IOException e) {
                        LOG.error("Failed to send message", e);
                    }
                }

                public void onClose(javax.websocket.Session session, javax.websocket.CloseReason closeReason) {
                    LOG.error("Closed websocket: " + closeReason.getReasonPhrase());
                }

                public void onError(javax.websocket.Session session, java.lang.Throwable thr) {
                    LOG.error("onError websocket: ", thr);
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
