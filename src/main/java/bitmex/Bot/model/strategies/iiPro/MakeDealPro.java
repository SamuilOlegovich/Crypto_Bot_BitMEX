package bitmex.Bot.model.strategies.iiPro;

import bitmex.Bot.model.strategies.oneStrategies.TradeSell;
import bitmex.Bot.model.strategies.oneStrategies.TradeBuy;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;





public class MakeDealPro extends Thread {
    private String stringIn;


    public MakeDealPro(String stringIn) {
        this.stringIn = stringIn;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Определяю какую сделку сделать согласно II Pro ПАТТЕРНАМ");

        String[] strings = stringIn.split("===");
        String stringOut = stringIn;

        if (Integer.parseInt(strings[1]) > Integer.parseInt(strings[3])) {

            if (Gasket.isTradingIIPro()) {
                new TradeBuy(stringOut);
            }
            if (Gasket.isTradingTestIIPro()) {
                new TestOrderBuyPatternPro(stringOut, Gasket.getBitmexQuote().getAskPrice());
            }

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + stringOut + " --- Согластно ПАТТЕРНУ II Pro сделал сделку БАЙ");

        } else if (Integer.parseInt(strings[1]) < Integer.parseInt(strings[3])) {

            if (Gasket.isTradingIIPro()) {
                new TradeSell(stringOut);
            }
            if (Gasket.isTradingTestIIPro()) {
                new TestOrderSellPatternPro(stringOut, Gasket.getBitmexQuote().getBidPrice());
            }

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + stringOut + " --- Согластно ПАТТЕРНУ II Pro сделал сделку СЕЛЛ");

        }
    }
}

