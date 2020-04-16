package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class StrategyOneSellThread extends Thread {
    private InfoIndicator volume;
    private InfoIndicator ask;
    private String ID;

    public StrategyOneSellThread(String id, InfoIndicator volume, InfoIndicator ask) {
        this.ID = id;
        this.volume = volume;
        this.ask = ask;
        start();
    }


    @Override
    public void run() {
        // получаем цену на данный момент и сравниваем с аском и объемом если цена поднялась выше этих уровней -
        // то все отменяем, если же пошла вниз то заходим в сел.
        double max = volume.getPrice() - Gasket.getRangePriceMAX();
        double min = volume.getPrice() < ask.getPrice()
                ? ask.getPrice() + Gasket.getRangePriceMIN() : volume.getPrice() + Gasket.getRangePriceMIN();

        ConsoleHelper.writeMessage(ID + " --- RUN класса Strategy One Sell Thread начал работать ---- " + getDate());
        ConsoleHelper.writeMessage(ID + " --- MAX ---- " + max + " --- MIN --- " + min);

        while (true) {

            double close = Gasket.getBitmexQuote().getBidPrice();



            if (close > min) {
                flag();
                ConsoleHelper.writeMessage(ID + " --- Сделка Селл ОТМЕНЕНА ---- " + getDate());
                break;
            }

            if (close < max) {
                if (Gasket.isTrading()) new TradeSell(ID);
                ConsoleHelper.writeMessage(ID + " --- Сделал сделку Селл ---- " + getDate());
                new TestOrderSell(ID, close).start();
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе RUN класса Strategy One Sell.");
                e.printStackTrace();
            }
        }
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void flag() {
        if (Gasket.getStrategyWorkOne() == 1) Gasket.setStrategyOneAllFLAG(true);
        else if (Gasket.getStrategyWorkOne() == 2) {
            if (!Gasket.isStrategyOneBuyRangeFLAG()) Gasket.setStrategyOneBuyRangeFLAG(true);
            if (!Gasket.isStrategyOneBuyFLAG()) Gasket.setStrategyOneBuyFLAG(true);
            if (!Gasket.isOneBuyFLAG()) Gasket.setOneBuyFLAG(true);
        }
    }
}
