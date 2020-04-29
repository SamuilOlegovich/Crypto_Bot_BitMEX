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
        List<BitmexChartData> list = Gasket.getBitmexClient().getChartData(Gasket.getTicker(),
                Gasket.getNumberOfCandlesForAnalysis(), ChartDataBinSize.ONE_MINUTE);
        double take = Gasket.getTake();
        double min = 0.0;

        for (BitmexChartData biData : list) {
            min = Math.min(min, biData.getLow());
        }

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
