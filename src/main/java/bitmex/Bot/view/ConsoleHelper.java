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

    public static void printInfoSettings() {
        ConsoleHelper.writeMessage("\n" + "--- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ --- " + "\n\n"
                + "timeBetweenOrders === " + Gasket.getTimeBetweenOrders()
                + " === время в секундах между выставлениями ордеров по одной стратегии\n"
                + "strategeWorkOne === " + Gasket.getStrategyWorkOne()
                + " === количество стратегий одновременно работающих (можно еще допелить или убрать)\n"
                + "dateDifference === " + Gasket.getDateDifference()
                + " ==== разница в часовом поясе\n\n"

                + "rangePriceMAX === " + Gasket.getRangePriceMAX()
                + " === диапазон в долларах от уровней для срабатывания ордера\n"
                + "rangePriceMIN === " + Gasket.getRangePriceMIN()
                + " === диапазон в долларах от уровней для отмены ордера\n\n"

                + "priceActiv === " + Gasket.getPriceActive()
                + " === цена тригер для стоп лимитов и тейк лимитов\n"
                + "rangeLivel === " + Gasket.getRangeLevel()
                + " === диапазон в долларах для появления уровней\n"
                + "typeOrder === " + Gasket.getTypeOrder()
                + " === тип первого открываемого ордера\n"
                + "visible === " + Gasket.getVisible()
                + " === видимость ордера в стакане -- 0.0 - не видно, 1.0 - видно\n\n"


                + "useRealOrNotReal === " + Gasket.isUseRealOrNotReal()
                + " === Выбираем счет, true - реальный счет\n"
                + "gameAllDirection === " + Gasket.isGameAllDirection()
                + " === true - играть во все стороны на одном счету\n"
                + "gameAllDirection === " + Gasket.isGameAllDirection()
                + " === true - играть во все стороны на одном счету\n"
                + "gameDirection === " + Gasket.isGameDirection()
                + " === направление игры при одном счете, true - Buy, false - Sell\n"
                + "twoAccounts === " + Gasket.isTwoAccounts()
                + " === true - два счета, можно играть в две стороны, false - только в одну сторону\n"
                + "trading === " + Gasket.isTrading()
                + " === торговать - true нет - false\n\n"

                + "PORT === " + Gasket.getPORT()
                + " === порт подключения\n\n"

                + "take === " + Gasket.getTake()
                + " === тейк профит в долларах\n"
                + "stop === " + Gasket.getStop()
                + " === стоп лосс в долларах\n"
                + "lot === " + Gasket.getLot()
                + " === количество контрактов\n\n"

                + "PROFIT_Sell === " + Gasket.getPROFIT_Sell()
                + " === профит по сделкам в селл\n"
                + "PROFIT_Buy === " + Gasket.getPROFIT_Buy()
                + " === профит по сделкам в бай\n"
                + "PROFIT === " + Gasket.getPROFIT()
                + " === итоговый\n\n"

                + "strategyOneRange === " + Gasket.isStrategyOneRange()
                + " === включить выключить стратегию\n"
                + "strategyOneTime === " + Gasket.isStrategyOneTime()
                + " === включить выключить стратегию\n"
                + "strategyOne === " + Gasket.isStrategyOne()
                + " === включить выключить стратегию\n"
                + "one === " + Gasket.isOne()
                + " === включить выключить стратегию\n"

                + "useStopLevelOrNotStopTime === " + Gasket.getUseStopLevelOrNotStopTime()
                + " === сколько минут отслеживать сделку вышедшею за MIN уровни\n"
                + "useStopLevelOrNotStop === " + Gasket.isUseStopLevelOrNotStop()
                + " === отменять или не отменять сделку вышедшею за MIN уровни\n"

                + "\nЕСЛИ ВЫ ЖЕЛАЕТЕ - ЭТИ НАСТРОЙКИ МОЖНО ИЗМЕНИТЬ\n"
                + "ВВЕДИТЕ ЖЕЛАЕМЫЙ ПАРАМЕТР И ЗНАЧЕНИЕ В ФОРМАТЕ\n"
                + "ПРИМЕР: lot=10.0\n");
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




    // TEST
    public static void main(String[] args) {
        ConsoleHelper.printInfoSettings();
    }
}
