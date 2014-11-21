package lightstomp;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by paba on 11/21/14.
 */
public class StompFrameParser {

    class Pair<T> {
        public Pair(T v, T v2){
            right = v;
            left = v2;
        }
        public T right;
        public T left;
    }

    public StompFrame parse(String payload) throws StompParseException{

        if(payload == null) throw new IllegalArgumentException("payload must not be NULL!");

        StompFrame frame = null;
        char[] payloadChr = payload.toCharArray();

        int p = 0;
        boolean inCommand = true;
        boolean inHeader = false;
        boolean previousWasEOL = false;
        boolean bodyStart = false;


        FrameType type = null;

        try {

            for (int i = 0; i <= payloadChr.length; i++) {
                boolean consumed = false;
                if (payloadChr[i] == StompFrame.PROTOCOL_EOL) {
                    if (!previousWasEOL) {
                        previousWasEOL = true;

                        if (inCommand) {
                            type = parseFrameType(payload, p, i);
                            frame = new StompFrame(type);
                            inCommand = false;
                            inHeader = true;
                            consumed = true;
                        } else if (inHeader) {
                            Pair<String> header = parseHeader(payload, p, i);
                            frame.withHeader(header.right, header.left);
                            inHeader = true;
                            consumed = true;
                        } else {

                        }
                    } else {
                        // Two EOLs - Body starts OR we are done

                        // TODO check if we dont have a body!
                        String body = copyBody(payload, p, i);
                        frame.withBody(body);
                        consumed = true;
                        break;
                    }
                } else if (payloadChr[i] == StompFrame.PROTOCOL_END) {
                    previousWasEOL = false;
                    break;
                }else{
                    previousWasEOL = false;
                }

                if(consumed){
                    p = i + 1;
                }
            }
        }catch (Throwable cause){
            throw new StompParseException("Can not parse message:" + System.lineSeparator() + payload, cause);
        }

        return frame;
    }

    private Pair<String> parseHeader(String payload, int start, int end) {
        String word = copyWord(payload, start, end);
        System.out.println("header: " + word);
        String[] parts = word.split(":");
        return new Pair<>(parts[0], parts[1]);
    }

    private FrameType parseFrameType(String payload, int start, int end){
        String word = copyWord(payload, start, end);
        return Enum.valueOf(FrameType.class, word);
    }

    private String copyWord(String payload, int start, int end) {
        String word = payload.substring(start, end);
        System.out.println(word);
        return word;
    }

    private String copyBody(String payload, int start, int end) {
        return payload.substring(start, end-1); // remove the EOF NULL
    }

}
