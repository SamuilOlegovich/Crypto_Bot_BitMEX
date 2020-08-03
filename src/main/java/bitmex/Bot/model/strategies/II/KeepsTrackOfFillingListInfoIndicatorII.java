package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Date;




public class KeepsTrackOfFillingListInfoIndicatorII extends Thread {
    private static KeepsTrackOfFillingListInfoIndicatorII keepsTrackOfFillingListInfoIndicatorII;

    private ListensLooksAndCompares listensLooksAndCompares;
    private ListensToLooksAndFills listensToLooksAndFills;


    public KeepsTrackOfFillingListInfoIndicatorII() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- Класс Keeps Track Of Filling List InfoIndicator II начал работать");
        start();
    }


    public static KeepsTrackOfFillingListInfoIndicatorII getInstance() {
        if (keepsTrackOfFillingListInfoIndicatorII == null) {
            keepsTrackOfFillingListInfoIndicatorII = new KeepsTrackOfFillingListInfoIndicatorII();
        }
        return keepsTrackOfFillingListInfoIndicatorII;
    }

    public void setListensLooksAndCompares(ListensLooksAndCompares listensLooksAndCompares) {
        this.listensLooksAndCompares = listensLooksAndCompares;
    }

    public void setListensToLooksAndFills(ListensToLooksAndFills listensToLooksAndFills) {
        this.listensToLooksAndFills = listensToLooksAndFills;
    }



    @Override
    public void run() {
        Timer time2 = new Timer();
        Timer time = new Timer();

        DateFormat dateFormat = new SimpleDateFormat("mm:ss");
        Date date = new Date();

        String[] strings = dateFormat.format(date).split(":");

        int minute = (5 - (Integer.parseInt(strings[0]) % 5)) * 60 * 1000;
        int seconds = ((60 - (Integer.parseInt(strings[1]))) == 60
                ? 0 : Integer.parseInt(strings[1])) * 1000;
        long timeStart = minute - seconds;

        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (listensLooksAndCompares != null) {
                    listensLooksAndCompares.setPriceNow(Gasket.getBitmexQuote().getBidPrice());
                    listensLooksAndCompares.setTimeNow(DatesTimes.getDateTerminalLong());
                    listensLooksAndCompares.startListSortedAndCompares(true);
                }

                if (listensToLooksAndFills != null) {
                    listensToLooksAndFills.setPriceNow(Gasket.getBitmexQuote().getBidPrice());
                    listensToLooksAndFills.setTimeNow(DatesTimes.getDateTerminalLong());
                    listensToLooksAndFills.startListSorter(true);
                }
            }
        }, timeStart, 1000 * 60 * 5);


        time2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isTime()) {
                    if (listensLooksAndCompares != null &&  listensLooksAndCompares.getSizeListInfoIndicator() > 0) {
                        listensLooksAndCompares.startListSortedAndCompares(false);
                    }

                    if (listensToLooksAndFills != null &&  listensToLooksAndFills.getSizeListInfoIndicator() > 0) {
                        listensToLooksAndFills.startListSorter(false);
                    }
                }
            }
        }, timeStart, 1000 * 10);
    }



    // Проверяем что бы наши пакеты данных не выбивалис из пятиминутки
    private synchronized boolean isTime() {
        String string = DatesTimes.getDateTerminal();
        String[] strings = string.split(":");
        double seconds = Double.parseDouble(strings[1] + "." + strings[2]);

        if (seconds > 00.05 && seconds < 4.98) {
            return false;
        } else if (seconds > 5.05 && seconds < 9.98) {
            return false;
        } else if (seconds > 10.05 && seconds < 14.98) {
            return false;
        } else if (seconds > 15.05 && seconds < 19.98) {
            return false;
        } else if (seconds > 20.05 && seconds < 24.98) {
            return false;
        } else if (seconds > 25.05 && seconds < 29.98) {
            return false;
        } else if (seconds > 30.05 && seconds < 34.98) {
            return false;
        } else if (seconds > 35.05 && seconds < 39.98) {
            return false;
        } else if (seconds > 40.05 && seconds < 44.98) {
            return false;
        } else if (seconds > 45.05 && seconds < 49.98) {
            return false;
        } else if (seconds > 50.05 && seconds < 54.98) {
            return false;
        } else if (seconds > 55.05 && seconds < 59.98) {
            return false;
        } else {
            return true;
        }
    }
}
