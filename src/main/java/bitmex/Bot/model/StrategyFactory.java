package bitmex.Bot.model;


import bitmex.Bot.model.strategies.iiPro.KeepsTrackOfFillingListInfoIndicatorIIPro;
import bitmex.Bot.model.strategies.II.KeepsTrackOfFillingListInfoIndicatorII;
import bitmex.Bot.model.strategies.IIUser.ListensLooksAndComparesUser;
import bitmex.Bot.model.strategies.iiPro.ListensLooksAndComparesPro;
import bitmex.Bot.model.strategies.iiPro.ListensToLooksAndFillsPro;
import bitmex.Bot.model.strategies.II.ListensLooksAndCompares;
import bitmex.Bot.model.strategies.II.ListensToLooksAndFills;
import bitmex.Bot.model.serverAndParser.InfoIndicator;
import bitmex.Bot.model.strategies.oneStrategies.*;



public class StrategyFactory {
    private static StrategyFactory strategyFactory;

    private KeepsTrackOfFillingListInfoIndicatorIIPro keepsTrackOfFillingListInfoIndicatorIIPro;
    private KeepsTrackOfFillingListInfoIndicatorII keepsTrackOfFillingListInfoIndicatorII;
    private ListensLooksAndComparesUser listensLooksAndComparesUser;
    private ListensLooksAndComparesPro listensLooksAndComparesPro;
    private ListensToLooksAndFillsPro listensToLooksAndFillsPro;
    private ListensLooksAndCompares listensLooksAndCompares;
    private ListensToLooksAndFills listensToLooksAndFills;

    private OneSell_5 oneSell5;
    private OneSell_4 oneSell4;
    private OneSell_3 oneSell3;
    private OneSell_2 oneSell2;
    private OneBuy_5 oneBuy5;
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

        if (Gasket.isObs()) {
            if (oneBuy == null) oneBuy = OneBuy.getInstance();
            oneBuy.setInfoString(infoIndicator);

            if (oneSell == null) oneSell = OneSell.getInstance();
            oneSell.setInfoString(infoIndicator);
        }



        if (Gasket.isObs_2()) {
            if (oneBuy2 == null) oneBuy2 = OneBuy_2.getInstance();
            oneBuy2.setInfoString(infoIndicator);

            if (oneSell2 == null) oneSell2 = OneSell_2.getInstance();
            oneSell2.setInfoString(infoIndicator);
        }



        if (Gasket.isObs_3()) {
            if (oneBuy3 == null) oneBuy3 = OneBuy_3.getInstance();
            oneBuy3.setInfoString(infoIndicator);

            if (oneSell3 == null) oneSell3 = OneSell_3.getInstance();
            oneSell3.setInfoString(infoIndicator);
        }



        if (Gasket.isObs_2()) {
            if (oneSell4 == null) oneSell4 = OneSell_4.getInstance();
            oneSell4.setInfoString(infoIndicator);

            if (oneBuy4 == null) oneBuy4 = OneBuy_4.getInstance();
            oneBuy4.setInfoString(infoIndicator);
        }



        if (Gasket.isObs_5()) {
            if (oneSell5 == null) oneSell5 = OneSell_5.getInstance();
            oneSell5.setInfoString(infoIndicator);

            if (oneBuy5 == null) oneBuy5 = OneBuy_5.getInstance();
            oneBuy5.setInfoString(infoIndicator);
        }



        if (Gasket.isSavedPatternsII()) {
            if (listensToLooksAndFills == null) {
                listensToLooksAndFills = ListensToLooksAndFills.getInstance();
                if (keepsTrackOfFillingListInfoIndicatorII == null) {
                    keepsTrackOfFillingListInfoIndicatorII = KeepsTrackOfFillingListInfoIndicatorII.getInstance();
                }
                keepsTrackOfFillingListInfoIndicatorII.setListensToLooksAndFills(listensToLooksAndFills);
            }
            listensToLooksAndFills.setInfoString(infoIndicator);
        }

        if (Gasket.isTradingPatternsII()) {
            if (listensLooksAndCompares == null) {
                listensLooksAndCompares = ListensLooksAndCompares.getInstance();
                if (keepsTrackOfFillingListInfoIndicatorII == null) {
                    keepsTrackOfFillingListInfoIndicatorII = KeepsTrackOfFillingListInfoIndicatorII.getInstance();
                }
                keepsTrackOfFillingListInfoIndicatorII.setListensLooksAndCompares(listensLooksAndCompares);
            }
            listensLooksAndCompares.setInfoString(infoIndicator);
        }



        if (Gasket.isTradingPatternsUser()) {
            if (listensLooksAndComparesUser == null) listensLooksAndComparesUser
                    = ListensLooksAndComparesUser.getInstance();
            listensLooksAndComparesUser.setInfoString(infoIndicator);
        }



        if (Gasket.isSavedPatternsIIPro()) {
            if (listensToLooksAndFillsPro == null) {
                listensToLooksAndFillsPro = ListensToLooksAndFillsPro.getInstance();
                if (keepsTrackOfFillingListInfoIndicatorIIPro == null) {
                    keepsTrackOfFillingListInfoIndicatorIIPro = KeepsTrackOfFillingListInfoIndicatorIIPro.getInstance();
                }
                keepsTrackOfFillingListInfoIndicatorIIPro.setListensToLooksAndFillsPro(listensToLooksAndFillsPro);
            }
            listensToLooksAndFillsPro.setInfoString(infoIndicator);
        }

        if (Gasket.isTradingPatternsIIPro()) {
            if (listensLooksAndComparesPro == null) {
                listensLooksAndComparesPro = ListensLooksAndComparesPro.getInstance();
                if (keepsTrackOfFillingListInfoIndicatorIIPro == null) {
                    keepsTrackOfFillingListInfoIndicatorIIPro = KeepsTrackOfFillingListInfoIndicatorIIPro.getInstance();
                }
                keepsTrackOfFillingListInfoIndicatorIIPro.setListensLooksAndComparesPro(listensLooksAndComparesPro);
            }
            listensLooksAndComparesPro.setInfoString(infoIndicator);
        }
    }
}
