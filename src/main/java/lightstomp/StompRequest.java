package lightstomp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paba on 11/17/14.
 */
public class StompRequest {

    private final CommandType command;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public StompRequest(CommandType command, String channel){
        this.command = command;
        withHeader("destination", channel);
    }

    public StompRequest(CommandType command){
        this.command = command;
    }

    public StompRequest withHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public StompRequest withBody(String message) {
        this.body = message;
        return this;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        final String eol = "\n";
        final char NullChar = '\u0000';

        sb.append(command.toString()).append(eol);

        for(Map.Entry<String, String> header : headers.entrySet()){
            sb.append(header.getKey()).append(":").append(header.getValue()).append(eol);
        }
        sb.append(eol);

        if(body != null){
            sb.append(body);
        }
        sb.append(NullChar);

        return sb.toString();
    }


}
