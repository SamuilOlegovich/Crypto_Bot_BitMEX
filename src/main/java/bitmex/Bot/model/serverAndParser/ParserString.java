package bitmex.Bot.model.serverAndParser;


import bitmex.Bot.model.strategies.StrategyFactory;
import bitmex.Bot.model.enums.TimeFrame;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.enums.BidAsk;
import bitmex.Bot.model.DatesTimes;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class ParserString {
    //private ArrayList<InfoIndicator> arrayList;
    private StrategyFactory strategyFactory;
    private boolean linkWithIndicator;



    public ParserString() {
        this.strategyFactory = StrategyFactory.getInstance();
        this.linkWithIndicator = true;
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- ПАРСЕР ЗАПУЩЕН");
        //this.arrayList = new ArrayList<>();
//        this.arrayListAllLevel = new ArrayList<>();
    }

    public synchronized void parserStringJson(String string) {

        if (linkWithIndicator) {
            if (string.length() > 20) {
                linkWithIndicator = false;
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                        + " --- СВЯЗЬ С ИНДИКАТОРОМ УСТАНОВЛЕНА");
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                        + " --- " + string);
            }
        }
//        showAllAvailableLevels(string);
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
        long avg = Long.parseLong(strings[6].trim().replaceAll("avg : {2}", ""));
        int dir = Integer.parseInt(strings[7].trim().replaceAll("dir : {2}", ""));
        double open = getDouble(strings[8].trim().replaceAll("open : {2}", ""));
        double close = getDouble(strings[9].trim().replaceAll("close : {2}", ""));
        double high = getDouble(strings[10].trim().replaceAll("high : {2}", ""));
        double low = getDouble(strings[11].trim().replaceAll("low : {2}", ""));


        InfoIndicator infoIndicator =
                new InfoIndicator(period, preview, time, price, value, type, avg, dir, open, close, high, low);
        strategyFactory.onOff(infoIndicator);
//        arrayList.add(infoIndicator);
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
        TimeFrame[] timeFrames = TimeFrame.values();

        for (TimeFrame timeFrame : timeFrames) {
            if (timeFrame.toString().equalsIgnoreCase(string)) {
                return timeFrame;
            }
        }
        return null;

