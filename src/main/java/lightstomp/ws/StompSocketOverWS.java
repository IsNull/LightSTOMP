package lightstomp.ws;



import lightstomp.StompFrame;
import lightstomp.StompFrameParser;
import lightstomp.StompParseException;
import lightstomp.stompSocket.ISocketListener;
import lightstomp.stompSocket.IStompSocket;
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
public class StompSocketOverWS implements IStompSocket {

    private final URI server;
    private Session webSession;
    private ISocketListener listener;

    private final StompFrameParser parser = new StompFrameParser();



    public StompSocketOverWS(String url) throws URISyntaxException  {
        server = new URI(url);
    }

    public void connect(){
        final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
        ClientManager client = ClientManager.createClient();
        try {

            client.connectToServer(new Endpoint() {
                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    webSession = session;
                    session.addMessageHandler(new MessageHandler.Whole<byte[]>() {
                        @Override
                        public void onMessage(byte[]  message) {
                            onWebSocketMessageReceived(message);
                        }
                    });

                    listener.connected();

                }
            }, cec, server);

        } catch (DeploymentException | IOException e) {
            e.printStackTrace(); // TODO
            listener.disconnected();
        }
    }

    @Override
    public void setFrameListener(ISocketListener listener){
        this.listener = listener;
    }

    @Override
    public String getHost() {
        return server.getHost();
    }

    @Override
    public boolean isConnected() {
        Session session = webSession;
        return session != null && session.isOpen();
    }

    @Override
    public void sendFrame(StompFrame frame){
        Session session = webSession;
        if(session != null && session.isOpen()){
            try {
                session.getBasicRemote().sendBinary(frame.toByteBuffer());
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
        }
    }


    private void onWebSocketMessageReceived(byte[] message){
        System.out.println("Received message: "+message); // TODO
        StompFrame frame = null;
        try {

            frame = parser.parse(message);

            if(listener != null) {
                listener.onStompFrameReceived(frame);
            }

        } catch (StompParseException e) {
            // TODO
            e.printStackTrace();
        }
    }



}
