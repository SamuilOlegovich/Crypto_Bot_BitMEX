package bitmex.Bot.model;

import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import bitmex.Bot.view.ConsoleHelper;

public class TradeSell extends Thread {

    private BitmexOrder orderLimitIfTouchedAnswer;
    private BitmexOrder orderLimitIfTouchedOpen;
    private BitmexOrder orderStopLimitAnswer;
    private BitmexOrder orderStopLimitOpen;
    private BitmexOrder orderSellAnswer;
    private BitmexClient bitmexClient;
    private BitmexOrder orderSellOpen;
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


    public TradeSell(String id) {
        this.timeBetweenOrders = Gasket.getTimeBetweenOrders();
        this.bitmexClient = Gasket.getBitmexClient();
        this.priceActiv = Gasket.getPriceActive();
        this.typeOrder = Gasket.getTypeOrder();
        this.orderSellOpen = new BitmexOrder();
        this.visible = Gasket.getVisible();
        this.ticker = Gasket.getTicker();
        this.take = Gasket.getTake();
        this.stop = Gasket.getStop();
        this.lot = Gasket.getLot();
        this.ID = id;

        init();
    }

    private void init() {
        if (!Gasket.isTwoAccounts() && !Gasket.isGameDirection() && Gasket.isGameAllDirection()) start();
        if (!Gasket.isTwoAccounts() && !Gasket.isGameDirection()) start();
        if (Gasket.isTwoAccounts()) {
            bitmexClient = Gasket.getBitmexClient2Accounts();
            start();
        }
    }

    private double getPrise() {

        if (Gasket.isTwoAccounts()) {
            return Gasket.getBitmex2Quote().getAskPrice();
        } else return Gasket.getBitmexQuote().getAskPrice();
    }


    @Override
    public void run() {
        price = getPrise();
        orderSellOpen.setTimeInForce("GoodTillCancel");
        orderSellOpen.setSymbol(ticker.getSymbol());
        orderSellOpen.setDisplayQty(visible);
        orderSellOpen.setOrdType(typeOrder);
        orderSellOpen.setOrderQty(lot);
        orderSellOpen.setPrice(price);
        orderSellOpen.setSide("Buy");


        orderSellAnswer = bitmexClient.submitOrder(orderSellOpen);
        ConsoleHelper.writeMessage(ID + " --- Открыл SELL позицию --- "
                + orderSellAnswer.getOrderID() + "\n"
                + orderSellAnswer.toString());

        try {
            Thread.sleep(1000 * timeBetweenOrders);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        orderLimitIfTouchedOpen = orderSellOpen;
        orderLimitIfTouchedOpen.setText("Take profit - " + orderSellAnswer.getOrderID());
        orderLimitIfTouchedOpen.setStopPx(price - priceActiv);
        orderLimitIfTouchedOpen.setOrdType("LimitIfTouched");
        orderLimitIfTouchedOpen.setPrice(price - take);
        orderLimitIfTouchedOpen.setSide("Sell");
        orderLimitIfTouchedOpen.setOrderID("");


        orderLimitIfTouchedAnswer = bitmexClient.submitOrder(orderLimitIfTouchedOpen);
        ConsoleHelper.writeMessage(ID + " --- Выставил Тейк для позиции --- "
                + orderLimitIfTouchedAnswer.getOrderID() + "\n"
                + orderLimitIfTouchedAnswer.toString());

        try {
            Thread.sleep(1000 * timeBetweenOrders);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        orderStopLimitOpen = orderSellOpen;
        orderStopLimitOpen.setText("Stop loss - " + orderSellAnswer.getOrderID());
        //orderStopLimitOpen.setStopPx(price + priceActiv);
        orderStopLimitOpen.setOrdType("Stop");
        orderStopLimitOpen.setPrice(price + stop);
        orderStopLimitOpen.setSide("Sell");
        orderStopLimitOpen.setOrderID("");


        orderStopLimitAnswer = bitmexClient.submitOrder(orderStopLimitOpen);
        ConsoleHelper.writeMessage(ID + " --- Выставил Стоп для позиции --- "
                + orderStopLimitAnswer.getOrderID() + "\n"
                + orderStopLimitAnswer.toString());
    }
}