package bitmex.Bot.controller;



import java.util.*;

public class Test {

    public static void main(String[] args) {
//
//        String string = "{\"period\": \"M30\",\"preview\": \"0\",\"time\": \"05.07.2020 09:56:00\",\"price\": \"8991.5\",\"value\": \"-1716204\",\"type\": \"OPEN_POS_BID_PLUS\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9040,0\",\"close\": \"8994,0\",\"high\": \"9040,0\",\"low\": \"8960,0\"}";
//        System.out.println(giveData(TypeData.period, string));
//        System.out.println(giveData(TypeData.preview, string));
//        System.out.println(giveData(TypeData.time, string));
//        System.out.println(Double.parseDouble(Objects.requireNonNull(giveData(TypeData.price, string))));
//        System.out.println(giveData(TypeData.value, string));
//        System.out.println(giveData(TypeData.type, string));
//        System.out.println(giveData(TypeData.avg, string));
//        System.out.println(giveData(TypeData.dir, string));
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(8);
        arrayList.add(0);
        arrayList.add(7);
        arrayList.add(7);
        arrayList.add(9);
        arrayList.add(10);
        arrayList.add(888);
        arrayList.add(18);
        arrayList.add(2);
        arrayList.add(1);

        TreeSet<Integer> hashSet = new TreeSet<>(arrayList);
        arrayList.clear();
        arrayList.addAll(hashSet);
        Collections.reverse(arrayList);
        System.out.println(arrayList);





    }
}
