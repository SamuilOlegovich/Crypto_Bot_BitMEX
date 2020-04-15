package bitmex.Bot.model;

import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.client.BitmexClient;
import bitmex.Bot.model.bitMEX.entity.BitmexQuote;

import java.util.List;

import static bitmex.Bot.model.bitMEX.enums.ChartDataBinSize.ONE_MINUTE;

public class Gasket {

            // флаги для разных режимов работы стратегий (можно дорабоать)
    private static volatile boolean strategyOneSellRangeFLAG = true;
    private static volatile boolean strategyOneBuyRangeFLAG = true;
    private static volatile boolean strategyOneSellFLAG = true;
    private static volatile boolean strategyOneBuyFLAG = true;
    private static volatile boolean strategyOneAllFLAG = true;
    private static volatile boolean oneSellFLAG = true;
    private static volatile boolean oneBuyFLAG = true;
    private static BitmexClient bitmexClient2Accounts;
    private static volatile BitmexQuote bitmex2Quote;   // для получения данных по насущной котировке.
    private static volatile double PROFIT_Sell = 0.0;   // профит по сделкам в селл
    private static volatile double PROFIT_Buy = 0.0;    // профит по сделкам в бай
    private static boolean gameAllDirection = false;    // true - играть во все стороны на одном счету
    private static volatile BitmexQuote bitmexQuote;    // для получения данных по насущной котировке.
    private static BitmexChartData bitmexChartData;     // для получение данных по истории свечек
    private static volatile double PROFIT = 0.0;        // итоговый профит
    private static boolean gameDirection = true;        // направление игры при одном счете, true - Buy, false - Sell
    private static boolean twoAccounts = true;          // true - два счета, можно играть в две стороны, false - только в одну сторону
    private static double rangePriceMAX = 4.0;          // диапазон в долларах от уровней для срабатывания ордера
    private static double rangePriceMIN = 0.0;          // диапазон в долларах от уровней для отмены ордера
    private static String typeOrder = "Limit";          // тип первого открываемого ордера
    private static int timeBetweenOrders = 10;          // время в секундах между выставлениями ордеров по одной стратегии
    private static BitmexClient bitmexClient;
    private static int strategeWorkOne = 2;     // количество стратегий одновременно работающих (можно еще допелить или убрать)
    private static double rangeLivel = 8.0;     // диапазон в долларах для появления уровней
    private static int dateDifference = -3;     // разница в часовом поясе
    private static boolean trading = false;     // торговать - true нет - false
    private static double priceActiv = 3.0;     // цена тригер для стоп лимитов и тейк лимитов
    private static double visible = 0.0;        // видимость ордера в стакане -- 0.0 - не видно, 1.0 - видно
    private static double take = 15.0;          // тейк профит в долларах
    private static double stop = 30.0;          // стоп лосс в долларах
    private static double lot = 1.0;            // количество контрактов
    private static int PORT = 4444;             // порт подключения

            // Данные сколько раз какие стратегиисработают по стопам или по тейкам (статистика)
    private static int SOS_R_STOP = 0;
    private static int SOS_R_TAKE = 0;
    private static int SOS_T_STOP = 0;
    private static int SOS_T_TAKE = 0;
    private static int SOB_R_STOP = 0;
    private static int SOB_R_TAKE = 0;
    private static int SOB_T_STOP = 0;
    private static int SOB_T_TAKE = 0;
    private static int SOS_STOP = 0;
    private static int SOS_TAKE = 0;
    private static int SOB_STOP = 0;
    private static int SOB_TAKE = 0;
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

    public static synchronized boolean isStrategyOneSellFLAG() {
        return strategyOneSellFLAG;
    }

    public static synchronized void setStrategyOneSellFLAG(boolean strategyOneSellFLAG) {
        Gasket.strategyOneSellFLAG = strategyOneSellFLAG;
    }

    public static synchronized boolean isStrategyOneBuyFLAG() {
        return Gasket.strategyOneBuyFLAG;
    }

