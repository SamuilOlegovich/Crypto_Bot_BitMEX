package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.view.ConsoleHelper;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class TestOrderSell extends Thread {
    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderSell(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder - Gasket.getTake();
        this.priseStopOrder = priseOpenOrder + Gasket.getStop();
        this.priseOpenOrder = priseOpenOrder;
        this.ID = id;
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
        if (ID.endsWith("SOSSCT")) Gasket.setSosTStop(Gasket.getSosTStop() + 1);
        if (ID.endsWith("SOSR")) Gasket.setSosRStop(Gasket.getSosRStop() + 1);
        if (ID.endsWith("SOS")) Gasket.setSosStop(Gasket.getSosStop() + 1);
        if (ID.endsWith("OS")) Gasket.setOsStop(Gasket.getOsStop() + 1);
    }

    private void setTake() {
        if (ID.endsWith("SOSSCT")) Gasket.setSosTTake(Gasket.getSosTTake() + 1);
        if (ID.endsWith("SOSR")) Gasket.setSosRTake(Gasket.getSosRTake() + 1);
        if (ID.endsWith("SOS")) Gasket.setSosTake(Gasket.getSosTake() + 1);
        if (ID.endsWith("OS")) Gasket.setOsTake(Gasket.getOsTake() + 1);
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса TestOrderSell начал считать --- " + getDate());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("d-MM-yyyy hh:mm:ss");
//        Calendar calendarS = new GregorianCalendar();
//        calendarS.roll(Calendar.HOUR, Gasket.getDateDifference());
//        writeMessage(ID + " --- RUN класса TestOrderSell начал считать --- "
//                + dateFormat.format(calendarS.getTime()));

        while (true) {
//            BitmexChartData bitmexChartData = Gasket.price(Gasket.getTicker());
//            double high = bitmexChartData.getHigh();
//            double low = bitmexChartData.getLow();

            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

//            writeMessage(ID + " --- OPEN --- " + priseOpenOrder
//                    + " --- HIGH --- " + high + " --- LOW --- " + low
//                    + " --- STOP --- " + priseStopOrder + " --- TAKE --- " + priseTakeOrder);

//            if (high >= priseStopOrder) {
            if (priceAsk >= priseStopOrder) {
                flag();
                setStop();

                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС ---- " + getDate());
//                Calendar calendar = new GregorianCalendar();
//                calendar.roll(Calendar.HOUR, Gasket.getDateDifference());
//                writeMessage(ID + " --- Сработал СТОП ЛОСС ---- " + dateFormat.format(calendar.getTime()));
                Gasket.setPROFIT_Sell(Gasket.getPROFIT_Sell() - Gasket.getStop());
                ConsoleHelper.writeMessage(ID + " --- ИТОГО на счету СЕЛЛ --- " + Gasket.getPROFIT_Sell());
                break;
            }

//            if (low <= priseTakeOrder) {
            if (priceBid <= priseTakeOrder) {
                flag();
                setTake();

                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ ---- " + getDate());
//                Calendar calendar = new GregorianCalendar();
//                calendar.roll(Calendar.HOUR, Gasket.getDateDifference());
//                writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ ---- " + dateFormat.format(calendar.getTime()));
                Gasket.setPROFIT_Sell(Gasket.getPROFIT_Sell() + Gasket.getTake());
                ConsoleHelper.writeMessage(ID + " --- ИТОГО на счету CEЛЛ --- " + Gasket.getPROFIT_Sell());
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
