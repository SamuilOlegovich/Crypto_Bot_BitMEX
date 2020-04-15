package bitmex.Bot.view;

import bitmex.Bot.model.Gasket;

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
