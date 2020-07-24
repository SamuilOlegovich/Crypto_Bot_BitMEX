package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.view.WriterAndReadFile;
import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.File;

import static bitmex.Bot.model.enums.TypeData.*;




public class ReadAndSavePatternsUser {

    public static void readFileStrategiesSettingsMartingale() {
        String path = Gasket.getFilesAndPathCreator().getPathStrategiesSettingsMartingale();
        Martingale martingale = Gasket.getMartingaleClass();

        try {
            ArrayList<String> listSettings =  WriterAndReadFile.readFile(path);
            // ID===4S===LOT===1*2*4*8*16===EXAMPLE

            if (listSettings.size() < 1) {
                try {
                    ConsoleHelper.writeMessage("Настроек в файле StrategiesSettingsMartingale.txt необнаружено "
                            + "- записан шаблон (ПРИМЕР) настроек. Согласно примеру введите свои настройки "
                            + "и перезапустите программу"
                    );
                    WriterAndReadFile.writerFile(getStringWrite(), path, false);
                } catch (Exception ex) {
                    ConsoleHelper.writeMessage("Ошибка в ЗАПИСИ файла StrategiesSettingsMartingale.txt .");
                }
            }


            for (String string : listSettings) {
                if (string.equalsIgnoreCase("END")) {
                    ConsoleHelper.writeMessage("Настройки стартегии Мартингейла УСПЕШНО считаны.");
                    return;
                }

                if (string.length() > 4 && !string.equalsIgnoreCase(EXAMPLE.toString())
                        && string.endsWith(EXAMPLE.toString())) {
                    martingale.setVolumeForEachStep(string.replaceAll(",", "."));
                }
            }

        } catch (Exception e) {
            ConsoleHelper.writeMessage("Ошибка в ЧТЕНИИ файла StrategiesSettingsMartingale.txt");
        }
    }



    private static String getStringWrite() {
        return TypeData.EXAMPLE.toString() + "\n" + "ID===4S===LOT===1*2*4*8*16===EXAMPLE\n"
                + "ID===7S===LOT===1.0*3.0*9.0*27.0===EXAMPLE\n" + "END\n";
    }



    public static synchronized void createSavedPatternsUser() {
        SavedPatternsUser savedPatternsUser = Gasket.getSavedPatternsUserClass();
        ConsoleHelper.writeMessage(DatesTimes.getDate() + " --- Востанавливаю Saved Patterns USER");

        File file = new File(Gasket.getFilesAndPathCreator().getPathPatternsUser());

        ArrayList<String> arrayListOut = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String string = reader.readLine();

                if (string.length() > 4 && !string.equals(START.toString())) {
                    arrayListOut.add(string + "\n");
                } else if (string.equals(NEXT.toString())) {
                    savedPatternsUser.setPatternsInListsPricePatternsUser(arrayListOut);
                    arrayListOut.clear();
                } else if (string.equalsIgnoreCase(END.toString())) {
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
                Gasket.getFilesAndPathCreator().getPathPatternsTemporaryUser(), false);
    }



    public static synchronized void saveSavedPatternsDeleteUser(ArrayList<ArrayList<String>> arrayListArrayList) {
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
                    Gasket.getFilesAndPathCreator().getPathPatternsDeleteUser(), true);
        }
    }



    public static synchronized void saveSavedPatternsUser() {
        StringBuilder stringBuilder = new StringBuilder();
        String lineBreak = "\n";
        String next = NEXT.toString() + lineBreak;
        stringBuilder.append(START.toString()).append(lineBreak);

        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsUserClass().getListsPricePatternsUser();

        for (ArrayList<String> arr : arrayLists) {

            for (String s : arr) {
                stringBuilder.append(s);
            }
            stringBuilder.append(next);
        }
        stringBuilder.append(END.toString()).append(lineBreak);

        WriterAndReadFile.writerFile(stringBuilder.toString(),
                Gasket.getFilesAndPathCreator().getPathPatternsUser(), false);
    }
}
