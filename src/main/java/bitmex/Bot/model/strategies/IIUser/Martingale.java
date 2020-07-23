package bitmex.Bot.model.strategies.IIUser;

import java.util.HashMap;

public class Martingale {

    private HashMap<String, Integer> hashMap;
    private double martingalePROFIT;
//    private int martingaleMaxSteep;

    public Martingale() {
        this.hashMap = new HashMap<>();
//        this.martingaleMaxSteep = 0;
        this.martingalePROFIT = 0.0;

    }


    public int getSteep(String key) {
//        synchronized (hashMap) {
            return hashMap.get(key);
//        }
    }

    public void upSteep(String key) {
//        synchronized (hashMap) {
            hashMap.put(key, hashMap.get(key) + 1);
//            martingaleMaxSteep = martingaleMaxSteep > (hashMap.get(key) + 1)
//                    ? martingaleMaxSteep : hashMap.get(key);
//        }
    }

    public void downSteep(String key) {
//        synchronized (hashMap) {
            hashMap.put(key, hashMap.get(key) - 1);
//            martingaleMaxSteep = martingaleMaxSteep > (hashMap.get(key) + 1)
//                    ? martingaleMaxSteep : hashMap.get(key);
//        }
    }

    public void zeroSteep(String key) {
//        synchronized (hashMap) {
            hashMap.put(key, 0);
//        }
    }

    public double getMartingalePROFIT() {
        return martingalePROFIT;
    }

    public void setMartingalePROFIT(double martingalePROFIT) {
        this.martingalePROFIT = martingalePROFIT;
    }

    public boolean isThereSuchKey(String key) {
//        synchronized (hashMap) {
            return hashMap.containsKey(key);
//        }
    }

    public void setHashMap(String key, int value) {
//        synchronized (hashMap) {
            hashMap.put(key, value);
//        }
    }
}
