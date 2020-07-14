package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.CompareHelper;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.util.*;

import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.*;
import static bitmex.Bot.model.CompareHelper.*;
import static java.lang.Float.NaN;



public class ListensLooksAndComparesUser {
    private static ListensLooksAndComparesUser listensLooksAndComparesUser;

    private KeepsTrackOfFillingListInfoIndicatorUser keepsTrackOfFillingListInfoIndicatorUser;
    private SavedPatternsUser savedPatternsUser;

    private final ArrayList<InfoIndicator> listInfoIndicatorWorkingCopy;
    private final ArrayList<ArrayList<String>> marketListsStrings;
    private final ArrayList<InfoIndicator> listInfoIndicator;

    private boolean stopStartFlag;
    private double priceStart;
    private double priceNow;
    private long timeNow;



    private ListensLooksAndComparesUser() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Класс ListensLooksAndComparesUser начал работать");

        this.keepsTrackOfFillingListInfoIndicatorUser
                = new KeepsTrackOfFillingListInfoIndicatorUser(this);
        this.savedPatternsUser = Gasket.getSavedPatternsUserClass();
        this.listInfoIndicatorWorkingCopy = new ArrayList<>();
        this.marketListsStrings = new ArrayList<>();
        this.listInfoIndicator = new ArrayList<>();
        this.stopStartFlag = true;
        this.priceStart = NaN;
        this.priceNow = NaN;
    }



    public static ListensLooksAndComparesUser getInstance() {

        if (listensLooksAndComparesUser == null) {
            listensLooksAndComparesUser = new ListensLooksAndComparesUser();
            Gasket.setListensLooksAndComparesUser(listensLooksAndComparesUser);
        }

        return listensLooksAndComparesUser;
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
            listInfoIndicatorWorkingCopy.addAll(listInfoIndicator);

            for (int i = listInfoIndicatorWorkingCopy.size() - 1; i > -1; i--) {
                listInfoIndicator.remove(i);
            }

            // сортируем и добавляем
            sortPrice(false);
            // приводим паттерны в порядок
//            setThePatternsInOrder();

            // получаем патерны
            ArrayList<ArrayList<String>> patternsListsStrings = savedPatternsUser.getListsPricePatternsUser();

            // сравниваем с патернами
            if (patternsListsStrings.size() > 0) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                        + "Сравниваю рынок с паттернами USER");

                for (ArrayList<String> marketListString : marketListsStrings) {
                    for (ArrayList<String> patternListString : patternsListsStrings) {
                        new CompareAndMakeDecisionUser(marketListString, patternListString);
                    }
                }
            }

            // удаляем ненужное
            removeUnnecessaryLists();
            // сохраняю те патерны которые еще актуальны на данный момент
            ReadAndSavePatternsUser.saveTemporarySavedPatternsUser(marketListsStrings);
        }
    }



    // удаляем листы размеры которых длиннее паттернов
    private synchronized void removeUnnecessaryLists() {
        ArrayList<ArrayList<String>> arrayListArrayList = new ArrayList<>();
        ArrayList<Integer> lineNumbersToDelete = new ArrayList<>();
        int maxCountBias = savedPatternsUser.getMaxCountBiasUser();


        if (marketListsStrings.size() > 0) {
            for (int i = 0; i < marketListsStrings.size(); i++) {
                int countBias = 0;

                for (String s : marketListsStrings.get(i)) {
                    if (s.startsWith(BIAS.toString())) {
                        countBias++;
                    }
                }

                if (countBias > maxCountBias) {
                    arrayListArrayList.add(marketListsStrings.get(i));
                    lineNumbersToDelete.add(i);
                }
            }

            Collections.reverse(lineNumbersToDelete);
            // сохраняю удаленные патерны
            ReadAndSavePatternsUser.saveSavedPatternsDeleteUser(arrayListArrayList);

            for (Integer integer : lineNumbersToDelete) {
                marketListsStrings.remove((int) integer);
            }
        }
    }



    // сортируем и наполняем лист сравнений листами строк
    // очищаем лист входящих объектов
    private synchronized void sortPrice(boolean b) {

        listInfoIndicatorWorkingCopy.sort(getSortPrice());


        if (b) {
            if (marketListsStrings.size() > 0) {
                for (ArrayList<String> arrayListString : marketListsStrings) {
                    String stringBias = BIAS.toString() + "===" + getBias()
                            + "===" + DatesTimes.getDateTerminal() + "\n";
                    arrayListString.add(stringBias);
                }
            }

            ArrayList<String> arrayListOut = new ArrayList<>();
            arrayListOut.add(NULL.toString() + "===" + DatesTimes.getDateTerminal() + "\n");
            marketListsStrings.add(arrayListOut);
            priceStart = Gasket.getBitmexQuote().getBidPrice();

        } else {

            if (marketListsStrings.size() > 0) {
                for (ArrayList<String> arrayListString : marketListsStrings) {
                    ArrayList<String> arrayListOut = new ArrayList<>(getListString(arrayListString));
                    arrayListString.clear();
                    arrayListString.addAll(arrayListOut);
                }
            }

            marketListsStrings.sort(getSortSize());
            listInfoIndicatorWorkingCopy.clear();
        }
    }



    // объекты преобразовываем в строки а так же проверяем есть ли такие уровни,
    // если есть то удаляем их из входящего листа и меняем их в листе направлений
    private synchronized ArrayList<String> getListString(ArrayList<String> arrayListIn) {

        ArrayList<InfoIndicator> infoIndicatorArrayListWorking = new ArrayList<>(listInfoIndicatorWorkingCopy);
        ArrayList<InfoIndicator> residualArrayList = new ArrayList<>();
        ArrayList<String> inArrayList = new ArrayList<>(arrayListIn);
        ArrayList<Integer> indexDelete = new ArrayList<>();
        int count = 0;
        long times;


        if (inArrayList.size() > 0) {

            // находим количество BIAS
            for (String s : inArrayList) {
                if (s.startsWith(BIAS.toString())) count++;
            }

            // согласно количеству BIAS находим максимальный нужный нам промежуток времени
            if (count >= 1) {
                times = timeNow - (1000 * 60 * 5 * (count + 1));
            } else {
                times = timeNow - (1000 * 60 * 6);
            }

                // перебираем объекты и смотрим вписываются ли они в промежуток времени
            for (InfoIndicator infoIndicator : infoIndicatorArrayListWorking) {

                    // если не вписались в промежуток удаляем объект и прирываем этот цикл
                if (infoIndicator.getTime().getTime() < times) {
                        indexDelete.add(infoIndicatorArrayListWorking.indexOf(infoIndicator));

                } else {
                        // сравниваем строки объекта с строками в списке
                    for (String string : inArrayList) {

                            // если длина строки объекта и массива равны то ...
                        if (!string.startsWith(BIAS.toString()) && !string.startsWith(BUY.toString())
                                && !string.startsWith(NULL.toString())) {

                                // если такая строка уже есть то заменяем ее на более новую
                            if (giveData(time, infoIndicator.toStringUser()).equals(giveData(time, string))
                                    && giveData(type, infoIndicator.toStringUser()).equals(giveData(type, string))
                                    && giveData(dir, infoIndicator.toStringUser()).equals(giveData(dir, string))) {

                                inArrayList.set(inArrayList.indexOf(string), infoIndicator.toStringUser());
                                indexDelete.add(infoIndicatorArrayListWorking.indexOf(infoIndicator));
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
                infoIndicatorArrayListWorking.remove((int) index);
            }
        }

            // определяем пределы последнего блока
        times = timeNow - (1000 * 60 * 6);

            // если еще остались строки, то добаляем их в последний блок
        if (infoIndicatorArrayListWorking.size() > 0) {
            for (InfoIndicator infoIndicator : infoIndicatorArrayListWorking) {
                if (infoIndicator.getTime().getTime() > times) {
                    inArrayList.add(infoIndicator.toStringUser());
                } else {
                    residualArrayList.add(infoIndicator);
                }
            }
        }

        return residualArrayList.size() > 0
                ? new ArrayList<>(insertRemainingLevels(inArrayList, residualArrayList)) : inArrayList;
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

                    if (DatesTimes.getDate(giveData(time, patternString)).getTime() < marketInfo.getTime().getTime()) {
                        index = inEdit.indexOf(patternString) - 1;
                        break;
                    }
                }
            }

            if (index > 0) {
                if (!inEdit.get(index).startsWith(BIAS.toString())) {
                    inEdit.add(index, marketInfo.toStringUser());
                } else {
                    long time = DatesTimes.getDate(giveData(TIME, inEdit.get(index))).getTime() - (1000 * 60 * 5);

                    if (time != 0 && time <= marketInfo.getTime().getTime()) {
                        inEdit.add(index + 1, marketInfo.toStringUser());
                    } else {
                        inEdit.add(index, marketInfo.toStringUser());
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



    // приводим паттерны в порядок
    // чистим от оставшихся предварительных исчезнувших уровняй
    private synchronized void setThePatternsInOrder() {
         // перебираем все листы листов
        for (ArrayList<String> inArrayList : marketListsStrings) {
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
            listInfoIndicatorWorkingCopy.clear();
            marketListsStrings.clear();
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