package bitmex.Bot.model;

import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.strategies.II.SavedPatterns;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;
import bitmex.Bot.model.strategies.IIUser.SavedPatternsUser;

import java.util.List;

import static bitmex.Bot.model.bitMEX.enums.ChartDataBinSize.ONE_MINUTE;

public class Gasket {

            // флаги для разных режимов работы стратегий (можно дорабоать)
    private static boolean activeNumberOfCandlesForAnalysis = true; // включаем отклюаем отслеживания диапазона в котором находится цена true - включено
    private static int timeCalculationCombinationLevel = 20;        // когда уровни сформированы указываем время жизни данной комбинации
    private static SavedPatternsUser savedPatternsUserClass;
    private static FilesAndPathCreator filesAndPathCreator;
    private static double takeForCollectingPatterns = 20;           // тейк для сбора и накопления паттернов
    private static boolean useStopLevelOrNotStop = true;            // отменять или не отменять сделку вышедшею за MIN уровни
    private static int numberOfCandlesForAnalysis = 60;             // количество свечей для анализа диапазона где мы находимся и стоит ли делать сделку
    private static volatile boolean oneSellFLAG = true;
    private static boolean tradingPatternsUser = false;             // включить по патернам User
    private static volatile boolean oneBuyFLAG = true;
    private static int useStopLevelOrNotStopTime = 10;              // сколько минут отслеживать сделку вышедшею за MIN уровни
    private static BitmexClient bitmexClient2Accounts;
    private static volatile BitmexQuote bitmex2Quote;   // для получения данных по насущной котировке.
    private static SavedPatterns savedPatternsClass;
    private static boolean gameAllDirection = false;    // true - играть во все стороны на одном счету
    private static volatile BitmexQuote bitmexQuote;    // для получения данных по насущной котировке.
    private static BitmexChartData bitmexChartData;     // для получение данных по истории свечек
    private static boolean maxAndMinAverage = true;     // при подсчете границ канала считаем среднюю пиков если - true или просто берем пики если false
    private static boolean useRealOrNotReal = true;     // true - реальный счет
    private static boolean tradingPatterns = false;     // включить по патернам патернов
    private static int numberOfHistoryBlocks = 5;       // количество блоков истории выше которого обрезать историю
    private static int timeCalculationLevel = 50;       // время за которое должны сформироваться уровни иначе все отменяется
    private static volatile double PROFIT = 0.0;        // итоговый профит
    private static boolean savedPatterns = true;        // включить нахождение и запись патернов
    private static boolean gameDirection = true;        // направление игры при одном счете, true - Buy, false - Sell
    private static boolean twoAccounts = true;          // true - два счета, можно играть в две стороны, false - только в одну сторону
    private static double rangePriceMAX = 4.0;          // диапазон в долларах от уровней для срабатывания ордера
    private static double rangePriceMIN = 0.0;          // диапазон в долларах от уровней для отмены ордера
    private static String typeOrder = "Limit";          // тип первого открываемого ордера
    private static int timeBetweenOrders = 10;          // время в секундах между выставлениями ордеров по одной стратегии
    private static BitmexClient bitmexClient;
    private static int secondsSleepTime = 11;        // время в секундах, указывает сколько по времени отдохнуть по появлению новой пятиминутки
    private static double priceActive = 3.0;        // цена тригер для стоп лимитов и тейк лимитов
    private static int strategyWorkOne = 2;         // количество стратегий одновременно работающих (можно еще допелить или убрать)
    private static double rangeLevel = 8.0;         // диапазон в долларах для появления уровней
    private static int dateDifference = -3;         // разница в часовом поясе
    private static boolean trading = false;         // торговать - true нет - false
    private static double visible = 0.0;            // видимость ордера в стакане -- 0.0 - не видно, 1.0 - видно
    private static double take = 15.0;              // тейк профит в долларах
    private static double stop = 30.0;              // стоп лосс в долларах
    private static double lot = 1.0;                // количество контрактов
    private static String path = "";
    private static int PORT = 4444;                 // порт подключения
    private static Ticker ticker;


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
    private static boolean obs_5 = true;
    private static boolean obs_4 = true;
    private static boolean obs_3 = true;
    private static boolean obs_2 = true;
    private static boolean obs = true;