//        switch (string) {
//            case "M1" :
//                return TimeFrame.M1;
//            case "M5" :
//                return TimeFrame.M5;
//            case "M15" :
//                return TimeFrame.M15;
//            case "M30" :
//                return TimeFrame.M30;
//            case "H1" :
//                return TimeFrame.H1;
//            case "H4" :
//                return TimeFrame.H4;
//            case "D1" :
//                return TimeFrame.D1;
//            case "W1" :
//                return TimeFrame.W1;
//            case "MN1" :
//                return TimeFrame.MN1;
//            case "Y1" :
//                return TimeFrame.Y1;
//        }
//        return null;
    }



    private BidAsk getType(String string) {
        BidAsk[] bidAsks = BidAsk.values();

        for (BidAsk bidAsk : bidAsks) {
            if (bidAsk.toString().replaceAll("_", "").equalsIgnoreCase(string)) {
                return bidAsk;
            }
        }
        return null;

//        switch (string) {
//            case "OpenPosBidMinusSmall" :
//                return BidAsk.OPEN_POS_BID_MINUS_SMALL;
//            case "OpenPosAskMinusSmall" :
//                return BidAsk.OPEN_POS_ASK_MINUS_SMALL;
//            case "OpenPosAskPlusSmall" :
//                return BidAsk.OPEN_POS_ASK_PLUS_SMALL;
//            case "OpenPosBidPlusSmall" :
//                return BidAsk.OPEN_POS_BID_PLUS_SMALL;
//            case "DeltaMinVolBidHL" :
//                return BidAsk.DELTA_MIN_VOL_BID_HL;
//            case "DeltaMinVolAskHL" :
//                return BidAsk.DELTA_MIN_VOL_ASK_HL;
//            case "OpenPosMinusSmall" :
//                return BidAsk.OPEN_POS_MINUS_SMALL;
//            case "OpenPosPlusSmall" :
//                return BidAsk.OPEN_POS_PLUS_SMALL;
//            case "DeltaZSMinMinus" :
//                return BidAsk.DELTA_ZS_MIN_MINUS;
//            case "OpenPosAskMinus" :
//                return BidAsk.OPEN_POS_ASK_MINUS;
//            case "OpenPosBidMinus" :
//                return BidAsk.OPEN_POS_BID_MINUS;
//            case "OpenPosAskPlus" :
//                return BidAsk.OPEN_POS_ASK_PLUS;
//            case "DeltaZSMinPlus" :
//                return BidAsk.DELTA_ZS_MIN_PLUS;
//            case "OpenPosBidPlus" :
//                return BidAsk.OPEN_POS_BID_PLUS;
//            case "OpenPosMinusHL" :
//                return BidAsk.OPEN_POS_MINUS_HL;
//            case "OpenPosPlusHL" :
//                return BidAsk.OPEN_POS_PLUS_HL;
//            case "OIZSMinMinus" :
//                return BidAsk.OI_ZS_MIN_MINUS;
//            case "DeltaBidSmall" :
//                return BidAsk.DELTA_BID_SMALL;
//            case "DeltaAskSmall" :
//                return BidAsk.DELTA_ASK_SMALL;
//            case "DeltaZSMinus" :
//                return BidAsk.DELTA_ZS_MINUS;
//            case "OIZSMinPlus" :
//                return BidAsk.OI_ZS_MIN_PLUS;
//            case "OpenPosMinus" :
//                return BidAsk.OPEN_POS_MINUS;
//            case "OpenPosPlus" :
//                return BidAsk.OPEN_POS_PLUS;
//            case "DeltaZSPlus" :
//                return BidAsk.DELTA_ZS_PLUS;
//            case "DeltaBidHL" :
//                return BidAsk.DELTA_BID_HL;
//            case "DeltaAskHL" :
//                return BidAsk.DELTA_ASK_HL;
//            case "VolumeSmall" :
//                return BidAsk.VOLUME_SMALL;
//            case "OIZSMinus" :
//                return BidAsk.OI_ZS_MINUS;
//            case  "OIZSPlus" :
//                return BidAsk.OI_ZS_PLUS;
//            case "DeltaAsk" :
//                return BidAsk.DELTA_ASK;
//            case "DeltaBid" :
//                return BidAsk.DELTA_BID;
//            case "AskSmall" :
//                return BidAsk.ASK_SMALL;
//            case "BidSmall" :
//                return BidAsk.BID_SMALL;
//            case "Volume" :
//                return BidAsk.VOLUME;
//            case "Bid" :
//                return BidAsk.BID;
//            case "Ask" :
//                return BidAsk.ASK;
//        }
//        return null;
    }



//    public ArrayList<InfoIndicator> getList() {
//        return arrayList;
//    }

//    public void setList(ArrayList list) {
//        this.arrayList = list;
//    }





    //TEST
    private ArrayList<String> arrayListAllLevel;
    private synchronized void showAllAvailableLevels(String string) {

        if (arrayListAllLevel.size() < 1) arrayListAllLevel.add(string);
        else {
            for (String s : arrayListAllLevel) {

                String[] arr1 = s.split("\"type\": \"");
                String[] arr2 = string.split("\"type\": \"");
                String[] strings1 = arr1[1].split("\"");
                String[] strings2 = arr2[1].split("\"");

                // если хоть один объект не равен то прирываем цикл
                if (strings1[0].equals(strings2[0])) {
                    return;
                }
            }

            arrayListAllLevel.add(string);

            if (arrayListAllLevel.size() == 23) {
                for (String s : arrayListAllLevel) {
                    System.out.println(s);
                }
            }
        }
    }
}
