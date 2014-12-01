package lightstomp;

import lightstomp.stompSocket.ISocketListener;
import lightstomp.stompSocket.IStompSocket;
import lightstomp.impl.StompWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Consumer;

/**
 * A simple STOMP client.
 *
 */
public class StompClient {

    private final static Logger LOG = LoggerFactory.getLogger(StompClient.class);

    /**
     * Builds a Stomp Client which uses STOMP over Websocket
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    public static StompClient connectOverWebSocket(String uri, ISTOMPListener listener) {
        return connectOverWebSocket(uri, null, null, listener);
    }

    /**
     * Builds a Stomp Client which uses STOMP over Websocket
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    public static StompClient connectOverWebSocket(String uri, String user, String password, ISTOMPListener listener) {
        StompWebSocket socket = null;
        try {
            URI serverUrl = new URI(uri);
            socket = new StompWebSocket(serverUrl);
        } catch (URISyntaxException e) {
            LOG.error("Could not parse URI!",e);
            listener.connectionFailed();
        }
        return new StompClient(socket, user, password, listener);
    }


    private final IStompSocket socket;
    private final SubscriptionRouter subscriptionRouter = new SubscriptionRouter();
    private final ISTOMPListener listener;

    private boolean isConnected = false;


    /**
     * Creates a new StompClient using the given socket
     * @param socket A STOMP socket implementation to wrap this client around.
     * @param user The STOMP user (can be left null if not required)
     * @param password The password for the STOMP user
     * @param listener A listener which receives basic protocol events such as connected / disconnected.
     */
    private StompClient(IStompSocket socket, String user, String password, ISTOMPListener listener){
        this.listener = listener;
        this.socket = socket;

        this.socket.connect(new ISocketListener(){
            @Override
            public void connected() {
                // The underling socket has connected, now we can connect with the
                // STOMP protocol
                stompConnect(user, password);
            }

            @Override
            public void onStompFrameReceived(StompFrame frame) {
                stompFrameReceived(frame);
            }

            @Override
            public void closed(String reason) {
                // The underling socket died
                LOG.warn("Underling Websocket has closed: " + reason);
                handleServerDisconnected();
            }

            @Override
            public void connectionFailed(Throwable e) {
                LOG.warn("Underling Websocket could not connect!", e);
                handleCanNotConnect();
            }
        });
    }

    /**
     * Returns true if the STOMP protocol has had a successful handshake
     * @return
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Send the given text message to the given channel
     * @param channel
     * @param message
     */
    public void stompSend(String channel, String message){
        StompFrame request = new StompFrame(
                FrameType.SEND, channel)
                .withBody(message);
        sendStompFrame(request);
    }

    /**
     * Subscribe to the given channel.
     * For all subsequent messages sent to the specified channel, the given message listener will be invoked.
     * @param channel
     * @param listener
     */
    public void subscribe(String channel, MessageListener listener){

        // We use a global unique id for the subscription to simplify subscription mapping on
        // our side.
        String subscriptionId = UUID.randomUUID().toString();

        StompFrame subscriptionRequest = new StompFrame(FrameType.SUBSCRIBE)
                .withHeader("id", subscriptionId)
                .withHeader("destination", channel)
                .withHeader("ack", "auto") // The client does not send ACK for received messages
                ;
        sendStompFrame(subscriptionRequest);

        subscriptionRouter.register(channel, subscriptionId, listener);
    }




    private void stompConnect(String user, String password) {
        StompFrame request = new StompFrame(FrameType.CONNECT)
                .withHeader("accept-version", "1.0,1.1,2.0")
                .withHeader("host", socket.getHost());

        if(user != null) {
            request.withHeader("login", user);
        }
        if(password != null) {
            request.withHeader("passcode", password);
        }

        sendStompFrame(request);
    }

    private void sendStompFrame(StompFrame request){
        socket.sendFrame(request);
    }

    /**
     * Occurs when we receive a stomp from the server
     * @param frame
     */
    private void stompFrameReceived(StompFrame frame){

        LOG.info("Received Stompframe " + frame);

        switch (frame.getType()){

            case CONNECTED:
                handleServerConnected(frame);
                break;

            case ERROR:
                handleServerError(frame);
                break;

            case RECEIPT:
                handleServerReceipt(frame);
                break;

            case MESSAGE:
                handleServerMessage(frame);
                break;

            default:
                LOG.error("Unexpected STOMPFrame-COMMAND received: " + frame.getType());
        }
    }

    private void handleServerMessage(StompFrame frame) {
        String channel = frame.getHeaderValue("destination");
        if(channel != null){
            subscriptionRouter.routeMessage(channel, frame.getBody());
        }else{
            LOG.warn("Message frame was missing destination!");
        }
    }

    private void handleServerReceipt(StompFrame frame) {
        LOG.error("Receipt handling not supported in this version!");
    }

    private void handleServerError(StompFrame frame) {
        // TODO
        LOG.warn("Received Error - connection will die now!"  + System.lineSeparator() + frame);
        handleServerDisconnected();
    }

    private void handleServerConnected(StompFrame frame) {
        isConnected = true;
        fireAll(l -> l.connectionSuccess(this));
    }

    private void handleServerDisconnected(){
        isConnected = false;
        fireAll(l -> l.disconnected());
    }

    private void handleCanNotConnect() {
        isConnected = false;
        fireAll(l -> l.connectionFailed());
    }

    private void fireAll(Consumer<ISTOMPListener> action){
        action.accept(listener);
    }


}
