package bitmex.Bot.controller;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.bitMEX.client.BitmexApiKey;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.client.BitmexWebsocketClient;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.serverAndParser.Server;
import bitmex.Bot.view.ConsoleHelper;

import java.util.List;

public class Test_2 {
    private static boolean useProduction = true; // true - реальный счет
    private static String apiKeyName = "2mGXmniE2uZcqn3Ds-Cz66dK";
    private static String apiKey = "0U0-z8_7rzMol3-W70hPjZRpDtODrsx7ydekmqhAnsDncJqe";

    public static void main(String[] args) {
        Server server = new Server();
        Ticker ticker = new Ticker("XBTUSD");
        BitmexApiKey bitmexApiKey = new BitmexApiKey(apiKeyName, apiKey, useProduction);
        BitmexClient bitmexClient = new BitmexClient(useProduction, apiKeyName, apiKey);
        BitmexWebsocketClient bitmexWebsocketClient = new BitmexWebsocketClient(false);
        Gasket.setBitmexClient(bitmexClient);
        List<BitmexChartData> list = null;
        Gasket.setTicker(ticker);
        bitmexClient.subscribeQuotes(ticker, bitmexClient);
        try {
            Thread.sleep(1000*7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ConsoleHelper.writeMessage("ПРОГРАММА УСПЕШНО ЗАПУЩЕНА");
        ConsoleHelper.writeMessage("ОЖИДАЙТЕ РЕЗУЛЬТАТОВ");
        ConsoleHelper.writeMessage("");

        for (int i = 0; i < 10; i++) {
            ConsoleHelper.writeMessage(Gasket.price(Gasket.getTicker()).toString());
            try {
                Thread.sleep(1000*2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 1000; i++) {
            ConsoleHelper.writeMessage(Gasket.getBitmexQuote().toString());

        }
    }
}

