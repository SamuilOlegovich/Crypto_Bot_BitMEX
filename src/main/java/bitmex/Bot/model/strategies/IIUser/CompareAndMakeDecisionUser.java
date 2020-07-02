package bitmex.Bot.model.strategies.IIUser;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashSet;

import static bitmex.Bot.model.DatesTimes.getDateTerminal;
import static bitmex.Bot.view.ConsoleHelper.writeMessage;
import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.*;




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
        writeMessage(getDateTerminal() + " --- Сравниваю паттерны USER --- "
                + this.getName());

        if (compareSheets()) {
            writeMessage(getDateTerminal() + " --- "
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

        // сравниваем размеры BIAS листов, если их размер не равен, то паттерны не равны
        // если их размер равен то проверяем равны ли в них направления
        if (marketBias.size() != patternBias.size()) {
            return false;
        } else if (marketBias.size() > 0) {
            for (String marketString : marketBias) {
                if (!giveData(BIAS.toString(), patternBias.get(marketBias.indexOf(marketString)))
                        .equalsIgnoreCase(giveData(BIAS.toString(), marketString))) {
                    return false;
                }
            }
        }

        maxBias = marketBias.size() + 1;

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

                if (bias == i) {
                    break;
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

                if (bias == i) {
                    break;
                }
            }

            // отправляем полученые блоки на дальнейшую проверку
            if (!removeAllUnnecessaryAndCheckForAMatch(marketStrings, patternStrings)) {
                return false;
            }
        }

        writeMessage(getDateTerminal() + " --- "
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
                    if (giveData(type.toString(), patternString).equals(giveData(type.toString(), marketString))) {
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


                // начинаем перебирать блоки строк и сравнивать их друг с другом
                for (String stringMarket : readyMarketBlock) {
                    String stringPattern = patternStrings.get(readyMarketBlock.indexOf(stringMarket));


                    // price
                    if (giveData(price.toString(), stringPattern).equalsIgnoreCase(NULL.toString())) {
                        // если прайс нул - то не важно в какой последовательности идут уровни,
                        // но цена у уровней в списке маркет должна быть ровна

                        if (readyMarketBlock.indexOf(stringMarket) != readyMarketBlock.size() - 1) {
                            // если это не последняя строка в блоке то заглядываем на строку вперед
                            // и смотрим не ровны ли цены между ними

                            String marketPlusOne = readyMarketBlock.get(readyMarketBlock.indexOf(stringMarket) + 1);
                            String patternPlusOne = patternStrings.get(readyMarketBlock.indexOf(stringMarket) + 1);

                            if (giveData(price.toString(), stringPattern)
                                    .equals(giveData(price.toString(), patternPlusOne))

                                    && !giveData(price.toString(), stringMarket)
                                    .equals(giveData(price.toString(), marketPlusOne))) {
                                return false;

                                // если цены на шаг вперед ровны добавляем в списки
                            } else if (giveData(price.toString(), stringPattern)
                                    .equals(giveData(price.toString(), patternPlusOne))

                                    && giveData(price.toString(), stringMarket)
                                    .equals(giveData(price.toString(), marketPlusOne))) {

                                patternComparePriceNull.add(giveData(type.toString(), patternPlusOne));
                                patternComparePriceNull.add(giveData(type.toString(), stringPattern));
                                marketComparePriceNull.add(giveData(type.toString(), marketPlusOne));
                                marketComparePriceNull.add(giveData(type.toString(), stringMarket));

                                patternComparePriceNullAll.add(patternPlusOne);
                                patternComparePriceNullAll.add(stringPattern);
                                marketComparePriceNullAll.add(marketPlusOne);
                                marketComparePriceNullAll.add(stringMarket);

                                // если цены на шаг впереди не ровны то проверяем на предыдущий шаг и добавляем
                            } else if (!giveData(price.toString(), stringPattern)
                                    .equals(giveData(price.toString(), patternPlusOne))) {

                                String marketMinusOne = readyMarketBlock.get(readyMarketBlock
                                        .indexOf(stringMarket) - 1);
                                String patternMinusOne = patternStrings.get(readyMarketBlock
                                        .indexOf(stringMarket) - 1);

                                if (giveData(price.toString(), stringPattern)
                                        .equals(giveData(price.toString(), patternMinusOne))

                                        && giveData(price.toString(), stringMarket)
                                        .equals(giveData(price.toString(), marketMinusOne))) {

                                    patternComparePriceNull.add(giveData(type.toString(), stringPattern));
                                    marketComparePriceNull.add(giveData(type.toString(), stringMarket));

                                    patternComparePriceNullAll.add(stringPattern);
                                    marketComparePriceNullAll.add(stringMarket);
                                } else {
                                    return false;
                                }
                            }
                        }
                    }

                        // Data
                    if (giveData(time.toString(), stringPattern).equalsIgnoreCase(NULL.toString())) {
                        // если время нулл - то у маркета должны быть в этом диапазоне все свечи на одной свече,
                        // а именно время ровно, направление свечи дир тоже должно быть ровно

                        if (readyMarketBlock.indexOf(stringMarket) != readyMarketBlock.size() - 1) {
                            // если это не последняя строка в блоке то заглядываем на строку вперед
                            // и смотрим не ровны ли даты между ними

                            String marketPlusOne = readyMarketBlock.get(readyMarketBlock.indexOf(stringMarket) + 1);
                            String patternPlusOne = patternStrings.get(readyMarketBlock.indexOf(stringMarket) + 1);

                            // если даты на шаг вперед ровны добавляем в списки
                            if (giveData(time.toString(), stringPattern)
                                    .equals(giveData(time.toString(), patternPlusOne))

                                    && giveData(time.toString(), stringMarket)
                                    .equals(giveData(time.toString(), marketPlusOne))

                                    && giveData(dir.toString(), stringPattern)
                                    .equals(giveData(dir.toString(), patternPlusOne))

                                    && giveData(dir.toString(), stringMarket)
                                    .equals(giveData(dir.toString(), marketPlusOne))

                                    && giveData(dir.toString(), stringPattern)
                                    .equals(giveData(dir.toString(), stringMarket))) {

                                patternCompareDataNull.add(giveData(type.toString(), patternPlusOne));
                                patternCompareDataNull.add(giveData(type.toString(), stringPattern));
                                marketCompareDataNull.add(giveData(type.toString(), marketPlusOne));
                                marketCompareDataNull.add(giveData(type.toString(), stringMarket));

                                patternCompareDataNullAll.add(patternPlusOne);
                                patternCompareDataNullAll.add(stringPattern);
                                marketCompareDataNullAll.add(marketPlusOne);
                                marketCompareDataNullAll.add(stringMarket);

                            } else if (giveData(time.toString(), stringPattern)
                                    .equals(giveData(time.toString(), patternPlusOne))

                                    && !giveData(time.toString(), stringMarket)
                                    .equals(giveData(time.toString(), marketPlusOne))) {

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

                    ArrayList<Integer> deleteIndex = new ArrayList<>();

                    for (String stringNull : marketCompareDataNull) {
                        for (String stringReady : readyMarketBlock) {
                            if (giveData(type.toString(), stringReady).equals(stringNull)) {
                                deleteIndex.add(readyMarketBlock.indexOf(stringReady));
                            }
                        }
                    }

                    for (String stringNull : marketComparePriceNull) {
                        for (String stringReady : readyMarketBlock) {
                            if (giveData(type.toString(), stringReady).equals(stringNull)) {
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

                            if (giveData(type.toString(), stringReady).equals(stringNull)) {
                                deleteIndex.add(patternStrings.indexOf(stringReady));
                            }
                        }
                    }

                    for (String stringNull : patternComparePriceNull) {
                        for (String stringReady : patternStrings) {

                            if (giveData(type.toString(), stringReady).equals(stringNull)) {
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

            writeMessage(getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 3-true");

            return true;
        }
        return false;
    }



    private boolean finallyComparisonOnAllData(String marketArray, String patternArray) {

        writeMessage(getDateTerminal() + " --- "
                + "---------------------------------------------------------------------------- 4");

            // period
        if (!giveData(period.toString(), patternArray).equalsIgnoreCase(NULL.toString())
                && !giveData(period.toString(), patternArray).equals(giveData(period.toString(), marketArray))) {

            writeMessage(getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-1");

            return false;
        }

            // preview
        if (!giveData(preview.toString(), patternArray).equalsIgnoreCase(NULL.toString())
                && !giveData(preview.toString(), patternArray).equals(giveData(preview.toString(), marketArray))) {

            writeMessage(getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-2");

            return false;
        }

            // type
        if (!giveData(type.toString(), patternArray).equalsIgnoreCase(NULL.toString())
                && !giveData(type.toString(), patternArray).equals(giveData(type.toString(), marketArray))) {

            writeMessage(getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-3");

            return false;
        }

            // avg
        if (!giveData(avg.toString(), patternArray).equalsIgnoreCase(NULL.toString())
                && !giveData(avg.toString(), patternArray).equals(giveData(avg.toString(), marketArray))) {

            writeMessage(getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-4");

            return false;
        }

            // dir
        if (!giveData(dir.toString(), patternArray).equalsIgnoreCase(NULL.toString())
                && !giveData(dir.toString(), patternArray).equals(giveData(dir.toString(), marketArray))) {

            writeMessage(getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-5");

            return false;
        }

        writeMessage(getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-true");

        return true;
    }



    /// === INNER CLASS === ///



    private class SortTheAlphabet implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int result = giveData(type.toString(), o1).compareTo(giveData(type.toString(), o2));
            return Integer.compare(result, 0);
        }
    }




    /// === TEST === ///



    public static void main(String[] args) {
        ArrayList<String> pattern = new ArrayList<>();
        ArrayList<String> market = new ArrayList<>();

        pattern.add("BUY===1===SELL===0===AVERAGE===3.28===MAX===5.0===SIZE===220===BLOCK===1===TYPE===OPEN_POS_BID_PLUS_SMALL===ID===1");
        pattern.add("period===M5===preview===null===time===2020-06-11 11:38:00===price===9781.0===value===69770===type===OPEN_POS_BID_PLUS_SMALL===avg===null===dir===null===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===M5===preview===null===time===2020-06-11 11:38:00===price===9780.5===value===-1088603===type===ASK_SMALL===avg===null===dir===null===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===M5===preview===null===time===2020-06-11 11:38:00===price===9778.0===value===69770===type===VOLUME_SMALL===avg===null===dir===null===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===M5===preview===null===time===2020-06-11 11:38:00===price===9777.5===value===-1088603===type===DELTA_ASK_SMALL===avg===null===dir===null===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===M5===preview===null===time===2020-06-11 11:37:00===price===9775.0===value===514702===type===BID_SMALL===avg===null===dir===null===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        pattern.add("BIAS===SELL===4.5===2020-06-11 11:40:00");
        pattern.add("period===null===preview===null===time===2020-06-11 11:37:00===price===9776.0===value===514702===type===OPEN_POS_PLUS===avg===null===dir===null===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        pattern.add("period===null===preview===null===time===2020-06-11 11:37:00===price===9775.0===value===514702===type===OPEN_POS_ASK_PLUS===avg===null===dir===null===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        pattern.add("period===null===preview===null===time===NULL===price===NULL===value===280876===type===ASK===avg===null===dir===-1===open===9777.5===close===9776.0===high===9778.0===low===9776.0");
        pattern.add("period===null===preview===null===time===NULL===price===NULL===value===-1633222===type===VOLUME===avg===null===dir===-1===open===9776.0===close===9774.0===high===9776.5===low===9774.0");
        pattern.add("period===null===preview===null===time===NULL===price===NULL===value===592200===type===BID===avg===null===dir===-1===open===9774.5===close===9774.5===high===9774.5===low===9774.0");
        pattern.add("period===null===preview===null===time===2020-06-11 11:38:00===price===9769.5===value===-1088603===type===DELTA_BID===avg===null===dir===-1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===null===preview===null===time===2020-06-11 11:38:00===price===9768.5===value===69770===type===OPEN_POS_BID_MINUS===avg===null===dir===-1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===NULL===preview===null===time===2020-06-11 11:37:00===price===9767.0===value===514702===type===DELTA_ZS_PLUS===avg===null===dir===1===open===9769.5===close===9781.5===high===9782.0===low===9769.0");






        market.add("BUY===1===SELL===0===AVERAGE===3.28===MAX===5.0===SIZE===220===BLOCK===1===TYPE===OPEN_POS_BID_PLUS_SMALL===ID===1");


        market.add("period===M5===preview===1===time===2020-06-11 11:38:00===price===9781.0===value===69770===type===OPEN_POS_BID_PLUS_SMALL===avg===null===dir===1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");

        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.5===value===377855===type===OPEN_POS_PLUS_HL===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-585140===type===DELTA_BID_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-194061===type===OPEN_POS_MINUS_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===276839===type===OI_ZS_MIN_MINUS===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.0===value===-585140===type===DELTA_ZS_MIN_PLUS===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");


        market.add("period===M5===preview===0===time===2020-06-11 11:38:00===price===9780.5===value===-1088603===type===ASK_SMALL===avg===null===dir===0===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        market.add("period===M5===preview===0===time===2020-06-11 11:38:00===price===9778.0===value===69770===type===VOLUME_SMALL===avg===null===dir===-1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        market.add("period===M5===preview===1===time===2020-06-11 11:38:00===price===9777.5===value===-1088603===type===DELTA_ASK_SMALL===avg===null===dir===1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        market.add("period===M5===preview===1===time===2020-06-11 11:37:00===price===9775.0===value===514702===type===BID_SMALL===avg===null===dir===1===open===9769.5===close===9781.5===high===9782.0===low===9769.0");


        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.5===value===377855===type===OPEN_POS_PLUS_HL===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-585140===type===DELTA_BID_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-194061===type===OPEN_POS_MINUS_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===276839===type===OI_ZS_MIN_MINUS===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.0===value===-585140===type===DELTA_ZS_MIN_PLUS===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");



        market.add("BIAS===SELL===4.5===2020-06-11 11:40:00");

        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.5===value===377855===type===OPEN_POS_PLUS_HL===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-585140===type===DELTA_BID_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-194061===type===OPEN_POS_MINUS_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M8===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===276839===type===OI_ZS_MIN_MINUS===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.0===value===-585140===type===DELTA_ZS_MIN_PLUS===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");


        market.add("period===M15===preview===1===time===2020-06-11 11:37:00===price===9776.0===value===514702===type===OPEN_POS_PLUS===avg===null===dir===1===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        market.add("period===M15===preview===0===time===2020-06-11 11:37:00===price===9775.0===value===514702===type===OPEN_POS_ASK_PLUS===avg===null===dir===1===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        market.add("period===M0===preview===1===time===2020-06-11 11:38:00===price===9774.0===value===592200===type===BID===avg===null===dir===-1===open===9774.5===close===9774.5===high===9774.5===low===9774.0");
        market.add("period===M15===preview===0===time===2020-06-11 11:38:00===price===9774.0===value===-1633222===type===VOLUME===avg===null===dir===-1===open===9776.0===close===9774.0===high===9776.5===low===9774.0");
        market.add("period===M5===preview===0===time===2020-06-11 11:38:00===price===9774.0===value===280876===type===ASK===avg===null===dir===-1===open===9777.5===close===9776.0===high===9778.0===low===9776.0");

        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.5===value===377855===type===OPEN_POS_PLUS_HL===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-585140===type===DELTA_BID_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M90===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-194061===type===OPEN_POS_MINUS_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===276839===type===OI_ZS_MIN_MINUS===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.0===value===-585140===type===DELTA_ZS_MIN_PLUS===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");


        market.add("period===M5===preview===0===time===2020-06-11 11:37:00===price===9769.5===value===-1088603===type===DELTA_BID===avg===null===dir===-1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        market.add("period===M5===preview===1===time===2020-06-11 11:36:00===price===9768.5===value===69770===type===OPEN_POS_BID_MINUS===avg===null===dir===-1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        market.add("period===M15===preview===1===time===2020-06-11 11:37:00===price===9767.0===value===514702===type===DELTA_ZS_PLUS===avg===null===dir===1===open===9769.5===close===9781.5===high===9782.0===low===9769.0");


        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.5===value===377855===type===OPEN_POS_PLUS_HL===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-585140===type===DELTA_BID_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-194061===type===OPEN_POS_MINUS_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===276839===type===OI_ZS_MIN_MINUS===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.0===value===-585140===type===DELTA_ZS_MIN_PLUS===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");



        new CompareAndMakeDecisionUser(market, pattern);
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
