/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import bitmex.Bot.model.bitMEX.listener.IExecutionListener;
import bitmex.Bot.model.bitMEX.listener.IPositionListener;
import bitmex.Bot.model.bitMEX.listener.ITradeListener;
import bitmex.Bot.model.bitMEX.listener.IQuoteListener;
import bitmex.Bot.model.bitMEX.listener.IOrderListener;
import bitmex.Bot.model.bitMEX.listener.IPongListener;
import bitmex.Bot.model.bitMEX.entity.BitmexExecution;
import bitmex.Bot.model.bitMEX.entity.BitmexPosition;
import bitmex.Bot.model.bitMEX.entity.BitmexResponse;
import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.bitMEX.entity.BitmexTrade;
import java.util.concurrent.LinkedBlockingQueue;

import bitmex.Bot.view.LoggerHelper;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
//import org.apache.log4j.Logger;
//import org.slf4j.LoggerFactory;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import java.util.HashSet;
//import org.slf4j.Logger;
import java.util.Set;


/**
 *
 * @author RobTerpilowski
 */
public class WebsocketMessageProcessor implements Runnable, IMessageProcessor {

//    protected static Logger logger = LoggerFactory.getLogger(WebsocketMessageProcessor.class);
    protected LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    protected JsonParser parser = new JsonParser();
    protected volatile boolean shouldRun = false;
    protected Gson gson = new Gson();

    protected Set<IExecutionListener> executionListeners = new HashSet<>();
    protected Set<IPositionListener> positionListeners = new HashSet<>();
    protected Set<IQuoteListener> quoteListeners = new HashSet<>();
    protected Set<IOrderListener> orderListeners = new HashSet<>();
    protected Set<ITradeListener> tradeListeners = new HashSet<>();
    protected Set<IPongListener> pongListeners = new HashSet<>();

