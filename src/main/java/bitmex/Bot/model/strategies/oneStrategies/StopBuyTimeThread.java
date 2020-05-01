package bitmex.Bot.model.strategies.oneStrategies;


import bitmex.Bot.model.strategies.DatesTimes;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;


// если цена опустилась за нижний предел минимум - то даем ей еще один шанс
public class StopBuyTimeThread extends Thread {

    private boolean flag;
    private double max;
    private double min;
    private String ID;


    public StopBuyTimeThread(String ID, double max, double min) {
        this.flag = false;
        this.max = max;
        this.min = min;
        this.ID = ID;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN Strategy Stop Buy Time начал работать ---- "
                + DatesTimes.getDate() + "\n" + ID + " --- MAX ---- " + max + " --- MIN --- " + min);
        timer();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе RUN класса StopSellTime.");
            e.printStackTrace();
        }

        while (true) {
            double close = Gasket.getBitmexQuote().getAskPrice();

            if (flag) {
                ConsoleHelper.writeMessage(ID + " --- Сделка Бай ОТМЕНЕНА по таймеру ---- "
                        + DatesTimes.getDate());
                return;
            }

            if (close > max) {
                if (Gasket.isActiveNumberOfCandlesForAnalysis()) {
                    new RangeFlatBuyThread(ID, close);
                    return;
                } else {
                    if (Gasket.isTrading()) new TradeBuy(ID);
                    ConsoleHelper.writeMessage(ID + " --- Сделал сделку Бай по таймеру ---- "
                            + DatesTimes.getDate());
                    new TestOrderBuy(ID, close);
                    return;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе RUN класса StopBuyTime.");
                e.printStackTrace();
            }
        }
    }



    private void timer() {
        try {
            Thread.sleep(1000 * 60 * Gasket.getUseStopLevelOrNotStopTime());
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе таймер класса StopBuyTime.");
            e.printStackTrace();
        }
        flag = true;
    }
}
