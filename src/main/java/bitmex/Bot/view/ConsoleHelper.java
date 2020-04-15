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
        ConsoleHelper.writeMessage("timeBetweenOrders === " + Gasket.getTimeBetweenOrders());
        ConsoleHelper.writeMessage("gameAllDirection === " + Gasket.isGameAllDirection());
        ConsoleHelper.writeMessage("strategeWorkOne === " + Gasket.getStrategeWorkOne());
        ConsoleHelper.writeMessage("dateDifference === " + Gasket.getDateDifference());
        ConsoleHelper.writeMessage("rangePriceMAX === " + Gasket.getRangePriceMAX());
        ConsoleHelper.writeMessage("rangePriceMIN === " + Gasket.getRangePriceMIN());
        ConsoleHelper.writeMessage("gameDirection === " + Gasket.isGameDirection());
        ConsoleHelper.writeMessage("PROFIT_Sell === " + Gasket.getPROFIT_Sell());
        ConsoleHelper.writeMessage("twoAccounts === " + Gasket.isTwoAccounts());
        ConsoleHelper.writeMessage("priceActiv === " + Gasket.getPriceActiv());
        ConsoleHelper.writeMessage("PROFIT_Buy === " + Gasket.getPROFIT_Buy());
        ConsoleHelper.writeMessage("rangeLivel === " + Gasket.getRangeLivel());
        ConsoleHelper.writeMessage("typeOrder === " + Gasket.getTypeOrder());
        ConsoleHelper.writeMessage("visible === " + Gasket.getVisible());
        ConsoleHelper.writeMessage("trading === " + Gasket.isTrading());
        ConsoleHelper.writeMessage("PROFIT === " + Gasket.getPROFIT());
        ConsoleHelper.writeMessage("take === " + Gasket.getTake());
        ConsoleHelper.writeMessage("stop === " + Gasket.getStop());
        ConsoleHelper.writeMessage("PORT === " + Gasket.getPORT());
        ConsoleHelper.writeMessage("lot === " + Gasket.getLot());
    }

    public static void printStatistics() {
        ConsoleHelper.writeMessage("SOS_R_STOP === " + Gasket.getSosRStop());
        ConsoleHelper.writeMessage("SOS_R_TAKE === " + Gasket.getSosRTake());
        ConsoleHelper.writeMessage("SOS_T_STOP === " + Gasket.getSosTStop());
        ConsoleHelper.writeMessage("SOS_T_TAKE === " + Gasket.getSosTTake());
        ConsoleHelper.writeMessage("SOB_R_STOP === " + Gasket.getSobRStop());
        ConsoleHelper.writeMessage("SOB_R_TAKE === " + Gasket.getSobRTake());
        ConsoleHelper.writeMessage("SOB_T_STOP === " + Gasket.getSobTStop());
        ConsoleHelper.writeMessage("SOB_T_TAKE === " + Gasket.getSobTTake());
        ConsoleHelper.writeMessage("SOS_STOP === " + Gasket.getSosStop());
        ConsoleHelper.writeMessage("SOS_TAKE === " + Gasket.getSosTake());
        ConsoleHelper.writeMessage("SOB_STOP === " + Gasket.getSobStop());
        ConsoleHelper.writeMessage("SOB_TAKE === " + Gasket.getSobTake());
        ConsoleHelper.writeMessage("OB_STOP === " + Gasket.getObStop());
        ConsoleHelper.writeMessage("OB_TAKE === " + Gasket.getObTake());
        ConsoleHelper.writeMessage("OS_STOP === " + Gasket.getOsStop());
        ConsoleHelper.writeMessage("OS_TAKE === " + Gasket.getOsTake());
        ConsoleHelper.writeMessage("");
    }
}
