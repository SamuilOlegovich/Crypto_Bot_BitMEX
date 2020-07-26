package bitmex.Bot.model.strategies.iiPro;

import bitmex.Bot.model.CompareHelper;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.strategies.II.ReadAndSavePatterns;

import java.util.ArrayList;

import static bitmex.Bot.model.Gasket.getLevelsToCompare;
import static bitmex.Bot.view.ConsoleHelper.writeMessage;
import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.*;
import static bitmex.Bot.model.CompareHelper.*;




public class SavedPatternsPro {

    private static SavedPatternsPro savedPatternsPro;

    private ArrayList<ArrayList<String>> listsPricePatterns;
    private String[] levelsToCompare;
    private int maxArraySize;



    private SavedPatternsPro() {
        this.levelsToCompare = getLevelsToCompare().split("-");
        this.listsPricePatterns = new ArrayList<>();
        this.maxArraySize = 0;
    }



    public static SavedPatternsPro getInstance() {
        if (savedPatternsPro == null) {
            savedPatternsPro = new SavedPatternsPro();
        }
        return savedPatternsPro;
    }



    public synchronized void addListsPricePatterns(ArrayList<String> listsPricePatterns) {
        isThereSuchCombination(listsPricePatterns);
    }



    // ищем есть ли такие патерны если нет то добавляем,
    // если есть то устанавливаем приоритет
    private synchronized void isThereSuchCombination(ArrayList<String> arrayList) {
        writeMessage(DatesTimes.getDateTerminal()
                + " --- Сравниваю iiPRO ПАТТЕРН с имеющимися");

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
                ArrayList<String> marketListCopy = new ArrayList<>(inArrayList);

                int block = Integer.parseInt(sBlock);

                // ограничиваем патерн нужным нам количеством блоков
                if (countBias >= block) {
                    int count = 0;
                    int index = 0;

                    for (int i = marketListCopy.size() - 1; i > 0; i--) {
                        if (marketListCopy.get(i).startsWith(BIAS.toString())) {
                            count++;
                            if (count == block) {
                                index = i;
                                break;
                            }
                        }
                    }

                    for (int i = index; i > 0; i--) {
                        marketListCopy.remove(i);
                    }
                }

                // чистим от оставшихся предварительных исчезнувших уровняй
                ArrayList<String> temporaryList = new ArrayList<>(removeExtraLevels(marketListCopy));
                marketListCopy.clear();
                marketListCopy.addAll(temporaryList);
                temporaryList.clear();

                // перебираем массив стратегий и сравниваем с пришедшим
                for (ArrayList<String> patternListIn : listsPricePatterns) {

                    ArrayList<String> patternBias = new ArrayList<>();
                    ArrayList<String> marketBias = new ArrayList<>();

                    boolean result = true;

                    // проверяем совпадает ли их размер
                    if (patternListIn.size() == marketListCopy.size()) {

                        for (String pattern : patternListIn) {
                            if (pattern.startsWith(BIAS.toString())) {
                                patternBias.add(pattern);
                            }
                        }

                        for (String market : marketListCopy) {
                            if (market.startsWith(BIAS.toString())) {
                                marketBias.add(market);
                            }
                        }

                        if (marketBias.size() == patternBias.size()) {
                            for (String market : marketBias) {
                                String pattern = patternBias.get(marketBias.indexOf(market));

                                if (!giveData(BIAS, market).equals(giveData(BIAS, pattern))) {
                                      result = false;
                                }
                            }
                        } else {
                            result = false;
                        }


                        if (result) {
                            ArrayList<String> patternList = new ArrayList<>();
                            ArrayList<String> marketList = new ArrayList<>();

                            for (String pattern : patternListIn) {
                                if (!pattern.startsWith(BIAS.toString()) && !pattern.startsWith(BUY.toString())
                                        && !pattern.startsWith(NULL.toString())) {
                                    patternList.add(pattern);
                                }
                            }

                            for (String market : marketListCopy) {
                                if (!market.startsWith(BIAS.toString()) && !market.startsWith(BUY.toString())
                                        && !market.startsWith(NULL.toString())) {
                                    marketList.add(market);
                                }
                            }

                            if (!compareLineSheets(marketList, patternList)) {
                                result = false;
                            }
                        }

                        // если цикл не прерван и флаг TRUE то корректируем инфо данные о патерне
                        // с учетом информации пришедшего паттерна
                        // а так же прекращаем процесс поиска и сравнения
                        if (result) {
                            String stringZero = setPriority(patternListIn.get(0), marketListCopy.get(0));
                            patternListIn.set(0, stringZero);

                            writeMessage(DatesTimes.getDateTerminal()
                                    + " --- iiPRO ПАТТЕРН такой есть - обновляю информацию по === "
                                    + giveData(ID, stringZero));
                            savedPatterns();
                            return;
                        }
                    }
                }

                // если совпадение не было найдено - добавляем данный патерн в массив
                writeMessage(DatesTimes.getDateTerminal()
                        + " --- Такого iiPRO ПАТТЕРНА нет - ДОБАВЛЕН --- "
                        + "SIZE --- " + inArrayList.size());

                // проверяю есть ли такой айди и если есть меняю его на другой
                inArrayList.set(0, checkingID(marketListCopy.get(0)));

                listsPricePatterns.add(0, marketListCopy);
                maxArraySize = Math.max(marketListCopy.size(), maxArraySize);

                listsPricePatterns.sort(getSortSize());
                savedPatterns();

            }
        } else {
            inArrayList.clear();
        }
    }

    private void savedPatterns() {
        ReadAndSavePatternsPro.saveSavedTrimmedPatternsFromUser();
        ReadAndSavePatternsPro.saveSavedPatternsFromUser();
        ReadAndSavePatternsPro.saveSavedPatterns();
    }



    private boolean compareLineSheets(ArrayList<String> marketListIn, ArrayList<String> patternListIn) {
        ArrayList<String> patternCompare = new ArrayList<>();
        ArrayList<String> marketCompare = new ArrayList<>();
        ArrayList<String> patternAll = new ArrayList<>();
        ArrayList<String> marketAll = new ArrayList<>();

        // начинаем перебирать блоки строк и сравнивать их друг с другом
        for (String stringMarket : marketListIn) {
            String stringPattern = patternListIn.get(marketListIn.indexOf(stringMarket));
            boolean flag = true;

            for (String string : levelsToCompare) {
                if (giveData(type, stringMarket).equals(giveData(type, stringPattern))
                        && giveData(type, stringMarket).equals(string)) {
                    patternCompare.add(stringPattern);
                    marketCompare.add(stringMarket);
                    flag = false;
                    break;
                }
            }

            if (flag) {
                patternAll.add(stringPattern);
                marketAll.add(stringMarket);
            }

        }

        // эти уровни без разницы как стоят, потому их сортируем по алфавиту
        patternAll.sort(getSortTheAlphabet());
        marketAll.sort(getSortTheAlphabet());

        // а эти должны быть в четкой последовательности
        for (String pattern : patternCompare) {
            String market = marketCompare.get(patternCompare.indexOf(pattern));
            if (!CompareHelper.finallyComparisonForPro(market, pattern)) {
//            if (!finallyComparisonOnAllData(market, pattern)) {
                return false;
            }
        }

        for (String pattern : patternAll) {
            String market = marketAll.get(patternAll.indexOf(pattern));
            if (!CompareHelper.finallyComparisonForPro(market, pattern)) {
//            if (!finallyComparisonOnAllData(market, pattern)) {
                return false;
            }
        }
        return true;
    }



