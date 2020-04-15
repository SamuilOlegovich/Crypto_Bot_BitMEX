package bitmex.Bot.model.serverAndParser.strategies.oneStrategies;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.view.ConsoleHelper;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class TestOrderBuy extends Thread {
    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderBuy(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder + Gasket.getTake();
        this.priseStopOrder = priseOpenOrder - Gasket.getStop();
        this.priseOpenOrder = priseOpenOrder;
        this.ID =  id;
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

    private void setStop() {
        if (ID.endsWith("SOBSCT")) Gasket.setSobTStop(Gasket.getSobTStop() + 1);
        if (ID.endsWith("SOBR")) Gasket.setSobRStop(Gasket.getSobRStop() + 1);
        if (ID.endsWith("SOB")) Gasket.setSobStop(Gasket.getSobStop() + 1);
        if (ID.endsWith("OB")) Gasket.setObStop(Gasket.getObStop() + 1);
    }

    private void setTake() {
        if (ID.endsWith("SOBSCT")) Gasket.setSobTTake(Gasket.getSobTTake() + 1);
        if (ID.endsWith("SOBR")) Gasket.setSobRTake(Gasket.getSobRTake() + 1);
        if (ID.endsWith("SOB")) Gasket.setSobTake(Gasket.getSobTake() + 1);
        if (ID.endsWith("OB")) Gasket.setObTake(Gasket.getObTake() + 1);
    }

    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса TestOrderBuy начал считать --- " + getDate());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("d-MM-yyyy hh:mm:ss");
//        Calendar calendarS = new GregorianCalendar();
//        calendarS.roll(Calendar.HOUR, Gasket.getDateDifference());
//        writeMessage(ID + " --- RUN класса TestOrderBuy начал считать --- "
//                + dateFormat.format(calendarS.getTime()));

        while (true) {
//            BitmexChartData bitmexChartData = Gasket.price(Gasket.getTicker());
//            double high = bitmexChartData.getHigh();
//            double low = bitmexChartData.getLow();
//            writeMessage(ID + " --- OPEN --- " + priseOpenOrder
//                    + " --- HIGH --- " + high + " --- LOW --- " + low
//                    + " --- STOP --- " + priseStopOrder + " --- TAKE --- " + priseTakeOrder);
            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

//            if (low <= priseStopOrder) {
            if (priceBid <= priseStopOrder) {
                flag();
                setStop();

                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС ---- " + getDate());
//                Calendar calendar = new GregorianCalendar();
//                calendarS.roll(Calendar.HOUR, Gasket.getDateDifference());
//                writeMessage(ID + " --- Сработал СТОП ЛОСС ---- " + dateFormat.format(calendar.getTime()));
                Gasket.setPROFIT_Buy(Gasket.getPROFIT_Buy() - Gasket.getStop());
                ConsoleHelper.writeMessage(ID + " --- ИТОГО на счету БАЙ --- " + Gasket.getPROFIT_Buy());
                break;
            }

//            if (high >= priseTakeOrder) {
            if (priceAsk >= priseTakeOrder) {
                flag();
                setTake();

                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ ---- " + getDate());
//                Calendar calendar = new GregorianCalendar();
//                calendarS.roll(Calendar.HOUR, Gasket.getDateDifference());
//                writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ ---- " + dateFormat.format(calendar.getTime()));
                Gasket.setPROFIT_Buy(Gasket.getPROFIT_Buy() + Gasket.getTake());
                ConsoleHelper.writeMessage(ID + " --- ИТОГО на счету БАЙ --- " + Gasket.getPROFIT_Buy());
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе RUN класса StrategyOneSell.");
                e.printStackTrace();
            }
        }
        ConsoleHelper.printStatistics();
    }
}
