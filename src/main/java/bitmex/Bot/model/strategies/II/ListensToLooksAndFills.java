package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.util.Comparator;
import java.util.ArrayList;

import static java.lang.Double.NaN;


public class ListensToLooksAndFills {

    private static ListensToLooksAndFills listensToLooksAndFills;

    private ArrayList<InfoIndicator> listInfoIndicator; // лист для входящих объектов
    private ArrayList<Double> listPriceDeviationsSell;  // лист для отслеживания отклонения ценыдля селл комбинации
    private ArrayList<Double> listPriceDeviationsBuy;   // лист для отслеживания отклонения ценыдля бай комбинации
    private ArrayList<String> listStringPriceSell;      // лист для формирования селл паттерна
    private ArrayList<String> listStringPriceBuy;       // лист для формирования бай паттерна

    private volatile double priceStartOrderSell;        // цена от которой отслеживаем отклонения
    private volatile double priceStartOrderBuy;         // цена от которой отслеживаем отклонения
    private volatile double priceStartOrder;            // цена от которой отслеживаем отклонения
    private volatile double priceEndSell;               // цена к которой должна прийти цена для фиксации паттерна
    private volatile double priceEndBuy;                // цена к которой должна прийти цена для фиксации паттерна
    private volatile double priceNow;                   // цена в данный момент

    private KeepsTrackOfFillingListInfoIndicator keepsTrackOfFillingListInfoIndicator;
    private CountPriseSell countPriseSell;
    private CountPriseBuy countPriseBuy;
    private SavedPatterns savedPatterns;
    private BitmexQuote bitmexQuote;
    private SortPrice sortPrice;

    private volatile boolean averageFlagSell;           // флаг для подсчета средней цены отклонения и максимального отклонения
    private volatile boolean averageFlagBuy;            // флаг для подсчета средней цены отклонения и максимального отклонения
    private boolean oneStartFlag;


    private ListensToLooksAndFills() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Начал работать класс сбора Паттернов");

