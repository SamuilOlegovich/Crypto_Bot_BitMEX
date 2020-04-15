/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.listener.IExecutionListener;
import bitmex.Bot.model.bitMEX.listener.IPositionListener;
import bitmex.Bot.model.bitMEX.listener.IOrderListener;
import bitmex.Bot.model.bitMEX.listener.ITradeListener;
import bitmex.Bot.model.bitMEX.listener.IQuoteListener;
//import com.sumzerotrading.data.Ticker;

/**
 *
 * @author RobTerpilowski
 */
public interface IBitmexWebsocketClient extends IQuoteListener {

    void subscribeQuotes(Ticker ticker, IQuoteListener listener);
    void subscribeTrades(Ticker ticker, ITradeListener listener);
    void subscribeExecutions(IExecutionListener listener);
    void subscribePositions(IPositionListener listener);
    String getApiSignature(String apiKey, long nonce);
    boolean connect(String apiKey, String apiSecret);
    void subscribeOrders(IOrderListener listener);
    void quoteUpdated(BitmexQuote quoteData);
    void subscribeInstrument(Ticker ticker);
    void subscribeOrderBook(Ticker ticker);
    void subscribeFunding(Ticker ticker);
    boolean isConnected();
    boolean connect();
    void disconnect();
}
