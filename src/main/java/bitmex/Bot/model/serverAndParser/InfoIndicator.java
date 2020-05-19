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
    private long close;
    private Date time;
    private long high;
    private long open;
    private long low;
    private long avg;
    private int dir;



    public InfoIndicator(TimeFrame period, int preview, Date time, double price, long value,
                         BidAsk type, long avg, int dir, long open, long close, long high, long low) {
        this.preview = preview;
        this.period = period;
        this.value = value;
        this.price = price;
        this.close = close;
        this.type = type;
        this.time = time;
        this.open = open;
        this.high = high;
        this.avg = avg;
        this.dir = dir;
        this.low = low;
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

    public long getClose() {
        return close;
    }

    public long getHigh() {
        return high;
    }

    public long getOpen() {
        return open;
    }

    public long getLow() {
        return low;
    }

    public long getAvg() {
        return avg;
    }

    public int getDir() {
        return dir;
    }



    public String toStringUser() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.format(time);

        return "period===" + period.toString() + "===preview===" + preview + "===value===" + value
                + "===type===" + type.toString() + "===avg===" + avg + "===dir===" + dir
                + "===open===" + open + "===close===" + close + "===high===" + high
                + "===low===" + low
                + "\n";
    }


    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.format(time);

        return "{\"period\": \"" + period + "\",\"preview\": \"" + preview + "\",\"time\": \""
                + dateFormat.format(time) + "\",\"price\": \"" + price + "\",\"value\": \"" + value
                + "\",\"type\": \"" + type + "\",\"avg\": \"" + avg + "\",\"dir\": \"" + dir
                + "\",\"open\": \"" + open + "\",\"close\": \"" + close
                + "\",\"high\": \"" + high + "\",\"low\": \"" + low
                + "\"}"
                + "\n";
    }
}