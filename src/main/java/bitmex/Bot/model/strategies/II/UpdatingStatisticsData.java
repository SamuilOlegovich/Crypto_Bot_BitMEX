package bitmex.Bot.model.strategies.II;

import bitmex.Bot.model.Gasket;

public class UpdatingStatisticsData extends Thread {
    private String id;

    public UpdatingStatisticsData(String id) {
        this.id = id;
        start();
    }

    @Override
    public void run() {
        Gasket.getSavedPatternsClass().updateFirstRowData(id);
    }
}
