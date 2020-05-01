package bitmex.Bot.model;

import bitmex.Bot.view.ConsoleHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;



public class FilesAndPathCreator {
    private String pathSettings;
    private String pathPatterns;
    private String pathLogs;


    public FilesAndPathCreator() {
        Gasket.setFilesAndPathCreator(this);
        createdPath();
        createdFileLog();
        isTheFileInPlace();
    }



    private void createdPath() {
        pathSettings = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/Settings.txt";

        pathPatterns = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/Patterns.txt";

        pathLogs = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/"
                + DatesTimes.getDate() + "=Log.txt";
    }

    private void isTheFileInPlace() {
        if (!Files.exists(Paths.get(pathSettings))) {
            createdFileSettings();
        }

        if (!Files.exists(Paths.get(pathPatterns))) {
            createdFilePatterns();
        }
    }

    private void createdFileSettings() {
        File file = new File(pathSettings);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage("Новый файл Настроек успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage("Не удалось создать файл Настроек.");
        }
    }

    private void createdFilePatterns() {
        File file = new File(pathPatterns);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage("Новый файл для Паттернов успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage("Не удалось создать файл Петтернов.");
        }
    }

    private void createdFileLog() {
        File file = new File(pathLogs);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage("Новый Лог файл успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage("Не удалось создать Лог файл.");
        }
    }


    public String getPathSettings() {
        return pathSettings;
    }

    public String getPathPatterns() {
        return pathPatterns;
    }

    public String getPathLogs() {
        return pathLogs;
    }
}
