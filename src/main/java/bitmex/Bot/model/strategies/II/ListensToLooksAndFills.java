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



    public synchronized void setIInfoString(InfoIndicator iInfoIndicator) {
        listInfoIndicator.add(iInfoIndicator);
        if (!timeFlag) {
            priceNow = Gasket.getBitmexQuote().getBidPrice();
            timeFlag = true;
            listSorter();
        }
    }


    private synchronized void listSorter() {
        if (timeFlag) {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Не смог проснуться в методе listSorter() класса ListensToLooksAndFills");
                e.printStackTrace();
            }
            timeFlag = false;
        }

        if (priceEndBuy == 0) {
            priceEndBuy = priceNow + priceTake;
        } else if (priceEndBuy >= priceNow) {
            priceEndBuy = priceNow + priceTake;
            SavedPatterns.addListsPriceBuy(listStringPriceBuy);
            listStringPriceBuy.clear();
            sortPrice(true);
        } else {
            sortPrice(true);
        }

        if (priceEndSell == 0) {
            priceEndSell = priceNow - priceTake;
        } else if (priceEndSell <= priceNow) {
            priceEndSell = priceNow - priceTake;
            SavedPatterns.addListsPriceSell(listStringPriceSell);
            listStringPriceSell.clear();
            sortPrice(false);
        } else {
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
