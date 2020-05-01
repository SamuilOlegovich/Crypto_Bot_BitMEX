package bitmex.Bot.controller;


import java.util.ArrayList;

public class Test_2 {



    public static void main(String[] args) {
        boolean result = true;
        for (int i = 0; i < 1000; i++) {
            if (i > 0) {
                for (int j = 0; j < 100; j++) {

                    if (i == 5 && j == 5) {
                        result = false;
                        break;
                    } else if (j == 20) break;
                }

                if (result) {
                    System.out.println("I === " + i);
                } else { result = true; }

            }
        }
        System.out.println("END");
    }
}




