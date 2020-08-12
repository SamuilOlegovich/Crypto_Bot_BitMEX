/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

/**
 *
 * @author RobTerpilowski
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitmexOrder {
    // ордер

    protected double orderQty;
    protected String orderID;
    protected String symbol;
    protected Double stopPx;//////////////////////
    protected Double price;
    //Valid options: Buy, Sell. 
    protected String side;
    // Valid options: Market, Limit, Stop, StopLimit, MarketIfTouched, LimitIfTouched, MarketWithLeftOverAsLimit,
    // Pegged.
    protected String ordType;
    // Day, GoodTillCancel, ImmediateOrCancel, FillOrKill. Defaults to 'GoodTillCancel' for 'Limit', 'StopLimit',
    // 'LimitIfTouched', and 'MarketWithLeftOverAsLimit' orders.
    // День, GoodTillCancel, ImmediateOrCancel, FillOrKill. По умолчанию используется GoodTillCancel для ордеров Limit,
    // StopLimit, LimitIfTouched и MarketWithLeftOverAsLimit.
    protected String timeInForce;
     // Valid options: ParticipateDoNotInitiate, AllOrNone, MarkPrice, IndexPrice, LastPrice, Close, ReduceOnly, Fixed.
     // 'AllOrNone' instruction requires displayQty to be 0. 'MarkPrice', 'IndexPrice' or
     // 'LastPrice' instruction valid for 'Stop', 'StopLimit', 'MarketIfTouched', and 'LimitIfTouched' orders.

    // Допустимые параметры: ParticipateDoNotInitiate, AllOrNone, MarkPrice, IndexPrice, LastPrice, Close, ReduceOnly,
    // Fixed. // Инструкция «AllOrNone» требует, чтобы displayQty было равно 0. Инструкция «MarkPrice»,
    // «IndexPrice» или // «LastPrice» действительна для ордеров «Stop», «StopLimit», «MarketIfTouched» и «LimitIfTouched».
    // ставим для сто процентного получения ребейта ParticipateDoNotInitiate
    protected String execInst;
    protected String ordStatus;
    // видно ли ордер в книге заказов 0 - не видно 1 - видно один (чтобы был полностю открыт - оставить поле пустое)
    protected double displayQty;//////////////////////////////////////////
    // доп инфа для ордера
    protected String text;/////////////////////////////////


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(double orderQty) {
        this.orderQty = orderQty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOrdType() {
        return ordType;
    }

    public void setOrdType(String ordType) {
        this.ordType = ordType;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public String getExecInst() {
        return execInst;
    }

    public void setExecInst(String execInst) {
        this.execInst = execInst;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrdStatus() {
        return ordStatus;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    /////////////////////
    public Double getStopPx() {
        return stopPx;
    }

    public void setStopPx(Double stopPx) {
        this.stopPx = stopPx;
    }

    public double getDisplayQty() {
        return displayQty;
    }

    public void setDisplayQty(double displayQty) {
        this.displayQty = displayQty;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    //////////////////////

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.orderQty) ^ (Double.doubleToLongBits(this.orderQty) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.stopPx) ^ (Double.doubleToLongBits(this.stopPx) >>> 32));///////////////
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.price) ^ (Double.doubleToLongBits(this.price) >>> 32));
        hash = 59 * hash + Objects.hashCode(this.timeInForce);
        hash = 59 * hash + Objects.hashCode(this.ordStatus);
        hash = 59 * hash + Objects.hashCode(this.execInst);
        hash = 59 * hash + Objects.hashCode(this.ordType);
        hash = 59 * hash + Objects.hashCode(this.orderID);
        hash = 59 * hash + Objects.hashCode(this.symbol);
        hash = 59 * hash + Objects.hashCode(this.side);

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BitmexOrder other = (BitmexOrder) obj;
        if (Double.doubleToLongBits(this.orderQty) != Double.doubleToLongBits(other.orderQty)) {
            return false;
        }
        if (Double.doubleToLongBits(this.price) != Double.doubleToLongBits(other.price)) {
            return false;
        }
        if (!Objects.equals(this.orderID, other.orderID)) {
            return false;
        }
        if (!Objects.equals(this.symbol, other.symbol)) {
            return false;
        }
        if (!Objects.equals(this.side, other.side)) {
            return false;
        }
        if (!Objects.equals(this.ordType, other.ordType)) {
            return false;
        }
        if (!Objects.equals(this.timeInForce, other.timeInForce)) {
            return false;
        }
        if (!Objects.equals(this.execInst, other.execInst)) {
            return false;
        }
        if (!Objects.equals(this.ordStatus, other.ordStatus)) {
            return false;
        }
        /////////////////////////////////////////////////////////////////////////////////////
        if (Double.doubleToLongBits(this.stopPx) != Double.doubleToLongBits(other.stopPx)) {
            return false;
        }
        if (Double.doubleToLongBits(this.displayQty) != Double.doubleToLongBits(other.displayQty)) {
            return false;
        }
        /////////////////////////////////////////////////////////////////////////////////////
        return true;
    }

    @Override
    public String toString() {
        return "BitmexOrder{" + "orderID=" + orderID + ", symbol=" + symbol + ", orderQty=" + orderQty + ", price=" + price + ", stopPx=" + stopPx + ", side=" + side + ", ordType=" + ordType + ", timeInForce=" + timeInForce + ", execInst=" + execInst + ", displayQty=" + displayQty + ", ordStatus=" + ordStatus + '}';
    }
    
    

    
    
    
}


/**
 *  "orderID": "string",
  "clOrdID": "string",
  "clOrdLinkID": "string",
  "account": 0,
  "symbol": "string",
  "side": "string",
  "simpleOrderQty": 0,
  "orderQty": 0,
  "price": 0,
  "displayQty": 0,
  "stopPx": 0,
  "pegOffsetValue": 0,
  "pegPriceType": "string",
  "currency": "string",
  "settlCurrency": "string",
  "ordType": "string",
  "timeInForce": "string",
  "execInst": "string",
  "contingencyType": "string",
  "exDestination": "string",
  "ordStatus": "string",
  "triggered": "string",
  "workingIndicator": true,
  "ordRejReason": "string",
  "simpleLeavesQty": 0,
  "leavesQty": 0,
  "simpleCumQty": 0,
  "cumQty": 0,
  "avgPx": 0,
  "multiLegReportingType": "string",
  "text": "string",
  "transactTime": "2018-05-09T21:47:34.391Z",
  "timestamp": "2018-05-09T21:47:34.391Z"
 */