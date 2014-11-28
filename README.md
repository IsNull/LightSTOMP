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
 StompClient stompClient = StompClient.connectOverWebSocket("ws://localhost:8080/ws/messages");

 stompClient.addListener(new ISTOMPListener() {
     @Override
     public void stompConnected() {
        
         stompClient.subscribe("/topic/echo", message -> {
             LOG.info("STOMP server sent: " + message);
         });

         stompClient.stompSend("/echo", "hello world!");
     }

     @Override
     public void stompClosed() {
        LOG.info("bye!");
     }
 });
```



[ ![Download](https://api.bintray.com/packages/isnull/maven/LightSTOMP/images/download.svg) ](https://bintray.com/isnull/maven/LightSTOMP/_latestVersion)
