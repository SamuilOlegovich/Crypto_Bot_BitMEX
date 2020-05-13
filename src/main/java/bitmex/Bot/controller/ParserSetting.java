package bitmex.Bot.controller;

import bitmex.Bot.model.FilesAndPathCreator;
import bitmex.Bot.view.WriterAndReadFile;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.Gasket;

import java.util.ArrayList;


public class ParserSetting {
    private ExecutorCommandos executorCommandos;
    private String path;
    // = "/Users/samuilolegovich/Desktop/bitmex-client-master/src/main/java/bitmex/Bot/Logs/Settings.txt";
    //    /Users/samuilolegovich/Desktop/bitmex-client-master/target/classes/bitmex/Bot/Logs/2020-04-18 12:38:46=Log.txt


    public ParserSetting(ExecutorCommandos executorCommandos) {
        this.path = Gasket.getFilesAndPathCreator().getPathSettings();
        this.executorCommandos = executorCommandos;
        readFileSettings();
    }



    private void readFileSettings() {
        executorCommandos.setParserSetting(this);
        executorCommandos.setFlag(true);

        try {
            ArrayList<String> listSettings =  WriterAndReadFile.readFile(path);

            if (listSettings.size() < 1) {
                try {
                    ConsoleHelper.writeMessage("Настроек в файле Settings.txt необнаружено " +
                            "- включены и вписаны настройки по умолчанию.");
                    WriterAndReadFile.writerFile(getStringWrite(), path, false);
                } catch (Exception ex) {
                    ConsoleHelper.writeMessage("Ошибка в ЗАПИСИ файла Settings.txt .");
                }
            }

            for (String string : listSettings) {
                if (string.equalsIgnoreCase("END")) {
                    ConsoleHelper.writeMessage("Настройки УСПЕШНО считьаны.");
                    return;
                }
                String[] strings;
                if (string.length() > 4
                        && !string.equals(" --- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ --- ")) {
                    strings = string.split(" ===== ");
                    executorCommandos.parserAndExecutor(strings[0]);
                }
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Ошибка в ЧТЕНИИ файла Settings.txt");
        }
        executorCommandos.setFlag(false);
    }


    public void writeSettings() {
        try {
            WriterAndReadFile.writerFile(getStringWrite(), path, false);
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Настройки не перезаписались после команды изменения.");
        }
    }



    private String getStringWrite() {
        return " --- В ДАННЫЙ МОМЕНТ ПРОГРАММА ИМЕЕТ ТАКИЕ НАСТРОЙКИ --- " + "\n\n"
                + "timeBetweenOrders=" + Gasket.getTimeBetweenOrders()
                + " ===== время в секундах между выставлениями ордеров по одной стратегии\n"
                + "strategyWorkOne=" + Gasket.getStrategyWorkOne()
                + " ===== количество стратегий одновременно работающих (можно еще допелить или убрать)\n"
                + "dateDifference=" + Gasket.getDateDifference()
                + " ===== разница в часовом поясе\n"
                + "\n"

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
                + " ===== видимость ордера в стакане -- 0.0 - не видно, 1.0 - видно\n"
                + "\n"


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
                + " ===== порт подключения\n"
                + "\n"


                + "take=" + Gasket.getTake()
                + " ===== тейк профит в долларах\n"
                + "stop=" + Gasket.getStop()
                + " ===== стоп лосс в долларах\n"
                + "lot=" + Gasket.getLot()
                + " ===== количество контрактов\n"
                + "\n"

                + "obs_5=" + Gasket.isObs_5()
                + " ===== включить выключить стратегию\n"
                + "obs_4=" + Gasket.isObs_4()
                + " ===== включить выключить стратегию\n"
                + "obs_3=" + Gasket.isObs_3()
                + " ===== включить выключить стратегию\n"
                + "obs_2=" + Gasket.isObs_2()
                + " ===== включить выключить стратегию\n"
                + "obs=" + Gasket.isObs()
                + " ===== включить выключить стратегию\n"
                + "\n"

                + "useStopLevelOrNotStopTime=" + Gasket.getUseStopLevelOrNotStopTime()
                + " ===== сколько минут отслеживать сделку вышедшею за MIN уровни\n"
                + "useStopLevelOrNotStop=" + Gasket.isUseStopLevelOrNotStop()
                + " ===== отменять или не отменять сделку вышедшею за MIN уровни\n"
                + "timeCalculationCombinationLevel=" + Gasket.getTimeCalculationCombinationLevel()
                + " ===== когда уровни сформированы указываем время жизни данной комбинации\n"
                + "timeCalculationLevel=" + Gasket.getTimeCalculationLevel()
                + " ===== время за которое должны сформироваться уровни иначе все отменяется\n"
                + "\n"

                + "tradingPatterns=" + Gasket.isTradingPatterns()
                + " ===== включить по патернам патернов\n"
                + "savedPatterns=" + Gasket.isSavedPatterns()
                + " ===== включить нахождение и запись патернов\n"

                + "takeForCollectingPatterns=" + Gasket.getTakeForCollectingPatterns()
                + " ===== тейк для сбора и накопления паттернов\n"


                + "\n"
                + "END\n";
    }




    // TEST
    public static void main(String[] args) {
        FilesAndPathCreator filesAndPathCreator = new FilesAndPathCreator();
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExecutorCommandos executorCommandos = new ExecutorCommandos();
        ParserSetting parserSetting = new ParserSetting(executorCommandos);
        //readerAndParserSetting.readFileSettings();
        ControlConsoleSetting controlConsoleSetting = new ControlConsoleSetting(executorCommandos);
        executorCommandos.setParserSetting(parserSetting);
        controlConsoleSetting.start();
    }
}
