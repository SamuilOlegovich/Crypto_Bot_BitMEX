package bitmex.Bot.controller;

import bitmex.Bot.model.FilesAndPathCreator;
import bitmex.Bot.view.WriterAndReadFile;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;

import java.util.ArrayList;


public class ParserSetting {
    private ExecutorCommandos executorCommandos;
    private String path;


    public ParserSetting(ExecutorCommandos executorCommandos) {
        this.path = Gasket.getFilesAndPathCreator().getPathSettings();
        this.executorCommandos = executorCommandos;
        readFileSettings();
    }



    private void readFileSettings() {
        executorCommandos.setParserSetting(this);

        try {
            ArrayList<String> listSettings =  WriterAndReadFile.readFile(path);

            if (listSettings.size() < 1) {
                try {
                    ConsoleHelper.writeMessage("Настроек в файле Settings.txt необнаружено " +
                            "- включены и вписаны настройки по умолчанию.");
                    WriterAndReadFile.writerFile(getStringWrite(), path, false);
                } catch (Exception ex) {
                    ConsoleHelper.writeMessage("Ошибка в ЗАПИСИ файла Settings.txt .");
                }
            }


            for (String string : listSettings) {
                if (string.equalsIgnoreCase("END")) {
                    ConsoleHelper.writeMessage("Настройки УСПЕШНО считьаны.");
                    return;
                }

                String[] strings;

                if (string.length() > 4
                        && !string.equalsIgnoreCase(
                                " --- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ --- ")) {
                    strings = string.split(" ----- ");
                    executorCommandos.parseAndExecute(strings[0]);
                }
            }

        } catch (Exception e) {
            ConsoleHelper.writeMessage("Ошибка в ЧТЕНИИ файла Settings.txt");
        }
    }



    public void writeSettings() {
        try {
            WriterAndReadFile.writerFile(getStringWrite(), path, false);
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Настройки не перезаписались после команды изменения.");
        }
    }



    private String getStringWrite() {
        return ConsoleHelper.getStringInfoSettings() + "\n" + "END\n";
    }



    // TEST
    public static void main(String[] args) {
        FilesAndPathCreator filesAndPathCreator = new FilesAndPathCreator();
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExecutorCommandos executorCommandos = new ExecutorCommandos();
        ParserSetting parserSetting = new ParserSetting(executorCommandos);
        //readerAndParserSetting.readFileSettings();
        ControlConsoleSetting controlConsoleSetting = new ControlConsoleSetting(executorCommandos);
        executorCommandos.setParserSetting(parserSetting);
        controlConsoleSetting.start();
    }
}
