package bitmex.Bot.controller;


import bitmex.Bot.model.strategies.IIUser.ReadAndSavePatternsUser;
import bitmex.Bot.model.strategies.IIUser.SavedPatternsUser;
import bitmex.Bot.model.strategies.II.ReadAndSavePatterns;
import bitmex.Bot.model.strategies.II.SavedPatterns;
import bitmex.Bot.model.FilesAndPathCreator;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.View;




public class Main {

    public static void main(String[] args) {
        View view = new View();
        Gasket.setViewThread(view);
        view.start();
        FilesAndPathCreator filesAndPathCreator = new FilesAndPathCreator();

        SavedPatternsUser savedPatternsUser = SavedPatternsUser.getInstance();
        SavedPatterns savedPatterns = SavedPatterns.getInstance();

        Gasket.setSavedPatternsUserClass(savedPatternsUser);
        ReadAndSavePatternsUser.createSavedPatternsUser();

        Gasket.setSavedPatternsClass(savedPatterns);
        ReadAndSavePatterns.createSavedPatterns();



        ExecutorCommandos executorCommandos = new ExecutorCommandos();
        ParserSetting parserSetting = new ParserSetting(executorCommandos);
    }
}
