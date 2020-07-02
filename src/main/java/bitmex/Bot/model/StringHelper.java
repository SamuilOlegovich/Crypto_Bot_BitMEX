package bitmex.Bot.model;





public class StringHelper {


    public static synchronized String giveData(String key, String in) {

        if (in.startsWith("{") && in.endsWith("}")) {
            String string = in.replaceAll("\\{", "").replaceAll("}", "")
                    .replaceAll("\"", "").replaceAll(":", "");

            String[] strings = string.split(",");

            for (String s : strings) {
                if (s.startsWith(key)) {
                    return s.replaceAll(key, "").trim();
                }
            }
        } else {
            String[] strings = in.split("===");
            for (int i = 0; i < strings.length; i++) {
                if (strings[i].equalsIgnoreCase(key)) {
                    return strings[i + 1];
                }
            }
        }
        return null;
    }
}
