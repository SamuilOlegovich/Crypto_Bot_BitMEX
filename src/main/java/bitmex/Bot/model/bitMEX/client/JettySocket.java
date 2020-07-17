/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import bitmex.Bot.model.bitMEX.listener.WebsocketDisconnectListener;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import bitmex.Bot.model.bitMEX.listener.IPongListener;
//import com.sumzerotrading.data.SumZeroException;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import bitmex.Bot.view.ConsoleHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import com.google.gson.JsonParser;
//import org.apache.log4j.Logger;
//import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import java.util.TimerTask;
//import org.slf4j.Logger;
import java.util.Timer;

/**
 *
 * @author RobTerpilowski
 */
@WebSocket(maxTextMessageSize = (64 * 1024 * 100), maxBinaryMessageSize = -1)
//@WebSocket
public class JettySocket implements IPongListener {

    protected final long MAX_PONG_TIME_SECONDS = 20;
    
//    public static Logger logger = LoggerFactory.getLogger(JettySocket.class);//////////////
    protected WebsocketDisconnectListener disconnectListener = null;
    protected JsonParser parser = new JsonParser();
    protected IMessageProcessor messageProcessor;
    protected volatile boolean shouldRun = true;
    protected final CountDownLatch closeLatch;
    protected boolean connected = false;
    protected Gson gson = new Gson();
    protected long lastPongTime = 0;
    protected Timer pingPongTimer;



    @SuppressWarnings("unused")
    protected Session session;
    
    //Used by unit tests
    protected JettySocket() {
        this.closeLatch = null;
    }

    public JettySocket(CountDownLatch latch, IMessageProcessor messageProcessor) {
        this(latch, messageProcessor, null);
    }
    
    public JettySocket(CountDownLatch latch, IMessageProcessor messageProcessor, WebsocketDisconnectListener disconnectListener) {
        this.closeLatch = latch;
        this.messageProcessor = messageProcessor;
        this.disconnectListener = disconnectListener;
        pingPongTimer = getPingPongTimer();
        pingPongTimer.scheduleAtFixedRate(getPingPongTimerTask(), 0, 10000);
    }    

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }
    
    
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        ConsoleHelper.writeERROR("Connection Closed: code: " + statusCode + " reason: " + reason);
        shouldRun = false;
        this.session = null;
        connected = false;
        if( disconnectListener != null ) {
            disconnectListener.socketDisconnectDetected();
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        connected = true;
        closeLatch.countDown();
        startPing();
    }

    public void startPing() {
        final String pingCommand = "ping";
        Thread thread = new Thread(() -> {
            while (shouldRun) {
                try {
                    if (session != null) {
                        Future<Void> fut = session.getRemote().sendStringByFuture(pingCommand);
                        ConsoleHelper.writeINFO("Sending ping");//////////////////////////
                    }
                    Thread.sleep(10000);
                } catch (Exception ex) {
                    ConsoleHelper.writeERROR(Arrays.toString(ex.getStackTrace()));
                }

            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        ConsoleHelper.writeINFO("msg: " + msg);//////////////////////////////
        messageProcessor.processMessage(msg);
    }

    @Override
    public void pongReceived() {
        lastPongTime = getCurrentTime();
    }

    
    
    public boolean isConnected() {
        return connected;
    }

    public void subscribe(String message) {
        ConsoleHelper.writeINFO("Sending command: " + message);/////////////////////////
        Future<Void> fut = session.getRemote().sendStringByFuture(message);
        try {
            fut.get(2, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ConsoleHelper.writeERROR(Arrays.toString(ex.getStackTrace()));

        }
    }
    
    protected Timer getPingPongTimer() {
        return new Timer("PingPongTimer", true);
    }
    
    protected TimerTask getPingPongTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    checkLastPongTime();
                } catch( Exception ex ) {
                    ConsoleHelper.writeERROR(ex.getMessage() + " === " + ex);
                }
            }
        };
    }

    
    protected void checkLastPongTime() {
        if( lastPongTime > 0 ) {
            long responseDelay = getCurrentTime() - lastPongTime;
            if( responseDelay >= (MAX_PONG_TIME_SECONDS * 1000) ) {
                ConsoleHelper.writeDEBUG("Pong not detected in " + responseDelay + "ms");
                disconnectListener.socketDisconnectDetected();
            }
        } else {
            ConsoleHelper.writeINFO("Last pong time has not been set");///////////////////////
        }
    }
    
    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
