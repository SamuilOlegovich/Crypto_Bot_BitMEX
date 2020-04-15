/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.listener.WebsocketDisconnectListener;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import bitmex.Bot.model.bitMEX.listener.IExecutionListener;
import bitmex.Bot.model.bitMEX.listener.IPositionListener;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import bitmex.Bot.model.bitMEX.listener.IOrderListener;
import bitmex.Bot.model.bitMEX.listener.ITradeListener;
import bitmex.Bot.model.bitMEX.listener.IQuoteListener;
//import com.sumzerotrading.data.SumZeroException;
import java.util.concurrent.CountDownLatch;
//import com.sumzerotrading.data.Ticker;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.spec.SecretKeySpec;
import com.google.common.base.Strings;
import java.util.concurrent.TimeUnit;
//import org.apache.log4j.Logger;
//import java.util.logging.Level;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashSet;
import javax.crypto.Mac;
import org.slf4j.Logger;
import java.io.IOError;
import java.util.List;
import java.util.Set;
import java.net.URI;

/**
 *
 * @author RobTerpilowski
 */
public class BitmexWebsocketClient implements IBitmexWebsocketClient, WebsocketDisconnectListener {

    protected static Logger logger = LoggerFactory.getLogger(BitmexWebsocketClient.class);
    protected String testnetApiUrl = "wss://testnet.bitmex.com/realtime";
    protected String productionApiUrl = "wss://www.bitmex.com/realtime";
    protected List<String> subscribeCommandList = new ArrayList<>(); // список команд подписки
    protected Set<String> subscribedQuoteTickers = new HashSet<>(); // Подписанные Тикеры Цитаты
    protected Set<String> subscribedTradeTickers = new HashSet<>(); // торговые тикеры
    protected CountDownLatch latch = new CountDownLatch(1); // обратный отсчет
    protected IMessageProcessor messageProcessor;
    protected boolean shouldReconnect = true;
    protected String websocketUrl = "";
    protected String apiSecret = "";
    protected String apiKey = "";

    WebSocketClient client = new WebSocketClient();
    JettySocket socket;

    protected boolean subscribedExecutions = false;
    protected boolean subscribedPositions = false;
    protected boolean subscribedOrders = false;
    protected boolean isStarted = false;
    protected boolean connected = false;

    //for unit tests
    protected BitmexWebsocketClient() {
    }

    // выбираем реальное или тестовое подключение (к реальному серверу или тестовому)
    // true - реальный
    public BitmexWebsocketClient(boolean useProduction) {
        if (useProduction) {
            websocketUrl = productionApiUrl;
        } else {
            websocketUrl = testnetApiUrl;
        }
        init();
    }
    
    protected void init() {
        messageProcessor = buildMessageProcessor();
        messageProcessor.startProcessor();
        socket = buildJettySocket();
        messageProcessor.addPongListener(socket);        
    }

    @Override
    public boolean connect() {
        return connect("", "");
    }

    @Override
    public boolean connect(String apiKey, String apiSecret) {
        try {
            this.apiKey = apiKey;
            this.apiSecret = apiSecret;
            logger.debug("Starting connection");////////////////////////////////////
            client.start();
            URI echoUri = new URI(websocketUrl);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            

            logger.debug("Connecting to : " + echoUri);////////////////////////////
            latch.await(15, TimeUnit.SECONDS);
            isStarted = socket.isConnected();
            connected = socket.isConnected();
            logger.debug("Connected: " + connected);//////////////////////////////

            if (!Strings.isNullOrEmpty(apiKey)) {
                long nonce = System.currentTimeMillis();
                String signature = getApiSignature(apiSecret, nonce);
                authenticate(apiKey, nonce, signature);
            }

            //socket.startPing();
        } catch (Exception ex) {
//            throw new SumZeroException(ex);
            throw new IOError(ex);
        } finally {
            return connected;
        }
    }

