package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.model.serverAndParser.enums.BidAsk;
import bitmex.Bot.model.serverAndParser.enums.TimeFrame;


import java.text.SimpleDateFormat;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return
                "{\"period\": \"" + period + "\",\"preview\": \"" + preview
                        + "\",\"time\": \"" + time + "\",\"price\": \"" + price
                        + "\",\"value\": \"" + value + "\",\"type\": \"" + type + "\"}";
    }
}