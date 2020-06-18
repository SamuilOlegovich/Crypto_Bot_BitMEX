package bitmex.Bot.controller;

import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;


public class ExecutorCommandos {
    private ParserSetting parserSetting;


    public ExecutorCommandos() {
        Gasket.setExecutorCommandos(this);
    }



    public void parseAndExecute(String string) {

        String[] strings = string.trim().split(" === ");

        if (strings.length < 1  || strings.length == 1) {
            if (string.equalsIgnoreCase("--- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ ---")) {
                return;
            }
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Вы допустили ошибку, повторите ввод === " + string + "\n");
            return;
        } else {
            strings[1] = strings[1].replaceAll(",", ".");
        }

        try {
            switch (strings[0]) {
                case "activeNumberOfCandlesForAnalysis" :
                    Gasket.setActiveNumberOfCandlesForAnalysis(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("activeNumberOfCandlesForAnalysis === "
                                + Gasket.isActiveNumberOfCandlesForAnalysis() + "\n");
                    break;
                case "useStopLevelOrNotStop" :
                    Gasket.setUseStopLevelOrNotStop(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("useStopLevelOrNotStop === "
                                + Gasket.isUseStopLevelOrNotStop() + "\n");
                    break;
                case "gameAllDirection":
                    Gasket.setGameAllDirection(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("gameAllDirection === "
                                + Gasket.isGameAllDirection() + "\n");
                    break;
                case "useRealOrNotReal":
                    Gasket.setUseRealOrNotReal(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("useRealOrNotReal === "
                                + Gasket.isUseRealOrNotReal() + "\n");
                    break;
                case "maxAndMinAverage" :
                    Gasket.setMaxAndMinAverage(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("maxAndMinAverage === "
                                + Gasket.isMaxAndMinAverage() + "\n");
                    break;
                case "tradingPatterns" :
                    Gasket.setTradingPatterns(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("tradingPatterns === "
                                + Gasket.isTradingPatterns() + "\n");
                    break;
                case "tradingPatternsUser" :
                    Gasket.setTradingPatternsUser(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("tradingPatterns === "
                                + Gasket.isTradingPatternsUser() + "\n");
                    break;
                case "timeStopLiveForUserPatterns" :
                    Gasket.setTimeStopLiveForUserPatterns(Integer.parseInt(strings[1]));
                    ConsoleHelper.writeMessage("timeStopLiveForUserPatterns === " +
                            + Gasket.getTimeStopLiveForUserPatterns());
                case "timeCalculationCombinationLevel" :
                    Gasket.setTimeCalculationCombinationLevel(Integer.parseInt(strings[1]));
                        ConsoleHelper.writeMessage("timeCalculationCombinationLevel === "
                                + Gasket.getTimeCalculationCombinationLevel() + "\n");
                    break;
                case "gameDirection":
                    Gasket.setGameDirection(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("gameDirection === "
                                + Gasket.isGameDirection() + "\n");
                    break;
                case "savedPatterns" :
                    Gasket.setSavedPatterns(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("savedPatterns === "
                                + Gasket.getLot() + "\n");
                    break;
                case "tradingTestUser" :
                    Gasket.setTradingTestUser(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("tradingTestUser === "
                            + Gasket.isTradingTestUser());
                    break;
                case "tradingTestII" :
                    Gasket.setTradingTestII(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("tradingTestII === "
                            + Gasket.isTradingTestII());
                    break;
                case "tradingUser" :
                    Gasket.setTradingUser(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("tradingUser === "
                            + Gasket.isTradingUser());
                    break;
                case "tradingII" :
                    Gasket.setTradingII(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("tradingII === "
                            + Gasket.isTradingII());
                    break;
                case "secondsSleepTime" :
                    Gasket.setSecondsSleepTime(Integer.parseInt(strings[1]));
                        ConsoleHelper.writeMessage("secondsSleepTime === "
                                + Gasket.getSecondsSleepTime() + "\n");
                    break;
                case "twoAccounts":
                    Gasket.setTwoAccounts(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("twoAccounts === "
                                + Gasket.isTwoAccounts() + "\n");
                    break;
                case "takeForCollectingPatterns" :
                    Gasket.setTakeForCollectingPatterns(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("takeForCollectingPatterns === "
                                + Gasket.getTakeForCollectingPatterns() + "\n");
                    break;
                case "numberOfCandlesForAnalysis" :
                    Gasket.setNumberOfCandlesForAnalysis(Integer.parseInt(strings[1]));
                        ConsoleHelper.writeMessage("numberOfCandlesForAnalysis === "
                                + Gasket.getNumberOfCandlesForAnalysis() + "\n");
                    break;
                case "useStopLevelOrNotStopTime" :
                    Gasket.setUseStopLevelOrNotStopTime(Integer.parseInt(strings[1]));
                        ConsoleHelper.writeMessage("useStopLevelOrNotStopTime === "
                                + Gasket.getUseStopLevelOrNotStopTime() + "\n");
                    break;
                case "trading":
                    Gasket.setTrading(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("trading === "
                                + Gasket.isTrading() + "\n");
                    break;
                case "obs_2":
                    Gasket.setObs_2(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("strategyOne === "
                                + Gasket.isObs_2() + "\n");
                    break;
                case "obs_4":
                    Gasket.setObs_4(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("strategyOneTime === "
                                + Gasket.isObs_4() + "\n");
                    break;
                case "obs_5":
                    Gasket.setObs_5(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("strategyOneTime === "
                                + Gasket.isObs_5() + "\n");
                    break;
                case "timeCalculationLevel" :
                    Gasket.setTimeCalculationLevel(Integer.parseInt(strings[1]));
                        ConsoleHelper.writeMessage("timeCalculationLevel === "
                                + Gasket.getTimeCalculationLevel() + "\n");
                    break;
                case "timeBetweenOrders":
                    Gasket.setTimeBetweenOrders(Integer.parseInt(strings[1]));
                        ConsoleHelper.writeMessage("timeBetweenOrders === "
                                + Gasket.getTimeBetweenOrders() + "\n");
                    break;
                case "obs_3":
                    Gasket.setObs_3(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("strategyOneRange === "
                                + Gasket.isObs_3() + "\n");
                    break;
                case "obs":
                    Gasket.setObs(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("one === "
                                + Gasket.isObs() + "\n");
                    break;
                case "oneBuyFLAG" :
                    Gasket.setOneBuyFLAG(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("one === "
                                + Gasket.isOneBuyFLAG() + "\n");
                    break;
                case "oneSellFLAG" :
                    Gasket.setOneSellFLAG(strings[1].equalsIgnoreCase("true"));
                        ConsoleHelper.writeMessage("one === "
                                + Gasket.isOneSellFLAG() + "\n");
                    break;
                case "numberOfHistoryBlocks" :
                    Gasket.setNumberOfHistoryBlocks(strings[1]);
                        ConsoleHelper.writeMessage("numberOfHistoryBlocks === "
                                + Gasket.getNumberOfHistoryBlocks() + "\n");
                    break;
                case "strategyWorkOne":
                    Gasket.setStrategyWorkOne(Integer.parseInt(strings[1]));
                        ConsoleHelper.writeMessage("strategyWorkOne === "
                                + Gasket.getStrategyWorkOne() + "\n");
                    break;
                case "dateDifference":
                    Gasket.setDateDifference(Integer.parseInt(strings[1]));
                        ConsoleHelper.writeMessage("dateDifference === "
                                + Gasket.getDateDifference() + "\n");
                    break;
                case "rangePriceMIN":
                    Gasket.setRangePriceMIN(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("rangePriceMIN === "
                                + Gasket.getRangePriceMIN() + "\n");
                    break;
                case "rangePriceMAX":
                    Gasket.setRangePriceMAX(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("rangePriceMAX === "
                                + Gasket.getRangePriceMAX() + "\n");
                    break;
                case "priceActive":
                    Gasket.setPriceActive(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("priceActive === "
                                + Gasket.getPriceActive() + "\n");
                    break;
                case "rangeLevel":
                    Gasket.setRangeLevel(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("rangeLevel === "
                                + Gasket.getRangeLevel() + "\n");
                    break;
                case "typeOrder":
                    Gasket.setTypeOrder(strings[1].trim());
                        ConsoleHelper.writeMessage("typeOrder === "
                                + Gasket.getTypeOrder() + "\n");
                    break;
                case "visible":
                    Gasket.setVisible(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("visible === "
                                + Gasket.getVisible() + "\n");
                    break;
                case "take":
                    Gasket.setTake(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("take === "
                                + Gasket.getTake() + "\n");
                    break;
                case "stop":
                    Gasket.setStop(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("stop === "
                                + Gasket.getStop() + "\n");
                    break;
                case "PORT":
                    Gasket.setPORT(Integer.parseInt(strings[1]));
                        ConsoleHelper.writeMessage("PORT === "
                                + Gasket.getPORT() + "\n");
                    break;
                case "PROFIT" :
                    Gasket.setPROFIT(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("PROFIT === "
                                + Gasket.getPROFIT() + "\n");
                    break;
                case "PROFIT_Sell" :
                    Gasket.setPROFIT_Sell(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("PROFIT === "
                                + Gasket.getPROFIT_Sell() + "\n");
                    break;
                case "PROFIT_Buy" :
                    Gasket.setPROFIT_Buy(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("PROFIT === "
                                + Gasket.getPROFIT_Buy() + "\n");
                    break;
                case "lot":
                    Gasket.setLot(Double.parseDouble(strings[1]));
                        ConsoleHelper.writeMessage("lot === "
                                + Gasket.getLot() + "\n");
                    break;
                case "apiKey" :
                    ApiKey.setApiKey(strings[1]);
                    ConsoleHelper.writeMessage("apiKey === "
                            + ApiKey.getApiKey() + "\n");
                    break;
                case "apiKeyName" :
                    ApiKey.setApiKeyName(strings[1]);
                    ConsoleHelper.writeMessage("apiKeyName === "
                            + ApiKey.getApiKeyName() + "\n");
                case "apiKey2Accounts" :
                    ApiKey.setApiKey2Accounts(strings[1]);
                    ConsoleHelper.writeMessage("apiKey === "
                            + ApiKey.getApiKey2Accounts() + "\n");
                    break;
                case "apiKeyName2Accounts" :
                    ApiKey.setApiKeyName2Accounts(strings[1]);
                    ConsoleHelper.writeMessage("apiKeyName === "
                            + ApiKey.getApiKeyName2Accounts() + "\n");
                case "SETTINGS" :
                    // SETTINGS=RESTART программа перезапустит настройки не отключаясь
                    if (strings[1].equalsIgnoreCase("RESTART")) parserSetting.writeSettings();
                    break;
                default:
                        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                                + " --- Вы ввели неверную команду, попробуйте еще раз === " + string + "\n");
            }
        } catch (Exception e) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                        + " --- Ошибочка, повторите ввод === " + string + "\n");
        }
        parserSetting.writeSettings();
    }

//    public boolean isFlag() {
//        return flag;
//    }

//    public void setFlag(boolean flag) {
//        this.flag = flag;
//    }

    public void setParserSetting(ParserSetting parserSetting) {
        this.parserSetting = parserSetting;
    }
}
