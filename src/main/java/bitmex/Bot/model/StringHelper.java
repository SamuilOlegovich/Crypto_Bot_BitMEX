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
}
