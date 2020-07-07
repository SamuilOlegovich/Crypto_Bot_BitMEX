package bitmex.Bot.model.strategies.iiPro;



import bitmex.Bot.model.CompareHelper;

import java.util.ArrayList;

import static bitmex.Bot.model.DatesTimes.getDateTerminal;
import static bitmex.Bot.model.Gasket.getLevelsToCompare;
import static bitmex.Bot.view.ConsoleHelper.writeMessage;
import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.*;




public class CompareAndMakeDecisionPro extends Thread {

    private ArrayList<String> patternsList;
    private ArrayList<String> marketList;
    private String[] levelsToCompare;



    public CompareAndMakeDecisionPro(ArrayList<String> marketList, ArrayList<String> patternsList) {
        this.levelsToCompare = getLevelsToCompare().split("-");
        this.patternsList = new ArrayList<>(patternsList);
        this.marketList = new ArrayList<>(marketList);
        start();
    }



    @Override
    public void run() {
        if (compareSheets()) {
            writeMessage(getDateTerminal() + " --- "
                    + "Нашел совпадения в рынке с ПАТТЕРНАМИ II Pro передаю на сделку");
            new MakeDealPro(patternsList.get(0));
        }

        patternsList.clear();
        marketList.clear();
    }



    // разбиваем листы на блоки и на строки биас и сравниваем
    private boolean compareSheets() {
        ArrayList<String> patternStrings = new ArrayList<>();
        ArrayList<String> marketStrings = new ArrayList<>();
        ArrayList<String> patternBias = new ArrayList<>();
        ArrayList<String> marketBias = new ArrayList<>();
        int maxBias = 0;


        // находим все BIAS в маркет листе
        for (String string : marketList) {
            if (string.startsWith(BIAS.toString())) {
                marketBias.add(string);
            }
        }

        // находим все BIAS в паттерн листе
        for (String string : patternsList) {
            if (string.startsWith(BIAS.toString())) {
                patternBias.add(string);
            }
        }

        // сравниваем размеры BIAS листов, если их размер не равен, то авттерны не равны
        // если их размер равен то проверяем равны ли в них направления
        if (marketBias.size() != patternBias.size()) {
            return false;
        } else if (marketBias.size() > 0){
            for (String marketString : marketBias) {
                String patternString = patternBias.get(marketBias.indexOf(marketString));

                if (!giveData(BIAS, marketString).equals(giveData(BIAS, patternString))) {
                    return false;
                }
            }
        }

        maxBias = marketBias.size() + 1;

        // формируем блоки строк находящиеся между BIAS и нулевой строкой
        for (int i = 1; i <= (maxBias > 0 ? maxBias : 1); i++) {
            int bias = 0;

            for (String string : marketList) {
                if (string.startsWith(BIAS.toString())) {
                    bias++;
                }

                if (!string.startsWith(BIAS.toString()) && !string.startsWith(NULL.toString())
                        && !string.startsWith(BUY.toString()) && bias == i - 1) {
                    marketStrings.add(string);
                }
            }

            bias = 0;

            for (String string : patternsList) {
                if (string.startsWith(BIAS.toString())) {
                    bias++;
                }

                if (!string.startsWith(BIAS.toString()) && !string.startsWith(NULL.toString())
                        && !string.startsWith(BUY.toString()) && bias == i - 1) {
                    patternStrings.add(string);
                }
            }

            // отправляем полученые блоки на дальнейшую проверку
            if (!removeAllUnnecessaryAndCheckForAMatch(marketStrings, patternStrings)) {
                return false;
            }
        }
        return true;
    }



    // сортируем, удаляем лишнее и сравниваем переданые блоки строк
    private boolean removeAllUnnecessaryAndCheckForAMatch(ArrayList<String> marketStrings,
                                                          ArrayList<String> patternStrings) {

        if (patternStrings != null && patternStrings.size() > 0
                && marketStrings != null && marketStrings.size() > 0) {

            // сравниваем размеры блоков
            if (marketStrings.size() != patternStrings.size()) {
                return false;
            } else {
                ArrayList<String> patternCompare = new ArrayList<>();
                ArrayList<String> marketCompare = new ArrayList<>();
                ArrayList<String> patternAll = new ArrayList<>();
                ArrayList<String> marketAll = new ArrayList<>();


                // начинаем перебирать блоки строк и сравнивать их друг с другом
                for (String stringMarket : marketStrings) {
                    String stringPattern = patternStrings.get(marketStrings.indexOf(stringMarket));
                    boolean flag = true;

                    for (String string : levelsToCompare) {
                        if (giveData(type, stringMarket).equals(giveData(type, stringPattern))
                                && giveData(type, stringMarket).equals(string)) {
                            patternCompare.add(stringPattern);
                            marketCompare.add(stringMarket);
                            flag = false;
                            break;
                        }
                    }

                    if (flag) {
                        patternAll.add(stringPattern);
                        marketAll.add(stringMarket);
                    }

                }

                patternAll.sort(CompareHelper.getSortTheAlphabet());
                marketAll.sort(CompareHelper.getSortTheAlphabet());

                for (String pattern : patternCompare) {
                    String market = marketCompare.get(patternCompare.indexOf(pattern));
                    if (!finallyComparisonOnAllData(market, pattern)) {
                        return false;
                    }
                }

                for (String pattern : patternAll) {
                    String market = marketAll.get(patternAll.indexOf(pattern));
                    if (!finallyComparisonOnAllData(market, pattern)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }



    private boolean finallyComparisonOnAllData(String marketString, String patternString) {

        writeMessage(getDateTerminal() + " --- "
                + "---------------------------------------------------------------------------- 4 ii Pro");


        if (!giveData(type, marketString).equals(giveData(type, patternString))) {

            writeMessage(getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-3 ii Pro");

            return false;
        }

        // направление свечи сравниваем только на избранных уровнях, на остальных это не важно
        for (String string : levelsToCompare) {
            if (string.equals(giveData(type, marketString)) && string.equals(giveData(type, patternString))) {
                if (!giveData(dir, marketString).equals(giveData(dir, patternString))) {

                    writeMessage(getDateTerminal() + " --- "
                            + "---------------------------------------------------------------------------- 4-4 ii Pro");

                    return false;
                }
            }
        }

        writeMessage(getDateTerminal() + " --- "
                + "---------------------------------------------------------------------------- 4-5 ii Pro");

        return true;
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

