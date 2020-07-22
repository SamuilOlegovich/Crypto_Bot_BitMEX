package bitmex.Bot.model.strategies.IIUser;

import java.util.HashMap;

public class Martingale {

    private HashMap<String, Integer> hashMap;
    private double martingalePROFIT;
    private int martingaleMaxSteep;

    public Martingale() {
        this.hashMap = new HashMap<>();
        this.martingaleMaxSteep = 0;
        this.martingalePROFIT = 0.0;

    }


    public synchronized int getSteep(String key) {
        return hashMap.get(key);
    }

    public synchronized void upSteep(String key) {
        hashMap.put(key, hashMap.get(key) + 1);
        martingaleMaxSteep = martingaleMaxSteep > (hashMap.get(key) + 1) ? martingaleMaxSteep : hashMap.get(key);
    }

    public synchronized void zeroSteep(String key) {
        hashMap.put(key, 0);
    }

    public double getMartingalePROFIT() {
        return martingalePROFIT;
    }

    public void setMartingalePROFIT(double martingalePROFIT) {
        this.martingalePROFIT = martingalePROFIT;
    }
}