    @Override
    public void startProcessor() {
        shouldRun = true;
        Thread thread = new Thread(this, "WebsocketMessageProcessor");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public int getQueueSize() {
        return messageQueue.size();
    }
    
    
    @Override
    public void stopProcessor() {
        shouldRun = false;
    }

    @Override
    public void processMessage(String message) {
        messageQueue.add(message);
    }

    @Override
    public void addQuoteListener(IQuoteListener listener) {
        quoteListeners.add(listener);
    }

    @Override
    public void addPositionListener(IPositionListener listener) {
        positionListeners.add(listener);
    }

    @Override
    public void addOrderListener(IOrderListener listener) {
        orderListeners.add(listener);
    }

    @Override
    public void addTradeListener(ITradeListener listener) {
        tradeListeners.add(listener);
    }

    @Override
    public void addExecutionListener(IExecutionListener listener) {
        executionListeners.add(listener);
    }

    @Override
    public void addPongListener(IPongListener listener) {
        pongListeners.add(listener);
    }
    
    
    

    @Override
    public void run() {
        while (shouldRun) {
            processNextMessage();
            Thread.yield();
        }
    }

    protected void processNextMessage() {
        String message = "";
        try {
            message = messageQueue.take();
            LoggerHelper.writeError("Processor got message: " + message);
            if( message.equalsIgnoreCase("Pong")) {
                firePongReceived();
                return;
            }
            JsonElement element = parser.parse(message);
            if (element.isJsonObject()) {
                JsonElement table = element.getAsJsonObject().get("table");

                if (table != null) {
                    String tableString = table.getAsString();
                    if (tableString.equals("quote")) {
                        processQuote(message);
                    } else if (tableString.equals("position")) {
                        processPosition(message);
                    } else if (tableString.equals("order")) {
                        processOrder(message);
                    } else if (tableString.equals("trade")) {
                        processTrade(message);
                    } else if (tableString.equals("execution")) {
                        processExecution(message);
                    }
                }
            }
        } catch (JsonSyntaxException ex) {
            LoggerHelper.writeError(ex.getMessage() + " === " + ex);
            LoggerHelper.writeError("error parsing: " + message);
        }    catch (Exception ex) {
            LoggerHelper.writeError(ex.getMessage() + " === " + ex);
        }
    }

    protected void processQuote(String message) {
        BitmexResponse<BitmexQuote> quote = parseMessage(message, new TypeToken<BitmexResponse<BitmexQuote>>(){});
        LoggerHelper.writeError("Parsed response: " + quote);
        fireQuoteMessage(quote);
    }

    protected void processPosition(String message) {
        BitmexResponse<BitmexPosition> position = parseMessage(message, new TypeToken<BitmexResponse<BitmexPosition>>(){});
        LoggerHelper.writeError("Parsed response: " + position);
        firePositionMessage(position);
    }

    protected void processOrder(String message) {
        BitmexResponse<BitmexOrder> order = parseMessage(message, new TypeToken<BitmexResponse<BitmexOrder>>(){});
        LoggerHelper.writeError("Parsed response: " + order);
        fireOrderMessage(order);
    }

    protected void processTrade(String message) {
        BitmexResponse<BitmexTrade> trade = parseMessage(message, new TypeToken<BitmexResponse<BitmexTrade>>(){} );
        LoggerHelper.writeError("Parsed response: " + trade);
        fireTradeMessage(trade);
    }
    
    protected void processExecution(String message) {
        BitmexResponse<BitmexExecution> execution = parseMessage(message, new TypeToken<BitmexResponse<BitmexExecution>>(){} );
        LoggerHelper.writeError("Parsed response: " + execution);
        fireExecutionMessage(execution);
    }    

    protected <T> BitmexResponse parseMessage(String message, TypeToken type) {
        Type collectionType = type.getType();
        BitmexResponse response = gson.fromJson(message, collectionType);
        return response;
    }
    
    protected void firePongReceived() {
        synchronized(pongListeners) {
            for( IPongListener listener : pongListeners ) {
                try {
                    listener.pongReceived();
                } catch( Exception ex ) {
                    LoggerHelper.writeError(ex.getMessage() + " === " + ex);
                }
            }
        }
    }

    protected void fireQuoteMessage(BitmexResponse<BitmexQuote> response) {
        synchronized (quoteListeners) {
            for (IQuoteListener listener : quoteListeners) {
                for (BitmexQuote data : response.getData()) {
                    try {
                        listener.quoteUpdated(data);
                    } catch (Exception ex) {
                        LoggerHelper.writeError(ex.getMessage() + " === " + ex);
                    }
                }
            }
        }
    }

    protected void firePositionMessage(BitmexResponse<BitmexPosition> response) {
        synchronized (quoteListeners) {
            for (IPositionListener listener : positionListeners) {
                for (BitmexPosition data : response.getData()) {
                    try {
                        listener.positionUpdated(data);
                    } catch (Exception ex) {
                        LoggerHelper.writeError(ex.getMessage() + " === " + ex);
                    }
                }
            }
        }
    }

    protected void fireOrderMessage(BitmexResponse<BitmexOrder> response) {
        synchronized (quoteListeners) {
            for (IOrderListener listener : orderListeners) {
                for (BitmexOrder data : response.getData()) {
                    try {
                        listener.orderUpdated(data);
                    } catch (Exception ex) {
                        LoggerHelper.writeError(ex.getMessage() + " === " + ex);
                    }
                }
            }
        }
    }

    protected void fireTradeMessage(BitmexResponse<BitmexTrade> trade) {
        synchronized (quoteListeners) {
            for (ITradeListener listener : tradeListeners) {
                for (BitmexTrade data : trade.getData()) {
                    try {
                        listener.tradeUpdated(data);
                    } catch (Exception ex) {
                        LoggerHelper.writeError(ex.getMessage() + " === " + ex);
                    }
                }
            }
        }
    }
    
    protected void fireExecutionMessage(BitmexResponse<BitmexExecution> execution) {
        synchronized (quoteListeners) {
            for (IExecutionListener listener : executionListeners) {
                for (BitmexExecution data : execution.getData()) {
                    try {
                        listener.executionUpdated(data);
                    } catch (Exception ex) {
                        LoggerHelper.writeError(ex.getMessage() + " === " + ex);
                    }
                }
            }
        }
    }
}
