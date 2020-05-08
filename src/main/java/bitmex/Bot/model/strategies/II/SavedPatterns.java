package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.FilesAndPathCreator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;


import java.util.ArrayList;
import java.io.*;



public class SavedPatterns implements Serializable {
    private static final long serialVersionUID = 908198101052020L;
    private static SavedPatterns savedPatterns;

    private ArrayList<ArrayList<String>> listsPricePatterns;
    private int maxArraySize;


    private SavedPatterns() {
        this.listsPricePatterns = new ArrayList<>();
        this.maxArraySize = 0;
    }

    public static SavedPatterns getInstance() {
        if (savedPatterns == null) savedPatterns = new SavedPatterns();
        return savedPatterns;
    }


    public synchronized void addListsPricePatterns(ArrayList<String> listsPricePatterns) {
        isThereSuchCombination(listsPricePatterns);
    }


    // ищем есть ли такие патерны если нет то добавляем,
    // если есть то устанавливаем приоритет
    private synchronized void isThereSuchCombination(ArrayList<String> arrayList) {
        ConsoleHelper.writeMessage("Сравниваю ПАТТЕРН с имеющимися ---- "
                + DatesTimes.getDateTerminal());



        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n").append(DatesTimes.getDateTerminal())
                .append(" - Размер входящего листа == ").append(arrayList.size()).append("\n");
        for (String s : arrayList) {
            stringBuilder.append(s);
        }
        stringBuilder.append("\n\n");
        ConsoleHelper.writeMessage(stringBuilder.toString());



        // перебираем массив стратегий и сравниваем с пришедшим
        for (ArrayList<String> stringArrayList : listsPricePatterns) {
            boolean result = true;

            // проверяем совпадает ли их размер
            if (stringArrayList.size() == arrayList.size()) {

                // если размер совпал то начинаем сравнивать построчно
                // не считая 0-вой строки так как там инфа о паттерне
                for (int i = 1; i < arrayList.size(); i++) {
                    String[] arr1 = stringArrayList.get(i).split("\"type\": \"");
                    String[] arr2 = arrayList.get(i).split("\"type\": \"");
                    String[] strings1 = arr1[1].split("\"");
                    String[] strings2 = arr2[1].split("\"");

                    // если хоть один объект не равен то прирываем цикл
                    if (!strings1[0].equals(strings2[0])) {
                        result = false;
                        break;
                    }
                }

                // если цикл не прерван и флаг TRUE то корректируем инфо данные о патерне
                // с учетом информации пришедшего паттерна
                // а так же прекращаем процесс поиска и сравнения
                if (result) {
                    ConsoleHelper.writeMessage("ПАТТЕРН такой есть - обновляю информацию ---- "
                            + DatesTimes.getDateTerminal());
                    String stringZero = setPriority(stringArrayList.get(0), arrayList.get(0));
                    stringArrayList.set(0, stringZero);
                    ReadAndSavePatterns.saveSavedPatterns();
                    return;
                }
            }
        }

        // если совпадение не было найдено - добавляем данный патерн в массив
        ConsoleHelper.writeMessage("Такого ПАТТЕРНА нет - ДОБАВЛЕН ---- "
                + DatesTimes.getDateTerminal());
        String stringZero = arrayList.get(0) + "===ID==="
                + ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39))
                + "\n";
        arrayList.set(0, stringZero);

        ConsoleHelper.writeMessage(stringZero +  " -- SIZE -- " + arrayList.size());

        listsPricePatterns.add(arrayList);
        maxArraySize = Math.max(arrayList.size(), maxArraySize);

        ConsoleHelper.writeMessage("Включаю СОХХРАНЕНИЕ ПАТТЕРНОВ");

        ReadAndSavePatterns.saveSavedPatterns();
    }


    // обновляем информационные данные в строке информации
    private String setPriority(String s1, String s2) {
        // распарсили строки
        String[] strings1 = s1.split("===");
        String[] strings2 = s2.split("===");

        // спарсили числа и прибавили их
        int sell = Integer.parseInt(strings1[3]) + Integer.parseInt(strings2[3]);
        int buy = Integer.parseInt(strings1[1]) + Integer.parseInt(strings2[1]);
        // считаем среднюю цену отклонения в противоположную сторону
        double average = (Double.parseDouble(strings1[5]) + Double.parseDouble(strings2[5])) / 2.0;
        // обновляем максимальное отклонение
//        double max = Math.max(Double.parseDouble(strings1[7]), Double.parseDouble(strings2[7]));
        double max = 0.0;
        try {
            max = Math.max(Double.parseDouble(strings1[7]), Double.parseDouble(strings2[7]));///////////////////////////
        } catch (NumberFormatException e) {
            ConsoleHelper.writeMessage("NumberFormatException");
        }

        System.out.println("\n");
        for (String a : strings1) {
            System.out.println(a);
        }
        System.out.println();
        for (String a : strings2) {
            System.out.println(a);
        }
        System.out.println("\n");


        // вернули итоговую инфо строку
        if (strings1.length <= 8) {

            System.out.println("\n" + s1 + "\n" + s2 + "\n");

            return strings1[0] + "===" + buy + "===" + strings1[2] + "===" + sell + "===" + strings1[4]
                    + "===" + average + "===" + strings1[6] + "===" + max + "===ID==="
                    + ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39))
                    + "\n";
        } else {
            return strings1[0] + "===" + buy + "===" + strings1[2] + "===" + sell + "===" + strings1[4] + "==="
                    + average + "===" + strings1[6] + "===" + max
                    + "===" + strings1[8] + "===" + strings1[9];
        }
    }


    // находим и отдаем массивы нужной длины - размера
    public ArrayList<ArrayList<String>> getListFoSize(int size) {
        ArrayList<ArrayList<String>> listFoSize = new ArrayList<>();

        for (ArrayList<String> list : listsPricePatterns) {
            if (list.size() == size) {
                listFoSize.add(list);
            }
        }

        if (listFoSize.size() > 0) return listFoSize;
        else return null;
    }


    // возвращаем самую большую длину имеющегося паттерна
    public int getMaxArraySize() {
        return maxArraySize;
    }


    public ArrayList<ArrayList<String>> getListsPricePatterns() {
        return listsPricePatterns;
    }

    public void setPatternsInListsPricePatterns(ArrayList<String> arrayList) {
        listsPricePatterns.add(arrayList);
        maxArraySize = Math.max(arrayList.size(), maxArraySize);
    }







    //TEST
    public static void main(String[] args) {

        FilesAndPathCreator filesAndPathCreator = new FilesAndPathCreator();//
        SavedPatterns savedPatterns = new SavedPatterns();
        Gasket.setSavedPatternsClass(savedPatterns);


        ArrayList<String> arrayList = new ArrayList<>();//
        arrayList.add("BUY===1===SELL===0===AVERAGE===8.0===MAX===19");//
        arrayList.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"19.03.2020 19:21:00\",\"price\": \"25,87\","
                + "\"value\": \"721\",\"type\": \"Ask\"}\n");//
        arrayList.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"19.03.2020 19:21:00\",\"price\": \"25,87\","
                + "\"value\": \"728\",\"type\": \"Bid\"}\n");//

        ArrayList<String> arrayList2 = new ArrayList<>();//
        arrayList2.add("BUY===1===SELL===0===AVERAGE===10.0===MAX===20");//
        arrayList2.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"19.03.2020 19:21:00\",\"price\": \"25,87\","
                + "\"value\": \"721\",\"type\": \"Ask\"}\n");//
        arrayList2.add("{\"period\": \"M30\",\"preview\": \"1\",\"time\": \"19.03.2020 19:21:00\",\"price\": \"25,87\","
                + "\"value\": \"728\",\"type\": \"Ask\"}\n");//

        ArrayList<String> arrayList3 = new ArrayList<>();//
        arrayList3.add("BUY===1===SELL===0===AVERAGE===8.0===MAX===19");
        arrayList3.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"19.03.2020 19:21:00\",\"price\": \"25,87\","
                + "\"value\": \"721\",\"type\": \"Ask\"}\n");//
        arrayList3.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"19.03.2020 19:21:00\",\"price\": \"25,87\","
                + "\"value\": \"728\",\"type\": \"Bid\"}\n");//

        System.out.println(arrayList);//
        System.out.println(arrayList2);//
        System.out.println(arrayList3);//


        savedPatterns.addListsPricePatterns(arrayList); //
        savedPatterns.addListsPricePatterns(arrayList2);//
        savedPatterns.addListsPricePatterns(arrayList3);//

        System.out.println();
        System.out.println(savedPatterns.listsPricePatterns);
        System.out.println();
        System.out.println("START");
        for (ArrayList<String> arr : savedPatterns.listsPricePatterns) {
            for (String s : arr)
            System.out.print(s);
            System.out.println("NEXT" + "\n");
        }
            System.out.println("END" + "\n");



        ReadAndSavePatterns.saveSavedPatterns();



//        // сер
//        try {
//            FileOutputStream outputStream = new FileOutputStream(Gasket.getFilesAndPathCreator().getPathPatterns());
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//            objectOutputStream.writeObject(savedPatterns);
//            objectOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(savedPatterns.listsPricePatterns);//
//        savedPatterns.listsPricePatterns.clear();
//        System.out.println(savedPatterns.listsPricePatterns.size());//
//        System.out.println();//


//        // десер
//        try {
//            FileInputStream fileInputStream = new FileInputStream(Gasket.getFilesAndPathCreator().getPathPatterns());
//            System.out.println(1);
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            System.out.println(2);
//            savedPatterns = (SavedPatterns) objectInputStream.readObject();
//            System.out.println(3);
//            fileInputStream.close();
//            System.out.println(4);
//        } catch (ClassNotFoundException | IOException e) {
//            System.out.println("Файл с ПАТТЕРНАМИ пуст или в нем есть ошибка");
//            e.printStackTrace();
//        }
//
//        if (savedPatterns != null) {
//            System.out.println(savedPatterns.listsPricePatterns);
//        }

    }
}
