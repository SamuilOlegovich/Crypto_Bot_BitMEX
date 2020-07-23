package bitmex.Bot.model.strategies.IIUser;

import java.util.HashMap;

public class OpenTransactions {

    private HashMap<String, Double> hashMap;

    public OpenTransactions() {
        this.hashMap = new HashMap<>();

    }

    public Double getOrderVolume(String key) {
        return hashMap.get(key);
    }

    public void upSteep(String key) {
        hashMap.put(key, hashMap.get(key) + 1);
    }

    public void zeroSteep(String key) {
        hashMap.put(key, 0.0);
    }

    public boolean isThereSuchKey(String key) {
        return hashMap.containsKey(key);
    }

    public void setHashMap(String key, Double value) {
        hashMap.put(key, value);
    }
}

