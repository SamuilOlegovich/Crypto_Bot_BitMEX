package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;


public class TestOrderBuyPatternUser extends Thread {

    private double priseTakeOrder;
    private double priseStopOrder;
    private double priseOpenOrder;
    private String ID;

    public TestOrderBuyPatternUser(String zeroStrimg, double priseOpenOrder) {
        this.priseTakeOrder = priseOpenOrder + Gasket.getTake();
        this.priseStopOrder = priseOpenOrder - Gasket.getStop();
        this.priseOpenOrder = priseOpenOrder;
        this.ID =  zeroStrimg;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage( DatesTimes.getDateTerminal() + " --- "
                + ID + " --- RUN класса Test Order Buy Pattern USER начал считать");

        while (true) {
            double priceAsk = Gasket.getBitmexQuote().getAskPrice();
            double priceBid = Gasket.getBitmexQuote().getBidPrice();

            if (priceBid <= priseStopOrder) {
                flag();
                setStop();

                // меняем число положительных / отрицательных сделок
                // а так же устанавливаем знаки для предсказателя
                String data = (Integer.parseInt(StringHelper.giveData(TypeData.SELL, ID)) + 1) + "";
//                String out = StringHelper.setData(TypeData.PREDICTOR, TypeData.LOSS.toString(), ID);
                String out = StringHelper.setData(TypeData.SELL, data, ID);

//                String[] strings = ID.split("===");
//                strings[3] = (Integer.parseInt(strings[3]) + 1) + "";
//
//                StringBuilder stringBuilder = new StringBuilder();
//
//                for (int i = 0; i < strings.length; i++) {
//                    stringBuilder.append(strings[i]);
//
//                    if (i != strings.length - 1) {
//                        stringBuilder.append("===");
//                    }
//                }

//                new UpdatingStatisticsDataUser(stringBuilder.toString());
                new UpdatingStatisticsDataUser(out);

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал СТОП ЛОСС USER");

                Gasket.setPROFIT_Buy_PAT(Gasket.getPROFIT_Buy_PAT() - Gasket.getStop());
                break;
            }

            if (priceAsk >= priseTakeOrder) {
                flag();
                setTake();

                // меняем число положительных / отрицательных сделок
                // а так же устанавливаем знаки для предсказателя
                String data = (Integer.parseInt(StringHelper.giveData(TypeData.BUY, ID)) + 1) + "";
//                String out = StringHelper.setData(TypeData.PREDICTOR, TypeData.TAKE.toString(), ID);
                String out = StringHelper.setData(TypeData.BUY, data, ID);

//                String[] strings = ID.split("===");
//                strings[1] = (Integer.parseInt(strings[1]) + 1) + "";
//
//                StringBuilder stringBuilder = new StringBuilder();
//
//                for (int i = 0; i < strings.length; i++) {
//                    stringBuilder.append(strings[i]);
//
//                    if (i != strings.length - 1) {
//                        stringBuilder.append("===");
//                    }
//                }

//                new UpdatingStatisticsDataUser(stringBuilder.toString());
                new UpdatingStatisticsDataUser(out);

                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Сработал ТЕЙК ПРОФИТ USER");

                Gasket.setPROFIT_Buy_PAT(Gasket.getPROFIT_Buy_PAT() + Gasket.getTake());
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + ID + " --- Не смогли проснуться в методе RUN класса StrategyOneSell USER");
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