        this.keepsTrackOfFillingListInfoIndicator = new KeepsTrackOfFillingListInfoIndicator();
        this.savedPatterns = Gasket.getSavedPatternsClass();
        this.listPriceDeviationsSell = new ArrayList<>();
        this.listPriceDeviationsBuy = new ArrayList<>();
        this.listStringPriceSell = new ArrayList<>();
        this.listStringPriceBuy = new ArrayList<>();
        this.listInfoIndicator = new ArrayList<>();
        this.bitmexQuote = Gasket.getBitmexQuote();
        this.countPriseSell = new CountPriseSell();
        this.priceNow = bitmexQuote.getBidPrice();
        this.countPriseBuy = new CountPriseBuy();
        this.sortPrice = new SortPrice();
        this.priceStartOrderSell = NaN;
        this.priceStartOrderBuy = NaN;
        this.averageFlagSell = true;
        this.priceStartOrder = NaN;
        this.averageFlagBuy = true;
        this.oneStartFlag = true;
        this.priceEndSell = NaN;
        this.priceEndBuy = NaN;
        new CountPriseSell();
        new CountPriseBuy();
    }

    public static ListensToLooksAndFills getInstance() {
        if (listensToLooksAndFills == null) listensToLooksAndFills = new ListensToLooksAndFills();
        return listensToLooksAndFills;
    }



    // принимаем объекты и если еще не запускали метод их обработки то запускаем его,
    // если он уже запущен то просто кладем объекты в массив
    // так же получаем текущую цену
    public synchronized void setInfoString(InfoIndicator infoIndicator) {
        if (oneStartFlag) {
//            countPriseSell.setPriceStartSell(bitmexQuote.getBidPrice());
//            countPriseBuy.setPriceStartBuy(bitmexQuote.getAskPrice());
//            oneStartFlag = false;
        }
        listInfoIndicator.add(infoIndicator);
    }



    // отсыпаемся и начинаем работать
    private synchronized void listSorter() {

        averageFlagSell = false;
        averageFlagBuy = false;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }

        listInfoIndicator.sort(sortPrice);

        // сразу запоминаем цену для дальнейшего просчета отклонения
        if (Double.isNaN(priceStartOrder)) {
            priceStartOrder = priceNow;
        }

        // если цены финиша нет то назначаем ее
        if (Double.isNaN(priceEndBuy)) {
            priceStartOrderBuy = priceNow;
            priceEndBuy = priceNow + Gasket.getTakeForCollectingPatterns();

            sortPrice(true);
        } else if (priceEndBuy <= priceNow) {
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

            sortPrice(true);

            // стираем данные из листа цен отклонения
            listPriceDeviationsBuy.clear();

            priceStartOrderBuy = priceNow;
            priceEndBuy = priceNow + Gasket.getTakeForCollectingPatterns();
        } else {
            // добавляем строку данных о поведении цены в промежутке между поступлениями уровней
            String stringBias = "BIAS===" + getBias() + "===AVERAGE===" + getAverageDeviations(true)
                    + "===MAX===" + getMaxDeviations(true);
            listStringPriceBuy.add(stringBias);

            priceStartOrderBuy = priceNow;
            priceEndBuy = priceNow + Gasket.getTakeForCollectingPatterns();

            sortPrice(true);

            listPriceDeviationsBuy.clear();
        }

        // тоже самое только для комбиначии СЕЛЛ
        if (Double.isNaN(priceEndSell)) {
            priceStartOrderSell = priceNow;
            priceEndSell = priceNow - Gasket.getTakeForCollectingPatterns();

            sortPrice(false);
        } else if (priceEndSell >= priceNow) {
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

            sortPrice(false);

            listPriceDeviationsSell.clear();

            priceStartOrderSell = priceNow;
            priceEndSell = priceNow - Gasket.getTakeForCollectingPatterns();
        } else {
            String stringBias = "BIAS===" + getBias() + "===AVERAGE===" + getAverageDeviations(false)
                    + "===MAX===" + getMaxDeviations(false);
            listStringPriceBuy.add(stringBias);

            priceStartOrderSell = priceNow;
            priceEndSell = priceNow - Gasket.getTakeForCollectingPatterns();

            sortPrice(false);

            listPriceDeviationsSell.clear();
        }

        // очищаем лист входящих объектов
        priceStartOrder = priceNow;

        listInfoIndicator.clear();

        averageFlagSell = true;
        averageFlagBuy = true;
    }


    // Cортируем и добавляем строки в листы направлений
    private void sortPrice(boolean buyOrSell) {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- Добавляем строки в листы направлений");

        if (buyOrSell) {
            for (InfoIndicator infoIndicator : listInfoIndicator) {
                listStringPriceBuy.add(infoIndicator.toString());
            }
        } else {
            for (InfoIndicator infoIndicator : listInfoIndicator) {
                listStringPriceSell.add(infoIndicator.toString());
            }
        }
    }


    // находим максимальную просадку
    private double getMaxDeviations(boolean b) {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- находим максимальную просадку");
        double result = 0;
        int count = 0;

        if (b) {
            for (Double d : listPriceDeviationsBuy) {
                if (count == 0) {
                    result = d;
                    count++;
                }
                result = Math.min(result, d);
            }

            if (result == 0) {
                System.out.println(priceStartOrderBuy + " - " + result
                        + " = " + (priceStartOrderBuy - result));
                return 0;
            } else {
                System.out.println(priceStartOrderBuy + " - " + result
                        + " = " + (priceStartOrderBuy - result));
                return priceStartOrderBuy - result; // проверить что не то с результатом он равен нулю постоянно
            }

        } else {

            for (Double d : listPriceDeviationsSell) {
                if (count == 0) {
                    result = d;
                    count++;
                }
                result = Math.max(result, d);
            }

            if (result == 0) {
                System.out.println(result + " - " + priceStartOrderSell
                        + " = " + (result - priceStartOrderSell)); //тут тоже резулт иногда ноль
                return 0;
            } else {
                System.out.println(result + " - "
                        + priceStartOrderSell + " = " + (result - priceStartOrderSell)); //тут тоже резулт иногда ноль
                return result - priceStartOrderSell;
            }
        }
    }


    // находим среднюю просадку
    private double getAverageDeviations(boolean b) {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- находим среднюю просадку");
        double result = 0;
//        int count = 0;

        if (b) {
            for (Double d : listPriceDeviationsBuy) {
                result = result + d;
//                count++;
            }

            if (result == 0) {
                System.out.println(priceStartOrderBuy - (result / listPriceDeviationsBuy.size()) + "---result==0");
                return 0;
            } else {
                System.out.println(priceStartOrderBuy - (result / listPriceDeviationsBuy.size()));
                return priceStartOrderBuy - (result / listPriceDeviationsBuy.size()); // резулт иногда ноль как итог вылетает НАН
            }
        } else {
            for (Double d : listPriceDeviationsSell) {
                result = result + d;
//                count++;
            }

            if (result == 0) {
                System.out.println((result / listPriceDeviationsSell.size()) - priceStartOrderSell + "---result==0");
                return 0;
            } else {
                System.out.println((result / listPriceDeviationsSell.size()) - priceStartOrderSell);
                return (result / listPriceDeviationsSell.size()) - priceStartOrderSell; // резулт иногда ноль как итог вылетает НАН
            }
        }
    }



