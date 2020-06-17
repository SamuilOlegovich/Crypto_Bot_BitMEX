package bitmex.Bot.model.strategies.IIUser;


import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;

import java.util.ArrayList;
import java.util.HashSet;


// сравниваю и принимаю решение
public class CompareAndMakeDecisionUser extends Thread {
    private ArrayList<String> patternsList;
    private ArrayList<String> marketList;



    public CompareAndMakeDecisionUser(ArrayList<String> marketList, ArrayList<String> patternsList) {
        this.patternsList = new ArrayList<>(patternsList);
        this.marketList = new ArrayList<>(marketList);
        start();
    }



    @Override
    public void run() {
        if (compareSheets()) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Нашел совпадения в рынке с ПАТТЕРНАМИ User передаю на сделку");
            new MakeDealUser(marketList, patternsList.get(0));
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
            if (string.startsWith("BIAS")) {
                marketBias.add(string);
            }
        }

        // находим все BIAS в паттерн листе
        for (String string : patternsList) {
            if (string.startsWith("BIAS")) {
                patternBias.add(string);
            }
        }

        // сравниваем размеры BIAS листов, если их размер не равен, то авттерны не равны
        // если их размер равен то проверяем равны ли в них направления
        if (marketBias.size() != patternBias.size()) {
            return false;
        } else if (marketBias.size() > 0){
            for (String marketString : marketBias) {
                String[] stringsPattern = patternBias.get(marketBias.indexOf(marketString)).split("===");
                String[] stringsMarket = marketString.split("===");

                if (!stringsPattern[1].equalsIgnoreCase(stringsMarket[1])) {
                    return false;
                }
            }
        }

        maxBias = marketBias.size();

