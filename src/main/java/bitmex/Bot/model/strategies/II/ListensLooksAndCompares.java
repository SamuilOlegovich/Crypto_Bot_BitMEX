package bitmex.Bot.model.strategies.II;


import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.CompareHelper;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;


import java.util.*;

import static bitmex.Bot.model.DatesTimes.getDateTerminal;
import static bitmex.Bot.model.DatesTimes.getDate;
import static bitmex.Bot.model.enums.TypeData.*;
import static bitmex.Bot.model.CompareHelper.*;




public class ListensLooksAndCompares {
    private static ListensLooksAndCompares listensLooksAndCompares;

    private ArrayList<InfoIndicator> marketObjectInfoWorkingCopy;
    private ArrayList<ArrayList<String>> marketListInListString;
    private ArrayList<InfoIndicator> listInfoIndicator;

    private SavedPatterns savedPatterns;


    private boolean stopStartFlag;
    private double priceStart;
    private double priceNow;
    private long timeNow;


    private ListensLooksAndCompares() {
        ConsoleHelper.writeMessage(getDateTerminal() + " --- "
                + "Класс Listens Looks And Compares II начал работать");

        this.marketObjectInfoWorkingCopy = new ArrayList<>();
        this.savedPatterns = Gasket.getSavedPatternsClass();
        this.marketListInListString = new ArrayList<>();
        this.listInfoIndicator = new ArrayList<>();
        this.priceStart = Float.NaN;
        this.priceNow = Float.NaN;
        this.stopStartFlag = true;
    }


    public static ListensLooksAndCompares getInstance() {
        if (listensLooksAndCompares == null) {
            listensLooksAndCompares = new ListensLooksAndCompares();
            Gasket.setListensLooksAndCompares(listensLooksAndCompares);
        }
        return listensLooksAndCompares;
    }



    // принимаем объекты и если еще не запускали метод их обработки то запускаем его,
    // если он уже запущен то просто кладем объекты в массив
    // так же получаем текущую цену
    public synchronized void setInfoString(InfoIndicator infoIndicator) {
        if (stopStartFlag) {
            if (Double.isNaN(priceStart)) {
                priceStart = Gasket.getBitmexQuote().getBidPrice();
            }
            listInfoIndicator.add(infoIndicator);
        }
    }



    // отсыпаемся и начинаем работать
    private synchronized void listSortedAndCompares(boolean flag) {
        if (flag) {
            sortPrice(true);
        } else {
            marketObjectInfoWorkingCopy.addAll(listInfoIndicator);

            for (int i = marketObjectInfoWorkingCopy.size() - 1; i > -1; i--) {
                listInfoIndicator.remove(i);
            }

            // сортируем и добавляем
            sortPrice(false);
            // приводим паттерны в порядок
            setThePatternsInOrder();
            // удаляем ненужное
            removeUnnecessaryLists();
            // сохраняю те патерны которые еще актуальны на данный момент
            ReadAndSavePatterns.saveTemporarySavedPatterns(marketListInListString);

            ConsoleHelper.writeMessage(getDateTerminal() + " --- "
                    + "Сравниваю рынок с паттернами II");

            // сравниваем оставшееся с патернами
            for (ArrayList<String> marketListString : marketListInListString) {
                // получаем равные по размеру патерны
                ArrayList<ArrayList<String>> inListPatterns = savedPatterns.getListFoSize(marketListString.size());

                // если равные по размеру патерны есть то начинаем сравнивать
                if (inListPatterns != null && inListPatterns.size() > 0) {

                    for (ArrayList<String> patternListString : inListPatterns) {
                        new CompareAndMakeDecision(marketListString, patternListString);
                    }
                }
            }
        }
    }



    // удаляем листы размеры которых длиннее паттернов
    private synchronized void removeUnnecessaryLists() {
        if (marketListInListString.size() > 0) {
            int maxArraySize = savedPatterns.getMaxArraySize();
            ArrayList<Integer> lineNumbersToDelete = new ArrayList<>();
            ArrayList<ArrayList<String>> arrayListArrayList = new ArrayList<>();

            for (int i = 0; i < marketListInListString.size(); i++) {
                if (marketListInListString.get(i).size() > maxArraySize) {
                    arrayListArrayList.add(marketListInListString.get(i));
                    lineNumbersToDelete.add(i);
                }
            }

            Collections.reverse(lineNumbersToDelete);
            // сохраняю удаленные патерны
            ReadAndSavePatterns.saveSavedPatternsDelete(arrayListArrayList);

            for (Integer integer : lineNumbersToDelete) {
                marketListInListString.remove((int) integer);
            }
        }
    }