//    // находим максимальную просадку
//    private double getMaxDeviations(boolean b) {
//        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
//                + " --- находим максимальную просадку");
//        double result = 0;
//        int count = 0;
//
//        if (b) {
//            ArrayList<Double> arrayList = new ArrayList<>(countPriseBuy.getArrayListBuy());
//
//            for (Double d : arrayList) {
//                if (count == 0) {
//                    result = d;
//                    count++;
//                }
//                result = Math.min(result, d);
//            }
//
//            if (result == 0) {
//                System.out.println(countPriseBuy.getPriceStartBuy() + " - " + result
//                        + " = " + (countPriseBuy.getPriceStartBuy() - result));
//
//                return 0;
//            } else {
//                System.out.println(countPriseBuy.getPriceStartBuy() + " - " + result
//                        + " = " + (countPriseBuy.getPriceStartBuy() - result));
//
//                return countPriseBuy.getPriceStartBuy() - result; // проверить что не то с результатом он равен нулю постоянно
//            }
//
//        } else {
//            ArrayList<Double> arrayList = new ArrayList<>(countPriseSell.getArrayListSell());
//
//            for (Double d : arrayList) {
//                if (count == 0) {
//                    result = d;
//                    count++;
//                }
//                result = Math.max(result, d);
//            }
//
//            if (result == 0) {
//                System.out.println(result + " - " + countPriseSell.getPriceStartSell()
//                        + " = " + (result - countPriseSell.getPriceStartSell())); //тут тоже резулт иногда ноль
//
//                return 0;
//            } else {
//                System.out.println(result + " - "
//                        + countPriseSell.getPriceStartSell() + " = " + (result - countPriseSell.getPriceStartSell())); //тут тоже резулт иногда ноль
//                return result - countPriseSell.getPriceStartSell();
//            }
//        }
//    }
//
//
//    // находим среднюю просадку
//    private double getAverageDeviations(boolean b) {
//        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
//                + " --- находим среднюю просадку");
//
//        double result = 0;
////        int count = 0;
//
//        if (b) {
//            ArrayList<Double> arrayList = countPriseBuy.getArrayListBuy();
//
//            for (Double d : arrayList) {
//                result = result + d;
////                count++;
//            }
//
//            if (result == 0) {
//                System.out.println("---result==0");
//                return 0;
//            } else {
//                System.out.println(countPriseBuy.getPriceStartBuy() - (result / arrayList.size()));
//                return countPriseBuy.getPriceStartBuy() - (result / arrayList.size()); // резулт иногда ноль как итог вылетает НАН
//            }
//        } else {
//            ArrayList<Double> arrayList = countPriseSell.getArrayListSell();
//
//            for (Double d : arrayList) {
//                result = result + d;
////                count++;
//            }
//
//            if (result == 0) {
//                System.out.println((result / arrayList.size()) - countPriseSell.getPriceStartSell() + "---result==0");
//                return 0;
//            } else {
//                System.out.println((result / arrayList.size()) - countPriseSell.getPriceStartSell() + "---result==0");
//                return (result / arrayList.size()) - countPriseSell.getPriceStartSell(); // резулт иногда ноль как итог вылетает НАН
//            }
//        }
//    }



    // находим куда сместилась цена и другие данные
    private String getBias() {
        double bias = priceNow - priceStartOrder;
        String stringOut = "";

        if (bias > 0) {
            stringOut = "BUY===" + bias;
        } else if (bias < 0) {
            stringOut = "SELL===" + bias;
        } else {
            stringOut = "NULL===0";
        }
        return stringOut;
    }


