package bitmex.Bot.model.strategies.oneStrategies;

import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.enums.TimeFrame;
import bitmex.Bot.model.Gasket;

import java.util.Date;

import static bitmex.Bot.model.Gasket.getTimeCalculationLevel;


// решил не удалять уровни, а так все также
public class OneBuy_3 {
    private static OneBuy_3 oneBuy;

    private InfoIndicator maxOpenInterestMinus;
    private InfoIndicator maxOpenInterestPlus;
    private InfoIndicator openInterestMinus;
    private InfoIndicator maxDeltaMinus;
    private InfoIndicator deltaMinus;
    private InfoIndicator volume;
    private InfoIndicator bid;

    private OneBuy_3() {
    }

    public static OneBuy_3 getInstance() {
        if (oneBuy == null) oneBuy = new OneBuy_3();
        return oneBuy;
    }



    public synchronized void setInfoString(InfoIndicator infoIndicator) {
        switch (infoIndicator.getType()) {
            case OPEN_POS_MINUS_HL:
                maxOpenInterestMinus = infoIndicator;
                break;
            case OPEN_POS_PLUS_HL:
                maxOpenInterestPlus = infoIndicator;
                break;
            case OPEN_POS_MINUS:
                openInterestMinus = infoIndicator;
                break;
            case DELTA_BID_HL:
                maxDeltaMinus = infoIndicator;
                break;
            case DELTA_BID:
                setDeltaMinus(infoIndicator);
                break;
            case VOLUME:
                setVolume(infoIndicator);
                break;
            case BID:
                setBid(infoIndicator);
                break;
        }
        makeADecision();
    }



    private void setBid(InfoIndicator infoIndicator) {
        if (bid == null) bid = infoIndicator;
        else if (bid.getPrice() > infoIndicator.getPrice()
                && isRangeTimeLevel(bid, infoIndicator)
                && (bid.getPeriod() == infoIndicator.getPeriod())) {
            bid = infoIndicator;
        } else if (isRangeTimeLevel(bid, infoIndicator)
                && isBigTimeFrame(bid, infoIndicator)) {
            bid = infoIndicator;
        }
    }



    private void setDeltaMinus(InfoIndicator infoIndicator) {
        if (deltaMinus == null) deltaMinus = infoIndicator;
        else if (deltaMinus.getPrice() > infoIndicator.getPrice()
                && isRangeTimeLevel(deltaMinus, infoIndicator)
                && (deltaMinus.getPeriod() == infoIndicator.getPeriod())) {
            deltaMinus = infoIndicator;
        } else if (isRangeTimeLevel(deltaMinus, infoIndicator)
                && isBigTimeFrame(deltaMinus, infoIndicator)) {
            deltaMinus = infoIndicator;
        }
    }


    private void setVolume(InfoIndicator infoIndicator) {
        if (volume == null) volume = infoIndicator;
        else if (volume.getPrice() > infoIndicator.getPrice()
                && isRangeTimeLevel(volume, infoIndicator)
                && (volume.getPeriod() == infoIndicator.getPeriod())) {
            volume = infoIndicator;
        } else if (isRangeTimeLevel(volume, infoIndicator)
                && isBigTimeFrame(volume, infoIndicator)) {
            volume = infoIndicator;
        }
    }


    // проверяем больший ли траймфрейм у данного уровня или нет
    private boolean isBigTimeFrame(InfoIndicator one, InfoIndicator two) {
//        return one.getPeriod().ordinal() < two.getPeriod().ordinal();
        return true;
    }



    // принимаем решение
    private synchronized void makeADecision() {
        if (volume == null || bid == null || deltaMinus == null || openInterestMinus == null
                || maxOpenInterestPlus == null || maxOpenInterestMinus == null
                || maxDeltaMinus == null) {
            return;
        } else if (!isTimeNotOld()) {
            volume = null;
        }else if (inTheRangePrice() && inTheRangeTime()) {
            if (Gasket.getStrategyWorkOne() == 1) {
                if (Gasket.isOb_os_Flag()) {
                    Gasket.setOb_os_Flag(false);
                    new StrategyOneBuyThread(
                            ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39))
                                    + "-OB_3", volume, getMin());
                }
            } else if (Gasket.getStrategyWorkOne() == 2) {
                if (Gasket.isObFlag_3()) {
                    Gasket.setObFlag_3(false);
                    new StrategyOneBuyThread(
                            ((int) (Math.round(Math.abs(Math.random() * 200 - 100)) * 39))
                                    + "-OB_3", volume, getMin());
                }
            }
//            maxOpenInterestMinus = null;
//            maxOpenInterestPlus = null;
//            openInterestMinus = null;
//            maxDeltaMinus = null;
//            deltaMinus = null;
            volume = null;
//            bid = null;
        }
    }




    // находим цену самого нижнего уровня
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

    // не старый ли уровень
    private boolean isTimeNotOld() {

        InfoIndicator infoIndicator = maxOpenInterestMinus.getTime().getTime() > maxOpenInterestPlus.getTime().getTime()
                ? maxOpenInterestMinus : maxOpenInterestPlus;
        infoIndicator = infoIndicator.getTime().getTime() > openInterestMinus.getTime().getTime()
                ? infoIndicator : openInterestMinus;
        infoIndicator = infoIndicator.getTime().getTime() > maxDeltaMinus.getTime().getTime()
                ? infoIndicator : maxDeltaMinus;
        infoIndicator = infoIndicator.getTime().getTime() > deltaMinus.getTime().getTime()
                ? infoIndicator : deltaMinus;

        if ((infoIndicator.getTime().getTime() - volume.getTime().getTime())
                < (long) (1000 * 60 * getTimeCalculationLevel())) {
            return true;
        } else return false;
    }

    // проверяем вписываемся ли в диапазон цен
    private boolean inTheRangePrice() {
        return (maxOpenInterestMinus.getPrice() <= volume.getPrice())
                && (maxOpenInterestPlus.getPrice() <= volume.getPrice())
                && (openInterestMinus.getPrice() <= volume.getPrice())
                && (maxDeltaMinus.getPrice() <= volume.getPrice())
                && (deltaMinus.getPrice() <= volume.getPrice());
    }

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
        return volume.getPreview() + bid.getPreview() + deltaMinus.getPreview() + openInterestMinus.getPreview()
                + maxOpenInterestPlus.getPreview() + maxOpenInterestMinus.getPreview() +
                maxDeltaMinus.getPreview() == 0;
    }


    // проверяем входим ли в диапазон по датам событий
    private boolean inTheRangeTime() {
        Date after = volume.getTime();

        return (maxOpenInterestMinus.getTime().getTime() >= after.getTime())
                && (maxOpenInterestPlus.getTime().getTime() >= after.getTime())
                && (openInterestMinus.getTime().getTime() >= after.getTime())
                && (maxDeltaMinus.getTime().getTime() >= after.getTime())
                && (deltaMinus.getTime().getTime() >= after.getTime())
                && (bid.getTime().getTime() >= after.getTime());
    }
}

