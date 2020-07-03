package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;

import java.util.ArrayList;

import static bitmex.Bot.model.enums.TypeData.BIAS;


public class SavedPatternsUser {
    private static SavedPatternsUser savedPatternsUser;

    private ArrayList<ArrayList<String>> listsPricePatternsUser;
    private int maxArraySizeUser;
    private int maxCountBiasUser;



    private SavedPatternsUser() {
        this.listsPricePatternsUser = new ArrayList<>();
        this.maxArraySizeUser = 0;
        this.maxCountBiasUser = 0;
    }



    public static SavedPatternsUser getInstance() {
        if (savedPatternsUser == null) savedPatternsUser = new SavedPatternsUser();
        return savedPatternsUser;
    }



    // находим и отдаем массивы нужной длины - размера
    public ArrayList<ArrayList<String>> getListFoSize(int size) {
        ArrayList<ArrayList<String>> listFoSize = new ArrayList<>();

        for (ArrayList<String> list : listsPricePatternsUser) {
            if (list.size() == size) {
                listFoSize.add(list);
            }
        }

        if (listFoSize.size() > 0) return listFoSize;
        else return null;
    }



    public void setPatternsInListsPricePatternsUser(ArrayList<String> arrayList) {
        ArrayList<String> strings = new ArrayList<>(arrayList);
        listsPricePatternsUser.add(strings);
        maxArraySizeUser = Math.max(strings.size(), maxArraySizeUser);
        int countBias = 0;

        for (String s : strings) {
            if (s.startsWith(BIAS.toString())) {
                countBias++;
            }
        }
        maxCountBiasUser = Math.max(countBias, maxCountBiasUser);
    }



    public void seeListsUser() {
        ConsoleHelper.writeMessage("\n"
                + "Востановленный лист патернов имеет размер --- "
                + listsPricePatternsUser.size());

        StringBuilder stringBuilder = new StringBuilder();

        for (ArrayList<String> arrayList : listsPricePatternsUser) {
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

        for (ArrayList<String> stringArrayList : listsPricePatternsUser) {
            thIs = stringArrayList.get(0).split("===");

            if (in.length == thIs.length) {
                if (in[in.length - 1].equals(thIs[thIs.length - 1])) {
                    stringArrayList.set(0, stringSet);
                    count++;
                }
            }
        }

        if (count > 0) {
            ReadAndSavePatternsUser.saveSavedPatternsUser();

            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- ОБНОВИЛ нулевую стороку ПАТТЕРНОВ USER согласно исходу сделки");
        } else {
            ConsoleHelper.writeMessage(DatesTimes.getDateTerminal()
                    + " --- Такого номера ===" + in[in.length - 1] + "=== ПАТТЕРНА USER нет");
        }
    }



    // возвращаем самую большую длину имеющегося паттерна
    public int getMaxArraySizeUser() {
        return maxArraySizeUser;
    }


    public ArrayList<ArrayList<String>> getListsPricePatternsUser() {
        return listsPricePatternsUser;
    }


    public int getMaxCountBiasUser() {
        return maxCountBiasUser;
    }
}
