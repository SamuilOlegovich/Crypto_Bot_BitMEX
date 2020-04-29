package bitmex.Bot.model;

import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;

import java.util.List;

import static bitmex.Bot.model.bitMEX.enums.ChartDataBinSize.ONE_MINUTE;

public class Gasket {
            // флаги для разных режимов работы стратегий (можно дорабоать)
    private static int timeCalculationCombinationLevel = 20;        // когда уровни сформированы указываем время жизни данной комбинации
    private static boolean useStopLevelOrNotStop = true;            // отменять или не отменять сделку вышедшею за MIN уровни
    private static int numberOfCandlesForAnalysis = 60;             // количество свечей для анализа диапазона где мы находимся и стоит ли делать сделку
    private static volatile boolean oneSellFLAG = true;
    private static volatile boolean oneBuyFLAG = true;
    private static int useStopLevelOrNotStopTime = 10;              // сколько минут отслеживать сделку вышедшею за MIN уровни
    private static BitmexClient bitmexClient2Accounts;
    private static volatile BitmexQuote bitmex2Quote;   // для получения данных по насущной котировке.
    private static volatile double PROFIT_Sell = 0.0;   // профит по сделкам в селл
    private static volatile double PROFIT_Buy = 0.0;    // профит по сделкам в бай
    private static boolean gameAllDirection = false;    // true - играть во все стороны на одном счету
    private static volatile BitmexQuote bitmexQuote;    // для получения данных по насущной котировке.
    private static BitmexChartData bitmexChartData;     // для получение данных по истории свечек
    private static boolean maxAndMinAverage = true;     // при подсчете границ канала считаем среднюю пиков если - true или просто берем пики если false
    private static boolean useRealOrNotReal = true;     // true - реальный счет
    private static int timeCalculationLevel = 50;       // время за которое должны сформироваться уровни иначе все отменяется
    private static volatile double PROFIT = 0.0;        // итоговый профит
    private static boolean gameDirection = true;        // направление игры при одном счете, true - Buy, false - Sell
    private static boolean twoAccounts = true;          // true - два счета, можно играть в две стороны, false - только в одну сторону
    private static double rangePriceMAX = 4.0;          // диапазон в долларах от уровней для срабатывания ордера
    private static double rangePriceMIN = 0.0;          // диапазон в долларах от уровней для отмены ордера
    private static String typeOrder = "Limit";          // тип первого открываемого ордера
    private static int timeBetweenOrders = 10;          // время в секундах между выставлениями ордеров по одной стратегии
    private static BitmexClient bitmexClient;
    private static double priceActive = 3.0;    // цена тригер для стоп лимитов и тейк лимитов
    private static int strategyWorkOne = 2;     // количество стратегий одновременно работающих (можно еще допелить или убрать)
    private static double rangeLevel = 8.0;     // диапазон в долларах для появления уровней
    private static int dateDifference = -3;     // разница в часовом поясе
    private static boolean trading = false;     // торговать - true нет - false
    private static double visible = 0.0;        // видимость ордера в стакане -- 0.0 - не видно, 1.0 - видно
    private static double take = 15.0;          // тейк профит в долларах
    private static double stop = 30.0;          // стоп лосс в долларах
    private static double lot = 1.0;            // количество контрактов
    private static String path = "";
    private static int PORT = 4444;             // порт подключения

        // флаги
    private static volatile boolean ob_os_Flag = true;
    private static volatile boolean osFlag_5 = true;
    private static volatile boolean obFlag_5 = true;
    private static volatile boolean osFlag_4 = true;
    private static volatile boolean obFlag_4 = true;
    private static volatile boolean obFlag_3 = true;
    private static volatile boolean osFlag_3 = true;
    private static volatile boolean obFlag_2 = true;
    private static volatile boolean osFlag_2 = true;
    private static volatile boolean obFlag = true;
    private static volatile boolean osFlag = true;

        // включаем отключаем стратегии
    private static boolean ob_5 = true;
    private static boolean ob_4 = true;
    private static boolean ob_3 = true;
    private static boolean ob_2 = true;
    private static boolean ob = true;

            // Данные сколько раз какие стратегиисработают по стопам или по тейкам (статистика)
    private static int OB_5_TAKE = 0;
    private static int OB_5_STOP = 0;
    private static int OS_5_TAKE = 0;
    private static int OS_5_STOP = 0;
    private static int OB_4_TAKE = 0;
    private static int OB_4_STOP = 0;
    private static int OS_4_TAKE = 0;
    private static int OS_4_STOP = 0;
    private static int OB_3_TAKE = 0;
    private static int OB_3_STOP = 0;
    private static int OS_3_TAKE = 0;
    private static int OS_3_STOP = 0;
    private static int OB_2_TAKE = 0;
    private static int OB_2_STOP = 0;
    private static int OS_2_TAKE = 0;
    private static int OS_2_STOP = 0;
    private static int OB_STOP = 0;
    private static int OB_TAKE = 0;
    private static int OS_STOP = 0;
    private static int OS_TAKE = 0;


