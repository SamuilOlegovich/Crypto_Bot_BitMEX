package bitmex.Bot.controller;

import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.enums.ChartDataBinSize;
import bitmex.Bot.model.bitMEX.client.BitmexApiKey;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.serverAndParser.Server;
import bitmex.Bot.model.FilesAndPathCreator;
import bitmex.Bot.model.strategies.II.ReadAndSavePatterns;
import bitmex.Bot.model.strategies.II.SavedPatterns;
import bitmex.Bot.model.strategies.IIUser.ReadAndSavePatternsUser;
import bitmex.Bot.model.strategies.IIUser.SavedPatternsUser;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;


import java.util.List;



public class Main {

    private static BitmexApiKey bitmexApiKey2Accounts;
    private static BitmexClient bitmexClient2Accounts;
    private static BitmexApiKey bitmexApiKey;
    private static BitmexClient bitmexClient;
    private static Ticker ticker;


    public static void main(String[] args) {
        FilesAndPathCreator filesAndPathCreator = new FilesAndPathCreator();
        SavedPatternsUser savedPatternsUser = SavedPatternsUser.getInstance();
        SavedPatterns savedPatterns = SavedPatterns.getInstance();
        Gasket.setSavedPatternsUserClass(savedPatternsUser);
        ReadAndSavePatternsUser.createSavedPatternsUser();
        Gasket.setSavedPatternsClass(savedPatterns);
        ReadAndSavePatterns.createSavedPatterns();
        //  тут можно подождать пару секунд
        //  тут можно подождать пару секунд
        ticker = new Ticker("XBTUSD");
        ExecutorCommandos executorCommandos = new ExecutorCommandos();
        ParserSetting parserSetting = new ParserSetting(executorCommandos);
        bitmexApiKey = new BitmexApiKey(ApiKey.getApiKeyName(), ApiKey.getApiKey(), Gasket.isUseRealOrNotReal());
        bitmexClient = new BitmexClient(Gasket.isUseRealOrNotReal(), ApiKey.getApiKeyName(), ApiKey.getApiKey());
        ControlConsoleSetting controlConsoleSetting = new ControlConsoleSetting(executorCommandos);
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
            } else { ConsoleHelper.writeMessage("Данные второго счета отсутствуют или не верны"); }
        }

        ConsoleHelper.printInfoSettings();
        controlConsoleSetting.start();
        server.start();

        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ConsoleHelper.writeMessage("");
        ConsoleHelper.writeMessage("ПРОГРАММА УСПЕШНО ЗАПУЩЕНА");
        ConsoleHelper.writeMessage("ОЖИДАЙТЕ РЕЗУЛЬТАТОВ");
        ConsoleHelper.writeMessage("");

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
