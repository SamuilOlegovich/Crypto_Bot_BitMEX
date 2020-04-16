package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

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
        timer();
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN Strategy Stop Sell Time начал работать ---- " + getDate());
        ConsoleHelper.writeMessage(ID + " --- MAX ---- " + max + " --- MIN --- " + min);

        while (true) {
            double close = Gasket.getBitmexQuote().getBidPrice();

            if (flag) {
                ConsoleHelper.writeMessage(ID + " --- Сделка Селл ОТМЕНЕНА по таймеру ---- " + getDate());
                break;
            }

            if (close < max) {
                if (Gasket.isTrading()) new TradeSell(ID);
                ConsoleHelper.writeMessage(ID + " --- Сделал сделку Селл по таймеру ---- " + getDate());
                new TestOrderSell(ID, close).start();
                break;
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

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