    public static synchronized void setStrategyOneBuyFLAG(boolean strategyOneBuyFLAG) {
        Gasket.strategyOneBuyFLAG = strategyOneBuyFLAG;
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

    public static double getStop() {
        return Gasket.stop;
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

    public static boolean isStrategyOneAllFLAG() {
        return Gasket.strategyOneAllFLAG;
    }

    public static void setStrategyOneAllFLAG(boolean strategyOneAllFLAG) {
        Gasket.strategyOneAllFLAG = strategyOneAllFLAG;
    }

    public static void setTicker(Ticker ticker) {
        Gasket.ticker = ticker;
    }

    public static double getRangePriceMAX() {
        return rangePriceMAX;
    }
    public static double getRangePriceMIN() {
        return rangePriceMIN;
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

    public static int getStrategeWorkOne() {
        return strategeWorkOne;
    }

    public static double getRangeLivel() {
        return rangeLivel;
    }

    public static boolean isStrategyOneBuyRangeFLAG() {
        return strategyOneBuyRangeFLAG;
    }

    public static void setStrategyOneBuyRangeFLAG(boolean strategyOneBuyRangeFLAG) {
        Gasket.strategyOneBuyRangeFLAG = strategyOneBuyRangeFLAG;
    }

    public static boolean isStrategyOneSellRangeFLAG() {
        return strategyOneSellRangeFLAG;
    }

    public static void setStrategyOneSellRangeFLAG(boolean strategyOneSellRangeFLAG) {
        Gasket.strategyOneSellRangeFLAG = strategyOneSellRangeFLAG;
    }

    public static int getDateDifference() {
        return dateDifference;
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

    public static double getPriceActiv() {
        return priceActiv;
    }

    public static void setPriceActiv(double priceActiv) {
        Gasket.priceActiv = priceActiv;
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


    public static int getSosRStop() {
        return SOS_R_STOP;
    }

    public static void setSosRStop(int sosRStop) {
        SOS_R_STOP = sosRStop;
    }

    public static int getSosRTake() {
        return SOS_R_TAKE;
    }

    public static void setSosRTake(int sosRTake) {
        SOS_R_TAKE = sosRTake;
    }

    public static int getSosTStop() {
        return SOS_T_STOP;
    }

    public static void setSosTStop(int sosTStop) {
        SOS_T_STOP = sosTStop;
    }

    public static int getSosTTake() {
        return SOS_T_TAKE;
    }

    public static void setSosTTake(int sosTTake) {
        SOS_T_TAKE = sosTTake;
    }

    public static int getSobRStop() {
        return SOB_R_STOP;
    }

    public static void setSobRStop(int sobRStop) {
        SOB_R_STOP = sobRStop;
    }

    public static int getSobRTake() {
        return SOB_R_TAKE;
    }

    public static void setSobRTake(int sobRTake) {
        SOB_R_TAKE = sobRTake;
    }

    public static int getSobTStop() {
        return SOB_T_STOP;
    }

    public static void setSobTStop(int sobTStop) {
        SOB_T_STOP = sobTStop;
    }

    public static int getSobTTake() {
        return SOB_T_TAKE;
    }

    public static void setSobTTake(int sobTTake) {
        SOB_T_TAKE = sobTTake;
    }

    public static int getSosStop() {
        return SOS_STOP;
    }

    public static void setSosStop(int sosStop) {
        SOS_STOP = sosStop;
    }

    public static int getSosTake() {
        return SOS_TAKE;
    }

    public static void setSosTake(int sosTake) {
        SOS_TAKE = sosTake;
    }

    public static int getSobStop() {
        return SOB_STOP;
    }

    public static void setSobStop(int sobStop) {
        SOB_STOP = sobStop;
    }

    public static int getSobTake() {
        return SOB_TAKE;
    }

    public static void setSobTake(int sobTake) {
        SOB_TAKE = sobTake;
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
}
