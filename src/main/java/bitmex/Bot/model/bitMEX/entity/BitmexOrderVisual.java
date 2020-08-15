package bitmex.Bot.model.bitMEX.entity;

public class BitmexOrderVisual extends BitmexOrder{
    // видно ли ордер в книге заказов 0 - не видно 1 - видно один (чтобы был полностю открыт - оставить поле пустое)
    protected double displayQty;

    @Override
    public double getDisplayQty() {
        return displayQty;
    }

    @Override
    public void setDisplayQty(double displayQty) {
        this.displayQty = displayQty;
    }

    @Override
    public String toString() {
        return "BitmexOrder{" + "orderID=" + super.orderID + ", symbol=" + super.symbol + ", orderQty=" + super.orderQty
                + ", price=" + super.price + ", stopPx=" + super.stopPx + ", side=" + super.side + ", ordType="
                + super.ordType + ", timeInForce=" + super.timeInForce + ", execInst=" + super.execInst
                + ", displayQty=" + displayQty + ", ordStatus=" + super.ordStatus + '}';
    }

    @Override
    public String toStringForJson() {
        return "BitmexOrder{" + "orderID=" + super.orderID + ", symbol=" + super.symbol + ", orderQty=" + super.orderQty
                + ", price=" + super.price + ", stopPx=" + super.stopPx + ", side=" + super.side + ", ordType="
                + super.ordType + ", timeInForce=" + super.timeInForce + ", execInst=" + super.execInst
                + ", displayQty=" + displayQty + ", ordStatus=" + super.ordStatus + '}';
    }
}
