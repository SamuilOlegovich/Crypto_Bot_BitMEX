package bitmex.Bot.model;


import java.util.TreeMap;

// класс для хранения информации по разным таймфреймам
public class TimeIntervals {
    private TreeMap<Integer, Integer> treeMap;

    public TimeIntervals() {
        this.treeMap = new TreeMap<>();
        this.treeMap.put(1, 5);
        this.treeMap.put(5, 15);
        this.treeMap.put(15, 30);
    }

    public int getTime() {
        return treeMap.get(Gasket.getTimeIntervals());
    }
}
