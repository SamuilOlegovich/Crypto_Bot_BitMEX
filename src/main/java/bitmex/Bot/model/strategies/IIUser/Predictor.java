package bitmex.Bot.model.strategies.IIUser;



import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.enums.TypeData;

import java.util.ArrayList;





public class Predictor extends Thread {
    private ArrayList<String> market;
    private String zeroString;


    public Predictor(ArrayList<String> market, String zeroString) {
        this.market = new ArrayList<>(market);
        this.zeroString = zeroString;
        run();
    }



    @Override
    public void run() {
        // если предсказатель не отключен на этом паттерне,
        // то работаем с ним, если отключен, просто передаем дальше
        if (!StringHelper.giveData(TypeData.PREDICTOR, zeroString).equalsIgnoreCase(TypeData.OFF.toString())) {
            if (StringHelper.giveData(TypeData.PREDICTOR, zeroString).equalsIgnoreCase(TypeData.LOSS.toString())) {
                new MakeDealPredictor(true, market, zeroString);
            } else {
                new MakeDealPredictor(false, market, zeroString);
            }
        } else {
            new MakeDealUser(market, zeroString);
        }
    }
}
