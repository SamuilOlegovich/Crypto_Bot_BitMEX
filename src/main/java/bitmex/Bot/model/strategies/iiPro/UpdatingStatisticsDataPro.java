package bitmex.Bot.model.strategies.iiPro;

import bitmex.Bot.model.Gasket;




public class UpdatingStatisticsDataPro extends Thread {
    private String id;

    public UpdatingStatisticsDataPro(String id) {
        this.id = id;
        start();
    }

    @Override
    public void run() {
        Gasket.getSavedPatternsProClass().updateFirstRowData(id);
    }
}

