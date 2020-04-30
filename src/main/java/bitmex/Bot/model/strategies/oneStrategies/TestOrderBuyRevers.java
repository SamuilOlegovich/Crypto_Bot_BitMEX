package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.strategies.DatesTimes;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;

public class TestOrderBuyRevers extends Thread {
    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderBuyRevers(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder - Gasket.getStop() + 5;
        this.priseStopOrder = priseOpenOrder + Gasket.getTake();
        this.priseOpenOrder = priseOpenOrder;
        this.ID = id + "-R";
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса Test Order Buy Revers начал считать --- "
                + DatesTimes.getDate());

        while (true) {
            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

            if (priceAsk >= priseStopOrder) {
                flag();
                setStop();
                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС R ---- "
                        + DatesTimes.getDate());
                Gasket.setPROFIT_Buy_R(Gasket.getPROFIT_Buy_R() - Gasket.getTake());
                break;
            }

            if (priceBid <= priseTakeOrder) {
                flag();
                setTake();
                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ R ---- "
                        + DatesTimes.getDate());
                Gasket.setPROFIT_Buy_R(Gasket.getPROFIT_Buy_R() + Gasket.getStop() - 5);
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе RUN класса TestOrderBuyRevers");
                e.printStackTrace();
            }
        }
        ConsoleHelper.printStatisticsR();
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
        if (ID.endsWith("-OS_5-R")) Gasket.setOs5StopMr(Gasket.getOs5StopMr() + 1);
        if (ID.endsWith("-OS_4-R")) Gasket.setOs4StopMr(Gasket.getOs4StopMr() + 1);
        if (ID.endsWith("-OS_3-R")) Gasket.setOs3StopMr(Gasket.getOs3StopMr() + 1);
        if (ID.endsWith("-OS_2-R")) Gasket.setOs2StopMr(Gasket.getOs2StopMr() + 1);
        if (ID.endsWith("-OS-R")) Gasket.setOsStopMr(Gasket.getOsStopMr() + 1);
    }

    private void setTake() {
        if (ID.endsWith("-OS_5-R")) Gasket.setOs5TakeMr(Gasket.getOs5TakeMr() + 1);
        if (ID.endsWith("-OS_4-R")) Gasket.setOs4TakeMr(Gasket.getOs4TakeMr() + 1);
        if (ID.endsWith("-OS_3-R")) Gasket.setOs3TakeMr(Gasket.getOs3TakeMr() + 1);
        if (ID.endsWith("-OS_2-R")) Gasket.setOs2TakeMr(Gasket.getOs2TakeMr() + 1);
        if (ID.endsWith("-OS-R")) Gasket.setOsTakeMr(Gasket.getOsTakeMr() + 1);
    }
}