    // сортируем и наполняем лист сравнений листами строк
    // очищаем лист входящих объектов
    private synchronized void sortPrice(boolean b) {
        marketObjectInfoWorkingCopy.sort(getSortPrice());

        if (b) {
            if (marketListInListString.size() > 0) {
                for (ArrayList<String> arrayListString : marketListInListString) {
                    String stringBias = BIAS.toString() + "===" + getBias() + "===" + getDateTerminal() + "\n";
                    arrayListString.add(stringBias);
                }
            }

            ArrayList<String> arrayListOut = new ArrayList<>();
            arrayListOut.add(NULL.toString() + "===" + getDateTerminal() + "\n");
            marketListInListString.add(arrayListOut);
            priceStart = Gasket.getBitmexQuote().getBidPrice();

        } else {

            if (marketListInListString.size() > 0) {
                for (ArrayList<String> arrayListString : marketListInListString) {
                    ArrayList<String> arrayListOut = new ArrayList<>(getListString(arrayListString));
                    arrayListString.clear();
                    arrayListString.addAll(arrayListOut);
                }
            }

            marketListInListString.sort(getSortSize());
            marketObjectInfoWorkingCopy.clear();
        }
    }



    // объекты преобразовываем в строки а так же проверяем есть ли такие уровни,
    // если есть то удаляем их из входящего листа и меняем их в листе направлений
    private ArrayList<String> getListString(ArrayList<String> inPatternList) {

        ArrayList<InfoIndicator> marketObjectList = new ArrayList<>(marketObjectInfoWorkingCopy);
        ArrayList<String> patternListIn = new ArrayList<>(inPatternList);

        ArrayList<InfoIndicator> residualList = new ArrayList<>();
        ArrayList<Integer> indexDelete = new ArrayList<>();

        int count = 0;
        long time;


        if (patternListIn.size() > 0) {

                // находим количество BIAS
            for (String s : patternListIn) {
                if (s.startsWith(BIAS.toString())) count++;
            }

                // согласно количеству BIAS находим максимальный нужный нам промежуток времени
            if (count >= 1) {
                time = timeNow - (1000 * 60 * 5 * (count + 1));
            } else {
                time = timeNow - (1000 * 60 * 6);
            }

                // перебираем объекты и смотрим вписываются ли они в промежуток времени
            for (InfoIndicator marketObjectInfo : marketObjectList) {

                    // если не вписались в промежуток удаляем объект и прирываем этот цикл
                if (marketObjectInfo.getTime().getTime() < time) {
                    indexDelete.add(marketObjectList.indexOf(marketObjectInfo));

                } else {
                        // сравниваем строки объекта с строками в списке
                    for (String string : patternListIn) {
                        String[] stringsMarket = marketObjectInfo.toString().split(",");
                        String[] stringsPattern = string.split(",");

                            // если длина строки объекта и массива равны то ...
                        if (stringsMarket.length == stringsPattern.length && stringsMarket.length > 2) {

                                // если такая строка уже есть то заменяем ее на более новую
                            if (stringsMarket[2].equals(stringsPattern[2])
                                    && stringsMarket[3].equals(stringsPattern[3])
                                    && stringsMarket[5].equals(stringsPattern[5])
                                    && stringsMarket[7].equals(stringsPattern[7])) {

                                patternListIn.set(patternListIn.indexOf(string), marketObjectInfo.toString());
                                indexDelete.add(marketObjectList.indexOf(marketObjectInfo));
                            }
                        }
                    }
                }
            }

                // удаляем строку
            TreeSet<Integer> treeSet = new TreeSet<>(indexDelete);
            indexDelete.clear();
            indexDelete.addAll(treeSet);
            Collections.reverse(indexDelete);

            for (Integer index : indexDelete) {
                marketObjectList.remove((int) index);
            }
        }

            // определяем пределы последнего блока
        time = timeNow - (1000 * 60 * 6);

            // если еще остались строки, то добаляем их в последний блок
        if (marketObjectList.size() > 0) {
            for (InfoIndicator marketObjectInfo : marketObjectList) {
                if (marketObjectInfo.getTime().getTime() > time) {
                    patternListIn.add(marketObjectInfo.toString());
                } else {
                    residualList.add(marketObjectInfo);
                }
            }
        }

        return residualList.size() > 0
                ? new ArrayList<>(insertRemainingLevels(patternListIn, residualList)) : patternListIn;
    }



