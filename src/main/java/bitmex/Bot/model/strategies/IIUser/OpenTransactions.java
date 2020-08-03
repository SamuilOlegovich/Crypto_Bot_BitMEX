package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.Gasket;

import java.util.ArrayList;
import java.util.HashMap;


// класс хранит айди и объем открытой сделки, если все закрыто - то объем равен 0.0
public class OpenTransactions {
//    private volatile HashMap<String, ArrayList<Double>> mapRealAllLots;
//    private volatile HashMap<String, ArrayList<Double>> mapTestAllLots;
    private volatile HashMap<String, Double> mapReal;
    private volatile HashMap<String, Double> mapTest;

    public OpenTransactions() {
        this.mapReal = new HashMap<>();
        this.mapTest = new HashMap<>();
    }



    public synchronized Double getOrderVolumeTest(String key) {
        return mapTest.get(key);
    }

    public synchronized Double getOrderVolumeReal(String key) {
        return mapReal.get(key);
    }

    public synchronized void zeroSteepTest(String key) {
        mapTest.put(key, 0.0);
    }

    public synchronized void zeroSteepReal(String key) {
        mapReal.put(key, 0.0);
    }

    public boolean isThereSuchKeyTest(String key) {
        return mapTest.containsKey(key);
    }

    public boolean isThereSuchKeyReal(String key) {
        return mapReal.containsKey(key);
    }

    public synchronized void setMapTest(String key, Double value) {
        mapTest.put(key, value);
        try {
            Thread.sleep(Gasket.getSECOND());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setMapReal(String key, Double value) {
        mapReal.put(key, value);
        try {
            Thread.sleep(Gasket.getSECOND());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

