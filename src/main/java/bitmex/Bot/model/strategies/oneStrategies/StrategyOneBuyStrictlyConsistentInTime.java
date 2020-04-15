package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.Gasket;

public class StrategyOneBuyStrictlyConsistentInTime {
    private static StrategyOneBuyStrictlyConsistentInTime strategyOneBuyStrictlyConsistentInTime;

    private InfoIndicator maxOpenInterestMinus;
    private InfoIndicator maxOpenInterestPlus;
    private InfoIndicator openInterestMinus;
    private InfoIndicator maxDeltaMinus;
    private InfoIndicator deltaMinus;
    private InfoIndicator volume;
    private InfoIndicator bid;



    private StrategyOneBuyStrictlyConsistentInTime() {
    }

    public static StrategyOneBuyStrictlyConsistentInTime getInstance() {
        if (strategyOneBuyStrictlyConsistentInTime == null) strategyOneBuyStrictlyConsistentInTime
                = new StrategyOneBuyStrictlyConsistentInTime();
        return strategyOneBuyStrictlyConsistentInTime;
    }

    public synchronized void setIInfoString(InfoIndicator iInfoIndicator) {
        switch (iInfoIndicator.getType()) {
            case VOLUME:
                volume = iInfoIndicator;
                break;
            case BID:
                bid = iInfoIndicator;
                break;
            case DELTA_BID:
                deltaMinus = iInfoIndicator;
                break;
            case OPEN_POS_MINUS:
                openInterestMinus = iInfoIndicator;
                break;
            case OPEN_POS_PLUS_HL:
                maxOpenInterestPlus = iInfoIndicator;
                break;
            case OPEN_POS_MINUS_HL:
                maxOpenInterestMinus = iInfoIndicator;
                break;
            case DELTA_BID_HL:
                maxDeltaMinus = iInfoIndicator;
                break;
        }
        makeADecision();
    }

    // принимаем решение
    private synchronized void makeADecision() {
        if (volume == null || bid == null || deltaMinus == null || openInterestMinus == null
                || maxOpenInterestPlus == null || maxOpenInterestMinus == null || maxDeltaMinus == null) {
            return;
        }

        if (inTheRangePrice() && inTheRangeTime()) {
            if (Gasket.getStrategeWorkOne() == 1) {
                if (Gasket.isStrategyOneAllFLAG()) {
                    Gasket.setStrategyOneAllFLAG(false);
                    new StrategyOneBuyThread(
                            ((int)(Math.round(Math.abs(Math.random()*200 - 100)) * 39)) + "-SOBSCT", volume, bid);
                }
            } else if (Gasket.getStrategeWorkOne() == 2) {
                if (Gasket.isStrategyOneBuyFLAG()) {
                    Gasket.setStrategyOneBuyFLAG(false);
                    new StrategyOneBuyThread(
                            ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39)) + "-SOBSCT", volume, bid);
                }
            }
            maxOpenInterestMinus = null;
            maxOpenInterestPlus = null;
            openInterestMinus = null;
            maxDeltaMinus = null;
            deltaMinus = null;
            volume = null;
            bid = null;
        }
    }

    // проверяем вписываемся ли в диапазон цен
    private boolean inTheRangePrice() {
        double lowLevel = volume.getPrice() < bid.getPrice()
                ? volume.getPrice() - Gasket.getRangeLivel() : bid.getPrice() - Gasket.getRangeLivel();

        return (maxOpenInterestMinus.getPrice() <= volume.getPrice() && maxOpenInterestMinus.getPrice() >= lowLevel)
                && (maxOpenInterestPlus.getPrice() <= volume.getPrice() && maxOpenInterestPlus.getPrice() >= lowLevel)
                && (openInterestMinus.getPrice() <= volume.getPrice() && openInterestMinus.getPrice() >= lowLevel)
                && (maxDeltaMinus.getPrice() <= volume.getPrice() && maxDeltaMinus.getPrice() >= lowLevel)
                && (deltaMinus.getPrice() <= volume.getPrice() && deltaMinus.getPrice() >= lowLevel);
    }

    // проверяем нет ли тут предварительных уровней
    private boolean isReal() {
        return volume.getPreview() + bid.getPreview() + deltaMinus.getPreview() + openInterestMinus.getPreview()
                + maxOpenInterestPlus.getPreview() + maxOpenInterestMinus.getPreview() +
                maxDeltaMinus.getPreview() == 0;
    }

    // проверяем входим ли в диапазон по датам событий
    private boolean inTheRangeTime() {
//        return (maxOpenInterestMinus.getTime().getTime() >= maxOpenInterestPlus.getTime().getTime())
//                && (openInterestMinus.getTime().getTime() >= maxOpenInterestMinus.getTime().getTime())
//                && (maxOpenInterestPlus.getTime().getTime() >= maxDeltaMinus.getTime().getTime())
//                && (deltaMinus.getTime().getTime() >= openInterestMinus.getTime().getTime())
//                && (bid.getTime().getTime() >= deltaMinus.getTime().getTime())
//                && (volume.getTime().getTime() >= bid.getTime().getTime());
        return true;
    }
}
