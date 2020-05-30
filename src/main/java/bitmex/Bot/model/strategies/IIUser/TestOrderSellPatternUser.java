package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;



public class TestOrderSellPatternUser extends Thread {

    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderSellPatternUser(String id, double priseOpenOrder) {
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
                + ID + " --- RUN класса TestOrderSellPatternUser начал считать");

        while (true) {
            double priceAsk = Gasket.getBitmexQuote().getAskPrice();
            double priceBid = Gasket.getBitmexQuote().getBidPrice();

            if (priceAsk >= priseStopOrder) {
                flag();
                setStop();

                String[] strings = ID.split("===");
                strings[1] = (Integer.parseInt(strings[1]) + 1) + "";

                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < strings.length; i++) {
                    stringBuilder.append(strings[i]);

                    if (i != strings.length - 1) {
                        stringBuilder.append("===");
                    }
                }

                new UpdatingStatisticsDataUser(stringBuilder.toString());

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал СТОП ЛОСС USER");

                Gasket.setPROFIT_Sell_PAT(Gasket.getPROFIT_Sell_PAT() - Gasket.getStop());
                break;
            }

            if (priceBid <= priseTakeOrder) {
                flag();
                setTake();
                String[] strings = ID.split("===");
                strings[3] = (Integer.parseInt(strings[3]) + 1) + "";

                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < strings.length; i++) {
                    stringBuilder.append(strings[i]);

                    if (i != strings.length - 1) {
                        stringBuilder.append("===");
                    }
                }

                new UpdatingStatisticsDataUser(stringBuilder.toString());

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал ТЕЙК ПРОФИТ USER");

                Gasket.setPROFIT_Sell_PAT(Gasket.getPROFIT_Sell_PAT() + Gasket.getTake());
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Не смогли проснуться в методе RUN класса TestOrderSellPatternUser");
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
        Gasket.setOsStopPat(Gasket.getOsStopPat() + 1);
    }

    private void setTake() {
        Gasket.setOsTakePat(Gasket.getOsTakePat() + 1);
    }
}

