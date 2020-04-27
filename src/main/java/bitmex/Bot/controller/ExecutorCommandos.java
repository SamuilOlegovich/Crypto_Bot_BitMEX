package bitmex.Bot.controller;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

public class ExecutorCommandos {
    private ParserSetting parserSetting;
    private boolean flag;

    public ExecutorCommandos() {
    }

    public synchronized void parserAndExecutor(String string) {
        String[] strings = string.trim().split("=");

        if (strings.length < 1  || strings.length == 1) {
            ConsoleHelper.writeMessage("\nВы допустили ошибку, повторите ввод.\n");
            return;
        } else {
            strings[1] = strings[1].replaceAll(",", ".");
        }

        try {
            switch (strings[0]) {
                case "timeCalculationCombinationLevel" :
                    Gasket.setTimeCalculationCombinationLevel(Integer.parseInt(strings[1]));
                    ConsoleHelper.writeMessage("timeCalculationCombinationLevel === " + Gasket.getTimeCalculationCombinationLevel() + "\n");
                    break;
                case "useStopLevelOrNotStopTime" :
                    Gasket.setUseStopLevelOrNotStopTime(Integer.parseInt(strings[1]));
                    ConsoleHelper.writeMessage("useStopLevelOrNotStopTime === " + Gasket.getUseStopLevelOrNotStopTime() + "\n");
                    break;
                case "useStopLevelOrNotStop" :
                    Gasket.setUseStopLevelOrNotStop(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("useStopLevelOrNotStop === " + Gasket.isUseStopLevelOrNotStop() + "\n");
                    break;
                case "timeCalculationLevel" :
                    Gasket.setTimeCalculationLevel(Integer.parseInt(strings[1]));
                    ConsoleHelper.writeMessage("timeCalculationLevel === " + Gasket.getTimeCalculationLevel() + "\n");
                    break;
                case "timeBetweenOrders":
                    Gasket.setTimeBetweenOrders(Integer.parseInt(strings[1]));
                    ConsoleHelper.writeMessage("timeBetweenOrders === " + Gasket.getTimeBetweenOrders() + "\n");
                    break;
                case "gameAllDirection":
                    Gasket.setGameAllDirection(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("gameAllDirection === " + Gasket.isGameAllDirection() + "\n");
                    break;
                case "strategyOneRange":
                    Gasket.setOb_3(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("strategyOneRange === " + Gasket.isOb_3() + "\n");
                    break;
                case "useRealOrNotReal":
                    Gasket.setUseRealOrNotReal(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("useRealOrNotReal === " + Gasket.isUseRealOrNotReal() + "\n");
                    break;
                case "strategyWorkOne":
                    Gasket.setStrategyWorkOne(Integer.parseInt(strings[1]));
                    ConsoleHelper.writeMessage("strategyWorkOne === " + Gasket.getStrategyWorkOne() + "\n");
                    break;
                case "strategyOneTime":
                    Gasket.setOb_4(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("strategyOneTime === " + Gasket.isOb_4() + "\n");
                    break;
                case "dateDifference":
                    Gasket.setDateDifference(Integer.parseInt(strings[1]));
                    ConsoleHelper.writeMessage("dateDifference === " + Gasket.getDateDifference() + "\n");
                    break;
                case "rangePriceMIN":
                    Gasket.setRangePriceMIN(Double.parseDouble(strings[1]));
                    ConsoleHelper.writeMessage("rangePriceMIN === " + Gasket.getRangePriceMIN() + "\n");
                    break;
                case "rangePriceMAX":
                    Gasket.setRangePriceMAX(Double.parseDouble(strings[1]));
                    ConsoleHelper.writeMessage("rangePriceMAX === " + Gasket.getRangePriceMAX() + "\n");
                    break;
                case "gameDirection":
                    Gasket.setGameDirection(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("gameDirection === " + Gasket.isGameDirection() + "\n");
                    break;
                case "strategyOne":
                    Gasket.setOb_2(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("strategyOne === " + Gasket.isOb_2() + "\n");
                    break;
                case "twoAccounts":
                    Gasket.setTwoAccounts(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("twoAccounts === " + Gasket.isTwoAccounts() + "\n");
                    break;
                case "priceActive":
                    Gasket.setPriceActive(Double.parseDouble(strings[1]));
                    ConsoleHelper.writeMessage("priceActive === " + Gasket.getPriceActive() + "\n");
                    break;
                case "rangeLevel":
                    Gasket.setRangeLevel(Double.parseDouble(strings[1]));
                    ConsoleHelper.writeMessage("rangeLevel === " + Gasket.getRangeLevel() + "\n");
                    break;
                case "typeOrder":
                    Gasket.setTypeOrder(strings[1].trim());
                    ConsoleHelper.writeMessage("typeOrder === " + Gasket.getTypeOrder() + "\n");
                    break;
                case "visible":
                    Gasket.setVisible(Double.parseDouble(strings[1]));
                    ConsoleHelper.writeMessage("visible === " + Gasket.getVisible() + "\n");
                    break;
                case "trading":
                    Gasket.setTrading(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("trading === " + Gasket.isTrading() + "\n");
                    break;
                case "take":
                    Gasket.setTake(Double.parseDouble(strings[1]));
                    ConsoleHelper.writeMessage("take === " + Gasket.getTake() + "\n");
                    break;
                case "stop":
                    Gasket.setStop(Double.parseDouble(strings[1]));
                    ConsoleHelper.writeMessage("stop === " + Gasket.getStop() + "\n");
                    break;
                case "PORT":
                    Gasket.setPORT(Integer.parseInt(strings[1]));
                    ConsoleHelper.writeMessage("PORT === " + Gasket.getPORT() + "\n");
                    break;
                case "lot":
                    Gasket.setLot(Double.parseDouble(strings[1]));
                    ConsoleHelper.writeMessage("lot === " + Gasket.getLot() + "\n");
                    break;
                case "one":
                    Gasket.setOb(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("one === " + Gasket.isOb() + "\n");
                    break;
                case "SETTINGS" :
                    if (strings[1].equals("RESTART")) parserSetting.stopAndRestart();
                    break;
                default:
                    if (flag) {
                        ConsoleHelper.writeMessage("\nВ файле настроек что-то не то с командой.\n "
                                + "Проверьте правильность настроек в файле Setting.txt потом перезапустите программу\n"
                                + "или выполните команду - 'SETTINGS=RESTART'.\n");
                    } else
                        ConsoleHelper.writeMessage("\nВы ввели неверную команду, попробуйте еще раз.\n");
            }
        } catch (Exception e) {
            if (flag) {
                ConsoleHelper.writeMessage("\nВ файле настроек что-то не то с командой.\n"
                        + "Проверьте правильность настроек в файле Setting.txt потом перезапустите программу\n"
                        + "или выполните команду - 'SETTINGS=RESTART'.\n");
            } else
                ConsoleHelper.writeMessage("\nОшибочка, повторите ввод.\n");
        }
        //parserSetting.stopAndRestart();
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setParserSetting(ParserSetting parserSetting) {
        this.parserSetting = parserSetting;
    }
}
