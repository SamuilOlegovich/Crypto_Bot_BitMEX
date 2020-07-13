package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.StringHelper;

import java.util.Collections;
import java.util.ArrayList;
import java.util.TreeSet;

import static bitmex.Bot.model.CompareHelper.getSortTheAlphabet;
import static bitmex.Bot.model.DatesTimes.getDateTerminal;
import static bitmex.Bot.view.ConsoleHelper.writeMessage;
import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.*;




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
            writeMessage(getDateTerminal() + " --- "
                    + "Нашел совпадения в рынке с ПАТТЕРНАМИ User передаю на сделку --- ID-"
                    + StringHelper.giveData(ID, patternsList.get(0)));

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

        // сравниваем размеры BIAS листов, если их размер не равен, то паттерны не равны
        // если их размер равен то проверяем равны ли в них направления
        if (marketBias.size() != patternBias.size()) {
            return false;
        } else if (patternBias.size() > 0) {
            for (String patternBi : patternBias) {
                // если нул_нул то не важно равны ли строки направления
                if (!patternBi.equalsIgnoreCase(NULL_NULL.toString())
                        && !giveData(BIAS, patternBi).equals(giveData(BIAS,
                        marketBias.get(patternBias.indexOf(patternBi))))) {
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

                if (!string.startsWith(NULL.toString()) && !string.startsWith(BIAS.toString())
                        && !string.startsWith(BUY.toString())
                        && bias == i - 1) {
                    marketStrings.add(string);
                }

                if (bias == i) {
                    break;
                }
            }

            bias = 0;

            for (String string : patternsList) {
                if (string.startsWith(BIAS.toString())) {
                    bias++;
                }

                if (!string.startsWith(NULL.toString()) && !string.startsWith(BIAS.toString())
                        && !string.startsWith(BUY.toString())
                        && bias == i - 1) {
                    patternStrings.add(string);
                }

                if (bias == i) {
                    break;
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
    private boolean removeAllUnnecessaryAndCheckForAMatch(ArrayList<String> inMarketStrings,
                                                          ArrayList<String> inPatternStrings) {

        if (inPatternStrings != null && inPatternStrings.size() > 0
                && inMarketStrings != null && inMarketStrings.size() > 0) {

            ArrayList<String> patternStrings = new ArrayList<>(inPatternStrings);
            ArrayList<String> readyMarketBlock = new ArrayList<>();


            // тут приводим в порядок маркет блок, все лишнее из него убираем
            for (String patternString : patternStrings) {
                for (String marketString : inMarketStrings) {
                    if (giveData(type, patternString).equals(giveData(type, marketString))) {
                        readyMarketBlock.add(marketString);
                        break;
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

                ArrayList<Integer> deleteIndexPattern = new ArrayList<>();
                ArrayList<Integer> deleteIndexMarket = new ArrayList<>();
////////////////////////////
//                ArrayList<String> patternNullNull = new ArrayList<>();
//                ArrayList<String> marketNullNull = new ArrayList<>();
////////////////////////////


                // начинаем перебирать блоки строк и сравнивать их друг с другом
                for (String stringMarket : readyMarketBlock) {
                    String stringPattern = patternStrings.get(readyMarketBlock.indexOf(stringMarket));

////////////////////////////
                    // если цена NULL-NULL то не важно где и как стоит этот элемент (Тест)
//                    if (giveData(price, stringPattern).equalsIgnoreCase(NULL_NULL.toString())) {
//                        for (String stringPatternSearch : patternStrings) {
//                            if (giveData(type, stringPatternSearch).equals(giveData(type, stringMarket))) {
//                                deleteIndexMarket.add(readyMarketBlock.indexOf(stringMarket));
//                                deleteIndexPattern.add(patternStrings.indexOf(stringPattern));
//
//                                patternNullNull.add(stringPatternSearch);
//                                marketNullNull.add(stringMarket);
//                            }
//                        }
//                    }
////////////////////////////

                    // price
                    if (giveData(price, stringPattern).equalsIgnoreCase(NULL.toString())) {
                        // если прайс нул - то не важно в какой последовательности идут уровни,
                        // но цена у уровней в списке маркет должна быть ровна

                        if (readyMarketBlock.indexOf(stringMarket) != readyMarketBlock.size() - 1) {
                            // если это не последняя строка в блоке то заглядываем на строку вперед
                            // и смотрим не ровны ли цены между ними

                            String marketPlusOne = readyMarketBlock.get(readyMarketBlock.indexOf(stringMarket) + 1);
                            String patternPlusOne = patternStrings.get(readyMarketBlock.indexOf(stringMarket) + 1);

                            if (giveData(price, stringPattern).equals(giveData(price, patternPlusOne))
                                    && !giveData(price, stringMarket).equals(giveData(price, marketPlusOne))) {
                                return false;

                                // если цены на шаг вперед ровны добавляем в списки
                            } else if (giveData(price, stringPattern).equals(giveData(price, patternPlusOne))
                                    && giveData(price, stringMarket).equals(giveData(price, marketPlusOne))) {

                                deleteIndexMarket.add(readyMarketBlock.indexOf(stringMarket) + 1);
                                deleteIndexPattern.add(patternStrings.indexOf(stringPattern) + 1);
                                deleteIndexMarket.add(readyMarketBlock.indexOf(stringMarket));
                                deleteIndexPattern.add(patternStrings.indexOf(stringPattern));

                                patternComparePriceNull.add(giveData(type, patternPlusOne));
                                patternComparePriceNull.add(giveData(type, stringPattern));
                                marketComparePriceNull.add(giveData(type, marketPlusOne));
                                marketComparePriceNull.add(giveData(type, stringMarket));


                                patternComparePriceNullAll.add(patternPlusOne);
                                patternComparePriceNullAll.add(stringPattern);
                                marketComparePriceNullAll.add(marketPlusOne);
                                marketComparePriceNullAll.add(stringMarket);

                                // если цены на шаг впереди не ровны то проверяем на предыдущий шаг и добавляем
                            } else if (!giveData(price, stringPattern).equals(giveData(price, patternPlusOne))) {

                                String marketMinusOne = readyMarketBlock.get(readyMarketBlock
                                        .indexOf(stringMarket) - 1);
                                String patternMinusOne = patternStrings.get(readyMarketBlock
                                        .indexOf(stringMarket) - 1);

                                if (giveData(price, stringPattern).equals(giveData(price, patternMinusOne))
                                        && giveData(price, stringMarket).equals(giveData(price, marketMinusOne))) {

                                    deleteIndexMarket.add(readyMarketBlock.indexOf(stringMarket));
                                    deleteIndexPattern.add(patternStrings.indexOf(stringPattern));

                                    patternComparePriceNull.add(giveData(type, stringPattern));
                                    marketComparePriceNull.add(giveData(type, stringMarket));

                                    patternComparePriceNullAll.add(stringPattern);
                                    marketComparePriceNullAll.add(stringMarket);
                                } else {
                                    return false;
                                }
                            }
                        }
                    }

                    // Data
                    if (giveData(time, stringPattern).equalsIgnoreCase(NULL.toString())) {
                        // если время нулл - то у маркета должны быть в этом диапазоне все свечи на одной свече,
                        // а именно время ровно, направление свечи дир тоже должно быть ровно

                        if (readyMarketBlock.indexOf(stringMarket) != readyMarketBlock.size() - 1) {
                            // если это не последняя строка в блоке то заглядываем на строку вперед
                            // и смотрим не ровны ли даты между ними

                            String marketPlusOne = readyMarketBlock.get(readyMarketBlock.indexOf(stringMarket) + 1);
                            String patternPlusOne = patternStrings.get(readyMarketBlock.indexOf(stringMarket) + 1);

                            // если даты на шаг вперед ровны добавляем в списки
                            if (giveData(time, stringPattern).equals(giveData(time, patternPlusOne))
                                    && giveData(time, stringMarket).equals(giveData(time, marketPlusOne))
                                    && giveData(dir, stringPattern).equals(giveData(dir, patternPlusOne))
                                    && giveData(dir, stringMarket).equals(giveData(dir, marketPlusOne))
                                    && giveData(dir, stringPattern).equals(giveData(dir, stringMarket))) {

                                deleteIndexMarket.add(readyMarketBlock.indexOf(stringMarket) + 1);
                                deleteIndexPattern.add(patternStrings.indexOf(stringPattern) + 1);
                                deleteIndexMarket.add(readyMarketBlock.indexOf(stringMarket));
                                deleteIndexPattern.add(patternStrings.indexOf(stringPattern));

                                patternCompareDataNull.add(giveData(type, patternPlusOne));
                                patternCompareDataNull.add(giveData(type, stringPattern));
                                marketCompareDataNull.add(giveData(type, marketPlusOne));
                                marketCompareDataNull.add(giveData(type, stringMarket));

                                patternCompareDataNullAll.add(patternPlusOne);
                                patternCompareDataNullAll.add(stringPattern);
                                marketCompareDataNullAll.add(marketPlusOne);
                                marketCompareDataNullAll.add(stringMarket);

                            } else if (giveData(time, stringPattern).equals(giveData(time, patternPlusOne))
                                    && !giveData(time, stringMarket).equals(giveData(time, marketPlusOne))) {
                                return false;
                            }
                        }
                    }
                }

                // в этой части мы удаляем из входящих списков паттернов и маркеров строки которые были перенесены
                // в другие списки сравнений согласно указанным требованиям
                if (patternComparePriceNull.size() > 0
                        && marketCompareDataNull.size() == marketComparePriceNull.size()
                        && marketComparePriceNull.size() == patternCompareDataNull.size()
                        && patternCompareDataNull.size() == patternComparePriceNull.size()) {

                    TreeSet<Integer> deleteIndexHashPattern = new TreeSet<>(deleteIndexPattern);
                    TreeSet<Integer> deleteIndexHashMarket = new TreeSet<>(deleteIndexMarket);

                    deleteIndexPattern.clear();
                    deleteIndexMarket.clear();

                    deleteIndexPattern.addAll(deleteIndexHashPattern);
                    deleteIndexMarket.addAll(deleteIndexHashMarket);

                    Collections.reverse(deleteIndexPattern);
                    Collections.reverse(deleteIndexMarket);

                    for (Integer index : deleteIndexPattern) {
                        patternStrings.remove((int) index);
                    }

                    for (Integer index : deleteIndexMarket) {
                        readyMarketBlock.remove((int) index);
                    }

                    deleteIndexHashPattern.clear();
                    deleteIndexHashMarket.clear();
                    deleteIndexPattern.clear();
                    deleteIndexMarket.clear();
                }

                // если блоки одинаковы и больше нуля сравниваем их
                if (readyMarketBlock.size() == patternStrings.size() && patternStrings.size() > 0) {

                    // сортируем блоки и сравниваем их дальше построчно
                    readyMarketBlock.sort(getSortTheAlphabet());
                    patternStrings.sort(getSortTheAlphabet());

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
                    TreeSet<String> hashSetPatternNull = new TreeSet<>(patternComparePriceNull);
                    TreeSet<String> hashSetMarketNull = new TreeSet<>(marketComparePriceNull);

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

                    patternComparePriceNullAll.sort(getSortTheAlphabet());
                    marketComparePriceNullAll.sort(getSortTheAlphabet());

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
                    TreeSet<String> hashSetPatternNull = new TreeSet<>(patternCompareDataNull);
                    TreeSet<String> hashSetMarketNull = new TreeSet<>(marketCompareDataNull);

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

                    patternCompareDataNullAll.sort(getSortTheAlphabet());
                    marketCompareDataNullAll.sort(getSortTheAlphabet());

                    for (String stringMarket : marketCompareDataNullAll) {
                        if (!finallyComparisonOnAllData(stringMarket,
                                patternCompareDataNullAll.get(marketCompareDataNullAll.indexOf(stringMarket)))) {
                            return false;
                        }
                    }
                } else if (patternCompareDataNull.size() != marketCompareDataNull.size()) {
                    return false;
                }

////////////////////////////
//                if (patternNullNull.size() == marketNullNull.size() && patternNullNull.size() > 0) {
//                    patternNullNull.sort(getSortTheAlphabet());
//                    marketNullNull.sort(getSortTheAlphabet());
//
//                    for (String stringMarket : marketNullNull) {
//                        if (!finallyComparisonOnAllData(stringMarket,
//                                patternNullNull.get(marketNullNull.indexOf(stringMarket)))) {
//                            return false;
//                        }
//                    }
//                } else {
//                    return false;
//                }
/////////////////////////////
            }
            return true;
        }
        return false;
    }


    private boolean finallyComparisonOnAllData(String marketArray, String patternArray) {

        // period
        if (!giveData(period, patternArray).equalsIgnoreCase(NULL.toString())
                && !giveData(period, patternArray).equals(giveData(period, marketArray))) {
            return false;
        }

        // preview
        if (!giveData(preview, patternArray).equalsIgnoreCase(NULL.toString())
                && !giveData(preview, patternArray).equals(giveData(preview, marketArray))) {
            return false;
        }

        // type
        if (!giveData(type, patternArray).equalsIgnoreCase(NULL.toString())
                && !giveData(type, patternArray).equals(giveData(type, marketArray))) {
            return false;
        }

        // avg
        if (!giveData(avg, patternArray).equalsIgnoreCase(NULL.toString())
                && !giveData(avg, patternArray).equals(giveData(avg, marketArray))) {
            return false;
        }

        // dir
        if (!giveData(dir, patternArray).equalsIgnoreCase(NULL.toString())
                && !giveData(dir, patternArray).equals(giveData(dir, marketArray))) {
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
