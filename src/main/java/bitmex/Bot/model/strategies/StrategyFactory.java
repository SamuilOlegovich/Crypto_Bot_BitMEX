package bitmex.Bot.model.strategies;


import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.strategies.oneStrategies.*;
import bitmex.Bot.model.Gasket;



public class StrategyFactory {
    private static StrategyFactory strategyFactory;
    private OneSell_4 oneSell4;
    private OneSell_3 oneSell3;
    private OneSell_2 oneSell2;
    private OneBuy_4 oneBuy4;
    private OneBuy_3 oneBuy3;
    private OneBuy_2 oneBuy2;
    private OneSell oneSell;
    private OneBuy oneBuy;


    private StrategyFactory() {
    }

    public static StrategyFactory getInstance() {
        if (strategyFactory == null) strategyFactory = new StrategyFactory();
        return strategyFactory;
    }


    public void onOff(InfoIndicator infoIndicator) {
        if (Gasket.isOb()) {
            if (oneBuy == null) {
                oneBuy = OneBuy.getInstance();
                oneBuy.setIInfoString(infoIndicator);
            } else {
                oneBuy.setIInfoString(infoIndicator);
            }

            if (oneSell == null) {
                oneSell = OneSell.getInstance();
                oneSell.setIInfoString(infoIndicator);
            } else {
                oneSell.setIInfoString(infoIndicator);
            }
        }

        if (Gasket.isOb_2()) {
            if (oneBuy2 == null) {
                oneBuy2 = OneBuy_2.getInstance();
                oneBuy2.setIInfoString(infoIndicator);
            } else {
                oneBuy2.setIInfoString(infoIndicator);
            }

            if (oneSell2 == null) {
                oneSell2 = OneSell_2.getInstance();
                oneSell2.setIInfoString(infoIndicator);
            } else {
                oneSell2.setIInfoString(infoIndicator);
            }
        }

        if (Gasket.isOb_3()) {
            if (oneBuy3 == null) {
                oneBuy3 = OneBuy_3.getInstance();
                oneBuy3.setIInfoString(infoIndicator);
            } else {
                oneBuy3.setIInfoString(infoIndicator);
            }

            if (oneSell3 == null) {
                oneSell3 = OneSell_3.getInstance();
                oneSell3.setIInfoString(infoIndicator);
            } else {
                oneSell3.setIInfoString(infoIndicator);
            }
        }

        if (Gasket.isOb_2()) {
            if (oneSell4 == null) {
                oneSell4 = OneSell_4.getInstance();
                oneSell4.setIInfoString(infoIndicator);
            } else {
                oneSell4.setIInfoString(infoIndicator);
            }

            if (oneBuy4 == null) {
                oneBuy4 = OneBuy_4.getInstance();
                oneBuy4.setIInfoString(infoIndicator);
            } else {
                oneBuy4.setIInfoString(infoIndicator);
            }
        }
    }
}
