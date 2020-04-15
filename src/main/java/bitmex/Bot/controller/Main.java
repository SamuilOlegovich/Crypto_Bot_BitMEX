package bitmex.Bot.controller;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.bitMEX.client.BitmexApiKey;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.serverAndParser.Server;
import bitmex.Bot.view.ConsoleHelper;

public class Main {

    private static boolean useProduction = true; // true - реальный счет
    private static String apiKey = "0U0-z8_7rzMol3-W70hPjZRpDtODrsx7ydekmqhAnsDncJqe";
    private static String apiKeyName = "2mGXmniE2uZcqn3Ds-Cz66dK";
    private static String apiKeyName2Accounts = "";
    private static String apiKey2Accounts = "";

    private static BitmexApiKey bitmexApiKey2Accounts;
    private static BitmexClient bitmexClient2Accounts;
    private static BitmexApiKey bitmexApiKey;
    private static BitmexClient bitmexClient;

    private static Ticker ticker;


    public static void main(String[] args) {
        ticker = new Ticker("XBTUSD");
        bitmexApiKey = new BitmexApiKey(apiKeyName, apiKey, useProduction);
        bitmexClient = new BitmexClient(useProduction, apiKeyName, apiKey);
        bitmexClient.subscribeQuotes(ticker, bitmexClient);
        Gasket.setBitmexClient(bitmexClient);
        Gasket.setGameDirection(true);
        Gasket.setTwoAccounts(false);
        Server server = new Server();
        Gasket.setTicker(ticker);
        bitmexClient.setID(1);

        if (Gasket.isTwoAccounts()) {
            if (!apiKey2Accounts.equals("") || !apiKeyName2Accounts.equals("")) {
                bitmexApiKey2Accounts = new BitmexApiKey(apiKeyName2Accounts, apiKey2Accounts, useProduction);
                bitmexClient2Accounts = new BitmexClient(useProduction, apiKeyName2Accounts, apiKey2Accounts);
                bitmexClient2Accounts.subscribeQuotes(ticker, bitmexClient2Accounts);
                Gasket.setBitmexClient2Accounts(bitmexClient2Accounts);
                bitmexClient2Accounts.setID(2);
            } else ConsoleHelper.writeMessage("Данные второго счета отсутствуют или не верны");
        }

        server.start();

        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ConsoleHelper.writeMessage("ПРОГРАММА УСПЕШНО ЗАПУЩЕНА");
        ConsoleHelper.writeMessage("ОЖИДАЙТЕ РЕЗУЛЬТАТОВ");
        ConsoleHelper.writeMessage("");
    }
}
