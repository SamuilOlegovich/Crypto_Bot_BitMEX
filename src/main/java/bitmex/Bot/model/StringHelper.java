package bitmex.Bot.model;





public class StringHelper {


    public static synchronized String giveData(String key, String in) {
        String out = null;

        if (in.startsWith("{") && in.endsWith("}")) {
            String string = in.replaceAll("\\{", "").replaceAll("}", "")
                    .replaceAll("\"", "").replaceAll(":", "");

            String[] strings = string.split(",");

            for (String s : strings) {
                if (s.startsWith(key)) {
                    out = s.replaceAll(key, "").trim();
                    break;
                }
            }
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
