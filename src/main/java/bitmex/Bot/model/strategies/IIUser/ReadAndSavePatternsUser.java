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
        ConsoleHelper.writeMessage(DatesTimes.getDate() + " --- Востанавливаю Saved Patterns USER");

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
                            + " --- Saved Patterns USER востановлен");

                    if (Gasket.isShowLoadPatternsUser()) {
                        savedPatternsUser.seeListsUser();
                    }

                    arrayListOut.clear();
                    return;
                }
            }
            arrayListOut.clear();
        } catch (Exception e) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Ошибка в ЧТЕНИИ файла uPatterns.txt");
        }
    }


    public static synchronized void saveTemporarySavedPatternsUser(ArrayList<ArrayList<String>> arrayListArrayList) {
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>(arrayListArrayList);
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = "NEXT" + lineBreak;
        stringBuilder.append("START").append(lineBreak);

        for (ArrayList<String> arr : arrayLists) {
            for (String s : arr) {
                stringBuilder.append(s);
            }
            stringBuilder.append(next);
        }
        stringBuilder.append("END").append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatternsTemporaryUser(), false);
    }


    public static void saveSavedPatternsDeleteUser(ArrayList<ArrayList<String>> arrayListArrayList) {
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>(arrayListArrayList);

        if (arrayLists.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            String lineBreak = "\n";
            String next = "NEXT" + lineBreak;
            stringBuilder.append("START").append(lineBreak);

            for (ArrayList<String> arr : arrayLists) {
                for (String s : arr) {
                    stringBuilder.append(s);
                }
                stringBuilder.append(next);
            }

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
