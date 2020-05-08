package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;



public class ListensToLooksAndFills {
    private static ListensToLooksAndFills listensToLooksAndFills;

    private ArrayList<InfoIndicator> listInfoIndicator;
    private ArrayList<Double> listPriceDeviationsSell;
    private ArrayList<Double> listPriceDeviationsBuy;
    private ArrayList<String> listStringPriceSell;
    private ArrayList<String> listStringPriceBuy;
    private SavedPatterns savedPatterns;

    private volatile double priceStartOrderSell;
    private volatile double priceStartOrderBuy;
    private volatile double priceEndSell;
    private volatile double priceEndBuy;
    private volatile double priceTake;
    private volatile double priceNow;

    private volatile boolean averageFlagSell;
    private volatile boolean averageFlagBuy;


    private ListensToLooksAndFills() {
        ConsoleHelper.writeMessage("Начал работать класс сбора Паттернов ---- "
                + DatesTimes.getDateTerminal());
        this.savedPatterns = Gasket.getSavedPatternsClass();
        this.listPriceDeviationsSell = new ArrayList<>();
        this.listPriceDeviationsBuy = new ArrayList<>();
        this.listStringPriceSell = new ArrayList<>();
        this.listStringPriceBuy = new ArrayList<>();
        this.listInfoIndicator = new ArrayList<>();
        new KeepsTrackOfFillingListInfoIndicator();
        this.priceStartOrderSell = 00.0;
        this.priceStartOrderBuy = 00.0;
        this.averageFlagSell = true;
        this.averageFlagBuy = true;
        this.priceEndSell = 00.0;
        this.priceEndBuy = 00.0;
        this.priceTake = 3.0;
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
        ConsoleHelper.writeMessage("listSorter() - работает - "
                    + DatesTimes.getDateTerminal());

        averageFlagSell = false;
        averageFlagBuy = false;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }

        Comparator sortPriceComparator = new SortPrice();
        Collections.sort(listInfoIndicator, sortPriceComparator);


        //////////////////////////////////////////////////////////
        StringBuilder string = new StringBuilder();
        for (InfoIndicator infoIndicator : listInfoIndicator) {
            string.append(infoIndicator.toString());
        }
        System.out.println("\n" + DatesTimes.getDateTerminal() + " --- Пришел массив длиной - " + listInfoIndicator.size());
        System.out.println(string.toString() + "\n" + "\n");
        ///////////////////////////////////////////////////////////

        // если цены финиша нет то назначаем ее
        if (priceEndBuy == 0) {
            priceEndBuy = priceNow + priceTake;
            priceStartOrderSell = priceNow;
            priceStartOrderBuy = priceNow;

            if (listStringPriceBuy.size() == 0) {
                listStringPriceBuy.add("BUY===1===SELL===0===AVERAGE===");
            }

            if (listStringPriceSell.size() == 0) {
                listStringPriceSell.add("BUY===0===SELL===1===AVERAGE===");
            }
        } else if (priceEndBuy <= priceNow) {
            // если же нынешняя цена вышла за пределы планируемой цены то назначаем следующую желаемую цену движения
            // добавляем лист в стратегии,
            ConsoleHelper.writeMessage("Добавляю лист в ПАТТЕРН Бай ---- "
                    + DatesTimes.getDateTerminal());
            String stringZero = listStringPriceBuy.get(0) + getAverageDeviations(true)
                    + "===MAX===" + getMaxDeviations(true);
            ConsoleHelper.writeMessage(stringZero + " ===SIZE=== " + listStringPriceBuy.size());
            listStringPriceBuy.set(0, stringZero);
            ConsoleHelper.writeMessage("Отправил лист длиной - " + listStringPriceBuy.size());
            savedPatterns.addListsPricePatterns(listStringPriceBuy);
            // стираем данные из листа цен отклонения
            listPriceDeviationsBuy.clear();
            // стираем и добавляем в него новые данные
            listStringPriceBuy.clear();
            // добавляем в начало листа метку что делать при совпадения патерна
            listStringPriceBuy.add("BUY===1===SELL===0===AVERAGE===");
            sortPrice(true);
            priceStartOrderBuy = priceNow;
            priceEndBuy = priceNow + priceTake;
        } else {
            // добавляем в лист метку что делать при совпадения патерна
            ConsoleHelper.writeMessage("Добавляю в лист нулевую строку - "
                    + DatesTimes.getDateTerminal());
            if (listStringPriceBuy.size() == 0) {
                listStringPriceBuy.add("BUY===1===SELL===0===AVERAGE===");
            }
            priceStartOrderBuy = priceNow;
            priceEndBuy = priceNow + priceTake;
            sortPrice(true);
        }

        // тоже самое только для комбиначии СЕЛЛ
        if (priceEndSell == 0) {
            priceEndSell = priceNow - priceTake;
        } else if (priceEndSell >= priceNow) {
            ConsoleHelper.writeMessage("Добавляю лист в ПАТТЕРН Селл ---- "
                    + DatesTimes.getDateTerminal());
            String stringZero = listStringPriceSell.get(0) + getAverageDeviations(false)
                    + "===MAX===" + getMaxDeviations(false);
            ConsoleHelper.writeMessage(stringZero + " ===SIZE=== " + listPriceDeviationsSell.size());
            listStringPriceSell.set(0, stringZero);
            savedPatterns.addListsPricePatterns(listStringPriceSell);
            listPriceDeviationsSell.clear();
            listStringPriceSell.clear();
            listStringPriceSell.add("BUY===0===SELL===1===AVERAGE===");
            sortPrice(false);
            averageFlagSell = true;
            priceStartOrderSell = priceNow;
            priceEndSell = priceNow - priceTake;
        } else {
            if (listStringPriceSell.size() == 0) {
                listStringPriceSell.add("BUY===0===SELL===1===AVERAGE===");
            }
            priceStartOrderSell = priceNow;
            priceEndSell = priceNow - priceTake;
            sortPrice(false);
        }

