package bitmex.Bot.model.strategies.II;


import bitmex.Bot.model.StringHelper;

import java.util.ArrayList;
import java.util.TreeSet;

import static bitmex.Bot.model.CompareHelper.getSortTheAlphabet;
import static bitmex.Bot.model.DatesTimes.getDateTerminal;
import static bitmex.Bot.view.ConsoleHelper.writeMessage;
import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.dir;
import static bitmex.Bot.model.enums.TypeData.*;




public class CompareAndMakeDecision extends Thread {

    private ArrayList<String> patternsList;
    private ArrayList<String> marketList;



    public CompareAndMakeDecision(ArrayList<String> marketList, ArrayList<String> patternsList) {
        this.patternsList = new ArrayList<>(patternsList);
        this.marketList = new ArrayList<>(marketList);
        start();
    }



    @Override
    public void run() {
        if (compareSheets()) {
            writeMessage(getDateTerminal() + " --- "
                    + "Нашел совпадения в рынке с ПАТТЕРНАМИ II передаю на сделку--- ID-"
                    + StringHelper.giveData(ID, patternsList.get(0)));
            new MakeDeal(patternsList.get(0));
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
                ArrayList<String> patternCompareAll = new ArrayList<>();
                ArrayList<String> marketCompareAll = new ArrayList<>();
                ArrayList<String> patternCompare = new ArrayList<>();
                ArrayList<String> marketCompare = new ArrayList<>();


                // начинаем перебирать блоки строк и сравнивать их друг с другом
                for (String stringMarket : marketStrings) {
                    String stringPattern = patternStrings.get(marketStrings.indexOf(stringMarket));

                    // если это не последняя строка в блоке то заглядываем на строку вперед
                    // и смотрим не ровны ли цены между ними
                    if (marketStrings.indexOf(stringMarket) != marketStrings.size() - 1) {
                        String stringPattern2 = patternStrings.get(marketStrings.indexOf(stringMarket) + 1);
                        String stringMarket2 = marketStrings.get(marketStrings.indexOf(stringMarket) + 1);

                        // если цены на шаг вперед во всех блоках ровны до добавляем в списки
                        if (giveData(price, stringPattern).equals(giveData(price, stringPattern2))
                                && giveData(price, stringMarket).equals(giveData(price, stringMarket2))) {

                            patternCompare.add(giveData(type, stringPattern2));
                            patternCompare.add(giveData(type, stringPattern));
                            marketCompare.add(giveData(type, stringMarket2));
                            marketCompare.add(giveData(type, stringMarket));

                            patternCompareAll.add(stringPattern2);
                            patternCompareAll.add(stringPattern);
                            marketCompareAll.add(stringMarket2);
                            marketCompareAll.add(stringMarket);

                            // если цены на шаг вперед во всех блоках не ровны то добавляем в списки
                        } else if (!giveData(price, stringPattern).equals(giveData(price, stringPattern2))
                                && !giveData(price, stringMarket).equals(giveData(price, stringMarket2))) {

                            // отсылаем непосредственно строки на последнее сравнение между собой
                            if (!finallyComparisonOnAllData(stringMarket, stringPattern)) {
                                return false;
                            }

                        } else {
                            return false;
                        }
                    } else {
                        if (!finallyComparisonOnAllData(stringMarket, stringPattern)) {
                            return false;
                        }
                    }
                }

                // если блоки одинаковы напонены сравниваем их
                if (marketCompare.size() > 0) {
                    // вначале сортируем простые блоки и сравниваем их
                    // если они ровны то можно сравнивать дальше построчно
                    TreeSet<String> setPattern = new TreeSet<>(patternCompare);
                    TreeSet<String> setMarket = new TreeSet<>(marketCompare);

                    patternCompare.clear();
                    marketCompare.clear();

                    patternCompare.addAll(setPattern);
                    marketCompare.addAll(setMarket);

                    for (String string : marketCompare) {
                        if (!string.equals(patternCompare.get(marketCompare.indexOf(string)))) {
                            return false;
                        }
                    }

                    setPattern.clear();
                    setMarket.clear();

                    patternCompareAll.sort(getSortTheAlphabet());
                    marketCompareAll.sort(getSortTheAlphabet());


                    for (String stringPattern : patternCompareAll) {
                        if (!finallyComparisonOnAllData(marketCompareAll.get(patternCompareAll.indexOf(stringPattern)),
                                stringPattern)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }



    private boolean finallyComparisonOnAllData(String marketString, String patternString) {

        if (!giveData(type, marketString).equals(giveData(type, patternString))) {
            return false;
        }

        if (!giveData(dir, marketString).equals(giveData(dir, patternString))) {
            return false;
        }

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

