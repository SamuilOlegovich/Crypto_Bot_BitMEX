package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import bitmex.Bot.model.bitMEX.entity.BitmexOrderVisual;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.view.ConsoleHelper;



// этот класс пока очень простой и без проверок и сопровождения ордеров, а так же без стоп ордера
// все выше перечисленное для полноценной торговли надо доделать(но пока не стоит такой задачи)
public class TradeBuyPro extends Thread {

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


    public TradeBuyPro(String id) {
        this.orderBuyOpen = Gasket.isVisibleOnOff() ? new BitmexOrder() : new BitmexOrderVisual();
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

    public TradeBuyPro(String id, double lot) {
        this.orderBuyOpen = Gasket.isVisibleOnOff() ? new BitmexOrder() : new BitmexOrderVisual();
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
        if (!Gasket.isTwoAccounts() && Gasket.isGameAllDirection()) start();
        if (!Gasket.isTwoAccounts() && Gasket.isGameDirection()) start();
        if (Gasket.isTwoAccounts()) start();
    }


    @Override
    public void run() {
        price = Gasket.getBitmexQuote().getBidPrice() - 1.0;
//        price = Gasket.getBitmexQuote().getBidPrice();
        orderBuyOpen.setExecInst(Gasket.getExecInst());
        orderBuyOpen.setTimeInForce("GoodTillCancel");
        orderBuyOpen.setSymbol(ticker.getSymbol());
        orderBuyOpen.setDisplayQty(visible);
        orderBuyOpen.setOrdType(typeOrder);
        orderBuyOpen.setOrderQty(lot);
        orderBuyOpen.setPrice(price);
        orderBuyOpen.setSide("Buy");


        orderBuyAnswer = bitmexClient.submitOrder(orderBuyOpen);

        ConsoleHelper.writeMessage(ID + " --- Открыл BUY позицию --- "
                + orderBuyAnswer.getOrderID() + " --- "
                + orderBuyAnswer.toString()
                + "\n"
        );

        try {
            Thread.sleep(Gasket.getSECOND() * timeBetweenOrders);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        price = price + Gasket.getTake();
        orderBuyOpen.setExecInst(Gasket.getExecInst());
        orderBuyOpen.setTimeInForce("GoodTillCancel");
        orderBuyOpen.setSymbol(ticker.getSymbol());
        orderBuyOpen.setDisplayQty(visible);
        orderBuyOpen.setOrdType(typeOrder);
        orderBuyOpen.setOrderQty(lot);
        orderBuyOpen.setPrice(price);
        orderBuyOpen.setSide("Sell");

        orderBuyAnswer = bitmexClient.submitOrder(orderBuyOpen);

        ConsoleHelper.writeMessage(ID + " --- Открыл BUY - TAKE позицию --- "
                + orderBuyAnswer.getOrderID() + " --- "
                + orderBuyAnswer.toString()
                + "\n"
        );
    }
}
