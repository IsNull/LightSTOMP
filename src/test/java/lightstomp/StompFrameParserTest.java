package lightstomp;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by paba on 11/21/14.
 */
public class StompFrameParserTest {

    @Test
    public void testParseValidNoBody() throws StompParseException {


        StompFrame frame = new StompFrame(FrameType.CONNECT)
                                .withHeader("accept-version", "1.0,1.1,2.0")
                                        .withHeader("host", "mydomain.org")
                                        .withHeader("login", "nice")
                                        .withHeader("passcode", "evenbetter");

        String payload = frame.toString();

        StompFrameParser parser = new StompFrameParser();

        StompFrame frameParsed = parser.parse(payload);

        assertEquals("Command was not correct",frame.getType(), frameParsed.getType());
        assertEquals("Header was not correct","1.0,1.1,2.0", frameParsed.getHeaderValue("accept-version"));
        assertEquals("Header was not correct","evenbetter", frameParsed.getHeaderValue("passcode"));
    }

    @Ignore
    @Test
    public void testParseValidBody() throws StompParseException {


        StompFrame frame = new StompFrame(FrameType.CONNECT)
                .withHeader("accept-version", "1.0,1.1,2.0")
                .withHeader("host", "mydomain.org")
                .withHeader("login", "nice")
                .withHeader("passcode", "evenbetter")

                .withBody("This is an example body without any bad chars!");

        String payload = frame.toString();

        StompFrameParser parser = new StompFrameParser();

        StompFrame frameParsed = parser.parse(payload);

        assertEquals("Body was not correct!", frame.getBody(), frameParsed.getBody()  );
    }

}
