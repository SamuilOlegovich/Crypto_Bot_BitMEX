package bitmex.Bot.model.serverAndParser;


import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.StrategyFactory;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.enums.TimeFrame;
import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.enums.BidAsk;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.view.ConsoleHelper;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static bitmex.Bot.model.StringHelper.giveData;


public class ParserString {
    private StrategyFactory strategyFactory;
    private ArrayList<String> arrayList;
    private boolean linkWithIndicator;
    private boolean show;



    public ParserString() {
        this.strategyFactory = StrategyFactory.getInstance();
        this.show = true;
        this.linkWithIndicator = true;

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- ПАРСЕР ЗАПУЩЕН");
    }

    public synchronized void parserStringJson(String string) {
        new WriterString(string);
//        ConsoleHelper.writeMessage("Входящаа строка ---- " + string);

        if (linkWithIndicator) {
            if (string.length() > 20) {
                linkWithIndicator = false;
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                        + " --- СВЯЗЬ С ИНДИКАТОРОМ УСТАНОВЛЕНА");
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                        + " --- " + string);
            }
        }

        if (show && Gasket.isShowAllLevels()) {
            showAllAvailableLevels(string);
        }

        double price = Double.parseDouble(giveData(TypeData.price, string));
        double close = Double.parseDouble(giveData(TypeData.close, string));
        TimeFrame period = getTimeFrame(giveData(TypeData.period, string));
        int preview = Integer.parseInt(giveData(TypeData.preview, string));
        double open = Double.parseDouble(giveData(TypeData.open, string));
        double high = Double.parseDouble(giveData(TypeData.high, string));
        double low = Double.parseDouble(giveData(TypeData.low, string));
        long value = Long.parseLong(giveData(TypeData.value, string));
        int dir = Integer.parseInt(giveData(TypeData.dir, string));
        long avg = Long.parseLong(giveData(TypeData.avg, string));
        BidAsk type = getType(giveData(TypeData.type, string));
        Date time = getDate(giveData(TypeData.time, string));

        InfoIndicator infoIndicator =
                new InfoIndicator(period, preview, time, price, value, type, avg, dir, open, close, high, low);
        strategyFactory.onOff(infoIndicator);
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
    }



    private BidAsk getType(String string) {
        BidAsk[] bidAsks = BidAsk.values();

        for (BidAsk bidAsk : bidAsks) {
            if (bidAsk.toString().replaceAll("_", "").equalsIgnoreCase(string)) {
                return bidAsk;
            }
        }
        return null;
    }


    private void showAllAvailableLevels(String string) {
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            arrayList.add(string);
        } else {
            for (String s : arrayList) {
                if (StringHelper.giveData(TypeData.type, s)
                        .equalsIgnoreCase(StringHelper.giveData(TypeData.type, string))) {
                    return;
                }
            }
            arrayList.add(string);
            if (arrayList.size() == Gasket.getTotalNumberOfAllLevels()) {
                StringBuilder stringBuilder = new StringBuilder("\n\n");
                for (String s : arrayList) {
                    stringBuilder.append(s + "\n");
                }
                stringBuilder.append("\n\n");
                ConsoleHelper.writeMessage(stringBuilder.toString());
                show = false;
            }
        }
    }
}
