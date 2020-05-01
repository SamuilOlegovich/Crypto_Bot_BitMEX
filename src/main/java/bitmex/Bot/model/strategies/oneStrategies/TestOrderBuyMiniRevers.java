package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;


public class TestOrderBuyMiniRevers extends Thread {
    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderBuyMiniRevers(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder - Gasket.getTake();
        this.priseStopOrder = priseOpenOrder + Gasket.getTake();
        this.priseOpenOrder = priseOpenOrder;
        this.ID = id + "-MR";
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса TestOrderSellMiniRevers начал считать --- "
                + DatesTimes.getDateTerminal());

        while (true) {
            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

            if (priceAsk >= priseStopOrder) {
                flag();
                setStop();

                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС MiniR ---- "
                        + DatesTimes.getDateTerminal());
                Gasket.setPROFIT_Buy_MR(Gasket.getPROFIT_Buy_MR() - Gasket.getTake());
                break;
            }

            if (priceBid <= priseTakeOrder) {
                flag();
                setTake();

                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ MiniR ---- "
                        + DatesTimes.getDateTerminal());
                Gasket.setPROFIT_Buy_MR(Gasket.getPROFIT_Buy_MR() + Gasket.getTake());
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе RUN класса TestOrderSellMiniRevers");
                e.printStackTrace();
            }
        }
        ConsoleHelper.printStatisticsMr();
    }





    private void flag() {
//        if (Gasket.getStrategyWorkOne() == 1) Gasket.setOb_os_Flag(true);
//        else if (Gasket.getStrategyWorkOne() == 2) {
//            if (!Gasket.isOsFlag_4()) Gasket.setOsFlag_4(true);
//            if (!Gasket.isOsFlag_3()) Gasket.setOsFlag_3(true);
//            if (!Gasket.isOsFlag_2()) Gasket.setOsFlag_2(true);
//            if (!Gasket.isOsFlag()) Gasket.setOsFlag(true);
//        }
    }

    private void setStop() {
        if (ID.endsWith("-OS_5-MR")) Gasket.setOs5StopMr(Gasket.getOs5StopMr() + 1);
        if (ID.endsWith("-OS_4-MR")) Gasket.setOs4StopMr(Gasket.getOs4StopMr() + 1);
        if (ID.endsWith("-OS_3-MR")) Gasket.setOs3StopMr(Gasket.getOs3StopMr() + 1);
        if (ID.endsWith("-OS_2-MR")) Gasket.setOs2StopMr(Gasket.getOs2StopMr() + 1);
        if (ID.endsWith("-OS-MR")) Gasket.setOsStopMr(Gasket.getOsStopMr() + 1);
    }

    private void setTake() {
        if (ID.endsWith("-OS_5-MR")) Gasket.setOs5TakeMr(Gasket.getOs5TakeMr() + 1);
        if (ID.endsWith("-OS_4-MR")) Gasket.setOs4TakeMr(Gasket.getOs4TakeMr() + 1);
        if (ID.endsWith("-OS_3-MR")) Gasket.setOs3TakeMr(Gasket.getOs3TakeMr() + 1);
        if (ID.endsWith("-OS_2-MR")) Gasket.setOs2TakeMr(Gasket.getOs2TakeMr() + 1);
        if (ID.endsWith("-OS-MR")) Gasket.setOsTakeMr(Gasket.getOsTakeMr() + 1);
    }
}

