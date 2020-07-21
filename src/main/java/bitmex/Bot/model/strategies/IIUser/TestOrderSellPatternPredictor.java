package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;




public class TestOrderSellPatternPredictor extends Thread {

    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private boolean doNotDoDeal;
    private String ID;

    public TestOrderSellPatternPredictor(boolean doNotDoDeal, String id, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder - Gasket.getTake();
        this.priseStopOrder = priseOpenOrder + Gasket.getStop();
        this.priseOpenOrder = priseOpenOrder;
        this.doNotDoDeal = doNotDoDeal;

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
                setStop();

                // меняем число положительных / отрицательных сделок
                // а так же устанавливаем знаки для предсказателя
                String out = StringHelper.setData(TypeData.PREDICTOR, TypeData.LOSS.toString(), ID);

                if (doNotDoDeal) {
                    String data = (Integer.parseInt(StringHelper.giveData(TypeData.BUY, ID)) + 1) + "";
                    out = StringHelper.setData(TypeData.BUY, data, out);
                }

                new UpdatingStatisticsDataUser(out);

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал СТОП ЛОСС USER");

                Gasket.setPROFIT_Sell_PAT(Gasket.getPROFIT_Sell_PAT() - Gasket.getStop());
                break;
            }

            if (priceBid <= priseTakeOrder) {
                setTake();

                // меняем число положительных / отрицательных сделок
                // а так же устанавливаем знаки для предсказателя
                String out = StringHelper.setData(TypeData.PREDICTOR, TypeData.TAKE.toString(), ID);
                if (doNotDoDeal) {
                    String data = (Integer.parseInt(StringHelper.giveData(TypeData.SELL, ID)) + 1) + "";
                    out = StringHelper.setData(TypeData.SELL, data, out);
                }

                new UpdatingStatisticsDataUser(out);

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


    private void setStop() {
        Gasket.setOsStopPat(Gasket.getOsStopPat() + 1);
    }

    private void setTake() {
        Gasket.setOsTakePat(Gasket.getOsTakePat() + 1);
    }
}

