package bitmex.Bot.model.serverAndParser.strategies.oneStrategies;

import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.Gasket;


public class StrategyOneSellStrictlyConsistentInTime {
    private static StrategyOneSellStrictlyConsistentInTime strategyOneSellStrictlyConsistentInTime;

    private InfoIndicator maxOpenInterestMinus;
    private InfoIndicator openInterestPlus;
    private InfoIndicator maxDeltaMinus;
    private InfoIndicator maxDeltaPlus2;
    private InfoIndicator maxDeltaPlus;
    private InfoIndicator deltaPlus;
    private InfoIndicator volume;
    private InfoIndicator ask;


    private int countDelta = 0;


    private StrategyOneSellStrictlyConsistentInTime() {
    }

    public static StrategyOneSellStrictlyConsistentInTime getInstance() {
        if (strategyOneSellStrictlyConsistentInTime == null) strategyOneSellStrictlyConsistentInTime
                = new StrategyOneSellStrictlyConsistentInTime();
        return strategyOneSellStrictlyConsistentInTime;
    }

    public void setIInfoString(InfoIndicator iInfoIndicator) {
        switch (iInfoIndicator.getType()) {
            case VOLUME:
                volume = iInfoIndicator;
                break;
            case DELTA_ASK:
                deltaPlus = iInfoIndicator;
                break;
            case ASK:
                ask = iInfoIndicator;
                break;
            case OPEN_POS_MINUS_HL:
                maxOpenInterestMinus = iInfoIndicator;
                break;
            case DELTA_ASK_HL:
                twoDelta(iInfoIndicator);
                break;
            case OPEN_POS_PLUS:
                openInterestPlus = iInfoIndicator;
                break;
            case DELTA_BID_HL:
                maxDeltaMinus = iInfoIndicator;
                break;
        }
        makeADecision();
    }

    // принимаем решение
    private void makeADecision() {
        if (volume == null || ask == null || deltaPlus == null || maxOpenInterestMinus == null
                || maxDeltaPlus == null || openInterestPlus == null || maxDeltaPlus2 == null
                || maxDeltaMinus == null ) {
            return;
        }

        if (inTheRangePrice() && inTheRangeTime() /*&& isReal()*/) {
            if (Gasket.getStrategeWorkOne() == 1) {
                if (Gasket.isStrategyOneAllFLAG()) {
                    Gasket.setStrategyOneAllFLAG(false);
                    new StrategyOneSellThread(
                            ((int)(Math.round(Math.abs(Math.random()*200 - 100)) * 39)) + "-SOSSCT", volume, ask);
                }
            } else if (Gasket.getStrategeWorkOne() == 2) {
                if (Gasket.isStrategyOneSellFLAG()) {
                    Gasket.setStrategyOneSellFLAG(false);
                    new StrategyOneSellThread(
                            ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39)) + "-SOSSCT", volume, ask);
                }
            }
            maxOpenInterestMinus = null;
            openInterestPlus = null;
            maxDeltaMinus = null;
            maxDeltaPlus2 = null;
            maxDeltaPlus = null;
            deltaPlus = null;
            volume = null;
            ask = null;
        }
    }

    // проверяем вписываемся ли в диапазон цен
    private boolean inTheRangePrice() {
        double topLevel = volume.getPrice() > ask.getPrice()
                ? volume.getPrice() + Gasket.getRangeLivel() : ask.getPrice() + Gasket.getRangeLivel();

        return (maxOpenInterestMinus.getPrice() >= volume.getPrice() && maxOpenInterestMinus.getPrice() <= topLevel)
                && (openInterestPlus.getPrice() >= volume.getPrice() && openInterestPlus.getPrice() <= topLevel)
                && (maxDeltaMinus.getPrice() >= volume.getPrice() && maxDeltaMinus.getPrice() <= topLevel)
                && (maxDeltaPlus2.getPrice() >= volume.getPrice() && maxDeltaPlus2.getPrice() <= topLevel)
                && (maxDeltaPlus.getPrice() >= volume.getPrice() && maxDeltaPlus.getPrice() <= topLevel)
                && (deltaPlus.getPrice() >= volume.getPrice() && deltaPlus.getPrice() <= topLevel);
    }

    // проверяем нет ли тут предварительных уровней
    private boolean isReal() {
        return volume.getPreview() + ask.getPreview() + maxOpenInterestMinus.getPreview() + openInterestPlus.getPreview()
                + maxDeltaMinus.getPreview() + maxDeltaPlus.getPreview() + maxDeltaPlus2.getPreview()
                + deltaPlus.getPreview() == 0;
    }

    private void twoDelta(InfoIndicator iInfoIndicator) {
        if (countDelta == 0) {
            maxDeltaPlus = iInfoIndicator;
            countDelta = 1;
        } else {
            maxDeltaPlus2 = iInfoIndicator;
            countDelta = 0;
        }
    }

    // проверяем входим ли в диапазон по датам событий
    private boolean inTheRangeTime() {
//        return (openInterestPlus.getTime().getTime() <= maxDeltaPlus.getTime().getTime())
//                && (maxDeltaPlus.getTime().getTime() <= maxOpenInterestMinus.getTime().getTime())
//                && (maxDeltaPlus2.getTime().getTime() <= openInterestPlus.getTime().getTime())
//                && (maxDeltaMinus.getTime().getTime() <= maxDeltaPlus.getTime().getTime())
//                && (maxOpenInterestMinus.getTime().getTime() <= ask.getTime().getTime())
//                && (deltaPlus.getTime().getTime() <= volume.getTime().getTime())
//                && (volume.getTime().getTime() >= deltaPlus.getTime().getTime())
//                && (ask.getTime().getTime() <= deltaPlus.getTime().getTime());
        return true;
    }
}
