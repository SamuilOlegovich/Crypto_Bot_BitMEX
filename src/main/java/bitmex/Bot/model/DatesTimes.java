package bitmex.Bot.model;

import bitmex.Bot.model.Gasket;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class DatesTimes {

    public static String getDateTerminal() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateFormat.format(date);
        date.setTime(Gasket.getDateDifference() > 0
                ? date.getTime() + (1000 * 60 * 60 * Math.abs(Gasket.getDateDifference()))
                : date.getTime() - (1000 * 60 * 60 * Math.abs(Gasket.getDateDifference())));
        return dateFormat.format(date);
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateFormat.format(date);
        return dateFormat.format(date);
    }
}
