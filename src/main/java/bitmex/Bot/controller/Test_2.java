package bitmex.Bot.controller;


import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

import static bitmex.Bot.model.enums.TimeFrame.M15;
import static bitmex.Bot.model.enums.TimeFrame.M30;

public class Test_2 {

    public static void main(String[] args) {

//      Date date = new Date();
//        try {
//            Thread.sleep(1000 * 3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Date date1 = new Date();
//
//        System.out.println(date.getTime() - date1.getTime() + "");
        System.out.println(M15 == M30);
    }

}


