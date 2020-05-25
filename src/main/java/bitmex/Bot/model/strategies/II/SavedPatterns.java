package bitmex.Bot.model.strategies.II;


import bitmex.Bot.model.FilesAndPathCreator;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;


import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.io.*;
import java.util.TreeSet;


public class SavedPatterns implements Serializable {

    private static final long serialVersionUID = 908198101052020L;
    private static SavedPatterns savedPatterns;
    private SortSize sortSize;

    private ArrayList<ArrayList<String>> listsPricePatterns;
    private int maxArraySize;


    private SavedPatterns() {
        this.listsPricePatterns = new ArrayList<>();
        this.sortSize = new SortSize();
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
        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- Сравниваю ПАТТЕРН с имеющимися");

        // находим количество смещений и запоминаем индекс первого смещения
        ArrayList<String> inArrayList = new ArrayList<>(arrayList);
        int countBias = 0;
        int indexBias = 0;

        for (String string : inArrayList) {
            if (string.startsWith("BIAS")) {
                if (indexBias == 0) {
                    indexBias = inArrayList.indexOf(string);
                }
                countBias++;
            }
        }

        if (countBias > 0) {

            // удаляю первый мусорный блок
            for (int i = indexBias; i > 0; i--) {
                inArrayList.remove(i);
            }


            // чистим от оставшихся предварительных исчезнувших уровняй
            ArrayList<Integer> indexArrayList = new ArrayList<>();

            for (String stringOne : inArrayList) {
                int bias = 0;

                if (!stringOne.startsWith("BUY") && !stringOne.startsWith("BIAS")) {
                    String[] oneStrings = stringOne.split(",");
                    String[] twoStrings;

                    for (int i = inArrayList.indexOf(stringOne) + 1; i < inArrayList.size(); i++) {
                        String stringTwo = inArrayList.get(i);

                        if (bias == 0) {
                            bias = bias + (stringTwo.startsWith("BIAS") ? 1 : 0);

                        } else if (bias == 1) {
                            twoStrings = stringTwo.split(",");

                            if (oneStrings.length == twoStrings.length) {

                                if (!oneStrings[5].equals("\"type\": \"OI_ZS_MIN_MINUS\"")
                                        && !oneStrings[5].equals("\"type\": \"OI_ZS_MIN_PLUS\"")
                                        && !oneStrings[5].equals("\"type\": \"DELTA_ZS_MIN_MINUS\"")
                                        && !oneStrings[5].equals("\"type\": \"DELTA_ZS_MIN_PLUS\"")) {

                                    if (oneStrings[0].equals(twoStrings[0])
                                            && oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                    } else if (oneStrings[0].equals(twoStrings[0])
                                            && (!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("\"preview\": \"1\""))
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                    } else if ((!oneStrings[0].equals(twoStrings[0])
                                            && oneStrings[0].equals("{\"period\": \"M5\""))
                                            && oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                    } else if ((!oneStrings[0].equals(twoStrings[0])
                                            && oneStrings[0].equals("{\"period\": \"M5\""))
                                            && (!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("\"preview\": \"1\""))
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringOne));

                                    } else if ((!oneStrings[0].equals(twoStrings[0])
                                            && oneStrings[0].equals("{\"period\": \"M5\""))
                                            && (!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("\"preview\": \"0\""))
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringTwo));

                                    } else if ((!oneStrings[0].equals(twoStrings[0])
                                            && twoStrings[0].equals("{\"period\": \"M5\""))
                                            && (!oneStrings[1].equals(twoStrings[1])
                                            && oneStrings[1].equals("\"preview\": \"0\""))
                                            && oneStrings[5].equals(twoStrings[5])) {
                                        indexArrayList.add(inArrayList.indexOf(stringTwo));
                                    }
                                }
                            }
                        } else if (bias == 2) {
                                break;
                        }
                    }
                }
            }

            // если каким-то образом будет два одинаковых индекса, так мы их нивилируем
            TreeSet<Integer> treeSet = new TreeSet<>(indexArrayList);
            indexArrayList.clear();
            indexArrayList.addAll(treeSet);
            Collections.reverse(indexArrayList);

            for (Integer index : indexArrayList) {
                inArrayList.remove((int) index);
            }



           /*
            0 {"period": "M5",
                    1       "preview": "1",
                    2      "time": "20.05.2020 21:19:00",
                    3     "price": "9554,5",
                    4    "value": "5465914",
                    5   "type": "Volume",
                    6  "avg": "7410290",
                    7 "dir": "-1",
                    8 "open": "9560,5",
                    9 "close": "9549,0",
                    10 "high": "9561,0",
                    11 "low": "9548,5"}*/


            // перебираем массив стратегий и сравниваем с пришедшим
            for (ArrayList<String> stringArrayList : listsPricePatterns) {
                boolean result = true;

                // проверяем совпадает ли их размер
                if (stringArrayList.size() == inArrayList.size()) {

                    // если размер совпал то начинаем сравнивать построчно
                    // не считая 0-вой строки так как там инфа о паттерне
                    for (int i = 1; i < inArrayList.size(); i++) {
                        String[] strings1;
                        String[] strings2;
                        String[] arr1;
                        String[] arr2;

                        // Тут мы так же определяем не строка ли это направления и сравниваем либо ее либо строки уровней
                        // BIAS===BUY===10===AVERAGE===3===MAX===5   <----- строка направления
                        if (inArrayList.get(i).startsWith("BIAS") && stringArrayList.get(i).startsWith("BIAS")) {
                            arr1 = stringArrayList.get(i).split("===");
                            arr2 = inArrayList.get(i).split("===");

                            // если хоть один объект не равен то прирываем цикл
                            if (!arr1[1].equals(arr2[1])) {
                                result = false;
                                break;
                            }

                        } else if ((inArrayList.get(i).startsWith("BIAS") && !stringArrayList.get(i).startsWith("BIAS"))
                                || (!inArrayList.get(i).startsWith("BIAS") && stringArrayList.get(i).startsWith("BIAS"))) {
                            // если под одним и тем же номером находятся разные по значимости строки то прирываем цикл
                            result = false;
                            break;
                        } else if (!inArrayList.get(i).startsWith("BIAS") && !stringArrayList.get(i).startsWith("BIAS")) {
                            arr1 = stringArrayList.get(i).split("\"type\": \"");
                            arr2 = inArrayList.get(i).split("\"type\": \"");
                            strings1 = arr1[1].split("\"");
                            strings2 = arr2[1].split("\"");

                            // если хоть один объект не равен то прирываем цикл
                            if (!strings1[0].equals(strings2[0])) {
                                result = false;
                                break;
                            }
                        }
                    }

                    // если цикл не прерван и флаг TRUE то корректируем инфо данные о патерне
                    // с учетом информации пришедшего паттерна
                    // а так же прекращаем процесс поиска и сравнения
                    if (result) {
                        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                                + " --- ПАТТЕРН такой есть - обновляю информацию");
                        String stringZero = setPriority(stringArrayList.get(0), inArrayList.get(0));
                        stringArrayList.set(0, stringZero);
                        ReadAndSavePatterns.saveSavedPatterns();
                        return;
                    }
                }
            }

            // если совпадение не было найдено - добавляем данный патерн в массив
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " --- Такого ПАТТЕРНА нет - ДОБАВЛЕН --- "
                    + "SIZE --- " + inArrayList.size());

            // проверяю есть ли такой айди и если есть меняю его на другой
            inArrayList.set(0, checkingID(inArrayList.get(0)));

            listsPricePatterns.add(0, inArrayList);
            maxArraySize = Math.max(inArrayList.size(), maxArraySize);

            listsPricePatterns.sort(sortSize);

            ReadAndSavePatterns.saveSavedPatterns();
        } else {
            inArrayList.clear();
        }
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
        double max = Math.max(Double.parseDouble(strings1[7]), Double.parseDouble(strings2[7]));

        return strings1[0] + "===" + buy + "===" + strings1[2] + "===" + sell + "===" + strings1[4]
                + "===" + average + "===" + strings1[6] + "===" + max + "===" + strings1[8]
                + "===" + strings1[9] + "===" + strings1[10] + "===" + strings1[11];
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
        ArrayList<String> strings = new ArrayList<>(arrayList);
        listsPricePatterns.add(strings);
        maxArraySize = Math.max(strings.size(), maxArraySize);
    }


    private String checkingID(String string) {
        StringBuilder stringOut = new StringBuilder(string);
        String[] strings = stringOut.toString().split("===");

        for (ArrayList<String> stringArrayList : listsPricePatterns) {
            if (strings[11].equals(stringArrayList.get(0))) {
                stringOut.append((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39));
            }
        }
        return stringOut.toString();
    }


    public void seeLists() {
        ConsoleHelper.writeMessage("\n"
                + "Востановленный лист патернов имеет размер --- "
                + listsPricePatterns.size());

        StringBuilder stringBuilder = new StringBuilder();

        for (ArrayList<String> arrayList : listsPricePatterns) {
            ConsoleHelper.writeMessage("\n"
                    + "Размер паттерна --- " + arrayList.size()
                    + "\n");
            for (String string : arrayList) {
                stringBuilder.delete(0, stringBuilder.length());
                stringBuilder.append(string).delete(stringBuilder.length() - "\n".length(), stringBuilder.length());
                ConsoleHelper.writeMessage(stringBuilder.toString());
            }
        }
        ConsoleHelper.writeMessage("");
    }



    /// === INNER CLASSES === ///



    private class SortSize implements Comparator<ArrayList<String>> {
        @Override
        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            double result = o1.size() - o2.size();
            if (result > 0) return 1;
            else if (result < 0) return -1;
            else return 0;
        }
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

        // сер
        try {
            FileOutputStream outputStream = new FileOutputStream(Gasket.getFilesAndPathCreator().getPathPatterns());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(savedPatterns);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(savedPatterns.listsPricePatterns);//
        savedPatterns.listsPricePatterns.clear();
        System.out.println(savedPatterns.listsPricePatterns.size());//
        System.out.println();//


        // десер
        try {
            FileInputStream fileInputStream = new FileInputStream(Gasket.getFilesAndPathCreator().getPathPatterns());
            System.out.println(1);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            System.out.println(2);
            savedPatterns = (SavedPatterns) objectInputStream.readObject();
            System.out.println(3);
            fileInputStream.close();
            System.out.println(4);
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Файл с ПАТТЕРНАМИ пуст или в нем есть ошибка");
            e.printStackTrace();
        }

        if (savedPatterns != null) {
            System.out.println(savedPatterns.listsPricePatterns);
        }

    }
}
