package lightstomp;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a STOMP Frame as described in
 * @link {http://stomp.github.io/stomp-specification-1.1.html#STOMP_Frames}
 *
 */
public class StompFrame {

    public final static char PROTOCOL_EOL = 0x0A;
    public final static char PROTOCOL_END = 0x00;



    private final FrameType command;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public StompFrame(FrameType command, String channel){
        this.command = command;
        withHeader("destination", channel);
    }

    public StompFrame(FrameType command){
        this.command = command;
    }

    public StompFrame withHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public StompFrame withBody(String message) {
        this.body = message;
        return this;
    }

    public FrameType getType(){
        return command;
    }

    /**
     * Returns the value of the requested header, or null if not present
     * @param key
     * @return
     */
    public String getHeaderValue(String key) {
        return headers.get(key);
    }



    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(command.toString()).append(PROTOCOL_EOL);

        for(Map.Entry<String, String> header : headers.entrySet()){
            sb.append(header.getKey()).append(":").append(header.getValue()).append(PROTOCOL_EOL);
        }
        sb.append(PROTOCOL_EOL);

        if(body != null){
            sb.append(body);
        }
        sb.append(PROTOCOL_END);

        return sb.toString();
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
