This repository contains a simple proof-of-concept Web application. 

The web application's scope in itself is very simple: it is the Tomcat7 websocket chat example, which can be found under `tomcat-path/examples/websocket/chat.html`.

In addition to the standard websocket example, this proof-of-concept app uses the Guice architecture implemented to persist chat logs to the DB. That's all.

Objectives and Structure
-

The objective is to be able to replicate the inability to use Tomcat7 web sockets with the Guice server-side architecture put in place in this architecture.

The structure of the project is divided in a `core` and in a `web` part:

* `core` implements the DAO layer, which handles the lifecycle of `EntityManager`s, the definition of entities that represent the DB tables, and general DB interaction

* `web` takes care of:

    * implementing the `Handler` layer and servlets utilizing it; a Handler in this context refers to an HTTP handler class in which the common HTTP variables, namely the request, response and session variables, are injected

    * implementing any servlet; in this project's `MainSocket` class extending `WebSocketServlet` is implemented


Maven/Eclipse/Tomcat7 Setup
-

*Steps:*

Navigate to core folder and execute:

    cd poc-core
    rm -rf .project .classpath .settings/
    mvn clean install
    mvn eclipse:eclipse

Success of the above operation ensures the `core` part builds successfully and sets up for an Eclipse Java project.

Navigate to the project root:

    cd ..
    rm -rf .project .classpath .settings/
    mvn clean install

This prepares for the build of the `web` part; execute:

    cd poc-web
    rm -rf .project .classpath .settings/
    mvn clean install
    mvn eclipse:eclipse -Dwtpversion=2.0;

The last command creates an Eclipse Java Web project.

*About web sockets*

This [wiki article](https://www.assembla.com/spaces/desktop_extjs_cif/wiki/Websockets) should answer any questions with regards to web sockets usage (in the *cif desktop*).

*Tomcat7 in Eclipse*

This [link](http://lackovic.wordpress.com/2012/05/31/set-up-eclipse-and-tomcat-7-on-ubuntu-12-04-to-create-java-restful-web-services-with-jersey/) was *more or less* followed to set up Tomcat7 locally within Eclipse IDE. But the guiding principle for this setup was to follow these steps:

1. download Tomcat7 
2. run Tomcat7 via Eclipse using the IDE's *New server wizard*
3. drag the web part `poc-web` project onto the Tomcat7 server in the `Servers` tab

The web part should run under `/poc-web/` under Tomcat7. For example, if your Tomcat7 runs at `localhost:8080`, the web application will run under `poc-web`. `poc-web` should have `poc-core` as one of its referenced libraries if the steps in *Maven Project Setup* were executed correctly.


Exception
-

The exception that is raised results in the following traceback:

    SEVERE: Error reading request, ignored
    java.lang.IllegalStateException: Attempting to execute an operation on a closed EntityManager.
    	at org.eclipse.persistence.internal.jpa.EntityManagerImpl.verifyOpen(EntityManagerImpl.java:1704)
    	at org.eclipse.persistence.internal.jpa.EntityManagerImpl.persist(EntityManagerImpl.java:446)
    	at com.cif.dt.domain.dao.AbstractDAO.insert(AbstractDAO.java:22)
    	at com.google.inject.persist.jpa.JpaLocalTxnInterceptor.invoke(JpaLocalTxnInterceptor.java:66)
    	at com.cif.dt.domain.dao.AbstractDAO.insert(AbstractDAO.java:12)
    	at com.cif.dt.app.chatlog.ChatLogHandlerImpl.logChatMsg(ChatLogHandlerImpl.java:25)
    	at com.cif.dt.app.websocket.MainSocket$SocketMessageInbound.onTextMessage(MainSocket.java:98)
    	at org.apache.catalina.websocket.MessageInbound.onTextData(MessageInbound.java:74)
    	at org.apache.catalina.websocket.StreamInbound.doOnTextData(StreamInbound.java:186)
    	at org.apache.catalina.websocket.StreamInbound.onData(StreamInbound.java:134)
    	at org.apache.coyote.http11.upgrade.UpgradeProcessor.upgradeDispatch(UpgradeProcessor.java:83)
    	at org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:583)
    	at org.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:310)
    	at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)
    	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)
    	at java.lang.Thread.run(Thread.java:662)

The reason for this exception is because the injected ChatLogHandler reference variable is *not* used from within the usual *servlet Singleton*, but rather it is used in one of the methods of a newly-instanciated instance of class `SocketMessageInbound` (see method `createWebSocketInbound`).

A more in detail description of the problem can be found on the [question posted on stackoverflow](http://stackoverflow.com/questions/13958143/tomcat-websocketservlet-and-google-guice).