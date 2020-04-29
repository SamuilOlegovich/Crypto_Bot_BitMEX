package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.strategies.DatesTimes;
import bitmex.Bot.view.ConsoleHelper;

public class TestOrderSellMiniRevers extends Thread {
    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderSellMiniRevers(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder + Gasket.getTake();
        this.priseStopOrder = priseOpenOrder - Gasket.getTake();
        this.priseOpenOrder = priseOpenOrder;
        this.ID = id + "-MR";
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса TestOrderBuyMiniRevers начал считать --- "
                + DatesTimes.getDate());

        while (true) {
            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

            if (priceBid <= priseStopOrder) {
                flag();
                setStop();

                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС MiniR ---- "
                        + DatesTimes.getDate());
                Gasket.setPROFIT_Buy_MR(Gasket.getPROFIT_Buy_MR() - Gasket.getTake());
                ConsoleHelper.writeMessage(ID + " --- ИТОГО на счету БАЙ MiniR --- " + Gasket.getPROFIT_Buy_MR());
                break;
            }

            if (priceAsk >= priseTakeOrder) {
                flag();
                setTake();

                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ MiniR ---- "
                        + DatesTimes.getDate());
                Gasket.setPROFIT_Buy_MR(Gasket.getPROFIT_Buy_MR() + Gasket.getTake());
                ConsoleHelper.writeMessage(ID + " --- ИТОГО на счету БАЙ MiniR --- " + Gasket.getPROFIT_Buy_MR());
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
        ConsoleHelper.printStatisticsMr();
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
        if (ID.endsWith("-OB_5-MR")) Gasket.setOb5StopMr(Gasket.getOb5StopMr() + 1);
        if (ID.endsWith("-OB_4-MR")) Gasket.setOb4StopMr(Gasket.getOb4StopMr() + 1);
        if (ID.endsWith("-OB_3-MR")) Gasket.setOb3StopMr(Gasket.getOb3StopMr() + 1);
        if (ID.endsWith("-OB_2-MR")) Gasket.setOb2StopMr(Gasket.getOb2StopMr() + 1);
        if (ID.endsWith("-OB-MR")) Gasket.setObStopMr(Gasket.getObStopMr() + 1);
    }

    private void setTake() {
        if (ID.endsWith("-OB_5-MR")) Gasket.setOb5TakeMr(Gasket.getOb5TakeMr() + 1);
        if (ID.endsWith("-OB_4-MR")) Gasket.setOb4TakeMr(Gasket.getOb4TakeMr() + 1);
        if (ID.endsWith("-OB_3-MR")) Gasket.setOb3TakeMr(Gasket.getOb3TakeMr() + 1);
        if (ID.endsWith("-OB_2-MR")) Gasket.setOb2TakeMr(Gasket.getOb2TakeMr() + 1);
        if (ID.endsWith("-OB-MR")) Gasket.setObTakeMr(Gasket.getObTakeMr() + 1);
    }
}

