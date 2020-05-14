package bitmex.Bot.model.strategies.II;


import bitmex.Bot.model.strategies.oneStrategies.TradeSell;
import bitmex.Bot.model.strategies.oneStrategies.TradeBuy;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;


import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

import static java.lang.Double.NaN;


public class ListensLooksAndCompares {
    private static ListensLooksAndCompares listensLooksAndCompares;

    private ArrayList<ArrayList<String>> listInListString;
    private ArrayList<InfoIndicator> listInfoIndicator;

    private KeepsTrackOfFillingListInfoIndicator keepsTrackOfFillingListInfoIndicator;
    private SavedPatterns savedPatterns;
    private SortSize sortSize;

    private boolean oneStartFlag;
    private boolean timeFlag;

    private double priceStart;
    private double priceNow;


    private ListensLooksAndCompares() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Класс ListensLooksAndCompares начал работать");

        this.keepsTrackOfFillingListInfoIndicator = new KeepsTrackOfFillingListInfoIndicator();
        this.savedPatterns = Gasket.getSavedPatternsClass();
        this.listInfoIndicator = new ArrayList<>();
        this.listInListString = new ArrayList<>();
        this.sortSize = new SortSize();
        this.oneStartFlag = true;
        this.timeFlag = false;
        this.priceNow = NaN;
    }


    public static ListensLooksAndCompares getInstance() {
        if (listensLooksAndCompares == null) listensLooksAndCompares = new ListensLooksAndCompares();
        return listensLooksAndCompares;
    }



    // принимаем объекты и если еще не запускали метод их обработки то запускаем его,
    // если он уже запущен то просто кладем объекты в массив
    // так же получаем текущую цену
    public synchronized void setInfoString(InfoIndicator infoIndicator) {
        if (oneStartFlag) {
            priceStart = Gasket.getBitmexQuote().getBidPrice();
        }
        listInfoIndicator.add(infoIndicator);
    }



    // отсыпаемся и начинаем работать
    private synchronized void listSortedAndCompares() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + "Не смог проснуться в методе listSorter() "
                    + "класса ListensToLooksAndFills");
        }

        // сортируем и добавляем
        sortPrice();
        // удаляем ненужное
        removeUnnecessaryLists();

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Сравниваю рынок с ПАТТЕРНАМИ");

        // сравниваем оставшееся с патернами
        for (ArrayList<String> thisArrayListString : listInListString) {
            // получаем равные по размеру патерны
            ArrayList<ArrayList<String>> inListPatterns = savedPatterns.getListFoSize(thisArrayListString.size());

            // если равные по размеру патерны есть то начинаем сравнивать
            if (inListPatterns != null) {

                for (ArrayList<String> inArrayListString : inListPatterns) {
                    boolean result = true;

                    for (int i = 1; i < inArrayListString.size(); i++) {
                        String[] strings1;
                        String[] strings2;
                        String[] arr1;
                        String[] arr2;

                        // Тут мы так же определяем не строка ли это направления и сравниваем либо ее либо строки уровней
                        // BIAS===BUY===10===AVERAGE===3===MAX===5   <----- строка направления
                        if (inArrayListString.get(i).startsWith("BIAS")
                                && thisArrayListString.get(i).startsWith("BIAS")) {

                            arr1 = thisArrayListString.get(i).split("===");
                            arr2 = inArrayListString.get(i).split("===");

                            // если хоть один объект не равен то прирываем цикл
                            if (!arr1[1].equals(arr2[1])) {
                                result = false;
                                break;
                            }

                        } else if ((inArrayListString.get(i).startsWith("BIAS")
                                && !thisArrayListString.get(i).startsWith("BIAS"))
                                || (!inArrayListString.get(i).startsWith("BIAS")
                                && thisArrayListString.get(i).startsWith("BIAS"))) {
                            // если под одним и тем же номером находятся разные по значимости строки то прирываем цикл
                            result = false;
                            break;
                        } else if (!inArrayListString.get(i).startsWith("BIAS")
                                && !thisArrayListString.get(i).startsWith("BIAS")) {

                            arr1 = thisArrayListString.get(i).split("\"type\": \"");
                            arr2 = inArrayListString.get(i).split("\"type\": \"");
                            strings1 = arr1[1].split("\"");
                            strings2 = arr2[1].split("\"");

                            // если хоть один объект не равен то прирываем цикл
                            if (!strings1[0].equals(strings2[0])) {
                                result = false;
                                break;
                            }
                        }
                    }

                    if (result) {
                        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                                + "Нашел совпадения в рынке с ПАТТЕРНАМИ передаю на сделку");
                        makeDeal(inArrayListString.get(0));
                        // возможно тут надо поставить return
                    }
                }
            }
        }
    }


    // Определяем какую сделку сделать и даем команду на ее исполнение
    private void makeDeal(String string) {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Определяю какую сделку сделать согласно ИНФО ПАТТЕРНАМ");
        String[] strings = string.split("===");

        if (Integer.parseInt(strings[1]) > Integer.parseInt(strings[3])) {
            String stringOut = string + "-PAT";

            if (Gasket.isTrading()) new TradeBuy(stringOut);
            new TestOrderBuyPattern(stringOut, Gasket.getBitmexQuote().getAskPrice());
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + stringOut + " --- Согластно ПАТТЕРНУ сделал сделку БАЙ");
        } else if (Integer.parseInt(strings[1]) < Integer.parseInt(strings[3])) {
            String stringOut = string + "-PAT";

            if (Gasket.isTrading()) new TradeSell(stringOut);
            new TestOrderSellPattern(stringOut, Gasket.getBitmexQuote().getBidPrice());
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                    + stringOut + " --- Согластно ПАТТЕРНУ сделал сделку СЕЛЛ");
        }
    }


    // удаляем листы размеры которых длиннее паттернов
    private void removeUnnecessaryLists() {
        if (listInListString.size() > 0) {
            int maxArraySize = savedPatterns.getMaxArraySize();
            ArrayList<Integer> lineNumbersToDelete = new ArrayList<>();

            for (int i = 0; i < listInListString.size(); i++) {
                if (listInListString.get(i).size() > maxArraySize) {
                    lineNumbersToDelete.add(i);
                }
            }

            Collections.reverse(lineNumbersToDelete);

            for (Integer integer : lineNumbersToDelete) {
                listInListString.remove((int) integer);
            }
        }
    }


    // сортируем и наполняем лист сравнений листами строк
    // очищаем лист входящих объектов
    private void sortPrice() {
        Comparator sortPriceComparator = new ListensLooksAndCompares.SortPrice();
        listInfoIndicator.sort(sortPriceComparator);
        oneStartFlag = false;

        if (listInListString.size() > 0) {
            for (ArrayList<String> arrayListString : listInListString) {
                String stringBias = "BIAS===" + getBias() + "\n";
                arrayListString.add(stringBias);
                arrayListString.addAll(getListString());
            }
            ArrayList<String> arrayList = new ArrayList<>(getListString());
            arrayList.add(0, "0\n");
            listInListString.add(arrayList);
            priceStart = Gasket.getBitmexQuote().getBidPrice();
        } else if (listInListString.size() == 0 && oneStartFlag == false) {
            ArrayList<String> arrayList = new ArrayList<>(getListString());
            arrayList.add(0, "0\n");
            listInListString.add(arrayList);
            priceStart = Gasket.getBitmexQuote().getBidPrice();
        } else {
            ArrayList<String> arrayList = new ArrayList<>(getListString());
            arrayList.add(0, "0\n");
            listInListString.add(arrayList);

        }
        listInfoIndicator.clear();
        listInListString.sort(sortSize);
    }


    // объекты преобразовываем в строки
    private ArrayList<String> getListString() {
        ArrayList<String> arrayList = new ArrayList<>();

        for (InfoIndicator infoIndicator : listInfoIndicator) {
            arrayList.add(infoIndicator.toString());
        }

        return arrayList;
    }


    // находим куда сместилась цена и другие данные
    private String getBias() {
        String stringOut;
        double bias = priceNow - priceStart;

        if (bias > 0) {
            stringOut = "BUY===" + bias;
        } else if (bias < 0) {
            stringOut = "SELL===" + bias;
        } else {
            stringOut = "NULL===0";
        }
        return stringOut;
    }




    /// === INNER CLASSES === ///



    private class SortPrice implements Comparator<InfoIndicator> {
        @Override
        public int compare(InfoIndicator o1, InfoIndicator o2) {
            double result = o1.getPrice() - o2.getPrice();
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }


    private class SortSize implements Comparator<ArrayList<String>> {
        @Override
        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            double result = o1.size() - o2.size();
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }



    // следит за наполнением листа и если наполнение больше нет то сортирует его и запускает нужные методы
    private class KeepsTrackOfFillingListInfoIndicator extends Thread {
        private int previousValue;


        public KeepsTrackOfFillingListInfoIndicator() {
            this.previousValue = 0;
            start();
        }

        @Override
        public void run() {

            while (true) {
                int size = listInfoIndicator.size();
                int sleep = 3;

                if (size > 0) {
                    if (previousValue == listInfoIndicator.size()) {
                        priceNow = Gasket.getBitmexQuote().getBidPrice();
                        previousValue = 0;
                        listSortedAndCompares();
                        sleep = 10;
                    } else {
                        previousValue = size;
                    }
                }

                try {
                    Thread.sleep(1000 * sleep);
                } catch (InterruptedException e) {
                    ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                            + " --- Не смог проснуться во внутреннем классе "
                            + "KeepsTrackOfFillingListInfoIndicator класса ListensToLooksAndFills - "
                            + " sleep = " + sleep);
                }
            }
        }
    }
}
