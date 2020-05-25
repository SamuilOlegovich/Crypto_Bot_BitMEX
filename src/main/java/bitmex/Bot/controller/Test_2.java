package bitmex.Bot.controller;



import bitmex.Bot.model.enums.BidAsk;

import java.util.*;

public class Test_2 {



    public static void main(String[] args) {
        List<String> treeSet = new ArrayList<>();
        treeSet.add(BidAsk.VOLUME.toString());
        treeSet.add(BidAsk.ASK_SMALL.toString());
        treeSet.add(BidAsk.DELTA_BID.toString());
        treeSet.add(BidAsk.OPEN_POS_ASK_MINUS_SMALL.toString());
//        System.out.println(treeSet);
//        Collections.reverse(treeSet);
//        Set<Integer> set = new TreeSet<>(treeSet);
//        treeSet.clear();
//        treeSet.addAll(set);
//        Collections.reverse(treeSet);
//        System.out.println(treeSet);

//        BidAsk bidAsk = BidAsk.OI_ZS_MIN_MINUS;

        System.out.println(treeSet);
        System.out.println();
        Collections.sort(treeSet);
        System.out.println(treeSet);


    }
}




