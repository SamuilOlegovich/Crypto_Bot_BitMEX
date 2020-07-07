package bitmex.Bot.model;


import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.view.ConsoleHelper;





public class StringHelper {


    public static synchronized String giveData(TypeData key, String in) {

        if (in.startsWith("{\"" + TypeData.period.toString() + "\":")) {
            String[] strings = in.split("\",\"");
            strings[0] = strings[0].replaceAll("\\{", "");
            strings[strings.length - 1] = strings[strings.length - 1].replaceAll("}", "");

            for (int i = 0; i < strings.length; i++) {
                strings[i] = strings[i].replaceAll("\"", "");
                strings[i] = strings[i].replaceAll(":", "");
            }

            for (String s : strings) {
                if (s.startsWith(key.toString())) {
                    return s.replaceAll(key.toString(), "").trim();
                }
            }
        } else {
            String[] strings = in.split("===");

            for (int i = 0; i < strings.length; i++) {
                if (strings[i].equalsIgnoreCase(key.toString())) {
                    return strings[i + 1];
                }
            }
        }

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " === " + key.toString() + " --- "
                + in + "=============================================================================================");
        return null;
    }



    /////////// PARSER ///////////



    public static synchronized String convertStringForUser(String string) {

        String period = giveData(TypeData.period, string);
        String preview = giveData(TypeData.preview, string);
        String time = giveData(TypeData.time, string);
        String price = giveData(TypeData.price, string);
        String value = giveData(TypeData.value, string);
        String type = giveData(TypeData.type, string);
        String avg = giveData(TypeData.avg, string);
        String dir = giveData(TypeData.dir, string);
        String open = giveData(TypeData.open, string);
        String close = giveData(TypeData.close, string);
        String high = giveData(TypeData.high, string);
        String low = giveData(TypeData.low, string);

        return "period===" + period + "===preview===" + preview + "===time===" + time + "===price===" + price
                + "===value===" + value + "===type===" + type + "===avg===" + avg + "===dir===" + dir
                + "===open===" + open + "===close===" + close + "===high===" + high
                + "===low===" + low
                + "\n";
    }
}
