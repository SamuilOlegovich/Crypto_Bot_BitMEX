package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.*;
import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.view.ConsoleHelper;

import java.util.ArrayList;
import java.util.Arrays;

import static bitmex.Bot.view.ConsoleHelper.writeMessage;
import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.*;




public class MakeDealMartingale extends Thread {
    private ArrayList<String> marketList;
    private String patternZeroString;
    private Martingale martingale;
    private String types;

    public MakeDealMartingale(ArrayList<String> marketList, String patternZeroString) {
        this.types = StringHelper.giveData(TYPE, patternZeroString);
        this.martingale = Gasket.getMartingaleClass();
        this.marketList = new ArrayList<>(marketList);
        this.patternZeroString = patternZeroString;
        start();
    }



    @Override
    public void run() {
        writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Определяю какую сделку сделать согласно ПАТТЕРНАМ USER " + MARTINGALE.toString());

        String stringOut = patternZeroString;


        if (Integer.parseInt(giveData(BUY, patternZeroString))
                > Integer.parseInt(giveData(SELL, patternZeroString))) {

            if (conditionsAreMet(true)) {
                if (Gasket.isTradingMartingale() && !patternZeroString.endsWith(TEST.toString())) {

                    double index = (double) Math.abs(Integer.parseInt(giveData(BUY, patternZeroString)))
                            / Math.abs(Integer.parseInt(giveData(SELL, patternZeroString)));

                    if (index >= Gasket.getIndexRatioTransactionsAtWhichEnterMarket()) {
                        martingale.upSteep(types);
                        new TradeBuy(stringOut);
                    }

                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                            + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                            + " сделал сделку БАЙ USER "  + MARTINGALE.toString() + " - REAL");
                }

                if (Gasket.isTradingTestMartingale()) {
                    if (Gasket.getMartingaleMaxSteep() >= martingale.getSteep(types)) {
                        martingale.upSteep(types);
                        new TestOrderBuyPatternMartingale(stringOut, Gasket.getBitmexQuote().getAskPrice());

                        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                                + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                                + " сделал сделку БАЙ USER - TEST " + MARTINGALE.toString());
                    } else {
                        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                                + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                                + " сделку БАЙ USER " + MARTINGALE.toString()
                                + "ОТМЕНИЛ --- перевышен МАКСИМАЛЬНЫЙ шаг - TEST");
                    }
                }

            } else {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                        + " сделку БАЙ USER " + MARTINGALE.toString() + "отменил по истечению времени");
            }

        } else if (Integer.parseInt(giveData(BUY, patternZeroString))
                < Integer.parseInt(giveData(SELL, patternZeroString))) {

            if (conditionsAreMet(false)) {
                if (Gasket.isTradingMartingale() && !patternZeroString.endsWith(TEST.toString())) {

                    double index = (double) Math.abs(Integer.parseInt(giveData(SELL, patternZeroString)))
                            / Math.abs(Integer.parseInt(giveData(BUY, patternZeroString)));

                    if (index >= Gasket.getIndexRatioTransactionsAtWhichEnterMarket()) {
                        martingale.upSteep(types);
                        new TradeSell(stringOut);
                    }

                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                            + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                            + " сделал сделку СЕЛЛ USER " + MARTINGALE.toString() + " - REAL");

                }

                if (Gasket.isTradingTestMartingale()) {
                    if (Gasket.getMartingaleMaxSteep() >= martingale.getSteep(types)) {
                        martingale.upSteep(types);
                        new TestOrderSellPatternMartingale(stringOut, Gasket.getBitmexQuote().getBidPrice());

                        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                                + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                                + " сделал сделку СЕЛЛ USER - TEST " + MARTINGALE.toString());
                    } else {
                        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                                + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                                + " сделку БАЙ USER " + MARTINGALE.toString()
                                + "ОТМЕНИЛ --- перевышен МАКСИМАЛЬНЫЙ шаг - TEST");
                    }
                }

            } else {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + stringOut + " --- Согластно ПАТТЕРНУ " + giveData(ID, patternZeroString)
                        + " сделку СЕЛЛ USER " + MARTINGALE.toString() + "отменил по истечению времени");
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
                            + " --- цена уровня - " + types + " - " + prices
                            + " - пробита - " + MARTINGALE.toString());
                    return true;
                }
            } else {
                if (Gasket.getBitmexQuote().getAskPrice() < prices) {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                            + " --- цена уровня - " + types + " - " + prices
                            + " - пробита - " + MARTINGALE.toString());
                    return true;
                }
            }

            try {
                Thread.sleep(Gasket.getSECOND());
            } catch (InterruptedException e) {
                ConsoleHelper.writeERROR(Arrays.toString(e.getStackTrace()));
            }

            time++;
        }
        return false;
    }
}

