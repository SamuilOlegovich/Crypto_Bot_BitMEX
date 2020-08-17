package bitmex.Bot.model.strategies.iiPro;

import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;


public class TestOrderBuyPatternPro extends Thread {

    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderBuyPatternPro(String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder + Gasket.getTake();
        this.priseStopOrder = priseOpenOrder - Gasket.getStop();
        this.priseOpenOrder = priseOpenOrder;
        this.ID =  id;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage( DatesTimes.getDateTerminal() + " --- "
                + ID + " --- RUN класса TestOrderBuyPattern iiPRO начал считать");

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

                new UpdatingStatisticsDataPro(stringBuilder.toString());

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал СТОП ЛОСС iiPRO");
                Gasket.setPROFIT_Buy(Gasket.getPROFIT_Buy() - Gasket.getStop());
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

                new UpdatingStatisticsDataPro(stringBuilder.toString());

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал ТЕЙК ПРОФИТ iiPRO");
                Gasket.setPROFIT_Buy(Gasket.getPROFIT_Buy() + Gasket.getTake());
                break;
            }

            try {
                Thread.sleep(Gasket.getSECOND());
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Не смогли проснуться в методе RUN класса StrategyOneSell iiPRO");
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


