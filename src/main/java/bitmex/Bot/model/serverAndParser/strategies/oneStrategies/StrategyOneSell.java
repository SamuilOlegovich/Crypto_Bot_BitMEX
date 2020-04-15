package bitmex.Bot.model.serverAndParser.strategies.oneStrategies;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.serverAndParser.InfoIndicator;

import java.util.Date;


public class StrategyOneSell {
    private static StrategyOneSell strategyOneSell;

    private InfoIndicator maxOpenInterestMinus;
    private InfoIndicator openInterestPlus;
    private InfoIndicator maxDeltaMinus;
    private InfoIndicator maxDeltaPlus2;
    private InfoIndicator maxDeltaPlus;
    private InfoIndicator deltaPlus;
    private InfoIndicator volume;
    private InfoIndicator ask;

    private int countDelta = 0;



    private StrategyOneSell() {
    }

    public static StrategyOneSell getInstance() {
        if (strategyOneSell == null) strategyOneSell = new StrategyOneSell();
        return strategyOneSell;
    }

    public void setIInfoString(InfoIndicator iInfoIndicator) {
        switch (iInfoIndicator.getType()) {
            case OPEN_POS_MINUS_HL:
                maxOpenInterestMinus = iInfoIndicator;
                break;
            case OPEN_POS_PLUS:
                openInterestPlus = iInfoIndicator;
                break;
            case DELTA_BID_HL:
                maxDeltaMinus = iInfoIndicator;
                break;
            case DELTA_ASK:
                deltaPlus = iInfoIndicator;
                break;
            case DELTA_ASK_HL:
                twoDelta(iInfoIndicator);
                break;
            case VOLUME:
                volume = iInfoIndicator;
                break;
            case ASK:
                ask = iInfoIndicator;
                break;
        }
        makeADecision();
    }

    // принимаем решение
    private synchronized void makeADecision() {
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
                            ((int)(Math.round(Math.abs(Math.random()*200 - 100)) * 39)) + "-SOS", volume, ask);
                }
            } else if (Gasket.getStrategeWorkOne() == 2) {
                if (Gasket.isStrategyOneSellFLAG()) {
                    Gasket.setStrategyOneSellFLAG(false);
                    new StrategyOneSellThread(
                            ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39)) + "-SOS", volume, ask);
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
        Date before = maxDeltaMinus.getTime();
        Date after = volume.getTime();

        return (maxOpenInterestMinus.getTime().getTime() >= after.getTime()
                && maxOpenInterestMinus.getTime().getTime() <= before.getTime())
                && (openInterestPlus.getTime().getTime() >= after.getTime()
                && openInterestPlus.getTime().getTime() <= before.getTime())
                && (maxDeltaPlus2.getTime().getTime() >= after.getTime()
                && maxDeltaPlus2.getTime().getTime() <= before.getTime())
                && (maxDeltaPlus.getTime().getTime() >= after.getTime()
                && maxDeltaPlus.getTime().getTime() <= before.getTime())
                && (deltaPlus.getTime().getTime() >= after.getTime()
                && deltaPlus.getTime().getTime() <= before.getTime())
                && (ask.getTime().getTime() >= after.getTime()
                && ask.getTime().getTime() <= before.getTime());
    }
}
