package bitmex.Bot.view;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.enums.TypeData;
import java.io.InputStreamReader;

import java.io.BufferedReader;
import java.io.IOException;

import static bitmex.Bot.model.Gasket.*;
import static bitmex.Bot.model.Gasket.getViewThread;
import static bitmex.Bot.model.Gasket.getViewThread;
import static bitmex.Bot.model.WriterAndReadFile.writerFile;
import static bitmex.Bot.model.enums.TypeData.MARTINGALE;
import static bitmex.Bot.model.enums.TypeData.REAL;


public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public static void writeMessage(String string) {
        getViewThread().updateInfoView(string);
        writerFile(string + "\n", getFilesAndPathCreator().getPathLogs(), true);
//        System.out.println(string);
    }




    public static void writeERROR(String string) {
        String out = string + " --- " + TypeData.ERROR.toString();
        if (Gasket.isERROR()) {
            getViewThread().updateInfoView(out);
        }
        writerFile(out + "\n", getFilesAndPathCreator().getPathLogs(), true);
//        System.out.println(out);
    }



    public static void writeDEBUG(String string) {
        String out = string + " --- " + TypeData.DEBUG.toString();
        if (Gasket.isDEBUG()) {
            getViewThread().updateInfoView(out);
        }
        writerFile(out + "\n", getFilesAndPathCreator().getPathLogs(), true);
//        System.out.println(out);
    }


    public static void writeINFO(String string) {
        String out = string + " --- " + TypeData.INFO.toString();
        if (Gasket.isINFO()) {
            getViewThread().updateInfoView(out);
        }
        writerFile(out + "\n", getFilesAndPathCreator().getPathLogs(), true);
//        System.out.println(out);
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
        return "--- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ ---" + "\n"
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
//                + "gameAllDirection === " + Gasket.isGameAllDirection()
//                + " ----- true - играть во все стороны на одном счету\n"
                + "gameDirection === " + Gasket.isGameDirection()
                + " ----- направление игры при одном счете, true - Buy, false - Sell\n"
                + "twoAccounts === " + Gasket.isTwoAccounts()
                + " ----- true - два счета, можно играть в две стороны, false - только в одну сторону\n"
                + "trading === " + Gasket.isTrading()
                + " ----- торговать - true нет - false\n"
                + "tradingTestII === " + Gasket.isTradingTestII()
                + " ----- на реале торговать - true нет - false\n"
                + "tradingTestUser === " + Gasket.isTradingTestUser()
                + " ----- на реале торговать - true нет - false\n"
                + "tradingUser === " + Gasket.isTradingUser()
                + " ----- на реале торговать - true нет - false\n"
                + "tradingII === " + Gasket.isTradingII()
                + " ----- на реале торговать - true нет - false\n"
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
                + "tradingPatterns === " + Gasket.isTradingPatternsII()
                + " ----- включить торговлю по патернам патернов\n"
                + "takeForCollectingPatterns === " + Gasket.getTakeForCollectingPatterns()
                + " ----- тейк для сбора и накопления паттернов\n"
                + "tradingPatternsUser === " + Gasket.isTradingPatternsUser()
                + " ----- торговля по паттернам USER\n"

                + "tradingPatterns === " + Gasket.isTradingPatternsII()
                + " ----- включить по патернам патернов\n"
                + "savedPatternsIIPro === " + Gasket.isSavedPatternsIIPro()
                + " ----- включить нахождение и запись патернов\n"
                + "savedPatterns === " + Gasket.isSavedPatternsII()
                + " ----- включить нахождение и запись патернов\n"
                + "timeStopLiveForUserPatterns === " + Gasket.getTimeStopLiveForUserPatterns()
                + " ----- время за которое паттерн должен отработать\n"
                + "numberOfHistoryBlocks === " + Gasket.getNumberOfHistoryBlocks()
                + " ----- количество блоков истории выше которого обрезать историю\n"
                + "showLoadPatternsUser === " + Gasket.isShowLoadPatternsUser()
                + " ----- показывать загрузку паттернов при запуске программы\n"
                + "showLoadPatternsII === " + Gasket.isShowLoadPatternsII()
                + " ----- показывать загрузку паттернов при запуске программы\n"
                + "showLoadPatternsIIPro === " + Gasket.isShowLoadPatternsIIPro()
                + " ----- показывать загрузку паттернов при запуске программы\n"
                + "levelsToCompare === " + Gasket.getLevelsToCompare()
                + " ----- уровни для сравнения II Pro\n"
                + "levelsForTrimmedPatterns === " + Gasket.getLevelsForTrimmedPatterns()
                + " ----- уровни которые следует оставлять для урезаных паттернов USERR"
                + "ERROR === " + Gasket.isERROR()
                + " ----- включить - отключить показ ошибок в окне программы\n"
                + "DEBUG === " + Gasket.isDEBUG()
                + " ----- включить - отключить показ ошибок в окне программы\n"
                + "INFO === " + Gasket.isINFO()
                + " ----- включить - отключить показ ошибок в окне программы\n"
                + "predictor === " + Gasket.isPredictor()
                + " ----- включить - отключить предсказателя собственных сделок\n"
                + "periodNULL === " + Gasket.getPeriodNULL()
                + " ----- если заменяем данные то на что\n"
                + "previewNULL === " + Gasket.getPreviewNULL()
                + " ----- если заменяем данные то на что\n"
                + "timeNULL === " + Gasket.getTimeNULL()
                + " ----- если заменяем данные то на что\n"
                + "priceNULL === " + Gasket.getPriceNULL()
                + " ----- если заменяем данные то на что\n"
                + "valueNULL === " + Gasket.getValueNULL()
                + " ----- если заменяем данные то на что\n"
                + "typeNULL === " + Gasket.getTypeNULL()
                + " ----- если заменяем данные то на что\n"
                + "avgNULL === " + Gasket.getAvgNULL()
                + " ----- если заменяем данные то на что\n"
                + "dirNULL === " + Gasket.getDirNULL()
                + " ----- если заменяем данные то на что\n"
                + "openNULL === " + Gasket.getOpenNULL()
                + " ----- если заменяем данные то на что\n"
                + "closeNULL === " + Gasket.getCloseNULL()
                + " ----- если заменяем данные то на что\n"
                + "highNULL === " + Gasket.getHighNULL()
                + " ----- если заменяем данные то на что\n"
                + "lowNULL === " + Gasket.getLowNULL()
                + " ----- если заменяем данные то на что\n"
                + "replaceDataWithNULL === " + Gasket.isReplaceDataWithNULL()
                + " ----- включить выключить замену данных\n"
                + "replaceDataWithNULLPro === " + Gasket.isReplaceDataWithNULL()
                + " ----- включить выключить замену данных\n"
                + "enableDisableReplacementIDinPatternsUser === "
                + Gasket.isEnableDisableReplacementIDinPatternsUser()
                + " ----- включить выключить замену id в паттернах USER\n"
                + "addOrTESTatTheEndOfTheLine === " + Gasket.isReplaceDataWithNULL()
                + " ----- добавлять или нет тест в конце строки\n"
                + "testOrRealAtTheEnd === " + Gasket.isTestOrRealAtTheEnd()
                + " ----- на что меняем окончание строки ID - на тест(false) или на реал(true)\n"
                + "indexRatioTransactionsAtWhichEnterMarket === "
                + Gasket.getIndexRatioTransactionsAtWhichEnterMarket()
                + " ----- индекс соотношения сделок прикотором входим в рынок\n"
                + "\n"
//                + TypeData.MARTINGALE.toString() + "\n"
                + "martingaleOpenOneLot === " + getMartingaleOpenOneLot()
                + " ----- количество первой сделки при мартингейле\n"
                + "martingaleOnOff === " + isMartingaleOnOff()
                + " ----- включить выключить игру по мартингейлу\n"
                + "martingaleIndex === " + getMartingaleIndex()
                + " ----- индекс мартингейла\n"
                + "martingaleMaxSteep === " + getMartingaleMaxSteep()
                + " ----- максимально разрешенный шаг\n"
                + "tradingTestMartingale === " + isTradingTestMartingale()
                + " ----- включить тестировку мартингейла\n"
                + "tradingMartingale === " + isTradingMartingale()
                + " ----- торговать или нет с помощью мартингейла\n"
                + "broadcastSignalsFurther === " + isBroadcastSignalsFurther()
                + " ----- транслировать сигналы дальше для других програм\n"
                + "BroadcastAddresses === " + getBroadcastAddresses()
                + " -----  адреса на которые надо ретраслировать сигналы"

                ;
    }



    public static void printInfoSettings() {
        writeMessage("\n\n"
                + getStringInfoSettings()

                + "\n"

                + "\nЕСЛИ ВЫ ЖЕЛАЕТЕ - ЭТИ НАСТРОЙКИ МОЖНО ИЗМЕНИТЬ\n"
                + "ВВЕДИТЕ ЖЕЛАЕМЫЙ ПАРАМЕТР И ЗНАЧЕНИЕ В ФОРМАТЕ\n"
                + "команда=значение ----> PORT=777\n"
                + "\n"
        );
    }



    public static void printStatistics() {
        writeMessage("\n"
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
                + "\n"
        );
    }



    public static void printStatisticsMr() {
        writeMessage("\n"
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
        writeMessage("\n"
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
                + "\n"
        );
    }



    public static void printStatisticsPatterns() {
        writeMessage("\n"
                + " --- ИТОГО на счету CEЛЛ ПАТТЕРН --- " + Gasket.getPROFIT_Sell_PAT() + "\n"
                + " --- ИТОГО на счету БАЙ ПАТТЕРН --- " + Gasket.getPROFIT_Buy_PAT() + "\n"
                + "OB_TAKE_PAT === " + Gasket.getObTakePat() + "\n"
                + "OB_STOP_PAT === " + Gasket.getObStopPat() + "\n"
                + "OS_TAKE_PAT === " + Gasket.getOsTakePat() + "\n"
                + "OS_STOP_PAT === " + Gasket.getOsStopPat() + "\n"
                + "\n"
        );
    }



    public static void printFlag() {
        writeMessage("\n"
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
                + "\n"
        );
    }



    public static void printStatisticsMartingale() {
        writeMessage("\n"
                + "   ----- ***** " + MARTINGALE.toString() + " ***** -----\n"
                + "        PROFIT === " + Gasket.getMartingaleClass().getMartingalePROFIT() + "\n"
                + "   -----     =====     -----\n"
                + "   ----- ***** " + MARTINGALE.toString() + " - " + REAL.toString() + " ***** -----\n"
                + "        PROFIT === " + Gasket.getMartingaleClass().getMartingalePROFIT() + "\n"
                + "   ----- ******************************** -----\n\n");
    }


    public static void showCommands() {
        writeMessage("\n\n"
                + "SETTINGS=RESTART программа перезапустит настройки не отключаясь\n"
                + "enableDisableReplacementIDinPatternsUser\n"
                + "indexRatioTransactionsAtWhichEnterMarket\n"
                + "activeNumberOfCandlesForAnalysis\n"
                + "timeCalculationCombinationLevel\n"
                + "timeStopLiveForUserPatterns\n"
                + "addOrTESTatTheEndOfTheLine\n"
                + "numberOfCandlesForAnalysis\n"
                + "takeForCollectingPatterns\n"
                + "useStopLevelOrNotStopTime\n"
                + "levelsForTrimmedPatterns\n"
                + "broadcastSignalsFurther\n"
                + "replaceDataWithNULLPro\n"
                + "useStopLevelOrNotStop\n"
                + "numberOfHistoryBlocks\n"
                + "showLoadPatternsUser\n"
                + "timeCalculationLevel\n"
                + "martingaleOpenOneLot\n"
                + "replaceDataWithNULL\n"
                + "tradingPatternsUser\n"
                + "apiKeyName2Accounts\n"
                + "martingaleMaxSteep\n"
                + "broadcastAddresses\n"
                + "testOrRealAtTheEnd\n"
                + "showLoadPatternsII\n"
                + "timeBetweenOrders\n"
                + "gameAllDirection\n"
                + "useRealOrNotReal\n"
                + "maxAndMinAverage\n"
                + "secondsSleepTime\n"
                + "martingaleIndex\n"
                + "martingaleOnOff\n"
                + "tradingPatterns\n"
                + "strategyWorkOne\n"
                + "apiKey2Accounts\n"
                + "tradingTestUser\n"
                + "levelsToCompare\n"
                + "dateDifference\n"
                + "rangePriceMIN\n"
                + "rangePriceMAX\n"
                + "gameDirection\n"
                + "savedPatterns\n"
                + "tradingTestII\n"
                + "twoAccounts\n"
                + "oneSellFLAG\n"
                + "priceActive\n"
                + "PROFIT_Sell\n"
                + "tradingUser\n"
                + "previewNULL\n"
                + "PROFIT_Buy\n"
                + "apiKeyName\n"
                + "oneBuyFLAG\n"
                + "rangeLevel\n"
                + "periodNULL\n"
                + "tradingII\n"
                + "priceNULL\n"
                + "valueNULL\n"
                + "typeOrder\n"
                + "predictor\n"
                + "closeNULL\n"
                + "showSteps\n"
                + "timeNULL\n"
                + "openNULL\n"
                + "highNULL\n"
                + "typeNULL\n"
                + "avgNULL\n"
                + "dirNULL\n"
                + "lowNULL\n"
                + "visible\n"
                + "trading\n"
                + "PROFIT\n"
                + "apiKey\n"
                + "DEBUG \n"
                + "obs_2\n"
                + "obs_5\n"
                + "obs_4\n"
                + "ERROR\n"
                + "obs_3\n"
                + "PORT\n"
                + "stop\n"
                + "take\n"
                + "INFO\n"
                + "obs\n"
                + "lot\n"
                + "\n\n"
        );
    }
}
