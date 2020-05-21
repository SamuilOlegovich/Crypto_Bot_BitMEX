package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.model.enums.TimeFrame;
import bitmex.Bot.model.enums.BidAsk;


import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class InfoIndicator {

    private TimeFrame period;       // таймфрейм
    private double price;           // цена
    private double close;           // цена закрытия свечи
    private BidAsk type;            // тип уровня
    private double high;            // максимальная цена свечи
    private double open;            // цена открытия свечи
    private int preview;            // предварительный или реальный уровень 0 - реальный, 1 - предварительный
    private long value;             // объём
    private double low;             // минимальная цена свечи
    private Date time;              // Дата свечи на которой находится данный уровень
    private long avg;
    private int dir;                // направление свечи 1 - бай, -1 - селл



    public InfoIndicator(TimeFrame period, int preview, Date time, double price, long value,
                         BidAsk type, long avg, int dir, double open, double close, double high, double low) {
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
        whatLevelIsIt();
    }


    private void whatLevelIsIt() {
        if (price > high || price < low) {
            price = (high + low) / 2.0;
        }
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

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getOpen() {
        return open;
    }

    public double getLow() {
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