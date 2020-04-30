package bitmex.Bot.controller;


import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.bitMEX.client.BitmexApiKey;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.enums.ChartDataBinSize;
import bitmex.Bot.model.serverAndParser.Server;
import bitmex.Bot.view.ConsoleHelper;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static bitmex.Bot.model.enums.TimeFrame.*;

public class Test_2 {

    private static BitmexApiKey bitmexApiKey2Accounts;
    private static BitmexClient bitmexClient2Accounts;
    private static BitmexApiKey bitmexApiKey;
    private static BitmexClient bitmexClient;
    private static Ticker ticker;

    public static void main(String[] args) {
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
            } else {
                ConsoleHelper.writeMessage("Данные второго счета отсутствуют или не верны");
            }
        }

        ConsoleHelper.printInfoSettings();
        controlConsoleSetting.start();

        try {
            Thread.sleep(1000 * 10);
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






//        int value = 1000;
//
//
//        List<Integer> list =  new ArrayList<>();
//        // добовляем в массив все значения нужного таймфрейма за нужное время
//        // можно их конечно и не добавлять, а считать по мере прихождения, как пример привел так, так как не знаю как
//        // приходят данные
//        list.add(value);
//        // сортируем если массив сам по себе не сортирует
//
//        // далее узнаем длину массива и делим ее на два
//        // потом узнаем средние числа этих двух половинок
//        int size = list.size();
//
//        int averageFinish = 0;
//        int averageStop = 0;
//        int count = 0;
//
//        for (int i = 0; i < size/2; i++) {
//            averageFinish = averageFinish + list.get(i);
//            count++;
//        }
//        averageFinish = averageFinish / count;
//        count = 0;
//
//        for (int i = size/2; i < size; i++) {
//            averageStop = averageStop + list.get(i);
//            count++;
//        }
//        averageStop = averageStop / count;
//        count = 0;
//
//        //  в итоге получаем актуальное среднее
//        int actualValues = 0;
//
//        for (Integer integer : list) {
//            if (integer > averageFinish && integer < averageStop) {
//                actualValues = actualValues + integer;
//                count++;
//            }
//        }
//        actualValues = actualValues / count; // актуальное значение готово
//        count = 0;
//        list.clear();
//
//        // можно еще варианты придумать, например
//        // там где можно определить пучности значений и узнать актуальное среднее значение пучносте




