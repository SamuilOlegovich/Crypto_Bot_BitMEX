package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.text.SimpleDateFormat;
import java.util.*;


import static java.lang.Double.NaN;



public class ListensToLooksAndFills {
    private static ListensToLooksAndFills listensToLooksAndFills;

    private ArrayList<InfoIndicator> listInfoIndicatorWorkingCopy;
    private ArrayList<InfoIndicator> listInfoIndicator;                // лист для входящих объектов
    private ArrayList<String> listStringPriceSell;                     // лист для формирования селл паттерна
    private ArrayList<String> listStringPriceBuy;                      // лист для формирования бай паттерна


    private double priceEndSell;               // цена к которой должна прийти цена для фиксации паттерна
    private double priceEndBuy;                // цена к которой должна прийти цена для фиксации паттерна
    private double priceNow;                   // цена в данный момент
    private long timeNow;


    private SortPriceRemainingLevels sortPriceRemainingLevels;
    private CountPriseSell countPriseSell;
    private CountPriseBuy countPriseBuy;
    private SavedPatterns savedPatterns;
    private SortPrice sortPrice;


    private volatile boolean oneStartFlag;



    private ListensToLooksAndFills() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Начал работать класс сбора Паттернов");

        this.sortPriceRemainingLevels = new SortPriceRemainingLevels();
        this.priceNow = Gasket.getBitmexQuote().getBidPrice();
        this.listInfoIndicatorWorkingCopy = new ArrayList<>();
        this.savedPatterns = Gasket.getSavedPatternsClass();
        this.listStringPriceSell = new ArrayList<>();
        this.listStringPriceBuy = new ArrayList<>();
        this.listInfoIndicator = new ArrayList<>();
        this.countPriseSell = new CountPriseSell();
        this.countPriseBuy = new CountPriseBuy();
        this.sortPrice = new SortPrice();
        this.oneStartFlag = true;
        this.priceEndSell = NaN;
        this.priceEndBuy = NaN;
    }


    public static ListensToLooksAndFills getInstance() {
        if (listensToLooksAndFills == null) listensToLooksAndFills = new ListensToLooksAndFills();
        return listensToLooksAndFills;
    }



    // принимаем объекты и если еще не запускали метод их обработки то запускаем его,
    // если он уже запущен то просто кладем объекты в массив
    // так же получаем текущую цену
    public void setInfoString(InfoIndicator infoIndicator) {
        listInfoIndicator.add(infoIndicator);
    }



    // отсыпаемся и начинаем работать
    private synchronized void listSorter(boolean flag) {
///////////////////// пересмотреть и доработать под новые реалии
        listInfoIndicatorWorkingCopy.addAll(listInfoIndicator);

        for (int i = listInfoIndicatorWorkingCopy.size() - 1; i > -1; i--) {
            listInfoIndicator.remove(i);
        }

        listInfoIndicatorWorkingCopy.sort(sortPrice);

        if (priceEndBuy <= priceNow && !oneStartFlag && flag) {
            // если же нынешняя цена вышла за пределы планируемой цены то назначаем следующую желаемую цену движения
            // добавляем лист в стратегии,
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                        + " --- Добавляю лист в ПАТТЕРН Бай");

            String stringZero = "BUY===1===SELL===0===AVERAGE===" + getAverageDeviations(true)
                        + "===MAX===" + getMaxDeviations(true)
                        + "===SIZE===" + (listStringPriceBuy.size() + 1)
                        + "===ID===" + ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39))
                        + "\n";

            listStringPriceBuy.add(0, stringZero);
            savedPatterns.addListsPricePatterns(listStringPriceBuy);
            // стираем и добавляем в него новые данные
            listStringPriceBuy.clear();
        } else {
            // добавляем строку данных о поведении цены в промежутке между поступлениями уровней
            if (!oneStartFlag && flag) {
                String stringBias = "BIAS===" + getBias(true) + "===AVERAGE===" + getAverageDeviations(true)
                        + "===MAX===" + getMaxDeviations(true)
                        + "===TIME===" + DatesTimes.getDateTerminal()
                        + "\n";
                listStringPriceBuy.add(stringBias);
            }
        }

        if (flag) {
            priceEndBuy = Gasket.getBitmexQuote().getAskPrice() + Gasket.getTakeForCollectingPatterns();
            countPriseBuy.clearList();
        }

        if (oneStartFlag) {
            priceEndBuy = Gasket.getBitmexQuote().getAskPrice() + Gasket.getTakeForCollectingPatterns();
        }

        addStringsInListDirections(true);



            // тоже самое только для комбиначии СЕЛЛ
        if (priceEndSell >= priceNow && !oneStartFlag) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                        + " --- Добавляю лист в ПАТТЕРН Селл");

            String stringZero = "BUY===0===SELL===1===AVERAGE===" + getAverageDeviations(false)
                        + "===MAX===" + getMaxDeviations(false)
                        + "===SIZE===" + (listStringPriceSell.size() + 1)
                        + "===ID===" + ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39))
                        + "\n";

            listStringPriceSell.add(0, stringZero);
            savedPatterns.addListsPricePatterns(listStringPriceSell);
            listStringPriceSell.clear();
        } else {
            if (!oneStartFlag && flag) {
                String stringBias = "BIAS===" + getBias(false) + "===AVERAGE===" + getAverageDeviations(false)
                        + "===MAX===" + getMaxDeviations(false)
                        + "===TIME===" + DatesTimes.getDateTerminal()
                        + "\n";
                listStringPriceSell.add(stringBias);
                countPriseSell.clearList();
            }
        }

        if (flag) {
            priceEndSell = Gasket.getBitmexQuote().getBidPrice() - Gasket.getTakeForCollectingPatterns();
            countPriseSell.clearList();
        }

        if (oneStartFlag) {
            priceEndSell = Gasket.getBitmexQuote().getBidPrice() - Gasket.getTakeForCollectingPatterns();
        }
        addStringsInListDirections(false);

            // очищаем лист входящих объектов
        listInfoIndicatorWorkingCopy.clear();
        oneStartFlag = false;
    }


    // добавляем строки в листы направлений
    private void addStringsInListDirections(boolean buyOrSell) {
        ArrayList<String> stringArrayListOut = new ArrayList<>();
//        ArrayList<InfoIndicator> arrayList = new ArrayList<>(checkIfThereAreSuchLevels(buyOrSell));


//        if (arrayList.size() > 0) {
            if (buyOrSell) {
                stringArrayListOut.addAll(checkIfThereAreSuchLevels(listStringPriceBuy));
                listStringPriceBuy.clear();
                listStringPriceBuy.addAll(stringArrayListOut);

//                for (InfoIndicator infoIndicator : arrayList) {
//                    listStringPriceBuy.add(infoIndicator.toString());
//
//                }
            } else {
                stringArrayListOut.addAll(checkIfThereAreSuchLevels(listStringPriceSell));
                listStringPriceSell.clear();
                listStringPriceSell.addAll(stringArrayListOut);


//                for (InfoIndicator infoIndicator : arrayList) {
//                    listStringPriceSell.add(infoIndicator.toString());
//                }
            }
//        }
    }


    // объекты преобразовываем в строки а так же проверяем есть ли такие уровни,
    // если есть то удаляем их из входящего листа и меняем их в листе направлений
    private synchronized ArrayList<String> checkIfThereAreSuchLevels(ArrayList<String> arrayListIn) {
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
                        if (stringsIn.length == stringsThis.length) {

                            // если такая строка уже есть то заменяем ее на более новую
                            if (stringsIn[2].equals(stringsThis[2]) && stringsIn[3].equals(stringsThis[3])
                                    && stringsIn[5].equals(stringsThis[5])
                                    && stringsIn[7].equals(stringsThis[7])) {

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
        time = timeNow - (1000 * 60 * 6);

        // если еще остались строки, то добаляем их в последний блок
        if (infoIndicatorArrayListWorking.size() > 0) {
            for (InfoIndicator infoIndicator : infoIndicatorArrayListWorking) {
                if (infoIndicator.getTime().getTime() > time) {
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
        ArrayList<String> value = new ArrayList<>();
        ArrayList<Integer> key = new ArrayList<>();
        ArrayList<String> out = new ArrayList<>();


        // находим минимальную дату ниже которой у нас ничего нет
        String stringDateStart = "";

        for (String string : inEdit) {
            if (string.startsWith("BIAS")) {
                String[] strings = inEdit.get(0).split("===");
                stringDateStart = "\"time\": \"" + strings[strings.length - 1] + "\"";
                break;
            }
        }

        Date startData = new Date(getDate(stringDateStart).getTime() - (1000 * 60 * 6));


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
                        //if ((startData != null ? startData.getTime() : date.getTime() + 1) <= date.getTime()) {
                        if (date.getTime() <= date2.getTime()) {
//                            hashMap.put(inEdit.indexOf(stringInEdit), inAdditionalLevel.toStringUser());
                            value.add(inAdditionalLevel.toStringUser());
                            key.add(inEdit.indexOf(stringInEdit));
//                            inEdit.add(inEdit.indexOf(stringInEdit), inAdditionalLevel.toStringUser());
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
            if (string.startsWith("BUY")) {
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
        String stringIn = string.replaceAll("\"", "").replace("time: ", "");
        Date dateFromString = null;

        try {
            dateFromString = simpleDateFormat.parse(stringIn);
        } catch (Exception e) {
            ConsoleHelper.writeMessage("неверный формат даты --- " + stringIn);
        }
        return dateFromString;
    }









//    // проверяем есть ли такие уровни, если есть то удаляем их из входящего листа и меняем их в листе направлений
//    private ArrayList<InfoIndicator> checkIfThereAreSuchLevels(boolean buyOrSell) {
//        ArrayList<InfoIndicator> arrayList = new ArrayList<>(listInfoIndicatorWorkingCopy);
//        long timeNow = DatesTimes.getDateTerminalLong();
//        int count = 0;
//        long time;
//
//        if (buyOrSell) {
//
//            for (String s : listStringPriceBuy) {
//                if (s.startsWith("BIAS")) count++;
//            }
//
//            if (count != 0) {
//                time = timeNow - (1000 * 60 * 5 * (count + 1));
//            } else {
//                time = timeNow - (1000 * 60 * 6);
//            }
//
//            for (int i = arrayList.size() - 1; i > -1; i--) {
//                InfoIndicator infoIndicator = arrayList.get(i);
//
//                if (infoIndicator.getTime().getTime() <= time) {
//                    arrayList.remove(i);
//                    break;
//                }
//
//                for (int j = listStringPriceBuy.size() - 1; j > -1; j--) {
//                    String[] stringsIn = infoIndicator.toString().split(",");
//                    String[] stringsThis = listStringPriceBuy.get(j).split(",");
//
//                    if (stringsIn.length == stringsThis.length) {
//                        if (stringsIn[2].equals(stringsThis[2]) && stringsIn[3].equals(stringsThis[3])
//                                && stringsIn[5].equals(stringsThis[5]) && stringsIn[7].equals(stringsThis[7])) {
//                            listStringPriceBuy.set(j, infoIndicator.toString());
//                            arrayList.remove(i);
//                            break;
//                        }
//                    }
//                }
//            }
//        } else {
//            for (String s : listStringPriceSell) {
//                if (s.startsWith("BIAS")) count++;
//            }
//
//            if (count != 0) {
//                time = timeNow - (1000 * 60 * 5 * (count + 1));
//            } else {
//                time = timeNow - (1000 * 60 * 6);
//            }
//
//            for (int i = arrayList.size() - 1; i > -1; i--) {
//                InfoIndicator infoIndicator = arrayList.get(i);
//
//                if (infoIndicator.getTime().getTime() < time) {
//                    arrayList.remove(i);
//                    break;
//                }
//
//                for (int j = listStringPriceSell.size() - 1; j > -1; j--) {
//                    String[] stringsIn = infoIndicator.toString().split(",");
//                    String[] stringsThis = listStringPriceSell.get(j).split(",");
//
//                    if (stringsIn.length == stringsThis.length) {
//                        if (stringsIn[2].equals(stringsThis[2]) && stringsIn[3].equals(stringsThis[3])
//                                && stringsIn[5].equals(stringsThis[5]) && stringsIn[7].equals(stringsThis[7])) {
//
//                            listStringPriceSell.set(j, infoIndicator.toString());
//                            arrayList.remove(i);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        time = timeNow - (1000 * 60 * 6);
//
//        for (int i = arrayList.size() - 1; i > -1; i--) {
//            if (arrayList.get(i).getTime().getTime() < time) {
//                arrayList.remove(i);
//            }
//        }
//
//        return arrayList;
//    }



    // находим максимальную просадку
    private double getMaxDeviations(boolean b) {
        double result = 0;
        int count = 0;

        if (b) {
            ArrayList<Double> arrayList = new ArrayList<>(countPriseBuy.getArrayListBuy());

            for (Double d : arrayList) {
                if (count == 0) {
                    result = d;
                    count++;
                }
                result = Math.min(result, d);
            }

            if (result == 0) {
                /////////////////////////////////
                System.out.println(countPriseBuy.getPriceStartBuy() + " - " + result
                        + " = " + (countPriseBuy.getPriceStartBuy() - result));
                /////////////////////////////////
                return 0;
            } else {
                //////////////////////////////////
                System.out.println(countPriseBuy.getPriceStartBuy() + " - " + result
                        + " = " + (countPriseBuy.getPriceStartBuy() - result));
                /////////////////////////////////
                return countPriseBuy.getPriceStartBuy() - result; // проверить что не то с результатом он равен нулю постоянно
            }

        } else {
            ArrayList<Double> arrayList = new ArrayList<>(countPriseSell.getArrayListSell());

            for (Double d : arrayList) {
                if (count == 0) {
                    result = d;
                    count++;
                }
                result = Math.max(result, d);
            }

            if (result == 0) {
                /////////////////////////////
                System.out.println(result + " - " + countPriseSell.getPriceStartSell()
                        + " = " + (result - countPriseSell.getPriceStartSell())); //тут тоже резулт иногда ноль
                ////////////////////////////
                return 0;
            } else {
                //////////////////////////
                System.out.println(result + " - "
                        + countPriseSell.getPriceStartSell() + " = " + (result - countPriseSell.getPriceStartSell())); //тут тоже резулт иногда ноль
                //////////////////////////
                return result - countPriseSell.getPriceStartSell();
            }
        }
    }


    // находим среднюю просадку
    private double getAverageDeviations(boolean b) {
        double result = 0;

        if (b) {
            ArrayList<Double> arrayList = countPriseBuy.getArrayListBuy();

            for (Double d : arrayList) {
                result = result + d;
            }

            if (result == 0) {
                ////////////////////////////////////
                System.out.println("---result==0");
                ///////////////////////////////////
                return 0;
            } else {
                //////////////////////////////////
                System.out.println(countPriseBuy.getPriceStartBuy() - (result / arrayList.size()));
                /////////////////////////////////
                return countPriseBuy.getPriceStartBuy() - (result / arrayList.size()); // резулт иногда ноль как итог вылетает НАН
            }
        } else {
            ArrayList<Double> arrayList = countPriseSell.getArrayListSell();

            for (Double d : arrayList) {
                result = result + d;
            }

            if (result == 0) {
                ///////////////////////////////////////
                System.out.println("---result==0===2");
                //////////////////////////////////////
                return 0;
            } else {
                ///////////////////////////////////////////////
                System.out.println((result / arrayList.size()) - countPriseSell.getPriceStartSell());
                //////////////////////////////////////////////
                return (result / arrayList.size()) - countPriseSell.getPriceStartSell(); // резулт иногда ноль как итог вылетает НАН
            }
        }
    }



    // находим куда сместилась цена и другие данные
    private String getBias(boolean b) {
        String stringOut = "";
        double bias;

        if (b) {
            bias = priceNow - countPriseBuy.getPriceStartBuy();
        } else {
            bias = priceNow - countPriseSell.getPriceStartSell();
        }

        if (bias > 0) {
            stringOut = "BUY===" + bias;
        } else if (bias < 0) {
            stringOut = "SELL===" + bias;
        } else {
            stringOut = "NULL===0";
        }
        return stringOut;
    }


    protected void startListSorter(boolean b) {
        listSorter(b);
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


    private class SortPriceRemainingLevels implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            String[] strings1 = o1.split(",");
            String[] strings2 = o2.split(",");

            double result = Double.parseDouble(strings2[3].replaceAll("\"", "")
                    .replaceAll("price: ", ""))
                    - Double.parseDouble(strings1[3].replaceAll("\"", "")
                    .replaceAll("price: ", ""));
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }



    // Фиксируем цену отклонения
    private class CountPriseBuy extends Thread {
        private ArrayList<Double> arrayListBuy;
        private ArrayList<Double> arrayListOut;
        private volatile boolean flag;
        private double priceStartBuy;

        public CountPriseBuy() {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Начал фиксировать цену отклонения Бай");
            this.arrayListOut = new ArrayList<>();
            this.arrayListBuy = new ArrayList<>();
            this.flag = false;
            start();
        }

        @Override
        public void run() {

            while (true) {
                if (flag) {
                    double price = Gasket.getBitmexQuote().getBidPrice();

                    if (price < priceStartBuy) {
                        this.arrayListBuy.add(price);
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                            + " --- Не смог проснуться в методе countPriseBuy() "
                            + "сласса ListensToLooksAndFills");
                }
            }
        }

        private ArrayList<Double> getArrayListBuy() {
            this.flag = false;
            this.arrayListOut.addAll(arrayListBuy);
            this.arrayListBuy.clear();
            return this.arrayListOut;
        }

        private void clearList() {
            this.priceStartBuy = Gasket.getBitmexQuote().getAskPrice();
            this.arrayListOut.clear();
            this.flag = true;
        }

        private double getPriceStartBuy() {
            return this.priceStartBuy;
        }
    }


    // Фиксируем цену отклонения
    private class CountPriseSell extends Thread {
        private ArrayList<Double> arrayListSell;
        private ArrayList<Double> arrayListOut;
        private double priceStartSell;
        private boolean flag;

        public CountPriseSell() {
            ConsoleHelper.writeMessage( DatesTimes.getDateTerminal()
                    + " --- Начал фиксировать цену отклонения Селл");
            this.arrayListSell = new ArrayList<>();
            this.arrayListOut = new ArrayList<>();
            this.flag = false;
            start();
        }

        @Override
        public void run() {

            while (true) {
                if (flag) {
                    double price = Gasket.getBitmexQuote().getBidPrice();

                    if (price > priceStartSell) {
                        this.arrayListSell.add(price);
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                            + " --- Не смог проснуться в методе countPriseSell() "
                            + "сласса ListensToLooksAndFills");
                }
            }
        }

        private ArrayList<Double> getArrayListSell() {
            this.flag = false;
            this.arrayListOut.addAll(arrayListSell);
            this.arrayListSell.clear();
            return this.arrayListOut;
        }

        private void clearList() {
            this.priceStartSell = Gasket.getBitmexQuote().getBidPrice();
            this.arrayListOut.clear();
            this.flag = true;
        }

        private double getPriceStartSell() {
            return this.priceStartSell;
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