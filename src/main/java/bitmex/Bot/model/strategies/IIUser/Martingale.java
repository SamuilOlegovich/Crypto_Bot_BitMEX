package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.Gasket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




// класс хранит айди и шаг в данный момент, если сделка окончена шаг равен - 0
public class Martingale {

    private volatile HashMap<String, String> volumeForEachStep;     // все значения следующих шагов
//    private volatile HashMap<String, Integer> volumeMaxStep;    // значение максимально разрешенного шага по данной стратегии
    private volatile HashMap<String, Integer> hashMap;
    private volatile double martingalePROFIT;

    public Martingale() {
        this.volumeForEachStep = new HashMap<>();
        this.hashMap = new HashMap<>();
        this.martingalePROFIT = 0.0;
        setMartin();
    }



    public int getSteep(String key) {
        synchronized (hashMap) {
            return hashMap.get(key);
        }
    }

    public void upSteep(String key) {
        synchronized (hashMap) {
            hashMap.put(key, hashMap.get(key) + 1);
        }
    }

    public void downSteep(String key) {
        synchronized (hashMap) {
            hashMap.put(key, hashMap.get(key) - 1);
        }
    }

    public void zeroSteep(String key) {
        synchronized (hashMap) {
            hashMap.put(key, 0);
        }
    }

    public double getMartingalePROFIT() {
        return martingalePROFIT;
    }

    public void setMartingalePROFIT(double martingalePROFIT) {
        this.martingalePROFIT = martingalePROFIT;
    }

    public boolean isThereSuchKey(String key) {
        synchronized (hashMap) {
            return hashMap.containsKey(key);
        }
    }


    public void setHashMap(String key, int value) {
        synchronized (hashMap) {
            hashMap.put(key, value);
        }
    }


    public synchronized void setVolumeForEachStep(String in) {
//        Integer steep = Integer.parseInt(StringHelper.giveData(TypeData.STEEP, in));
        String value = StringHelper.giveData(TypeData.LOT, in);
        String key = StringHelper.giveData(TypeData.ID, in);
        volumeForEachStep.put(key, value);
//        volumeMaxStep.put(key, steep);
    }


    private synchronized void setMartin() {
        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsUserClass().getListsPricePatternsUser();
        for (ArrayList<String> a : arrayLists) {
            String value = StringHelper.giveData(TypeData.LOT, a.get(0).replaceAll("\n", ""));
            String key = StringHelper.giveData(TypeData.ID, a.get(0).replaceAll("\n", ""));
            volumeForEachStep.put(key, value);
        }
    }


    public String showSteps() {
        StringBuilder stringBuilder = new StringBuilder("\n\nMartingale STEEP\n");
        for (Map.Entry entry : hashMap.entrySet()) {
            stringBuilder.append("ID===" + entry.getKey() + "===STEEP===" + entry.getValue() + "\n");
        }
        return stringBuilder.toString() + "\n\n";
    }



    public synchronized Double getLotForThisSteep(String key, int steep) {
        String[] strings = volumeForEachStep.get(key).split("\\*");
        return steep - 1 < strings.length ? Double.parseDouble(strings[steep - 1]) : null;
    }



    public synchronized String showVolumeForEachStep() {
        StringBuilder stringBuilder = new StringBuilder("\n\nMartingale Settings\n");
        for (Map.Entry entry : volumeForEachStep.entrySet()) {
            stringBuilder.append("ID===" + entry.getKey() + "===LOT===" + entry.getValue() + "\n");
        }
        return stringBuilder.toString() + "\n\n";
    }
}
