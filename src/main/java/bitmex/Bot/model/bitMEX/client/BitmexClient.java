/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.bitMEX.enums.ChartDataBinSize;
import bitmex.Bot.model.bitMEX.listener.IExecutionListener;
import bitmex.Bot.model.bitMEX.listener.IPositionListener;
import bitmex.Bot.model.bitMEX.listener.IOrderListener;
import bitmex.Bot.model.bitMEX.listener.IQuoteListener;
import bitmex.Bot.model.bitMEX.listener.ITradeListener;
import bitmex.Bot.model.bitMEX.entity.BitmexAmendOrder;
import bitmex.Bot.model.bitMEX.entity.BitmexInstrument;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
//import com.sumzerotrading.data.Ticker;
import java.util.List;

/**
 *
 * @author RobTerpilowski
 */
public class BitmexClient implements IBitmexClient {

    protected IBitmexWebsocketClient websocketClient;
    protected IBitmexRestClient restClient;
    private BitmexQuote bitmexQuote;
    private int ID;
    
    public BitmexClient( boolean useProduction ) {
        restClient = new BitmexRestClient(useProduction);
        websocketClient = new BitmexWebsocketClient(useProduction);
        websocketClient.connect();
    }
    
    public BitmexClient( boolean useProduction, String apiKeyName, String apiKey ) {
        restClient = new BitmexRestClient(useProduction, apiKeyName, apiKey);
        websocketClient = new BitmexWebsocketClient(useProduction);
        websocketClient.connect(apiKeyName, apiKey);
    }


    @Override   // изменить ордер
    public BitmexOrder amendOrder(BitmexAmendOrder order) {
        return restClient.amendOrder(order);
    }

    @Override   // отменить ордер
    public BitmexOrder[] cancelOrder(BitmexOrder order) {
        return restClient.cancelOrder(order);
    }

    @Override   // получить данные о свечках
    public List<BitmexChartData> getChartData(Ticker ticker, int count, ChartDataBinSize binSize) {
        return restClient.getChartData(ticker, count, binSize);
    }

    @Override   // получить данные о свечках
    public List<BitmexChartData> getChartData(Ticker ticker, int count, ChartDataBinSize binSize, String endTime) {
        return restClient.getChartData(ticker, count, binSize, endTime);
    }

    @Override   // получить данные о свечках
    public List<BitmexChartData> getChartData(Ticker ticker, int count,
                                              ChartDataBinSize binSize, String endTime, boolean getInprogressBar) {
        return restClient.getChartData(ticker, count, binSize, endTime, getInprogressBar);
    }

    @Override   // Получить данные инструмента ставка финансирования, скорость фондирования, временную зону
    public BitmexInstrument getInstrument(Ticker ticker) {
        return restClient.getInstrument(ticker);
    }

    @Override   // Подтвердить ордер
    public BitmexOrder submitOrder(BitmexOrder order) {
        return restClient.submitOrder(order);
    }


    @Override
    public boolean connect() {
        return websocketClient.connect();
    }

    @Override
    public boolean connect(String apiKey, String apiSecret) {
        return websocketClient.connect(apiKey, apiSecret);
    }

    @Override
    public void disconnect() {
        websocketClient.disconnect();
    }

    @Override
    public String getApiSignature(String apiKey, long nonce) {
        return websocketClient.getApiSignature(apiKey, nonce);
    }

    @Override
    public boolean isConnected() {
        return websocketClient.isConnected();
    }

    @Override
    public void quoteUpdated(BitmexQuote quoteData) {
        if (ID == 1) Gasket.setBitmexQuote(quoteData);
        if (ID == 2) Gasket.setBitmex2Quote(quoteData);
    }

    @Override
    public void subscribeExecutions(IExecutionListener listener) {
        websocketClient.subscribeExecutions(listener);
    }

    @Override
    public void subscribeFunding(Ticker ticker) {
        websocketClient.subscribeFunding(ticker);
    }

    @Override
    public void subscribeInstrument(Ticker ticker) {
        websocketClient.subscribeInstrument(ticker);
    }

    @Override   // подписаться Заказать книгу
    public void subscribeOrderBook(Ticker ticker) {
        websocketClient.subscribeOrderBook(ticker);
    }

    @Override
    public void subscribeOrders(IOrderListener listener) {
        websocketClient.subscribeOrders(listener);
    }

    @Override
    public void subscribePositions(IPositionListener listener) {
        websocketClient.subscribePositions(listener);
    }

    @Override   // подписаться Котировки
    public void subscribeQuotes(Ticker ticker, IQuoteListener listener) {
        websocketClient.subscribeQuotes(ticker, listener);
    }

    @Override // подписаться на торги
    public void subscribeTrades(Ticker ticker, ITradeListener listener) {
        websocketClient.subscribeTrades(ticker, listener);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
