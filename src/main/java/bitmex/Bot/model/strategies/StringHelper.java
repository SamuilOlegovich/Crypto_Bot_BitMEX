package bitmex.Bot.model.strategies;

import bitmex.Bot.view.ConsoleHelper;

import java.text.SimpleDateFormat;
import java.util.Date;




public class StringHelper {



    public static synchronized Date getDate(String string) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateFromString = null;

        if (string.startsWith("\"") && string.endsWith("\"")) {
            String stringIn = string.replaceAll("\"", "").replace("time: ", "");

            try {
                dateFromString = simpleDateFormat.parse(stringIn);
            } catch (Exception e) {
                ConsoleHelper.writeMessage("неверный формат даты --- " + stringIn);
            }
            return dateFromString;

        } else {

            try {
                dateFromString = simpleDateFormat.parse(string);
            } catch (Exception e) {
                ConsoleHelper.writeMessage("неверный формат даты --- " + string);
            }

            return dateFromString;
        }
    }



    public static String giveData(String key, String in) {
        String out = null;

        if (in.startsWith("{") && in.endsWith("}")) {

        } else {

        }

        return out;
    }
}
