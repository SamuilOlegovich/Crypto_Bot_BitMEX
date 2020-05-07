package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.SaveRestoreHelper;
import bitmex.Bot.model.Gasket;

import java.util.ArrayList;

public class ReadAndSavePatterns {

    public static void createSavedPatterns() {
        SavedPatterns savedPatterns = SavedPatterns.getInstance();
        Gasket.setSavedPatternsClass(savedPatterns);
        SaveRestoreHelper.restoreSavedPatternsClass();
    }

    public static void saveSavedPatterns() {
        SaveRestoreHelper.saveSavedPatternsClass();
    }

//    System.out.println("START");
//        for (
//    ArrayList<String> arr : savedPatterns.listsPricePatterns) {
//        for (String s : arr)
//            System.out.print(s);
//        System.out.println("NEXT" + "\n");
//    }
//            System.out.println("END" + "\n");


}
