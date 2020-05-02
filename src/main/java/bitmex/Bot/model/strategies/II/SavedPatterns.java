package bitmex.Bot.model.strategies.II;

import java.util.ArrayList;

public class SavedPatterns {
    private static ArrayList<ArrayList<String>> listsPricePatterns = new ArrayList<>();


    public static ArrayList<ArrayList<String>> getListsPricePatterns() {
        return listsPricePatterns;
    }


    public static void addListsPricePatterns(ArrayList<String> listsPricePatterns) {
        SavedPatterns.isThereSuchCombination(listsPricePatterns);
    }


    // ищем есть ли такие патерны если нет то добавляем,
    // если есть то устанавливаем приоритет
    private static void isThereSuchCombination(ArrayList<String> arrayList) {

        // перебираем массив стратегий и сравниваем с пришедшим
        for (ArrayList<String> stringArrayList : SavedPatterns.listsPricePatterns) {
            boolean result = true;

            // проверяем совпадает ли их размер
            if (stringArrayList.size() == arrayList.size()) {

                // если размер совпал то начинаем сравнивать построчно
                // не считая 0-вой строки так как там инфа о паттерне
                for (int i = 1; i < arrayList.size(); i++) {
                    String[] arr1 = stringArrayList.get(i).split("\"preview\": \"");
                    String[] arr2 = arrayList.get(i).split("\"preview\": \"");
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
                    stringArrayList.add(0, SavedPatterns.setPriority(stringArrayList.get(0), arrayList.get(0)));
                    return;
                }
            }
        }

        // если совпадение не было найдено - добавляем данный патерн в массив
        SavedPatterns.listsPricePatterns.add(arrayList);

    }


    // обновляем информационные данные в строке информации
    private static String setPriority(String s1, String s2) {
        // распарсили строки
        String[] strings1 = s1.split("===");
        String[] strings2 = s2.split("===");

        // спарсили числа и прибавили их
        int sell = Integer.parseInt(strings1[3]) + Integer.parseInt(strings2[3]);
        int buy = Integer.parseInt(strings1[1]) + Integer.parseInt(strings2[1]);

        // вернули итоговую инфо строку
        return  strings1[0] + "===" + buy + "===" + strings1[2] + "===" + sell;
    }
}
