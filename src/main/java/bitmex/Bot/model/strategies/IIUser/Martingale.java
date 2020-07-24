package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.StringHelper;

import java.util.HashMap;




// класс хранит айди и шаг в данный момент, если сделка окончена шаг равен - 0
public class Martingale {

    private volatile HashMap<String, String> volumeForEachStep; // все значения следующих шагов
    private volatile HashMap<String, Integer> hashMap;
    private double martingalePROFIT;

    public Martingale() {
        this.volumeForEachStep = new HashMap<>();
        this.hashMap = new HashMap<>();
        this.martingalePROFIT = 0.0;
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

    public synchronized void setMartingalePROFIT(double martingalePROFIT) {
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
        String value = StringHelper.giveData(TypeData.LOT, in);
        String key = StringHelper.giveData(TypeData.ID, in);
        volumeForEachStep.put(key, value);
    }



    public synchronized Double getLotForThisSteep(String key, int steep) {
        String[] strings = volumeForEachStep.get(key).split("\\*");
        return Double.parseDouble(strings[steep - 1]);
    }
}
