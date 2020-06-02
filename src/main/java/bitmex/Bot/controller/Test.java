package bitmex.Bot.controller;


import bitmex.Bot.model.DatesTimes;


import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Date;




public class Test {

    public static void main(String[] args) {
        Timer time = new Timer();
        DateFormat dateFormat = new SimpleDateFormat("mm:ss");
        Date date = new Date();
        String[] strings = dateFormat.format(date).split(":");
        int minute = (5 - (Integer.parseInt(strings[0]) % 5)) * 60 * 1000;
        int seconds = ((60 - (Integer.parseInt(strings[1]))) == 60 ? 0 : Integer.parseInt(strings[1])) * 1000;
        long timeStart = minute - seconds - 3000;
        System.out.println(dateFormat.format(date) + "=="
                + (minute/60)/1000 + "=="
                + (seconds)/1000);
        System.out.println(dateFormat.format(date) + "=="
                + (timeStart / 60) / 1000 + "=="
                + (timeStart % 60));

        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("7 --- " + DatesTimes.getDateTerminal() + " --- " + dateFormat.format(date));
            }
        }, timeStart, 1000 * 2);
    }
}
