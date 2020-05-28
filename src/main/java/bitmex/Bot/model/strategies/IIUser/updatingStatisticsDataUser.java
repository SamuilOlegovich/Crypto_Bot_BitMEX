package bitmex.Bot.model.strategies.IIUser;

import bitmex.Bot.model.Gasket;

public class updatingStatisticsDataUser extends Thread {
    private String id;

    public updatingStatisticsDataUser(String id) {
        this.id = id;
        start();
    }

    @Override
    public void run() {
        Gasket.getSavedPatternsUserClass().updateFirstRowData(id);
    }
}
