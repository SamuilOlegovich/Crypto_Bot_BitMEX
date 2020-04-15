package bitmex.Bot.model.strategies;


import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.strategies.oneStrategies.*;
//import com.sumzerotrading.bitmex.model.serverAndParser.strategies.oneStrategies.*;

public class StrategyFactory {
    private static StrategyFactory strategyFactory;
    private StrategyOneSellTime strategyOneSellStrictlyConsistentInTime;
    private StrategyOneBuyTime strategyOneBuyStrictlyConsistentInTime;
    private StrategyOneSellRange strategyOneSellRange;
    private StrategyOneBuyRange strategyOneBuyRange;
    private StrategyOneSell strategyOneSell;
    private StrategyOneBuy strategyOneBuy;
    private OneSell oneSell;
    private OneBuy oneBuy;


    private StrategyFactory() {
    }

    public static StrategyFactory getInstance() {
        if (strategyFactory == null) strategyFactory = new StrategyFactory();
        return strategyFactory;
    }


    public void onOff(InfoIndicator infoIndicator) {
        if (Gasket.isOne()) {
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

        if (Gasket.isStrategyOne()) {
            if (strategyOneBuy == null) {
                strategyOneBuy = StrategyOneBuy.getInstance();
                strategyOneBuy.setIInfoString(infoIndicator);
            } else {
                strategyOneBuy.setIInfoString(infoIndicator);
            }

            if (strategyOneSell == null) {
                strategyOneSell = StrategyOneSell.getInstance();
                strategyOneSell.setIInfoString(infoIndicator);
            } else {
                strategyOneSell.setIInfoString(infoIndicator);
            }
        }

        if (Gasket.isStrategyOneRange()) {
            if (strategyOneBuyRange == null) {
                strategyOneBuyRange = StrategyOneBuyRange.getInstance();
                strategyOneBuyRange.setIInfoString(infoIndicator);
            } else {
                strategyOneBuyRange.setIInfoString(infoIndicator);
            }

            if (strategyOneSellRange == null) {
                strategyOneSellRange = StrategyOneSellRange.getInstance();
                strategyOneSellRange.setIInfoString(infoIndicator);
            } else {
                strategyOneSellRange.setIInfoString(infoIndicator);
            }
        }

        if (Gasket.isStrategyOne()) {
            if (strategyOneSellStrictlyConsistentInTime == null) {
                strategyOneSellStrictlyConsistentInTime = StrategyOneSellTime.getInstance();
                strategyOneSellStrictlyConsistentInTime.setIInfoString(infoIndicator);
            } else {
                strategyOneSellStrictlyConsistentInTime.setIInfoString(infoIndicator);
            }

            if (strategyOneBuyStrictlyConsistentInTime == null) {
                strategyOneBuyStrictlyConsistentInTime = StrategyOneBuyTime.getInstance();
                strategyOneBuyStrictlyConsistentInTime.setIInfoString(infoIndicator);
            } else {
                strategyOneBuyStrictlyConsistentInTime.setIInfoString(infoIndicator);
            }
        }
    }
}