//    // находим куда сместилась цена и другие данные
//    private String getBias(boolean b) {
//        String stringOut = "";
//        double bias;
//        if (b) {
//            bias = priceNow - countPriseBuy.getPriceStartBuy();
//        } else {
//            bias = priceNow - countPriseSell.getPriceStartSell();
//        }
//
//        if (bias > 0) {
//            stringOut = "BUY===" + bias;
//        } else if (bias < 0) {
//            stringOut = "SELL===" + bias;
//        } else {
//            stringOut = "NULL===0";
//        }
//        return stringOut;
//    }


    /// === INNER CLASSES === ///


    private class SortPrice implements Comparator<InfoIndicator> {
        @Override
        public int compare(InfoIndicator o1, InfoIndicator o2) {
            double result = o1.getPrice() - o2.getPrice();
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }


    // Фиксируем цену отклонения
    private class CountPriseBuy extends Thread {

        public CountPriseBuy() {
            start();
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Начал фиксировать цену отклонения Бай");

            while (true) {
                if (averageFlagBuy) {
                    if (!Double.isNaN(priceEndBuy) && !Double.isNaN(priceStartOrderBuy)) {
//                        if (priceEndBuy != 0 && priceStartOrderBuy != 0) {
                            double price = bitmexQuote.getBidPrice();

                            if (price < priceStartOrderBuy) {
                                listPriceDeviationsBuy.add(price);
                            }
//                        }
                    }
                    else {
                        ConsoleHelper.writeMessage("Работает NaN");
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
    }


    // Фиксируем цену отклонения
    private class CountPriseSell extends Thread {

        public CountPriseSell() {
            start();
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage( DatesTimes.getDateTerminal()
                    + " --- Начал фиксировать цену отклонения Селл");

            while (true) {
                if (averageFlagSell) {
                    if (!Double.isNaN(priceEndSell) && !Double.isNaN(priceStartOrderSell)) {
//                        if (priceEndSell != 0 && priceStartOrderSell != 0) {
                            double price = bitmexQuote.getAskPrice();

                            if (price > priceStartOrderSell) {
                                listPriceDeviationsSell.add(price);
                            }
//                        }
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
    }


    // следит за наполнением листа и если наполнение больше нет то сортирует его и запускает нужные методы
    private class KeepsTrackOfFillingListInfoIndicator extends Thread {
        public KeepsTrackOfFillingListInfoIndicator() {
            start();
        }

        @Override
        public void run() {

            int previousValue = 0;

            while (true) {
                int size = listInfoIndicator.size();
                int sleep = 3;

                if (size > 0) {
                    if (previousValue == listInfoIndicator.size()) {
                        priceNow = bitmexQuote.getBidPrice();
                        previousValue = 0;
                        listSorter();
                        sleep = 10;
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
    }






//    // следит за наполнением листа и если наполнение больше нет то сортирует его и запускает нужные методы
//    private class KeepsTrackOfFillingListInfoIndicator extends Thread {
//        private int previousValue;
//
//
//        public KeepsTrackOfFillingListInfoIndicator() {
//            this.previousValue = 0;
//            start();
//        }
//
//        @Override
//        public void run() {
//
//            while (true) {
//                int size = listInfoIndicator.size();
//                int sleep = 3;
//
//                if (size > 0) {
//                    if (previousValue == listInfoIndicator.size()) {
//                        priceNow = bitmexQuote.getBidPrice();
//                        previousValue = 0;
//                        listSortedAndCompares();
//                        sleep = 10;
//                    } else {
//                        previousValue = size;
//                    }
//                }
//
//                try {
//                    Thread.sleep(1000 * sleep);
//                } catch (InterruptedException e) {
//                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
//                            + " --- Не смог проснуться во внутреннем классе "
//                            + "KeepsTrackOfFillingListInfoIndicator класса ListensToLooksAndFills - "
//                            + " sleep = " + sleep);
//                }
//            }
//        }
//    }
//
//
//
//    // Фиксируем цену отклонения
//    private class CountPriseBuy extends Thread {
//        private ArrayList<Double> arrayListBuy;
//        private ArrayList<Double> arrayListOut;
//        private volatile boolean flag;
//        private double priceStartBuy;
//
//        public CountPriseBuy() {
//            this.arrayListOut = new ArrayList<>();
//            this.arrayListBuy = new ArrayList<>();
//            this.flag = false;
//            start();
//        }
//
//        @Override
//        public void run() {
//            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
//                    + " --- Начал фиксировать цену отклонения Бай");
//
//            while (true) {
//                if (flag) {
//                    double price = bitmexQuote.getBidPrice();
//
//                    if (price < priceStartBuy) {
//                        arrayListBuy.add(price);
//                    }
//                }
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
//                            + " --- Не смог проснуться в методе countPriseBuy() "
//                            + "сласса ListensToLooksAndFills");
//                }
//            }
//        }
//
//        private void setPriceStartBuy(double priceStartBuy) {
//            this.priceStartBuy = priceStartBuy;
//            flag = true;
//        }
//
//        private ArrayList<Double> getArrayListBuy() {
//            flag = false;
//            arrayListOut.addAll(arrayListBuy);
//            priceStartBuy = bitmexQuote.getAskPrice();
//            arrayListBuy.clear();
//            flag = true;
//            return arrayListOut;
//        }
//
//        private void clearList() {
//            priceStartBuy = bitmexQuote.getAskPrice();
//            arrayListOut.clear();
//        }
//
//        private double getPriceStartBuy() {
//            return priceStartBuy;
//        }
//    }
//
//
//    // Фиксируем цену отклонения
//    private class CountPriseSell extends Thread {
//        private ArrayList<Double> arrayListSell;
//        private ArrayList<Double> arrayListOut;
//        private double priceStartSell;
//        private boolean flag;
//
//        public CountPriseSell() {
//            this.arrayListSell = new ArrayList<>();
//            this.arrayListOut = new ArrayList<>();
//            this.flag = false;
//            start();
//        }
//
//        @Override
//        public void run() {
//            ConsoleHelper.writeMessage( DatesTimes.getDateTerminal()
//                    + " --- Начал фиксировать цену отклонения Селл");
//
//            while (true) {
//                if (flag) {
//                    double price = bitmexQuote.getBidPrice();
//
//                    if (price > priceStartSell) {
//                        arrayListSell.add(price);
//                    }
//                }
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
//                            + " --- Не смог проснуться в методе countPriseSell() "
//                            + "сласса ListensToLooksAndFills");
//                }
//            }
//        }
//
//        private void setPriceStartSell(double priceStartBuy) {
//            this.priceStartSell = priceStartBuy;
//            flag = true;
//        }
//
//        private ArrayList<Double> getArrayListSell() {
//            flag = false;
//            arrayListOut.addAll(arrayListSell);
//            priceStartSell = bitmexQuote.getAskPrice();
//            arrayListSell.clear();
//            flag = true;
//            return arrayListOut;
//        }
//
//        private void clearList() {
//            priceStartSell = bitmexQuote.getBidPrice();
//            arrayListOut.clear();
//        }
//
//        private double getPriceStartSell() {
//            return priceStartSell;
//        }
//    }

}
