package lightstomp;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by paba on 11/21/14.
 */
public class StompFrameTest {

    @Test
    public void testFrameEOF() throws StompParseException {
        StompFrame frame = new StompFrame(FrameType.CONNECT);
        byte[] frameRaw = frame.toByteBuffer().array();
        int indexOfEOF = frameRaw.length -1;
        assertEquals(StompFrame.PROTOCOL_END, frameRaw[indexOfEOF]);
    }



}
