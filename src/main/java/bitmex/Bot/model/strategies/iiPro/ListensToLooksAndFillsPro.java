package bitmex.Bot.model.strategies.iiPro;

import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.CompareHelper;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.util.Collections;
import java.util.ArrayList;
import java.util.TreeSet;


import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.*;
import static java.lang.Double.NaN;




public class ListensToLooksAndFillsPro {
    private static ListensToLooksAndFillsPro listensToLooksAndFillsPro;

    private ArrayList<InfoIndicator> listInfoIndicatorWorkingCopy;
    private ArrayList<InfoIndicator> listInfoIndicator;                // лист для входящих объектов
    private ArrayList<String> listStringPriceSell;                     // лист для формирования селл паттерна
    private ArrayList<String> listStringPriceBuy;                      // лист для формирования бай паттерна


    private SavedPatternsPro savedPatterns;
    private CountPriseSell countPriseSell;
    private CountPriseBuy countPriseBuy;


    private boolean stopStartFlag;
    private double priceEndSell;               // цена к которой должна прийти цена для фиксации паттерна
    private double priceEndBuy;                // цена к которой должна прийти цена для фиксации паттерна
    private double priceNow;                   // цена в данный момент
    private long timeNow;

    private volatile boolean oneStartFlag;



    private ListensToLooksAndFillsPro() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- " + "Начал работать класс сбора II Паттернов");
        this.priceNow = Gasket.getBitmexQuote().getBidPrice();
        this.listInfoIndicatorWorkingCopy = new ArrayList<>();
        this.savedPatterns = Gasket.getSavedPatternsProClass();
        this.listStringPriceSell = new ArrayList<>();
        this.listStringPriceBuy = new ArrayList<>();
        this.listInfoIndicator = new ArrayList<>();
        this.countPriseSell = new CountPriseSell();
        this.countPriseBuy = new CountPriseBuy();
        this.stopStartFlag = true;
        this.oneStartFlag = true;
        this.priceEndSell = NaN;
        this.priceEndBuy = NaN;
    }



    public static ListensToLooksAndFillsPro getInstance() {
        if (listensToLooksAndFillsPro == null) {
            listensToLooksAndFillsPro = new ListensToLooksAndFillsPro();
            Gasket.setListensToLooksAndFillsPro(listensToLooksAndFillsPro);
        }
        return listensToLooksAndFillsPro;
    }



    // принимаем объекты и если еще не запускали метод их обработки то запускаем его,
    // если он уже запущен то просто кладем объекты в массив
    // так же получаем текущую цену
    public void setInfoString(InfoIndicator infoIndicator) {
        if (stopStartFlag) {
            listInfoIndicator.add(infoIndicator);
        }
    }



    // отсыпаемся и начинаем работать
    private synchronized void listSorter(boolean flag) {
        listInfoIndicatorWorkingCopy.addAll(listInfoIndicator);

        for (int i = listInfoIndicatorWorkingCopy.size() - 1; i > -1; i--) {
            listInfoIndicator.remove(i);
        }

        listInfoIndicatorWorkingCopy.sort(CompareHelper.getSortPrice());

        if (priceEndBuy <= priceNow && !oneStartFlag && flag) {
            // если же нынешняя цена вышла за пределы планируемой цены то назначаем следующую желаемую цену движения
            // добавляем лист в стратегии,
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Добавляю лист в II ПАТТЕРН Бай");

            String stringZero = BUY.toString() + "===1===" + SELL.toString() + "===0===" + AVERAGE.toString()
                    + "===" + getAverageDeviations(true)
                    + "===" + MAX.toString() + "===" + getMaxDeviations(true)
                    + "===" + SIZE.toString() + "===" + (listStringPriceBuy.size() + 1)
                    + "===" + ID.toString() + "===" + ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39))
                    + "\n";

            listStringPriceBuy.add(0, stringZero);
            savedPatterns.addListsPricePatterns(listStringPriceBuy);
            // стираем и добавляем в него новые данные
            listStringPriceBuy.clear();
        } else {
            // добавляем строку данных о поведении цены в промежутке между поступлениями уровней
            if (!oneStartFlag && flag) {
                String stringBias = BIAS.toString() + "===" + getBias(true) + "===" + AVERAGE.toString()
                        + "===" + getAverageDeviations(true)
                        + "===" + MAX.toString() + "===" + getMaxDeviations(true)
                        + "===" + TIME.toString() + "===" + DatesTimes.getDateTerminal()
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
                    + " --- Добавляю лист в II Pro ПАТТЕРН Селл");

            String stringZero = BUY.toString() + "===0===" + SELL.toString() + "===1===" + AVERAGE.toString()
                    + "===" + getAverageDeviations(false)
                    + "===" + MAX.toString() + "===" + getMaxDeviations(false)
                    + "===" + SIZE.toString() + "===" + (listStringPriceSell.size() + 1)
                    + "===" + ID.toString() + "===" + ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39))
                    + "\n";

            listStringPriceSell.add(0, stringZero);
            savedPatterns.addListsPricePatterns(listStringPriceSell);
            listStringPriceSell.clear();
        } else {
            if (!oneStartFlag && flag) {
                String stringBias = BIAS.toString() + "===" + getBias(false) + "===" + AVERAGE.toString() + "==="
                        + getAverageDeviations(false)
                        + "===" + MAX.toString() + "===" + getMaxDeviations(false)
                        + "===" + TIME.toString() + "===" + DatesTimes.getDateTerminal()
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

        if (buyOrSell) {
            stringArrayListOut.addAll(checkIfThereAreSuchLevels(listStringPriceBuy));
            listStringPriceBuy.clear();
            listStringPriceBuy.addAll(stringArrayListOut);
        } else {
            stringArrayListOut.addAll(checkIfThereAreSuchLevels(listStringPriceSell));
            listStringPriceSell.clear();
            listStringPriceSell.addAll(stringArrayListOut);
        }
    }



    // объекты преобразовываем в строки а так же проверяем есть ли такие уровни,
    // если есть то удаляем их из входящего листа и меняем их в листе направлений
    private synchronized ArrayList<String> checkIfThereAreSuchLevels(ArrayList<String> arrayListIn) {

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
//                        String[] stringsIn = infoIndicator.toString().split(",");
//                        String[] stringsThis = string.split(",");

                        // если длина строки объекта и массива равны то ...
                        if (!string.startsWith(BUY.toString()) && !string.startsWith(NULL.toString())
                                && !string.startsWith(BIAS.toString())) {
//                        if (stringsIn.length == stringsThis.length) {

                            // если такая строка уже есть то заменяем ее на более новую
                            if (giveData(time, infoIndicator.toString()).equals(giveData(time, string))
                                    && giveData(price, infoIndicator.toString()).equals(giveData(price, string))
                                    && giveData(type, infoIndicator.toString()).equals(giveData(type, string))
                                    && giveData(dir, infoIndicator.toString()).equals(giveData(dir, string))) {
//                            if (stringsIn[2].equals(stringsThis[2]) && stringsIn[3].equals(stringsThis[3])
//                                    && stringsIn[5].equals(stringsThis[5])
//                                    && stringsIn[7].equals(stringsThis[7])) {

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
        times = timeNow - (1000 * 60 * 6);

        // если еще остались строки, то добаляем их в последний блок
        if (infoIndicatorArrayListWorking.size() > 0) {
            for (InfoIndicator infoIndicator : infoIndicatorArrayListWorking) {
                if (infoIndicator.getTime().getTime() > times) {
                    inArrayList.add(infoIndicator.toString());
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
        inAdditionalLevels.sort(CompareHelper.getSortTime());

        // вставляем оставшиеся объекты в нужный нам блок
        for (InfoIndicator marketInfo : inAdditionalLevels) {
            int index = -1;

            for (String patternString : inEdit) {

                if (!patternString.startsWith(NULL.toString())
                        && !patternString.startsWith(BUY.toString()) && !patternString.startsWith(BIAS.toString())) {
//                    String[] stringsPattern = patternString.split(",");

                    if (DatesTimes.getDate(giveData(time, patternString)).getTime() < marketInfo.getTime().getTime()) {
//                    if (DatesTimes.getDate(stringsPattern[2]).getTime() < marketInfo.getTime().getTime()) {
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
//                    long time = 0;
                    long time = DatesTimes.getDate(giveData(TIME, inEdit.get(index))).getTime() - (1000 * 60 * 5);

//                    for (int i = 0; i < stringBias.length; i++) {
//                        if (stringBias[i].equalsIgnoreCase(TIME.toString())) {
//                            time = DatesTimes.getDate(stringBias[i + 1]).getTime() - (1000 * 60 * 5);
//                            break;
//                        }
//                    }

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
            if (string.startsWith(BUY.toString())) {
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
                return 0;
            } else {
                return countPriseBuy.getPriceStartBuy() - result;
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
                return 0;
            } else {
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
                return 0;
            } else {
                return countPriseBuy.getPriceStartBuy() - (result / arrayList.size());
            }
        } else {
            ArrayList<Double> arrayList = countPriseSell.getArrayListSell();

            for (Double d : arrayList) {
                result = result + d;
            }

            if (result == 0) {
                return 0;
            } else {
                return (result / arrayList.size()) - countPriseSell.getPriceStartSell();
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
            stringOut = BUY.toString() + "===" + bias;
        } else if (bias < 0) {
            stringOut = SELL.toString() + "===" + bias;
        } else {
            stringOut = NULL.toString() + "===0";
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


    public void setStopStartFlag(boolean stopStartFlag) {
        this.stopStartFlag = stopStartFlag;

        if (!stopStartFlag) {
            listInfoIndicatorWorkingCopy.clear();
            listStringPriceSell.clear();
            listStringPriceBuy.clear();
            listInfoIndicator.clear();
        }
    }




/// === INNER CLASSES === ///



    // Фиксируем цену отклонения
    private class CountPriseBuy extends Thread {
        private ArrayList<Double> arrayListBuy;
        private ArrayList<Double> arrayListOut;
        private volatile boolean flag;
        private double priceStartBuy;


        public CountPriseBuy() {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Начал фиксировать цену отклонения II Pro Бай");
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
                            + "сласса ListensToLooksAndFillsPro");
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
                    + " --- Начал фиксировать цену отклонения II Pro Селл");
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
                            + "сласса ListensToLooksAndFillsPro");
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
