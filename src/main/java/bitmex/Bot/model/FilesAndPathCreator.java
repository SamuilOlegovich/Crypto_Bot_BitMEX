package bitmex.Bot.model;

import bitmex.Bot.view.ConsoleHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;



public class FilesAndPathCreator {

    private String pathPatternsTemporaryIIPro;
    private String pathPatternsTemporaryUser;
    private String pathPatternsForUserIIPro;
    private String pathPatternsDeleteIIPro;
    private String pathPatternsDeleteUser;
    private String pathPatternsTemporary;
    private String pathPatternsForUser;
    private String pathPatternsDelete;
    private String pathPatternsIIPro;
    private String pathPatternsUser;
    private String pathSettings;
    private String pathPatterns;
    private String pathLogs;


    public FilesAndPathCreator() {
        Gasket.setFilesAndPathCreator(this);

        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        createdPath();
        createdFileLog();
        isTheFileInPlace();
        showPath();
    }



    private void createdPath() {

        String[] strings = getClass().getResource("").getPath().split("bitmex-client.jar");
        String finish = strings[0].replaceAll("file:", "");

        if (System.getProperty("os.name").startsWith("Windows")) {
            finish = finish.replaceFirst("/", "").replaceAll("/", "\\\\");
        }


        if (strings.length == 2) {

            if (System.getProperty("os.name").startsWith("Windows")) {

                Path pathIIProPatterns = Paths.get(finish + "iiProPatterns");

                if (!Files.exists(pathIIProPatterns)) {
                    try {
                        Files.createDirectories(Paths.get("iiProPatterns"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Path pathIIPatterns = Paths.get(finish + "iiPatterns");

                if (!Files.exists(pathIIPatterns)) {
                    try {
                        Files.createDirectories(Paths.get("iiPatterns"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Path pathUPatterns = Paths.get(finish + "uPatterns");

                if (!Files.exists(pathUPatterns)) {
                    try {
                        Files.createDirectories(Paths.get("uPatterns"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Path pathSetting = Paths.get(finish + "Settings");

                if (!Files.exists(pathSetting)) {
                    // действия, если папка существует
                    try {
                        Files.createDirectories(Paths.get("Settings"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Path pathLog = Paths.get(finish + "Logs");

                if (!Files.exists(pathLog)) {
                    // действия, если папка существует
                    try {
                        Files.createDirectories(Paths.get("Logs"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                pathLogs = finish  + "Logs\\" + DatesTimes.getDateLogs().replaceAll(":", "-")
                        + " Log.txt";
                pathPatternsTemporaryIIPro = finish + "iiProPatterns\\iiProTemporaryPatterns.txt";
                pathPatternsDeleteIIPro = finish + "iiProPatterns\\iiProTemporaryDelete.txt";
                pathPatternsForUserIIPro = finish + "uPatterns\\iiProPatternsForUser.txt";
                pathPatternsTemporaryUser = finish + "uPatterns\\uTemporaryPatterns.txt";
                pathPatternsTemporary = finish + "iiPatterns\\iiTemporaryPatterns.txt";
                pathPatternsDeleteUser = finish + "uPatterns\\uTemporaryDelete.txt";
                pathPatternsDelete = finish + "iiPatterns\\iiTemporaryDelete.txt";
                pathPatternsForUser = finish + "uPatterns\\iiPatternsForUser.txt";
                pathPatternsIIPro = finish + "iiProPatterns\\iiProPatterns.txt";
                pathPatternsUser = finish + "iiProPatterns\\iiProPatterns.txt";
                pathPatternsUser = finish + "uPatterns\\uPatterns.txt";
                pathPatterns = finish + "iiPatterns\\iiPatterns.txt";
                pathSettings = finish + "Settings\\Settings.txt";

            } else {
                Path pathIIProPatterns = Paths.get(strings[0] + "iiProPatterns");

                if (!Files.exists(pathIIProPatterns)) {
                    try {
                        Files.createDirectories(Paths.get("iiProPatterns"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Path pathIIPatterns = Paths.get(strings[0] + "iiPatterns");

                if (!Files.exists(pathIIPatterns)) {
                    try {
                        Files.createDirectories(Paths.get("iiPatterns"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Path pathUPatterns = Paths.get(strings[0] + "uPatterns");

                if (!Files.exists(pathUPatterns)) {
                    try {
                        Files.createDirectories(Paths.get("uPatterns"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Path pathSetting = Paths.get(strings[0] + "Settings");

                if (!Files.exists(pathSetting)) {
                    // действия, если папка существует
                    try {
                        Files.createDirectories(Paths.get("Settings"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Path pathLog = Paths.get(strings[0] + "Logs");

                if (!Files.exists(pathLog)) {
                    // действия, если папка существует
                    try {
                        Files.createDirectories(Paths.get("Logs"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                pathPatternsTemporaryIIPro = finish + "iiProPatterns/iiProTemporaryPatterns.txt";
                pathPatternsDeleteIIPro = finish + "iiProPatterns/iiProTemporaryDelete.txt";
                pathPatternsTemporaryUser = finish + "uPatterns/uTemporaryPatterns.txt";
                pathPatternsTemporary = finish + "iiPatterns/iiTemporaryPatterns.txt";
                pathPatternsForUserIIPro = finish + "uPatterns/iiProPatternsFor.txt";
                pathLogs = finish + "Logs/" + DatesTimes.getDateLogs() + " Log.txt";
                pathPatternsDeleteUser = finish + "uPatterns/uTemporaryDelete.txt";
                pathPatternsDelete = finish + "iiPatterns/iiTemporaryDelete.txt";
                pathPatternsIIPro = finish + "iiProPatterns/iiProPatterns.txt";
                pathPatternsForUser = finish + "uPatterns/iiPatternsFor.txt";
                pathPatternsUser = finish + "uPatterns/uPatterns.txt";
                pathPatterns = finish + "iiPatterns/iiPatterns.txt";
                pathSettings = finish + "Settings/Settings.txt";
            }
        } else {
            String string = getClass().getResource("").getPath()
                    .replaceAll("target/classes", "src/main/java")
                    .replaceAll("model/", "");

            pathPatternsTemporaryIIPro = string + "Logs/PatternsUser/iiProTemporaryPatterns.txt";
            pathPatternsForUserIIPro = string + "Logs/PatternsUser/iiProPatternsForUser.txt";
            pathPatternsTemporaryUser = string + "Logs/PatternsUser/uTemporaryPatterns.txt";
            pathPatternsDeleteIIPro = string + "Logs/PatternsUser/iiProTemporaryDelete.txt";
            pathPatternsDeleteUser = string + "Logs/PatternsUser/uTemporaryDelete.txt";
            pathLogs = string + "Logs/Log/" + DatesTimes.getDateLogs() + "===Log.txt";
            pathPatternsForUser = string + "Logs/PatternsUser/iiPatternsForUser.txt";
            pathPatternsTemporary = string + "Logs/Patterns/TemporaryPatterns.txt";
            pathPatternsIIPro = string + "Logs/PatternsUser/iiProPatterns.txt";
            pathPatternsDelete = string + "Logs/Patterns/TemporaryDelete.txt";
            pathPatternsUser = string + "Logs/PatternsUser/uPatterns.txt";
            pathPatterns = string + "Logs/Patterns/Patterns.txt";
            pathSettings = string + "Logs/Settings.txt";
        }

        if (System.getProperty("os.name").startsWith("Windows")) {
            pathPatternsTemporaryIIPro = pathPatternsTemporaryIIPro
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsTemporaryUser = pathPatternsTemporaryUser
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsForUserIIPro = pathPatternsForUserIIPro
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsDeleteIIPro = pathPatternsDeleteIIPro
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsDeleteUser = pathPatternsDeleteUser
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsTemporary = pathPatternsTemporary
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsForUser = pathPatternsForUser
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsDelete = pathPatternsDelete
                    .replaceFirst("/", "").replaceAll("/", "\\\\");

            pathPatternsIIPro = pathPatternsIIPro
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
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsTemporaryIIPro);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsTemporaryUser);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsForUserIIPro);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsDeleteIIPro);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsDeleteUser);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsTemporary);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsForUser);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsDelete);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsIIPro);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatternsUser);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathSettings);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathPatterns);
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- " + pathLogs);
    }



    private void isTheFileInPlace() {
        if (!Files.exists(Paths.get(pathPatternsTemporaryUser))) {
            createdFileTemporaryPatternsUser();
        }

        if (!Files.exists(Paths.get(pathPatternsTemporaryIIPro))) {
            createdFileTemporaryPatternsIIPro();
        }

        if (!Files.exists(Paths.get(pathPatternsForUserIIPro))) {
            createdFilePatternsForUserIIPro();
        }

        if (!Files.exists(Paths.get(pathPatternsTemporary))) {
            createdFileTemporaryPatterns();
        }

        if (!Files.exists(Paths.get(pathPatternsDeleteUser))) {
            createdFileDeletePatternsUser();
        }

        if (!Files.exists(Paths.get(pathPatternsDeleteIIPro))) {
            createdFileDeletePatternsIIPro();
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

        if (!Files.exists(Paths.get(pathPatternsIIPro))) {
            createdFilePatternsIIPro();
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



    private void createdFilePatternsForUserIIPro() {
        File file = new File(pathPatternsForUserIIPro);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для FOR User Паттернов II Pro успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл FOR User II Pro Петтернов.");
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



    private void createdFilePatternsIIPro() {
        File file = new File(pathPatternsIIPro);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для Паттернов II Pro успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл Петтернов II Pro.");
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



    private void createdFileDeletePatternsIIPro() {
        File file = new File(pathPatternsDeleteIIPro);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для Удаленных Паттернов II Pro успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл Удаленных Петтернов II Pro.");
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



    private void createdFileTemporaryPatternsIIPro() {
        File file = new File(pathPatternsTemporaryIIPro);
        try {
            boolean newFile = file.createNewFile();
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для Временных Паттернов II Pro успешно создан.");
        } catch (IOException ex) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл Временных Петтернов II Pro.");
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

    public String getPathPatternsTemporaryIIPro() {
        return pathPatternsTemporaryIIPro;
    }

    public String getPathPatternsForUserIIPro() {
        return pathPatternsForUserIIPro;
    }

    public String getPathPatternsDeleteIIPro() {
        return pathPatternsDeleteIIPro;
    }

    public String getPathPatternsIIPro() {
        return pathPatternsIIPro;
    }
}
