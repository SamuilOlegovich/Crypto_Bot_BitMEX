package bitmex.Bot.model.strategies.II;


import bitmex.Bot.model.CompareHelper;
import bitmex.Bot.model.FilesAndPathCreator;
import bitmex.Bot.model.enums.TimeFrame;
import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;


import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.TreeSet;
import java.io.*;

import static bitmex.Bot.model.CompareHelper.getSortSize;
import static bitmex.Bot.model.enums.TimeFrame.M5;
import static bitmex.Bot.model.enums.TypeData.BIAS;
import static bitmex.Bot.model.enums.TypeData.BUY;


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
        ReadAndSavePatterns.savePureHistoryOfPatternsIn(arrayList);

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                + " --- Сравниваю II ПАТТЕРН с имеющимися");

        // находим количество смещений и запоминаем индекс первого смещения
        ArrayList<String> inArrayList = new ArrayList<>(arrayList);
        int countBias = 0;
        int indexBias = 0;

        for (String string : inArrayList) {
            if (string.startsWith(BIAS.toString())) {
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

            String[] strings = Gasket.getNumberOfHistoryBlocks().split("-");

            for (String sBlock : strings) {
                ArrayList<String> inArrayListCopy = new ArrayList<>(inArrayList);

                int block = Integer.parseInt(sBlock);

                // ограничиваем патерн нужным нам количеством блоков
                if (countBias >= block) {
                    int count = 0;
                    int index = 0;

                    for (int i = inArrayListCopy.size() - 1; i > 0; i--) {
                        if (inArrayListCopy.get(i).startsWith(BIAS.toString())) {
                            count++;
                            if (count == block) {
                                index = i;
                                break;
                            }
                        }
                    }

                    for (int i = index; i > 0; i--) {
                        inArrayListCopy.remove(i);
                    }
                }

                // чистим от оставшихся предварительных исчезнувших уровняй
                ArrayList<Integer> indexArrayList = new ArrayList<>();

                for (String stringOne : inArrayListCopy) {
                    int bias = 0;

                    if (!stringOne.startsWith(BUY.toString()) && !stringOne.startsWith(BIAS.toString())) {
                        String[] oneStrings = stringOne.split(",");
                        String[] twoStrings;

                        for (int i = inArrayListCopy.indexOf(stringOne) + 1; i < inArrayListCopy.size(); i++) {
                            String stringTwo = inArrayList.get(i);

                            bias = bias + (stringTwo.startsWith(BIAS.toString()) ? 1 : 0);

                            if (bias == 1) {
                                // если мы сюда заши то значит мы перешли в нужны нам блок
                                // начинаем сравнения с его строками
                                twoStrings = stringTwo.split(",");

                                if (oneStrings.length == twoStrings.length) {

                                    // эти уровни есть всегда их не надо уничтожать
                                    if (!oneStrings[5].equals("\"type\": \"OI_ZS_MIN_MINUS\"")
                                            && !oneStrings[5].equals("\"type\": \"OI_ZS_MIN_PLUS\"")
                                            && !oneStrings[5].equals("\"type\": \"DELTA_ZS_MIN_PLUS\"")
                                            && !oneStrings[5].equals("\"type\": \"DELTA_ZS_MIN_MINUS\"")) {

                                        // M5 == M5  1 == 1  ASK == ASK
                                        if (oneStrings[0].equals(twoStrings[0])
                                                && oneStrings[1].equals(twoStrings[1])
                                                && oneStrings[5].equals(twoStrings[5])) {
                                            indexArrayList.add(inArrayListCopy.indexOf(stringOne));

                                            // M5 == M5  1 != 1(0)  ASK == ASK
                                        } else if (oneStrings[0].equals(twoStrings[0])
                                                && (!oneStrings[1].equals(twoStrings[1])
                                                && oneStrings[1].equals("1"))
                                                && oneStrings[5].equals(twoStrings[5])) {
                                            indexArrayList.add(inArrayListCopy.indexOf(stringOne));

                                            // M5 == M5  (0)1 != 1  ASK == ASK
                                        } else if (oneStrings[0].equals(twoStrings[0])
                                                && (!oneStrings[1].equals(twoStrings[1])
                                                && oneStrings[1].equals("0"))
                                                && oneStrings[5].equals(twoStrings[5])) {
                                            indexArrayList.add(inArrayListCopy.indexOf(stringTwo));

                                            // M5 != M5(M15)  1 == 1  ASK == ASK
                                        } else if ((!oneStrings[0].equals(twoStrings[0])
                                                && oneStrings[0].equals(M5.toString()))
                                                && oneStrings[1].equals(twoStrings[1])
                                                && oneStrings[5].equals(twoStrings[5])) {
                                            indexArrayList.add(inArrayListCopy.indexOf(stringOne));

                                            // M5 != M5(M15)  1 != 1(0)  ASK == ASK
                                        } else if ((!oneStrings[0].equals(twoStrings[0])
                                                && oneStrings[0].equals(M5.toString()))
                                                && (!oneStrings[1].equals(twoStrings[1])
                                                && oneStrings[1].equals("1"))
                                                && oneStrings[5].equals(twoStrings[5])) {
                                            indexArrayList.add(inArrayListCopy.indexOf(stringOne));

                                            // M5 != M5(M15)  (0)1 != 1  ASK == ASK
                                        } else if ((!oneStrings[0].equals(twoStrings[0])
                                                && oneStrings[0].equals(M5.toString()))
                                                && (!oneStrings[1].equals(twoStrings[1])
                                                && oneStrings[1].equals("0"))
                                                && oneStrings[5].equals(twoStrings[5])) {
                                            indexArrayList.add(inArrayListCopy.indexOf(stringTwo));

                                            // M5 != M5(M15)  (0)1 != 1  ASK == ASK
                                        } else if ((!oneStrings[0].equals(twoStrings[0])
                                                && twoStrings[0].equals(M5.toString()))
                                                && (!oneStrings[1].equals(twoStrings[1])
                                                && oneStrings[1].equals("0"))
                                                && oneStrings[5].equals(twoStrings[5])) {
                                            indexArrayList.add(inArrayListCopy.indexOf(stringTwo));
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
                    inArrayListCopy.remove((int) index);
                }


                // перебираем массив стратегий и сравниваем с пришедшим
                for (ArrayList<String> stringArrayList : listsPricePatterns) {
                    boolean result = true;

                    // проверяем совпадает ли их размер
                    if (stringArrayList.size() == inArrayListCopy.size()) {

                        // если размер совпал то начинаем сравнивать построчно
                        // не считая 0-вой строки так как там инфа о паттерне
                        for (int i = 1; i < inArrayListCopy.size(); i++) {
                            String[] strings1;
                            String[] strings2;
                            String[] arr1;
                            String[] arr2;

                            // Тут мы так же определяем не строка ли это направления и сравниваем либо ее либо строки уровней
                            // BIAS===BUY===10===AVERAGE===3===MAX===5   <----- строка направления
                            if (inArrayList.get(i).startsWith(BIAS.toString())
                                    && stringArrayList.get(i).startsWith(BIAS.toString())) {

                                arr1 = stringArrayList.get(i).split("===");
                                arr2 = inArrayListCopy.get(i).split("===");

                                // если хоть один объект не равен то прирываем цикл
                                if (!arr1[1].equals(arr2[1])) {
                                    result = false;
                                    break;
                                }

                            } else if ((inArrayListCopy.get(i).startsWith(BIAS.toString())
                                    && !stringArrayList.get(i).startsWith(BIAS.toString()))
                                    || (!inArrayListCopy.get(i).startsWith(BIAS.toString())
                                    && stringArrayList.get(i).startsWith(BIAS.toString()))) {

                                // если под одним и тем же номером находятся разные по значимости строки то прирываем цикл
                                result = false;
                                break;
                            } else if (!inArrayListCopy.get(i).startsWith(BIAS.toString())
                                    && !stringArrayList.get(i).startsWith(BIAS.toString())) {

                                arr1 = stringArrayList.get(i).split("\"type\": \"");
                                arr2 = inArrayListCopy.get(i).split("\"type\": \"");
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
                                    + " --- II ПАТТЕРН такой есть - обновляю информацию");
                            String stringZero = setPriority(stringArrayList.get(0), inArrayListCopy.get(0));
                            stringArrayList.set(0, stringZero);

                            ReadAndSavePatterns.saveSavedPatternsFromUser();
                            ReadAndSavePatterns.saveSavedPatterns();
                            return;
                        }
                    }
                }

                // если совпадение не было найдено - добавляем данный патерн в массив
                ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                        + " --- Такого II ПАТТЕРНА нет - ДОБАВЛЕН --- "
                        + "SIZE --- " + inArrayList.size());

                // проверяю есть ли такой айди и если есть меняю его на другой
                inArrayList.set(0, checkingID(inArrayListCopy.get(0)));

                listsPricePatterns.add(0, inArrayListCopy);
                maxArraySize = Math.max(inArrayListCopy.size(), maxArraySize);

                listsPricePatterns.sort(getSortSize());

                ReadAndSavePatterns.saveSavedPatternsFromUser();
                ReadAndSavePatterns.saveSavedPatterns();
            }
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


    public synchronized void updateFirstRowData(String string) {
        String stringSet = string;
        String[] in = stringSet.split("===");
        String[] thIs;

        int count = 0;

        for (ArrayList<String> stringArrayList : listsPricePatterns) {
            thIs = stringArrayList.get(0).split("===");

            if (in.length == thIs.length) {
                if (in[in.length - 1].equals(thIs[thIs.length - 1])) {
                    stringArrayList.set(0, stringSet);
                    count++;
                }
            }
        }

        if (count > 0) {
            ReadAndSavePatterns.saveSavedPatterns();

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- ОБНОВИЛ нулевую стороку II ПАТТЕРНОВ согласно исходу сделки");
        } else {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Такого номера ===" + in[in.length - 1] + "=== ПАТТЕРНА нет");
        }
    }
}