        // очищаем лист входящих объектов
        listInfoIndicator.clear();
        averageFlagSell = true;
        averageFlagBuy = true;
        ConsoleHelper.writeMessage(listInfoIndicator.size() + "--- listInfoIndicator");
    }


    // Cортируем и добавляем строки в листы направлений
    private void sortPrice(boolean buyOrSell) {
        ConsoleHelper.writeMessage("Добавляем строки в листы направлений - "
                + DatesTimes.getDateTerminal());

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
        ConsoleHelper.writeMessage("находим максимальную просадку - "
                + DatesTimes.getDateTerminal());
        double result = 0;

        if (b) {
            for (Double d : listPriceDeviationsBuy) {
                result = Math.min(result, d);
            }

            if (result == 0) {
                System.out.println(priceStartOrderBuy + " - " + result + " = " + (priceStartOrderBuy - result));
                return 0;
            } else {
                System.out.println(priceStartOrderBuy + " - " + result + " = " + (priceStartOrderBuy - result));
                return priceStartOrderBuy - result; // проверить что не то с результатом он равен нулю постоянно
            }
        } else {
            for (Double d : listPriceDeviationsSell) {
                result = Math.max(result, d);
            }

            if (result == 0) {
                System.out.println(result + " - " + priceStartOrderSell + " = " + (result - priceStartOrderSell)); //тут тоже резулт иногда ноль
                return 0;
            } else {
                System.out.println(result + " - " + priceStartOrderSell + " = " + (result - priceStartOrderSell)); //тут тоже резулт иногда ноль
                return result - priceStartOrderSell;
            }
        }
    }


    // находим среднюю просадку
    private double getAverageDeviations(boolean b) {
        ConsoleHelper.writeMessage("находим среднюю просадку - "
                + DatesTimes.getDateTerminal());
        double result = 0.0;
        int count = 0;

        if (b) {
            for (Double d : listPriceDeviationsBuy) {
                result = result + d;
                count++;
            }

            if (result == 0) {
                System.out.println(result/count);
                return 0;
            } else {
                System.out.println(result/count);
                return result / count; // резулт иногда ноль как итог вылетает НАН
            }
        } else {
            for (Double d : listPriceDeviationsSell) {
                result = result + d;
                count++;
            }

            if (result == 0) {
                System.out.println(result / count);
                return 0;
            } else {
                System.out.println(result / count);
                return result / count; // резулт иногда ноль как итог вылетает НАН
            }
        }
    }



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
            ConsoleHelper.writeMessage("Начал фиксировать цену отклонения Бай ---- "
                    + DatesTimes.getDateTerminal());

            while (true) {
                if (averageFlagBuy) {
                    if (priceEndBuy != 0 && priceStartOrderBuy != 0) {
                        double price = Gasket.getBitmexQuote().getBidPrice();

                        if (price < priceStartOrderBuy) {
                            listPriceDeviationsBuy.add(price);
                        }
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    ConsoleHelper.writeMessage("Не смог проснуться в методе countPriseBuy() "
                            + "сласса ListensToLooksAndFills ---- " + DatesTimes.getDateTerminal());
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
            ConsoleHelper.writeMessage("Начал фиксировать цену отклонения Селл ---- "
                    + DatesTimes.getDateTerminal());

            while (true) {
                if (averageFlagSell) {
                    if (priceEndSell != 0 && priceStartOrderSell != 0) {
                        double price = Gasket.getBitmexQuote().getBidPrice();

                        if (price > priceStartOrderSell) {
                            listPriceDeviationsSell.add(price);
                        }
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    ConsoleHelper.writeMessage("Не смог проснуться в методе countPriseSell() "
                            + "сласса ListensToLooksAndFills ---- " + DatesTimes.getDateTerminal());
                }
            }
        }
    }


    // следит за наполнением листа и если наполнение больше нет то сортирует его и запускает нужные методы
    private class KeepsTrackOfFillingListInfoIndicator extends Thread {
        public KeepsTrackOfFillingListInfoIndicator() {
            ConsoleHelper.writeMessage("Начал свою работу класс KeepsTrackOfFillingListInfoIndicator"
                    + " ---- " + DatesTimes.getDateTerminal());
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
                        priceNow = Gasket.getBitmexQuote().getBidPrice();
                        previousValue = 0;
                        listSorter();
                        sleep = 60;
                    } else {
                        previousValue = size;
                    }
                }

                try {
                    Thread.sleep(1000 * sleep);
                } catch (InterruptedException e) {
                    ConsoleHelper.writeMessage("Не смог проснуться во внутреннем классе "
                            + "KeepsTrackOfFillingListInfoIndicator класса ListensToLooksAndFills - "
                            + " sleep = " + sleep + " ---- " + DatesTimes.getDateTerminal());
                    e.printStackTrace();
                }
            }
        }
    }
}
