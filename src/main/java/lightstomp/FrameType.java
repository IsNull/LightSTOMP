package lightstomp;

/**
 * Created by paba on 11/17/14.
 */
public enum FrameType {
    CONNECT,
    SEND,
    SUBSCRIBE,
    UNSUBSCRIBE,
    BEGIN,
    COMMIT,
    ABORT,
    ACK,
    NACK,
    DISCONNECT,

    CONNECTED,
    RECEIPT,
    ERROR,
    MESSAGE;

}
