package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.view.ConsoleHelper;

import java.util.ArrayList;

public class SavedPatternsUser {
    private static SavedPatternsUser savedPatternsUser;

    private ArrayList<ArrayList<String>> listsPricePatternsUser;
    private int maxArraySizeUser;


    private SavedPatternsUser() {
        this.listsPricePatternsUser = new ArrayList<>();
        this.maxArraySizeUser = 0;
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

    // возвращаем самую большую длину имеющегося паттерна
    public int getMaxArraySizeUser() {
        return maxArraySizeUser;
    }


    public void setPatternsInListsPricePatternsUser(ArrayList<String> arrayList) {
        ArrayList<String> strings = new ArrayList<>(arrayList);
        listsPricePatternsUser.add(strings);
        maxArraySizeUser = Math.max(strings.size(), maxArraySizeUser);
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
}
