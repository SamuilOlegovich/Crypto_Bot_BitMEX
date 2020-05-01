package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;

public class TestOrderSellRevers extends Thread {
    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderSellRevers(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder + Gasket.getStop() - 5;
        this.priseStopOrder = priseOpenOrder - Gasket.getTake();
        this.priseOpenOrder = priseOpenOrder;
        this.ID = id + "-R";
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса Test Order Sell Revers начал считать --- "
                + DatesTimes.getDateTerminal());

        while (true) {
            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

            if (priceBid <= priseStopOrder) {
                flag();
                setStop();
                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС R ---- "
                        + DatesTimes.getDateTerminal());
                Gasket.setPROFIT_Sell_R(Gasket.getPROFIT_Sell_R() - Gasket.getTake());
                break;
            }

            if (priceAsk >= priseTakeOrder) {
                flag();
                setTake();
                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ R ---- "
                        + DatesTimes.getDateTerminal());
                Gasket.setPROFIT_Sell_R(Gasket.getPROFIT_Sell_R() + Gasket.getStop() - 5);
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID
                        + " --- Не смогли проснуться в методе RUN класса TestOrderBuyMiniRevers");
                e.printStackTrace();
            }
        }
        ConsoleHelper.printStatisticsR();
    }




    private void flag() {
//        if (Gasket.getStrategyWorkOne() == 1) Gasket.setOb_os_Flag(true);
//        else if (Gasket.getStrategyWorkOne() == 2) {
//            if (!Gasket.isObFlag_4()) Gasket.setObFlag_4(true);
//            if (!Gasket.isObFlag_3()) Gasket.setObFlag_3(true);
//            if (!Gasket.isObFlag_2()) Gasket.setObFlag_2(true);
//            if (!Gasket.isObFlag()) Gasket.setObFlag(true);
//        }
    }

    private void setStop() {
        if (ID.endsWith("-OB_5-R")) Gasket.setOb5StopR(Gasket.getOb5StopR() + 1);
        if (ID.endsWith("-OB_4-R")) Gasket.setOb4StopR(Gasket.getOb4StopR() + 1);
        if (ID.endsWith("-OB_3-R")) Gasket.setOb3StopR(Gasket.getOb3StopR() + 1);
        if (ID.endsWith("-OB_2-R")) Gasket.setOb2StopR(Gasket.getOb2StopR() + 1);
        if (ID.endsWith("-OB-R")) Gasket.setObStopR(Gasket.getObStopR() + 1);
    }

    private void setTake() {
        if (ID.endsWith("-OB_5-R")) Gasket.setOb5TakeR(Gasket.getOb5TakeR() + 1);
        if (ID.endsWith("-OB_4-R")) Gasket.setOb4TakeR(Gasket.getOb4TakeR() + 1);
        if (ID.endsWith("-OB_3-R")) Gasket.setOb3TakeR(Gasket.getOb3TakeR() + 1);
        if (ID.endsWith("-OB_2-R")) Gasket.setOb2TakeR(Gasket.getOb2TakeR() + 1);
        if (ID.endsWith("-OB-R")) Gasket.setObTakeR(Gasket.getObTakeR() + 1);
    }
}

