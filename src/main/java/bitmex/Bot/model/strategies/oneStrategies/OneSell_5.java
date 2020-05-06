package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.model.serverAndParser.InfoIndicator;

import java.util.Date;

import static bitmex.Bot.model.Gasket.getTimeCalculationLevel;

public class OneSell_5 {
    private static OneSell_5 oneSell;

    private InfoIndicator maxOpenInterestMinus;
    private InfoIndicator openInterestPlus;
    private InfoIndicator maxDeltaMinus;
    private InfoIndicator maxDeltaPlus2;
    private InfoIndicator maxDeltaPlus;
    private InfoIndicator deltaPlus;
    private InfoIndicator volume;
    private InfoIndicator ask;

    private int countDelta = 0;

    private OneSell_5() {
    }

    public static OneSell_5 getInstance() {
        if (oneSell == null) oneSell = new OneSell_5();
        return oneSell;
    }


    public void setInfoString(InfoIndicator infoIndicator) {
        switch (infoIndicator.getType()) {
            case OPEN_POS_MINUS_HL:
                maxOpenInterestMinus = infoIndicator;
                break;
            case OPEN_POS_PLUS:
                openInterestPlus = infoIndicator;
                break;
            case DELTA_BID_HL:
                maxDeltaMinus = infoIndicator;
                break;
            case DELTA_ASK:
//                setDeltaPlus(infoIndicator);
                deltaPlus = infoIndicator;
                break;
            case VOLUME:
                setVolume(infoIndicator);
                break;
            case DELTA_ASK_HL:
                twoDelta(infoIndicator);
                break;
            case ASK:
//                setAsk(infoIndicator);
                ask = infoIndicator;
                break;
        }
        makeADecision();
    }



//    private void setAsk(InfoIndicator infoIndicator) {
//        if (ask == null || !isRangeTimeLevel(ask, infoIndicator)) {
//            ask = infoIndicator;
//        } else if (ask.getPrice() < infoIndicator.getPrice()
//                && isRangeTimeLevel(ask, infoIndicator)
//                && (ask.getPeriod() == infoIndicator.getPeriod())) {
//            ask = infoIndicator;
//        } else if (isRangeTimeLevel(ask, infoIndicator)
//                && isBigTimeFrame(ask, infoIndicator)) {
//            ask = infoIndicator;
//        } else if (isRangeTimeLevel(ask, infoIndicator)
//                && !isBigTimeFrame(ask, infoIndicator)
//                && ask.getPrice() > infoIndicator.getPrice()) {
//            ask = infoIndicator;
//        }
//    }


//    private void setDeltaPlus(InfoIndicator infoIndicator) {
//        if (deltaPlus == null || !isRangeTimeLevel(deltaPlus, infoIndicator)) {
//            deltaPlus = infoIndicator;
//        } else if (deltaPlus.getPrice() < infoIndicator.getPrice()
//                && isRangeTimeLevel(deltaPlus, infoIndicator)
//                && (deltaPlus.getPeriod() == infoIndicator.getPeriod())) {
//            deltaPlus = infoIndicator;
//        } else if (isRangeTimeLevel(deltaPlus, infoIndicator)
//                && isBigTimeFrame(deltaPlus, infoIndicator)) {
//            deltaPlus = infoIndicator;
//        } else if (isRangeTimeLevel(deltaPlus, infoIndicator)
//                && !isBigTimeFrame(deltaPlus, infoIndicator)
//                && deltaPlus.getPrice() > infoIndicator.getPrice()) {
//            deltaPlus = infoIndicator;
//        }
//    }


    private void setVolume(InfoIndicator infoIndicator) {
        if (volume == null || !isRangeTimeLevel(volume, infoIndicator)) {
            volume = infoIndicator;
        } else if (volume.getPrice() < infoIndicator.getPrice()
                && isRangeTimeLevel(volume, infoIndicator)
                && (volume.getPeriod() == infoIndicator.getPeriod())) {
            volume = infoIndicator;
        } else if (isRangeTimeLevel(volume, infoIndicator)
                && isBigTimeFrame(volume, infoIndicator)) {
            volume = infoIndicator;
        } else if (isRangeTimeLevel(volume, infoIndicator)
                && !isBigTimeFrame(volume, infoIndicator)
                && volume.getPrice() > infoIndicator.getPrice()) {
            volume = infoIndicator;
        }
    }


    // проверяем больший ли траймфрейм у данного уровня или нет
    private boolean isBigTimeFrame(InfoIndicator one, InfoIndicator two) {
        return one.getPeriod().ordinal() < two.getPeriod().ordinal();
//        return true;
    }



