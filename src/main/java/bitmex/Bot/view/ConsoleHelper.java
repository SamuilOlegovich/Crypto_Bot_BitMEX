package bitmex.Bot.view;

import bitmex.Bot.model.FilesAndPathCreator;
import java.io.InputStreamReader;
import bitmex.Bot.model.Gasket;
import java.io.BufferedReader;
import java.io.IOException;


public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public static void writeMessage(String string) {
        Gasket.getViewThread().updateInfoView(string);
        WriterAndReadFile.writerFile(string + "\n", Gasket.getFilesAndPathCreator().getPathLogs(), true);
        //System.out.println(string);
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



    public static String getStringInfoSettings() {
        return "--- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ --- " + "\n"
                + "\n"
                + "timeBetweenOrders === " + Gasket.getTimeBetweenOrders()
                + " ----- время в секундах между выставлениями ордеров по одной стратегии\n"
                + "strategyWorkOne === " + Gasket.getStrategyWorkOne()
                + " ----- количество стратегий одновременно работающих (можно еще допелить или убрать)\n"
                + "dateDifference === " + Gasket.getDateDifference()
                + " ----- разница в часовом поясе\n"
                + "\n"

                + "rangePriceMAX === " + Gasket.getRangePriceMAX()
                + " ----- диапазон в долларах от уровней для срабатывания ордера\n"
                + "rangePriceMIN === " + Gasket.getRangePriceMIN()
                + " ----- диапазон в долларах от уровней для отмены ордера\n"
                + "\n"

                + "priceActive === " + Gasket.getPriceActive()
                + " ----- цена тригер для стоп лимитов и тейк лимитов\n"
                + "rangeLevel === " + Gasket.getRangeLevel()
                + " ----- диапазон в долларах для появления уровней\n"
                + "typeOrder === " + Gasket.getTypeOrder()
                + " ----- тип первого открываемого ордера\n"
                + "visible === " + Gasket.getVisible()
                + " ----- видимость ордера в стакане -- 0.0 - не видно, 1.0 - видно\n"
                + "\n"


                + "useRealOrNotReal === " + Gasket.isUseRealOrNotReal()
                + " ----- Выбираем счет, true - реальный счет\n"
                + "gameAllDirection === " + Gasket.isGameAllDirection()
                + " ----- true - играть во все стороны на одном счету\n"
                + "gameAllDirection === " + Gasket.isGameAllDirection()
                + " ----- true - играть во все стороны на одном счету\n"
                + "gameDirection === " + Gasket.isGameDirection()
                + " ----- направление игры при одном счете, true - Buy, false - Sell\n"
                + "twoAccounts === " + Gasket.isTwoAccounts()
                + " ----- true - два счета, можно играть в две стороны, false - только в одну сторону\n"
                + "trading === " + Gasket.isTrading()
                + " ----- торговать - true нет - false\n"
                + "\n"

                + "PORT === " + Gasket.getPORT()
                + " ----- порт подключения\n"
                + "\n"

                + "take === " + Gasket.getTake()
                + " ----- тейк профит в долларах\n"
                + "stop === " + Gasket.getStop()
                + " ----- стоп лосс в долларах\n"
                + "lot === " + Gasket.getLot()
                + " ----- количество контрактов\n"
                + "\n"

                + "PROFIT_Sell === " + Gasket.getPROFIT_Sell()
                + " ----- профит по сделкам в селл\n"
                + "PROFIT_Buy === " + Gasket.getPROFIT_Buy()
                + " ----- профит по сделкам в бай\n"
                + "PROFIT === " + Gasket.getPROFIT()
                + " ----- итоговый\n"
                + "\n"

                + "obs_5 === " + Gasket.isObs_5()
                + " ----- включить выключить стратегию\n"
                + "obs_4 === " + Gasket.isObs_4()
                + " ----- включить выключить стратегию\n"
                + "obs_3 === " + Gasket.isObs_3()
                + " ----- включить выключить стратегию\n"
                + "obs_2 === " + Gasket.isObs_2()
                + " ----- включить выключить стратегию\n"
                + "obs === " + Gasket.isObs()
                + " ----- включить выключить стратегию\n"
                + "\n"

                + "useStopLevelOrNotStopTime === " + Gasket.getUseStopLevelOrNotStopTime()
                + " ----- сколько минут отслеживать сделку вышедшею за MIN уровни\n"
                + "useStopLevelOrNotStop === " + Gasket.isUseStopLevelOrNotStop()
                + " ----- отменять или не отменять сделку вышедшею за MIN уровни\n"
                + "timeCalculationLevel === " + Gasket.getTimeCalculationLevel()
                + " ----- время за которое должны сформироваться уровни иначе все отменяется\n"
                + "timeCalculationCombinationLevel === " + Gasket.getTimeCalculationCombinationLevel()
                + " ----- когда уровни сформированы указываем время жизни данной комбинации\n"
                + "numberOfCandlesForAnalysis === " + Gasket.getNumberOfCandlesForAnalysis()
                + " ----- количество свечей для анализа диапазона где мы находимся и стоит ли делать сделку\n"
                + "maxAndMinAverage === " + Gasket.isMaxAndMinAverage()
                + " ----- при подсчете границ канала считаем среднюю пиков если - true или просто берем пики если false\n"
                + "activeNumberOfCandlesForAnalysis === " + Gasket.isActiveNumberOfCandlesForAnalysis()
                + " ----- включаем отклюаем отслеживания диапазона в котором находится цена true - включено\n"
                + "tradingPatterns === " + Gasket.isTradingPatterns()
                + " ----- включить торговлю по патернам патернов\n"
                + "takeForCollectingPatterns === " + Gasket.getTakeForCollectingPatterns()
                + " ----- тейк для сбора и накопления паттернов\n"
                + "tradingPatternsUser === " + Gasket.isTradingPatternsUser()
                + " ----- торговля по паттернам USER\n"

                + "tradingPatterns === " + Gasket.isTradingPatterns()
                + " ----- включить по патернам патернов\n"
                + "savedPatterns === " + Gasket.isSavedPatterns()
                + " ----- включить нахождение и запись патернов\n"
                + "numberOfHistoryBlocks === " + Gasket.getNumberOfHistoryBlocks()
                + " ----- количество блоков истории выше которого обрезать историю\n";
    }



    public static void printInfoSettings() {
        ConsoleHelper.writeMessage("\n\n"
                + ConsoleHelper.getStringInfoSettings()

                + "\n"

                + "\nЕСЛИ ВЫ ЖЕЛАЕТЕ - ЭТИ НАСТРОЙКИ МОЖНО ИЗМЕНИТЬ\n"
                + "ВВЕДИТЕ ЖЕЛАЕМЫЙ ПАРАМЕТР И ЗНАЧЕНИЕ В ФОРМАТЕ\n"
                + "команда=значение ----> PORT=777\n"
                + "\n");
    }



    public static void printStatistics() {
        ConsoleHelper.writeMessage("\n"
                + " --- ИТОГО на счету CEЛЛ --- " + Gasket.getPROFIT_Sell() + "\n"
                + " --- ИТОГО на счету БАЙ --- " + Gasket.getPROFIT_Buy() + "\n"
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



    public static void printStatisticsMr() {
        ConsoleHelper.writeMessage("\n"
                + " --- ИТОГО на счету CEЛЛ MR --- " + Gasket.getPROFIT_Sell_MR() + "\n"
                + " --- ИТОГО на счету БАЙ MR --- " + Gasket.getPROFIT_Buy_MR() + "\n"
                + "OB_5_TAKE_MR === " + Gasket.getOb5TakeMr() + "\n"
                + "OB_5_STOP_MR === " + Gasket.getOb5StopMr() + "\n"
                + "OS_5_TAKE_MR === " + Gasket.getOs5TakeMr() + "\n"
                + "OS_5_STOP_MR === " + Gasket.getOs5StopMr() + "\n"
                + "OB_4_TAKE_MR === " + Gasket.getOb4TakeMr() + "\n"
                + "OB_4_STOP_MR === " + Gasket.getOb4StopMr() + "\n"
                + "OS_4_TAKE_MR === " + Gasket.getOs4TakeMr() + "\n"
                + "OS_4_STOP_MR === " + Gasket.getOs4StopMr() + "\n"
                + "OB_3_TAKE_MR === " + Gasket.getOb3TakeMr() + "\n"
                + "OB_3_STOP_MR === " + Gasket.getOb3StopMr() + "\n"
                + "OS_3_TAKE_MR === " + Gasket.getOs3TakeMr() + "\n"
                + "OS_3_STOP_MR === " + Gasket.getOs3StopMr() + "\n"
                + "OB_2_TAKE_MR === " + Gasket.getOb2TakeMr() + "\n"
                + "OB_2_STOP_MR === " + Gasket.getOb2StopMr() + "\n"
                + "OS_2_TAKE_MR === " + Gasket.getOs2TakeMr() + "\n"
                + "OS_2_STOP_MR === " + Gasket.getOs2StopMr() + "\n"
                + "OB_TAKE_MR === " + Gasket.getObTakeMr() + "\n"
                + "OB_STOP_MR === " + Gasket.getObStopMr() + "\n"
                + "OS_TAKE_MR === " + Gasket.getOsTakeMr() + "\n"
                + "OS_STOP_MR === " + Gasket.getOsStopMr() + "\n"
                + "\n");
    }



    public static void printStatisticsR() {
        ConsoleHelper.writeMessage("\n"
                + " --- ИТОГО на счету CEЛЛ R --- " + Gasket.getPROFIT_Sell_R() + "\n"
                + " --- ИТОГО на счету БАЙ R --- " + Gasket.getPROFIT_Buy_R() + "\n"
                + "OB_5_TAKE_R === " + Gasket.getOb5TakeR() + "\n"
                + "OB_5_STOP_R === " + Gasket.getOb5StopR() + "\n"
                + "OS_5_TAKE_R === " + Gasket.getOs5TakeR() + "\n"
                + "OS_5_STOP_R === " + Gasket.getOs5StopR() + "\n"
                + "OB_4_TAKE_R === " + Gasket.getOb4TakeR() + "\n"
                + "OB_4_STOP_R === " + Gasket.getOb4StopR() + "\n"
                + "OS_4_TAKE_R === " + Gasket.getOs4TakeR() + "\n"
                + "OS_4_STOP_R === " + Gasket.getOs4StopR() + "\n"
                + "OB_3_TAKE_R === " + Gasket.getOb3TakeR() + "\n"
                + "OB_3_STOP_R === " + Gasket.getOb3StopR() + "\n"
                + "OS_3_TAKE_R === " + Gasket.getOs3TakeR() + "\n"
                + "OS_3_STOP_R === " + Gasket.getOs3StopR() + "\n"
                + "OB_2_TAKE_R === " + Gasket.getOb2TakeR() + "\n"
                + "OB_2_STOP_R === " + Gasket.getOb2StopR() + "\n"
                + "OS_2_TAKE_R === " + Gasket.getOs2TakeR() + "\n"
                + "OS_2_STOP_R === " + Gasket.getOs2StopR() + "\n"
                + "OB_TAKE_R === " + Gasket.getObTakeR() + "\n"
                + "OB_STOP_R === " + Gasket.getObStopR() + "\n"
                + "OS_TAKE_R === " + Gasket.getOsTakeR() + "\n"
                + "OS_STOP_R === " + Gasket.getOsStopR() + "\n"
                + "\n");
    }



    public static void printStatisticsPatterns() {
        ConsoleHelper.writeMessage("\n"
                + " --- ИТОГО на счету CEЛЛ ПАТТЕРН --- " + Gasket.getPROFIT_Sell_PAT() + "\n"
                + " --- ИТОГО на счету БАЙ ПАТТЕРН --- " + Gasket.getPROFIT_Buy_PAT() + "\n"
                + "OB_TAKE_PAT === " + Gasket.getObTakePat() + "\n"
                + "OB_STOP_PAT === " + Gasket.getObStopPat() + "\n"
                + "OS_TAKE_PAT === " + Gasket.getOsTakePat() + "\n"
                + "OS_STOP_PAT === " + Gasket.getOsStopPat() + "\n"
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
}
