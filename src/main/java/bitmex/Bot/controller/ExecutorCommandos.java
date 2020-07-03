package bitmex.Bot.controller;

import bitmex.Bot.model.Gasket;

import static bitmex.Bot.model.DatesTimes.getDateTerminal;
import static bitmex.Bot.view.ConsoleHelper.writeMessage;
import static bitmex.Bot.model.Gasket.*;



public class ExecutorCommandos {
    private ParserSetting parserSetting;


    public ExecutorCommandos() {
        setExecutorCommandos(this);
    }



    public void parseAndExecute(String string) {

        String[] strings = string.trim().split(" === ");

        if (strings.length < 1  || strings.length == 1) {
            if (string.equalsIgnoreCase("--- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ ---")) {
                return;
            }
            writeMessage(getDateTerminal()
                    + " --- Вы допустили ошибку, повторите ввод === " + string + "\n");
            return;
        } else {
            strings[1] = strings[1].replaceAll(",", ".");
        }

        try {
            switch (strings[0]) {
                case "activeNumberOfCandlesForAnalysis" :
                    setActiveNumberOfCandlesForAnalysis(strings[1].equalsIgnoreCase("true"));
                        writeMessage("activeNumberOfCandlesForAnalysis === "
                                + isActiveNumberOfCandlesForAnalysis() + "\n");
                    break;
                case "useStopLevelOrNotStop" :
                    setUseStopLevelOrNotStop(strings[1].equalsIgnoreCase("true"));
                        writeMessage("useStopLevelOrNotStop === "
                                + isUseStopLevelOrNotStop() + "\n");
                    break;
                case "gameAllDirection":
                    setGameAllDirection(strings[1].equalsIgnoreCase("true"));
                        writeMessage("gameAllDirection === "
                                + isGameAllDirection() + "\n");
                    break;
                case "useRealOrNotReal":
                    setUseRealOrNotReal(strings[1].equalsIgnoreCase("true"));
                        writeMessage("useRealOrNotReal === "
                                + isUseRealOrNotReal() + "\n");
                    break;
                case "showLoadPatternsUser" :
                    setShowLoadPatternsUser(strings[1].equalsIgnoreCase("true"));
                    writeMessage("showLoadPatternsUser === "
                            + isShowLoadPatternsUser() + "\n");
                    break;
                case "showLoadPatternsII" :
                    setShowLoadPatternsII(strings[1].equalsIgnoreCase("true"));
                    writeMessage("showLoadPatternsII === "
                            + isShowLoadPatternsII() + "\n");
                    break;
                case "showLoadPatternsIIPro" :
                    setShowLoadPatternsIIPro(strings[1].equalsIgnoreCase("true"));
                    writeMessage("showLoadPatternsIIPro === "
                            + isShowLoadPatternsIIPro() + "\n");
                    break;
                case "levelsToCompare" :
                    setLevelsToCompare(strings[1]);
                    writeMessage("levelsToCompare === "
                            + getLevelsToCompare() + "\n");
                    break;
                case "maxAndMinAverage" :
                    setMaxAndMinAverage(strings[1].equalsIgnoreCase("true"));
                        writeMessage("maxAndMinAverage === "
                                + isMaxAndMinAverage() + "\n");
                    break;
                case "tradingPatterns" :
                    Gasket.setTradingPatternsII(strings[1].equalsIgnoreCase("true"));
                        writeMessage("tradingPatterns === "
                                + Gasket.isTradingPatternsII() + "\n");
                    break;
                case "tradingPatternsUser" :
                    Gasket.setTradingPatternsUser(strings[1].equalsIgnoreCase("true"));
                        writeMessage("tradingPatterns === "
                                + Gasket.isTradingPatternsUser() + "\n");
                    break;
                case "savedPatternsIIPro" :
                    Gasket.setSavedPatternsIIPro(strings[1].equalsIgnoreCase("true"));
                    writeMessage("savedPatternsIIPro === "
                            + Gasket.isSavedPatternsIIPro() + "\n");
                    break;
                case "timeStopLiveForUserPatterns" :
                    Gasket.setTimeStopLiveForUserPatterns(Integer.parseInt(strings[1]));
                    writeMessage("timeStopLiveForUserPatterns === " +
                            + Gasket.getTimeStopLiveForUserPatterns());
                    break;
                case "timeCalculationCombinationLevel" :
                    Gasket.setTimeCalculationCombinationLevel(Integer.parseInt(strings[1]));
                        writeMessage("timeCalculationCombinationLevel === "
                                + Gasket.getTimeCalculationCombinationLevel() + "\n");
                    break;
                case "gameDirection":
                    Gasket.setGameDirection(strings[1].equalsIgnoreCase("true"));
                        writeMessage("gameDirection === "
                                + Gasket.isGameDirection() + "\n");
                    break;
                case "savedPatterns" :
                    Gasket.setSavedPatternsII(strings[1].equalsIgnoreCase("true"));
                        writeMessage("savedPatterns === "
                                + Gasket.isSavedPatternsII() + "\n");
                    break;
                case "tradingTestUser" :
                    Gasket.setTradingTestUser(strings[1].equalsIgnoreCase("true"));
                    writeMessage("tradingTestUser === "
                            + Gasket.isTradingTestUser());
                    break;
                case "tradingTestII" :
                    Gasket.setTradingTestII(strings[1].equalsIgnoreCase("true"));
                    writeMessage("tradingTestII === "
                            + Gasket.isTradingTestII());
                    break;
                case "tradingUser" :
                    Gasket.setTradingUser(strings[1].equalsIgnoreCase("true"));
                    writeMessage("tradingUser === "
                            + Gasket.isTradingUser());
                    break;
                case "tradingII" :
                    Gasket.setTradingII(strings[1].equalsIgnoreCase("true"));
                    writeMessage("tradingII === "
                            + Gasket.isTradingII());
                    break;
                case "secondsSleepTime" :
                    Gasket.setSecondsSleepTime(Integer.parseInt(strings[1]));
                        writeMessage("secondsSleepTime === "
                                + Gasket.getSecondsSleepTime() + "\n");
                    break;
                case "twoAccounts":
                    Gasket.setTwoAccounts(strings[1].equalsIgnoreCase("true"));
                        writeMessage("twoAccounts === "
                                + Gasket.isTwoAccounts() + "\n");
                    break;
                case "takeForCollectingPatterns" :
                    Gasket.setTakeForCollectingPatterns(Double.parseDouble(strings[1]));
                        writeMessage("takeForCollectingPatterns === "
                                + Gasket.getTakeForCollectingPatterns() + "\n");
                    break;
                case "numberOfCandlesForAnalysis" :
                    Gasket.setNumberOfCandlesForAnalysis(Integer.parseInt(strings[1]));
                        writeMessage("numberOfCandlesForAnalysis === "
                                + Gasket.getNumberOfCandlesForAnalysis() + "\n");
                    break;
                case "useStopLevelOrNotStopTime" :
                    Gasket.setUseStopLevelOrNotStopTime(Integer.parseInt(strings[1]));
                        writeMessage("useStopLevelOrNotStopTime === "
                                + Gasket.getUseStopLevelOrNotStopTime() + "\n");
                    break;
                case "trading":
                    Gasket.setTrading(strings[1].equalsIgnoreCase("true"));
                        writeMessage("trading === "
                                + Gasket.isTrading() + "\n");
                    break;
                case "obs_2":
                    Gasket.setObs_2(strings[1].equalsIgnoreCase("true"));
                        writeMessage("strategyOne === "
                                + Gasket.isObs_2() + "\n");
                    break;
                case "obs_4":
                    Gasket.setObs_4(strings[1].equalsIgnoreCase("true"));
                        writeMessage("strategyOneTime === "
                                + Gasket.isObs_4() + "\n");
                    break;
                case "obs_5":
                    Gasket.setObs_5(strings[1].equalsIgnoreCase("true"));
                        writeMessage("strategyOneTime === "
                                + Gasket.isObs_5() + "\n");
                    break;
                case "timeCalculationLevel" :
                    Gasket.setTimeCalculationLevel(Integer.parseInt(strings[1]));
                        writeMessage("timeCalculationLevel === "
                                + Gasket.getTimeCalculationLevel() + "\n");
                    break;
                case "timeBetweenOrders":
                    Gasket.setTimeBetweenOrders(Integer.parseInt(strings[1]));
                        writeMessage("timeBetweenOrders === "
                                + Gasket.getTimeBetweenOrders() + "\n");
                    break;
                case "obs_3":
                    Gasket.setObs_3(strings[1].equalsIgnoreCase("true"));
                        writeMessage("strategyOneRange === "
                                + Gasket.isObs_3() + "\n");
                    break;
                case "obs":
                    Gasket.setObs(strings[1].equalsIgnoreCase("true"));
                        writeMessage("one === "
                                + Gasket.isObs() + "\n");
                    break;
                case "oneBuyFLAG" :
                    Gasket.setOneBuyFLAG(strings[1].equalsIgnoreCase("true"));
                        writeMessage("one === "
                                + Gasket.isOneBuyFLAG() + "\n");
                    break;
                case "oneSellFLAG" :
                    Gasket.setOneSellFLAG(strings[1].equalsIgnoreCase("true"));
                        writeMessage("one === "
                                + Gasket.isOneSellFLAG() + "\n");
                    break;
                case "numberOfHistoryBlocks" :
                    Gasket.setNumberOfHistoryBlocks(strings[1]);
                        writeMessage("numberOfHistoryBlocks === "
                                + Gasket.getNumberOfHistoryBlocks() + "\n");
                    break;
                case "strategyWorkOne":
                    Gasket.setStrategyWorkOne(Integer.parseInt(strings[1]));
                        writeMessage("strategyWorkOne === "
                                + Gasket.getStrategyWorkOne() + "\n");
                    break;
                case "dateDifference":
                    Gasket.setDateDifference(Integer.parseInt(strings[1]));
                        writeMessage("dateDifference === "
                                + Gasket.getDateDifference() + "\n");
                    break;
                case "rangePriceMIN":
                    Gasket.setRangePriceMIN(Double.parseDouble(strings[1]));
                        writeMessage("rangePriceMIN === "
                                + Gasket.getRangePriceMIN() + "\n");
                    break;
                case "rangePriceMAX":
                    Gasket.setRangePriceMAX(Double.parseDouble(strings[1]));
                        writeMessage("rangePriceMAX === "
                                + Gasket.getRangePriceMAX() + "\n");
                    break;
                case "priceActive":
                    Gasket.setPriceActive(Double.parseDouble(strings[1]));
                        writeMessage("priceActive === "
                                + Gasket.getPriceActive() + "\n");
                    break;
                case "rangeLevel":
                    Gasket.setRangeLevel(Double.parseDouble(strings[1]));
                        writeMessage("rangeLevel === "
                                + Gasket.getRangeLevel() + "\n");
                    break;
                case "typeOrder":
                    Gasket.setTypeOrder(strings[1].trim());
                        writeMessage("typeOrder === "
                                + Gasket.getTypeOrder() + "\n");
                    break;
                case "visible":
                    Gasket.setVisible(Double.parseDouble(strings[1]));
                        writeMessage("visible === "
                                + Gasket.getVisible() + "\n");
                    break;
                case "take":
                    Gasket.setTake(Double.parseDouble(strings[1]));
                        writeMessage("take === "
                                + Gasket.getTake() + "\n");
                    break;
                case "stop":
                    Gasket.setStop(Double.parseDouble(strings[1]));
                        writeMessage("stop === "
                                + Gasket.getStop() + "\n");
                    break;
                case "PORT":
                    Gasket.setPORT(Integer.parseInt(strings[1]));
                        writeMessage("PORT === "
                                + Gasket.getPORT() + "\n");
                        Gasket.setServerRestart(false);
                    break;
                case "PROFIT" :
                    Gasket.setPROFIT(Double.parseDouble(strings[1]));
                        writeMessage("PROFIT === "
                                + Gasket.getPROFIT() + "\n");
                    break;
                case "PROFIT_Sell" :
                    Gasket.setPROFIT_Sell(Double.parseDouble(strings[1]));
                        writeMessage("PROFIT === "
                                + Gasket.getPROFIT_Sell() + "\n");
                    break;
                case "PROFIT_Buy" :
                    Gasket.setPROFIT_Buy(Double.parseDouble(strings[1]));
                        writeMessage("PROFIT === "
                                + Gasket.getPROFIT_Buy() + "\n");
                    break;
                case "lot":
                    Gasket.setLot(Double.parseDouble(strings[1]));
                        writeMessage("lot === "
                                + Gasket.getLot() + "\n");
                    break;
                case "apiKey" :
                    ApiKey.setApiKey(strings[1]);
                    writeMessage("apiKey === "
                            + ApiKey.getApiKey() + "\n");
                    break;
                case "apiKeyName" :
                    ApiKey.setApiKeyName(strings[1]);
                    writeMessage("apiKeyName === "
                            + ApiKey.getApiKeyName() + "\n");
                    break;
                case "apiKey2Accounts" :
                    ApiKey.setApiKey2Accounts(strings[1]);
                    writeMessage("apiKey === "
                            + ApiKey.getApiKey2Accounts() + "\n");
                    break;
                case "apiKeyName2Accounts" :
                    ApiKey.setApiKeyName2Accounts(strings[1]);
                    writeMessage("apiKeyName === "
                            + ApiKey.getApiKeyName2Accounts() + "\n");
                    break;
                case "SETTINGS" :
                    // SETTINGS=RESTART программа перезапустит настройки не отключаясь
                    if (strings[1].equalsIgnoreCase("RESTART")) parserSetting.writeSettings();
                    break;
                default:
                        writeMessage(getDateTerminal()
                                + " --- Вы ввели неверную команду, попробуйте еще раз === " + string + "\n");
            }
        } catch (Exception e) {
                writeMessage(getDateTerminal()
                        + " --- Ошибочка, повторите ввод === " + string + "\n");
        }
        parserSetting.writeSettings();
    }

    public void setParserSetting(ParserSetting parserSetting) {
        this.parserSetting = parserSetting;
    }
}
