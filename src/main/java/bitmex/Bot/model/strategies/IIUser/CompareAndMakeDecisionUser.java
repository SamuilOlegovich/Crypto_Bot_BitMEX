package bitmex.Bot.model.strategies.IIUser;


import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;


// сравниваю и принимаю решение
public class CompareAndMakeDecisionUser extends Thread {
    private SortTheAlphabet sortTheAlphabet;

    private ArrayList<String> patternsList;
    private ArrayList<String> marketList;




    public CompareAndMakeDecisionUser(ArrayList<String> marketList, ArrayList<String> patternsList) {
        this.patternsList = new ArrayList<>(patternsList);
        this.marketList = new ArrayList<>(marketList);
        this.sortTheAlphabet = new SortTheAlphabet();
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
        } else if (marketBias.size() > 0) {
            for (String marketString : marketBias) {
                if (!StringHelper.giveData(TypeData.BIAS.toString(),
                        patternBias.get(marketBias.indexOf(marketString)))
                        .equalsIgnoreCase(StringHelper.giveData(TypeData.BIAS.toString(), marketString))) {
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

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "---------------------------------------------------------------------------- 2-true");

        return true;
    }



    // сортируем, удаляем лишнее и сравниваем переданые блоки строк
    private boolean removeAllUnnecessaryAndCheckForAMatch(ArrayList<String> inMarketStrings,
                                                          ArrayList<String> inPatternStrings) {

        if (inPatternStrings != null && inPatternStrings.size() > 0
                && inMarketStrings != null && inMarketStrings.size() > 0) {

            ArrayList<String> patternStrings = new ArrayList<>(inPatternStrings);
            ArrayList<String> readyMarketBlock = new ArrayList<>();


            // тут приводим в порядок маркет блок, все лишнее из него убираем
            for (String patternString : patternStrings) {
                for (String marketString : inMarketStrings) {
                    if (StringHelper.giveData(TypeData.type.toString(), patternString)
                            .equals(StringHelper.giveData(TypeData.type.toString(), marketString))) {
                        readyMarketBlock.add(marketString);
                    }
                }
            }

            // сравниваем размеры блоков
            if (readyMarketBlock.size() != patternStrings.size()) {
                return false;
            } else {
                ArrayList<String> patternComparePriceNullAll = new ArrayList<>();
                ArrayList<String> marketComparePriceNullAll = new ArrayList<>();
                ArrayList<String> patternComparePriceNull = new ArrayList<>();
                ArrayList<String> marketComparePriceNull = new ArrayList<>();

                ArrayList<String> patternCompareDataNullAll = new ArrayList<>();
                ArrayList<String> marketCompareDataNullAll = new ArrayList<>();
                ArrayList<String> patternCompareDataNull = new ArrayList<>();
                ArrayList<String> marketCompareDataNull = new ArrayList<>();


                // начинаем перебирать блоки строк и сравнивать их друг с другом
                for (String sMarket : readyMarketBlock) {

                    String stringPattern = patternStrings.get(readyMarketBlock.indexOf(sMarket));

                    String[] stringsPattern = stringPattern.split("===");
                    String[] stringsMarket = sMarket.split("===");

                    // price
                    if (stringsMarket[7].equalsIgnoreCase("NULL")) {
                        // если прайс нул - то не важно в какой последовательности идут уровни,
                        // но цена у уровней в списке маркет должна быть ровна

                        if (readyMarketBlock.indexOf(sMarket) != readyMarketBlock.size() - 1) {
                            // если это не последняя строка в блоке то заглядываем на строку вперед
                            // и смотрим не ровны ли цены между ними

                            String marketPlusOne = readyMarketBlock.get(readyMarketBlock.indexOf(sMarket) + 1);
                            String patternPlusOne = patternStrings.get(readyMarketBlock.indexOf(sMarket) + 1);

                            String[] stringsPattern2 = patternPlusOne.split("===");
                            String[] stringsMarket2 = marketPlusOne.split("===");

                            // если цены на шаг вперед ровны добавляем в списки
                            if (stringsPattern[7].equals(stringsPattern2[7])
                                    && stringsMarket[7].equals(stringsMarket2[7])) {

                                patternComparePriceNullAll.add(patternPlusOne);
                                patternComparePriceNullAll.add(stringPattern);
                                marketComparePriceNullAll.add(marketPlusOne);
                                marketComparePriceNullAll.add(sMarket);

                                patternComparePriceNull.add(stringsPattern2[11]);
                                patternComparePriceNull.add(stringsPattern[11]);
                                marketComparePriceNull.add(stringsMarket2[11]);
                                marketComparePriceNull.add(stringsMarket[11]);

                            }
                        }
                        // Data
                    } else if (stringsMarket[5].equalsIgnoreCase("NULL")) {
                        // если время нулл - то у маркета должны быть в этом диапазоне все свечи на одной свече,
                        // а именно время ровно, направление свечи дир тоже должно быть ровно

                        if (readyMarketBlock.indexOf(sMarket) != readyMarketBlock.size() - 1) {
                            // если это не последняя строка в блоке то заглядываем на строку вперед
                            // и смотрим не ровны ли даты между ними

                            String marketPlusOne = readyMarketBlock.get(readyMarketBlock.indexOf(sMarket) + 1);
                            String patternPlusOne = patternStrings.get(readyMarketBlock.indexOf(sMarket) + 1);

                            String[] stringsPattern2 = patternPlusOne.split("===");
                            String[] stringsMarket2 = marketPlusOne.split("===");

                            // если даты на шаг вперед ровны добавляем в списки
                            if (stringsPattern[5].equals(stringsPattern2[5])
                                    && stringsMarket[5].equals(stringsMarket2[5])
                                    && stringsPattern[15].equals(stringsPattern2[15])
                                    && stringsMarket[15].equals(stringsMarket2[15])
                                    && stringsPattern[15].equals(stringsMarket[15])) {

                                patternCompareDataNullAll.add(patternPlusOne);
                                patternCompareDataNullAll.add(stringPattern);
                                marketCompareDataNullAll.add(marketPlusOne);
                                marketCompareDataNullAll.add(sMarket);

                                patternCompareDataNull.add(stringsPattern2[11]);
                                patternCompareDataNull.add(stringsPattern[11]);
                                marketCompareDataNull.add(stringsMarket2[11]);
                                marketCompareDataNull.add(stringsMarket[11]);
                            }
                        }
                    }
                }

                // в этой части мы удаляем из входящих списков паттернов и маркеров строки которые были перенесены
                // в другие списки сравнений согласно указанным требованиям
                ArrayList<Integer> deleteIndex = new ArrayList<>();

                for (String stringNull : marketCompareDataNull) {
                    for (String stringReady : readyMarketBlock) {
                        if (StringHelper.giveData(TypeData.type.toString(), stringReady).equals(stringNull)) {
                           deleteIndex.add(readyMarketBlock.indexOf(stringReady));
                        }
                    }
                }

                for (String stringNull : marketComparePriceNull) {
                    for (String stringReady : readyMarketBlock) {
                        if (StringHelper.giveData(TypeData.type.toString(), stringReady).equals(stringNull)) {
                            deleteIndex.add(readyMarketBlock.indexOf(stringReady));
                        }
                    }
                }

                HashSet<Integer> deleteIndexHash = new HashSet<>(deleteIndex);
                deleteIndex.addAll(deleteIndexHash);
                Collections.reverse(deleteIndex);
                deleteIndexHash.clear();

                for (Integer index : deleteIndex) {
                    readyMarketBlock.remove((int) index);
                }

                deleteIndex.clear();

                for (String stringNull : patternCompareDataNull) {
                    for (String stringReady : patternStrings) {

                        if (StringHelper.giveData(TypeData.type.toString(), stringReady).equals(stringNull)) {
                            deleteIndex.add(patternStrings.indexOf(stringReady));
                        }
                    }
                }

                for (String stringNull : patternComparePriceNull) {
                    for (String stringReady : patternStrings) {

                        if (StringHelper.giveData(TypeData.type.toString(), stringReady).equals(stringNull)) {
                            deleteIndex.add(patternStrings.indexOf(stringReady));
                        }
                    }
                }

                deleteIndexHash.addAll(deleteIndex);
                deleteIndex.addAll(deleteIndexHash);
                Collections.reverse(deleteIndex);
                deleteIndexHash.clear();

                for (Integer index : deleteIndex) {
                    patternStrings.remove((int) index);
                }

                // если блоки одинаковы и больше нуля сравниваем их
                if (readyMarketBlock.size() == patternStrings.size() && patternStrings.size() > 0) {

                    // сортируем блоки и сравниваем их дальше построчно
                    readyMarketBlock.sort(sortTheAlphabet);
                    patternStrings.sort(sortTheAlphabet);

                    for (String stringReady : readyMarketBlock) {
                        if (!finallyComparisonOnAllData(stringReady,
                                patternStrings.get(readyMarketBlock.indexOf(stringReady)))) {
                            return false;
                        }
                    }

                } else if (readyMarketBlock.size() != patternStrings.size()) {
                    return false;
                }

                // если блоки одинаковы и больше нуля сравниваем их
                if (patternComparePriceNull.size() == marketComparePriceNull.size()
                        && marketComparePriceNull.size() > 0) {
                    // вначале сортируем простые блоки и сравниваем их
                    // если они ровны то можно сравнивать дальше построчно
                    HashSet<String> hashSetPatternNull = new HashSet<>(patternComparePriceNull);
                    HashSet<String> hashSetMarketNull = new HashSet<>(marketComparePriceNull);

                    patternComparePriceNull.clear();
                    marketComparePriceNull.clear();

                    patternComparePriceNull.addAll(hashSetPatternNull);
                    marketComparePriceNull.addAll(hashSetMarketNull);

                    for (String string : marketComparePriceNull) {
                        if (!string.equals(patternComparePriceNull.get(marketComparePriceNull.indexOf(string)))) {
                            return false;
                        }
                    }

                    hashSetPatternNull.clear();
                    hashSetMarketNull.clear();

                    patternComparePriceNullAll.sort(sortTheAlphabet);
                    marketComparePriceNullAll.sort(sortTheAlphabet);

                    for (String stringMarket : marketComparePriceNullAll) {
                        if (!finallyComparisonOnAllData(stringMarket,
                                patternComparePriceNullAll.get(marketComparePriceNullAll.indexOf(stringMarket)))) {
                            return false;
                        }
                    }
                } else if (patternComparePriceNull.size() != marketComparePriceNull.size()) {
                    return false;
                }

                // если блоки одинаковы и больше нуля сравниваем их
                if (patternCompareDataNull.size() == marketCompareDataNull.size()
                        && marketCompareDataNull.size() > 0) {
                    // вначале сортируем простые блоки и сравниваем их
                    // если они ровны то можно сравнивать дальше построчно
                    HashSet<String> hashSetPatternNull = new HashSet<>(patternCompareDataNull);
                    HashSet<String> hashSetMarketNull = new HashSet<>(marketCompareDataNull);

                    patternCompareDataNull.clear();
                    marketCompareDataNull.clear();

                    patternCompareDataNull.addAll(hashSetPatternNull);
                    marketCompareDataNull.addAll(hashSetMarketNull);

                    for (String string : marketCompareDataNull) {
                        if (!string.equals(patternCompareDataNull.get(marketCompareDataNull.indexOf(string)))) {
                            return false;
                        }
                    }

                    hashSetPatternNull.clear();
                    hashSetMarketNull.clear();

                    patternCompareDataNullAll.sort(sortTheAlphabet);
                    marketCompareDataNullAll.sort(sortTheAlphabet);

                    for (String stringMarket : marketCompareDataNullAll) {
                        if (!finallyComparisonOnAllData(stringMarket,
                                patternCompareDataNullAll.get(marketCompareDataNullAll.indexOf(stringMarket)))) {
                            return false;
                        }
                    }
                } else if (patternCompareDataNull.size() != marketCompareDataNull.size()) {
                    return false;
                }
            }

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 3-true");

            return true;
        }
        return false;
    }



    private boolean finallyComparisonOnAllData(String marketArray, String patternArray) {
        String[] stringsPattern = patternArray.split("===");
        String[] stringsMarket = marketArray.split("===");

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "---------------------------------------------------------------------------- 4");

            // period
//        if (!StringHelper.giveData(TypeData.period.toString(), patternArray)
//                .equalsIgnoreCase(TypeData.NULL.toString()) && !StringHelper.giveData(TypeData.period.toString()
//                , patternArray).equals(StringHelper.giveData(TypeData.period.toString(), marketArray))) {
//            return false
//        }

        if (!stringsPattern[1].equalsIgnoreCase("null")
                && !stringsMarket[1].equals(stringsPattern[1])) {

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-1");

            return false;
        }

            // preview
        if (!stringsPattern[3].equalsIgnoreCase("null")
                && !stringsMarket[3].equals(stringsPattern[3])) {

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-2");

            return false;
        }

            // type
        if (!stringsPattern[11].equalsIgnoreCase("null")
                && !stringsMarket[11].equals(stringsPattern[11])) {

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-3");

            return false;
        }

            // avg
        if (!stringsPattern[13].equalsIgnoreCase("null")
                && !stringsMarket[13].equals(stringsPattern[13])) {

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-4");

            return false;
        }

            // dir
        if (!stringsPattern[15].equalsIgnoreCase("null")
                && !stringsMarket[15].equals(stringsPattern[15])) {

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-5");

            return false;
        }

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-true");

        return true;
    }



    /// === INNER CLASS === ///



    private class SortTheAlphabet implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {

            int result = StringHelper.giveData("type", o1).compareTo(StringHelper.giveData("type", o2));

            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
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
