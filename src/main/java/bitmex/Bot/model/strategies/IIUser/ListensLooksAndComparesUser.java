package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.strategies.oneStrategies.TradeSell;
import bitmex.Bot.model.strategies.oneStrategies.TradeBuy;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.TreeSet;

import static java.lang.Float.NaN;


public class ListensLooksAndComparesUser {
    private static ListensLooksAndComparesUser listensLooksAndComparesUser;

    private volatile ArrayList<InfoIndicator> listInfoIndicatorWorkingCopy;
    private volatile ArrayList<ArrayList<String>> listInListString;
    private volatile ArrayList<InfoIndicator> listInfoIndicator;

    private KeepsTrackOfFillingListInfoIndicatorUser keepsTrackOfFillingListInfoIndicatorUser;
    private SortPriceUser sortPriceComparatorUser;
    private SavedPatternsUser savedPatternsUser;
    private SortSizeUser sortSizeUser;

//    private boolean oneStartFlag;

    private double priceStart;
    private double priceNow;


    private ListensLooksAndComparesUser() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Класс ListensLooksAndCompares начал работать");

        this.keepsTrackOfFillingListInfoIndicatorUser = new KeepsTrackOfFillingListInfoIndicatorUser();
        this.savedPatternsUser = Gasket.getSavedPatternsUserClass();
        this.listInfoIndicatorWorkingCopy = new ArrayList<>();
        this.sortPriceComparatorUser = new SortPriceUser();
        this.listInfoIndicator = new ArrayList<>();
        this.listInListString = new ArrayList<>();
        this.sortSizeUser = new SortSizeUser();
//        this.oneStartFlag = true;
        this.priceStart = NaN;
        this.priceNow = NaN;
    }


    public static ListensLooksAndComparesUser getInstance() {
        if (listensLooksAndComparesUser == null) listensLooksAndComparesUser = new ListensLooksAndComparesUser();
        return listensLooksAndComparesUser;
    }



    // принимаем объекты и если еще не запускали метод их обработки то запускаем его,
    // если он уже запущен то просто кладем объекты в массив
    // так же получаем текущую цену
    public synchronized void setInfoString(InfoIndicator infoIndicator) {
        if (Double.isNaN(priceStart)) {
            priceStart = Gasket.getBitmexQuote().getBidPrice();
        }
        listInfoIndicator.add(infoIndicator);
    }



    // отсыпаемся и начинаем работать
    private synchronized void listSortedAndCompares() {
        if (isTime()) {
            try {
                Thread.sleep(1000 * 11);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            listInfoIndicatorWorkingCopy.addAll(listInfoIndicator);
            listInfoIndicator.clear();
            // сортируем и добавляем
            sortPrice(true);
            // приводим паттерны в порядок
            setThePatternsInOrder();
            // удаляем ненужное
            removeUnnecessaryLists();
            // сохраняю те патерны которые еще актуальны на данный момент
            ReadAndSavePatternsUser.saveTemporarySavedPatternsUser(listInListString);

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Сравниваю рынок с ПАТТЕРНАМИ USER");

            // сравниваем оставшееся с патернами
            for (ArrayList<String> thisArrayListString : listInListString) {
                // получаем равные по размеру патерны
                ArrayList<ArrayList<String>> inListPatterns = savedPatternsUser.getListFoSize(thisArrayListString.size());

                // если равные по размеру патерны есть то начинаем сравнивать
                if (inListPatterns != null) {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- Есть - "
                            + inListPatterns.size() + " - паттерна по размеру");

                    for (ArrayList<String> inArrayListString : inListPatterns) {
                        ArrayList<String> thisTheSamePriceList = new ArrayList<>();
                        ArrayList<String> inTheSamePriceList = new ArrayList<>();
                        boolean result = true;

                        for (int i = 1; i < inArrayListString.size(); i++) {
                            String[] strings1;
                            String[] strings2;
                            String[] strings3;
                            String[] strings4;

                            // Тут мы так же определяем не строка ли это направления и сравниваем либо ее
                            // либо строки уровней
                            //
                            // period===M5===preview===1===price===9690,0===value===1187305===
                            // type===OpenPosMinusSmall===avg===0===dir===1===open===9674,5===
                            // close===9697,5===high===9697,5===low===9674,5    <----- строка уровней
                            //
                            // BIAS===BUY===10===AVERAGE===3===MAX===5   <----- строка направления
                            if (inArrayListString.get(i).startsWith("BIAS")
                                    && thisArrayListString.get(i).startsWith("BIAS")) {

                                strings1 = thisArrayListString.get(i).split("===");
                                strings2 = inArrayListString.get(i).split("===");

                                // если хоть один объект не равен то прирываем цикл
                                if (!strings1[1].equals(strings2[1])) {
                                    result = false;
                                    break;
                                }

                            } else if ((inArrayListString.get(i).startsWith("BIAS")
                                    && !thisArrayListString.get(i).startsWith("BIAS"))
                                    || (!inArrayListString.get(i).startsWith("BIAS")
                                    && thisArrayListString.get(i).startsWith("BIAS"))) {
                                // если под одним и тем же номером находятся разные по значимости строки то прирываем цикл
                                result = false;
                                break;

                            } else if (!inArrayListString.get(i).startsWith("BIAS")
                                    && !thisArrayListString.get(i).startsWith("BIAS")) {

                                // тут мы заглядываем на строку вперед и проверяем не сходятся ли там цена с этой строкой
                                // если сходится то складируем их отдельно, сортируем и сравниваем
                                // это позволяет в любой очередности выставлять уровни находящиеся в одной ценовой точке
                                strings1 = thisArrayListString.get(i).split("===");
                                strings2 = inArrayListString.get(i).split("===");
                                strings3 = thisArrayListString.get((i + 1) < thisArrayListString.size() - 1
                                        ? (i + 1) : i).split("===");
                                strings4 = inArrayListString.get((i + 1) < inArrayListString.size() - 1
                                        ? (i + 1) : i).split("===");

                                if (i < inArrayListString.size() - 1) {
                                    if (!strings3[0].equals("BIAS") && !strings4[0].equals("BIAS")
                                            && !strings1[0].equals("BIAS") && !strings2[0].equals("BIAS")
                                            && !strings1[7].equals(strings3[7]) && !strings2[7].equals(strings4[7])) {

                                        // если хоть один объект не равен то прирываем цикл
                                        if (!strings1[11].equals(strings2[11]) || !strings1[15].equals(strings2[15])) {
                                            result = false;
                                            break;
                                        }
                                    } else if (!strings3[0].equals("BIAS") && !strings4[0].equals("BIAS")
                                            && strings1[7].equals(strings3[7]) && strings2[7].equals(strings4[7])) {
                                        thisTheSamePriceList.add(strings3[11]);
                                        thisTheSamePriceList.add(strings4[11]);
                                        inTheSamePriceList.add(strings1[11]);
                                        inTheSamePriceList.add(strings2[11]);
                                    }
                                } else {
                                    if (!strings1[11].equals(strings2[11]) || !strings1[15].equals(strings2[15])) {
                                        result = false;
                                        break;
                                    } else {
                                        Collections.sort(thisTheSamePriceList);
                                        Collections.sort(inTheSamePriceList);

                                        for (String string : thisTheSamePriceList) {
                                            if (!string.equals(inTheSamePriceList
                                                    .get(thisTheSamePriceList.indexOf(string)))) {
                                                result = false;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (result) {
                            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                                    + "Нашел совпадения в рынке с ПАТТЕРНАМИ User передаю на сделку");
                            makeDeal(inArrayListString.get(0));
                            return;
                            // возможно тут надо поставить return
                        } else {
                            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                                    + "Совпадений с ПАТТЕРНАМИ USER не найдено");
                        }
                    }
                } else {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                            + " --- Паттернов равных по размеру нет");
                }
            }
        } else {
            listInfoIndicatorWorkingCopy.addAll(listInfoIndicator);
            listInfoIndicator.clear();
            // сортируем и добавляем
            sortPrice(false);
            // приводим паттерны в порядок
            setThePatternsInOrder();
            // удаляем ненужное
            removeUnnecessaryLists();
            // сохраняю те патерны которые еще актуальны на данный момент
            ReadAndSavePatternsUser.saveTemporarySavedPatternsUser(listInListString);

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Сравниваю рынок с ПАТТЕРНАМИ USER");

            // сравниваем оставшееся с патернами
            for (ArrayList<String> thisArrayListString : listInListString) {
                // получаем равные по размеру патерны
                ArrayList<ArrayList<String>> inListPatterns = savedPatternsUser.getListFoSize(thisArrayListString.size());

                // если равные по размеру патерны есть то начинаем сравнивать
                if (inListPatterns != null) {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- Есть - "
                            + inListPatterns.size() + " - паттерна по размеру");

                    for (ArrayList<String> inArrayListString : inListPatterns) {
                        ArrayList<String> thisTheSamePriceList = new ArrayList<>();
                        ArrayList<String> inTheSamePriceList = new ArrayList<>();
                        boolean result = true;

                        for (int i = 1; i < inArrayListString.size(); i++) {
                            String[] strings1;
                            String[] strings2;
                            String[] strings3;
                            String[] strings4;

                            // Тут мы так же определяем не строка ли это направления и сравниваем либо ее
                            // либо строки уровней
                            //
                            // period===M5===preview===1===price===9690,0===value===1187305===
                            // type===OpenPosMinusSmall===avg===0===dir===1===open===9674,5===
                            // close===9697,5===high===9697,5===low===9674,5    <----- строка уровней
                            //
                            // BIAS===BUY===10===AVERAGE===3===MAX===5   <----- строка направления
                            if (inArrayListString.get(i).startsWith("BIAS")
                                    && thisArrayListString.get(i).startsWith("BIAS")) {

                                strings1 = thisArrayListString.get(i).split("===");
                                strings2 = inArrayListString.get(i).split("===");

                                // если хоть один объект не равен то прирываем цикл
                                if (!strings1[1].equals(strings2[1])) {
                                    result = false;
                                    break;
                                }

                            } else if ((inArrayListString.get(i).startsWith("BIAS")
                                    && !thisArrayListString.get(i).startsWith("BIAS"))
                                    || (!inArrayListString.get(i).startsWith("BIAS")
                                    && thisArrayListString.get(i).startsWith("BIAS"))) {
                                // если под одним и тем же номером находятся разные по значимости строки то прирываем цикл
                                result = false;
                                break;

                            } else if (!inArrayListString.get(i).startsWith("BIAS")
                                    && !thisArrayListString.get(i).startsWith("BIAS")) {

                                // тут мы заглядываем на строку вперед и проверяем не сходятся ли там цена с этой строкой
                                // если сходится то складируем их отдельно, сортируем и сравниваем
                                // это позволяет в любой очередности выставлять уровни находящиеся в одной ценовой точке
                                strings1 = thisArrayListString.get(i).split("===");
                                strings2 = inArrayListString.get(i).split("===");
                                strings3 = thisArrayListString.get((i + 1) < thisArrayListString.size() - 1
                                        ? (i + 1) : i).split("===");
                                strings4 = inArrayListString.get((i + 1) < inArrayListString.size() - 1
                                        ? (i + 1) : i).split("===");

                                if (i < inArrayListString.size() - 1) {
                                    if (!strings3[0].equals("BIAS") && !strings4[0].equals("BIAS")
                                            && !strings1[0].equals("BIAS") && !strings2[0].equals("BIAS")
                                            && !strings1[7].equals(strings3[7]) && !strings2[7].equals(strings4[7])) {

                                        // если хоть один объект не равен то прирываем цикл
                                        if (!strings1[11].equals(strings2[11]) || !strings1[15].equals(strings2[15])) {
                                            result = false;
                                            break;
                                        }
                                    } else if (!strings3[0].equals("BIAS") && !strings4[0].equals("BIAS")
                                            && strings1[7].equals(strings3[7]) && strings2[7].equals(strings4[7])) {
                                        thisTheSamePriceList.add(strings3[11]);
                                        thisTheSamePriceList.add(strings4[11]);
                                        inTheSamePriceList.add(strings1[11]);
                                        inTheSamePriceList.add(strings2[11]);
                                    }
                                } else {
                                    if (!strings1[11].equals(strings2[11]) || !strings1[15].equals(strings2[15])) {
                                        result = false;
                                        break;
                                    } else {
                                        Collections.sort(thisTheSamePriceList);
                                        Collections.sort(inTheSamePriceList);

                                        for (String string : thisTheSamePriceList) {
                                            if (!string.equals(inTheSamePriceList
                                                    .get(thisTheSamePriceList.indexOf(string)))) {
                                                result = false;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (result) {
                            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                                    + "Нашел совпадения в рынке с ПАТТЕРНАМИ User передаю на сделку");
                            makeDeal(inArrayListString.get(0));
                            return;
                            // возможно тут надо поставить return
                        } else {
                            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                                    + "Совпадений с ПАТТЕРНАМИ USER не найдено");
                        }
                    }
                } else {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                            + " --- Паттернов равных по размеру нет");
                }
            }
        }
    }


    // Определяем какую сделку сделать и даем команду на ее исполнение
    private synchronized void makeDeal(String string) {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Определяю какую сделку сделать согласно ИНФО ПАТТЕРНАМ");
        String[] strings = string.split("===");

        if (Integer.parseInt(strings[1]) > Integer.parseInt(strings[3])) {
            String stringOut = string;

            if (Gasket.isTrading()) new TradeBuy(stringOut);
            new TestOrderBuyPatternUser(stringOut, Gasket.getBitmexQuote().getAskPrice());
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + stringOut + " --- Согластно ПАТТЕРНУ сделал сделку БАЙ USER");
        } else if (Integer.parseInt(strings[1]) < Integer.parseInt(strings[3])) {
            String stringOut = string;

            if (Gasket.isTrading()) new TradeSell(stringOut);
            new TestOrderSellPatternUser(stringOut, Gasket.getBitmexQuote().getBidPrice());
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + stringOut + " --- Согластно ПАТТЕРНУ сделал сделку СЕЛЛ USER");
        }
    }


    // удаляем листы размеры которых длиннее паттернов
    private synchronized void removeUnnecessaryLists() {
        if (listInListString.size() > 0) {
            int maxArraySize = savedPatternsUser.getMaxArraySizeUser();
            ArrayList<Integer> lineNumbersToDelete = new ArrayList<>();
            ArrayList<ArrayList<String>> arrayListArrayList = new ArrayList<>();

            for (int i = 0; i < listInListString.size(); i++) {
                if (listInListString.get(i).size() > maxArraySize) {
                    arrayListArrayList.add(listInListString.get(i));
                    lineNumbersToDelete.add(i);
                }
            }

            Collections.reverse(lineNumbersToDelete);
            // сохраняю удаленные патерны
            ReadAndSavePatternsUser.saveSavedPatternsDeleteUser(arrayListArrayList);

            for (Integer integer : lineNumbersToDelete) {
                listInListString.remove((int) integer);
            }
        }
    }


    // сортируем и наполняем лист сравнений листами строк
    // очищаем лист входящих объектов
    private synchronized void sortPrice(boolean b) {
        listInfoIndicatorWorkingCopy.sort(sortPriceComparatorUser);
        ArrayList<String> arrayList = new ArrayList<>(getListString(null));
        arrayList.add(0, "0\n");
        listInListString.add(0, arrayList);

        if (listInListString.size() > 1 && b) { // && flag) {
            for (ArrayList<String> arrayListString : listInListString) {

                if (listInListString.indexOf(arrayListString) != 0) {
                    String stringBias = "BIAS===" + getBias() + "\n";
                    arrayListString.add(stringBias);
//                    ArrayList<String> arrayListOut = new ArrayList<>(getListString(arrayListString));
                    arrayListString.clear();
                    arrayListString.addAll(getListString(arrayListString));
                }
            }
        }

        priceStart = Gasket.getBitmexQuote().getBidPrice();
        listInfoIndicatorWorkingCopy.clear();
        listInListString.sort(sortSizeUser);

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- В листе для сравнения уже - "
                + listInListString.size() + " - паттернов USER");
    }


    // объекты преобразовываем в строки а так же проверяем есть ли такие уровни,
    // если есть то удаляем их из входящего листа и меняем их в листе направлений
    private synchronized ArrayList<String> getListString(ArrayList<String> arrayListIn) {
        long timeNow = DatesTimes.getDateTerminalLong();
        ArrayList<InfoIndicator> infoIndicatorArrayList = new ArrayList<>(listInfoIndicatorWorkingCopy);
        ArrayList<String> inArrayList = null;

        if (arrayListIn != null) {
            inArrayList = new ArrayList<>(arrayListIn);
        } else {
            inArrayList = new ArrayList<>();
        }

        int count = 0;
        long time;

////////////////////////////////////////

        if (inArrayList.size() > 1) {
            //arrayList.addAll(arrayListIn);

            // находим еоличество BIAS
            for (String s : inArrayList) {
                if (s.startsWith("BIAS")) count++;
            }

            // согласно количеству BIAS находим максимальный нужный нам промежуток времени
            if (count >= 1) {
                time = timeNow - (1000 * 60 * (count + 1));
            } else {
                time = timeNow - (1000 * 60 * 6);
            }

            ArrayList<Integer> indexDelete = new ArrayList<>();

            // перебираем объекты и смотрим вписываются ли они в промежуток времени
            for (InfoIndicator infoIndicator : infoIndicatorArrayList) {

                // если не вписались в промежуток удаляем объект и прирываем этот цикл
                if (infoIndicator.getTime().getTime() < time) {
                    indexDelete.add(infoIndicatorArrayList.indexOf(infoIndicator));
                } else {
                    // сравниваем строки объекта с строками в списке
                    for (String string : inArrayList) {
                        String[] stringsIn = infoIndicator.toStringUser().split("===");
                        String[] stringsThis = string.split("===");

                        // если длина строки объекта и массива равны то ...
                        if (stringsIn.length == stringsThis.length) {

                            // если такая строка уже есть то заменяем ее на более новую
                            if (stringsIn[5].equals(stringsThis[5]) && stringsIn[7].equals(stringsThis[7])
                                    && stringsIn[11].equals(stringsThis[11]) && stringsIn[15].equals(stringsThis[15])) {

                                inArrayList.set(inArrayList.indexOf(string), infoIndicator.toStringUser());
                                indexDelete.add(infoIndicatorArrayList.indexOf(infoIndicator));
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
                infoIndicatorArrayList.remove((int) index);
            }
        }
        ////////////////////////////////////


        // определяем пределы последнего блока
        time = timeNow - (1000 * 60 * 6);

        // если еще остались строки, то добаляем их в последний блок
        if (infoIndicatorArrayList.size() > 0) {
            for (InfoIndicator infoIndicator : infoIndicatorArrayList) {
                if (infoIndicator.getTime().getTime() > time)
                    inArrayList.add(infoIndicator.toStringUser());
            }
        }

        return inArrayList;
    }



    // приводим паттерны в порядок
    // чистим от оставшихся предварительных исчезнувших уровняй
    private synchronized void setThePatternsInOrder() {
        // перебираем все листы листов
        for (ArrayList<String> inArrayList : listInListString) {
            // индекса строк в массиве которые надо будет удалить
            ArrayList<Integer> indexArrayList = new ArrayList<>();

            for (String stringOne : inArrayList) {
                int bias = 0;

                // если строка не ровна нулевой строке или промежуточной
                if (!stringOne.startsWith("0") && !stringOne.startsWith("BIAS")) {
                    // разбиваем строку на запчасти
                    String[] oneStrings = stringOne.split("===");
                    String[] twoStrings;

                    // начинаем шагать по тому же массиву и искать ближайший BIAS
                    for (int i = inArrayList.indexOf(stringOne) + 1; i < inArrayList.size(); i++) {
                        String stringTwo = inArrayList.get(i);

                        bias = bias + (stringTwo.startsWith("BIAS") ? 1 : 0);

                        if (bias == 1) {
                            // если мы сюда заши то значит мы перешли в нужны нам блок
                            // начинаем сравнения с его строками
                            twoStrings = stringTwo.split("===");

                            if (oneStrings.length == twoStrings.length) {

                                // эти уровни есть всегда их не надо уничтожать
                                if (!oneStrings[11].equals("OI_ZS_MIN_MINUS")
                                        && !oneStrings[11].equals("OI_ZS_MIN_PLUS")
                                        && !oneStrings[11].equals("DELTA_ZS_MIN_MINUS")
                                        && !oneStrings[11].equals("DELTA_ZS_MIN_PLUS")) {

                                                    /*
                0{"period": "M5",
                1"preview": "1",
                2"time": "2020-05-27 12:28:00",
                3"price": "9175.0",
                4"value": "2920763",
                5"type": "ASK",
                6"avg": "2871888",
                7"dir": "1",
                8"open": "9167.5",
                9"close": "9178.5",
                10"high": "9183.0",
                11"low": "9167.0"}


    0 period
    1 period.toString()
    2 ===preview=== +
    3 preview +
    4 "===time===" +
    5 dateFormat.format(time)
    6 "===price===" +
    7 price
    8 "===value===" +
    9 value +
    10 "===type===" +
    11 type.toString() +
    12 "===avg===" +
    13 avg
    14 "===dir===" +
    15 dir + "
    16 ===open===" +
    17 open + "
    18 ===close===" +
    19 close + "
    20 ===high===" +
    21 high
    22 ===low===" +
    23 low
                */
                                        // M5 == M5  1 == 1  ASK == ASK
                                    if (oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[3].equals(twoStrings[3])
                                            && oneStrings[11].equals(twoStrings[11])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                        // M5 == M5  1 != 1(0)  ASK == ASK
                                    } else if (oneStrings[1].equals(twoStrings[1])
                                            && (!oneStrings[3].equals(twoStrings[3])
                                            && oneStrings[3].equals("1"))
                                            && oneStrings[11].equals(twoStrings[11])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                        // M5 == M5  (0)1 != 1  ASK == ASK
                                    } else if (oneStrings[1].equals(twoStrings[1])
                                            && (!oneStrings[3].equals(twoStrings[3])
                                            && oneStrings[3].equals("0"))
                                            && oneStrings[11].equals(twoStrings[11])) {
                                        indexArrayList.add(inArrayList.indexOf(stringTwo));

                                        // M5 != M5(M15)  1 == 1  ASK == ASK
                                    } else if ((!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("M5"))
                                            && oneStrings[3].equals(twoStrings[3])
                                            && oneStrings[11].equals(twoStrings[11])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                        // M5 != M5(M15)  1 != 1(0)  ASK == ASK
                                    } else if ((!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("M5"))
                                            && (!oneStrings[3].equals(twoStrings[3])
                                            && oneStrings[3].equals("1"))
                                            && oneStrings[11].equals(twoStrings[11])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                        // M5 != M5(M15)  (0)1 != 1  ASK == ASK
                                    } else if ((!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("M5"))
                                            && (!oneStrings[3].equals(twoStrings[3])
                                            && oneStrings[3].equals("0"))
                                            && oneStrings[11].equals(twoStrings[11])) {
                                        indexArrayList.add(inArrayList.indexOf(stringTwo));

                                        // M5 != M5(M15)  (0)1 != 1  ASK == ASK
                                    } else if ((!oneStrings[1].equals(twoStrings[1])
                                            && twoStrings[1].equals("M5"))
                                            && (!oneStrings[3].equals(twoStrings[3])
                                            && oneStrings[3].equals("0"))
                                            && oneStrings[11].equals(twoStrings[11])) {
                                        indexArrayList.add(inArrayList.indexOf(stringTwo));
                                    }
                                }
                            }
                        } else if (bias == 2) {
                            break;
                        }
                    }
                }
            }

            // если каким-то образом будет два одинаковых индекса, так мы их нивилируем
            TreeSet<Integer> treeSet = new TreeSet<>(indexArrayList);

            indexArrayList.clear();
            indexArrayList.addAll(treeSet);
            Collections.reverse(indexArrayList);

            for (Integer index : indexArrayList) {
                inArrayList.remove((int) index);
            }
        }
    }



    // находим куда сместилась цена и другие данные
    private synchronized String getBias() {
        double bias = priceNow - priceStart;
        String stringOut;

        if (bias > 0) {
            stringOut = "BUY===" + bias;
        } else if (bias < 0) {
            stringOut = "SELL===" + bias;
        } else {
            stringOut = "NULL===0";
        }
        return stringOut;
    }


    // Проверяем что бы наши пакеты данных не выбивалис из пятиминутки
    private synchronized boolean isTime() {
        String string = DatesTimes.getDateTerminal();
        String[] strings = string.split(":");
        double seconds = Double.parseDouble(strings[1] + "." + strings[2]);

        if (seconds > 00.05 && seconds < 4.98) {
            return false;
        } else if (seconds > 5.05 && seconds < 9.98) {
            return false;
        } else if (seconds > 10.05 && seconds < 14.98) {
            return false;
        } else if (seconds > 15.05 && seconds < 19.98) {
            return false;
        } else if (seconds > 20.05 && seconds < 24.98) {
            return false;
        } else if (seconds > 25.05 && seconds < 29.98) {
            return false;
        } else if (seconds > 30.05 && seconds < 34.98) {
            return false;
        } else if (seconds > 35.05 && seconds < 39.98) {
            return false;
        } else if (seconds > 40.05 && seconds < 44.98) {
            return false;
        } else if (seconds > 45.05 && seconds < 49.98) {
            return false;
        } else if (seconds > 50.05 && seconds < 54.98) {
            return false;
        } else if (seconds > 55.05 && seconds < 59.98) {
            return false;
        } else {
            keepsTrackOfFillingListInfoIndicatorUser.setSleep();
            return true;
        }
    }






    /// === INNER CLASSES === ///



    private class SortPriceUser implements Comparator<InfoIndicator> {
        @Override
        public int compare(InfoIndicator o1, InfoIndicator o2) {
            double result = o2.getPrice() - o1.getPrice();
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }


    private class SortSizeUser implements Comparator<ArrayList<String>> {
        @Override
        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            double result = o1.size() - o2.size();
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }



    // следит за наполнением листа и если наполнение больше нет то сортирует его и запускает нужные методы
    private class KeepsTrackOfFillingListInfoIndicatorUser extends Thread {
        private volatile int sleep = 2;
        private int previousValue;


        public KeepsTrackOfFillingListInfoIndicatorUser() {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Внутренний класс KeepsTrackOfFillingListInfoIndicatorUser начал работать");
            this.previousValue = 0;
            start();
        }

        @Override
        public void run() {

            while (true) {
                int size;

                if (listInfoIndicator != null) {
                    size = listInfoIndicator.size();
                } else {
                    size = 0;
                }

                if (size > 0) {
                    if (previousValue == listInfoIndicator.size()) {
                        priceNow = Gasket.getBitmexQuote().getBidPrice();
                        previousValue = 0;
                        listSortedAndCompares();
                    } else {
                        previousValue = size;
                    }
                }

                try {
                    Thread.sleep(1000 * sleep);
                } catch (InterruptedException e) {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                            + " --- Не смог проснуться во внутреннем классе "
                            + "KeepsTrackOfFillingListInfoIndicator класса ListensToLooksAndFills - "
                            + " sleep = " + sleep);
                }
            }
        }
        private void setSleep() {
            sleep = Gasket.getSecondsSleepTime();
        }
    }
}