    @Override
    public void socketDisconnectDetected() {
        logger.error("Disconnect detected, should reconnect: " + shouldReconnect );
        if (shouldReconnect) {
            logger.error("Disconnect detected....reconnecting");
            connect(apiKey, apiSecret);
            for (String command : subscribeCommandList) {
                logger.error("Resubmitting subscribe command: " + command);
                socket.subscribe(command);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                }
            }
        } else {
            logger.debug("Disconnect detected, but will not reconnect");//////////////////////////
        }
    }

    @Override
    public void disconnect() {
        try {
            shouldReconnect = false;
            client.stop();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void subscribeExecutions(IExecutionListener listener) {
        messageProcessor.addExecutionListener(listener);
        if( !subscribedExecutions ) {
            subscribedExecutions = true;
            socket.subscribe(buildSubscribeCommand("execution"));
        }
    }

    @Override
    public void subscribeOrders(IOrderListener listener) {
        messageProcessor.addOrderListener(listener);
        if (!subscribedOrders) {
            subscribedOrders = true;
            socket.subscribe(buildSubscribeCommand("order"));
        }
    }

    @Override
    public void subscribePositions(IPositionListener listener) {
        messageProcessor.addPositionListener(listener);
        if (!subscribedPositions) {
            socket.subscribe(buildSubscribeCommand("position"));
            subscribedPositions = true;
        }

    }

    @Override
    public void subscribeInstrument(Ticker ticker) {
        socket.subscribe(buildSubscribeCommand("instrument:" + ticker.getSymbol()));
    }

    @Override
    public void subscribeFunding(Ticker ticker) {
        socket.subscribe(buildSubscribeCommand("funding:" + ticker.getSymbol()));
    }

    @Override
    public void subscribeQuotes(Ticker ticker, IQuoteListener listener) {
        messageProcessor.addQuoteListener(listener);
        if (!subscribedQuoteTickers.contains(ticker.getSymbol())) {
            subscribedQuoteTickers.add(ticker.getSymbol());
            socket.subscribe(buildSubscribeCommand("quote:" + ticker.getSymbol()));
        }

    }

    @Override
    public void subscribeOrderBook(Ticker ticker) {
        socket.subscribe(buildSubscribeCommand("orderBookL2:" + ticker.getSymbol()));
    }

    @Override
    public void subscribeTrades(Ticker ticker, ITradeListener listener) {
        messageProcessor.addTradeListener(listener);
        if (!subscribedTradeTickers.contains(ticker.getSymbol())) {
            subscribedTradeTickers.add(ticker.getSymbol());
            socket.subscribe(buildSubscribeCommand("trade:" + ticker.getSymbol()));
        }
    }

    protected void authenticate(String apiKey, long nonce, String signature) {
        socket.subscribe(buildAuthKeyCommand(apiKey, nonce, signature));
    }

    protected String buildAuthKeyCommand(String apiKey, long nonce, String signature) {
        return buildCommandJson("authKey", apiKey, nonce, signature);
    }

    protected String buildSubscribeCommand(String... args) {
        String command = buildCommandJson("subscribe", args);
        subscribeCommandList.add(command);
        return command;
    }

    protected String buildCommandJson(String command, Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"op\": \"")
                .append(command)
                .append("\", \"args\": [");
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                sb.append("\"");
            }
            sb.append(args[i]);
            if (args[i] instanceof String) {
                sb.append("\"");
            }
            if (i == args.length - 1) {
                sb.append("]");
            } else {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void quoteUpdated(BitmexQuote quoteData) {

    }

    @Override
    public String getApiSignature(String apiKey, long nonce) {

        String keyString = "GET" + "/realtime" + nonce;

        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(apiKey.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            String hash = DatatypeConverter.printHexBinary(sha256_HMAC.doFinal(keyString.getBytes()));
            return hash;
        } catch (Exception e) {
//            throw new SumZeroException(e);
            throw new IOError(e);
        }
    }
    
    public int getMessageProcessorCount() {
        return messageProcessor.getQueueSize();
               
    }

    
    protected IMessageProcessor buildMessageProcessor() {
        return  new WebsocketMessageProcessor();
    }
    
    protected JettySocket buildJettySocket() {
        return new JettySocket(latch, messageProcessor, this);
    }
    
}
