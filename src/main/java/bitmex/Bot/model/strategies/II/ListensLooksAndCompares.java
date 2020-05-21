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

//    private boolean oneStartFlag;

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
//        this.oneStartFlag = true;
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
        if (Double.isNaN(priceStart)) {
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
        // сохраняю те патерны которые еще актуальны на данный момент
        ReadAndSavePatterns.saveTemporarySavedPatterns(listInListString);

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Сравниваю рынок с ПАТТЕРНАМИ");

        // сравниваем оставшееся с патернами
        for (ArrayList<String> thisArrayListString : listInListString) {
            // получаем равные по размеру патерны
            ArrayList<ArrayList<String>> inListPatterns = savedPatterns.getListFoSize(thisArrayListString.size());

            // если равные по размеру патерны есть то начинаем сравнивать
            if (inListPatterns != null) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- Есть - "
                        + inListPatterns.size() + " - паттерна по размеру");

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
            }  else {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- Паттернов равных по размеру нет");
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
            ArrayList<ArrayList<String>> arrayListArrayList = new ArrayList<>();

            for (int i = 0; i < listInListString.size(); i++) {
                if (listInListString.get(i).size() > maxArraySize) {
                    arrayListArrayList.add(listInListString.get(i));
                    lineNumbersToDelete.add(i);
                }
            }

            Collections.reverse(lineNumbersToDelete);
            // сохраняю удаленные патерны
            ReadAndSavePatterns.saveSavedPatternsDelete(arrayListArrayList);
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- Удалил - "
                    + lineNumbersToDelete.size() + " - паттерна большего размера");

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
        boolean flag = isTime();

        if (listInListString.size() > 0) {
            for (ArrayList<String> arrayListString : listInListString) {
                // если этот пакет уровней пришлел в нужное время, то добавляем вначале строку смещения
                if (flag) {
                    String stringBias = "BIAS===" + getBias() + "\n";
                    arrayListString.add(stringBias);
                    flag = true;
                }
                arrayListString.addAll(getListString(arrayListString));
            }

            if (flag) {
                ArrayList<String> arrayList = new ArrayList<>(getListString(null));
                arrayList.add(0, "0\n");
                listInListString.add(arrayList);
                priceStart = Gasket.getBitmexQuote().getBidPrice();
            }
        } else {
            ArrayList<String> arrayList = new ArrayList<>(getListString(null));
            arrayList.add(0, "0\n");
            listInListString.add(arrayList);
            priceStart = Gasket.getBitmexQuote().getBidPrice();
//        } else {
//                ArrayList<String> arrayList = new ArrayList<>(getListString());
//                arrayList.add(0, "0\n");
//                listInListString.add(arrayList);
        }

        listInfoIndicator.clear();
        listInListString.sort(sortSize);

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- В листе для сравнения уже - "
                + listInListString.size() + " - паттернов");
    }


    // объекты преобразовываем в строки а так же проверяем есть ли такие уровни,
    // если есть то удаляем их из входящего листа и меняем их в листе направлений
    private ArrayList<String> getListString(ArrayList<String> arrayListIn) {
        ArrayList<InfoIndicator> infoIndicatorArrayList = new ArrayList<>(listInfoIndicator);
        ArrayList<String> arrayList = new ArrayList<>();
        int count = 0;
        long time;

        if (arrayListIn != null) {
            for (String s : arrayListIn) {
                if (s.startsWith("BIAS")) count++;
            }

            if (count != 0) time = DatesTimes.getDateTerminalLong() - (1000 * 60 * count);
            else time = DatesTimes.getDateTerminalLong();

            for (int i = infoIndicatorArrayList.size() - 1; i > -1; i--) {
                InfoIndicator infoIndicator = infoIndicatorArrayList.get(i);

                if (infoIndicator.getTime().getTime() <= time) {
                    infoIndicatorArrayList.remove(i);
                    break;
                }

                for (int j = arrayListIn.size() - 1; j > -1; j--) {
                    String[] stringsIn = infoIndicator.toString().split(",");
                    String[] stringsThis = arrayListIn.get(j).split(",");

                    if (stringsIn[2].equals(stringsThis[2]) && stringsIn[3].equals(stringsThis[3])
                            && stringsIn[5].equals(stringsThis[5]) && stringsIn[7].equals(stringsThis[7])) {
                        arrayListIn.set(j, infoIndicator.toString());
                        infoIndicatorArrayList.remove(i);
                    }
                }
            }
        }

        if (infoIndicatorArrayList.size() != 0) {
            for (InfoIndicator infoIndicator : infoIndicatorArrayList) {
                arrayList.add(infoIndicator.toString());
            }
        }

        return arrayList;
    }


    // находим куда сместилась цена и другие данные
    private String getBias() {
        double bias = priceNow - priceStart;
        String stringOut;

        if (bias > 0) {
            stringOut = "BUY===" + bias;
        } else if (bias < 0) {
            stringOut = "SELL===" + bias;
        } else {
            stringOut = "NULL===0";
        }
        return stringOut;
    }



    // Проверяем что бы наши пакеты данных не выбивалис из пятиминутки
    private boolean isTime() {
        String string = DatesTimes.getDateTerminal();
        String[] strings = string.split(":");
        double seconds = Double.parseDouble(strings[1] + "." + strings[2]);

        if (seconds > 0.05 && seconds < 4.98) {
            return false;
        } else if (seconds > 5.20 && seconds < 9.98) {
            return false;
        } else if (seconds > 10.20 && seconds < 14.98) {
            return false;
        } else if (seconds > 15.20 && seconds < 19.98) {
            return false;
        } else if (seconds > 20.20 && seconds < 24.98) {
            return false;
        } else if (seconds > 25.20 && seconds < 29.98) {
            return false;
        } else if (seconds > 30.20 && seconds < 34.98) {
            return false;
        } else if (seconds > 35.20 && seconds < 39.98) {
            return false;
        } else if (seconds > 40.20 && seconds < 44.98) {
            return false;
        } else if (seconds > 45.20 && seconds < 49.98) {
            return false;
        } else if (seconds > 50.20 && seconds < 54.98) {
            return false;
        } else if (seconds > 55.20 && seconds < 59.98) {
            return false;
        } else {
            keepsTrackOfFillingListInfoIndicator.setSleep();
            return true;
        }
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
        private int sleep = 3;


        public KeepsTrackOfFillingListInfoIndicator() {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Внутренний класс KeepsTrackOfFillingListInfoIndicator начал работать");
            this.previousValue = 0;
            start();
        }

        @Override
        public void run() {

            while (true) {
                int size = listInfoIndicator.size();

                if (size > 0) {
                    if (previousValue == listInfoIndicator.size()) {    // && isTime()) {
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


        private void setSleep() {
            sleep = Gasket.getSecondsSleepTime();
        }
    }
}
