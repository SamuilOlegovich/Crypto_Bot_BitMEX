package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;

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



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса TestOrderBuy начал считать --- " + getDate());

        while (true) {
            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

            if (priceBid <= priseStopOrder) {
                flag();
                setStop();

                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС ---- " + getDate());
                Gasket.setPROFIT_Buy(Gasket.getPROFIT_Buy() - Gasket.getStop());
                ConsoleHelper.writeMessage(ID + " --- ИТОГО на счету БАЙ --- " + Gasket.getPROFIT_Buy());
                break;
            }

            if (priceAsk >= priseTakeOrder) {
                flag();
                setTake();

                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ ---- " + getDate());
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



    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateFormat.format(date);
        date.setTime(Gasket.getDateDifference() > 0
                ? date.getTime() + (1000 * 60 * 60 * Math.abs(Gasket.getDateDifference()))
                : date.getTime() - (1000 * 60 * 60 * Math.abs(Gasket.getDateDifference())));
        return dateFormat.format(date);
    }

    private void flag() {
        if (Gasket.getStrategyWorkOne() == 1) Gasket.setOb_os_Flag(true);
        else if (Gasket.getStrategyWorkOne() == 2) {
            if (!Gasket.isObFlag_4()) Gasket.setObFlag_4(true);
            if (!Gasket.isObFlag_3()) Gasket.setObFlag_3(true);
            if (!Gasket.isObFlag_2()) Gasket.setObFlag_2(true);
            if (!Gasket.isObFlag()) Gasket.setObFlag(true);
        }
    }

    private void setStop() {
        if (ID.endsWith("-OB_4")) Gasket.setOb4Stop(Gasket.getOb4Stop() + 1);
        if (ID.endsWith("-OB_3")) Gasket.setOb3Stop(Gasket.getOb3Stop() + 1);
        if (ID.endsWith("-OB_2")) Gasket.setOb2Stop(Gasket.getOb2Stop() + 1);
        if (ID.endsWith("-OB")) Gasket.setObStop(Gasket.getObStop() + 1);
    }

    private void setTake() {
        if (ID.endsWith("-OB_4")) Gasket.setOb4Take(Gasket.getOb4Take() + 1);
        if (ID.endsWith("-OB_3")) Gasket.setOb3Take(Gasket.getOb3Take() + 1);
        if (ID.endsWith("-OB_2")) Gasket.setOb2Take(Gasket.getOb2Take() + 1);
        if (ID.endsWith("-OB")) Gasket.setObTake(Gasket.getObTake() + 1);
    }

}
