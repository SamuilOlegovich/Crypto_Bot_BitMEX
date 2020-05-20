package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

import static java.lang.Float.NaN;


public class ListensLooksAndComparesUser {
    private static ListensLooksAndComparesUser listensLooksAndComparesUser;

    private ArrayList<ArrayList<String>> listInListString;
    private ArrayList<InfoIndicator> listInfoIndicator;

    private KeepsTrackOfFillingListInfoIndicatorUser keepsTrackOfFillingListInfoIndicatorUser;
    private SortPriceUser sortPriceComparatorUser;
    private SavedPatternsUser savedPatternsUser;
    private SortSizeUser sortSizeUser;

//    private boolean oneStartFlag;

    private double priceStart;
    private double priceNow;


    private ListensLooksAndComparesUser() {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Класс ListensLooksAndCompares начал работать");

        this.keepsTrackOfFillingListInfoIndicatorUser = new KeepsTrackOfFillingListInfoIndicatorUser();
        this.savedPatternsUser = Gasket.getSavedPatternsUserClass();
        this.sortPriceComparatorUser = new SortPriceUser();
        this.listInfoIndicator = new ArrayList<>();
        this.listInListString = new ArrayList<>();
        this.sortSizeUser = new SortSizeUser();
//        this.oneStartFlag = true;
        this.priceStart = NaN;
        this.priceNow = NaN;
    }


    public static ListensLooksAndComparesUser getInstance() {
        if (listensLooksAndComparesUser == null) listensLooksAndComparesUser = new ListensLooksAndComparesUser();
        return listensLooksAndComparesUser;
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
        ReadAndSavePatternsUser.saveTemporarySavedPatternsUser(listInListString);

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Сравниваю рынок с ПАТТЕРНАМИ");

        // сравниваем оставшееся с патернами
        for (ArrayList<String> thisArrayListString : listInListString) {
            // получаем равные по размеру патерны
            ArrayList<ArrayList<String>> inListPatterns = savedPatternsUser.getListFoSize(thisArrayListString.size());

            // если равные по размеру патерны есть то начинаем сравнивать
            if (inListPatterns != null) {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- Есть - "
                        + inListPatterns.size() + " - паттерна по размеру");

                for (ArrayList<String> inArrayListString : inListPatterns) {
                    boolean result = true;

                    for (int i = 1; i < inArrayListString.size(); i++) {
                        String[] strings1;
                        String[] strings2;

                        // Тут мы так же определяем не строка ли это направления и сравниваем либо ее
                        // либо строки уровней
                        //
                        // period===M5===preview===1===price===9690,0===value===1187305===
                        // type===OpenPosMinusSmall===avg===0===dir===1===open===9674,5===
                        // close===9697,5===high===9697,5===low===9674,5    <----- строка уровней
                        //
                        // BIAS===BUY===10===AVERAGE===3===MAX===5   <----- строка направления
                        if (inArrayListString.get(i).startsWith("BIAS")
                                && thisArrayListString.get(i).startsWith("BIAS")) {

                            strings1 = thisArrayListString.get(i).split("===");
                            strings2 = inArrayListString.get(i).split("===");

                            // если хоть один объект не равен то прирываем цикл
                            if (!strings1[1].equals(strings2[1])) {
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

                            strings1 = thisArrayListString.get(i).split("===");
                            strings2 = inArrayListString.get(i).split("===");

                            // если хоть один объект не равен то прирываем цикл
                            if (!strings1[9].equals(strings2[9]) || !strings1[13].equals(strings2[13])) {
                                result = false;
                                break;
                            }
                        }
                    }

                    if (result) {
                        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                                + "Нашел совпадения в рынке с ПАТТЕРНАМИ User передаю на сделку");
                        makeDeal(inArrayListString.get(0));
                        return;
                        // возможно тут надо поставить return
                    }
                }
            }  else {
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                        + " --- Паттернов равных по размеру нет");
            }
        }
    }


    // Определяем какую сделку сделать и даем команду на ее исполнение
    private void makeDeal(String string) {
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
                + "Определяю какую сделку сделать согласно ИНФО ПАТТЕРНАМ");
