package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.model.enums.TimeFrame;
import bitmex.Bot.model.enums.BidAsk;


import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class InfoIndicator {

    private TimeFrame period;
    private double price;
    private BidAsk type;
    private int preview;
    private long value;
    private Date time;


    public InfoIndicator(TimeFrame period, int preview, Date time, double price, long value, BidAsk type) {
        this.preview = preview;
        this.period = period;
        this.value = value;
        this.price = price;
        this.type = type;
        this.time = time;
    }


    public TimeFrame getPeriod() {
        return period;
    }

    public double getPrice() {
        return price;
    }

    public BidAsk getType() {
        return type;
    }

    public int getPreview() {
        return preview;
    }

    public long getValue() {
        return value;
    }

    public Date getTime() {
        return time;
    }


    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.format(time);

        return "{\"period\": \"" + period + "\",\"preview\": \"" + preview + "\",\"time\": \""
                + dateFormat.format(time) + "\",\"price\": \""
                + price + "\",\"value\": \"" + value
                + "\",\"type\": \"" + type + "\"}";
    }
}