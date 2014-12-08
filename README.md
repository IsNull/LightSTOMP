LightSTOMP
==========

A lightweight [STOMP](https://stomp.github.io/index.html) client for Java.

Features
--------

* Provides a STOMP over WebSocket client for Java.
* Currently, it is a minimal subset of the STOMP protocol, supporting frames:
  * CONNECT / CONNECTED
  * SUBSCRIBE
  * MESSAGE
  * SEND


How to use
----------

```java
StompClient.connectOverWebSocket(url, new ISTOMPListener() {
    @Override
    public void connectionSuccess(StompClient connection) {
        // Successful connected

        connection.subscribe("/topic/echo", message -> {
            // Simple echo subscription to test
            LOG.info("STOMP server sent: " + message);
        });

        connection.stompSend("/app/echo", "hello world!");
    }

    @Override
    public void connectionFailed(Throwable e) {
        LOG.error("Could not connect!", e);
    }

    @Override
    public void disconnected(String reason) {
        LOG.error("Lost connection: " + reason);
    }
});
```



[ ![Download](https://api.bintray.com/packages/isnull/maven/LightSTOMP/images/download.svg) ](https://bintray.com/isnull/maven/LightSTOMP/_latestVersion)
