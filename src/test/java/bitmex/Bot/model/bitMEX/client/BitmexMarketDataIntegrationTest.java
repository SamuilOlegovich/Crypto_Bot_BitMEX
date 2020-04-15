/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import bitmex.Bot.model.bitMEX.entity.newClass.GenericTicker;
import bitmex.Bot.model.bitMEX.entity.newClass.StockTicker;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.entity.BitmexAmendOrder;
import bitmex.Bot.model.bitMEX.entity.BitmexInstrument;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.entity.BitmexExecution;
import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import bitmex.Bot.model.bitMEX.entity.BitmexTrade;
//import com.sumzerotrading.data.GenericTicker;
//import com.sumzerotrading.data.StockTicker;
//import com.sumzerotrading.data.Ticker;
import java.time.temporal.ChronoUnit;
import java.time.ZonedDateTime;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import java.time.ZoneId;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.After;
import java.util.List;
import org.junit.Test;

import static bitmex.Bot.model.bitMEX.enums.ChartDataBinSize.ONE_MINUTE;

/**
 *
 * @author RobTerpilowski
 */
// Тест интеграции рыночных данных Bitmex
public class BitmexMarketDataIntegrationTest {

    public BitmexMarketDataIntegrationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    //@Test
    @Ignore
    public void testWebsockClient() throws Exception {
        BitmexWebsocketClient client = new BitmexWebsocketClient(true);
        client.connect();
        // Ticker ticker = new StockTicker("usd_btc_ticker_this_week");
        Ticker ticker = new GenericTicker("XBTUSD");
        client.subscribeTrades(ticker, (BitmexTrade trade) -> {
        });

//Thread.sleep(10000);
        //client.subscribeOrderBook(ticker);
        //client.subscribeFunding(ticker);
//        client.subscribeQuotes(ticker, (OkExQuoteData data) -> {
//            System.out.println("Data is: " + data);
//        });
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000000);
            } catch (InterruptedException ex) {
            }
        });
        thread.start();
        Thread.sleep(300000000);
        System.out.println("Test started");
    }

    @Ignore
    public void testRestClient() throws Exception {
        String apiKeyName = "";
        String apiKey = "";
        BitmexRestClient client = new BitmexRestClient(false, apiKeyName, apiKey);
        Ticker ticker = new StockTicker("XBTUSD");
        BitmexInstrument instrument = client.getInstrument(ticker);
        System.out.println("Instrument: " + instrument);
        System.out.println("Funding rate: " + instrument.getAnnualizedFundingRate());
        System.out.println("Indicative rate: " + instrument.getAnnualizedIndicativeFundingRate());
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        System.out.println("Next funding in: " + now.until(instrument.getFundingTimestamp(), ChronoUnit.MINUTES) + " minutes");
    }

    @Ignore
    //@Test
    public void testSubmitAndCancelLimitOrder() throws Exception {
        String apiKeyName = "";
        String apiKey = "";

        BitmexApiKey keyProp = BitmexApiKey.readApiKey("/Users/RobTerpilowski/Documents/BitmexTestnet.prop");
        apiKeyName = keyProp.getApiKeyName();
        apiKey = keyProp.getApiKey();        

        BitmexRestClient client = new BitmexRestClient(false, apiKeyName, apiKey);
        Ticker ticker = new StockTicker("XBTUSD");
        BitmexOrder order = new BitmexOrder();
        order.setSymbol("XBTUSD");
        order.setOrderQty(1.0);
        order.setPrice(7500.0);

        BitmexOrder result = client.submitOrder(order);
        System.out.println("Order returned is: " + result);
        Thread.sleep(5000);
        BitmexAmendOrder amend = new BitmexAmendOrder();
        amend.setOrderID(result.getOrderID());
        amend.setPrice(7400.0);

        System.out.println("Submitting new order: " + amend);
        BitmexOrder newOrder = client.amendOrder(amend);
        System.out.println("New order: " + newOrder);
        Thread.sleep(2000);
        client.cancelOrder(newOrder);
    }

    @Ignore
    //@Test
    public void testSubmitMarketOrder() throws Exception {
        String apiKeyName = "";
        String apiKey = "";
        BitmexApiKey keyProp = BitmexApiKey.readApiKey("/Users/RobTerpilowski/Documents/BitmexTestnet.prop");
        apiKeyName = keyProp.getApiKeyName();
        apiKey = keyProp.getApiKey();
        BitmexRestClient client = new BitmexRestClient(false, apiKeyName, apiKey);
        BitmexWebsocketClient wsClient = new BitmexWebsocketClient(false);
        wsClient.connect(apiKeyName, apiKey);
        wsClient.subscribeExecutions((BitmexExecution execution) -> {
            System.out.println(execution);
        }
        );
        BitmexOrder order = new BitmexOrder();
        order.setSymbol("XBTU18");
        order.setSide("Buy");
        order.setOrderQty(5000.0);
        order.setOrdType("Limit");
        order.setPrice(7282.0);
//        order.setExecInst("ParticipateDoNotInitiate");
//        order.setPrice(8000.0);

        BitmexOrder result = client.submitOrder(order);
        System.out.println("Order returned is: " + result);
        Thread.sleep(300000000);

    }

    @Test
    public void testGetChartData() {
        String apiKeyName = "";
        String apiKey = "";
        BitmexRestClient client = new BitmexRestClient(false, apiKeyName, apiKey);
        Ticker ticker = new StockTicker("XBTM18");

        List<BitmexChartData> data = client.getChartData(ticker, 100, ONE_MINUTE);
        for (BitmexChartData point : data) {
            System.out.println("Data: " + point);
        }

    }

}
