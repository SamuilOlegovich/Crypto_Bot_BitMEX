package bitmex.Bot.model;

import bitmex.Bot.model.serverAndParser.InfoIndicator;

import java.util.*;

import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.BIAS;
import static bitmex.Bot.model.enums.TimeFrame.M5;
import static bitmex.Bot.model.enums.TypeData.*;
import static bitmex.Bot.model.enums.BidAsk.*;




public class CompareHelper {

    private static SortPriceRemainingLevels sortPriceRemainingLevels = new SortPriceRemainingLevels();
    private static SortTheAlphabet sortTheAlphabet = new SortTheAlphabet();
    private static SortPrice sortPrice = new SortPrice();
    private static SortSize sortSize = new SortSize();
    private static SortTime sortTime = new SortTime();




    public static ArrayList<String> removeExtraLevels(ArrayList<String> in1, ArrayList<String> in2) {
        ArrayList<String> listIn = new ArrayList<>(in1);
        ArrayList<String> listIn2 = new ArrayList<>(in2);

        // чистим от оставшихся предварительных исчезнувших уровняй
        ArrayList<Integer> indexArrayList = new ArrayList<>();

        for (String stringOne : listIn) {
            int bias = 0;

            if (!stringOne.startsWith(NULL.toString()) && !stringOne.startsWith(BUY.toString())
                    && !stringOne.startsWith(BIAS.toString())) {

                for (int i = listIn.indexOf(stringOne) + 1; i < listIn.size(); i++) {
                    String stringTwo = listIn2.get(i);
                    bias = bias + (stringTwo.startsWith(BIAS.toString()) ? 1 : 0);

                    if (bias == 1) {
                        // если мы сюда зашли то значит мы перешли в нужны нам блок
                        // начинаем сравнения с его строками
                        if (!stringTwo.startsWith(NULL.toString()) && !stringTwo.startsWith(BUY.toString())
                                && !stringTwo.startsWith(BIAS.toString())) {

                            // эти уровни есть всегда их не надо уничтожать
                            if (!giveData(type, stringOne).equals(OI_ZS_MIN_MINUS.toString())
                                    && !giveData(type, stringOne).equals(OI_ZS_MIN_PLUS.toString())
                                    && !giveData(type, stringOne).equals(DELTA_ZS_MIN_PLUS.toString())
                                    && !giveData(type, stringOne).equals(DELTA_ZS_MIN_MINUS.toString())) {

                                // M5 == M5  1 == 1  ASK == ASK
                                if (giveData(period, stringOne).equals(giveData(period, stringTwo))
                                        && giveData(preview, stringOne).equals(giveData(preview, stringTwo))
                                        && giveData(type, stringOne).equals(giveData(type, stringTwo))) {
                                    indexArrayList.add(listIn.indexOf(stringOne));

                                    // M5 == M5  1 != 1(0)  ASK == ASK
                                } else if (giveData(period, stringOne).equals(giveData(period, stringTwo))
                                        && (!giveData(preview, stringOne).equals(giveData(preview, stringTwo))
                                        && giveData(preview, stringOne).equals("1"))
                                        && giveData(type, stringOne).equals(giveData(type, stringTwo))) {
                                    indexArrayList.add(listIn.indexOf(stringOne));

                                    // M5 == M5  (0)1 != 1  ASK == ASK
                                } else if (giveData(period, stringOne).equals(giveData(period, stringTwo))
                                        && (!giveData(preview, stringOne).equals(giveData(preview, stringTwo))
                                        && giveData(preview, stringOne).equals("0"))
                                        && giveData(type, stringOne).equals(giveData(type, stringTwo))) {
                                    indexArrayList.add(listIn.indexOf(stringTwo));

                                    // M5 != M5(M15)  1 == 1  ASK == ASK
                                } else if ((!giveData(period, stringOne).equals(giveData(period, stringTwo))
                                        && giveData(period, stringOne).equals(M5.toString()))
                                        && giveData(preview, stringOne).equals(giveData(preview, stringTwo))
                                        && giveData(type, stringOne).equals(giveData(type, stringTwo))) {
                                    indexArrayList.add(listIn.indexOf(stringOne));

                                    // M5 != M5(M15)  1 != 1(0)  ASK == ASK
                                } else if ((!giveData(period, stringOne).equals(giveData(period, stringTwo))
                                        && giveData(period, stringOne).equals(M5.toString()))
                                        && (!giveData(preview, stringOne).equals(giveData(preview, stringTwo))
                                        && giveData(preview, stringOne).equals("1"))
                                        && giveData(type, stringOne).equals(giveData(type, stringTwo))) {
                                    indexArrayList.add(listIn.indexOf(stringOne));

                                    // M5 != M5(M15)  (0)1 != 1  ASK == ASK
                                } else if ((!giveData(period, stringOne).equals(giveData(period, stringTwo))
                                        && giveData(period, stringOne).equals(M5.toString()))
                                        && (!giveData(preview, stringOne).equals(giveData(preview, stringTwo))
                                        && giveData(preview, stringOne).equals("0"))
                                        && giveData(type, stringOne).equals(giveData(type, stringTwo))) {
                                    indexArrayList.add(listIn.indexOf(stringTwo));

                                    // M5 != M5(M15)  (0)1 != 1  ASK == ASK
                                } else if ((!giveData(period, stringOne).equals(giveData(period, stringTwo))
                                        && giveData(period, stringOne).equals(M5.toString()))
                                        && (!giveData(preview, stringOne).equals(giveData(preview, stringTwo))
                                        && giveData(preview, stringOne).equals("0"))
                                        && giveData(type, stringOne).equals(giveData(type, stringTwo))) {
                                    indexArrayList.add(listIn.indexOf(stringTwo));
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
        HashSet<Integer> hashSet = new HashSet<>(indexArrayList);
        indexArrayList.clear();
        indexArrayList.addAll(hashSet);
        Collections.reverse(indexArrayList);

        for (Integer index : indexArrayList) {
            listIn.remove((int) index);
        }

        return listIn;
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
//            if (result > 0) return 1;
//            if (result < 0) return -1;
//            return 0;
            return Double.compare(result, 0);
        }
    }



    private static class SortPrice implements Comparator<InfoIndicator> {
        @Override
        public int compare(InfoIndicator o1, InfoIndicator o2) {
            double result = o2.getPrice() - o1.getPrice();
//            if (result > 0) return 1;
//            if (result < 0) return -1;
//            return 0;
            return Double.compare(result, 0);
        }
    }



    private static class SortTime implements Comparator<InfoIndicator> {
        @Override
        public int compare(InfoIndicator o1, InfoIndicator o2) {
            long result = o1.getTime().getTime() - o2.getTime().getTime();
//            if (result > 0) return 1;
//            if (result < 0) return -1;
//            return 0;
            return Long.compare(result, 0);
        }
    }



    private static class SortPriceRemainingLevels implements Comparator<String> {
        @Override
        public synchronized int compare(String o1, String o2) {
//            String[] strings1 = o1.split(",");
//            String[] strings2 = o2.split(",");

//            double result = Double.parseDouble(strings2[3].replaceAll("\"", "")
//                    .replaceAll("price: ", ""))
//                    - Double.parseDouble(strings1[3].replaceAll("\"", "")
//                    .replaceAll("price: ", ""));

            double result = Double.parseDouble(StringHelper.giveData(price, o2))
                    - Double.parseDouble(StringHelper.giveData(price, o1));
//            if (result > 0) return 1;
//            if (result < 0) return -1;
//            return 0;
            return Double.compare(result, 0);
        }
    }


    public static SortPriceRemainingLevels getSortPriceRemainingLevels() {
        return sortPriceRemainingLevels;
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