    // приводим паттерны в порядок
    // чистим от оставшихся предварительных исчезнувших уровняй
    private synchronized void setThePatternsInOrder() {
        // перебираем все листы листов
        for (ArrayList<String> inArrayList : marketListInListString) {
            ArrayList<String> tmpList = new ArrayList<>(CompareHelper.removeExtraLevels(inArrayList));
            inArrayList.clear();
            inArrayList.addAll(tmpList);
            tmpList.clear();
        }
    }



    // находим куда сместилась цена и другие данные
    private synchronized String getBias() {
        double bias = priceNow - priceStart;

        if (bias > 0) {
            return BUY.toString() + "===" + bias;
        }

        if (bias < 0) {
            return SELL.toString() + "===" + bias;
        }

        return NULL.toString() + "===0";
    }



    // тут мы находи нужное место расположение к вновь пришедшим уровням более длительной давности чем пять минут
    // при том их вообще могло и не быть, потому раньше мы их не заменили на более новые
    // и потом конечно же сортируем массив по новому
    private synchronized ArrayList<String> insertRemainingLevels(ArrayList<String> edit,
                                                                 ArrayList<InfoIndicator> additionalLevels) {

        ArrayList<InfoIndicator> inAdditionalLevels = new ArrayList<>(additionalLevels);
        ArrayList<String> intermediary = new ArrayList<>();
        ArrayList<String> inEdit = new ArrayList<>(edit);
        ArrayList<String> out = new ArrayList<>();


        // сортируем масив по времени от меньшего к большему
        inAdditionalLevels.sort(getSortTime());

        // вставляем оставшиеся объекты в нужный нам блок
        for (InfoIndicator marketInfo : inAdditionalLevels) {
            int index = -1;

            for (String patternString : inEdit) {

                if (!patternString.startsWith(NULL.toString())
                        && !patternString.startsWith(BUY.toString()) && !patternString.startsWith(BIAS.toString())) {
                    String[] stringsPattern = patternString.split(",");

                    if (getDate(stringsPattern[2]).getTime() < marketInfo.getTime().getTime()) {
                        index = inEdit.indexOf(patternString) - 1;
                        break;
                    }
                }
            }

            if (index > 0) {
                if (!inEdit.get(index).startsWith(BIAS.toString())) {
                    inEdit.add(index, marketInfo.toString());
                } else {
                    String[] stringBias = inEdit.get(index).split("===");
                    long time = 0;

                    for (int i = 0; i < stringBias.length; i++) {
                        if (stringBias[i].equalsIgnoreCase(TIME.toString())) {
                            time = getDate(stringBias[i + 1]).getTime() - (1000 * 60 * 5);
                            break;
                        }
                    }

                    if (time != 0 && time <= marketInfo.getTime().getTime()) {
                        inEdit.add(index + 1, marketInfo.toString());
                    } else {
                        inEdit.add(index, marketInfo.toString());
                    }
                }
            }
        }

        // сортируем по новому
        for (String string : inEdit) {
            if (string.startsWith(NULL.toString())) {
                out.add(string);
            } else if (string.startsWith(BIAS.toString())) {
                intermediary.sort(CompareHelper.getSortPriceRemainingLevels());
                intermediary.add(string);
                out.addAll(intermediary);
                intermediary.clear();
            } else if (inEdit.indexOf(string) == inEdit.size() - 1) {
                intermediary.add(string);
                intermediary.sort(CompareHelper.getSortPriceRemainingLevels());
                out.addAll(intermediary);
                intermediary.clear();
            } else {
                intermediary.add(string);
            }
        }

        intermediary.clear();
        inEdit.clear();
        return out;
    }



    protected void startListSortedAndCompares(boolean b) {
        listSortedAndCompares(b);
    }

    protected void setPriceNow(double priceNow) {
        this.priceNow = priceNow;
    }

    protected void setTimeNow(long timeNow) {
        this.timeNow = timeNow;
    }

    protected int getSizeListInfoIndicator() {
        return listInfoIndicator.size();
    }

    public void setStopStartFlag(boolean stopStartFlag) {
        this.stopStartFlag = stopStartFlag;

        if (!stopStartFlag) {
            marketObjectInfoWorkingCopy.clear();
            marketListInListString.clear();
            listInfoIndicator.clear();
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