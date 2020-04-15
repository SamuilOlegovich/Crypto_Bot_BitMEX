package bitmex.Bot.model.serverAndParser.strategies.oneStrategies;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

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
        // получаем цену на данный момент и сравниваем с аском и объемом если цена поднялась выше этих уровней -
        // то все отменяем, если же пошла вниз то заходим в сел.
        double max = volume.getPrice() - Gasket.getRangePriceMAX();
        double min = volume.getPrice() < ask.getPrice()
                ? ask.getPrice() + Gasket.getRangePriceMIN() : volume.getPrice() + Gasket.getRangePriceMIN();

        ConsoleHelper.writeMessage(ID + " --- RUN класса StrategyOneSellThread начал работать ---- " + getDate());
        ConsoleHelper.writeMessage(ID + " --- MAX ---- " + max + " --- MIN --- " + min);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("d-MM-yyyy hh:mm:ss");
//        Calendar calendarS = new GregorianCalendar();
//        calendarS.roll(Calendar.HOUR, Gasket.getDateDifference());
//        writeMessage(ID + " --- RUN класса StrategyOneSellThread начал работать ---- "
//                + dateFormat.format(calendarS.getTime()));

        while (true) {

//            double close = Gasket.price(Gasket.getTicker()).getClose();
            double close = Gasket.getBitmexQuote().getBidPrice();

//            writeMessage(ID + " --- CLOSE --- " + close + " --- MAX --- " + max + " --- MIN --- " + min);


//            if ((close >  ask.getPrice() && close > volume.getPrice())) {
            if (close > min) {
                flag();
                ConsoleHelper.writeMessage(ID + " --- Сделка Селл ОТМЕНЕНА ---- " + getDate());
//                Calendar calendar = new GregorianCalendar();
//                calendarS.roll(Calendar.HOUR, Gasket.getDateDifference());
//                writeMessage(ID + " --- Сделка Селл ОТМЕНЕНА ---- " + dateFormat.format(calendar.getTime()));
                break;
            }

//            if (close < askD && close < volumeD) {
            if (close < max) {
                if (Gasket.isTrading()) new TradeSell(ID);
                ConsoleHelper.writeMessage(ID + " --- Сделал сделку Селл ---- " + getDate());
//                Calendar calendar = new GregorianCalendar();
//                calendarS.roll(Calendar.HOUR, Gasket.getDateDifference());
//                writeMessage(ID + " --- Сделал сделку Селл ---- " + dateFormat.format(calendar.getTime()));
                new TestOrderSell(ID, close).start();
                break;
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
