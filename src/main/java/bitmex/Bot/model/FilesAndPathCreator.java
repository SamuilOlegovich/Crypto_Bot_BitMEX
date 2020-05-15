package bitmex.Bot.model;

import bitmex.Bot.view.ConsoleHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;



public class FilesAndPathCreator {
    private String pathPatternsTemporary;
    private String pathPatternsDelete;
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
        pathPatternsTemporary = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/TemporaryPatterns.txt";

        pathPatternsDelete = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/TemporaryDelete.txt";

        pathSettings = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/Settings.txt";

        pathPatterns = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/Patterns.txt";

        pathLogs = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/"
                + DatesTimes.getDateLogs() + "===Log.txt";

        if (System.getProperty("os.name").startsWith("Windows")) {
            pathPatternsTemporary = pathPatternsTemporary
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsDelete = pathPatternsDelete
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathSettings = pathSettings
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatterns = pathPatterns
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathLogs = pathLogs
                    .replaceFirst("/", "").replaceAll("/", "\\\\");
        }

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsTemporary);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsDelete);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathSettings);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatterns);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathLogs);
    }

    private void isTheFileInPlace() {
        if (!Files.exists(Paths.get(pathPatternsTemporary))) {
            createdFileTemporaryPatterns();
        }

        if (!Files.exists(Paths.get(pathPatternsDelete))) {
            createdFileDeletePatterns();
        }

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
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл Настроек успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл Настроек.");
        }
    }

    private void createdFilePatterns() {
        File file = new File(pathPatterns);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для Паттернов успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл Петтернов.");
        }
    }

    private void createdFileDeletePatterns() {
        File file = new File(pathPatternsDelete);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для Удаленных Паттернов успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл Удаленных Петтернов.");
        }
    }

    private void createdFileTemporaryPatterns() {
        File file = new File(pathPatternsTemporary);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для Временных Паттернов успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл Временных Петтернов.");
        }
    }

    private void createdFileLog() {
        File file = new File(pathLogs);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый Лог файл успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать Лог файл.");
        }
    }


    public String getPathPatternsTemporary() {
        return pathPatternsTemporary;
    }

    public String getPathPatternsDelete() {
        return pathPatternsDelete;
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
