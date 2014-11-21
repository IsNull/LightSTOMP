package lightstomp;



import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Implements a simple STOMP over Websocket client in pure Java.
 *
 * Created by paba on 11/17/14.
 */
public class StompOverWSClient {


    private Session webSession;


    public StompOverWSClient(String url)  {
        final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();

        ClientManager client = ClientManager.createClient();
        try {
            try {
                client.connectToServer(new Endpoint() {
                    @Override
                    public void onOpen(Session session, EndpointConfig config) {
                        webSession = session;
                        session.addMessageHandler(new MessageHandler.Whole<String>() {
                            @Override
                            public void onMessage(String message) {
                                System.out.println("Received message: "+message);
                            }
                        });
                    }
                }, cec, new URI(url));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } catch (DeploymentException | IOException e) {
            e.printStackTrace(); // TODO
        }
    }

    public void send(String channel, String message){
        Session session = webSession;
        StompFrame request = new StompFrame(CommandType.SEND, channel).withBody(message);

        if(session != null && session.isOpen()){
            try {
                session.getBasicRemote().sendText(request.toString());
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
        }
    }

    public void subscribe(String channel, MessageListener listener){
        // TODO
    }

}
