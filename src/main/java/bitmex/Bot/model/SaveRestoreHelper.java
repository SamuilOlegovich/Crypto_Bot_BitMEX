package bitmex.Bot.model;

import bitmex.Bot.model.strategies.II.SavedPatterns;
import bitmex.Bot.view.ConsoleHelper;

import java.io.*;

public class SaveRestoreHelper {

    public synchronized static void saveSavedPatternsClass() {
        try {
            FileOutputStream outputStream = new FileOutputStream(Gasket.getFilesAndPathCreator().getPathPatterns());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(Gasket.getSavedPatternsClass());
            objectOutputStream.flush();
            objectOutputStream.close();
            ConsoleHelper.writeMessage(DatesTimes.getDate() + " --- SavedPatterns сохранен");
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Не удалось сохранить SavePattern, "
                    + "что-то не так в методе saveSavePatternClass, "
                    + "класса SaveRestoreHelper");
        }
    }

    public synchronized static void restoreSavedPatternsClass() {

        try {
            FileInputStream fileInputStream = new FileInputStream(Gasket.getFilesAndPathCreator().getPathPatterns());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Gasket.setSavedPatternsClass((SavedPatterns) objectInputStream.readObject());
            fileInputStream.close();
            ConsoleHelper.writeMessage(DatesTimes.getDate() + " --- SavedPatterns востановлен");
        } catch (ClassNotFoundException | IOException e) {
            ConsoleHelper.writeMessage("Не удалось востановить SavePattern, "
                    + "что-то не так в методе restoreSavePatternClass, "
                    + "класса SaveRestoreHelper");
        }
    }
}
