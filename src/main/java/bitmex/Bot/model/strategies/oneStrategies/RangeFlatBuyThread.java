package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.enums.ChartDataBinSize;
import bitmex.Bot.model.strategies.DatesTimes;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;

import java.util.List;

public class RangeFlatBuyThread extends Thread {

    private double close;
    private String ID;

    public RangeFlatBuyThread(String ID, double close) {
        this.close = close;
        this.ID = ID;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса Range Flat Buy Thread начал работать ---- "
                + DatesTimes.getDate());
        // тут мы получаем максимальные границы канала и смотрим где по отношению к ним находится цена сейчас
        // можно конечно сделать среднюю границу канала (но это потом пока проверим так)
        List<BitmexChartData> list = Gasket.getBitmexClient().getChartData(Gasket.getTicker(),
                Gasket.getNumberOfCandlesForAnalysis(), ChartDataBinSize.ONE_MINUTE);
        double take = Gasket.getTake();
        double maxAverage = 0.0;
        double max = 0.0;

        for (BitmexChartData biData : list) {
            max = Math.max(max, biData.getHigh());
            maxAverage = maxAverage + biData.getHigh();
        }

        maxAverage = maxAverage / list.size();
        list.clear();

        if (Gasket.isMaxAndMinAverage()) {
            if (close > maxAverage || (close < maxAverage && close < (maxAverage - (take * 2)))) {
                if (Gasket.isTrading()) new TradeBuy(ID);
                ConsoleHelper.writeMessage(ID + " --- Сделал сделку Бай ---- " + DatesTimes.getDate());
                new TestOrderBuy(ID, close);
            } else {
                ConsoleHelper.writeMessage(ID + " --- Сделка Бай ОТМЕНЕНА по диапазону ФЛЭТа ---- "
                        + DatesTimes.getDate());
            }
        } else {
            if (close > max || (close < max && close < (max - (take * 2)))) {
                if (Gasket.isTrading()) new TradeBuy(ID);
                ConsoleHelper.writeMessage(ID + " --- Сделал сделку Бай ---- " + DatesTimes.getDate());
                new TestOrderBuy(ID, close);
            } else {
                ConsoleHelper.writeMessage(ID + " --- Сделка Бай ОТМЕНЕНА по диапазону ФЛЭТа ---- "
                        + DatesTimes.getDate());
            }
        }
    }
}
