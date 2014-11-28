package lightstomp;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by paba on 11/28/14.
 */
public class StompClientTest {


    private class TestResult {
        public boolean gotSuccess = false;
        public boolean gotError = false;
    }

    @Test
    public void testFailedConnection() throws StompParseException {

        final TestResult result = new TestResult();
        CountDownLatch c = new CountDownLatch(1);

        StompClient.connectOverWebSocket("iDoNotExisit", new ISTOMPListener() {
            @Override
            public void connectionSuccess(StompClient connection) {
                result.gotSuccess = true;
                c.countDown();
            }

            @Override
            public void connectionFailed() {
                result.gotError = true;
                c.countDown();
            }

            @Override
            public void disconnected() {
                c.countDown();
            }
        });

        try {
            c.await(5000, TimeUnit.MILLISECONDS);

            assertEquals("There was no error but one was expected!", result.gotError, true);
            assertEquals("There was an success but error was expected!", result.gotSuccess, false);

        } catch (InterruptedException e) {
            assertTrue("Waited 5sec and no answer from connection.", false);
        }


    }

}
