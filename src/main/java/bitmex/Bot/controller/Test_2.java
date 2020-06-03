package bitmex.Bot.controller;

import bitmex.Bot.model.enums.BidAsk;

import java.util.ArrayList;
import java.util.Collections;

public class Test_2 {

    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("\"type\": \"ASK\"");
        arrayList.add("\"type\": \"CSK\"");
        arrayList.add("\"type\": \"FSK\"");
        arrayList.add("\"type\": \"DSK\"");
        arrayList.add("\"type\": \"ASK\"");

        Collections.sort(arrayList);

        System.out.println(arrayList);
    }
}




