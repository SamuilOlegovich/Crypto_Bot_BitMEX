package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

import static bitmex.Bot.model.enums.TypeData.MARTINGALE;
import static bitmex.Bot.model.enums.TypeData.TYPE;




public class TestOrderSellPatternMartingale extends Thread {
    private OpenTransactions openTransactions;
    private Martingale martingale;
    private double priseTakeOrder;
    private double priseStopOrder;
    private String zeroString;
    private String steeps;
//    private String types;
    private String IDs;


    public TestOrderSellPatternMartingale(String zeroString, double priseOpenOrder) {
        this.steeps = StringHelper.giveData(MARTINGALE, zeroString);
        this.IDs = StringHelper.giveData(TypeData.ID, zeroString);
        this.priseTakeOrder = priseOpenOrder + Gasket.getTake();
        this.priseStopOrder = priseOpenOrder - Gasket.getStop();
//        this.types = StringHelper.giveData(TYPE, zeroString);
        this.openTransactions = Gasket.getOpenTransactions();
        this.martingale = Gasket.getMartingaleClass();
        this.zeroString = zeroString;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + IDs + " --- RUN класса TestOrderSellPatternMartingale начал считать");

        while (true) {
            double priceAsk = Gasket.getBitmexQuote().getAskPrice();
            double priceBid = Gasket.getBitmexQuote().getBidPrice();

            if (priceAsk >= priseStopOrder) {
                openTransactions.zeroSteep(IDs);
                setStop();

                // меняем число положительных / отрицательных сделок
                // а так же максимальный шаг мартина
                String data = (Integer.parseInt(StringHelper.giveData(TypeData.BUY, zeroString)) - 1) + "";
                String out = StringHelper.setData(TypeData.BUY, data, zeroString);

                out = Integer.parseInt(steeps) > martingale.getSteep(IDs)
                        ? out : StringHelper.setData(MARTINGALE, martingale.getSteep(IDs) + "", out);


                new UpdatingStatisticsDataUser(out);

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + IDs + " --- Сработал СТОП ЛОСС USER " + MARTINGALE.toString());
                break;
            }

            if (priceBid <= priseTakeOrder) {
                openTransactions.zeroSteep(IDs);
                martingale.zeroSteep(IDs);
                setTake();


                // меняем число положительных / отрицательных сделок
                // а так же максимальный шаг мартина
                String data = (Integer.parseInt(StringHelper.giveData(TypeData.SELL, zeroString)) + 1) + "";
                String out = StringHelper.setData(TypeData.SELL, data, zeroString);

                out = Integer.parseInt(steeps) > martingale.getSteep(IDs)
                        ? out : StringHelper.setData(MARTINGALE, martingale.getSteep(IDs) + "", out);

                new UpdatingStatisticsDataUser(out);

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + IDs + " --- Сработал ТЕЙК ПРОФИТ USER " + MARTINGALE.toString());
                break;
            }

            try {
                Thread.sleep(Gasket.getSECOND());
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + IDs + " --- Не смогли проснуться в методе RUN класса TestOrderSellPatternMartingale");
            }
        }
        ConsoleHelper.printStatisticsMartingale();
    }


    private void setStop() {
        double priseLot = Gasket.getStop() / Gasket.getBitmexQuote().getAskPrice();
        double resultLot = Gasket.getMartingaleOpenOneLot();
        int steep = martingale.getSteep(IDs);
        double result = 0.0;

        if (steep > 1) {
            for (int i = 2; i <= steep; i++) {
                resultLot = resultLot * Gasket.getMartingaleIndex();
            }
        }

        result = resultLot * priseLot;
        martingale.setMartingalePROFIT(martingale.getMartingalePROFIT() - result);
    }

    private void setTake() {
        double priseLot = Gasket.getTake() / Gasket.getBitmexQuote().getAskPrice();
        double resultLot = Gasket.getMartingaleOpenOneLot();
        int steep = martingale.getSteep(IDs);
        double result = 0.0;

        if (steep > 1) {
            for (int i = 2; i <= steep; i++) {
                resultLot = resultLot * Gasket.getMartingaleIndex();
            }
        }

        result = resultLot * priseLot;
        martingale.setMartingalePROFIT(martingale.getMartingalePROFIT() + result);
    }
}