    private static Ticker ticker;


    public static BitmexChartData price(Ticker ticker) {
            List<BitmexChartData> list = bitmexClient.getChartData(ticker, 1, ONE_MINUTE);
            return bitmexChartData = list.get(0);
    }

    public static void setBitmexClient(BitmexClient bitmexClient) {
        Gasket.bitmexClient = bitmexClient;
    }

    public static BitmexClient getBitmexClient() {
        return bitmexClient;
    }

    public static BitmexClient getBitmexClient2Accounts() {
        return bitmexClient2Accounts;
    }

    public static void setBitmexClient2Accounts(BitmexClient bitmexClient2Accounts) {
        Gasket.bitmexClient2Accounts = bitmexClient2Accounts;
    }

    public static BitmexChartData getBitmexChartData() {
        return Gasket.bitmexChartData;
    }

    public static void setBitmexChartData(BitmexChartData bitmexChartData) {
        Gasket.bitmexChartData = bitmexChartData;
    }

    public static double getTake() {
        return Gasket.take;
    }

    public static void setTake(double take) {
        Gasket.take = take;
    }

    public static boolean isUseRealOrNotReal() {
        return useRealOrNotReal;
    }

    public static void setUseRealOrNotReal(boolean useRealOrNotReal) {
        Gasket.useRealOrNotReal = useRealOrNotReal;
    }

    public static double getStop() {
        return Gasket.stop;
    }

    public static void setStop(double stop) {
        Gasket.stop = stop;
    }

    public static double getPROFIT() {
        return Gasket.PROFIT;
    }

    public static void setPROFIT(double PROFIT) {
        Gasket.PROFIT = PROFIT;
    }

    public static Ticker getTicker() {
        return Gasket.ticker;
    }

    public static void setTicker(Ticker ticker) {
        Gasket.ticker = ticker;
    }

    public static double getRangePriceMAX() {
        return rangePriceMAX;
    }

    public static void setRangePriceMAX(double rangePriceMAX) {
        Gasket.rangePriceMAX = rangePriceMAX;
    }

    public static double getRangePriceMIN() {
        return rangePriceMIN;
    }

    public static void setRangePriceMIN(double rangePriceMIN) {
        Gasket.rangePriceMIN = rangePriceMIN;
    }

    public static double getPROFIT_Buy() {
        return PROFIT_Buy;
    }

    public static void setPROFIT_Buy(double PROFIT_Buy) {
        Gasket.PROFIT_Buy = PROFIT_Buy;
    }

    public static double getPROFIT_Sell() {
        return PROFIT_Sell;
    }

    public static void setPROFIT_Sell(double PROFIT_Sell) {
        Gasket.PROFIT_Sell = PROFIT_Sell;
    }

    public static int getStrategyWorkOne() {
        return strategyWorkOne;
    }

    public static void setStrategyWorkOne(int strategyWorkOne) {
        Gasket.strategyWorkOne = strategyWorkOne;
    }

    public static double getRangeLevel() {
        return rangeLevel;
    }

    public static void setRangeLevel(double rangeLevel) {
        Gasket.rangeLevel = rangeLevel;
    }

    public static int getDateDifference() {
        return dateDifference;
    }

    public static void setDateDifference(int dateDifference) {
        Gasket.dateDifference = dateDifference;
    }

    public static void setBitmexQuote(BitmexQuote bitmexQuote) {
        Gasket.bitmexQuote = bitmexQuote;
    }

    public static BitmexQuote getBitmexQuote() {
        return Gasket.bitmexQuote;
    }

    public static BitmexQuote getBitmex2Quote() {
        return bitmex2Quote;
    }

    public static void setBitmex2Quote(BitmexQuote bitmex2Quote) {
        Gasket.bitmex2Quote = bitmex2Quote;
    }

    public static boolean isOneSellFLAG() {
        return oneSellFLAG;
    }

    public static void setOneSellFLAG(boolean oneSellFLAG) {
        Gasket.oneSellFLAG = oneSellFLAG;
    }

    public static boolean isOneBuyFLAG() {
        return oneBuyFLAG;
    }

    public static void setOneBuyFLAG(boolean oneBuyFLAG) {
        Gasket.oneBuyFLAG = oneBuyFLAG;
    }

    public static boolean isTrading() {
        return trading;
    }

    public static void setTrading(boolean trading) {
        Gasket.trading = trading;
    }

    public static double getPriceActive() {
        return priceActive;
    }

