package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.strategies.oneStrategies.TestOrderBuyMiniRevers;
import bitmex.Bot.model.strategies.oneStrategies.TestOrderBuyRevers;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;




public class TestOrderBuyPattern extends Thread{
    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderBuyPattern(String id, double priseOpenOrder) {
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
        ConsoleHelper.writeMessage(ID + " --- RUN класса TestOrderBuyPattern начал считать --- "
                + DatesTimes.getDateTerminal());

        while (true) {
            BitmexQuote bitmexQuote = Gasket.getBitmexQuote();
            double priceAsk = bitmexQuote.getAskPrice();
            double priceBid = bitmexQuote.getAskPrice();

            if (priceBid <= priseStopOrder) {
                flag();
                setStop();

                ConsoleHelper.writeMessage(ID + " --- Сработал СТОП ЛОСС ---- "
                        + DatesTimes.getDateTerminal());
                Gasket.setPROFIT_Buy(Gasket.getPROFIT_Buy() - Gasket.getStop());
                break;
            }

            if (priceAsk >= priseTakeOrder) {
                flag();
                setTake();

                ConsoleHelper.writeMessage(ID + " --- Сработал ТЕЙК ПРОФИТ ---- "
                        + DatesTimes.getDateTerminal());
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
        ConsoleHelper.printStatisticsPatterns();
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
        if (ID.endsWith("-PAT")) Gasket.setObStopPat(Gasket.getObStopPat() + 1);
    }

    private void setTake() {
        if (ID.endsWith("-PAT")) Gasket.setObTakePat(Gasket.getObTakePat() + 1);
    }
}

