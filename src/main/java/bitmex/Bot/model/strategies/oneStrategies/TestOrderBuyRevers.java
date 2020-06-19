package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.DatesTimes;
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
                + DatesTimes.getDateTerminal());

        while (true) {
            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

            if (priceAsk >= priseStopOrder) {
                flag();
                setStop();
                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС R ---- "
                        + DatesTimes.getDateTerminal());
                Gasket.setPROFIT_Buy_R(Gasket.getPROFIT_Buy_R() - Gasket.getTake());
                break;
            }

            if (priceBid <= priseTakeOrder) {
                flag();
                setTake();
                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ R ---- "
                        + DatesTimes.getDateTerminal());
                Gasket.setPROFIT_Buy_R(Gasket.getPROFIT_Buy_R() + Gasket.getStop() - 5);
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID
                        + " --- Не смогли проснуться в методе RUN класса TestOrderBuyRevers");
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
        if (ID.endsWith("-OS_5-R")) Gasket.setOs5StopR(Gasket.getOs5StopR() + 1);
        if (ID.endsWith("-OS_4-R")) Gasket.setOs4StopR(Gasket.getOs4StopR() + 1);
        if (ID.endsWith("-OS_3-R")) Gasket.setOs3StopR(Gasket.getOs3StopR() + 1);
        if (ID.endsWith("-OS_2-R")) Gasket.setOs2StopR(Gasket.getOs2StopR() + 1);
        if (ID.endsWith("-OS-R")) Gasket.setOsStopR(Gasket.getOsStopR() + 1);
    }

    private void setTake() {
        if (ID.endsWith("-OS_5-R")) Gasket.setOs5TakeR(Gasket.getOs5TakeR() + 1);
        if (ID.endsWith("-OS_4-R")) Gasket.setOs4TakeR(Gasket.getOs4TakeR() + 1);
        if (ID.endsWith("-OS_3-R")) Gasket.setOs3TakeR(Gasket.getOs3TakeR() + 1);
        if (ID.endsWith("-OS_2-R")) Gasket.setOs2TakeR(Gasket.getOs2TakeR() + 1);
        if (ID.endsWith("-OS-R")) Gasket.setOsTakeR(Gasket.getOsTakeR() + 1);
    }
}