        // формируем блоки строк находящиеся между BIAS и нулевой строкой
        for (int i = 1; i <= (maxBias > 0 ? maxBias : 1); i++) {
            int bias = 0;

            for (String string : marketList) {
                if (string.startsWith("BIAS")) {
                    bias++;
                }

                if (!string.startsWith("0") && !string.startsWith("BIAS") && !string.startsWith("BUY")
                        && bias == i - 1) {
                    marketStrings.add(string);
                }
            }

            bias = 0;

            for (String string : patternsList) {
                if (string.startsWith("BIAS")) {
                    bias++;
                }

                if (!string.startsWith("0") && !string.startsWith("BIAS") && !string.startsWith("BUY")
                        && bias == i - 1) {
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
        ArrayList<String> readyMarketBlock = new ArrayList<>();

        if (patternStrings != null && patternStrings.size() > 0 && marketStrings != null && marketStrings.size() > 0) {

            // тут приводим в порядок маркет блок, все лишнее из него убираем
            for (String patternString : patternStrings) {
                String[] stringsPattern = patternString.split("===");

                for (String marketString : marketStrings) {
                    String[] stringsMarket = marketString.split("===");

                    if (stringsPattern[11].equals(stringsMarket[11])) {
                        readyMarketBlock.add(marketString);
                    }
                }
            }

            // сравниваем размеры блоков
            if (readyMarketBlock.size() != patternStrings.size()) {
                return false;
            } else {
                ArrayList<String> patternCompareAll = new ArrayList<>();
                ArrayList<String> marketCompareAll = new ArrayList<>();
                ArrayList<String> patternCompare = new ArrayList<>();
                ArrayList<String> marketCompare = new ArrayList<>();


                // начинаем перебирать блоки строк и сравнивать их друг с другом
                for (String sMarket : readyMarketBlock) {
                    String[] stringsPattern = patternStrings.get(readyMarketBlock.indexOf(sMarket)).split("===");
                    String[] stringsMarket = sMarket.split("===");

                    // если это не последняя строка в блоке то заглядываем на строку вперед
                    // и смотрим не ровны ли цены между ними
                    if (readyMarketBlock.indexOf(sMarket) != readyMarketBlock.size() - 1) {
                        String[] stringsMarket2 = readyMarketBlock.get(readyMarketBlock.indexOf(sMarket) + 1)
                                .split("===");
                        String[] stringsPattern2 = patternStrings.get(readyMarketBlock.indexOf(sMarket) + 1)
                                .split("===");

                        // если цены на шаг вперед во всех блоках ровны до добавляем в списки
                        if (stringsPattern[7].equals(stringsPattern2[7])
                                && stringsMarket[7].equals(stringsMarket2[7])) {

                            patternCompare.add(stringsPattern2[11]);
                            patternCompare.add(stringsPattern[11]);
                            marketCompare.add(stringsMarket2[11]);
                            marketCompare.add(stringsMarket[11]);

                            marketCompareAll.add(readyMarketBlock.get(readyMarketBlock.indexOf(sMarket) + 1));
                            patternCompareAll.add(patternStrings.get(readyMarketBlock.indexOf(sMarket) + 1));
                            patternCompareAll.add(patternStrings.get(readyMarketBlock.indexOf(sMarket)));
                            marketCompareAll.add(sMarket);

                            // если цены на шаг вперед во всех блоках не ровны до добавляем в списки
                        } else if (!stringsPattern[7].equals(stringsPattern2[7])
                                && !stringsMarket[7].equals(stringsMarket2[7])) {

                            // отсылаем непосредственно строки на последнее сравнение между собой
                            if (!finallyComparisonOnAllData(sMarket, patternStrings.get(readyMarketBlock
                                    .indexOf(sMarket)))) {
                                return false;
                            }

                        } else {
                            return false;
                        }
                    } else {
                        if (!finallyComparisonOnAllData(sMarket, patternStrings.get(readyMarketBlock
                                .indexOf(sMarket)))) {
                            return false;
                        }
                    }
                }

                // если блоки одинаковых ен напонены сравниваем их
                if (marketCompare.size() > 0) {
                    // вначале сортируем простые блоки и сравниваем их
                    // если они ровны то можно сравнивать дальше построчно
                    HashSet<String> hashSetPattern = new HashSet<>(patternCompare);
                    HashSet<String> hashSetMarket = new HashSet<>(marketCompare);

                    patternCompare.clear();
                    marketCompare.clear();

                    patternCompare.addAll(hashSetPattern);
                    marketCompare.addAll(hashSetMarket);

                    for (String string : marketCompare) {
                        if (!string.equals(patternCompare.get(marketCompare.indexOf(string)))) {
                            return false;
                        }
                    }

                    hashSetPattern.clear();
                    hashSetMarket.clear();

                    hashSetPattern.addAll(patternCompareAll);
                    hashSetMarket.addAll(marketCompareAll);

                    patternCompareAll.clear();
                    marketCompareAll.clear();

                    patternCompareAll.addAll(hashSetPattern);
                    marketCompareAll.addAll(hashSetMarket);

                    hashSetPattern.clear();
                    hashSetMarket.clear();

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



    private boolean finallyComparisonOnAllData(String marketArray, String patternArray) {
        String[] stringsPattern = patternArray.split("===");
        String[] stringsMarket = marketArray.split("===");

        // period
        if (!stringsPattern[1].equalsIgnoreCase("null")
                && !stringsMarket[1].equals(stringsPattern[1])) {
            return false;
            // preview
        } else if (!stringsPattern[3].equalsIgnoreCase("null")
                && !stringsMarket[3].equals(stringsPattern[3])) {
            return false;
            // type
        } else if (!stringsPattern[11].equalsIgnoreCase("null")
                && !stringsMarket[11].equals(stringsPattern[11])) {
            return false;
            // avg
        } else if (!stringsPattern[13].equalsIgnoreCase("null")
                && !stringsMarket[13].equals(stringsPattern[13])) {
            return false;
            // dir
        } else if (!stringsPattern[15].equalsIgnoreCase("null")
                && !stringsMarket[15].equals(stringsPattern[15])) {
            return false;
        } else {
            return true;
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
