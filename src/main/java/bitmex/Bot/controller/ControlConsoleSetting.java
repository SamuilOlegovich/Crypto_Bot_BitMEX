package bitmex.Bot.controller;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

public class ControlConsoleSetting extends Thread {

    public ControlConsoleSetting() {
    }

    @Override
    public void run() {
        while (true) {
            String string = ConsoleHelper.readString();
            if (string.length() > 3) {
                if (string.trim().equals("info")) {
                    ConsoleHelper.printInfoSettings();
                } else {
                    parserAndExecutor(string);
                }
            }
        }
    }

    private void parserAndExecutor(String string) {
        String[] strings = string.trim().split("=");

        if (strings.length < 2) {
            ConsoleHelper.writeMessage("\nВы допустили ошибку, повторите ввод.\n");
            return;
        } else {
             strings[1] = strings[1].replaceAll(",", ".");
        }

        try {
            switch (strings[0]) {

                case "timeBetweenOrders":
                    Gasket.setTimeBetweenOrders(Integer.parseInt(strings[1]));
                    ConsoleHelper.writeMessage("timeBetweenOrders === " + Gasket.getTimeBetweenOrders() + "\n");
                    break;
                case "gameAllDirection":
                    Gasket.setGameAllDirection(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("gameAllDirection === " + Gasket.isGameAllDirection() + "\n");
                    break;
                case "strategyOneRange":
                    Gasket.setStrategyOneRange(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("strategyOneRange === " + Gasket.isStrategyOneRange() + "\n");
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
                    Gasket.setStrategyOneTime(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("strategyOneTime === " + Gasket.isStrategyOneTime() + "\n");
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
                    Gasket.setStrategyOne(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("strategyOne === " + Gasket.isStrategyOne() + "\n");
                    break;
                case "twoAccounts":
                    Gasket.setTwoAccounts(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("twoAccounts === " + Gasket.isTwoAccounts() + "\n");
                    break;
                case "priceActiv":
                    Gasket.setPriceActiv(Double.parseDouble(strings[1]));
                    ConsoleHelper.writeMessage("priceActiv === " + Gasket.getPriceActiv() + "\n");
                    break;
                case "rangeLivel":
                    Gasket.setRangeLivel(Double.parseDouble(strings[1]));
                    ConsoleHelper.writeMessage("rangeLivel === " + Gasket.getRangeLivel() + "\n");
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
                    Gasket.setOne(strings[1].equalsIgnoreCase("true"));
                    ConsoleHelper.writeMessage("one === " + Gasket.isOne() + "\n");
                    break;
                default:
                    ConsoleHelper.writeMessage("\nВы ввели неверную команду, попробуйте еще раз.\n");
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage("\nОшибочка, повторите ввод.\n");
        }
    }

    // TEST
    public static void main(String[] args) {
        new ControlConsoleSetting().start();
    }
}
