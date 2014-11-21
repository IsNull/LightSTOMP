package lightstomp;

import lightstomp.stompSocket.ISocketListener;
import lightstomp.stompSocket.IStompSocket;
import lightstomp.ws.StompSocketOverWS;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple STOMP client.
 *
 */
public class StompClient {

    /**
     * Builds a Stomp Client which uses STOMP over Websocket
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    public static StompClient stompOverWebSocket(String uri) throws URISyntaxException {
        return new StompClient(new StompSocketOverWS(uri));
    }


    private final IStompSocket socket;
    private final Map<String, List<MessageListener>> subscriptionMap = new HashMap<>();

    private boolean isConnected = false;


    /**
     * Creates a new StompClient using the given socket
     * @param socket
     */
    private StompClient(IStompSocket socket){
        this.socket = socket;
        socket.setFrameListener(
                new ISocketListener(){
                    @Override
                    public void connected() {
                        // The Socket has connected
                        stompConnect("admin", "admin");
                    }

                    @Override
                    public void onStompFrameReceived(StompFrame frame) {
                        stompFrameReceived(frame);
                    }

                    @Override
                    public void disconnected() {
                        // The underling socket died
                        isConnected = false;
                    }
                }
        );

        this.socket.connect();
    }

    /**
     * Returns true if the STOMP protocol has had a successful handshake
     * @return
     */
    public boolean isConnected() {
        return isConnected;
    }

    public void stompSend(String channel, String message){
        StompFrame request = new StompFrame(
                FrameType.SEND, channel)
                .withBody(message);
        socket.sendFrame(request);
    }


    public void subscribe(String channel, MessageListener listener){

        String id = "0";// TODO

        StompFrame request = new StompFrame(FrameType.SUBSCRIBE)
                .withHeader("id", "0")
                .withHeader("destination", channel)
                ;

        mapSubscription(channel + id, listener);

        socket.sendFrame(request);
    }


    private void mapSubscription(String subscriptionId, MessageListener listener){
        List<MessageListener> listeners = subscriptionMap.get(subscriptionId);
        if(listeners == null){
            listeners = new ArrayList<>();
            subscriptionMap.put(subscriptionId, listeners);
        }
        listeners.add(listener);
    }




    private void stompConnect(String user, String password) {
        StompFrame request = new StompFrame(FrameType.CONNECT)
                .withHeader("accept-version", "1.0,1.1,2.0")
                .withHeader("host", socket.getHost())
                .withHeader("login", user)
                .withHeader("passcode", password);

        socket.sendFrame(request);
    }

    /**
     * Occurs when we receive a stomp from the server
     * @param frame
     */
    private void stompFrameReceived(StompFrame frame){
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
        }
    }

    private void handleServerMessage(StompFrame frame) {
        // TODO
    }

    private void handleServerReceipt(StompFrame frame) {
        // TODO
    }

    private void handleServerError(StompFrame frame) {
        // TODO
        System.err.println("Received Error!" + frame.getHeaderValue("message"));
    }

    private void handleServerConnected(StompFrame frame) {
        isConnected = true;
    }


}
