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
StompClient.connectOverWebSocket("ws://myServer.com/messages", new ISTOMPListener() {
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

Maven Dependency
----------------
This library is deployed to my [bintray](https://bintray.com) repository.

[ ![Download](https://api.bintray.com/packages/isnull/maven/LightSTOMP/images/download.svg) ](https://bintray.com/isnull/maven/LightSTOMP/_latestVersion)

In order to use this dependency in your project, add the bintray repository:
```
        <repository>
            <id>bintray-isnull</id>
            <name>BinTray Repository (IsNull)</name>
            <url>http://dl.bintray.com/isnull/maven/</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
```

Then you can use the dependency as if it where on [Maven Central](http://search.maven.org/):
```
        <dependency>
            <groupId>light-stomp</groupId>
            <artifactId>light-stomp-client</artifactId>
            <version>0.1.3</version>
        </dependency>
```
