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
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = "NEXT" + lineBreak;
        stringBuilder.append("START").append(lineBreak);
        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsClass().getListsPricePatterns();


        for (ArrayList<String> arr : arrayLists) {

            for (String s : arr) {
                stringBuilder.append(s);
            }
            stringBuilder.append(next);
        }
        stringBuilder.append("END").append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatterns(), false);
    }



    // сделать тут преобразование стров в строки для юзера
    public static void saveSavedPatternsFromUser() {
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = "NEXT" + lineBreak;
        stringBuilder.append("START").append(lineBreak);
        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsClass().getListsPricePatterns();


        for (ArrayList<String> arr : arrayLists) {

            for (String s : arr) {
                if (!s.startsWith("BIAS") && !s.startsWith("BUY")) {
                    stringBuilder.append(convertStringForUser(s));
                } else {
                    stringBuilder.append(s);
                }
            }
            stringBuilder.append(next);
        }
        stringBuilder.append("END").append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatternsForUser(), false);
    }



    public static void saveTemporarySavedPatterns(ArrayList<ArrayList<String>> arrayListArrayList) {
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
        //stringBuilder.delete(stringBuilder.length() - next.length(), stringBuilder.length());
        stringBuilder.append("END").append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatternsTemporary(), false);
    }



    public static void saveSavedPatternsDelete(ArrayList<ArrayList<String>> arrayListArrayList) {
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
                    Gasket.getFilesAndPathCreator().getPathPatternsDelete(), true);
        }
    }



    /////////// PARSER ///////////



    private static String convertStringForUser(String string) {
        String in = string.replaceAll("\\{", "");
        in = in.replaceAll("}", "");
        in = in.replaceAll("\"", " ");
        String[] strings = in.split(" , ");
        String period = strings[0].trim().replaceAll("period : {2}","");
        String preview = strings[1].trim().replaceAll("preview : {2}", "");
        String time = strings[2].trim().replaceAll("time : {2}", "");
        String price = strings[3].trim().replaceAll("price : {2}", "");
        String value = strings[4].trim().replaceAll("value : {2}", "");
        String type = strings[5].trim().replaceAll("type : {2}", "");
        String avg = strings[6].trim().replaceAll("avg : {2}", "");
        String dir = strings[7].trim().replaceAll("dir : {2}", "");
        String open = strings[8].trim().replaceAll("open : {2}", "");
        String close = strings[9].trim().replaceAll("close : {2}", "");
        String high = strings[10].trim().replaceAll("high : {2}", "");
        String low = strings[11].trim().replaceAll("low : {2}", "");


        return "period===" + period + "===preview===" + preview + "===time===" + time + "===price===" + price
                + "===value===" + value + "===type===" + type + "===avg===" + avg + "===dir===" + dir
                + "===open===" + open + "===close===" + close + "===high===" + high
                + "===low===" + low
                + "\n";
    }
}
