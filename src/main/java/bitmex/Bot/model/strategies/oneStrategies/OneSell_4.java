package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.enums.TimeFrame;
import bitmex.Bot.model.Gasket;

import java.util.Date;

import static bitmex.Bot.model.Gasket.getTimeCalculationLevel;


// пробую упростить критерии отбора и убрать вторые ненужные уровни
public class OneSell_4 {
    private static OneSell_4 oneSell;

    private InfoIndicator maxOpenInterestMinus;
    private InfoIndicator openInterestPlus;
    private InfoIndicator maxDeltaMinus;
    private InfoIndicator maxDeltaPlus2;
    private InfoIndicator maxDeltaPlus;
    private InfoIndicator deltaPlus;
    private InfoIndicator volume;
    private InfoIndicator ask;

    private int countDelta = 0;

    private OneSell_4() {
    }

    public static OneSell_4 getInstance() {
        if (oneSell == null) oneSell = new OneSell_4();
        return oneSell;
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
                setDeltaPlus(iInfoIndicator);
                break;
            case VOLUME:
                setVolume(iInfoIndicator);
                break;
            case DELTA_ASK_HL:
                twoDelta(iInfoIndicator);
                break;
            case ASK:
                setAsk(iInfoIndicator);
                break;
        }
        makeADecision();
    }



    private void setAsk(InfoIndicator infoIndicator) {
        if (ask == null) ask = infoIndicator;
        else if (ask.getPrice() < infoIndicator.getPrice()
                && isRangeTimeLevel(ask, infoIndicator)
                && (ask.getPeriod() == TimeFrame.M5
                && infoIndicator.getPeriod() == TimeFrame.M5)) {
            ask = infoIndicator;
        } else if (isRangeTimeLevel(ask, infoIndicator)
                && isBigTimeFrame(ask, infoIndicator)) {
            ask = infoIndicator;
        }
    }


    private void setDeltaPlus(InfoIndicator infoIndicator) {
        if (deltaPlus == null) deltaPlus = infoIndicator;
        else if (deltaPlus.getPrice() < infoIndicator.getPrice()
                && isRangeTimeLevel(deltaPlus, infoIndicator)
                && (deltaPlus.getPeriod() == TimeFrame.M5
                && infoIndicator.getPeriod() == TimeFrame.M5)) {
            deltaPlus = infoIndicator;
        } else if (isRangeTimeLevel(deltaPlus, infoIndicator)
                && isBigTimeFrame(deltaPlus, infoIndicator)) {
            deltaPlus = infoIndicator;
        }
    }


    private void setVolume(InfoIndicator infoIndicator) {
        if (volume == null) volume = infoIndicator;
        else if (volume.getPrice() < infoIndicator.getPrice()
                && isRangeTimeLevel(volume, infoIndicator)
                && (volume.getPeriod() == TimeFrame.M5
                && infoIndicator.getPeriod() == TimeFrame.M5)) {
            volume = infoIndicator;
        } else if (isRangeTimeLevel(volume, infoIndicator)
                && isBigTimeFrame(volume, infoIndicator)) {
            volume = infoIndicator;
        }
    }


    // проверяем больший ли траймфрейм у данного уровня или нет
    private boolean isBigTimeFrame(InfoIndicator one, InfoIndicator two) {
        return ((one.getPeriod() == TimeFrame.M5 && two.getPeriod() == TimeFrame.M15)
                || (one.getPeriod() == TimeFrame.M5 && two.getPeriod() == TimeFrame.M30)
                || (one.getPeriod() == TimeFrame.M5 && two.getPeriod() == TimeFrame.H1)
                || (one.getPeriod() == TimeFrame.M15 && two.getPeriod() == TimeFrame.M30)
                || (one.getPeriod() == TimeFrame.M15 && two.getPeriod() == TimeFrame.H1)
                || (one.getPeriod() == TimeFrame.M30 && two.getPeriod() == TimeFrame.H1));
    }



    // принимаем решение
    private synchronized void makeADecision() {
        if (volume == null || ask == null || deltaPlus == null || maxOpenInterestMinus == null
                || maxDeltaPlus == null || openInterestPlus == null || maxDeltaPlus2 == null
                || maxDeltaMinus == null ) {
            return;
        }

        if (inTheRangePrice() && inTheRangeTime() && isTimeNotOld()) {
            if (Gasket.getStrategyWorkOne() == 1) {
                if (Gasket.isOb_os_Flag()) {
                    Gasket.setOb_os_Flag(false);
                    new StrategyOneSellThread(
                            ((int)(Math.round(Math.abs(Math.random()*200 - 100)) * 39))
                                    + "- OS_4", volume, getMin());
                }
            } else if (Gasket.getStrategyWorkOne() == 2) {
                if (Gasket.isOsFlag_4()) {
                    Gasket.setOsFlag_4(false);
                    new StrategyOneSellThread(
                            ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39))
                                    + "-OS_4", volume, getMin());
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
        Date after = volume.getTime();

        return (maxOpenInterestMinus.getTime().getTime() >= after.getTime())
                && (openInterestPlus.getTime().getTime() >= after.getTime())
                && (maxDeltaPlus2.getTime().getTime() >= after.getTime())
                && (maxDeltaPlus.getTime().getTime() >= after.getTime())
                && (deltaPlus.getTime().getTime() >= after.getTime())
                && (ask.getTime().getTime() >= after.getTime());
    }
}