//        String[] strings = string.split("===");
//
//        if (Integer.parseInt(strings[1]) > Integer.parseInt(strings[3])) {
//            String stringOut = string + "-PAT";
//
//            if (Gasket.isTrading()) new TradeBuy(stringOut);
//            new TestOrderBuyPattern(stringOut, Gasket.getBitmexQuote().getAskPrice());
//            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
//                    + stringOut + " --- Согластно ПАТТЕРНУ сделал сделку БАЙ");
//        } else if (Integer.parseInt(strings[1]) < Integer.parseInt(strings[3])) {
//            String stringOut = string + "-PAT";
//
//            if (Gasket.isTrading()) new TradeSell(stringOut);
//            new TestOrderSellPattern(stringOut, Gasket.getBitmexQuote().getBidPrice());
//            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- "
//                    + stringOut + " --- Согластно ПАТТЕРНУ сделал сделку СЕЛЛ");
//        }
    }


    // удаляем листы размеры которых длиннее паттернов
    private void removeUnnecessaryLists() {
        if (listInListString.size() > 0) {
            int maxArraySize = savedPatternsUser.getMaxArraySizeUser();
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
            ReadAndSavePatternsUser.saveSavedPatternsDeleteUser(arrayListArrayList);
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
        listInfoIndicator.sort(sortPriceComparatorUser);
        boolean flag = isTime();
//        oneStartFlag = false;

        if (listInListString.size() > 0) {
            for (ArrayList<String> arrayListString : listInListString) {
                if (flag) {
                    String stringBias = "BIAS===" + getBias() + "\n";
                    arrayListString.add(stringBias);
                    flag = true;
                }
                arrayListString.addAll(getListString());
            }
            if (flag) {
                ArrayList<String> arrayList = new ArrayList<>(getListString());
                arrayList.add(0, "0\n");
                listInListString.add(arrayList);
                priceStart = Gasket.getBitmexQuote().getBidPrice();
            }
        } else {
            ArrayList<String> arrayList = new ArrayList<>(getListString());
            arrayList.add(0, "0\n");
            listInListString.add(arrayList);
            priceStart = Gasket.getBitmexQuote().getBidPrice();
//        } else {
//                ArrayList<String> arrayList = new ArrayList<>(getListString());
//                arrayList.add(0, "0\n");
//                listInListString.add(arrayList);
        }



        listInfoIndicator.clear();
        listInListString.sort(sortSizeUser);

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- В листе для сравнения уже - "
                + listInListString.size() + " - паттернов");
    }


    // объекты преобразовываем в строки
    private ArrayList<String> getListString() {
        ArrayList<String> arrayList = new ArrayList<>();

        for (InfoIndicator infoIndicator : listInfoIndicator) {
            arrayList.add(infoIndicator.toStringUser());
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
            keepsTrackOfFillingListInfoIndicatorUser.setSleep();
            return true;
        }
    }






    /// === INNER CLASSES === ///



    private class SortPriceUser implements Comparator<InfoIndicator> {
        @Override
        public int compare(InfoIndicator o1, InfoIndicator o2) {
            double result = o1.getPrice() - o2.getPrice();
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }


    private class SortSizeUser implements Comparator<ArrayList<String>> {
        @Override
        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            double result = o1.size() - o2.size();
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
    }



    // следит за наполнением листа и если наполнение больше нет то сортирует его и запускает нужные методы
    private class KeepsTrackOfFillingListInfoIndicatorUser extends Thread {
        private int previousValue;
        private int sleep = 2;


        public KeepsTrackOfFillingListInfoIndicatorUser() {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Внутренний класс KeepsTrackOfFillingListInfoIndicatorUser начал работать");
            this.previousValue = 0;
            start();
        }

        @Override
        public void run() {

            while (true) {
                int size = listInfoIndicator.size();

                if (size > 0) {
                    if (previousValue == listInfoIndicator.size()) { // && isTime()) {
                        priceNow = Gasket.getBitmexQuote().getBidPrice();
                        previousValue = 0;
                        listSortedAndCompares();
//                        sleep = 2;
                    } else {
                        previousValue = size;
                        sleep = 2;
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


//        // Проверяем что бы наши пакеты данных не выбивалис из пятиминутки
//        private boolean isTime() {
//            String string = DatesTimes.getDateTerminal();
//            String[] strings = string.split(":");
//            int seconds = Integer.parseInt(strings[2]);
//
//            if (strings[1].equals("00") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("05") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("10") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("15") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("20") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("25") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("30") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("35") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("40") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("45") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("50") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else if (strings[1].equals("55") && seconds > Gasket.getSecondsSleepTime()) {
//                return true;
//            } else {
//                return false;
//            }
//        }
    }
}
