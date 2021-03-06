package bitmex.Bot.model.strategies.iiPro;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.WriterAndReadFile;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.File;

import static bitmex.Bot.model.enums.TypeData.*;




public class ReadAndSavePatternsPro {

    public static void createSavedPatternsPro() {
        SavedPatternsPro savedPatternsPro = Gasket.getSavedPatternsProClass();
        ConsoleHelper.writeMessage(DatesTimes.getDate() + " --- Востанавливаю Saved Patterns iiPRO");
        File file = new File(Gasket.getFilesAndPathCreator().getPathPatternsIIPro());
        ArrayList<String> arrayListOut = new ArrayList<>();


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String string = reader.readLine();

                if (string.length() > 4 && !string.equals(START.toString())) {
                    arrayListOut.add(string + "\n");
                } else if (string.equals(NEXT.toString())) {
                    savedPatternsPro.setPatternsInListsPricePatterns(arrayListOut);
                    arrayListOut.clear();
                } else if (string.equalsIgnoreCase(END.toString())) {
                    ConsoleHelper.writeMessage(DatesTimes.getDate()
                            + " --- Saved Patterns iiPRO востановлен");

                    if (Gasket.isShowLoadPatternsIIPro()) {
                        savedPatternsPro.seeLists();
                    }

                    arrayListOut.clear();
                    return;
                }
            }
            arrayListOut.clear();
        } catch (Exception e) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Ошибка в ЧТЕНИИ файла iiProPatterns.txt");
        }
    }



    public synchronized static void saveSavedPatterns() {
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
                Gasket.getFilesAndPathCreator().getPathPatternsIIPro(), false);
    }



    // сделать тут преобразование строк в строки для юзера
    public static void saveSavedPatternsFromUser() {
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = NEXT.toString() + lineBreak;
        stringBuilder.append(START.toString()).append(lineBreak);
        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsProClass().getListsPricePatterns();


        for (ArrayList<String> arr : arrayLists) {

            for (String s : arr) {
                if (!s.startsWith(BIAS.toString()) && !s.startsWith(BUY.toString())) {
                    if (Gasket.isReplaceDataWithNULLPro()) {
                        stringBuilder.append(StringHelper.convertStringForUserInsertNulls(s));
                        stringBuilder.append("\n");
                    } else {
                        stringBuilder.append(StringHelper.convertStringForUser(s));
                    }
                } else if (s.startsWith(BIAS.toString())) {
                    stringBuilder.append(s);
                } else if (s.startsWith(BUY.toString())) {
                    stringBuilder.append(StringHelper.insertTheMissingDataInTheZeroLine(s));
                }
            }
            stringBuilder.append(next);
        }
        stringBuilder.append(END.toString()).append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatternsForUserIIPro(), false);
    }



    // сделать тут преобразование строк в строки для юзера с учетом укорочения
    public static void saveSavedTrimmedPatternsFromUser() {
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = NEXT.toString() + lineBreak;
        stringBuilder.append(START.toString()).append(lineBreak);
        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsProClass().getListsPricePatterns();


        for (ArrayList<String> arr : arrayLists) {

            for (String s : arr) {
                if (!s.startsWith(BIAS.toString()) && !s.startsWith(BUY.toString())) {
                    if (eqalseTrimmedLevels(s)) {
                        String one = arr.get(arr.indexOf(s) > 0 ? arr.indexOf(s) - 1 : 0);
                        String two = arr.get(arr.indexOf(s) < arr.size() - 1 ? arr.indexOf(s) + 1 : arr.indexOf(s));
                        String original = ifPricesAreEqualPutNull(one, s, two);

                        if (Gasket.isReplaceDataWithNULLPro()) {
                            stringBuilder.append(StringHelper.convertStringForUserInsertNulls(original));
                            stringBuilder.append("\n");
                        } else {
                            stringBuilder.append(StringHelper.convertStringForUser(s));
                        }
                    }
                } else if (s.startsWith(BIAS.toString())) {
                    stringBuilder.append(s);
                } else if (s.startsWith(BUY.toString())) {
                    stringBuilder.append(StringHelper.insertTheMissingDataInTheZeroLine(s));
                }
            }
            stringBuilder.append(next);
        }
        stringBuilder.append(END.toString()).append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathLevelsForTrimmedPatternsIIPro(), false);
    }

    private static boolean eqalseTrimmedLevels(String in) {
        String[] strings = Gasket.getLevelsForTrimmedPatterns().split("-");
        for (String s : strings) {
            if (s.equals(StringHelper.giveData(type, in))) {
                return true;
            }
        }
        return false;
    }

    // смотрим, ровны ли цены на ближайших строках, если ровны, то меняем ее на null
    private static String ifPricesAreEqualPutNull(String one, String original, String two) {
        if (one != null && !one.startsWith(TypeData.BIAS.toString()) && !one.startsWith(TypeData.BUY.toString())) {
            if (StringHelper.giveData(price, original).equals(StringHelper.giveData(price, one))) {
                return StringHelper.setData(price, NULL.toString(), original);
            }
        }
        if (two != null && !two.startsWith(TypeData.BIAS.toString()) && !two.startsWith(TypeData.BUY.toString())) {
            if (StringHelper.giveData(price, original).equals(StringHelper.giveData(price, two))) {
                return StringHelper.setData(price, NULL.toString(), original);
            }
        }
        return original;
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
                Gasket.getFilesAndPathCreator().getPathPatternsTemporaryIIPro(), false);
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
                    Gasket.getFilesAndPathCreator().getPathPatternsDeleteIIPro(), true);
        }
    }
}

