package bitmex.Bot.model.serverAndParser;



import bitmex.Bot.model.enums.BidAsk;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.strategies.StrategyFactory;
import bitmex.Bot.model.enums.TimeFrame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ParserString {
    private ArrayList<InfoIndicator> arrayList;
    private StrategyFactory strategyFactory;


    public ParserString() {
        this.arrayList = new ArrayList<InfoIndicator>();
        this.strategyFactory = StrategyFactory.getInstance();
    }

    public synchronized void parserStringJson(String string) {
        String in = string.replaceAll("\\{", "");
        in = in.replaceAll("}", "");
        in = in.replaceAll("\"", " ");
        String[] strings = in.split(" , ");
        TimeFrame period = getTimeFrame(strings[0].trim().replaceAll("period : {2}",""));
        int preview = Integer.parseInt(strings[1].trim().replaceAll("preview : {2}", ""));
        Date time = getDate(strings[2].trim().replaceAll("time : {2}", ""));
        double price = getDouble(strings[3].trim().replaceAll("price : {2}", ""));
        long value = Long.parseLong(strings[4].trim().replaceAll("value : {2}", ""));
        BidAsk type = getType(strings[5].trim().replaceAll("type : {2}", ""));
        InfoIndicator infoIndicator = new InfoIndicator(period, preview, time, price, value, type);
        strategyFactory.onOff(infoIndicator);
        arrayList.add(infoIndicator);
    }

    private Date getDate(String string) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date dateFromString = null;

        try {
            dateFromString = simpleDateFormat.parse(string);
        } catch (Exception e) {
            ConsoleHelper.writeMessage("неверный формат даты");
        }
        return dateFromString;
    }

    private Double getDouble(String string) {
        return Double.parseDouble(string.replaceAll(",", "."));
    }

    private TimeFrame getTimeFrame(String string) {
        switch (string) {
            case "M1" :
                return TimeFrame.M1;
            case "M5" :
                return TimeFrame.M5;
            case "M15" :
                return TimeFrame.M15;
            case "M30" :
                return TimeFrame.M30;
            case "H1" :
                return TimeFrame.H1;
            case "H4" :
                return TimeFrame.H4;
            case "D1" :
                return TimeFrame.D1;
            case "W1" :
                return TimeFrame.W1;
            case "MN1" :
                return TimeFrame.MN1;
            case "Y1" :
                return TimeFrame.Y1;
        }
        return null;
    }

    private BidAsk getType(String string) {
        switch (string) {
            case "DeltaMinVolBidHL" :
                return BidAsk.DELTA_MIN_VOL_BID_HL;
            case "DeltaMinVolAskHL":
                return BidAsk.DELTA_MIN_VOL_ASK_HL;
            case "OpenPosMinusHL" :
                return BidAsk.OPEN_POS_MINUS_HL;
            case "OpenPosPlusHL" :
                return BidAsk.OPEN_POS_PLUS_HL;
            case "OpenPosMinus" :
                return BidAsk.OPEN_POS_MINUS;
            case "OpenPosPlus" :
                return BidAsk.OPEN_POS_PLUS;
            case "DeltaBidHL":
                return BidAsk.DELTA_BID_HL;
            case "DeltaAskHL":
                return BidAsk.DELTA_ASK_HL;
            case "DeltaAsk" :
                return BidAsk.DELTA_ASK;
            case "DeltaBid" :
                return BidAsk.DELTA_BID;
            case "Volume":
                return BidAsk.VOLUME;
            case "Bid":
                return BidAsk.BID;
            case "Ask":
                return BidAsk.ASK;
        }
        return null;
    }

    public ArrayList<InfoIndicator> getList() {
        return arrayList;
    }

    public void setList(ArrayList list) {
        this.arrayList = list;
    }
}
