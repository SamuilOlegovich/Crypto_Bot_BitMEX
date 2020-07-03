package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;



public class TestOrderBuyPatternUser extends Thread {

    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderBuyPatternUser(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder + Gasket.getTake();
        this.priseStopOrder = priseOpenOrder - Gasket.getStop();
        this.priseOpenOrder = priseOpenOrder;
        this.ID =  id;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage( DatesTimes.getDateTerminal() + " --- "
                + ID + " --- RUN класса TestOrderBuyPatternUser начал считать");

        while (true) {
            double priceAsk = Gasket.getBitmexQuote().getAskPrice();
            double priceBid = Gasket.getBitmexQuote().getBidPrice();

            if (priceBid <= priseStopOrder) {
                flag();
                setStop();

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
                        + ID + " --- Сработал СТОП ЛОСС USER");

                Gasket.setPROFIT_Buy_PAT(Gasket.getPROFIT_Buy_PAT() - Gasket.getStop());
                break;
            }

            if (priceAsk >= priseTakeOrder) {
                flag();
                setTake();

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
                        + ID + " --- Сработал ТЕЙК ПРОФИТ USER");

                Gasket.setPROFIT_Buy_PAT(Gasket.getPROFIT_Buy_PAT() + Gasket.getTake());
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Не смогли проснуться в методе RUN класса StrategyOneSellUser");
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
        Gasket.setObStopPat(Gasket.getObStopPat() + 1);
    }

    private void setTake() {
        Gasket.setObTakePat(Gasket.getObTakePat() + 1);
    }
}

