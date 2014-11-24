package lightstomp;

import java.util.*;

/**
 *
 *
 *
 * Created by paba on 11/24/14.
 */
public class SubscriptionRouter {

    private final Map<String, MessageListener> uuidToListenerMap = new HashMap<>();
    private final Map<String, List<MessageListener>> channelToListenerMap = new HashMap<>();


    /**
     * Register a route
     * @param channel The channel
     * @param subscriptionId THe UNIQUE ID for this listener
     * @param listener
     */
    public synchronized void register(String channel, String subscriptionId, MessageListener listener){

        // Unique id map
        uuidToListenerMap.put(subscriptionId, listener);

        // Channel map
        List<MessageListener> listeners = channelToListenerMap.get(channel);
        if(listeners == null){
            listeners = new ArrayList<>();
            channelToListenerMap.put(channel, listeners);
        }
        listeners.add(listener);
    }

    public synchronized void routeMessage(String channel, String message){
        List<MessageListener> listeners = channelToListenerMap.get(channel);
        if(listeners != null){
            for(MessageListener l : listeners){
                l.messageReceived(message);
            }
        }
    }

}
