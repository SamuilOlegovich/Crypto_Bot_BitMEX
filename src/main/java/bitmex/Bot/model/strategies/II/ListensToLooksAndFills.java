package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;



public class ListensToLooksAndFills {
    private ListensToLooksAndFills listensToLooksAndFills;
    private ArrayList<InfoIndicator> listInfoIndicator;
    private ArrayList<String> listStringPriceSell;
    private ArrayList<String> listStringPriceBuy;
    private double priceEndSell;
    private double priceEndBuy;
    private double priceTake;
    private double priceNow;

    private boolean timeFlag;


    private ListensToLooksAndFills() {
        this.listStringPriceSell = new ArrayList<>();
        this.listStringPriceBuy = new ArrayList<>();
        this.listInfoIndicator = new ArrayList<>();
        this.priceEndSell = 00.0;
        this.priceEndBuy = 00.0;
        this.priceTake = 30.0;
        this.timeFlag = false;
    }

    public ListensToLooksAndFills getInstance() {
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


    // отсыпаемся и начинаем работать
    private synchronized void listSorter() {
        if (timeFlag) {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Не смог проснуться в методе listSorter() "
                        + "класса ListensToLooksAndFills");
                e.printStackTrace();
            }
            timeFlag = false;
        }


        // если цены финиша нет то назначаем ее
        if (priceEndBuy == 0) {
            priceEndBuy = priceNow + priceTake;
        } else if (priceEndBuy >= priceNow) {
            // если же нынешняя цена вышла за пределы планируемой цены то назначаем следующую желаемую цену движения
            priceEndBuy = priceNow + priceTake;
            // добавляем лист в стратегии,
            SavedPatterns.addListsPricePatterns(listStringPriceBuy);
            // стираем и добавляем в него новые данные
            listStringPriceBuy.clear();
            // добавляем в начало листа метку что делать при совпадения патерна
            listStringPriceBuy.add("BUY===1===SELL===0");
            sortPrice(true);
        } else {
            // добавляем в конец листа метку что делать при совпадения патерна
            if (listStringPriceBuy.size() == 0) {
                listStringPriceBuy.add("BUY===1===SELL===0");
            }
            sortPrice(true);
        }

        if (priceEndSell == 0) {
            priceEndSell = priceNow - priceTake;
        } else if (priceEndSell <= priceNow) {
            priceEndSell = priceNow - priceTake;

            SavedPatterns.addListsPricePatterns(listStringPriceSell);
            listStringPriceSell.clear();
            listStringPriceSell.add("BUY===0===SELL===1");
            sortPrice(false);
        } else {
            if (listStringPriceSell.size() == 0) {
                listStringPriceSell.add("BUY===0===SELL===1");
            }
            sortPrice(false);
        }
        listInfoIndicator.clear();
    }


    private void sortPrice(boolean buyOrSell) {
        Comparator sortPrice = new SortPrice();
        Collections.sort(listInfoIndicator, sortPrice);

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
