package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class StrategyOneBuyThread extends Thread {
    private InfoIndicator volume;
    private InfoIndicator bid;
    private String ID;

    public StrategyOneBuyThread(String id, InfoIndicator volume, InfoIndicator bid) {
        this.volume = volume;
        this.bid = bid;
        this.ID = id;
        start();
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateN = new Date();
        return dateFormat.format(dateN);
    }

    private void flag() {
        if (Gasket.getStrategeWorkOne() == 1) Gasket.setStrategyOneAllFLAG(true);
        else if (Gasket.getStrategeWorkOne() == 2) {
            if (!Gasket.isStrategyOneBuyRangeFLAG()) Gasket.setStrategyOneBuyRangeFLAG(true);
            if (!Gasket.isStrategyOneBuyFLAG()) Gasket.setStrategyOneBuyFLAG(true);
            if (!Gasket.isOneBuyFLAG()) Gasket.setOneBuyFLAG(true);
        }
    }


    @Override
    public void run() {
        // получаем цену на данный момент и сравниваем с бид и объемом если цена поднялась выше этих уровней -
        // то все отменяем, если же пошла вниз то заходим в сел.
        double max = volume.getPrice() + Gasket.getRangePriceMAX();
        double min = volume.getPrice() > bid.getPrice()
                ? bid.getPrice() - Gasket.getRangePriceMIN() : volume.getPrice() - Gasket.getRangePriceMIN();

        ConsoleHelper.writeMessage(ID + " --- RUN StrategyOneBuyThread начал работать ---- " + getDate());
        ConsoleHelper.writeMessage(ID + " --- MAX ---- " + max + " --- MIN --- " + min);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("d-MM-yyyy hh:mm:ss");
//        Calendar calendarS = new GregorianCalendar();
//        calendarS.roll(Calendar.HOUR, Gasket.getDateDifference());
//        writeMessage(ID + " --- RUN StrategyOneBuyThread начал работать ---- "
//                + dateFormat.format(calendarS.getTime()));

        while (true) {
//            double close = Gasket.price(Gasket.getTicker()).getClose();
//            writeMessage(ID + " --- CLOSE --- " + close + " --- MAX --- " + max + " --- MIN --- " + min);
            double close = Gasket.getBitmexQuote().getAskPrice();

//            if ((close <  volume.getPrice() && close < bid.getPrice())) {
            if (close < min) {
               flag();

                ConsoleHelper.writeMessage(ID + " --- Сделку Бай ОТМЕНЕНА ---- " + getDate());
//                Calendar calendar = new GregorianCalendar();
//                calendarS.roll(Calendar.HOUR, Gasket.getDateDifference());
//                writeMessage(ID + " --- Сделку Бай ОТМЕНЕНА ---- " + dateFormat.format(calendar.getTime()));
                return;
            }

//            if (close > bid.getPrice() && close > volumeD) {
            if (close > max) {
                if (Gasket.isTrading()) new TradeBuy(ID);
                ConsoleHelper.writeMessage(ID + " --- Сделал сделку Бай ---- " + getDate());
//                Calendar calendar = new GregorianCalendar();
//                calendarS.roll(Calendar.HOUR, Gasket.getDateDifference());
//                writeMessage(ID + " --- Сделал сделку Бай ---- " + dateFormat.format(calendar.getTime()));
                new TestOrderBuy(ID, close).start();
                return;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе RUN класса StrategyOneSell.");
                e.printStackTrace();
            }
        }
    }
}

