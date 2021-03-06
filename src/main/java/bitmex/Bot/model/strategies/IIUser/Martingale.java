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
    private volatile HashMap<String, Integer> steepTest;
    private volatile HashMap<String, Integer> steepReal;
    private volatile double martingaleRealPROFIT;
    private volatile double martingalePROFIT;
    private volatile int deal = 0;////////////////////////////


    public Martingale() {
        this.volumeForEachStep = new HashMap<>();
        this.steepTest = new HashMap<>();
        this.steepReal = new HashMap<>();
        this.martingaleRealPROFIT = 0.0;
        this.martingalePROFIT = 0.0;
        this.deal = 0;//////////////////////////////////////
        setMartin();
    }



    public int getSteep(String key) {
        synchronized (steepTest) {
            return steepTest.get(key);
        }
    }

    public int getSteepReal(String key) {
        synchronized (steepReal) {
            return steepReal.get(key);
        }
    }

    public void upSteep(String key) {
        synchronized (steepTest) {
            steepTest.put(key, steepTest.get(key) + 1);
        }
    }

    public void upSteepReal(String key) {
        synchronized (steepReal) {
            steepReal.put(key, steepReal.get(key) + 1);
        }
    }

    public void downSteep(String key) {
        synchronized (steepTest) {
            steepTest.put(key, steepTest.get(key) - 1);
        }
    }

    public void downSteepReal(String key) {
        synchronized (steepReal) {
            steepReal.put(key, steepReal.get(key) - 1);
        }
    }

    public void zeroSteep(String key) {
        synchronized (steepTest) {
            steepTest.put(key, 0);
        }
    }

    public void zeroSteepReal(String key) {
        synchronized (steepReal) {
            steepReal.put(key, 0);
        }
    }

    public double getMartingalePROFIT() {
        return martingalePROFIT;
    }

    public double getMartingaleRealPROFIT() {
        return martingaleRealPROFIT;
    }

    public void setMartingalePROFIT(double martingalePROFIT) {
        this.martingalePROFIT = martingalePROFIT;
    }

    public void setMartingaleRealPROFIT(double martingaleRealPROFIT) {
        this.martingaleRealPROFIT = martingaleRealPROFIT;
    }

    public boolean isThereSuchKey(String key) {
        synchronized (steepTest) {
            return steepTest.containsKey(key);
        }
    }

    public boolean isThereSuchKeyReal(String key) {
        synchronized (steepReal) {
            return steepReal.containsKey(key);
        }
    }


    public void setMapSteep(String key, int value) {
        synchronized (steepTest) {
            steepTest.put(key, value);
        }
    }

    public void setMapSteepReal(String key, int value) {
        synchronized (steepReal) {
            steepReal.put(key, value);
        }
    }


    public synchronized void setVolumeForEachStep(String in) {
        String value = StringHelper.giveData(TypeData.LOT, in);
        String key = StringHelper.giveData(TypeData.ID, in);
        volumeForEachStep.put(key, value);
    }


    private synchronized void setMartin() {
        ArrayList<ArrayList<String>> arrayLists = Gasket.getSavedPatternsUserClass().getListsPricePatternsUser();
        for (ArrayList<String> a : arrayLists) {
            String key = StringHelper.giveData(TypeData.ID, a.get(0).replaceAll("\n", ""));
            String value = StringHelper.giveData(TypeData.LOT, a.get(0));
            volumeForEachStep.put(key, value);
        }
    }


    public String showSteps() {
        StringBuilder stringBuilder = new StringBuilder("\n\nMartingale STEEP\n");
        stringBuilder.append("   ===*** " + TypeData.TEST.toString() + " ***===\n");

        if (steepTest.size() > 0) {
            for (Map.Entry entry : steepTest.entrySet()) {
                stringBuilder.append("ID===" + entry.getKey() + "===STEEP===" + entry.getValue() + "\n");
            }
        }

        stringBuilder.append("\n\n   ===*** " + TypeData.REAL.toString() + " ***===\n");

        if (steepReal.size() > 0) {
            for (Map.Entry entry : steepReal.entrySet()) {
                stringBuilder.append("ID===" + entry.getKey() + "===STEEP===" + entry.getValue() + "\n");
            }
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

////////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized int getDeal() {
        return deal;
    }

    public synchronized void setDeal(int deal) {
        this.deal = deal;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
