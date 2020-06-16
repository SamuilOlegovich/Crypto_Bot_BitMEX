package bitmex.Bot.model;

import bitmex.Bot.view.ConsoleHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;



public class FilesAndPathCreator {
    private String pathPatternsTemporaryUser;
    private String pathPatternsDeleteUser;
    private String pathPatternsTemporary;
    private String pathPatternsForUser;
    private String pathPatternsDelete;
    private String pathPatternsUser;
    private String pathSettings;
    private String pathPatterns;
    private String pathLogs;


    public FilesAndPathCreator() {
        Gasket.setFilesAndPathCreator(this);
        createdPath();
        createdFileLog();
        isTheFileInPlace();
        showPath();
    }



    private void createdPath() {
        String[] strings = getClass().getResource("").getPath().split("bitmex-client.jar");
        String finish = strings[0].replaceAll("file:", "");

        if (strings.length == 2) {
            String pathLogsTest = strings[0] + "Logs";
            Path path = Paths.get(pathLogsTest);

            if (!Files.exists(path)) {
                // действия, если папка существует
                try {
                    Files.createDirectories(Paths.get("iiPatterns"));
                    Files.createDirectories(Paths.get("uPatterns"));
                    Files.createDirectories(Paths.get("Settings"));
                    Files.createDirectories(Paths.get("Logs"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            pathPatternsTemporaryUser = finish + "uPatterns/uTemporaryPatterns.txt";
            pathLogs = finish  + "Logs/" + DatesTimes.getDateLogs() + "===Log.txt";
            pathPatternsTemporary = finish + "iiPatterns/iiTemporaryPatterns.txt";
            pathPatternsDeleteUser = finish + "uPatterns/uTemporaryDelete.txt";
            pathPatternsDelete = finish + "iiPatterns/iiTemporaryDelete.txt";
            pathPatternsForUser = finish + "uPatterns/uPatternsFor.txt";
            pathPatternsUser = finish + "uPatterns/uPatterns.txt";
            pathPatterns = finish + "iiPatterns/iiPatterns.txt";
            pathSettings = finish + "Settings/Settings.txt";

        } else {
            String string = getClass().getResource("").getPath()
                    .replaceAll("target/classes", "src/main/java")
                    .replaceAll("model/", "");

            pathPatternsTemporaryUser = string + "Logs/PatternsUser/TemporaryPatternsUser.txt";
            pathPatternsDeleteUser = string + "Logs/PatternsUser/TemporaryDeleteUser.txt";
            pathLogs = string + "Logs/Log/" + DatesTimes.getDateLogs() + "===Log.txt";
            pathPatternsForUser = string + "Logs/PatternsUser/PatternsForUser.txt";
            pathPatternsTemporary = string + "Logs/Patterns/TemporaryPatterns.txt";
            pathPatternsDelete = string + "Logs/Patterns/TemporaryDelete.txt";
            pathPatternsUser = string + "Logs/PatternsUser/PatternsUser.txt";
            pathPatterns = string + "Logs/Patterns/Patterns.txt";
            pathSettings = string + "Logs/Settings.txt";
        }

        if (System.getProperty("os.name").startsWith("Windows")) {
            pathPatternsTemporaryUser = pathPatternsTemporaryUser
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsDeleteUser = pathPatternsDeleteUser
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsTemporary = pathPatternsTemporary
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsForUser = pathPatternsForUser
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsDelete = pathPatternsDelete
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsUser = pathPatternsUser
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathSettings = pathSettings
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatterns = pathPatterns
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathLogs = pathLogs
                    .replaceFirst("/", "").replaceAll("/", "\\\\");
        }
    }



    private void showPath() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsTemporaryUser);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsDeleteUser);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsTemporary);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsForUser);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsDelete);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsUser);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathSettings);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatterns);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathLogs);
    }



    private void isTheFileInPlace() {
        if (!Files.exists(Paths.get(pathPatternsTemporaryUser))) {
            createdFileTemporaryPatternsUser();
        }

        if (!Files.exists(Paths.get(pathPatternsTemporary))) {
            createdFileTemporaryPatterns();
        }

        if (!Files.exists(Paths.get(pathPatternsDeleteUser))) {
            createdFileDeletePatternsUser();
        }

        if (!Files.exists(Paths.get(pathPatternsForUser))) {
            createdFilePatternsForUser();
        }

        if (!Files.exists(Paths.get(pathPatternsDelete))) {
            createdFileDeletePatterns();
        }

        if (!Files.exists(Paths.get(pathPatternsUser))) {
            createdFilePatternsUser();
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



    private void createdFilePatternsUser() {
        File file = new File(pathPatternsUser);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для User Паттернов успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл User Петтернов.");
        }
    }



    private void createdFilePatternsForUser() {
        File file = new File(pathPatternsForUser);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для FOR User Паттернов успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл FOR User Петтернов.");
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



    private void createdFileDeletePatternsUser() {
        File file = new File(pathPatternsDeleteUser);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для Удаленных User Паттернов успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл User Удаленных Петтернов.");
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



    private void createdFileTemporaryPatternsUser() {
        File file = new File(pathPatternsTemporaryUser);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для User Временных Паттернов успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл User Временных Петтернов.");
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



    public String getPathPatternsTemporaryUser() {
        return pathPatternsTemporaryUser;
    }

    public String getPathPatternsTemporary() {
        return pathPatternsTemporary;
    }

    public String getPathPatternsForUser() {
        return pathPatternsForUser;
    }

    public String getPathPatternsDeleteUser() {
        return pathPatternsDeleteUser;
    }

    public String getPathPatternsDelete() {
        return pathPatternsDelete;
    }

    public String getPathSettings() {
        return pathSettings;
    }

    public String getPathPatternsUser() {
        return pathPatternsUser;
    }

    public String getPathPatterns() {
        return pathPatterns;
    }

    public String getPathLogs() {
        return pathLogs;
    }
}
