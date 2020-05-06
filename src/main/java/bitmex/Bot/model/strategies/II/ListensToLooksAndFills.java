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
    private double priceEndSell;
    private double priceEndBuy;
    private double priceTake;
    private double priceNow;

    private boolean averageFlagSell;
    private boolean averageFlagBuy;
    private boolean timeFlag;


    private ListensToLooksAndFills() {
        System.out.println(1);
        ConsoleHelper.writeMessage("Начал работать класс сбора Паттернов ---- "
                + DatesTimes.getDateTerminal());
        this.savedPatterns = Gasket.getSavedPatternsClass();
        this.listPriceDeviationsSell = new ArrayList<>();
        this.listPriceDeviationsBuy = new ArrayList<>();
        this.listStringPriceSell = new ArrayList<>();
        this.listStringPriceBuy = new ArrayList<>();
        this.listInfoIndicator = new ArrayList<>();
        System.out.println(2);
        this.averageFlagSell = true;
        this.averageFlagBuy = true;
        this.priceEndSell = 00.0;
        this.priceEndBuy = 00.0;
        this.priceTake = 3.0;
        this.timeFlag = false;
        System.out.println(3);
        countPriseSell();
        System.out.println(4);
        countPriseBuy();
        System.out.println(5);
    }

    public static ListensToLooksAndFills getInstance() {
        if (listensToLooksAndFills == null) listensToLooksAndFills = new ListensToLooksAndFills();
        return listensToLooksAndFills;
    }



    // принимаем объекты и если еще не запускали метод их обработки то запускаем его,
    // если он уже запущен то просто кладем объекты в массив
    // так же получаем текущую цену
    public synchronized void setInfoString(InfoIndicator infoIndicator) {
        System.out.println(6);
        listInfoIndicator.add(infoIndicator);
        System.out.println(7);
        if (!timeFlag) {
            timeFlag = true;
            priceNow = Gasket.getBitmexQuote().getBidPrice();
            listSorter();
            System.out.println(7.1);
        }
    }

    // Фиксируем цену отклонения
    private void countPriseBuy() {
        ConsoleHelper.writeMessage("Начал фиксировать цену отклонения Бай ---- "
                + DatesTimes.getDateTerminal());
        System.out.println(8);
        while (true) {
            System.out.println(8.1);
            if (averageFlagBuy) {
                System.out.println(8.2);
                if (priceEndBuy != 0 && priceNow != 0) {
                    System.out.println(8.3);
                    double price = Gasket.getBitmexQuote().getBidPrice();

                    if (price < priceNow) {
                        System.out.println(8.4);
                        listPriceDeviationsBuy.add(price);
                        ConsoleHelper.writeMessage(price + "-Buy");
                    }
                }
            }
            try {
                System.out.println(8.5);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Не смог проснуться в методе countPriseBuy() "
                        + "сласса ListensToLooksAndFills ---- " + DatesTimes.getDateTerminal());
            }
        }
    }

    // Фиксируем цену отклонения
    private void countPriseSell() {
        ConsoleHelper.writeMessage("Начал фиксировать цену отклонения Селл ---- "
                + DatesTimes.getDateTerminal());
        while (true) {
            if (averageFlagSell) {
                if (priceEndSell != 0 && priceNow != 0) {
                    double price = Gasket.getBitmexQuote().getBidPrice();

                    if (price > priceNow) {
                        listPriceDeviationsSell.add(price);
                        ConsoleHelper.writeMessage(price + "-Sell");
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


    // отсыпаемся и начинаем работать
    private synchronized void listSorter() {
        System.out.println(9);
            ConsoleHelper.writeMessage("listSorter() - работает - " + DatesTimes.getDateTerminal());
        if (timeFlag) {
            System.out.println(9.1);
            ConsoleHelper.writeMessage("Заснул - " + DatesTimes.getDateTerminal());
            try {
                System.out.println(9.2);
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Не смог проснуться в методе listSorter() "
                        + "класса ListensToLooksAndFills ---- " + DatesTimes.getDateTerminal());
                e.printStackTrace();
            }
            ConsoleHelper.writeMessage("Отоспался - " + DatesTimes.getDateTerminal());
            timeFlag = false;
        }

        System.out.println(9.3);
        averageFlagSell = false;
        averageFlagBuy = false;


        // если цены финиша нет то назначаем ее
        if (priceEndBuy == 0) {
            System.out.println(9.4);
            priceEndBuy = priceNow + priceTake;
            ConsoleHelper.writeMessage("Назначаем цену priceEndBuy - " + priceEndBuy);
        } else if (priceEndBuy <= priceNow) {
            System.out.println(9.5);
            // если же нынешняя цена вышла за пределы планируемой цены то назначаем следующую желаемую цену движения
            priceEndBuy = priceNow + priceTake;
            // добавляем лист в стратегии,
            ConsoleHelper.writeMessage("Добавляю лист в ПАТТЕРН Бай ---- "
                    + DatesTimes.getDateTerminal());
            listStringPriceBuy.set(0, listStringPriceBuy.get(0) + getAverageDeviations(true)
                    + "===MAX===" + getMaxDeviations(true));
            savedPatterns.addListsPricePatterns(listStringPriceBuy);
            // стираем данные из листа цен отклонения
            listPriceDeviationsBuy.clear();
            // стираем и добавляем в него новые данные
            listStringPriceBuy.clear();
            // добавляем в начало листа метку что делать при совпадения патерна
            listStringPriceBuy.add("BUY===1===SELL===0===AVERAGE===");
            sortPrice(true);
            System.out.println(9.6);
        } else {
            // добавляем в лист метку что делать при совпадения патерна
            ConsoleHelper.writeMessage("Добавляю в лист нулевую строку - "
                    + DatesTimes.getDateTerminal());
            if (listStringPriceBuy.size() == 0) {
                System.out.println(9.7);
                listStringPriceBuy.add("BUY===1===SELL===0===AVERAGE===");
            }
            sortPrice(true);
        }

        // тоже самое только для комбиначии СЕЛЛ
        if (priceEndSell == 0) {
            priceEndSell = priceNow - priceTake;
        } else if (priceEndSell >= priceNow) {
            priceEndSell = priceNow - priceTake;
            ConsoleHelper.writeMessage("Добавляю лист в ПАТТЕРН Селл ---- "
                    + DatesTimes.getDateTerminal());
            listStringPriceSell.set(0, listStringPriceSell.get(0) + getAverageDeviations(false)
                    + "===MAX===" + getMaxDeviations(false));
            savedPatterns.addListsPricePatterns(listStringPriceSell);
            listPriceDeviationsSell.clear();
            listStringPriceSell.clear();
            listStringPriceSell.add("BUY===0===SELL===1===AVERAGE===");
            sortPrice(false);
            averageFlagSell = true;
        } else {
            if (listStringPriceSell.size() == 0) {
                listStringPriceSell.add("BUY===0===SELL===1===AVERAGE===");
            }
            sortPrice(false);
        }
        // очищаем лист входящих объектов
        listInfoIndicator.clear();
        averageFlagSell = true;
        averageFlagBuy = true;
    }


    // Cортируем и добавляем строки в листы направлений
    private void sortPrice(boolean buyOrSell) {
        ConsoleHelper.writeMessage("Cортируем и добавляем строки в листы направлений - " + DatesTimes.getDateTerminal());
        Comparator sortPriceComparator = new SortPrice();
        Collections.sort(listInfoIndicator, sortPriceComparator);
        System.out.println(10);

        if (buyOrSell) {
            System.out.println(10.1);
            for (InfoIndicator infoIndicator : listInfoIndicator) {
                listStringPriceBuy.add(infoIndicator.toString());
            }
        } else {
            System.out.println(10.2);
            for (InfoIndicator infoIndicator : listInfoIndicator) {
                listStringPriceSell.add(infoIndicator.toString());
            }
        }
    }


    // находим максимальную просадку
    private double getMaxDeviations(boolean b) {
        ConsoleHelper.writeMessage("находим максимальную просадку - " + DatesTimes.getDateTerminal());
        double result = 0;
            System.out.println(11);
        if (b) {
            System.out.println(11.1);
            for (Double d : listPriceDeviationsBuy) {
                result = Math.min(result, d);
            }
            return priceNow - result;
        } else {
            System.out.println(11.2);
            for (Double d : listPriceDeviationsSell) {
                result = Math.min(result, d);
            }
            return result - priceNow;
        }
    }


    // находим среднюю просадку
    private double getAverageDeviations(boolean b) {
        ConsoleHelper.writeMessage("находим среднюю просадку - " + DatesTimes.getDateTerminal());
        double result = 0;
        if (b) {
            System.out.println(12);
            for (Double d : listPriceDeviationsBuy) {
                result = result + d;
            }
            return result / listPriceDeviationsBuy.size();
        } else {
            System.out.println(12.1);
            for (Double d : listPriceDeviationsSell) {
                result = result + d;
            }
            return result / listPriceDeviationsSell.size();
        }
    }



    private class SortPrice implements Comparator<InfoIndicator> {
        @Override
        public int compare(InfoIndicator o1, InfoIndicator o2) {
            System.out.println(13);
            double result = o1.getPrice() - o2.getPrice();
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }
}
