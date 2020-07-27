package bitmex.Bot.model.strategies.IIUser;

import java.util.ArrayList;
import java.util.HashMap;


// класс хранит айди и объем открытой сделки, если все закрыто - то объем равен 0.0
public class OpenTransactions {
    private volatile HashMap<String, ArrayList<Double>> mapRealAllLots;
    private volatile HashMap<String, ArrayList<Double>> mapTestAllLots;
    private volatile HashMap<String, Double> mapReal;
    private volatile HashMap<String, Double> mapTest;

    public OpenTransactions() {
        this.mapReal = new HashMap<>();
        this.mapTest = new HashMap<>();
    }



    public Double getOrderVolumeTest(String key) {
        return mapTest.get(key);
    }

    public void zeroSteepTest(String key) {
        mapTest.put(key, 0.0);
    }

    public boolean isThereSuchKeyTest(String key) {
        return mapTest.containsKey(key);
    }

    public void setMapTest(String key, Double value) {
        mapTest.put(key, value);
    }

    public void setMapReal(String key, Double value) {
        mapReal.put(key, value);
    }
}
