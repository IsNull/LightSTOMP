package lightstomp.impl;



import lightstomp.StompFrame;
import lightstomp.StompParseException;
import lightstomp.stompSocket.ISocketListener;
import lightstomp.stompSocket.IStompSocket;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Implements a simple STOMP over Websocket
 *
 * Created by paba on 11/17/14.
 */
public class StompWebSocket implements IStompSocket {

    private final URI server;
    private Session webSession;
    private ISocketListener listener;

    private final StompFrameParser parser = new StompFrameParser();



    public StompWebSocket(String url) throws URISyntaxException  {
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
                            onWebSocketMessageReceivedBinary(message);
                        }
                    });

                    session.addMessageHandler(new MessageHandler.Whole<String>() {
                        @Override
                        public void onMessage(String  message) {
                            onWebSocketMessageReceivedText(message);
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
                //session.getBasicRemote().sendBinary(frame.toByteBuffer());
                session.getBasicRemote().sendText(frame.toString());
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
        }
    }


    private void onWebSocketMessageReceivedBinary(byte[] message){
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

    private void onWebSocketMessageReceivedText(String message){
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
