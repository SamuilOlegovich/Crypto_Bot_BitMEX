package bitmex.Bot.model.strategies.IIUser;

public class updatingStatisticsDataUser extends Thread {
    private String id;

    public updatingStatisticsDataUser(String id) {
        this.id = id;
        start();
    }

    @Override
    public void run() {

    }

}
