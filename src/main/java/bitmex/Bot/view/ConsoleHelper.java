package bitmex.Bot.view;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String string) {
        System.out.println(string);
    }

    public static String readString() {
        try {
            return reader.readLine();
        } catch (IOException e){
            writeMessage("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
            return readString();
        }
    }

    public static int readInt() {
        try {
            return Integer.parseInt(readString());
        } catch (NumberFormatException e) {
            writeMessage("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            return readInt();
        }
    }

    public static void printInfoSettings() {
        ConsoleHelper.writeMessage("\n" + "--- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ --- " + "\n"
                + "timeBetweenOrders === " + Gasket.getTimeBetweenOrders() + "\n"
                + "gameAllDirection === " + Gasket.isGameAllDirection() + "\n"
                + "strategeWorkOne === " + Gasket.getStrategeWorkOne() + "\n"
                + "dateDifference === " + Gasket.getDateDifference() + "\n"
                + "rangePriceMAX === " + Gasket.getRangePriceMAX() + "\n"
                + "rangePriceMIN === " + Gasket.getRangePriceMIN() + "\n"
                + "gameDirection === " + Gasket.isGameDirection() + "\n"
                + "PROFIT_Sell === " + Gasket.getPROFIT_Sell() + "\n"
                + "twoAccounts === " + Gasket.isTwoAccounts() + "\n"
                + "priceActiv === " + Gasket.getPriceActiv() + "\n"
                + "PROFIT_Buy === " + Gasket.getPROFIT_Buy() + "\n"
                + "rangeLivel === " + Gasket.getRangeLivel() + "\n"
                + "typeOrder === " + Gasket.getTypeOrder() + "\n"
                + "visible === " + Gasket.getVisible() + "\n"
                + "trading === " + Gasket.isTrading() + "\n"
                + "PROFIT === " + Gasket.getPROFIT() + "\n"
                + "take === " + Gasket.getTake() + "\n"
                + "stop === " + Gasket.getStop() + "\n"
                + "PORT === " + Gasket.getPORT() + "\n"
                + "lot === " + Gasket.getLot() + "\n"
                + "strategyOneRange === " + Gasket.isStrategyOneRange() + "\n"
                + "strategyOneTime === " + Gasket.isStrategyOneTime() + "\n"
                + "strategyOne === " + Gasket.isStrategyOne() + "\n"
                + "one === " + Gasket.isOne() + "\n"  + "\n"
                + "ЕСЛИ ВЫ ЖЕЛАЕТЕ - ЭТИ НАСТРОЙКИ МОЖНО ИЗМЕНИТЬ" + "\n"
                + "ВВЕДИТЕ ВВЕДИТЕ ЖЕЛАЕМЫЙ ПАРАМЕТР И ЗНАЧЕНИЕ В ФОРМАТЕ"  + "\n"
                + "ПРИМЕР: lot=10.0" + "\n");
    }

    public static void printStatistics() {
        ConsoleHelper.writeMessage("\n" + "SOS_R_STOP === " + Gasket.getSosRStop() + "\n"
                + "SOS_R_TAKE === " + Gasket.getSosRTake() + "\n"
                + "SOS_T_STOP === " + Gasket.getSosTStop() + "\n"
                + "SOS_T_TAKE === " + Gasket.getSosTTake() + "\n"
                + "SOB_R_STOP === " + Gasket.getSobRStop() + "\n"
                + "SOB_R_TAKE === " + Gasket.getSobRTake() + "\n"
                + "SOB_T_STOP === " + Gasket.getSobTStop() + "\n"
                + "SOB_T_TAKE === " + Gasket.getSobTTake() + "\n"
                + "SOS_STOP === " + Gasket.getSosStop() + "\n"
                + "SOS_TAKE === " + Gasket.getSosTake() + "\n"
                + "SOB_STOP === " + Gasket.getSobStop() + "\n"
                + "SOB_TAKE === " + Gasket.getSobTake() + "\n"
                + "OB_STOP === " + Gasket.getObStop() + "\n"
                + "OB_TAKE === " + Gasket.getObTake() + "\n"
                + "OS_STOP === " + Gasket.getOsStop() + "\n"
                + "OS_TAKE === " + Gasket.getOsTake() + "\n");
    }
}