            // Данные сколько раз какие стратегиисработают по стопам или по тейкам (статистика)
    private static volatile double PROFIT_Sell = 0.0;   // профит по сделкам в селл
    private static volatile double PROFIT_Buy = 0.0;    // профит по сделкам в бай
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
    private static int OB_TAKE = 0;
    private static int OB_STOP = 0;
    private static int OS_TAKE = 0;
    private static int OS_STOP = 0;


            // Данные сколько раз какие стратегиисработают по стопам или по тейкам (статистика) для MiniRevers
    private static volatile double PROFIT_Sell_MR = 0.0;            // профит по сделкам в селл _MR -- MiniRevers
    private static volatile double PROFIT_Buy_MR = 0.0;             // профит по сделкам в бай _MR -- MiniRevers
    private static int OB_5_TAKE_MR = 0;
    private static int OB_5_STOP_MR = 0;
    private static int OS_5_TAKE_MR = 0;
    private static int OS_5_STOP_MR = 0;
    private static int OB_4_TAKE_MR = 0;
    private static int OB_4_STOP_MR = 0;
    private static int OS_4_TAKE_MR = 0;
    private static int OS_4_STOP_MR = 0;
    private static int OB_3_TAKE_MR = 0;
    private static int OB_3_STOP_MR = 0;
    private static int OS_3_TAKE_MR = 0;
    private static int OS_3_STOP_MR = 0;
    private static int OB_2_TAKE_MR = 0;
    private static int OB_2_STOP_MR = 0;
    private static int OS_2_TAKE_MR = 0;
    private static int OS_2_STOP_MR = 0;
    private static int OB_TAKE_MR = 0;
    private static int OB_STOP_MR = 0;
    private static int OS_TAKE_MR = 0;
    private static int OS_STOP_MR = 0;


            // Данные сколько раз какие стратегиисработают по стопам или по тейкам (статистика) для Revers
    private static volatile double PROFIT_Sell_R = 0.0;            // профит по сделкам в селл _MR -- MiniRevers
    private static volatile double PROFIT_Buy_R = 0.0;             // профит по сделкам в бай _MR -- MiniRevers
    private static int OB_5_TAKE_R = 0;
    private static int OB_5_STOP_R = 0;
    private static int OS_5_TAKE_R = 0;
    private static int OS_5_STOP_R = 0;
    private static int OB_4_TAKE_R = 0;
    private static int OB_4_STOP_R = 0;
    private static int OS_4_TAKE_R = 0;
    private static int OS_4_STOP_R = 0;
    private static int OB_3_TAKE_R = 0;
    private static int OB_3_STOP_R = 0;
    private static int OS_3_TAKE_R = 0;
    private static int OS_3_STOP_R = 0;
    private static int OB_2_TAKE_R = 0;
    private static int OB_2_STOP_R = 0;
    private static int OS_2_TAKE_R = 0;
    private static int OS_2_STOP_R = 0;
    private static int OB_TAKE_R = 0;
    private static int OB_STOP_R = 0;
    private static int OS_TAKE_R = 0;
    private static int OS_STOP_R = 0;


            // Данные сколько раз какие стратегиисработают по стопам или по тейкам (статистика) для ПАТТЕРН
    private static volatile double PROFIT_Sell_PAT = 0.0;            // профит по сделкам в селл ПАТТЕРН
    private static volatile double PROFIT_Buy_PAT = 0.0;             // профит по сделкам в бай ПАТТЕРН
    private static int OB_TAKE_PAT = 0;
    private static int OB_STOP_PAT = 0;
    private static int OS_TAKE_PAT = 0;
    private static int OS_STOP_PAT = 0;



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




    public static boolean isObs_3() {
        return obs_3;
    }

    public static void setObs_3(boolean obs_3) {
        Gasket.obs_3 = obs_3;
    }

    public static boolean isObs_4() {
        return obs_4;
    }

    public static void setObs_4(boolean obs_4) {
        Gasket.obs_4 = obs_4;
    }

    public static boolean isObs_2() {
        return obs_2;
    }

    public static void setObs_2(boolean obs_2) {
        Gasket.obs_2 = obs_2;
    }

    public static boolean isObs() {
        return obs;
    }

