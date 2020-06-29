package bitmex.Bot.model;





public class StringHelper {


    public static String giveData(String key, String in) {
        String out = null;

        if (in.startsWith("{") && in.endsWith("}")) {

        } else {
            String[] strings = in.split("===");

            for (int i = 0; i < strings.length; i++) {

                if (strings[i].equalsIgnoreCase(key)) {
                    out = strings[i + 1];
                    break;
                }
            }
        }
        return out;
    }
}
