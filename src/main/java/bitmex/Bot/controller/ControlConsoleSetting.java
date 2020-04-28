package bitmex.Bot.controller;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

public class ControlConsoleSetting extends Thread {
    private ExecutorCommandos executorCommandos;

    public ControlConsoleSetting(ExecutorCommandos executorCommandos) {
        this.executorCommandos = executorCommandos;
    }

    @Override
    public void run() {
        while (true) {
            String string = ConsoleHelper.readString();
            if (string.length() > 3) {
                if (string.trim().equalsIgnoreCase("info")) {
                    ConsoleHelper.printInfoSettings();
                } else if (string.trim().equalsIgnoreCase("flag")) {
                    ConsoleHelper.printFlag();
                } else {
                    executorCommandos.parserAndExecutor(string);
                }
            }
        }
    }
}
