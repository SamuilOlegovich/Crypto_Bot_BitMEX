package bitmex.Bot.model.strategies.II;

import bitmex.Bot.view.WriterAndReadFile;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;


import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.File;

import static bitmex.Bot.model.enums.TypeData.*;




public class ReadAndSavePatterns {

    public static void createSavedPatterns() {
        SavedPatterns savedPatterns = Gasket.getSavedPatternsClass();
        ConsoleHelper.writeMessage(DatesTimes.getDate() + " --- Востанавливаю Saved Patterns II");
        File file = new File(Gasket.getFilesAndPathCreator().getPathPatterns());
        ArrayList<String> arrayListOut = new ArrayList<>();


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String string = reader.readLine();

                if (string.length() > 4 && !string.equals(START.toString())) {
                    arrayListOut.add(string + "\n");
                } else if (string.equals("NEXT")) {
                    savedPatterns.setPatternsInListsPricePatterns(arrayListOut);
                    arrayListOut.clear();
                } else if (string.equalsIgnoreCase(END.toString())) {
                    ConsoleHelper.writeMessage(DatesTimes.getDate()
                            + " --- Saved Patterns II востановлен");

                    if (Gasket.isShowLoadPatternsII()) {
                        savedPatterns.seeLists();
                    }

                    arrayListOut.clear();
                    return;
                }
            }
            arrayListOut.clear();
        } catch (Exception e) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Ошибка в ЧТЕНИИ файла iiPatterns.txt");
        }
    }



    public static void saveSavedPatterns() {
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = NEXT.toString() + lineBreak;
        stringBuilder.append(START.toString()).append(lineBreak);
        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsClass().getListsPricePatterns();


        for (ArrayList<String> arr : arrayLists) {

            for (String s : arr) {
                stringBuilder.append(s);
            }
            stringBuilder.append(next);
        }
        stringBuilder.append(END.toString()).append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatterns(), false);
    }



    // сделать тут преобразование стров в строки для юзера
    public static void saveSavedPatternsFromUser() {
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = NEXT.toString() + lineBreak;
        stringBuilder.append(START.toString()).append(lineBreak);
        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsClass().getListsPricePatterns();


        for (ArrayList<String> arr : arrayLists) {

            for (String s : arr) {
                if (!s.startsWith(BIAS.toString()) && !s.startsWith(BUY.toString())) {
                    stringBuilder.append(StringHelper.convertStringForUser(s));
                } else {
                    stringBuilder.append(s);
                }
            }
            stringBuilder.append(next);
        }
        stringBuilder.append(END.toString()).append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatternsForUser(), false);
    }



    public static void saveTemporarySavedPatterns(ArrayList<ArrayList<String>> arrayListArrayList) {
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>(arrayListArrayList);
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = NEXT.toString() + lineBreak;
        stringBuilder.append(START.toString()).append(lineBreak);


        for (ArrayList<String> arr : arrayLists) {
            for (String s : arr) {
                stringBuilder.append(s);
            }
            stringBuilder.append(next);
        }
        stringBuilder.append(END.toString()).append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatternsTemporary(), false);
    }



    public static void saveSavedPatternsDelete(ArrayList<ArrayList<String>> arrayListArrayList) {
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>(arrayListArrayList);

        if (arrayLists.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            String lineBreak = "\n";
            String next = NEXT.toString() + lineBreak;
            stringBuilder.append(START.toString()).append(lineBreak);

            for (ArrayList<String> arr : arrayLists) {
                for (String s : arr) {
                    stringBuilder.append(s);
                }
                stringBuilder.append(next);
            }

            WriterAndReadFile.writerFile(stringBuilder.toString(),
                    Gasket.getFilesAndPathCreator().getPathPatternsDelete(), true);
        }
    }



    public static void savePureHistoryOfPatternsIn(ArrayList<String> arrayListArrayList) {
        ArrayList<String> arrayLists = new ArrayList<>(arrayListArrayList);

        if (arrayLists.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            String lineBreak = "\n";
            String next = NEXT.toString() + lineBreak;
            stringBuilder.append(START.toString()).append(lineBreak);

            for (String string : arrayLists) {
                stringBuilder.append(string);
            }
            stringBuilder.append(next);

            WriterAndReadFile.writerFile(stringBuilder.toString(),
                    Gasket.getFilesAndPathCreator().getPathPureHistoryOfPatternsIn(), true);
        }
    }
}
