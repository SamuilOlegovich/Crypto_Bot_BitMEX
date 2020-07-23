package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.TradeSell;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;


public class StopSellTimeThread extends Thread {

    private boolean flag;
    private double max;
    private double min;
    private String ID;


    public StopSellTimeThread(String ID, double max, double min) {
        this.flag = false;
        this.max = max;
        this.min = min;
        this.ID = ID;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN Strategy Stop Sell Time начал работать ---- "
                + DatesTimes.getDateTerminal() + "\n" + ID + " --- MAX ---- " + max + " --- MIN --- " + min);
        timer();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе RUN класса StopSellTime.");
            e.printStackTrace();
        }

        while (true) {
            double close = Gasket.getBitmexQuote().getBidPrice();

            if (flag) {
                ConsoleHelper.writeMessage(ID + " --- Сделка Селл ОТМЕНЕНА по таймеру ---- "
                        + DatesTimes.getDateTerminal());
                break;
            }

            if (close < max) {
                if (Gasket.isActiveNumberOfCandlesForAnalysis()) {
                    new RangeFlatSellThread(ID, close);
                    return;
                } else {
                    if (Gasket.isTrading()) new TradeSell(ID);
                    ConsoleHelper.writeMessage(ID + " --- Сделал сделку Селл по таймеру ---- "
                            + DatesTimes.getDateTerminal());
                    new TestOrderSell(ID, close);
                    return;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе RUN класса StopSellTime.");
                e.printStackTrace();
            }
        }
    }



    private void timer() {
        try {
            Thread.sleep(1000 * 60 * Gasket.getUseStopLevelOrNotStopTime());
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе таймер класса StopSellTime.");
            e.printStackTrace();
        }
        flag = true;
    }
}
