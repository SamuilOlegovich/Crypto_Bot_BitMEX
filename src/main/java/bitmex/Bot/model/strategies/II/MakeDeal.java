package bitmex.Bot.model.strategies.II;


import bitmex.Bot.model.strategies.oneStrategies.TradeBuy;
import bitmex.Bot.model.strategies.oneStrategies.TradeSell;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;





// Определяем какую сделку сделать и даем команду на ее исполнение
public class MakeDeal extends Thread {
    private String stringIn;


    public MakeDeal(String stringIn) {
        this.stringIn = stringIn;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Определяю какую сделку сделать согласно ИНФО ПАТТЕРНАМ");

        String[] strings = stringIn.split("===");
        String stringOut = stringIn;

        if (Integer.parseInt(strings[1]) > Integer.parseInt(strings[3])) {

            if (Gasket.isTrading()) new TradeBuy(stringOut);
            new TestOrderBuyPattern(stringOut, Gasket.getBitmexQuote().getAskPrice());

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + stringOut + " --- Согластно ПАТТЕРНУ сделал сделку БАЙ");

        } else if (Integer.parseInt(strings[1]) < Integer.parseInt(strings[3])) {

            if (Gasket.isTrading()) new TradeSell(stringOut);
            new TestOrderSellPattern(stringOut, Gasket.getBitmexQuote().getBidPrice());

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + stringOut + " --- Согластно ПАТТЕРНУ сделал сделку СЕЛЛ");

        }
    }
}
