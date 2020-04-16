package bitmex.Bot.model.strategies.oneStrategies;


import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;


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
        timer();
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN Strategy Stop Buy Time начал работать ---- " + getDate());
        ConsoleHelper.writeMessage(ID + " --- MAX ---- " + max + " --- MIN --- " + min);

        while (true) {
            double close = Gasket.getBitmexQuote().getAskPrice();

            if (flag) {
                ConsoleHelper.writeMessage(ID + " --- Сделка Бай ОТМЕНЕНА по таймеру ---- " + getDate());
                return;
            }

            if (close > max) {
                if (Gasket.isTrading()) new TradeBuy(ID);
                ConsoleHelper.writeMessage(ID + " --- Сделал сделку Бай по таймеру ---- " + getDate());
                new TestOrderBuy(ID, close).start();
                return;
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


    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
