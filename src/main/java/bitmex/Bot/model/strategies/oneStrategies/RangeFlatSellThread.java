package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.enums.ChartDataBinSize;
import bitmex.Bot.model.strategies.DatesTimes;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;


import java.util.List;

public class RangeFlatSellThread extends Thread {

    private double close;
    private String ID;

    public RangeFlatSellThread(String ID, double close) {
        this.close = close;
        this.ID = ID;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса Range Flat Sell Thread начал работать ---- "
                + DatesTimes.getDate());
        List<BitmexChartData> list = Gasket.getBitmexClient().getChartData(Gasket.getTicker(),
                Gasket.getNumberOfCandlesForAnalysis(), ChartDataBinSize.ONE_MINUTE);
        double take = Gasket.getTake();
        double minAverage = 0.0;
        double min = 0.0;

        for (BitmexChartData biData : list) {
            min = Math.min(min, biData.getLow());
            minAverage = minAverage + biData.getLow();
        }

        minAverage = minAverage / list.size();
        list.clear();

        if (Gasket.isMaxAndMinAverage()) {
            if (close < minAverage || (close > minAverage && close > (minAverage + (take * 2)))) {
                if (Gasket.isTrading()) new TradeSell(ID);
                ConsoleHelper.writeMessage(ID + " --- Сделал сделку Селл ---- "
                        + DatesTimes.getDate());
                new TestOrderSell(ID, close);
            } else {
                ConsoleHelper.writeMessage(ID + " --- Сделка Селл ОТМЕНЕНА по диапазону ФЛЭТа ---- "
                        + DatesTimes.getDate());
            }
        } else {
            if (close < min || (close > min && close > (min + (take * 2)))) {
                if (Gasket.isTrading()) new TradeSell(ID);
                ConsoleHelper.writeMessage(ID + " --- Сделал сделку Селл ---- "
                        + DatesTimes.getDate());
                new TestOrderSell(ID, close);
            } else {
                ConsoleHelper.writeMessage(ID + " --- Сделка Селл ОТМЕНЕНА по диапазону ФЛЭТа ---- "
                        + DatesTimes.getDate());
            }
        }
    }

}
