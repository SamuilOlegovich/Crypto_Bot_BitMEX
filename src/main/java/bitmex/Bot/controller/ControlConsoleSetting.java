package bitmex.Bot.controller;

import bitmex.Bot.view.ConsoleHelper;

public class ControlConsoleSetting extends Thread {

    public ControlConsoleSetting() {
        ConsoleHelper.printInfoSettings();
        start();
    }

    @Override
    public void run() {
        while (true) {
            String string = ConsoleHelper.readString();
            if (string.length() > 3) parserAndExecutor(string);
        }
    }

    private void parserAndExecutor(String string) {
        String[] strings = string.trim().split("=");
        if (strings.length < 2) {
            ConsoleHelper.writeMessage("\nВы допустили ошибку, повторите ввод.\n");
            return;
        }


    }
}
