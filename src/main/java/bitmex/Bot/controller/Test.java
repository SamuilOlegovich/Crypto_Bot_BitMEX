package bitmex.Bot.controller;


import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.bitMEX.client.BitmexApiKey;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.enums.ChartDataBinSize;
import bitmex.Bot.model.serverAndParser.Server;
import bitmex.Bot.view.ConsoleHelper;

import java.util.List;

public class Test {



    private static boolean useProduction = true; // true - реальный счет
    private static String apiKeyName = "80p470zA0Yl7hr3sIXRoW_CR";
    private static String apiKey = "ZzQSrzFmixcVr83JTYLPsrOuz1R1VsYgFkiQ9BtiGV0lr_yv";
//    private static BitmexQuote bitmexQuote;

    public static void main(String[] args) {

//        Test test = new Test();
        Server server = new Server();
        Ticker ticker = new Ticker("XBTUSD");
        BitmexApiKey bitmexApiKey = new BitmexApiKey(apiKeyName, apiKey, useProduction);
        BitmexClient bitmexClient = new BitmexClient(useProduction, apiKeyName, apiKey);
//        BitmexWebsocketClient bitmexWebsocketClient = new BitmexWebsocketClient(false);
        Gasket.setBitmexClient(bitmexClient);
//        List<BitmexChartData> list = null;
        Gasket.setTicker(ticker);
//        server.start();

        bitmexClient.subscribeQuotes(ticker, null);

        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ConsoleHelper.writeMessage("Поток котировок подключен.");

        BitmexOrder bitmexOrderBuyOpen = new BitmexOrder();
        double price = Gasket.getBitmexQuote().getAskPrice();
        bitmexOrderBuyOpen.setTimeInForce("GoodTillCancel");
        bitmexOrderBuyOpen.setSymbol(ticker.getSymbol());
        bitmexOrderBuyOpen.setOrdType("Limit");
        bitmexOrderBuyOpen.setDisplayQty(0.0);
        bitmexOrderBuyOpen.setOrderQty(1.0);
        bitmexOrderBuyOpen.setPrice(price);
        bitmexOrderBuyOpen.setSide("Buy");

        BitmexOrder bitmexOrderBuyAnswer = bitmexClient.submitOrder(bitmexOrderBuyOpen);
        System.out.println(bitmexOrderBuyAnswer);

        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BitmexOrder bitmexOrderLimitIfTouchedTakeOpen = bitmexOrderBuyOpen;
        bitmexOrderLimitIfTouchedTakeOpen.setText("Take profit - " + bitmexOrderBuyAnswer.getOrderID());
        bitmexOrderLimitIfTouchedTakeOpen.setOrdType("LimitIfTouched");
        bitmexOrderLimitIfTouchedTakeOpen.setPrice(price + 10.0);
        bitmexOrderLimitIfTouchedTakeOpen.setStopPx(price + 8.0);
        bitmexOrderLimitIfTouchedTakeOpen.setSide("Sell");
        bitmexOrderLimitIfTouchedTakeOpen.setOrderID("");
        System.out.println(bitmexOrderLimitIfTouchedTakeOpen);

        BitmexOrder bitmexOrderLimitIfTouchedAnswer = bitmexClient.submitOrder(bitmexOrderLimitIfTouchedTakeOpen);
        System.out.println(bitmexOrderLimitIfTouchedAnswer);

        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BitmexOrder bitmexOrderStopLimitOpen = bitmexOrderBuyOpen;
        bitmexOrderStopLimitOpen.setText("Stop loss - " + bitmexOrderBuyAnswer.getOrderID());
        bitmexOrderStopLimitOpen.setOrdType("StopLimit");
        bitmexOrderStopLimitOpen.setPrice(price - 10.0);
        bitmexOrderStopLimitOpen.setStopPx(price - 8.0);
        bitmexOrderStopLimitOpen.setSide("Sell");
        bitmexOrderStopLimitOpen.setOrderID("");
        System.out.println(bitmexOrderStopLimitOpen);

        BitmexOrder bitmexOrderStopLimitAnswer = bitmexClient.submitOrder(bitmexOrderStopLimitOpen);
        System.out.println(bitmexOrderStopLimitAnswer);


        System.out.println("ВСЕ ГУДЕН ТАГ");


        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BitmexOrder[] bitmexOrders = bitmexClient.cancelOrder(bitmexOrderStopLimitAnswer);
        for (BitmexOrder bitmexOrder : bitmexOrders) {
            ConsoleHelper.writeMessage(bitmexOrder.toString());
        }

        System.out.println("ВСЕ ГУДЕН ТАГ");

        List<BitmexChartData> list = Gasket.getBitmexClient().getChartData(Gasket.getTicker(),
                Gasket.getNumberOfCandlesForAnalysis(), ChartDataBinSize.ONE_MINUTE);
        double maxAverage = 0.0;
        double max = 0.0;

        for (BitmexChartData biData : list) {
            max = Math.max(max, biData.getHigh());
            maxAverage = maxAverage + biData.getHigh();
            System.out.println(max + " ---- " + maxAverage);
        }
    }
}
