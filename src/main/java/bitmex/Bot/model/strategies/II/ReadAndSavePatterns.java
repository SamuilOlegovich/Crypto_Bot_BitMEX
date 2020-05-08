package bitmex.Bot.model.strategies.II;

import bitmex.Bot.view.WriterAndReadFile;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;


import java.util.ArrayList;


public class ReadAndSavePatterns {

    public static void createSavedPatterns() {
        SavedPatterns savedPatterns = SavedPatterns.getInstance();
        Gasket.setSavedPatternsClass(savedPatterns);
//        SaveRestoreHelper.restoreSavedPatternsClass();
        ConsoleHelper.writeMessage(DatesTimes.getDate() + " --- Востанавливаю SavedPatterns");


        try {
            ArrayList<String> listSettings =  WriterAndReadFile
                    .readFile(Gasket.getFilesAndPathCreator().getPathPatterns());

            if (listSettings.size() < 1) {
                    ConsoleHelper.writeMessage("Патернов в файле Patterns.txt необнаружено ");
                    return;
            }

            ArrayList<String> arrayListOut = new ArrayList<>();

            for (String string : listSettings) {
                if (string.equalsIgnoreCase("END")) {
                    ConsoleHelper.writeMessage(DatesTimes.getDate() + " --- SavedPatterns востановлен");
                    return;
                }

                if (string.length() > 4 && !string.equals("START")) {
                    arrayListOut.add(string);
                }else if (string.equals("NEXT")) {
                    savedPatterns.setPatternsInListsPricePatterns(arrayListOut);
                    arrayListOut.clear();
                } else if (string.equals("END")) {
                    return;
                }
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Ошибка в ЧТЕНИИ файла Patterns.txt");
        }
    }


    public static void saveSavedPatterns() {
//        SaveRestoreHelper.saveSavedPatternsClass();
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = "NEXT" + lineBreak;
        stringBuilder.append("START").append(lineBreak);

        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsClass().getListsPricePatterns();

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " -- Принял на запись лист размером --- "
                + arrayLists.size());

        System.out.println(arrayLists.size());

        for (ArrayList<String> arr : arrayLists) {

            System.out.println(DatesTimes.getDateTerminal() + " -- В нем лежат листы размерами --- " + arr.size());

            for (String s : arr) {
                stringBuilder.append(s);
            }
            stringBuilder.append(next);
        }
        stringBuilder.delete(stringBuilder.length() - next.length(), stringBuilder.length());
        stringBuilder.append("END").append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatterns(), false);
    }
}
