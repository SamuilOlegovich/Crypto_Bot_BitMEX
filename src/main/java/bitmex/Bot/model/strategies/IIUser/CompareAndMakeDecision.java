package bitmex.Bot.model.strategies.IIUser;


import bitmex.Bot.model.strategies.oneStrategies.TradeSell;
import bitmex.Bot.model.strategies.oneStrategies.TradeBuy;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.util.ArrayList;
import java.util.HashSet;


// сравниваю и принимаю решение
public class CompareAndMakeDecision extends Thread {
    private ArrayList<String> thisArrayListStrings;
    private ArrayList<String> inArrayListStrings;



    public CompareAndMakeDecision(ArrayList<String> thisArrayListStrings, ArrayList<String> inArrayListStrings) {
        this.thisArrayListStrings = thisArrayListStrings;
        this.inArrayListStrings = inArrayListStrings;
        start();
    }



    @Override
    public void run() {
        if (compareSheets()) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Нашел совпадения в рынке с ПАТТЕРНАМИ User передаю на сделку");
            makeDeal(inArrayListStrings.get(0));
        }

        thisArrayListStrings.clear();
        inArrayListStrings.clear();
    }



    // разбиваем листы на блоки и на строки биас и сравниваем
    private synchronized boolean compareSheets() {
        ArrayList<String> thisBiasStrings = new ArrayList<>();
        ArrayList<String> inBiasStrings = new ArrayList<>();
        ArrayList<String> thisStrings = new ArrayList<>();
        ArrayList<String> inStrings = new ArrayList<>();
        int maxBias = 0;


        for (String string : thisArrayListStrings) {
            if (!string.startsWith("0") && string.startsWith("BIAS") && !string.startsWith("BUY")) {
                thisBiasStrings.add(string);
            }
        }

        for (String string : inArrayListStrings) {
            if (!string.startsWith("0") && string.startsWith("BIAS") && !string.startsWith("BUY")) {
                inBiasStrings.add(string);
            }
        }

        if (thisBiasStrings.size() != inBiasStrings.size()) {
            return false;
        } else if (thisArrayListStrings.size() > 0){
            for (String thisString : thisBiasStrings) {
                String[] stringsIn = inBiasStrings.get(thisStrings.indexOf(thisString)).split("===");
                String[] stringsThis = thisString.split("===");

                if (!stringsIn[1].equalsIgnoreCase(stringsThis[1])) {
                    return false;
                }
            }
        }

        maxBias = thisBiasStrings.size();
        thisArrayListStrings.clear();
        inArrayListStrings.clear();

        for (int i = 1; i <= (maxBias > 0 ? maxBias : 1); i++) {
            int bias = 0;

            for (String string : thisArrayListStrings) {
                if (!string.startsWith("0") && string.startsWith("BIAS") && !string.startsWith("BUY")) {
                    bias++;
                }

                if (!string.startsWith("0") && !string.startsWith("BIAS") && !string.startsWith("BUY")
                        && bias == i - 1) {
                    thisStrings.add(string);
                }
            }

            bias = 0;

            for (String string : inArrayListStrings) {
                if (!string.startsWith("0") && string.startsWith("BIAS") && !string.startsWith("BUY")) {
                    bias++;
                }

                if (!string.startsWith("0") && !string.startsWith("BIAS") && !string.startsWith("BUY")
                        && bias == i - 1) {
                    inStrings.add(string);
                }
            }

            if (!removeAllUnnecessaryAndCheckForAMatch(thisStrings, inStrings)) {
                return false;
            }
        }
        return true;
    }


    // сортируем, удаляем лишнее и сравниваем переданые блоки строк
    private boolean removeAllUnnecessaryAndCheckForAMatch(ArrayList<String> thisStrings, ArrayList<String> inStrings) {
        ArrayList<String> in = new ArrayList<>();

        if (inStrings != null && inStrings.size() > 0 && thisStrings != null && thisStrings.size() > 0) {
            for (String sIn : inStrings) {
                String[] stringsIn = sIn.split("===");

                for (String sThis : thisStrings) {
                    String[] stringsThis = sThis.split("===");

                    if (stringsIn[11].equals(stringsThis[11])) {
                        in.add(sIn);
                    }
                }
            }

            if (in.size() != thisStrings.size()) {
                return false;
            } else {
                ArrayList<String> thisCompare = new ArrayList<>();
                ArrayList<String> inCompare = new ArrayList<>();
                ArrayList<String> thisCompareAll = new ArrayList<>();
                ArrayList<String> inCompareAll = new ArrayList<>();


                for (String sIn : in) {
                    String[] stringsIn = sIn.split("===");
                    String[] stringsThis = thisStrings.get(in.indexOf(sIn)).split("===");

                    if (in.indexOf(sIn) != in.size() - 1) {
                        String[] stringsIn2 = in.get(in.indexOf(sIn) + 1).split("===");
                        String[] stringsThis2 = thisStrings.get(in.indexOf(sIn) + 1).split("===");

                        if (stringsIn[7].equals(stringsIn2[7]) && stringsThis[7].equals(stringsThis2[7])) {
                            thisCompare.add(stringsThis2[11]);
                            thisCompare.add(stringsThis[11]);
                            inCompare.add(stringsIn2[11]);
                            inCompare.add(stringsIn[11]);

                            inCompareAll.add(thisStrings.get(in.indexOf(sIn) + 1));
                            inCompareAll.add(thisStrings.get(in.indexOf(sIn)));
                            thisCompareAll.add(in.get(in.indexOf(sIn) + 1));
                            thisCompareAll.add(sIn);
                        } else if (!stringsIn[7].equals(stringsIn2[7]) && !stringsThis[7].equals(stringsThis2[7])) {


                            if (!finallyComparisonOnAllData(thisStrings.get(in.indexOf(sIn)), sIn)) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        if (!finallyComparisonOnAllData(thisStrings.get(in.indexOf(sIn)), sIn)) {
                            return false;
                        }
                    }
                }

                if (thisCompare.size() > 0) {
                    HashSet<String> hashSetThis = new HashSet<>(thisCompare);
                    HashSet<String> hashSetIn = new HashSet<>(inCompare);

                    thisCompare.clear();
                    inCompare.clear();

                    thisCompare.addAll(hashSetThis);
                    inCompare.addAll(hashSetIn);

                    for (String string : thisCompare) {
                        if (!string.equals(inCompare.get(thisCompare.indexOf(string)))) {
                            return false;
                        }
                    }

                    hashSetThis.clear();
                    hashSetIn.clear();

                    hashSetThis.addAll(thisCompareAll);
                    hashSetIn.addAll(inCompareAll);

                    thisCompareAll.clear();
                    inCompareAll.clear();

                    thisCompareAll.addAll(hashSetThis);
                    inCompareAll.addAll(hashSetIn);

                    hashSetThis.clear();
                    hashSetIn.clear();

                    for (String stringIn : inCompareAll) {
                        if (!finallyComparisonOnAllData(thisCompareAll.get(inCompareAll.indexOf(stringIn)),
                                stringIn)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }



    private boolean finallyComparisonOnAllData(String thisArray, String inArray) {
        String[] stringsThis = thisArray.split("===");
        String[] stringsIn = inArray.split("===");

        if (!stringsThis[1].equalsIgnoreCase("null") && !stringsThis[1].equals(stringsIn[1])) {
            return false;
        } else if (!stringsThis[3].equalsIgnoreCase("null") && !stringsThis[3].equals(stringsIn[3])) {
            return false;
        } else if (!stringsThis[11].equalsIgnoreCase("null") && !stringsThis[11].equals(stringsIn[11])) {
            return false;
        } else if (!stringsThis[13].equalsIgnoreCase("null") && !stringsThis[13].equals(stringsIn[13])) {
            return false;
        } else if (!stringsThis[15].equalsIgnoreCase("null") && !stringsThis[15].equals(stringsIn[15])) {
            return false;
        }
        return true;
    }



    // Определяем какую сделку сделать и даем команду на ее исполнение
    private synchronized void makeDeal(String stringIn) {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Определяю какую сделку сделать согласно ИНФО ПАТТЕРНАМ");
        String[] strings = stringIn.split("===");
        String stringOut = stringIn;


        if (Integer.parseInt(strings[1]) > Integer.parseInt(strings[3])) {

            if (Gasket.isTrading()) new TradeBuy(stringOut);
            new TestOrderBuyPatternUser(stringOut, Gasket.getBitmexQuote().getAskPrice());

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + stringOut + " --- Согластно ПАТТЕРНУ сделал сделку БАЙ USER");

        } else if (Integer.parseInt(strings[1]) < Integer.parseInt(strings[3])) {

            if (Gasket.isTrading()) new TradeSell(stringOut);
            new TestOrderSellPatternUser(stringOut, Gasket.getBitmexQuote().getBidPrice());

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + stringOut + " --- Согластно ПАТТЕРНУ сделал сделку СЕЛЛ USER");

        }
    }
}

//    0 {"period": "M5",
//    1 "preview": "1",
//    2 "time": "2020-05-27 12:28:00",
//    3 "price": "9175.0",
//    4 "value": "2920763",
//    5 "type": "ASK",
//    6 "avg": "2871888",
//    7 "dir": "1",
//    8 "open": "9167.5",
//    9 "close": "9178.5",
//    10 "high": "9183.0",
//    11 "low": "9167.0"}
//
//
//    0 period
//    1 period.toString()
//    2 ===preview=== +
//    3 preview +
//    4 "===time===" +
//    5 dateFormat.format(time)
//    6 "===price===" +
//    7 price
//    8 "===value===" +
//    9 value +
//    10 "===type===" +
//    11 type.toString() +
//    12 "===avg===" +
//    13 avg
//    14 "===dir===" +
//    15 dir + "
//    16 ===open===" +
//    17 open + "
//    18 ===close===" +
//    19 close + "
//    20 ===high===" +
//    21 high
//    22 ===low===" +
//    23 low
