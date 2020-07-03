package bitmex.Bot.model;


import bitmex.Bot.model.enums.TypeData;

public class StringHelper {


    public static synchronized String giveData(TypeData key, String in) {

        if (in.startsWith("{") && in.endsWith("}")) {
            String string = in.replaceAll("\\{", "").replaceAll("}", "")
                    .replaceAll("\"", "").replaceAll(":", "");

            String[] strings = string.split(",");

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
        return null;
    }



    /////////// PARSER ///////////



    public static synchronized String convertStringForUser(String string) {
//        String in = string.replaceAll("\\{", "");
//
//        in = in.replaceAll("}", "");
//        in = in.replaceAll("\"", " ");
//
//        String[] strings = in.split(" , ");
//
//        String period = strings[0].trim().replaceAll("period : {2}","");
//        String preview = strings[1].trim().replaceAll("preview : {2}", "");
//        String time = strings[2].trim().replaceAll("time : {2}", "");
//        String price = strings[3].trim().replaceAll("price : {2}", "");
//        String value = strings[4].trim().replaceAll("value : {2}", "");
//        String type = strings[5].trim().replaceAll("type : {2}", "");
//        String avg = strings[6].trim().replaceAll("avg : {2}", "");
//        String dir = strings[7].trim().replaceAll("dir : {2}", "");
//        String open = strings[8].trim().replaceAll("open : {2}", "");
//        String close = strings[9].trim().replaceAll("close : {2}", "");
//        String high = strings[10].trim().replaceAll("high : {2}", "");
//        String low = strings[11].trim().replaceAll("low : {2}", "");


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
