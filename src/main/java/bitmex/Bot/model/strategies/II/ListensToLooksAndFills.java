package bitmex.Bot.model.strategies.II;

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
    private ArrayList<String> listStringPriceSell;      // лист для формирования селл паттерна
    private ArrayList<String> listStringPriceBuy;       // лист для формирования бай паттерна

    private volatile double priceEndSell;               // цена к которой должна прийти цена для фиксации паттерна
    private volatile double priceEndBuy;                // цена к которой должна прийти цена для фиксации паттерна
    private volatile double priceNow;                   // цена в данный момент

    private KeepsTrackOfFillingListInfoIndicator keepsTrackOfFillingListInfoIndicator;
    private CountPriseSell countPriseSell;
    private CountPriseBuy countPriseBuy;
    private SavedPatterns savedPatterns;
    private SortPrice sortPrice;

    private boolean oneStartFlag;


    private ListensToLooksAndFills() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Начал работать класс сбора Паттернов");

        this.keepsTrackOfFillingListInfoIndicator = new KeepsTrackOfFillingListInfoIndicator();
        this.priceNow = Gasket.getBitmexQuote().getBidPrice();
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
        listInfoIndicator.add(infoIndicator);
    }



    // отсыпаемся и начинаем работать
    private synchronized void listSorter() {
        listInfoIndicator.sort(sortPrice);
        boolean flag = isTime();

        if (priceEndBuy <= priceNow && !oneStartFlag) {
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
                        + "===MAX===" + getMaxDeviations(true);
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
                        + "===MAX===" + getMaxDeviations(false);
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
        listInfoIndicator.clear();
        oneStartFlag = false;
    }


    // добавляем строки в листы направлений
    private void addStringsInListDirections(boolean buyOrSell) {

        if (buyOrSell) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Добавляем строки в листы направлений Бай");

            for (InfoIndicator infoIndicator : listInfoIndicator) {
                listStringPriceBuy.add(infoIndicator.toString());
            }
        } else {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Добавляем строки в листы направлений Селл");

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
            ArrayList<Double> arrayList = new ArrayList<>(countPriseBuy.getArrayListBuy());

            for (Double d : arrayList) {
                if (count == 0) {
                    result = d;
                    count++;
                }
                result = Math.min(result, d);
            }

            if (result == 0) {
                System.out.println(countPriseBuy.getPriceStartBuy() + " - " + result
                        + " = " + (countPriseBuy.getPriceStartBuy() - result));

                return 0;
            } else {
                System.out.println(countPriseBuy.getPriceStartBuy() + " - " + result
                        + " = " + (countPriseBuy.getPriceStartBuy() - result));

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
                System.out.println(result + " - " + countPriseSell.getPriceStartSell()
                        + " = " + (result - countPriseSell.getPriceStartSell())); //тут тоже резулт иногда ноль

                return 0;
            } else {
                System.out.println(result + " - "
                        + countPriseSell.getPriceStartSell() + " = " + (result - countPriseSell.getPriceStartSell())); //тут тоже резулт иногда ноль
                return result - countPriseSell.getPriceStartSell();
            }
        }
    }


    // находим среднюю просадку
    private double getAverageDeviations(boolean b) {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- находим среднюю просадку");

        double result = 0;

        if (b) {
            ArrayList<Double> arrayList = countPriseBuy.getArrayListBuy();

            for (Double d : arrayList) {
                result = result + d;
            }

            if (result == 0) {
                System.out.println("---result==0");
                return 0;
            } else {
                System.out.println(countPriseBuy.getPriceStartBuy() - (result / arrayList.size()));
                return countPriseBuy.getPriceStartBuy() - (result / arrayList.size()); // резулт иногда ноль как итог вылетает НАН
            }
        } else {
            ArrayList<Double> arrayList = countPriseSell.getArrayListSell();

            for (Double d : arrayList) {
                result = result + d;
            }

            if (result == 0) {
                System.out.println("---result==0===2");
                return 0;
            } else {
                System.out.println((result / arrayList.size()) - countPriseSell.getPriceStartSell());
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




    // Проверяем что бы наши пакеты данных не выбивалис из пятиминутки
    private boolean isTime() {
        String string = DatesTimes.getDateTerminal();
        String[] strings = string.split(":");
        double seconds = Double.parseDouble(strings[1] + "." + strings[2]);

        if (seconds > 0.05 && seconds < 4.98) {
            return false;
        } else if (seconds > 5.20 && seconds < 9.98) {
            return false;
        } else if (seconds > 10.20 && seconds < 14.98) {
            return false;
        } else if (seconds > 15.20 && seconds < 19.98) {
            return false;
        } else if (seconds > 20.20 && seconds < 24.98) {
            return false;
        } else if (seconds > 25.20 && seconds < 29.98) {
            return false;
        } else if (seconds > 30.20 && seconds < 34.98) {
            return false;
        } else if (seconds > 35.20 && seconds < 39.98) {
            return false;
        } else if (seconds > 40.20 && seconds < 44.98) {
            return false;
        } else if (seconds > 45.20 && seconds < 49.98) {
            return false;
        } else if (seconds > 50.20 && seconds < 54.98) {
            return false;
        } else if (seconds > 55.20 && seconds < 59.98) {
            return false;
        } else {
            keepsTrackOfFillingListInfoIndicator.setSleep();
            return true;
        }
    }




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


    // следит за наполнением листа и если наполнение больше нет то сортирует его и запускает нужные методы
    private class KeepsTrackOfFillingListInfoIndicator extends Thread {
        private int previousValue;
        private int sleep = 2;

        public KeepsTrackOfFillingListInfoIndicator() {
            this.previousValue = 0;
            start();
        }

        @Override
        public void run() {

            while (true) {
                int size = listInfoIndicator.size();

                if (size > 0) {
                    if (previousValue == listInfoIndicator.size()) { // && isTime()) {
                        priceNow = Gasket.getBitmexQuote().getBidPrice();
                        previousValue = 0;
                        listSorter();
//                        sleep = 10;
                    } else {
                        previousValue = size;
                        sleep = 2;
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


//        // Проверяем что бы наши пакеты данных не выбивалис из пятиминутки
//        private boolean isTime() {
//            String string = DatesTimes.getDateTerminal();
//            String[] strings = string.split(":");
//            int seconds = Integer.parseInt(strings[2]);
//
//            if (strings[1].equals("00") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("05") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("10") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("15") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("20") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("25") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("30") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("35") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("40") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("45") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("50") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("55") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else {
//                return false;
//            }
//        }
    }



    // Фиксируем цену отклонения
    private class CountPriseBuy extends Thread {
        private ArrayList<Double> arrayListBuy;
        private ArrayList<Double> arrayListOut;
        private volatile boolean flag;
        private double priceStartBuy;

        public CountPriseBuy() {
            this.arrayListOut = new ArrayList<>();
            this.arrayListBuy = new ArrayList<>();
            this.flag = false;
            start();
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Начал фиксировать цену отклонения Бай");

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

        private void setPriceStartBuy(double priceStartBuy) {
            this.priceStartBuy = priceStartBuy;
            this.flag = true;
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
            this.arrayListSell = new ArrayList<>();
            this.arrayListOut = new ArrayList<>();
            this.flag = false;
            start();
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage( DatesTimes.getDateTerminal()
                    + " --- Начал фиксировать цену отклонения Селл");

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

        private void setPriceStartSell(double priceStartBuy) {
            this.priceStartSell = priceStartBuy;
            this.flag = true;
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