//    private boolean finallyComparisonOnAllData(String marketString, String patternString) {
//
//        if (!giveData(type, marketString).equals(giveData(type, patternString))) {
//            return false;
//        }
//
//        // направление свечи сравниваем только на избранных уровнях, на остальных это не важно
//        for (String string : levelsToCompare) {
//            if (string.equals(giveData(type, marketString)) && string.equals(giveData(type, patternString))) {
//                if (!giveData(dir, marketString).equals(giveData(dir, patternString))) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }



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

        for (ArrayList<String> stringArrayList : listsPricePatterns) {
            if (StringHelper.giveData(ID, stringOut.toString()).equals(stringArrayList.get(0))) {
                stringOut.append((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39));
            }
        }
        return stringOut.toString();
    }



    public void seeLists() {
        writeMessage("\n"
                + "Востановленный лист патернов имеет размер --- "
                + listsPricePatterns.size());

        StringBuilder stringBuilder = new StringBuilder();

        for (ArrayList<String> arrayList : listsPricePatterns) {
            writeMessage("\n"
                    + "Размер паттерна --- " + arrayList.size()
                    + "\n");
            for (String string : arrayList) {
                stringBuilder.delete(0, stringBuilder.length());
                stringBuilder.append(string).delete(stringBuilder.length() - "\n".length(), stringBuilder.length());

                writeMessage(stringBuilder.toString());
            }
        }
        writeMessage("");
    }



    public synchronized void updateFirstRowData(String string) {
        int count = 0;

        for (ArrayList<String> stringArrayList : listsPricePatterns) {
            if (StringHelper.giveData(ID, string).equals(StringHelper.giveData(ID, stringArrayList.get(0)))) {
                stringArrayList.set(0, string);
                count++;
            }
        }

        if (count > 0) {
            ReadAndSavePatternsPro.saveSavedPatterns();

            writeMessage(DatesTimes.getDateTerminal()
                    + " --- ОБНОВИЛ нулевую стороку iiPRO ПАТТЕРНОВ согласно исходу сделки === "
                    + StringHelper.giveData(ID, string));

        } else {

            writeMessage(DatesTimes.getDateTerminal()
                    + " --- Такого номера ===" + StringHelper.giveData(ID, string) + "=== iiPRO ПАТТЕРНА нет");
        }
    }
}
