package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.Gasket;

import java.util.Date;


public class StrategyOneBuy {
    private static StrategyOneBuy strategyOneBuy;

    private InfoIndicator maxOpenInterestMinus;
    private InfoIndicator maxOpenInterestPlus;
    private InfoIndicator openInterestMinus;
    private InfoIndicator maxDeltaMinus;
    private InfoIndicator deltaMinus;
    private InfoIndicator volume;
    private InfoIndicator bid;


    private StrategyOneBuy() {
    }

    public static StrategyOneBuy getInstance() {
        if (strategyOneBuy == null) strategyOneBuy = new StrategyOneBuy();
        return strategyOneBuy;
    }

    public synchronized void setIInfoString(InfoIndicator iInfoIndicator) {
        switch (iInfoIndicator.getType()) {
            case OPEN_POS_MINUS_HL:
                maxOpenInterestMinus = iInfoIndicator;
                break;
            case OPEN_POS_PLUS_HL:
                maxOpenInterestPlus = iInfoIndicator;
                break;
            case OPEN_POS_MINUS:
                openInterestMinus = iInfoIndicator;
                break;
            case DELTA_BID_HL:
                maxDeltaMinus = iInfoIndicator;
                break;
            case DELTA_BID:
                deltaMinus = iInfoIndicator;
                break;
            case VOLUME:
                volume = iInfoIndicator;
                break;
            case BID:
                bid = iInfoIndicator;
                break;
        }
        makeADecision();
    }

    // принимаем решение
    private synchronized void makeADecision() {
        if (volume == null || bid == null || deltaMinus == null || openInterestMinus == null
                || maxOpenInterestPlus == null || maxOpenInterestMinus == null
                || maxDeltaMinus == null) {
            return;
        }

        if (inTheRangePrice() && inTheRangeTime()) {
            if (Gasket.getStrategyWorkOne() == 1) {
                if (Gasket.isStrategyOneAllFLAG()) {
                    Gasket.setStrategyOneAllFLAG(false);
                    new StrategyOneBuyThread(
                            ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39)) + "-SOB", volume, getMin());
                }
            } else if (Gasket.getStrategyWorkOne() == 2) {
                if (Gasket.isStrategyOneBuyFLAG()) {
                    Gasket.setStrategyOneBuyFLAG(false);
                    new StrategyOneBuyThread(
                            ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39)) + "-SOB", volume, getMin());
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

    private InfoIndicator getMin() {
        InfoIndicator infoIndicator = maxOpenInterestMinus.getPrice() < maxOpenInterestPlus.getPrice()
                ? maxOpenInterestMinus : maxOpenInterestPlus;
        infoIndicator = infoIndicator.getPrice() < openInterestMinus.getPrice()
                ? infoIndicator : openInterestMinus;
        infoIndicator = infoIndicator.getPrice() < maxDeltaMinus.getPrice()
                ? infoIndicator : maxDeltaMinus;
        infoIndicator = infoIndicator.getPrice() < deltaMinus.getPrice()
                ? infoIndicator : deltaMinus;
        return infoIndicator;
    }

    // проверяем вписываемся ли в диапазон цен
    private boolean inTheRangePrice() {
        double lowLevel = volume.getPrice() > bid.getPrice()
                ? bid.getPrice() - Gasket.getRangeLevel() : volume.getPrice() - Gasket.getRangeLevel();

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
        Date before = maxDeltaMinus.getTime();
        Date after = volume.getTime();

        return (maxOpenInterestMinus.getTime().getTime() >= after.getTime()
                && maxOpenInterestMinus.getTime().getTime() <= before.getTime())
                && (maxOpenInterestPlus.getTime().getTime() >= after.getTime()
                && maxOpenInterestPlus.getTime().getTime() <= before.getTime())
                && (openInterestMinus.getTime().getTime() >= after.getTime()
                && openInterestMinus.getTime().getTime() <= before.getTime())
                && (maxDeltaMinus.getTime().getTime() >= after.getTime()
                && maxDeltaMinus.getTime().getTime() <= before.getTime())
                && (deltaMinus.getTime().getTime() >= after.getTime()
                && deltaMinus.getTime().getTime() <= before.getTime())
                && (bid.getTime().getTime() >= after.getTime()
                && bid.getTime().getTime() <= before.getTime());
    }
}
