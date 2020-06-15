package bitmex.Bot.model.strategies.IIUser;


import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Date;



// следит за наполнением листа и если наполнение больше нет то сортирует его и запускает нужные методы
public class KeepsTrackOfFillingListInfoIndicatorUser extends Thread {
    private ListensLooksAndComparesUser listensLooksAndComparesUser;


    public KeepsTrackOfFillingListInfoIndicatorUser(ListensLooksAndComparesUser listensLooksAndComparesUser) {
        this.listensLooksAndComparesUser = listensLooksAndComparesUser;
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- Класс KeepsTrackOfFillingListInfoIndicatorUser начал работать");
        start();
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
                listensLooksAndComparesUser.setPriceNow(Gasket.getBitmexQuote().getBidPrice());
                listensLooksAndComparesUser.setTimeNow(DatesTimes.getDateTerminalLong());
                listensLooksAndComparesUser.startListSortedAndCompares(true);
            }
        }, timeStart, 1000 * 60 * 5);


        time2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isTime() && listensLooksAndComparesUser.getSizeListInfoIndicator() > 0) {
                    listensLooksAndComparesUser.startListSortedAndCompares(false);
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

