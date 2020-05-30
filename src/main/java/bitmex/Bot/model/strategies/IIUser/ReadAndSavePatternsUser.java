package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.view.WriterAndReadFile;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.File;



public class ReadAndSavePatternsUser {

    public static void createSavedPatternsUser() {
        SavedPatternsUser savedPatternsUser = Gasket.getSavedPatternsUserClass();
        ConsoleHelper.writeMessage(DatesTimes.getDate() + " --- Востанавливаю SavedPatternsUser");

        File file = new File(Gasket.getFilesAndPathCreator().getPathPatternsUser());

        ArrayList<String> arrayListOut = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String string = reader.readLine();

                if (string.length() > 4 && !string.equals("START")) {
                    arrayListOut.add(string + "\n");
                } else if (string.equals("NEXT")) {
                    savedPatternsUser.setPatternsInListsPricePatternsUser(arrayListOut);
                    arrayListOut.clear();
                } else if (string.equalsIgnoreCase("END")) {
                    ConsoleHelper.writeMessage(DatesTimes.getDate()
                            + " --- SavedPatternsUser востановлен");
                    savedPatternsUser.seeListsUser();
                    arrayListOut.clear();
                    return;
                }
            }
            arrayListOut.clear();
        } catch (Exception e) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Ошибка в ЧТЕНИИ файла PatternsUser.txt");
        }
    }


    public static void saveTemporarySavedPatternsUser(ArrayList<ArrayList<String>> arrayListArrayList) {
//        SaveRestoreHelper.saveSavedPatternsClass();
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>(arrayListArrayList);
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = "NEXT" + lineBreak;
        stringBuilder.append("START").append(lineBreak);

//        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
//                + " --- Принял на запись временные лист User размером --- "
//                + arrayLists.size());
//
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
                Gasket.getFilesAndPathCreator().getPathPatternsTemporaryUser(), false);
    }


    public static void saveSavedPatternsDeleteUser(ArrayList<ArrayList<String>> arrayListArrayList) {
//        SaveRestoreHelper.saveSavedPatternsClass();
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>(arrayListArrayList);

        if (arrayLists.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            String lineBreak = "\n";
            String next = "NEXT" + lineBreak;
            stringBuilder.append("START").append(lineBreak);

//        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
//                + " --- Принял на запись удаленные User лист размером --- "
//                + arrayLists.size());
//
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
                    Gasket.getFilesAndPathCreator().getPathPatternsDeleteUser(), true);
        }
    }

    public static void saveSavedPatternsUser() {
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = "NEXT" + lineBreak;
        stringBuilder.append("START").append(lineBreak);

        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsUserClass().getListsPricePatternsUser();

        for (ArrayList<String> arr : arrayLists) {

            for (String s : arr) {
                stringBuilder.append(s);
            }
            stringBuilder.append(next);
        }
        stringBuilder.append("END").append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatternsUser(), false);
    }
}
