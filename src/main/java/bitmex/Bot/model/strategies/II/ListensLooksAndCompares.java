package bitmex.Bot.model.strategies.II;


import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;


import java.text.SimpleDateFormat;
import java.util.*;





public class ListensLooksAndCompares {
    private static ListensLooksAndCompares listensLooksAndCompares;

    private ArrayList<InfoIndicator> listInfoIndicatorWorkingCopy;
    private ArrayList<ArrayList<String>> marketListInListString;
    private ArrayList<InfoIndicator> listInfoIndicator;

    private SortPriceRemainingLevels sortPriceRemainingLevels;
    private SortPrice sortPriceComparator;
    private SavedPatterns savedPatterns;
    private SortSize sortSize;


    private boolean stopStartFlag;
    private double priceStart;
    private double priceNow;
    private long timeNow;


    private ListensLooksAndCompares() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Класс ListensLooksAndCompares начал работать");

        this.sortPriceRemainingLevels = new SortPriceRemainingLevels();
        this.listInfoIndicatorWorkingCopy = new ArrayList<>();
        this.savedPatterns = Gasket.getSavedPatternsClass();
        this.marketListInListString = new ArrayList<>();
        this.sortPriceComparator = new SortPrice();
        this.listInfoIndicator = new ArrayList<>();
        Gasket.setListensLooksAndCompares(this);
        this.sortSize = new SortSize();
        this.priceStart = Float.NaN;
        this.priceNow = Float.NaN;
        this.stopStartFlag = true;
    }


    public static ListensLooksAndCompares getInstance() {
        if (listensLooksAndCompares == null) listensLooksAndCompares = new ListensLooksAndCompares();
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
            listInfoIndicatorWorkingCopy.addAll(listInfoIndicator);

            for (int i = listInfoIndicatorWorkingCopy.size() - 1; i > -1; i--) {
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

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Сравниваю рынок с ПАТТЕРНАМИ II");

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
        listInfoIndicatorWorkingCopy.sort(sortPriceComparator);

        if (b) {
            if (marketListInListString.size() > 0) {
                for (ArrayList<String> arrayListString : marketListInListString) {
                    String stringBias = "BIAS===" + getBias() + "===" + DatesTimes.getDateTerminal() + "\n";
                    arrayListString.add(stringBias);
                }
            }

            ArrayList<String> arrayListOut = new ArrayList<>();
            arrayListOut.add("0===" + DatesTimes.getDateTerminal() + "\n");
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

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- В листе для сравнения уже - "
                    + marketListInListString.size() + " - паттернов");

            marketListInListString.sort(sortSize);
            listInfoIndicatorWorkingCopy.clear();
        }
    }




    // объекты преобразовываем в строки а так же проверяем есть ли такие уровни,
    // если есть то удаляем их из входящего листа и меняем их в листе направлений
    private ArrayList<String> getListString(ArrayList<String> arrayListIn) {
        ArrayList<InfoIndicator> infoIndicatorArrayListWorking = new ArrayList<>(listInfoIndicatorWorkingCopy);
        ArrayList<InfoIndicator> residualArrayList = new ArrayList<>();
        ArrayList<String> inArrayList = new ArrayList<>(arrayListIn);
        ArrayList<Integer> indexDelete = new ArrayList<>();

        int count = 0;
        long time;


        if (inArrayList.size() > 0) {

                // находим количество BIAS
            for (String s : inArrayList) {
                if (s.startsWith("BIAS")) count++;
            }

                // согласно количеству BIAS находим максимальный нужный нам промежуток времени
            if (count >= 1) {
                time = timeNow - (1000 * 60 * 5 * (count + 1));
            } else {
                time = timeNow - (1000 * 60 * 6);
            }

                // перебираем объекты и смотрим вписываются ли они в промежуток времени
            for (InfoIndicator infoIndicator : infoIndicatorArrayListWorking) {

                    // если не вписались в промежуток удаляем объект и прирываем этот цикл
                if (infoIndicator.getTime().getTime() < time) {
                    indexDelete.add(infoIndicatorArrayListWorking.indexOf(infoIndicator));

                } else {
                        // сравниваем строки объекта с строками в списке
                    for (String string : inArrayList) {
                        String[] stringsIn = infoIndicator.toString().split(",");
                        String[] stringsThis = string.split(",");

                            // если длина строки объекта и массива равны то ...
                        if (stringsIn.length == stringsThis.length && stringsIn.length > 2) {

                                // если такая строка уже есть то заменяем ее на более новую
                            if (stringsIn[2].equals(stringsThis[2]) && stringsIn[3].equals(stringsThis[3])
                                    && stringsIn[5].equals(stringsThis[5])
                                    && stringsIn[7].equals(stringsThis[7])) {

                                inArrayList.set(inArrayList.indexOf(string), infoIndicator.toString());
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
        time = timeNow - (1000 * 60 * 6);

            // если еще остались строки, то добаляем их в последний блок
        if (infoIndicatorArrayListWorking.size() > 0) {
            for (InfoIndicator infoIndicator : infoIndicatorArrayListWorking) {
                if (infoIndicator.getTime().getTime() > time) {
                    inArrayList.add(infoIndicator.toString());
                } else {
                    residualArrayList.add(infoIndicator);
                }
            }
        }

        return residualArrayList.size() > 0
                ? new ArrayList<>(insertRemainingLevels(inArrayList, residualArrayList)) : inArrayList;
    }



    // приводим паттерны в порядок
    // чистим от оставшихся предварительных исчезнувших уровняй
    private synchronized void setThePatternsInOrder() {
        // перебираем все листы листов
        for (ArrayList<String> inArrayList : marketListInListString) {
            // индекса строк в массиве которые надо будет удалить
            ArrayList<Integer> indexArrayList = new ArrayList<>();

            for (String stringOne : inArrayList) {
                int bias = 0;

                // если строка не ровна нулевой строке или промежуточной
                if (!stringOne.startsWith("0") && !stringOne.startsWith("BIAS")) {
                    // разбиваем строку на запчасти
                    String[] oneStrings = stringOne.split(",");
                    String[] twoStrings;

                    // начинаем шагать по тому же массиву и искать ближайший BIAS
                    for (int i = inArrayList.indexOf(stringOne) + 1; i < inArrayList.size(); i++) {
                        String stringTwo = inArrayList.get(i);

                        bias = bias + (stringTwo.startsWith("BIAS") ? 1 : 0);

                        if (bias == 1) {
                            // если мы сюда заши то значит мы перешли в нужны нам блок
                            // начинаем сравнения с его строками
                            twoStrings = stringTwo.split(",");

                            if (oneStrings.length == twoStrings.length) {

                                // эти уровни есть всегда их не надо уничтожать
                                if (!oneStrings[5].equals("\"type\": \"OI_ZS_MIN_MINUS\"")
                                        && !oneStrings[5].equals("\"type\": \"OI_ZS_MIN_PLUS\"")
                                        && !oneStrings[5].equals("\"type\": \"DELTA_ZS_MIN_PLUS\"")
                                        && !oneStrings[5].equals("\"type\": \"DELTA_ZS_MIN_MINUS\"")) {

                                    // M5 == M5  1 == 1  ASK == ASK
                                    if (oneStrings[0].equals(twoStrings[0])
                                            && oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                        // M5 == M5  1 != 1(0)  ASK == ASK
                                    } else if (oneStrings[0].equals(twoStrings[0])
                                            && (!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("1"))
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                        // M5 == M5  (0)1 != 1  ASK == ASK
                                    } else if (oneStrings[0].equals(twoStrings[0])
                                            && (!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("0"))
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringTwo));

                                        // M5 != M5(M15)  1 == 1  ASK == ASK
                                    } else if ((!oneStrings[0].equals(twoStrings[0])
                                            && oneStrings[0].equals("M5"))
                                            && oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                        // M5 != M5(M15)  1 != 1(0)  ASK == ASK
                                    } else if ((!oneStrings[0].equals(twoStrings[0])
                                            && oneStrings[0].equals("M5"))
                                            && (!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("1"))
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                        // M5 != M5(M15)  (0)1 != 1  ASK == ASK
                                    } else if ((!oneStrings[0].equals(twoStrings[0])
                                            && oneStrings[0].equals("M5"))
                                            && (!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("0"))
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringTwo));

                                        // M5 != M5(M15)  (0)1 != 1  ASK == ASK
                                    } else if ((!oneStrings[0].equals(twoStrings[0])
                                            && twoStrings[0].equals("M5"))
                                            && (!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("0"))
                                            && oneStrings[5].equals(twoStrings[5])) {
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



    // тут мы находи нужное место расположение к вновь пришедшим уровням более длительной давности чем пять минут
    // при том их вообще могло и не быть, потому раньше мы их не заменили на более новые
    // и потом конечно же сортируем массив по новому
    private synchronized ArrayList<String> insertRemainingLevels(ArrayList<String> edit,
                                                                 ArrayList<InfoIndicator> additionalLevels) {

        ArrayList<InfoIndicator> inAdditionalLevels = new ArrayList<>(additionalLevels);
        ArrayList<String> intermediary = new ArrayList<>();
        ArrayList<String> inEdit = new ArrayList<>(edit);
        ArrayList<String> value = new ArrayList<>();
        ArrayList<Integer> key = new ArrayList<>();
        ArrayList<String> out = new ArrayList<>();


        // находим минимальную дату ниже которой у нас ничего нет
        String[] strings = inEdit.get(0).split("===");
        Date startData = new Date(getDate(strings[strings.length - 1]).getTime() - (5 * 60 * 1000));


        // перебираем пришедшие уровни и ищем куда бы их вставить
        for (InfoIndicator inAdditionalLevel : inAdditionalLevels) {
            Date date = inAdditionalLevel.getTime();

            // перебираем основные уровни паттернов
            for (String stringInEdit : inEdit) {

                // если это не первая и не промежуточная строка то находим ее время
                if (!stringInEdit.startsWith("0") && !stringInEdit.startsWith("BIAS")
                        && !stringInEdit.startsWith("BUY")) {
                    String[] stringsInEdit = stringInEdit.split(",");
                    Date date2 = getDate(stringsInEdit[2]);

                    // если дата входящих объектов больше или ровна дате старта то работаем с ней дальше
                    if (startData.getTime() <= date.getTime()) {
                        if (date.getTime() <= date2.getTime()) {
                            value.add(inAdditionalLevel.toString());
                            key.add(inEdit.indexOf(stringInEdit));
                        }
                    }
                }
            }
        }

        HashSet<String> hashSetValue = new HashSet<>(value);
        HashSet<Integer> hashSetKey = new HashSet<>(key);
        inAdditionalLevels.clear();
        value.clear();
        key.clear();

        value.addAll(hashSetValue);
        key.addAll(hashSetKey);

        Collections.reverse(value);
        Collections.reverse(key);

        for (int i = 0; i < value.size(); i++) {
            inEdit.add(key.get(i), value.get(i));
        }

        // сортируем по новому
        for (String string : inEdit) {
            if (string.startsWith("0")) {
                out.add(string);
            } else if (string.startsWith("BIAS")) {
                intermediary.sort(sortPriceRemainingLevels);
                intermediary.add(string);
                out.addAll(intermediary);
                intermediary.clear();
            } else if (inEdit.indexOf(string) == inEdit.size() - 1) {
                intermediary.add(string);
                intermediary.sort(sortPriceRemainingLevels);
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



    private Date getDate(String string) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String inString = string.replaceAll("\"", "").replaceAll("time: ", "");
        Date dateFromString = null;


        try {
            dateFromString = simpleDateFormat.parse(inString);
        } catch (Exception e) {
            ConsoleHelper.writeMessage("неверный формат даты --- " + inString);
        }
        return dateFromString;
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
            marketListInListString.clear();
            listInfoIndicator.clear();
        }
    }

/// === INNER CLASSES === ///




    private class SortPrice implements Comparator<InfoIndicator> {
        @Override
        public int compare(InfoIndicator o1, InfoIndicator o2) {
            double result = o2.getPrice() - o1.getPrice();

            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }



    private class SortSize implements Comparator<ArrayList<String>> {
        @Override
        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            double result = o1.size() - o2.size();

            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }



    private class SortPriceRemainingLevels implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            String[] strings1 = o1.split(",");
            String[] strings2 = o2.split(",");

            double result = Double.parseDouble(strings2[3].replaceAll("\"", "")
                    .replaceAll("price: ", "")) - Double.parseDouble(strings1[3]
                    .replaceAll("\"", "").replaceAll("price: ", ""));

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