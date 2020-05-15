package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.strategies.oneStrategies.TestOrderSellMiniRevers;
import bitmex.Bot.model.strategies.oneStrategies.TestOrderSellRevers;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

public class TestOrderSellPattern extends Thread {

    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderSellPattern(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder - Gasket.getTake();
        this.priseStopOrder = priseOpenOrder + Gasket.getStop();
//        new TestOrderSellMiniRevers(id, priseOpenOrder);
//        new TestOrderSellRevers(id, priseOpenOrder);
        this.priseOpenOrder = priseOpenOrder;
        this.ID = id;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + ID + " --- RUN класса TestOrderSellPattern начал считать");

        while (true) {
            double priceAsk = Gasket.getBitmexQuote().getAskPrice();
            double priceBid = Gasket.getBitmexQuote().getBidPrice();

            if (priceAsk >= priseStopOrder) {
                flag();
                setStop();

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал СТОП ЛОСС");
                Gasket.setPROFIT_Sell(Gasket.getPROFIT_Sell() - Gasket.getStop());
                break;
            }

            if (priceBid <= priseTakeOrder) {
                flag();
                setTake();

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал ТЕЙК ПРОФИТ");
                Gasket.setPROFIT_Sell(Gasket.getPROFIT_Sell() + Gasket.getTake());
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Не смогли проснуться в методе RUN класса TestOrderSellPattern");
            }
        }
        ConsoleHelper.printStatisticsPatterns();
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
        if (ID.endsWith("-PAT")) Gasket.setOsStopPat(Gasket.getOsStopPat() + 1);
    }

    private void setTake() {
        if (ID.endsWith("-PAT")) Gasket.setOsTakePat(Gasket.getOsTakePat() + 1);
    }
}
