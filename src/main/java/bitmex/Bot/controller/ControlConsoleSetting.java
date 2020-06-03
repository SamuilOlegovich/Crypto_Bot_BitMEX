package bitmex.Bot.controller;

import bitmex.Bot.model.bitMEX.enums.ChartDataBinSize;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;



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
                } else if (string.trim().equalsIgnoreCase("price")) {
                    ConsoleHelper.writeMessage("price now === " + Gasket.getBitmexQuote().getBidPrice());
                } else if (string.trim().equalsIgnoreCase("chart")) {
                    ConsoleHelper.writeMessage("chart === " + Gasket.getBitmexClient().getChartData(Gasket.getTicker(),
                            10, ChartDataBinSize.ONE_MINUTE));
                } else {
                    executorCommandos.parseAndExecute(string);
                }
            }
        }
    }
}
