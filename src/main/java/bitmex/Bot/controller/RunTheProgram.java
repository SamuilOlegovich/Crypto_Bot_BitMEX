package bitmex.Bot.controller;

import bitmex.Bot.model.strategies.IIUser.Martingale;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.client.BitmexApiKey;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.serverAndParser.Server;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.strategies.IIUser.OpenTransactions;
import bitmex.Bot.model.strategies.IIUser.ReadAndSavePatternsUser;
import bitmex.Bot.view.ConsoleHelper;


public class RunTheProgram extends Thread {
    private static BitmexApiKey bitmexApiKey2Accounts;
    private static BitmexClient bitmexClient2Accounts;
    private static BitmexApiKey bitmexApiKey;
    private static BitmexClient bitmexClient;
    private static Ticker ticker;

    public RunTheProgram() {
        start();
    }

    @Override
    public void run() {

        ticker = new Ticker("XBTUSD");

        bitmexApiKey = new BitmexApiKey(ApiKey.getApiKeyName(), ApiKey.getApiKey(), Gasket.isUseRealOrNotReal());
        bitmexClient = new BitmexClient(Gasket.isUseRealOrNotReal(), ApiKey.getApiKeyName(), ApiKey.getApiKey());
        bitmexClient.subscribeQuotes(ticker, bitmexClient);
        Gasket.setBitmexClient(bitmexClient);
        Gasket.setGameDirection(true);
        Gasket.setTwoAccounts(false);
        Server server = new Server();
        Gasket.setTicker(ticker);
        bitmexClient.setID(1);

        if (Gasket.isTwoAccounts()) {
            if (!ApiKey.getApiKey2Accounts().equals("") || !ApiKey.getApiKeyName2Accounts().equals("")) {
                bitmexApiKey2Accounts = new BitmexApiKey(
                        ApiKey.getApiKeyName2Accounts(), ApiKey.getApiKey2Accounts(), Gasket.isUseRealOrNotReal());
                bitmexClient2Accounts = new BitmexClient(
                        Gasket.isUseRealOrNotReal(), ApiKey.getApiKeyName2Accounts(), ApiKey.getApiKey2Accounts());
                bitmexClient2Accounts.subscribeQuotes(ticker, bitmexClient2Accounts);
                Gasket.setBitmexClient2Accounts(bitmexClient2Accounts);
                bitmexClient2Accounts.setID(2);
            } else { ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + "Данные второго счета отсутствуют или не верны"); }
        }

        ConsoleHelper.printInfoSettings();
        server.start();

        // подготавливаем строки предсказания
        Gasket.getSavedPatternsUserClass().replacePredictions();
        Gasket.setOpenTransactions(new OpenTransactions());
        Gasket.setMartingaleClass(new Martingale());
        ConsoleHelper.writeMessage(Gasket.getMartingaleClass().showVolumeForEachStep());


        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ConsoleHelper.writeMessage("\n\n"
                + DatesTimes.getDateTerminal()
                + " --- ПРОГРАММА УСПЕШНО ЗАПУЩЕНА - ОЖИДАЙТЕ РЕЗУЛЬТАТОВ"
                + "\n\n");
    }
}
