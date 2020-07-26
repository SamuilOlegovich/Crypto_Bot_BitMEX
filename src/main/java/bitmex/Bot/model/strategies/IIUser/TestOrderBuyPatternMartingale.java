package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import static bitmex.Bot.model.enums.TypeData.ID;
import static bitmex.Bot.model.enums.TypeData.MARTINGALE;


public class TestOrderBuyPatternMartingale extends Thread {
    private OpenTransactions openTransactions;
    private Martingale martingale;
    private double priseTakeOrder;
    private double priseStopOrder;
    private String zeroString;
    private String steeps;
    private String IDs;


    public TestOrderBuyPatternMartingale(String zeroString, double priseOpenOrder) {
        this.steeps = StringHelper.giveData(MARTINGALE, zeroString);
        this.IDs = StringHelper.giveData(TypeData.ID, zeroString);
        this.priseTakeOrder = priseOpenOrder + Gasket.getTake();
        this.priseStopOrder = priseOpenOrder - Gasket.getStop();
        this.openTransactions = Gasket.getOpenTransactions();
        this.martingale = Gasket.getMartingaleClass();
        this.zeroString = zeroString;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage( DatesTimes.getDateTerminal() + " --- "
                + IDs + " --- RUN класса TestOrderBuyPatternMartingale начал считать");

        while (true) {
            double priceAsk = Gasket.getBitmexQuote().getAskPrice();
            double priceBid = Gasket.getBitmexQuote().getBidPrice();

            if (priceBid <= priseStopOrder) {
                setStop();

                // меняем число положительных / отрицательных сделок
                // а так же максимальный шаг мартина
                String data = (Integer.parseInt(StringHelper.giveData(TypeData.SELL, zeroString)) - 1) + "";
                String out = StringHelper.setData(TypeData.SELL, data, zeroString);

                new UpdatingStatisticsDataUser(out);
                openTransactions.zeroSteepTest(IDs);

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + IDs + " --- Сработал СТОП ЛОСС USER " + MARTINGALE.toString());
                break;
            }

            if (priceAsk >= priseTakeOrder) {
                setTake();

                // меняем число положительных / отрицательных сделок
                // а так же максимальный шаг мартина
                String data = (Integer.parseInt(StringHelper.giveData(TypeData.BUY, zeroString)) + 1) + "";
                String out = StringHelper.setData(TypeData.BUY, data, zeroString);

                new UpdatingStatisticsDataUser(out);
                openTransactions.zeroSteepTest(IDs);
                martingale.zeroSteep(IDs);

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + IDs + " --- Сработал ТЕЙК ПРОФИТ USER " + MARTINGALE.toString());
                break;
            }

            try {
                Thread.sleep(Gasket.getSECOND());
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + IDs + " --- Не смогли проснуться в методе RUN класса TestOrderBuyPatternMartingale");
            }
        }
        ConsoleHelper.printStatisticsMartingale();
    }

    private void setStop() {
//        double priseLot = Gasket.getStop() / Gasket.getBitmexQuote().getAskPrice();
        double openLot = openTransactions.getOrderVolumeTest(IDs);
//        double resultLot = Gasket.getMartingaleOpenOneLot();
        double priceLot = Gasket.getStop() / 10000.0;
//        int steep = martingale.getSteep(IDs);
        double result = 0.0;
//        if (steep > 1) {
//            for (int i = 2; i <= steep; i++) {
//                resultLot = resultLot + (result * Gasket.getMartingaleIndex());
//            }
//        }
//        result = resultLot * priseLot;
        result = priceLot * openLot;
        martingale.setMartingalePROFIT(martingale.getMartingalePROFIT() - result);
    }

    private void setTake() {
//        double priceLot = Gasket.getTake() / Gasket.getBitmexQuote().getAskPrice();
        double priceLot = Gasket.getTake() / 10000.0;
//        double resultLot = Gasket.getMartingaleOpenOneLot();
        double openLot = openTransactions.getOrderVolumeTest(IDs);
//        int steep = martingale.getSteep(IDs);
        double result = 0.0;
//        if (steep > 1) {
//            for (int i = 2; i <= steep; i++) {
//                resultLot = (resultLot * Gasket.getMartingaleIndex());
//            }
//        }
//        result = resultLot * priseLot;
        result = priceLot * openLot;
        martingale.setMartingalePROFIT(martingale.getMartingalePROFIT() + result);
    }
}
