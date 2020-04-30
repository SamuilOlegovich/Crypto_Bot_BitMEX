package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.strategies.DatesTimes;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;



public class TestOrderBuy extends Thread {
    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderBuy(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder + Gasket.getTake();
        this.priseStopOrder = priseOpenOrder - Gasket.getStop();
        new TestOrderBuyMiniRevers(id, priseOpenOrder);
        new TestOrderBuyRevers(id, priseOpenOrder);
        this.priseOpenOrder = priseOpenOrder;
        this.ID =  id;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(ID + " --- RUN класса TestOrderBuy начал считать --- "
                + DatesTimes.getDate());

        while (true) {
            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

            if (priceBid <= priseStopOrder) {
                flag();
                setStop();

                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС ---- "
                        + DatesTimes.getDate());
                Gasket.setPROFIT_Buy(Gasket.getPROFIT_Buy() - Gasket.getStop());
                break;
            }

            if (priceAsk >= priseTakeOrder) {
                flag();
                setTake();

                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ ---- "
                        + DatesTimes.getDate());
                Gasket.setPROFIT_Buy(Gasket.getPROFIT_Buy() + Gasket.getTake());
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID
                        + " --- Не смогли проснуться в методе RUN класса StrategyOneSell.");
                e.printStackTrace();
            }
        }
        ConsoleHelper.printStatistics();
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
        if (ID.endsWith("-OB_5")) Gasket.setOb5Stop(Gasket.getOb5Stop() + 1);
        if (ID.endsWith("-OB_4")) Gasket.setOb4Stop(Gasket.getOb4Stop() + 1);
        if (ID.endsWith("-OB_3")) Gasket.setOb3Stop(Gasket.getOb3Stop() + 1);
        if (ID.endsWith("-OB_2")) Gasket.setOb2Stop(Gasket.getOb2Stop() + 1);
        if (ID.endsWith("-OB")) Gasket.setObStop(Gasket.getObStop() + 1);
    }

    private void setTake() {
        if (ID.endsWith("-OB_5")) Gasket.setOb5Take(Gasket.getOb5Take() + 1);
        if (ID.endsWith("-OB_4")) Gasket.setOb4Take(Gasket.getOb4Take() + 1);
        if (ID.endsWith("-OB_3")) Gasket.setOb3Take(Gasket.getOb3Take() + 1);
        if (ID.endsWith("-OB_2")) Gasket.setOb2Take(Gasket.getOb2Take() + 1);
        if (ID.endsWith("-OB")) Gasket.setObTake(Gasket.getObTake() + 1);
    }

}
