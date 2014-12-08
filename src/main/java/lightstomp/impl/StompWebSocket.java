package lightstomp.impl;



import lightstomp.StompFrame;
import lightstomp.StompParseException;
import lightstomp.stompSocket.ISocketListener;
import lightstomp.stompSocket.IStompSocket;
import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final static Logger LOG = LoggerFactory.getLogger(StompWebSocket.class);


    private final URI server;
    private Session webSession;
    private ISocketListener listener;

    private final StompFrameParser parser = new StompFrameParser();



    public StompWebSocket(URI url)   {
        server = url;
    }

    public void connect(ISocketListener listener){

        this.listener = listener;

        final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
        ClientManager client = ClientManager.createClient();
        try {

            client.connectToServer(new Endpoint() {
                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    webSession = session;
                    session.addMessageHandler(new MessageHandler.Whole<byte[]>(){ // WARNING: DO NOT USE LAMBDA HERE!
                        @Override
                        public void onMessage(byte[] bytes) {
                            onWebSocketMessageReceivedBinary(bytes);
                        }
                    });

                    session.addMessageHandler(new MessageHandler.Whole<String>(){ // WARNING: DO NOT USE LAMBDA HERE!
                        @Override
                        public void onMessage(String s) {
                            onWebSocketMessageReceivedText(s);
                        }
                    });

                    listener.connected();

                }

                public void onClose(javax.websocket.Session session, javax.websocket.CloseReason closeReason) {
                    listener.closed(closeReason.getCloseCode() +": "+ closeReason.getReasonPhrase());
                }

                public void onError(javax.websocket.Session session, java.lang.Throwable thr) {
                    listener.closed(thr.getClass().getCanonicalName() + ": " + thr.getMessage());
                }

            }, cec, server);

        } catch (DeploymentException | IOException e) {
            LOG.error("Failed to connect to "+server+" -> " + e.getMessage() );
            listener.connectionFailed(e);
        }
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
                LOG.error("Failed to send STOMP Frame!", e);
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
            LOG.error("Illegal STOMP Frame received!", e);
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
            LOG.error("Illegal STOMP Frame received!", e);
        }
    }




}