    public static void setPriceActive(double priceActive) {
        Gasket.priceActive = priceActive;
    }

    public static double getVisible() {
        return visible;
    }

    public static void setVisible(double visible) {
        Gasket.visible = visible;
    }

    public static double getLot() {
        return lot;
    }

    public static void setLot(double lot) {
        Gasket.lot = lot;
    }

    public static String getTypeOrder() {
        return typeOrder;
    }

    public static void setTypeOrder(String typeOrder) {
        Gasket.typeOrder = typeOrder;
    }

    public static int getTimeBetweenOrders() {
        return timeBetweenOrders;
    }

    public static void setTimeBetweenOrders(int timeBetweenOrders) {
        Gasket.timeBetweenOrders = timeBetweenOrders;
    }



    public static int getOs3Stop() {
        return OS_3_STOP;
    }

    public static void setOs3Stop(int os3Stop) {
        OS_3_STOP = os3Stop;
    }

    public static int getOs3Take() {
        return OS_3_TAKE;
    }

    public static void setOs3Take(int os3Take) {
        OS_3_TAKE = os3Take;
    }

    public static int getOs4Stop() {
        return OS_4_STOP;
    }

    public static void setOs4Stop(int os4Stop) {
        OS_4_STOP = os4Stop;
    }

    public static int getOs4Take() {
        return OS_4_TAKE;
    }

    public static void setOs4Take(int os4Take) {
        OS_4_TAKE = os4Take;
    }

    public static int getOb3Stop() {
        return OB_3_STOP;
    }

    public static void setOb3Stop(int ob3Stop) {
        OB_3_STOP = ob3Stop;
    }

    public static int getOb3Take() {
        return OB_3_TAKE;
    }

    public static void setOb3Take(int ob3Take) {
        OB_3_TAKE = ob3Take;
    }

    public static int getOb4Stop() {
        return OB_4_STOP;
    }

    public static void setOb4Stop(int ob4Stop) {
        OB_4_STOP = ob4Stop;
    }

    public static int getOb4Take() {
        return OB_4_TAKE;
    }

    public static void setOb4Take(int ob4Take) {
        OB_4_TAKE = ob4Take;
    }

    public static int getOs2Stop() {
        return OS_2_STOP;
    }

    public static void setOs2Stop(int os2Stop) {
        OS_2_STOP = os2Stop;
    }

    public static int getOs2Take() {
        return OS_2_TAKE;
    }

    public static void setOs2Take(int os2Take) {
        OS_2_TAKE = os2Take;
    }

    public static int getOb2Stop() {
        return OB_2_STOP;
    }

    public static void setOb2Stop(int ob2Stop) {
        OB_2_STOP = ob2Stop;
    }

    public static int getOb2Take() {
        return OB_2_TAKE;
    }

    public static void setOb2Take(int ob2Take) {
        OB_2_TAKE = ob2Take;
    }

    public static int getObStop() {
        return OB_STOP;
    }

    public static void setObStop(int obStop) {
        OB_STOP = obStop;
    }

    public static int getObTake() {
        return OB_TAKE;
    }

    public static void setObTake(int obTake) {
        OB_TAKE = obTake;
    }

    public static int getOsStop() {
        return OS_STOP;
    }

    public static void setOsStop(int osStop) {
        OS_STOP = osStop;
    }

    public static int getOsTake() {
        return OS_TAKE;
    }

    public static void setOsTake(int osTake) {
        OS_TAKE = osTake;
    }



    public static boolean isGameDirection() {
        return gameDirection;
    }

    public static void setGameDirection(boolean gameDirection) {
        Gasket.gameDirection = gameDirection;
    }

    public static boolean isTwoAccounts() {
        return twoAccounts;
    }

    public static void setTwoAccounts(boolean twoAccounts) {
        Gasket.twoAccounts = twoAccounts;
    }

    public static boolean isGameAllDirection() {
        return gameAllDirection;
    }

    public static void setGameAllDirection(boolean gameAllDirection) {
        Gasket.gameAllDirection = gameAllDirection;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int PORT) {
        Gasket.PORT = PORT;
    }

    public static boolean isOb_3() {
        return ob_3;
    }

    public static void setOb_3(boolean ob_3) {
        Gasket.ob_3 = ob_3;
    }

    public static boolean isOb_4() {
        return ob_4;
    }

    public static void setOb_4(boolean ob_4) {
        Gasket.ob_4 = ob_4;
    }

    public static boolean isOb_2() {
        return ob_2;
    }

    public static void setOb_2(boolean ob_2) {
        Gasket.ob_2 = ob_2;
    }

    public static boolean isOb() {
        return ob;
    }

    public static void setOb(boolean ob) {
        Gasket.ob = ob;
    }

