package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;


public class TestOrderSell extends Thread {
    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderSell(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder - Gasket.getTake();
        this.priseStopOrder = priseOpenOrder + Gasket.getStop();
        new TestOrderSellMiniRevers(id, priseOpenOrder);
        new TestOrderSellRevers(id, priseOpenOrder);
        this.priseOpenOrder = priseOpenOrder;
        this.ID = id;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса TestOrderSell начал считать --- "
                + DatesTimes.getDateTerminal());

        while (true) {
            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

            if (priceAsk >= priseStopOrder) {
                flag();
                setStop();

                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС ---- "
                        + DatesTimes.getDateTerminal());
                Gasket.setPROFIT_Sell(Gasket.getPROFIT_Sell() - Gasket.getStop());
                break;
            }

            if (priceBid <= priseTakeOrder) {
                flag();
                setTake();

                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ ---- "
                        + DatesTimes.getDateTerminal());
                Gasket.setPROFIT_Sell(Gasket.getPROFIT_Sell() + Gasket.getTake());
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID + " --- Не смогли проснуться в методе RUN класса StrategyOneSell");
                e.printStackTrace();
            }
        }
        ConsoleHelper.printStatistics();
    }





    private void flag() {
        if (Gasket.getStrategyWorkOne() == 1) Gasket.setOb_os_Flag(true);
        else if (Gasket.getStrategyWorkOne() == 2) {
            if (!Gasket.isOsFlag_4()) Gasket.setOsFlag_4(true);
            if (!Gasket.isOsFlag_3()) Gasket.setOsFlag_3(true);
            if (!Gasket.isOsFlag_2()) Gasket.setOsFlag_2(true);
            if (!Gasket.isOsFlag()) Gasket.setOsFlag(true);
        }
    }

    private void setStop() {
        if (ID.endsWith("-OS_5")) Gasket.setOs5Stop(Gasket.getOs5Stop() + 1);
        if (ID.endsWith("-OS_4")) Gasket.setOs4Stop(Gasket.getOs4Stop() + 1);
        if (ID.endsWith("-OS_3")) Gasket.setOs3Stop(Gasket.getOs3Stop() + 1);
        if (ID.endsWith("-OS_2")) Gasket.setOs2Stop(Gasket.getOs2Stop() + 1);
        if (ID.endsWith("-OS")) Gasket.setOsStop(Gasket.getOsStop() + 1);
    }

    private void setTake() {
        if (ID.endsWith("-OS_5")) Gasket.setOs5Take(Gasket.getOs5Take() + 1);
        if (ID.endsWith("-OS_4")) Gasket.setOs4Take(Gasket.getOs4Take() + 1);
        if (ID.endsWith("-OS_3")) Gasket.setOs3Take(Gasket.getOs3Take() + 1);
        if (ID.endsWith("-OS_2")) Gasket.setOs2Take(Gasket.getOs2Take() + 1);
        if (ID.endsWith("-OS")) Gasket.setOsTake(Gasket.getOsTake() + 1);
    }
}
