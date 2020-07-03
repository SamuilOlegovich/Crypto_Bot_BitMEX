package bitmex.Bot.model;

import bitmex.Bot.model.serverAndParser.InfoIndicator;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.TreeSet;

import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.BIAS;
import static bitmex.Bot.model.enums.TypeData.*;




public class CompareHelper {

    private static SortTheAlphabet sortTheAlphabet = new SortTheAlphabet();
    private static SortPrice sortPrice = new SortPrice();
    private static SortSize sortSize = new SortSize();
    private static SortTime sortTime = new SortTime();




    public static ArrayList<String> removeExtraLevels(ArrayList<String> in1, ArrayList<String> in2) {
        ArrayList<String> arrayListIn = new ArrayList<>(in1);
        ArrayList<String> inArrayList = new ArrayList<>(in2);

        // чистим от оставшихся предварительных исчезнувших уровняй
        ArrayList<Integer> indexArrayList = new ArrayList<>();

        for (String stringOne : arrayListIn) {
            int bias = 0;

            if (!stringOne.startsWith(BUY.toString()) && !stringOne.startsWith(BIAS.toString())) {
                String[] oneStrings = stringOne.split(",");
                String[] twoStrings;

                for (int i = arrayListIn.indexOf(stringOne) + 1; i < arrayListIn.size(); i++) {
                    String stringTwo = inArrayList.get(i);

                    bias = bias + (stringTwo.startsWith(BIAS.toString()) ? 1 : 0);


                    if (bias == 1) {
                        // если мы сюда зашли то значит мы перешли в нужны нам блок
                        // начинаем сравнения с его строками
                        twoStrings = stringTwo.split(",");

                        if (oneStrings.length == twoStrings.length) {

                            // эти уровни есть всегда их не надо уничтожать
                            if (!oneStrings[5].equals("\"type\": \"OI_ZS_MIN_MINUS\"")
                                    && !oneStrings[5].equals("\"type\": \"OI_ZS_MIN_PLUS\"")
                                    && !oneStrings[5].equals("\"type\": \"DELTA_ZS_MIN_PLUS\"")
                                    && !oneStrings[5].equals("\"type\": \"DELTA_ZS_MIN_MINUS\"")) {

                                // M5 == M5  1 == 1  ASK == ASK
                                if (oneStrings[0].equals(twoStrings[0])
                                        && oneStrings[1].equals(twoStrings[1])
                                        && oneStrings[5].equals(twoStrings[5])) {
                                    indexArrayList.add(arrayListIn.indexOf(stringOne));

                                    // M5 == M5  1 != 1(0)  ASK == ASK
                                } else if (oneStrings[0].equals(twoStrings[0])
                                        && (!oneStrings[1].equals(twoStrings[1])
                                        && oneStrings[1].equals("1"))
                                        && oneStrings[5].equals(twoStrings[5])) {
                                    indexArrayList.add(arrayListIn.indexOf(stringOne));

                                    // M5 == M5  (0)1 != 1  ASK == ASK
                                } else if (oneStrings[0].equals(twoStrings[0])
                                        && (!oneStrings[1].equals(twoStrings[1])
                                        && oneStrings[1].equals("0"))
                                        && oneStrings[5].equals(twoStrings[5])) {
                                    indexArrayList.add(arrayListIn.indexOf(stringTwo));

                                    // M5 != M5(M15)  1 == 1  ASK == ASK
                                } else if ((!oneStrings[0].equals(twoStrings[0])
                                        && oneStrings[0].equals("M5"))
                                        && oneStrings[1].equals(twoStrings[1])
                                        && oneStrings[5].equals(twoStrings[5])) {
                                    indexArrayList.add(arrayListIn.indexOf(stringOne));

                                    // M5 != M5(M15)  1 != 1(0)  ASK == ASK
                                } else if ((!oneStrings[0].equals(twoStrings[0])
                                        && oneStrings[0].equals("M5"))
                                        && (!oneStrings[1].equals(twoStrings[1])
                                        && oneStrings[1].equals("1"))
                                        && oneStrings[5].equals(twoStrings[5])) {
                                    indexArrayList.add(arrayListIn.indexOf(stringOne));

                                    // M5 != M5(M15)  (0)1 != 1  ASK == ASK
                                } else if ((!oneStrings[0].equals(twoStrings[0])
                                        && oneStrings[0].equals("M5"))
                                        && (!oneStrings[1].equals(twoStrings[1])
                                        && oneStrings[1].equals("0"))
                                        && oneStrings[5].equals(twoStrings[5])) {
                                    indexArrayList.add(arrayListIn.indexOf(stringTwo));

                                    // M5 != M5(M15)  (0)1 != 1  ASK == ASK
                                } else if ((!oneStrings[0].equals(twoStrings[0])
                                        && twoStrings[0].equals("M5"))
                                        && (!oneStrings[1].equals(twoStrings[1])
                                        && oneStrings[1].equals("0"))
                                        && oneStrings[5].equals(twoStrings[5])) {
                                    indexArrayList.add(arrayListIn.indexOf(stringTwo));
                                }
                            }
                        }
                    } else if (bias == 2) {
                        break;
                    }
                }
            }
        }

        // если каким-то образом будет два одинаковых индекса, так мы их нивилируем
        TreeSet<Integer> treeSet = new TreeSet<>(indexArrayList);
        indexArrayList.clear();
        indexArrayList.addAll(treeSet);
        Collections.reverse(indexArrayList);

        for (Integer index : indexArrayList) {
            arrayListIn.remove((int) index);
        }

        return arrayListIn;
    }



    private static class SortTheAlphabet implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int result = giveData(type, o1).compareTo(giveData(type, o2));
            return Integer.compare(result, 0);
        }
    }



    private static class SortSize implements Comparator<ArrayList<String>> {
        @Override
        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            double result = o1.size() - o2.size();
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }



    private static class SortPrice implements Comparator<InfoIndicator> {
        @Override
        public int compare(InfoIndicator o1, InfoIndicator o2) {
            double result = o2.getPrice() - o1.getPrice();

            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }



    private static class SortTime implements Comparator<InfoIndicator> {
        @Override
        public int compare(InfoIndicator o1, InfoIndicator o2) {
            long result = o1.getTime().getTime() - o2.getTime().getTime();

            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }




    public static SortTheAlphabet getSortTheAlphabet() {
        return sortTheAlphabet;
    }

    public static SortPrice getSortPrice() {
        return sortPrice;
    }

    public static SortSize getSortSize() {
        return sortSize;
    }

    public static SortTime getSortTime() {
        return sortTime;
    }
}
