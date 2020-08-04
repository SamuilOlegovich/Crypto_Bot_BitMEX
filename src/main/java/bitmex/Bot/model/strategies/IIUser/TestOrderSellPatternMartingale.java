package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

import static bitmex.Bot.model.enums.TypeData.MARTINGALE;


public class TestOrderSellPatternMartingale extends Thread {
    private OpenTransactions openTransactions;
    private Martingale martingale;
    private double priseTakeOrder;
    private double priseStopOrder;
    private boolean testOrReal;
    private String zeroString;
    private String steeps;
    private String IDs;


    public TestOrderSellPatternMartingale(String zeroString, double priseOpenOrder) {
        this.steeps = StringHelper.giveData(MARTINGALE, zeroString);
        this.IDs = StringHelper.giveData(TypeData.ID, zeroString);
        this.priseTakeOrder = priseOpenOrder - Gasket.getTake();
        this.priseStopOrder = priseOpenOrder + Gasket.getStop();
        this.openTransactions = Gasket.getOpenTransactions();
        this.martingale = Gasket.getMartingaleClass();
        this.zeroString = zeroString;
        this.testOrReal = false;
        start();
    }

    public TestOrderSellPatternMartingale(boolean b, String zeroString, double priseOpenOrder) {
        this.steeps = StringHelper.giveData(MARTINGALE, zeroString);
        this.IDs = StringHelper.giveData(TypeData.ID, zeroString);
        this.priseTakeOrder = priseOpenOrder - Gasket.getTake();
        this.priseStopOrder = priseOpenOrder + Gasket.getStop();
        this.openTransactions = Gasket.getOpenTransactions();
        this.martingale = Gasket.getMartingaleClass();
        this.zeroString = zeroString;
        this.testOrReal = b;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + IDs + " --- RUN класса TestOrderSellPatternMartingale начал считать");

        if (Gasket.isIndentPriceOnOff()) {
            priseStopOrder = priseStopOrder - Gasket.getIndentPrice();
        }


        while (true) {
            double priceAsk = Gasket.getBitmexQuote().getAskPrice();
            double priceBid = Gasket.getBitmexQuote().getBidPrice();

            if (priceAsk >= priseStopOrder) {
                setStop();
                // меняем число положительных / отрицательных сделок
                // а так же максимальный шаг мартина
                String data = (Integer.parseInt(StringHelper.giveData(TypeData.BUY, zeroString)) - 1) + "";
                String out = StringHelper.setData(TypeData.BUY, data, zeroString);

                new UpdatingStatisticsDataUser(out);

                if (testOrReal) {
                    openTransactions.zeroSteepReal(IDs);
                } else {
                    openTransactions.zeroSteepTest(IDs);
                }

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + IDs + " --- Сработал СТОП ЛОСС USER " + MARTINGALE.toString() + " - по цене - "
                        + Gasket.getBitmexQuote().getAskPrice()
                );
                break;
            }

            if (priceBid <= priseTakeOrder) {
                setTake();

                // меняем число положительных / отрицательных сделок
                // а так же максимальный шаг мартина
                String data = (Integer.parseInt(StringHelper.giveData(TypeData.SELL, zeroString)) + 1) + "";
                String out = StringHelper.setData(TypeData.SELL, data, zeroString);

                new UpdatingStatisticsDataUser(out);
                if (testOrReal) {
                    openTransactions.zeroSteepReal(IDs);
                    martingale.zeroSteepReal(IDs);
                } else {
                    openTransactions.zeroSteepTest(IDs);
                    martingale.zeroSteep(IDs);
                }

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + IDs + " --- Сработал ТЕЙК ПРОФИТ USER " + MARTINGALE.toString() + " - по цене - "
                        + Gasket.getBitmexQuote().getBidPrice()
                );
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
        double priceLot = Gasket.getStop() / 10000.0;
        double result = 0.0;
        double openLot;

        if (testOrReal) {
            openLot = openTransactions.getOrderVolumeReal(IDs);
        } else {
            openLot = openTransactions.getOrderVolumeTest(IDs);
        }

        result = priceLot * openLot;

        if (testOrReal) {
            martingale.setMartingaleRealPROFIT(martingale.getMartingaleRealPROFIT() - result);
        } else {
            martingale.setMartingalePROFIT(martingale.getMartingalePROFIT() - result);
        }
    }



    private void setTake() {
        double priceLot = Gasket.getTake() / 10000.0;
        double result = 0.0;
        double openLot;

        if (testOrReal) {
            openLot = openTransactions.getOrderVolumeReal(IDs);
        } else {
            openLot = openTransactions.getOrderVolumeTest(IDs);
        }

        result = priceLot * openLot;

        if (testOrReal) {
            martingale.setMartingaleRealPROFIT(martingale.getMartingaleRealPROFIT() + result);
        } else {
            martingale.setMartingalePROFIT(martingale.getMartingalePROFIT() + result);
        }
    }
}