    public static boolean isUseStopLevelOrNotStop() {
        return useStopLevelOrNotStop;
    }

    public static void setUseStopLevelOrNotStop(boolean useStopLevelOrNotStop) {
        Gasket.useStopLevelOrNotStop = useStopLevelOrNotStop;
    }

    public static int getUseStopLevelOrNotStopTime() {
        return useStopLevelOrNotStopTime;
    }

    public static void setUseStopLevelOrNotStopTime(int useStopLevelOrNotStopTime) {
        Gasket.useStopLevelOrNotStopTime = useStopLevelOrNotStopTime;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        Gasket.path = path;
    }

    public static int getTimeCalculationLevel() {
        return timeCalculationLevel;
    }

    public static void setTimeCalculationLevel(int timeCalculationLevel) {
        Gasket.timeCalculationLevel = timeCalculationLevel;
    }

    public static int getTimeCalculationCombinationLevel() {
        return timeCalculationCombinationLevel;
    }

    public static void setTimeCalculationCombinationLevel(int timeCalculationCombinationLevel) {
        Gasket.timeCalculationCombinationLevel = timeCalculationCombinationLevel;
    }

    public static boolean isOb_os_Flag() {
        return ob_os_Flag;
    }

    public static void setOb_os_Flag(boolean ob_os_Flag) {
        Gasket.ob_os_Flag = ob_os_Flag;
    }

    public static boolean isOsFlag_4() {
        return osFlag_4;
    }

    public static void setOsFlag_4(boolean osFlag_4) {
        Gasket.osFlag_4 = osFlag_4;
    }

    public static boolean isObFlag_4() {
        return obFlag_4;
    }

    public static void setObFlag_4(boolean obFlag_4) {
        Gasket.obFlag_4 = obFlag_4;
    }

    public static boolean isObFlag_3() {
        return obFlag_3;
    }

    public static void setObFlag_3(boolean obFlag_3) {
        Gasket.obFlag_3 = obFlag_3;
    }

    public static boolean isOsFlag_3() {
        return osFlag_3;
    }

    public static void setOsFlag_3(boolean osFlag_3) {
        Gasket.osFlag_3 = osFlag_3;
    }

    public static boolean isObFlag_2() {
        return obFlag_2;
    }

    public static void setObFlag_2(boolean obFlag_2) {
        Gasket.obFlag_2 = obFlag_2;
    }

    public static boolean isOsFlag_2() {
        return osFlag_2;
    }

    public static void setOsFlag_2(boolean osFlag_2) {
        Gasket.osFlag_2 = osFlag_2;
    }

    public static boolean isObFlag() {
        return obFlag;
    }

    public static void setObFlag(boolean obFlag) {
        Gasket.obFlag = obFlag;
    }

    public static boolean isOsFlag() {
        return osFlag;
    }

    public static void setOsFlag(boolean osFlag) {
        Gasket.osFlag = osFlag;
    }

    public static boolean isOb_5() {
        return ob_5;
    }

    public static void setOb_5(boolean ob_5) {
        Gasket.ob_5 = ob_5;
    }

    public static boolean isOsFlag_5() {
        return osFlag_5;
    }

    public static void setOsFlag_5(boolean osFlag_5) {
        Gasket.osFlag_5 = osFlag_5;
    }

    public static boolean isObFlag_5() {
        return obFlag_5;
    }

    public static void setObFlag_5(boolean obFlag_5) {
        Gasket.obFlag_5 = obFlag_5;
    }

    public static int getOb5Take() {
        return OB_5_TAKE;
    }

    public static void setOb5Take(int ob5Take) {
        OB_5_TAKE = ob5Take;
    }

    public static int getOb5Stop() {
        return OB_5_STOP;
    }

    public static void setOb5Stop(int ob5Stop) {
        OB_5_STOP = ob5Stop;
    }

    public static int getOs5Take() {
        return OS_5_TAKE;
    }

    public static void setOs5Take(int os5Take) {
        OS_5_TAKE = os5Take;
    }

    public static int getOs5Stop() {
        return OS_5_STOP;
    }

    public static void setOs5Stop(int os5Stop) {
        OS_5_STOP = os5Stop;
    }

    public static int getNumberOfCandlesForAnalysis() {
        return numberOfCandlesForAnalysis;
    }

    public static void setNumberOfCandlesForAnalysis(int numberOfCandlesForAnalysis) {
        Gasket.numberOfCandlesForAnalysis = numberOfCandlesForAnalysis;
    }

    public static boolean isMaxAndMinAverage() {
        return maxAndMinAverage;
    }

    public static void setMaxAndMinAverage(boolean maxAndMinAverage) {
        Gasket.maxAndMinAverage = maxAndMinAverage;
    }
}
