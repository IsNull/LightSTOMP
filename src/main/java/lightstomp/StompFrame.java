package lightstomp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paba on 11/17/14.
 */
public class StompFrame {

    private final CommandType command;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public StompFrame(CommandType command, String channel){
        this.command = command;
        withHeader("destination", channel);
    }

    public StompFrame(CommandType command){
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

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        final char linefeed = 0x0A;
        final char NullChar = 0x00;

        sb.append(command.toString()).append(linefeed);

        for(Map.Entry<String, String> header : headers.entrySet()){
            sb.append(header.getKey()).append(":").append(header.getValue()).append(linefeed);
        }
        sb.append(linefeed);

        if(body != null){
            sb.append(body);
        }
        sb.append(NullChar);

        return sb.toString();
    }


}
