package bitmex.Bot.controller;


import bitmex.Bot.model.enums.BidAsk;
import bitmex.Bot.model.enums.TypeOpenOrders;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;

public class Test_2 {

    public static void main(String[] args) {
    Test_2 test_2 = new Test_2();
    SortTheAlphabet sortTheAlphabet = new SortTheAlphabet();

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(BidAsk.OPEN_POS_ASK_MINUS.toString());
        arrayList.add(BidAsk.ASK_SMALL.toString());
        arrayList.add(BidAsk.OPEN_POS_MINUS_HL.toString());
        arrayList.add(BidAsk.OPEN_POS_ASK_MINUS_SMALL.toString());
        arrayList.add(BidAsk.ASK_SMALL.toString());
        arrayList.add(BidAsk.DELTA_BID.toString());
        arrayList.add(BidAsk.DELTA_BID_HL.toString());
        arrayList.add(BidAsk.DELTA_MIN_VOL_BID_HL.toString());
        arrayList.add(BidAsk.VOLUME_SMALL.toString());
        arrayList.add(BidAsk.OPEN_POS_BID_PLUS.toString());

        arrayList.sort(sortTheAlphabet);

        System.out.println(arrayList);

    }


    private static class SortTheAlphabet implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int result = o1.compareTo(o2);
            return Integer.compare(result, 0);
        }
    }
}




