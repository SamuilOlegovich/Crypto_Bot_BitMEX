package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.TradeSell;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;


import java.util.Date;

import static bitmex.Bot.model.Gasket.getTimeCalculationCombinationLevel;

public class StrategyOneSellThread extends Thread {
    private InfoIndicator volume;
    private InfoIndicator ask;
    private String ID;
    private Date date;

    public StrategyOneSellThread(String id, InfoIndicator volume, InfoIndicator ask) {
        this.date = new Date();
        this.volume = volume;
        this.ask = ask;
        this.ID = id;
        start();
    }


    @Override
    public void run() {
        // получаем цену на данный момент и сравниваем с аском и объемом если цена поднялась выше этих уровней -
        // то все отменяем, если же пошла вниз то заходим в сел.
        double max = volume.getPrice() - Gasket.getRangePriceMAX();
        double min = volume.getPrice() < ask.getPrice()
                ? ask.getPrice() + Gasket.getRangePriceMIN() : volume.getPrice() + Gasket.getRangePriceMIN();

        ConsoleHelper.writeMessage(ID + " --- RUN класса Strategy One Sell Thread начал работать ---- "
                + DatesTimes.getDateTerminal() + "\n" + ID + " --- MAX ---- " + max + " --- MIN --- " + min);

        while (true) {

            double close = Gasket.getBitmexQuote().getBidPrice();

            if (close > min || !isRelevantDate()) {
                flag();
                if (!isRelevantDate()) {
                    ConsoleHelper.writeMessage(ID + " --- Сделка Селл ОТМЕНЕНА по дате комбинации---- "
                            + DatesTimes.getDateTerminal());
                }
                else if (Gasket.isUseStopLevelOrNotStop() && close > min) {
                    ConsoleHelper.writeMessage(ID + " --- Сделка Селл ВЫШЛА ЗА уровень MIN ---- "
                            + DatesTimes.getDateTerminal());
                    new StopSellTimeThread(ID, max, min);
                } else {
                    ConsoleHelper.writeMessage(ID + " --- Сделка Селл ОТМЕНЕНА ---- "
                            + DatesTimes.getDateTerminal());
                }
                break;
            }

            if (close < max && isRelevantDate()) {
                if (Gasket.isActiveNumberOfCandlesForAnalysis()) {
                    new RangeFlatSellThread(ID, close);
                    return;
                } else {
                    if (Gasket.isTrading()) new TradeSell(ID);
                    ConsoleHelper.writeMessage(ID + " --- Сделал сделку Селл ---- "
                            + DatesTimes.getDateTerminal());
                    new TestOrderSell(ID, close);
                    return;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(ID
                        + " --- Не смогли проснуться в методе RUN класса Strategy One Sell");
                e.printStackTrace();
            }
        }
    }



    private void flag() {
        if (Gasket.getStrategyWorkOne() == 1) Gasket.setOb_os_Flag(true);
        else if (Gasket.getStrategyWorkOne() == 2) {
            if (!Gasket.isOsFlag_5()) Gasket.setOsFlag_5(true);
            if (!Gasket.isOsFlag_4()) Gasket.setOsFlag_4(true);
            if (!Gasket.isOsFlag_3()) Gasket.setOsFlag_3(true);
            if (!Gasket.isOsFlag_2()) Gasket.setOsFlag_2(true);
            if (!Gasket.isOsFlag()) Gasket.setOsFlag(true);
        }
    }

    private boolean isRelevantDate() {
        Date dateNow = new Date();
        if ((dateNow.getTime() - date.getTime())
                < (long) (1000 * 60 * getTimeCalculationCombinationLevel())) {
            return true;
        } else return false;
    }
}
