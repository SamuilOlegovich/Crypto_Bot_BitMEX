package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.Gasket;





public class UpdatingStatisticsDataUser extends Thread {
    private String id;

    public UpdatingStatisticsDataUser(String id) {
        this.id = id;
        start();
    }

    @Override
    public void run() {
        Gasket.getSavedPatternsUserClass().updateFirstRowData(id);
    }
}
