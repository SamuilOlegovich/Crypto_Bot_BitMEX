package bitmex.Bot.view;

import bitmex.Bot.model.strategies.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;


public class ConsoleHelper {
    private static String path = "/Users/samuilolegovich/Desktop/bitmex-client-master/src/main/java/bitmex/Bot/Logs/";
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static boolean crateLogFile = true;


    public static void writeMessage(String string) {
        if (crateLogFile) {
            crateLogFile = false;
            getPathString();

            try {
                WriterAndReadFile.createNewFile(path);
                try {
                    WriterAndReadFile.writerFile(string, path, true);
                } catch (Exception e) {
                    System.out.println("Не удалось записать меседж в лог.");
                }
            } catch (Exception e) {
                System.out.println("Не удалось создать Лог файл.");
            }
        } else {
            try {
                WriterAndReadFile.writerFile(string, path, true);
            } catch (Exception e) {
                System.out.println("Не удалось записать меседж в лог.");
            }
            System.out.println(string);
        }
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
                + "\n"
                + "timeBetweenOrders === " + Gasket.getTimeBetweenOrders()
                + " === время в секундах между выставлениями ордеров по одной стратегии\n"
                + "strategyWorkOne === " + Gasket.getStrategyWorkOne()
                + " === количество стратегий одновременно работающих (можно еще допелить или убрать)\n"
                + "dateDifference === " + Gasket.getDateDifference()
                + " ==== разница в часовом поясе\n"
                + "\n"

                + "rangePriceMAX === " + Gasket.getRangePriceMAX()
                + " === диапазон в долларах от уровней для срабатывания ордера\n"
                + "rangePriceMIN === " + Gasket.getRangePriceMIN()
                + " === диапазон в долларах от уровней для отмены ордера\n"
                + "\n"

                + "priceActive === " + Gasket.getPriceActive()
                + " === цена тригер для стоп лимитов и тейк лимитов\n"
                + "rangeLevel === " + Gasket.getRangeLevel()
                + " === диапазон в долларах для появления уровней\n"
                + "typeOrder === " + Gasket.getTypeOrder()
                + " === тип первого открываемого ордера\n"
                + "visible === " + Gasket.getVisible()
                + " === видимость ордера в стакане -- 0.0 - не видно, 1.0 - видно\n"
                + "\n"


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
                + " === торговать - true нет - false\n"
                + "\n"

                + "PORT === " + Gasket.getPORT()
                + " === порт подключения\n"
                + "\n"

                + "take === " + Gasket.getTake()
                + " === тейк профит в долларах\n"
                + "stop === " + Gasket.getStop()
                + " === стоп лосс в долларах\n"
                + "lot === " + Gasket.getLot()
                + " === количество контрактов\n"
                + "\n"

                + "PROFIT_Sell === " + Gasket.getPROFIT_Sell()
                + " === профит по сделкам в селл\n"
                + "PROFIT_Buy === " + Gasket.getPROFIT_Buy()
                + " === профит по сделкам в бай\n"
                + "PROFIT === " + Gasket.getPROFIT()
                + " === итоговый\n"
                + "\n"

                + "strategyOneRange === " + Gasket.isOb_3()
                + " === включить выключить стратегию\n"
                + "strategyOneTime === " + Gasket.isOb_4()
                + " === включить выключить стратегию\n"
                + "strategyOne === " + Gasket.isOb_2()
                + " === включить выключить стратегию\n"
                + "one === " + Gasket.isOb()
                + " === включить выключить стратегию\n"
                + "\n"

                + "useStopLevelOrNotStopTime === " + Gasket.getUseStopLevelOrNotStopTime()
                + " === сколько минут отслеживать сделку вышедшею за MIN уровни\n"
                + "useStopLevelOrNotStop === " + Gasket.isUseStopLevelOrNotStop()
                + " === отменять или не отменять сделку вышедшею за MIN уровни\n"
                + "timeCalculationLevel === " + Gasket.getTimeCalculationLevel()
                + " === время за которое должны сформироваться уровни иначе все отменяется\n"
                + "timeCalculationCombinationLevel === " + Gasket.getTimeCalculationCombinationLevel()
                + " === когда уровни сформированы указываем время жизни данной комбинации\n"
                + "numberOfCandlesForAnalysis === " + Gasket.getNumberOfCandlesForAnalysis()
                + " === количество свечей для анализа диапазона где мы находимся и стоит ли делать сделку\n"
                + "maxAndMinAverage === " + Gasket.isMaxAndMinAverage()
                + " === при подсчете границ канала считаем среднюю пиков если - true или просто берем пики если false\n"
                + "\n"

                + "\nЕСЛИ ВЫ ЖЕЛАЕТЕ - ЭТИ НАСТРОЙКИ МОЖНО ИЗМЕНИТЬ\n"
                + "ВВЕДИТЕ ЖЕЛАЕМЫЙ ПАРАМЕТР И ЗНАЧЕНИЕ В ФОРМАТЕ\n"
                + "ПРИМЕР: lot=10.0\n"
                + "\n");
    }

    public static void printStatistics() {
        ConsoleHelper.writeMessage("\n"
                + "OB_5_TAKE === " + Gasket.getOb5Take() + "\n"
                + "OB_5_STOP === " + Gasket.getOb5Stop() + "\n"
                + "OS_5_TAKE === " + Gasket.getOs5Take() + "\n"
                + "OS_5_STOP === " + Gasket.getOs5Stop() + "\n"
                + "OB_4_TAKE === " + Gasket.getOb4Take() + "\n"
                + "OB_4_STOP === " + Gasket.getOb4Stop() + "\n"
                + "OS_4_TAKE === " + Gasket.getOs4Take() + "\n"
                + "OS_4_STOP === " + Gasket.getOs4Stop() + "\n"
                + "OB_3_TAKE === " + Gasket.getOb3Take() + "\n"
                + "OB_3_STOP === " + Gasket.getOb3Stop() + "\n"
                + "OS_3_TAKE === " + Gasket.getOs3Take() + "\n"
                + "OS_3_STOP === " + Gasket.getOs3Stop() + "\n"
                + "OB_2_TAKE === " + Gasket.getOb2Take() + "\n"
                + "OB_2_STOP === " + Gasket.getOb2Stop() + "\n"
                + "OS_2_TAKE === " + Gasket.getOs2Take() + "\n"
                + "OS_2_STOP === " + Gasket.getOs2Stop() + "\n"
                + "OB_TAKE === " + Gasket.getObTake() + "\n"
                + "OB_STOP === " + Gasket.getObStop() + "\n"
                + "OS_TAKE === " + Gasket.getOsTake() + "\n"
                + "OS_STOP === " + Gasket.getOsStop() + "\n"
                + "\n");
    }

    public static void printFlag() {
        ConsoleHelper.writeMessage("\n"
                + "OB_5 === " + Gasket.isObFlag_5() + "\n"
                + "OS_5 === " + Gasket.isOsFlag_5() + "\n"
                + "OB_4 === " + Gasket.isObFlag_4() + "\n"
                + "OS_4 === " + Gasket.isOsFlag_4() + "\n"
                + "OB_3 === " + Gasket.isObFlag_3() + "\n"
                + "OS_3 === " + Gasket.isOsFlag_3() + "\n"
                + "OB_2 === " + Gasket.isObFlag_2() + "\n"
                + "OS_2 === " + Gasket.isOsFlag_2() + "\n"
                + "OB === " + Gasket.isObFlag() + "\n"
                + "OS === " + Gasket.isOsFlag() + "\n"
                + "\n");
    }


    private static void getPathString() {
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        path = Gasket.getPath() + "Logs/" + DatesTimes.getDate() + "=Log.txt";
    }



    // TEST
    public static void main(String[] args) {
        ConsoleHelper.printInfoSettings();
    }
}
