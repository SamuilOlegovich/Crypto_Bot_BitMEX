package bitmex.Bot.model.strategies.II;

import java.util.ArrayList;

public class SavedPatterns {
    private static ArrayList<ArrayList<String>> listsPriceSell = new ArrayList<>();
    private static ArrayList<ArrayList<String>> listsPriceBuy = new ArrayList<>();


    public static ArrayList<ArrayList<String>> getListsPriceSell() {
        return listsPriceSell;
    }

    public static void addListsPriceSell(ArrayList<String> listsPriceSell) {
        SavedPatterns.isThereSuchCombination(listsPriceSell, false);
    }

    public static ArrayList<ArrayList<String>> getListsPriceBuy() {
        return listsPriceBuy;
    }

    public static void addListsPriceBuy(ArrayList<String> listsPriceBuy) {
        SavedPatterns.isThereSuchCombination(listsPriceBuy, true);
    }


    private static void isThereSuchCombination(ArrayList<String> arrayList, boolean buyOrSell) {
        boolean result = true;

        for (ArrayList<String> stringArrayList : buyOrSell
                ? SavedPatterns.listsPriceBuy : SavedPatterns.listsPriceSell) {

            if (stringArrayList.size() == arrayList.size()) {

                for (int i = 0; i < arrayList.size(); i++) {
                    String[] arr1 = stringArrayList.get(i).split("\"preview\": \"");
                    String[] arr2 = arrayList.get(i).split("\"preview\": \"");
                    String[] strings1 = arr1[1].split("\"");
                    String[] strings2 = arr2[1].split("\"");

                    if (!strings1[0].equals(strings2[0])) {
                        result = false;
                        break;
                    }
                }

                if (result) {
                    stringArrayList.add(0, SavedPatterns.setPriority(stringArrayList.get(0)));
                    return;
                } else { result = true; }
            }
        }

        arrayList.add(0, SavedPatterns.setPriority(arrayList.get(0)));

        if (buyOrSell) {
            SavedPatterns.listsPriceBuy.add(arrayList);
        } else {
            SavedPatterns.listsPriceSell.add(arrayList);
        }
    }

    private static String setPriority(String string) {
        String result = "";
        String[] strings = string.split("===");

        if (strings.length > 1) {
            int priority = Integer.parseInt(strings[1]) + 1;
            result = strings[0] + "===" + priority;
        } else {
            result = string + "===" + 1;
        }
        return result;
    }
}
