package bitmex.Bot.model;

import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import bitmex.Bot.view.ConsoleHelper;

public class TradeBuy extends Thread {

    private BitmexOrder orderLimitIfTouchedAnswer;
    private BitmexOrder orderLimitIfTouchedOpen;
    private BitmexOrder orderStopLimitAnswer;
    private BitmexOrder orderStopLimitOpen;
    private BitmexOrder orderBuyAnswer;
    private BitmexClient bitmexClient;
    private BitmexOrder orderBuyOpen;
    private int timeBetweenOrders;
    private double priceActiv;
    private String typeOrder;
    private double visible;
    private Ticker ticker;
    private double price;
    private double take;
    private double stop;
    private double lot;
    private String ID;


    public TradeBuy(String id) {
        this.timeBetweenOrders = Gasket.getTimeBetweenOrders();
        this.bitmexClient = Gasket.getBitmexClient();
        this.priceActiv = Gasket.getPriceActive();
        this.typeOrder = Gasket.getTypeOrder();
        this.orderBuyOpen = new BitmexOrder();
        this.visible = Gasket.getVisible();
        this.ticker = Gasket.getTicker();
        this.take = Gasket.getTake();
        this.stop = Gasket.getStop();
        this.lot = Gasket.getLot();
        this.ID = id;
        init();
    }

    public TradeBuy(String id, double lot) {
        this.timeBetweenOrders = Gasket.getTimeBetweenOrders();
        this.bitmexClient = Gasket.getBitmexClient();
        this.priceActiv = Gasket.getPriceActive();
        this.typeOrder = Gasket.getTypeOrder();
        this.orderBuyOpen = new BitmexOrder();
        this.visible = Gasket.getVisible();
        this.ticker = Gasket.getTicker();
        this.take = Gasket.getTake();
        this.stop = Gasket.getStop();
        this.lot = lot;
        this.ID = id;
        init();
    }

    private void init() {
        if (!Gasket.isTwoAccounts() && Gasket.isGameAllDirection()) start();
        if (!Gasket.isTwoAccounts() && Gasket.isGameDirection()) start();
        if (Gasket.isTwoAccounts()) start();
    }


    @Override
    public void run() {
        price = Gasket.getBitmexQuote().getAskPrice();
        orderBuyOpen.setTimeInForce("GoodTillCancel");
        orderBuyOpen.setSymbol(ticker.getSymbol());
        orderBuyOpen.setDisplayQty(visible);
        orderBuyOpen.setOrdType(typeOrder);
        orderBuyOpen.setOrderQty(lot);
        orderBuyOpen.setPrice(price);
        orderBuyOpen.setSide("Buy");


        orderBuyAnswer = bitmexClient.submitOrder(orderBuyOpen);
        ConsoleHelper.writeMessage(ID + " --- Открыл BUY позицию --- "
                + orderBuyAnswer.getOrderID() + "\n"
                + orderBuyAnswer.toString());

        try {
            Thread.sleep(Gasket.getSECOND() * timeBetweenOrders);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        orderLimitIfTouchedOpen = orderBuyOpen;
        orderLimitIfTouchedOpen.setText("Take profit - " + orderBuyAnswer.getOrderID());
        orderLimitIfTouchedOpen.setStopPx(price + priceActiv);
        orderLimitIfTouchedOpen.setOrdType("LimitIfTouched");
        orderLimitIfTouchedOpen.setPrice(price + take);
        orderLimitIfTouchedOpen.setSide("Sell");
        orderLimitIfTouchedOpen.setOrderID("");


        orderLimitIfTouchedAnswer = bitmexClient.submitOrder(orderLimitIfTouchedOpen);
        ConsoleHelper.writeMessage(ID + " --- Выставил Тейк для позиции --- "
                + orderLimitIfTouchedAnswer.getOrderID() + "\n"
                + orderLimitIfTouchedAnswer.toString());

        try {
            Thread.sleep(Gasket.getSECOND() * timeBetweenOrders);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        orderStopLimitOpen = orderBuyOpen;
        orderStopLimitOpen.setText("Stop loss - " + orderBuyAnswer.getOrderID());
        //orderStopLimitOpen.setStopPx(price - priceActiv);
        orderStopLimitOpen.setOrdType("Stop");
        orderStopLimitOpen.setPrice(price - stop);
        orderStopLimitOpen.setSide("Sell");
        orderStopLimitOpen.setOrderID("");


        orderStopLimitAnswer = bitmexClient.submitOrder(orderStopLimitOpen);
        ConsoleHelper.writeMessage(ID + " --- Выставил Стоп для позиции --- "
                + orderStopLimitAnswer.getOrderID() + "\n"
                + orderStopLimitAnswer.toString());
    }
}