    // принимаем решение
    private synchronized void makeADecision() {
        if (volume == null || ask == null || deltaPlus == null || maxOpenInterestMinus == null
                || maxDeltaPlus == null || openInterestPlus == null || maxDeltaPlus2 == null
                || maxDeltaMinus == null ) {
            return;
        } else if (!isTimeNotOld()) {
            volume = null;
        } else if (inTheRangePrice() && inTheRangeTime()) {
            if (Gasket.getStrategyWorkOne() == 1) {
                if (Gasket.isOb_os_Flag()) {
                    Gasket.setOb_os_Flag(false);
                    new StrategyOneSellThread(
                            ((int)(Math.round(Math.abs(Math.random()*200 - 100)) * 39))
                                    + "- OS_5", volume, getMin());
                }
            } else if (Gasket.getStrategyWorkOne() == 2) {
                if (Gasket.isOsFlag_5()) {
                    Gasket.setOsFlag_5(false);
                    new StrategyOneSellThread(
                            ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39))
                                    + "-OS_5", volume, getMin());
                }
            }
//            maxOpenInterestMinus = null;
//            openInterestPlus = null;
//            maxDeltaMinus = null;
//            maxDeltaPlus2 = null;
//            maxDeltaPlus = null;
//            deltaPlus = null;
            volume = null;
//            ask = null;
        }
    }


    // не старый ли уровень
    private boolean isTimeNotOld() {

        InfoIndicator infoIndicator = maxOpenInterestMinus.getTime().getTime() > openInterestPlus.getTime().getTime()
                ? maxOpenInterestMinus : openInterestPlus;
        infoIndicator = infoIndicator.getTime().getTime() > maxDeltaMinus.getTime().getTime()
                ? infoIndicator : maxDeltaMinus;
        infoIndicator = infoIndicator.getTime().getTime() > maxDeltaPlus2.getTime().getTime()
                ? infoIndicator : maxDeltaPlus2;
        infoIndicator = infoIndicator.getTime().getTime() > maxDeltaPlus.getTime().getTime()
                ? infoIndicator : maxDeltaPlus;
        infoIndicator = infoIndicator.getTime().getTime() > deltaPlus.getTime().getTime()
                ? infoIndicator : deltaPlus;

        if ((infoIndicator.getTime().getTime() - volume.getTime().getTime())
                < (long) (1000 * 60 * getTimeCalculationLevel())) {
            return true;
        } else return false;
    }


    // находим найвысший элемен, это и будет точка минимум для села
    private InfoIndicator getMin() {
        InfoIndicator infoIndicator = maxOpenInterestMinus.getPrice() > openInterestPlus.getPrice()
                ? maxOpenInterestMinus : openInterestPlus;
        infoIndicator = infoIndicator.getPrice() > maxDeltaMinus.getPrice()
                ? infoIndicator : maxDeltaMinus;
        infoIndicator = infoIndicator.getPrice() > maxDeltaPlus2.getPrice()
                ? infoIndicator : maxDeltaPlus2;
        infoIndicator = infoIndicator.getPrice() > maxDeltaPlus.getPrice()
                ? infoIndicator : maxDeltaPlus;
        infoIndicator = infoIndicator.getPrice() > deltaPlus.getPrice()
                ? infoIndicator : deltaPlus;
        return infoIndicator;
    }

    // проверяем вписываемся ли в диапазон цен
    private boolean inTheRangePrice() {
        return (maxOpenInterestMinus.getPrice() >= volume.getPrice())
                && (openInterestPlus.getPrice() >= volume.getPrice())
                && (maxDeltaMinus.getPrice() >= volume.getPrice())
                && (maxDeltaPlus2.getPrice() >= volume.getPrice())
                && (maxDeltaPlus.getPrice() >= volume.getPrice())
                && (deltaPlus.getPrice() >= volume.getPrice());
    }

    // не устарели ли уровни
    private boolean isRangeTimeLevel(InfoIndicator one, InfoIndicator two) {
        if ((two.getTime().getTime() - one.getTime().getTime())
                < (long) (1000 * 60 * getTimeCalculationLevel())) {
            return true;
        } else {
            return false;
        }
    }

    // проверяем нет ли тут предварительных уровней
    private boolean isReal() {
        return volume.getPreview() + ask.getPreview() + maxOpenInterestMinus.getPreview()
                + openInterestPlus.getPreview() + maxDeltaMinus.getPreview()
                + maxDeltaPlus.getPreview() + maxDeltaPlus2.getPreview()
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
        Date after = volume.getTime();

        return (maxOpenInterestMinus.getTime().getTime() >= after.getTime())
                && (openInterestPlus.getTime().getTime() >= after.getTime())
                && (maxDeltaPlus2.getTime().getTime() >= after.getTime())
                && (maxDeltaMinus.getTime().getTime() >= after.getTime())
                && (maxDeltaPlus.getTime().getTime() >= after.getTime())
                && (deltaPlus.getTime().getTime() >= after.getTime());

    }
}

