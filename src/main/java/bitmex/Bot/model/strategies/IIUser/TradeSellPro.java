package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.bitMEX.entity.BitmexOrderVisual;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;




public class TradeSellPro extends Thread {

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


    public TradeSellPro(String id) {
        this.orderSellOpen = Gasket.isVisibleOnOff() ? new BitmexOrder() : new BitmexOrderVisual();
        this.timeBetweenOrders = Gasket.getTimeBetweenOrders();
        this.bitmexClient = Gasket.getBitmexClient();
        this.priceActiv = Gasket.getPriceActive();
        this.typeOrder = Gasket.getTypeOrder();
        this.visible = Gasket.getVisible();
        this.ticker = Gasket.getTicker();
        this.take = Gasket.getTake();
        this.stop = Gasket.getStop();
        this.lot = Gasket.getLot();
        this.ID = id;
        init();
    }

    public TradeSellPro(String id, double lot) {
        this.orderSellOpen = Gasket.isVisibleOnOff() ? new BitmexOrder() : new BitmexOrderVisual();
        this.timeBetweenOrders = Gasket.getTimeBetweenOrders();
        this.bitmexClient = Gasket.getBitmexClient();
        this.priceActiv = Gasket.getPriceActive();
        this.typeOrder = Gasket.getTypeOrder();
        this.visible = Gasket.getVisible();
        this.ticker = Gasket.getTicker();
        this.take = Gasket.getTake();
        this.stop = Gasket.getStop();
        this.lot = lot;
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
        price = Gasket.getBitmexQuote().getBidPrice() - 1.0;
//        price = Gasket.getBitmexQuote().getBidPrice();
        orderSellOpen.setExecInst(Gasket.getExecInst());
        orderSellOpen.setTimeInForce("GoodTillCancel");
        orderSellOpen.setSymbol(ticker.getSymbol());
        orderSellOpen.setDisplayQty(visible);
        orderSellOpen.setOrdType(typeOrder);
        orderSellOpen.setOrderQty(lot);
        orderSellOpen.setPrice(price);
        orderSellOpen.setSide("Buy");


        orderStopLimitAnswer = bitmexClient.submitOrder(orderSellOpen);

        ConsoleHelper.writeMessage(ID + " --- Открыл BUY позицию --- "
                + orderStopLimitAnswer.getOrderID() + " --- "
                + orderStopLimitAnswer.toString()
                + "\n"
        );

        try {
            Thread.sleep(Gasket.getSECOND() * timeBetweenOrders);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        price = price + Gasket.getTake();
        orderSellOpen.setExecInst(Gasket.getExecInst());
        orderSellOpen.setTimeInForce("GoodTillCancel");
        orderSellOpen.setSymbol(ticker.getSymbol());
        orderSellOpen.setDisplayQty(visible);
        orderSellOpen.setOrdType(typeOrder);
        orderSellOpen.setOrderQty(lot);
        orderSellOpen.setPrice(price);
        orderSellOpen.setSide("Sell");

        orderStopLimitAnswer = bitmexClient.submitOrder(orderSellOpen);

        ConsoleHelper.writeMessage(ID + " --- Открыл BUY - TAKE позицию --- "
                + orderStopLimitAnswer.getOrderID() + " --- "
                + orderStopLimitAnswer.toString()
                + "\n"
        );
    }
}
