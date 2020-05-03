package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;
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
    private double priceEndSell;
    private double priceEndBuy;
    private double priceTake;
    private double priceNow;

    private boolean averageFlagSell;
    private boolean averageFlagBuy;
    private boolean timeFlag;


    private ListensToLooksAndFills() {
        ConsoleHelper.writeMessage("Начал работать класс сбора Паттернов ---- "
                + DatesTimes.getDateTerminal());
        this.listPriceDeviationsSell = new ArrayList<>();
        this.listPriceDeviationsBuy = new ArrayList<>();
        this.listStringPriceSell = new ArrayList<>();
        this.listStringPriceBuy = new ArrayList<>();
        this.listInfoIndicator = new ArrayList<>();
        this.averageFlagSell = true;
        this.averageFlagBuy = true;
        this.priceEndSell = 00.0;
        this.priceEndBuy = 00.0;
        this.priceTake = 30.0;
        this.timeFlag = false;
        countPriseSell();
        countPriseBuy();
    }

    public static ListensToLooksAndFills getInstance() {
        if (listensToLooksAndFills == null) listensToLooksAndFills = new ListensToLooksAndFills();
        return listensToLooksAndFills;
    }



    // принимаем объекты и если еще не запускали метод их обработки то запускаем его,
    // если он уже запущен то просто кладем объекты в массив
    // так же получаем текущую цену
    public synchronized void setIInfoString(InfoIndicator iInfoIndicator) {
        listInfoIndicator.add(iInfoIndicator);
        if (!timeFlag) {
            timeFlag = true;
            priceNow = Gasket.getBitmexQuote().getBidPrice();
            listSorter();
        }
    }

    // Фиксируем цену отклонения
    private void countPriseBuy() {
        ConsoleHelper.writeMessage("Начал фиксировать цену отклонения Бай ---- "
                + DatesTimes.getDateTerminal());
        while (true) {
            if (averageFlagBuy) {
                if (priceEndBuy != 0 && priceNow != 0) {
                    double price = Gasket.getBitmexQuote().getBidPrice();

                    if (price < priceNow) {
                        listPriceDeviationsBuy.add(price);
                    }
                }
            }
            try {
                Thread.sleep(1000 * 1);
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
                    }
                }
            }
            try {
                Thread.sleep(1000 * 1);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Не смог проснуться в методе countPriseSell() "
                        + "сласса ListensToLooksAndFills ---- " + DatesTimes.getDateTerminal());
            }
        }
    }


    // отсыпаемся и начинаем работать
    private synchronized void listSorter() {
        if (timeFlag) {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Не смог проснуться в методе listSorter() "
                        + "класса ListensToLooksAndFills ---- " + DatesTimes.getDateTerminal());
                e.printStackTrace();
            }
            timeFlag = false;
        }

        averageFlagBuy = false;
        averageFlagSell = false;

        // если цены финиша нет то назначаем ее
        if (priceEndBuy == 0) {
            priceEndBuy = priceNow + priceTake;
        } else if (priceEndBuy >= priceNow) {
            // если же нынешняя цена вышла за пределы планируемой цены то назначаем следующую желаемую цену движения
            priceEndBuy = priceNow + priceTake;
            // добавляем лист в стратегии,
            ConsoleHelper.writeMessage("Добавляю лист в ПАТТЕРН Бай ---- "
                    + DatesTimes.getDateTerminal());
            listStringPriceBuy.add(0, listStringPriceBuy.get(0) + getAverageDeviations(true)
                    + "===MAX===" + getMaxDeviations(true));
            SavedPatterns.addListsPricePatterns(listStringPriceBuy);
            // стираем данные из листа цен отклонения
            listPriceDeviationsBuy.clear();
            // стираем и добавляем в него новые данные
            listStringPriceBuy.clear();
            // добавляем в начало листа метку что делать при совпадения патерна
            listStringPriceBuy.add("BUY===1===SELL===0===AVERAGE===");
            sortPrice(true);
        } else {
            // добавляем в лист метку что делать при совпадения патерна
            if (listStringPriceBuy.size() == 0) {
                listStringPriceBuy.add("BUY===1===SELL===0===AVERAGE===");
            }
            sortPrice(true);
        }

        // тоже самое только для комбиначии СЕЛЛ
        if (priceEndSell == 0) {
            priceEndSell = priceNow - priceTake;
        } else if (priceEndSell <= priceNow) {
            priceEndSell = priceNow - priceTake;
            ConsoleHelper.writeMessage("Добавляю лист в ПАТТЕРН Селл ---- "
                    + DatesTimes.getDateTerminal());
            listStringPriceSell.add(0, listStringPriceSell.get(0) + getAverageDeviations(false)
                    + "===MAX===" + getMaxDeviations(false));
            SavedPatterns.addListsPricePatterns(listStringPriceSell);
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
        Comparator sortPriceComparator = new SortPrice();
        Collections.sort(listInfoIndicator, sortPriceComparator);

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
        double result = 0;
        if (b) {
            for (Double d : listPriceDeviationsBuy) {
                result = Math.min(result, d);
            }
            return priceNow - result;
        } else {
            for (Double d : listPriceDeviationsSell) {
                result = Math.min(result, d);
            }
            return result - priceNow;
        }
    }


    // находим среднюю просадку
    private double getAverageDeviations(boolean b) {
        double result = 0;
        if (b) {
            for (Double d : listPriceDeviationsBuy) {
                result = result + d;
            }
            return result / listPriceDeviationsBuy.size();
        } else {
            for (Double d : listPriceDeviationsSell) {
                result = result + d;
            }
            return result / listPriceDeviationsSell.size();
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
}
