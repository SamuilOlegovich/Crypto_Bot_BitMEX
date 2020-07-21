package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.TradeSell;
import bitmex.Bot.model.TradeBuy;
import bitmex.Bot.model.Gasket;

import java.util.ArrayList;
import java.util.Arrays;

import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.*;



public class MakeDealPredictor extends Thread {
    private ArrayList<String> marketList;
    private String patternZeroString;
    private boolean doNotDoDeal;


    public MakeDealPredictor(boolean doNotDoDeal, ArrayList<String> marketList, String patternZeroString) {
        this.marketList = new ArrayList<>(marketList);
        this.patternZeroString = patternZeroString;
        this.doNotDoDeal = doNotDoDeal;
        start();
    }



    @Override
    public void run() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Определяю какую сделку сделать согласно ПАТТЕРНАМ USER");

        String stringOut = patternZeroString;


        if (Integer.parseInt(giveData(BUY, patternZeroString))
                > Integer.parseInt(giveData(SELL, patternZeroString))) {

            if (conditionsAreMet(true)) {
                if (Gasket.isTradingUser() && !patternZeroString.endsWith(TEST.toString())) {
                    double index = (double) Integer.parseInt(giveData(BUY, patternZeroString))
                            / Integer.parseInt(giveData(SELL, patternZeroString));

                    if (index >= Gasket.getIndexRatioTransactionsAtWhichEnterMarket() && doNotDoDeal) {
                        new TradeBuy(stringOut);
                    }

                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                            + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                            + " сделал сделку БАЙ USER");
                }

                if (Gasket.isTradingTestUser()) {
                    new TestOrderBuyPatternPredictor(doNotDoDeal, stringOut, Gasket.getBitmexQuote().getAskPrice());

                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                            + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                            + " сделал сделку БАЙ USER - TEST");
                }

            } else {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                        + " сделку БАЙ USER отменил по истечению времени");
            }

        } else if (Integer.parseInt(giveData(BUY, patternZeroString))
                < Integer.parseInt(giveData(SELL, patternZeroString))) {

            if (conditionsAreMet(false)) {
                if (Gasket.isTradingUser() && !patternZeroString.endsWith(TEST.toString())) {
                    double index = (double) Integer.parseInt(giveData(SELL, patternZeroString))
                            / Integer.parseInt(giveData(BUY, patternZeroString));

                    if (index >= Gasket.getIndexRatioTransactionsAtWhichEnterMarket() && doNotDoDeal) {
                        new TradeSell(stringOut);
                    }

                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                            + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                            + " сделал сделку СЕЛЛ USER");

                }

                if (Gasket.isTradingTestUser()) {
                    new TestOrderSellPatternPredictor(doNotDoDeal, stringOut, Gasket.getBitmexQuote().getBidPrice());

                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                            + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                            + " сделал сделку СЕЛЛ USER - TEST");
                }

            } else {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                        + " сделку СЕЛЛ USER отменил по истечению времени");
            }
        }
    }



    // BUY===1===SELL===0===AVERAGE===3.28===MAX===5.0===SIZE===220===BLOCK===1===TYPE===ASK===ID===4
    // тут мы находим цену выше которой надо подняться или опустится в течении определенного времени
    // что бы сделать сделку иначе отбой
    private boolean conditionsAreMet(boolean b) {
        long timeStop = 60 * Gasket.getTimeStopLiveForUserPatterns();
        int blockSearch = 1;
        double prices = 0.0;
        String types = "";
        int blocks = 0;
        long time = 0;

        if (!giveData(TypeData.BLOCK, patternZeroString).equalsIgnoreCase(NULL.toString())) {

            blocks = Integer.parseInt(giveData(TypeData.BLOCK, patternZeroString));
            types = giveData(TypeData.TYPE, patternZeroString);

            if (types.equalsIgnoreCase(NULL.toString())) {
                if (b) {
                    // если сделка бай и тайп null то сразу берем цену первой строки нужного блока
                    for (String string : marketList) {
                        if (string.startsWith(BIAS.toString())) {
                            blockSearch++;
                        }

                        if (blocks == blockSearch) {
                            prices = Double.parseDouble(giveData(TypeData.price,
                                    marketList.get(marketList.indexOf(string) + 1)));
                            break;
                        }

                    }
                } else {
                    // если сделка селл и тайп null то сразу берем цену последней строки нужного блока
                    for (String string : marketList) {
                        if (string.startsWith(BIAS.toString())) {
                            blockSearch++;
                        }

                        if (blocks + 1 == blockSearch) {
                            prices = Double.parseDouble(giveData(TypeData.price,
                                    marketList.get(marketList.indexOf(string) - 1)));
                            break;
                        }
                    }
                }
            } else {
                for (String string : marketList) {
                    if (!string.startsWith(BIAS.toString()) && !string.startsWith(BUY.toString())
                            && !string.startsWith(NULL.toString())) {

                        if (blocks == blockSearch) {
                            if (giveData(TypeData.type, string).equalsIgnoreCase(types)) {
                                prices = Double.parseDouble(giveData(TypeData.price, string));
                            }
                        }
                    } else if (string.startsWith(BIAS.toString())) {
                        blockSearch++;
                    }
                }
            }
        } else {
            prices = Gasket.getBitmexQuote().getAskPrice();
        }

        while (time < timeStop) {

            if (b) {
                if (Gasket.getBitmexQuote().getBidPrice() > prices) {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                            + " --- цена уровня - " + types + " - " + prices + " - пробита");
                    return true;
                }
            } else {
                if (Gasket.getBitmexQuote().getAskPrice() < prices) {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                            + " --- цена уровня - " + types + " - " + prices + " - пробита");
                    return true;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeERROR(Arrays.toString(e.getStackTrace()));
            }

            time++;
        }
        return false;
    }
}

