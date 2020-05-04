package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.SaveRestoreHelper;
import bitmex.Bot.model.Gasket;

public class ReadAndSavePatterns {

    public static void createSavedPatterns() {
        SavedPatterns savedPatterns = SavedPatterns.getInstance();
        Gasket.setSavedPatternsClass(savedPatterns);
        SaveRestoreHelper.restoreSavedPatternsClass();
    }

    public static void saveSavedPatterns() {
        SaveRestoreHelper.saveSavedPatternsClass();
    }


}
