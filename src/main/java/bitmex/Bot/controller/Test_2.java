package bitmex.Bot.controller;


import bitmex.Bot.model.Gasket;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class Test_2 {
    void run() {
        String testFilePath = getResourcePath("test");
        System.out.println(testFilePath);
    }

    String getResourcePath(String file) {
        return getClass().getResource("").getPath().replaceAll("controller/", "Logs/");
    }

    public static void main(String[] args) {
        new Test_2().run();
    }

}