    public static void setObs(boolean obs) {
        Gasket.obs = obs;
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

    public static boolean isObs_5() {
        return obs_5;
    }

    public static void setObs_5(boolean obs_5) {
        Gasket.obs_5 = obs_5;
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

    public static boolean isActiveNumberOfCandlesForAnalysis() {
        return activeNumberOfCandlesForAnalysis;
    }

    public static void setActiveNumberOfCandlesForAnalysis(boolean activeNumberOfCandlesForAnalysis) {
        Gasket.activeNumberOfCandlesForAnalysis = activeNumberOfCandlesForAnalysis;
    }




    public static double getPROFIT_Sell_MR() {
        return PROFIT_Sell_MR;
    }

    public static void setPROFIT_Sell_MR(double PROFIT_Sell_MR) {
        Gasket.PROFIT_Sell_MR = PROFIT_Sell_MR;
    }

    public static double getPROFIT_Buy_MR() {
        return PROFIT_Buy_MR;
    }

    public static void setPROFIT_Buy_MR(double PROFIT_Buy_MR) {
        Gasket.PROFIT_Buy_MR = PROFIT_Buy_MR;
    }




    public static int getOb5TakeMr() {
        return OB_5_TAKE_MR;
    }

    public static void setOb5TakeMr(int ob5TakeMr) {
        OB_5_TAKE_MR = ob5TakeMr;
    }

    public static int getOb5StopMr() {
        return OB_5_STOP_MR;
    }

    public static void setOb5StopMr(int ob5StopMr) {
        OB_5_STOP_MR = ob5StopMr;
    }

    public static int getOs5TakeMr() {
        return OS_5_TAKE_MR;
    }

    public static void setOs5TakeMr(int os5TakeMr) {
        OS_5_TAKE_MR = os5TakeMr;
    }

    public static int getOs5StopMr() {
        return OS_5_STOP_MR;
    }

    public static void setOs5StopMr(int os5StopMr) {
        OS_5_STOP_MR = os5StopMr;
    }

    public static int getOb4TakeMr() {
        return OB_4_TAKE_MR;
    }

    public static void setOb4TakeMr(int ob4TakeMr) {
        OB_4_TAKE_MR = ob4TakeMr;
    }

    public static int getOb4StopMr() {
        return OB_4_STOP_MR;
    }

    public static void setOb4StopMr(int ob4StopMr) {
        OB_4_STOP_MR = ob4StopMr;
    }

    public static int getOs4TakeMr() {
        return OS_4_TAKE_MR;
    }

    public static void setOs4TakeMr(int os4TakeMr) {
        OS_4_TAKE_MR = os4TakeMr;
    }

    public static int getOs4StopMr() {
        return OS_4_STOP_MR;
    }

    public static void setOs4StopMr(int os4StopMr) {
        OS_4_STOP_MR = os4StopMr;
    }

    public static int getOb3TakeMr() {
        return OB_3_TAKE_MR;
    }

    public static void setOb3TakeMr(int ob3TakeMr) {
        OB_3_TAKE_MR = ob3TakeMr;
    }

    public static int getOb3StopMr() {
        return OB_3_STOP_MR;
    }

    public static void setOb3StopMr(int ob3StopMr) {
        OB_3_STOP_MR = ob3StopMr;
    }

    public static int getOs3TakeMr() {
        return OS_3_TAKE_MR;
    }

    public static void setOs3TakeMr(int os3TakeMr) {
        OS_3_TAKE_MR = os3TakeMr;
    }

    public static int getOs3StopMr() {
        return OS_3_STOP_MR;
    }

    public static void setOs3StopMr(int os3StopMr) {
        OS_3_STOP_MR = os3StopMr;
    }

    public static int getOb2TakeMr() {
        return OB_2_TAKE_MR;
    }

    public static void setOb2TakeMr(int ob2TakeMr) {
        OB_2_TAKE_MR = ob2TakeMr;
    }

    public static int getOb2StopMr() {
        return OB_2_STOP_MR;
    }

    public static void setOb2StopMr(int ob2StopMr) {
        OB_2_STOP_MR = ob2StopMr;
    }

    public static int getOs2TakeMr() {
        return OS_2_TAKE_MR;
    }

    public static void setOs2TakeMr(int os2TakeMr) {
        OS_2_TAKE_MR = os2TakeMr;
    }

    public static int getOs2StopMr() {
        return OS_2_STOP_MR;
    }

    public static void setOs2StopMr(int os2StopMr) {
        OS_2_STOP_MR = os2StopMr;
    }

    public static int getObStopMr() {
        return OB_STOP_MR;
    }

    public static void setObStopMr(int obStopMr) {
        OB_STOP_MR = obStopMr;
    }

    public static int getObTakeMr() {
        return OB_TAKE_MR;
    }

    public static void setObTakeMr(int obTakeMr) {
        OB_TAKE_MR = obTakeMr;
    }

    public static int getOsStopMr() {
        return OS_STOP_MR;
    }

    public static void setOsStopMr(int osStopMr) {
        OS_STOP_MR = osStopMr;
    }

    public static int getOsTakeMr() {
        return OS_TAKE_MR;
    }

    public static void setOsTakeMr(int osTakeMr) {
        OS_TAKE_MR = osTakeMr;
    }




    public static double getPROFIT_Sell_R() {
        return PROFIT_Sell_R;
    }

    public static void setPROFIT_Sell_R(double PROFIT_Sell_R) {
        Gasket.PROFIT_Sell_R = PROFIT_Sell_R;
    }

    public static double getPROFIT_Buy_R() {
        return PROFIT_Buy_R;
    }

    public static void setPROFIT_Buy_R(double PROFIT_Buy_R) {
        Gasket.PROFIT_Buy_R = PROFIT_Buy_R;
    }




    public static int getOb5TakeR() {
        return OB_5_TAKE_R;
    }

    public static void setOb5TakeR(int ob5TakeR) {
        OB_5_TAKE_R = ob5TakeR;
    }

    public static int getOb5StopR() {
        return OB_5_STOP_R;
    }

    public static void setOb5StopR(int ob5StopR) {
        OB_5_STOP_R = ob5StopR;
    }

    public static int getOs5TakeR() {
        return OS_5_TAKE_R;
    }

    public static void setOs5TakeR(int os5TakeR) {
        OS_5_TAKE_R = os5TakeR;
    }

    public static int getOs5StopR() {
        return OS_5_STOP_R;
    }

    public static void setOs5StopR(int os5StopR) {
        OS_5_STOP_R = os5StopR;
    }

    public static int getOb4TakeR() {
        return OB_4_TAKE_R;
    }

    public static void setOb4TakeR(int ob4TakeR) {
        OB_4_TAKE_R = ob4TakeR;
    }

    public static int getOb4StopR() {
        return OB_4_STOP_R;
    }

    public static void setOb4StopR(int ob4StopR) {
        OB_4_STOP_R = ob4StopR;
    }

    public static int getOs4TakeR() {
        return OS_4_TAKE_R;
    }

    public static void setOs4TakeR(int os4TakeR) {
        OS_4_TAKE_R = os4TakeR;
    }

    public static int getOs4StopR() {
        return OS_4_STOP_R;
    }

    public static void setOs4StopR(int os4StopR) {
        OS_4_STOP_R = os4StopR;
    }

    public static int getOb3TakeR() {
        return OB_3_TAKE_R;
    }

    public static void setOb3TakeR(int ob3TakeR) {
        OB_3_TAKE_R = ob3TakeR;
    }

    public static int getOb3StopR() {
        return OB_3_STOP_R;
    }

    public static void setOb3StopR(int ob3StopR) {
        OB_3_STOP_R = ob3StopR;
    }

    public static int getOs3TakeR() {
        return OS_3_TAKE_R;
    }

    public static void setOs3TakeR(int os3TakeR) {
        OS_3_TAKE_R = os3TakeR;
    }

    public static int getOs3StopR() {
        return OS_3_STOP_R;
    }

    public static void setOs3StopR(int os3StopR) {
        OS_3_STOP_R = os3StopR;
    }

    public static int getOb2TakeR() {
        return OB_2_TAKE_R;
    }

    public static void setOb2TakeR(int ob2TakeR) {
        OB_2_TAKE_R = ob2TakeR;
    }

    public static int getOb2StopR() {
        return OB_2_STOP_R;
    }

    public static void setOb2StopR(int ob2StopR) {
        OB_2_STOP_R = ob2StopR;
    }

    public static int getOs2TakeR() {
        return OS_2_TAKE_R;
    }

    public static void setOs2TakeR(int os2TakeR) {
        OS_2_TAKE_R = os2TakeR;
    }

    public static int getOs2StopR() {
        return OS_2_STOP_R;
    }

    public static void setOs2StopR(int os2StopR) {
        OS_2_STOP_R = os2StopR;
    }

    public static int getObStopR() {
        return OB_STOP_R;
    }

    public static void setObStopR(int obStopR) {
        OB_STOP_R = obStopR;
    }

    public static int getObTakeR() {
        return OB_TAKE_R;
    }

    public static void setObTakeR(int obTakeR) {
        OB_TAKE_R = obTakeR;
    }

    public static int getOsStopR() {
        return OS_STOP_R;
    }

    public static void setOsStopR(int osStopR) {
        OS_STOP_R = osStopR;
    }

    public static int getOsTakeR() {
        return OS_TAKE_R;
    }

    public static void setOsTakeR(int osTakeR) {
        OS_TAKE_R = osTakeR;
    }



    public static FilesAndPathCreator getFilesAndPathCreator() {
        return filesAndPathCreator;
    }

    public static void setFilesAndPathCreator(FilesAndPathCreator filesAndPathCreator) {
        Gasket.filesAndPathCreator = filesAndPathCreator;
    }



    public static boolean isTradingPatterns() {
        return tradingPatterns;
    }

    public static void setTradingPatterns(boolean tradingPatterns) {
        Gasket.tradingPatterns = tradingPatterns;
    }

    public static boolean isSavedPatterns() {
        return savedPatterns;
    }

    public static void setSavedPatterns(boolean savedPatterns) {
        Gasket.savedPatterns = savedPatterns;
    }



    public static double getPROFIT_Sell_PAT() {
        return PROFIT_Sell_PAT;
    }

    public static void setPROFIT_Sell_PAT(double PROFIT_Sell_PAT) {
        Gasket.PROFIT_Sell_PAT = PROFIT_Sell_PAT;
    }

    public static double getPROFIT_Buy_PAT() {
        return PROFIT_Buy_PAT;
    }

    public static void setPROFIT_Buy_PAT(double PROFIT_Buy_PAT) {
        Gasket.PROFIT_Buy_PAT = PROFIT_Buy_PAT;
    }

    public static int getObTakePat() {
        return OB_TAKE_PAT;
    }

    public static void setObTakePat(int obTakePat) {
        OB_TAKE_PAT = obTakePat;
    }

    public static int getObStopPat() {
        return OB_STOP_PAT;
    }

    public static void setObStopPat(int obStopPat) {
        OB_STOP_PAT = obStopPat;
    }

    public static int getOsTakePat() {
        return OS_TAKE_PAT;
    }

    public static void setOsTakePat(int osTakePat) {
        OS_TAKE_PAT = osTakePat;
    }

    public static int getOsStopPat() {
        return OS_STOP_PAT;
    }

    public static void setOsStopPat(int osStopPat) {
        OS_STOP_PAT = osStopPat;
    }


    
    public static SavedPatterns getSavedPatternsClass() {
        return savedPatternsClass;
    }

    public static void setSavedPatternsClass(SavedPatterns savedPatternsClass) {
        Gasket.savedPatternsClass = savedPatternsClass;
    }



    public static double getTakeForCollectingPatterns() {
        return takeForCollectingPatterns;
    }

    public static void setTakeForCollectingPatterns(double takeForCollectingPatterns) {
        Gasket.takeForCollectingPatterns = takeForCollectingPatterns;
    }



    public static SavedPatternsUser getSavedPatternsUserClass() {
        return savedPatternsUserClass;
    }

    public static void setSavedPatternsUserClass(SavedPatternsUser savedPatternsUserClass) {
        Gasket.savedPatternsUserClass = savedPatternsUserClass;
    }



    public static int getSecondsSleepTime() {
        return secondsSleepTime;
    }

    public static void setSecondsSleepTime(int secondsSleepTime) {
        Gasket.secondsSleepTime = secondsSleepTime;
    }



    public static boolean isTradingPatternsUser() {
        return tradingPatternsUser;
    }

    public static void setTradingPatternsUser(boolean tradingPatternsUser) {
        Gasket.tradingPatternsUser = tradingPatternsUser;
    }



    public static int getNumberOfHistoryBlocks() {
        return numberOfHistoryBlocks;
    }

    public static void setNumberOfHistoryBlocks(int numberOfHistoryBlocks) {
        Gasket.numberOfHistoryBlocks = numberOfHistoryBlocks;
    }
}
