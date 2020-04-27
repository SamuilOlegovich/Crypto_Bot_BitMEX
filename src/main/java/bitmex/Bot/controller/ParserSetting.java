package bitmex.Bot.controller;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.view.WriterAndReadFile;


public class ParserSetting {
    private ExecutorCommandos executorCommandos;
    private boolean createNewFile = true;
    private boolean stopAndRestart;
    private String path;
    // = "/Users/samuilolegovich/Desktop/bitmex-client-master/src/main/java/bitmex/Bot/Logs/Settings.txt";
    //    /Users/samuilolegovich/Desktop/bitmex-client-master/target/classes/bitmex/Bot/Logs/2020-04-18 12:38:46=Log.txt

    public ParserSetting(ExecutorCommandos executorCommandos) {
        String string = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java");
        Gasket.setPath(string.replaceAll("controller/", ""));
        this.path = Gasket.getPath() + "Logs/Settings.txt";
        this.executorCommandos = executorCommandos;
        readFileSettings();
    }

    private void readFileSettings() {
        executorCommandos.setParserSetting(this);
        executorCommandos.setFlag(true);
        stopAndRestart = true;

        try {
            for (String string : WriterAndReadFile.readFile(path)) {
                if (!stopAndRestart) return;
                String[] strings;
                if (string.length() > 3
                        && !string.equals(" --- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ --- ")) {
                    strings = string.split(" ===== ");
                    executorCommandos.parserAndExecutor(strings[0]);
                }
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Ошибка в ЧТЕНИИ файла Settings.txt .");
            try {
                if (createNewFile) {
                    WriterAndReadFile.createNewFile(path);
                    try {
                        WriterAndReadFile.writerFile(getStringWrite(), path, true);
                    } catch (Exception ex) {
                        ConsoleHelper.writeMessage("Ошибка в ЗАПИСИ файла Settings.txt .");
                    }
                    readFileSettings();
                }
            } catch (Exception ex) {
                ConsoleHelper.writeMessage("По-пытался создать новый файл, не получилось.");
                createNewFile = false;
            }
        }
        executorCommandos.setFlag(false);
    }

    public void writeSettings() {
        try {
            WriterAndReadFile.writerFile(getStringWrite(), path, true);
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Настройки не перезаписались после команды изменения.");
        }
    }

    public void stopAndRestart() {
        createNewFile = false;
        readFileSettings();
    }



    private String getStringWrite() {
        return " --- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ --- " + "\n\n"
                + "timeBetweenOrders=" + Gasket.getTimeBetweenOrders()
                + " ===== время в секундах между выставлениями ордеров по одной стратегии\n"
                + "strategyWorkOne=" + Gasket.getStrategyWorkOne()
                + " ===== количество стратегий одновременно работающих (можно еще допелить или убрать)\n"
                + "dateDifference=" + Gasket.getDateDifference()
                + " ===== разница в часовом поясе\n\n"

                + "rangePriceMAX=" + Gasket.getRangePriceMAX()
                + " ===== диапазон в долларах от уровней для срабатывания ордера\n"
                + "rangePriceMIN=" + Gasket.getRangePriceMIN()
                + " ===== диапазон в долларах от уровней для отмены ордера\n"

                + "priceActive=" + Gasket.getPriceActive()
                + " ===== цена тригер для стоп лимитов и тейк лимитов\n"
                + "rangeLevel=" + Gasket.getRangeLevel()
                + " ===== диапазон в долларах для появления уровней\n"
                + "typeOrder=" + Gasket.getTypeOrder()
                + " ===== тип первого открываемого ордера\n"
                + "visible=" + Gasket.getVisible()
                + " ===== видимость ордера в стакане -- 0.0 - не видно, 1.0 - видно\n\n"


                + "useRealOrNotReal=" + Gasket.isUseRealOrNotReal()
                + " ===== Выбираем счет, true - реальный счет\n"
                + "gameAllDirection=" + Gasket.isGameAllDirection()
                + " ===== true - играть во все стороны на одном счету\n"
                + "gameAllDirection=" + Gasket.isGameAllDirection()
                + " ===== true - играть во все стороны на одном счету\n"
                + "gameDirection=" + Gasket.isGameDirection()
                + " ===== направление игры при одном счете, true - Buy, false - Sell\n"
                + "twoAccounts=" + Gasket.isTwoAccounts()
                + " ===== true - два счета, можно играть в две стороны, false - только в одну сторону\n"
                + "trading=" + Gasket.isTrading()
                + " ===== торговать - true нет - false\n"
                + "PORT=" + Gasket.getPORT()
                + " ===== порт подключения\n\n"


                + "take=" + Gasket.getTake()
                + " ===== тейк профит в долларах\n"
                + "stop=" + Gasket.getStop()
                + " ===== стоп лосс в долларах\n"
                + "lot=" + Gasket.getLot()
                + " ===== количество контрактов\n\n"

                + "strategyOneRange=" + Gasket.isOb_3()
                + " ===== включить выключить стратегию\n"
                + "strategyOneTime=" + Gasket.isOb_4()
                + " ===== включить выключить стратегию\n"
                + "strategyOne=" + Gasket.isOb_2()
                + " ===== включить выключить стратегию\n"
                + "one=" + Gasket.isOb()
                + " ===== включить выключить стратегию\n\n"

                + "useStopLevelOrNotStopTime=" + Gasket.getUseStopLevelOrNotStopTime()
                + " ===== сколько минут отслеживать сделку вышедшею за MIN уровни\n"
                + "useStopLevelOrNotStop=" + Gasket.isUseStopLevelOrNotStop()
                + " ===== отменять или не отменять сделку вышедшею за MIN уровни\n"
                + "timeCalculationCombinationLevel=" + Gasket.getTimeCalculationCombinationLevel()
                + " ===== когда уровни сформированы указываем время жизни данной комбинации\n"
                + "timeCalculationLevel=" + Gasket.getTimeCalculationLevel()
                + " ===== время за которое должны сформироваться уровни иначе все отменяется\n\n";
    }


    // TEST
    public static void main(String[] args) {
        ExecutorCommandos executorCommandos = new ExecutorCommandos();
        ParserSetting parserSetting = new ParserSetting(executorCommandos);
        //readerAndParserSetting.readFileSettings();
        ControlConsoleSetting controlConsoleSetting = new ControlConsoleSetting(executorCommandos);
        executorCommandos.setParserSetting(parserSetting);
        controlConsoleSetting.start();
    }
}
