package bitmex.Bot.model.strategies.II;

import bitmex.Bot.view.WriterAndReadFile;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;


import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.File;


public class ReadAndSavePatterns {

    public static void createSavedPatterns() {
        SavedPatterns savedPatterns = Gasket.getSavedPatternsClass();
//        SaveRestoreHelper.restoreSavedPatternsClass();
        ConsoleHelper.writeMessage(DatesTimes.getDate() + " --- Востанавливаю SavedPatterns");

        File file = new File(Gasket.getFilesAndPathCreator().getPathPatterns());

        ArrayList<String> arrayListOut = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String string = reader.readLine();

                if (string.length() > 4 && !string.equals("START")) {
                    arrayListOut.add(string + "\n");
                } else if (string.equals("NEXT")) {
                    savedPatterns.setPatternsInListsPricePatterns(arrayListOut);
                    arrayListOut.clear();
                } else if (string.equalsIgnoreCase("END")) {
                    ConsoleHelper.writeMessage(DatesTimes.getDate()
                            + " --- SavedPatterns востановлен");
                    savedPatterns.seeLists();
                    arrayListOut.clear();
                    return;
                }
            }
            arrayListOut.clear();
        } catch (Exception e) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Ошибка в ЧТЕНИИ файла Patterns.txt");
        }
    }


    public static void saveSavedPatterns() {
//        SaveRestoreHelper.saveSavedPatternsClass();
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = "NEXT" + lineBreak;
        stringBuilder.append("START").append(lineBreak);

        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsClass().getListsPricePatterns();

//        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- Принял на запись лист размером --- "
//                + arrayLists.size());

//        System.out.println(arrayLists.size());

        for (ArrayList<String> arr : arrayLists) {

//            System.out.println(DatesTimes.getDateTerminal() + " --- В нем лежат листы размерами --- " + arr.size());

            for (String s : arr) {
                stringBuilder.append(s);
            }
            stringBuilder.append(next);
        }
        //stringBuilder.delete(stringBuilder.length() - next.length(), stringBuilder.length());
        stringBuilder.append("END").append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatterns(), false);
    }


    public static void saveTemporarySavedPatterns(ArrayList<ArrayList<String>> arrayListArrayList) {
//        SaveRestoreHelper.saveSavedPatternsClass();
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>(arrayListArrayList);
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = "NEXT" + lineBreak;
        stringBuilder.append("START").append(lineBreak);

//        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
//                + " --- Принял на запись временные лист размером --- "
//                + arrayLists.size());

//        System.out.println(arrayLists.size());

        for (ArrayList<String> arr : arrayLists) {
            for (String s : arr) {
                stringBuilder.append(s);
            }
            stringBuilder.append(next);
        }
        //stringBuilder.delete(stringBuilder.length() - next.length(), stringBuilder.length());
        stringBuilder.append("END").append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatternsTemporary(), false);
    }


    public static void saveSavedPatternsDelete(ArrayList<ArrayList<String>> arrayListArrayList) {
//        SaveRestoreHelper.saveSavedPatternsClass();
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>(arrayListArrayList);
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = "NEXT" + lineBreak;
        stringBuilder.append("START").append(lineBreak);

//        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
//                + " --- Принял на запись удаленные лист размером --- "
//                + arrayLists.size());

//        System.out.println(arrayLists.size());

        for (ArrayList<String> arr : arrayLists) {
            for (String s : arr) {
                stringBuilder.append(s);
            }
            stringBuilder.append(next);
        }
//        stringBuilder.delete(stringBuilder.length() - next.length(), stringBuilder.length());
//        stringBuilder.append("END").append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatternsDelete(), true);
    }
}
