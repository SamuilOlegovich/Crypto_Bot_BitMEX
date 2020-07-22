package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import static bitmex.Bot.model.enums.TypeData.MARTINGALE;
import static bitmex.Bot.model.enums.TypeData.TYPE;



public class TestOrderBuyPatternMartingale extends Thread {
    private Martingale martingale;
    private double priseTakeOrder;
    private double priseStopOrder;
    private String zeroString;
    private String steeps;
    private String types;
    private String ID;


    public TestOrderBuyPatternMartingale(String zeroString, double priseOpenOrder) {
        this.steeps = StringHelper.giveData(MARTINGALE, zeroString);
        this.ID = StringHelper.giveData(TypeData.ID, zeroString);
        this.priseTakeOrder = priseOpenOrder + Gasket.getTake();
        this.priseStopOrder = priseOpenOrder - Gasket.getStop();
        this.types = StringHelper.giveData(TYPE, zeroString);
        this.martingale = Gasket.getMartingaleClass();
        this.zeroString = zeroString;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage( DatesTimes.getDateTerminal() + " --- "
                + ID + " --- RUN класса TestOrderBuyPatternMartingale начал считать");

        while (true) {
            double priceAsk = Gasket.getBitmexQuote().getAskPrice();
            double priceBid = Gasket.getBitmexQuote().getBidPrice();

            if (priceBid <= priseStopOrder) {
                setStop();

                // меняем число положительных / отрицательных сделок
                // а так же максимальный шаг мартина
                String data = (Integer.parseInt(StringHelper.giveData(TypeData.SELL, zeroString)) - 1) + "";
                String out = StringHelper.setData(TypeData.SELL, data, zeroString);

                out = Integer.parseInt(steeps) > martingale.getSteep(types)
                        ? out : StringHelper.setData(MARTINGALE, martingale.getSteep(types) + "", out);

                new UpdatingStatisticsDataUser(out);

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал СТОП ЛОСС USER " + MARTINGALE.toString());
                break;
            }

            if (priceAsk >= priseTakeOrder) {
                setTake();

                // меняем число положительных / отрицательных сделок
                // а так же максимальный шаг мартина
                String data = (Integer.parseInt(StringHelper.giveData(TypeData.BUY, zeroString)) + 1) + "";
                String out = StringHelper.setData(TypeData.BUY, data, zeroString);

                out = Integer.parseInt(steeps) > martingale.getSteep(types)
                        ? out : StringHelper.setData(MARTINGALE, martingale.getSteep(types) + "", out);


                new UpdatingStatisticsDataUser(out);

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал ТЕЙК ПРОФИТ USER " + MARTINGALE.toString());
                break;
            }

            try {
                Thread.sleep(Gasket.getSECOND());
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Не смогли проснуться в методе RUN класса TestOrderBuyPatternMartingale");
            }
        }
        ConsoleHelper.printStatisticsMartingale();
    }

    private void setStop() {
        double priseLot = Gasket.getStop() / Gasket.getBitmexQuote().getAskPrice();
        int steep = martingale.getSteep(StringHelper.giveData(TYPE, zeroString));
        double resultLot = Gasket.getMartingaleOpenOneLot();
        double result = 0.0;

        if (steep > 1) {
            for (int i = 2; i <= steep; i++) {
                resultLot = resultLot * Gasket.getMartingaleIndex();
            }
        }

        result = resultLot * priseLot;
        martingale.setMartingalePROFIT(martingale.getMartingalePROFIT() + result);
    }

    private void setTake() {
        double priseLot = Gasket.getStop() / Gasket.getBitmexQuote().getAskPrice();
        int steep = martingale.getSteep(StringHelper.giveData(TYPE, zeroString));
        double resultLot = Gasket.getMartingaleOpenOneLot();
        double result = 0.0;

        if (steep > 1) {
            for (int i = 2; i <= steep; i++) {
                resultLot = resultLot * Gasket.getMartingaleIndex();
            }
        }

        result = resultLot * priseLot;
        martingale.zeroSteep(StringHelper.giveData(TYPE, zeroString));
        martingale.setMartingalePROFIT(martingale.getMartingalePROFIT() + result);
    }
}
