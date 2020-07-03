package bitmex.Bot.model.strategies.iiPro;



import java.util.Comparator;
import java.util.ArrayList;

import static bitmex.Bot.model.DatesTimes.getDateTerminal;
import static bitmex.Bot.model.Gasket.getLevelsToCompare;
import static bitmex.Bot.view.ConsoleHelper.writeMessage;
import static bitmex.Bot.model.StringHelper.giveData;
import static bitmex.Bot.model.enums.TypeData.*;




public class CompareAndMakeDecisionPro extends Thread {
    private SortTheAlphabet sortTheAlphabet;

    private ArrayList<String> patternsList;
    private ArrayList<String> marketList;
    private String[] levelsToCompare;



    public CompareAndMakeDecisionPro(ArrayList<String> marketList, ArrayList<String> patternsList) {
        this.levelsToCompare = getLevelsToCompare().split("-");
        this.patternsList = new ArrayList<>(patternsList);
        this.marketList = new ArrayList<>(marketList);
        this.sortTheAlphabet = new SortTheAlphabet();
        start();
    }



    @Override
    public void run() {
        if (compareSheets()) {
            writeMessage(getDateTerminal() + " --- "
                    + "Нашел совпадения в рынке с ПАТТЕРНАМИ II Pro передаю на сделку");
            new MakeDealPro(patternsList.get(0));
        }

        patternsList.clear();
        marketList.clear();
    }



    // разбиваем листы на блоки и на строки биас и сравниваем
    private boolean compareSheets() {
        ArrayList<String> patternStrings = new ArrayList<>();
        ArrayList<String> marketStrings = new ArrayList<>();
        ArrayList<String> patternBias = new ArrayList<>();
        ArrayList<String> marketBias = new ArrayList<>();
        int maxBias = 0;


        // находим все BIAS в маркет листе
        for (String string : marketList) {
            if (string.startsWith(BIAS.toString())) {
                marketBias.add(string);
            }
        }

        // находим все BIAS в паттерн листе
        for (String string : patternsList) {
            if (string.startsWith(BIAS.toString())) {
                patternBias.add(string);
            }
        }

        // сравниваем размеры BIAS листов, если их размер не равен, то авттерны не равны
        // если их размер равен то проверяем равны ли в них направления
        if (marketBias.size() != patternBias.size()) {
            return false;
        } else if (marketBias.size() > 0){
            for (String marketString : marketBias) {
                String patternString = patternBias.get(marketBias.indexOf(marketString));

                if (!giveData(BIAS, marketString).equals(giveData(BIAS, patternString))) {
                    return false;
                }
            }
        }

        maxBias = marketBias.size() + 1;

        // формируем блоки строк находящиеся между BIAS и нулевой строкой
        for (int i = 1; i <= (maxBias > 0 ? maxBias : 1); i++) {
            int bias = 0;

            for (String string : marketList) {
                if (string.startsWith(BIAS.toString())) {
                    bias++;
                }

                if (!string.startsWith(BIAS.toString()) && !string.startsWith(NULL.toString())
                        && !string.startsWith(BUY.toString()) && bias == i - 1) {
                    marketStrings.add(string);
                }
            }

            bias = 0;

            for (String string : patternsList) {
                if (string.startsWith(BIAS.toString())) {
                    bias++;
                }

                if (!string.startsWith(BIAS.toString()) && !string.startsWith(NULL.toString())
                        && !string.startsWith(BUY.toString()) && bias == i - 1) {
                    patternStrings.add(string);
                }
            }

            // отправляем полученые блоки на дальнейшую проверку
            if (!removeAllUnnecessaryAndCheckForAMatch(marketStrings, patternStrings)) {
                return false;
            }
        }
        return true;
    }



    // сортируем, удаляем лишнее и сравниваем переданые блоки строк
    private boolean removeAllUnnecessaryAndCheckForAMatch(ArrayList<String> marketStrings,
                                                          ArrayList<String> patternStrings) {

        if (patternStrings != null && patternStrings.size() > 0
                && marketStrings != null && marketStrings.size() > 0) {

            // сравниваем размеры блоков
            if (marketStrings.size() != patternStrings.size()) {
                return false;
            } else {
                ArrayList<String> patternCompare = new ArrayList<>();
                ArrayList<String> marketCompare = new ArrayList<>();
                ArrayList<String> patternAll = new ArrayList<>();
                ArrayList<String> marketAll = new ArrayList<>();


                // начинаем перебирать блоки строк и сравнивать их друг с другом
                for (String stringMarket : marketStrings) {
                    String stringPattern = patternStrings.get(marketStrings.indexOf(stringMarket));
                    boolean flag = true;

                    for (String string : levelsToCompare) {
                        if (giveData(type, stringMarket).equals(giveData(type, stringPattern))
                                && giveData(type, stringMarket).equals(string)) {
                            patternCompare.add(stringPattern);
                            marketCompare.add(stringMarket);
                            flag = false;
                            break;
                        }
                    }

                    if (flag) {
                        patternAll.add(stringPattern);
                        marketAll.add(stringMarket);
                    }

                }

                patternAll.sort(sortTheAlphabet);
                marketAll.sort(sortTheAlphabet);

                for (String pattern : patternCompare) {
                    String market = marketCompare.get(patternCompare.indexOf(pattern));
                    if (!finallyComparisonOnAllData(market, pattern)) {
                        return false;
                    }
                }

                for (String pattern : patternAll) {
                    String market = marketAll.get(patternAll.indexOf(pattern));
                    if (!finallyComparisonOnAllData(market, pattern)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }



    private boolean finallyComparisonOnAllData(String marketString, String patternString) {

        writeMessage(getDateTerminal() + " --- "
                + "---------------------------------------------------------------------------- 4 ii Pro");


        if (!giveData(type, marketString).equals(giveData(type, patternString))) {

            writeMessage(getDateTerminal() + " --- "
                    + "---------------------------------------------------------------------------- 4-3 ii Pro");

            return false;
        }

        // направление свечи сравниваем только на избранных уровнях, на остальных это не важно
        for (String string : levelsToCompare) {
            if (string.equals(giveData(type, marketString)) && string.equals(giveData(type, patternString))) {
                if (!giveData(dir, marketString).equals(giveData(dir, patternString))) {

                    writeMessage(getDateTerminal() + " --- "
                            + "---------------------------------------------------------------------------- 4-4 ii Pro");

                    return false;
                }
            }
        }

        writeMessage(getDateTerminal() + " --- "
                + "---------------------------------------------------------------------------- 4-5 ii Pro");

        return true;
    }



    /// === INNER CLASS === ///



    private class SortTheAlphabet implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int result = giveData(type, o1).compareTo(giveData(type, o2));
            return Integer.compare(result, 0);
        }
    }






    /// === TEST === ///




    public static void main(String[] args) {
        ArrayList<String> pattern = new ArrayList<>();
        ArrayList<String> market = new ArrayList<>();

        pattern.add("BUY===0===SELL===1===AVERAGE===0.5===MAX===0.5===SIZE===281===ID===1014\n");
        pattern.add("{\"period\": \"M5\",\"preview\": \"0\",\"time\": \"2020-06-26 07:06:00\",\"price\": \"9173.0\",\"value\": \"-968678\",\"type\": \"DELTA_ZS_MIN_PLUS\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9174.0\",\"close\": \"9172.0\",\"high\": \"9174.5\",\"low\": \"9171.5\"}");
        pattern.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"2020-06-26 07:06:00\",\"price\": \"9172.0\",\"value\": \"475050\",\"type\": \"DELTA_ASK_HL\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9174.0\",\"close\": \"9172.0\",\"high\": \"9174.5\",\"low\": \"9171.5\"}");
        pattern.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"2020-06-26 07:05:00\",\"price\": \"9164.5\",\"value\": \"14551995\",\"type\": \"ASK\",\"avg\": \"6690397\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        pattern.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"2020-06-26 07:05:00\",\"price\": \"9164.5\",\"value\": \"29086551\",\"type\": \"BID\",\"avg\": \"7188808\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        pattern.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"2020-06-26 07:05:00\",\"price\": \"9164.5\",\"value\": \"43638546\",\"type\": \"VOLUME\",\"avg\": \"13931647\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        pattern.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"2020-06-26 07:05:00\",\"price\": \"9164.5\",\"value\": \"-14534556\",\"type\": \"DELTA_BID\",\"avg\": \"-4440315\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        pattern.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"2020-06-26 07:05:00\",\"price\": \"9163.5\",\"value\": \"-2268771\",\"type\": \"OPEN_POS_MINUS_HL\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"0\",\"time\": \"2020-06-26 07:05:00\",\"price\": \"9163.5\",\"value\": \"747470\",\"type\": \"OI_ZS_MIN_MINUS\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"2020-06-26 07:09:00\",\"price\": \"9163.0\",\"value\": \"537027\",\"type\": \"OPEN_POS_ASK_PLUS\",\"avg\": \"384893\",\"dir\": \"1\",\"open\": \"9160.5\",\"close\": \"9165.0\",\"high\": \"9165.0\",\"low\": \"9160.0\"}" );
        pattern.add("BIAS===SELL===-6.5===AVERAGE===2.9746835443038435===MAX===3.0===TIME===2020-06-26 07:15:00" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"0\",\"time\": \"2020-06-26 07:10:00\",\"price\": \"9165.0\",\"value\": \"678184\",\"type\": \"DELTA_ASK_HL\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9165.0\",\"close\": \"9167.5\",\"high\": \"9168.0\",\"low\": \"9164.5\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"0\",\"time\": \"2020-06-26 07:14:00\",\"price\": \"9156.5\",\"value\": \"-899524\",\"type\": \"DELTA_ZS_MIN_PLUS\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9154.5\",\"close\": \"9158.5\",\"high\": \"9158.5\",\"low\": \"9154.5\"}" );
        pattern.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"2020-06-26 07:14:00\",\"price\": \"9156.5\",\"value\": \"1318763\",\"type\": \"DELTA_ZS_PLUS\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9154.5\",\"close\": \"9158.5\",\"high\": \"9158.5\",\"low\": \"9154.5\"}" );
        pattern.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"2020-06-26 07:13:00\",\"price\": \"9155.0\",\"value\": \"2022265\",\"type\": \"OPEN_POS_ASK_PLUS\",\"avg\": \"755337\",\"dir\": \"1\",\"open\": \"9150.0\",\"close\": \"9155.0\",\"high\": \"9155.0\",\"low\": \"9149.0\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"0\",\"time\": \"2020-06-26 07:12:00\",\"price\": \"9140.5\",\"value\": \"116301\",\"type\": \"OI_ZS_MIN_MINUS\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9167.5\",\"close\": \"9150.0\",\"high\": \"9167.5\",\"low\": \"9134.0\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"0\",\"time\": \"2020-06-26 07:12:00\",\"price\": \"9140.5\",\"value\": \"-977403\",\"type\": \"OPEN_POS_BID_MINUS\",\"avg\": \"-426882\",\"dir\": \"-1\",\"open\": \"9167.5\",\"close\": \"9150.0\",\"high\": \"9167.5\",\"low\": \"9134.0\"}" );
        pattern.add("{\"period\": \"M15\",\"preview\": \"1\",\"time\": \"2020-06-26 07:12:00\",\"price\": \"9140.5\",\"value\": \"-1268917\",\"type\": \"OI_ZS_MINUS\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9167.5\",\"close\": \"9150.0\",\"high\": \"9167.5\",\"low\": \"9134.0\"}" );
        pattern.add("BIAS===SELL===-19.0===AVERAGE===4.452554744526424===MAX===8.5===TIME===2020-06-26 07:20:00" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"2020-06-26 07:16:00\",\"price\": \"9164.5\",\"value\": \"-34587\",\"type\": \"OI_ZS_MINUS\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9161.0\",\"close\": \"9164.5\",\"high\": \"9167.0\",\"low\": \"9160.5\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"2020-06-26 07:16:00\",\"price\": \"9164.5\",\"value\": \"1884288\",\"type\": \"OI_ZS_MIN_MINUS\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9161.0\",\"close\": \"9164.5\",\"high\": \"9167.0\",\"low\": \"9160.5\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"2020-06-26 07:16:00\",\"price\": \"9163.5\",\"value\": \"-1299409\",\"type\": \"DELTA_ZS_MIN_PLUS\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9161.0\",\"close\": \"9164.5\",\"high\": \"9167.0\",\"low\": \"9160.5\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"2020-06-26 07:19:00\",\"price\": \"9140.0\",\"value\": \"802311\",\"type\": \"OPEN_POS_BID_PLUS\",\"avg\": \"360048\",\"dir\": \"-1\",\"open\": \"9151.5\",\"close\": \"9139.5\",\"high\": \"9151.5\",\"low\": \"9139.0\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"2020-06-26 07:19:00\",\"price\": \"9140.0\",\"value\": \"611306\",\"type\": \"OPEN_POS_PLUS_HL\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9151.5\",\"close\": \"9139.5\",\"high\": \"9151.5\",\"low\": \"9139.0\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"2020-06-26 07:19:00\",\"price\": \"9140.0\",\"value\": \"1884288\",\"type\": \"OPEN_POS_PLUS\",\"avg\": \"607601\",\"dir\": \"-1\",\"open\": \"9151.5\",\"close\": \"9139.5\",\"high\": \"9151.5\",\"low\": \"9139.0\"}" );
        pattern.add("{\"period\": \"M5\",\"preview\": \"1\",\"time\": \"2020-06-26 07:19:00\",\"price\": \"9139.0\",\"value\": \"-1299409\",\"type\": \"DELTA_BID_HL\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9151.5\",\"close\": \"9139.5\",\"high\": \"9151.5\",\"low\": \"9139.0\"}" );







        market.add("BUY===0===SELL===1===AVERAGE===0.5===MAX===0.5===SIZE===281===ID===1014");
        market.add("{\"period\": \"M15\",\"preview\": \"01\",\"time\": \"2020-06-16 07:06:00\",\"price\": \"19173.0\",\"value\": \"-968678\",\"type\": \"DELTA_ZS_MIN_PLUS\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9174.0\",\"close\": \"9172.0\",\"high\": \"9174.5\",\"low\": \"9171.5\"}");
        market.add("{\"period\": \"M15\",\"preview\": \"11\",\"time\": \"2020-06-16 07:06:00\",\"price\": \"19172.0\",\"value\": \"475050\",\"type\": \"DELTA_ASK_HL\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9174.0\",\"close\": \"9172.0\",\"high\": \"9174.5\",\"low\": \"9171.5\"}");
        market.add("{\"period\": \"M115\",\"preview\": \"11\",\"time\": \"2020-06-16 07:05:00\",\"price\": \"19164.5\",\"value\": \"14551995\",\"type\": \"ASK\",\"avg\": \"6690397\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        market.add("{\"period\": \"M115\",\"preview\": \"11\",\"time\": \"2020-06-16 07:05:00\",\"price\": \"19164.5\",\"value\": \"29086551\",\"type\": \"BID\",\"avg\": \"7188808\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        market.add("{\"period\": \"M115\",\"preview\": \"11\",\"time\": \"2020-06-16 07:05:00\",\"price\": \"19164.5\",\"value\": \"43638546\",\"type\": \"VOLUME\",\"avg\": \"13931647\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        market.add("{\"period\": \"M115\",\"preview\": \"11\",\"time\": \"2020-06-16 07:05:00\",\"price\": \"19164.5\",\"value\": \"-14534556\",\"type\": \"DELTA_BID\",\"avg\": \"-4440315\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        market.add("{\"period\": \"M115\",\"preview\": \"11\",\"time\": \"2020-06-16 07:05:00\",\"price\": \"19163.5\",\"value\": \"-2268771\",\"type\": \"OPEN_POS_MINUS_HL\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"01\",\"time\": \"2020-06-16 07:05:00\",\"price\": \"19163.5\",\"value\": \"747470\",\"type\": \"OI_ZS_MIN_MINUS\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9178.5\",\"close\": \"9174.0\",\"high\": \"9179.0\",\"low\": \"9150.0\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"11\",\"time\": \"2020-06-16 07:09:00\",\"price\": \"19163.0\",\"value\": \"537027\",\"type\": \"OPEN_POS_ASK_PLUS\",\"avg\": \"384893\",\"dir\": \"1\",\"open\": \"9160.5\",\"close\": \"9165.0\",\"high\": \"9165.0\",\"low\": \"9160.0\"}" );
        market.add("BIAS===SELL===-6.5===AVERAGE===2.9746835443038435===MAX===3.0===TIME===2020-06-26 07:19:00" );
        market.add("{\"period\": \"M15\",\"preview\": \"01\",\"time\": \"2020-06-16 07:10:00\",\"price\": \"19165.0\",\"value\": \"678184\",\"type\": \"DELTA_ASK_HL\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9165.0\",\"close\": \"9167.5\",\"high\": \"9168.0\",\"low\": \"9164.5\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"01\",\"time\": \"2020-06-16 07:14:00\",\"price\": \"19156.5\",\"value\": \"-899524\",\"type\": \"DELTA_ZS_MIN_PLUS\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9154.5\",\"close\": \"9158.5\",\"high\": \"9158.5\",\"low\": \"9154.5\"}" );
        market.add("{\"period\": \"M115\",\"preview\": \"11\",\"time\": \"2020-06-16 07:14:00\",\"price\": \"19156.5\",\"value\": \"1318763\",\"type\": \"DELTA_ZS_PLUS\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9154.5\",\"close\": \"9158.5\",\"high\": \"9158.5\",\"low\": \"9154.5\"}" );
        market.add("{\"period\": \"M115\",\"preview\": \"11\",\"time\": \"2020-06-16 07:13:00\",\"price\": \"19155.0\",\"value\": \"2022265\",\"type\": \"OPEN_POS_ASK_PLUS\",\"avg\": \"755337\",\"dir\": \"1\",\"open\": \"9150.0\",\"close\": \"9155.0\",\"high\": \"9155.0\",\"low\": \"9149.0\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"01\",\"time\": \"2020-06-16 07:12:00\",\"price\": \"19140.5\",\"value\": \"116301\",\"type\": \"OI_ZS_MIN_MINUS\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9167.5\",\"close\": \"9150.0\",\"high\": \"9167.5\",\"low\": \"9134.0\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"01\",\"time\": \"2020-06-16 07:12:00\",\"price\": \"19140.5\",\"value\": \"-977403\",\"type\": \"OPEN_POS_BID_MINUS\",\"avg\": \"-426882\",\"dir\": \"-1\",\"open\": \"9167.5\",\"close\": \"9150.0\",\"high\": \"9167.5\",\"low\": \"9134.0\"}" );
        market.add("{\"period\": \"M115\",\"preview\": \"11\",\"time\": \"2020-06-16 07:12:00\",\"price\": \"19140.5\",\"value\": \"-1268917\",\"type\": \"OI_ZS_MINUS\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9167.5\",\"close\": \"9150.0\",\"high\": \"9167.5\",\"low\": \"9134.0\"}" );
        market.add("BIAS===SELL===-19.0===AVERAGE===4.452554744526424===MAX===8.5===TIME===2020-06-26 07:79:00" );
        market.add("{\"period\": \"M15\",\"preview\": \"11\",\"time\": \"2020-06-16 07:16:00\",\"price\": \"19164.5\",\"value\": \"-34587\",\"type\": \"OI_ZS_MINUS\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9161.0\",\"close\": \"9164.5\",\"high\": \"9167.0\",\"low\": \"9160.5\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"11\",\"time\": \"2020-06-16 07:16:00\",\"price\": \"19164.5\",\"value\": \"1884288\",\"type\": \"OI_ZS_MIN_MINUS\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9161.0\",\"close\": \"9164.5\",\"high\": \"9167.0\",\"low\": \"9160.5\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"11\",\"time\": \"2020-06-16 07:16:00\",\"price\": \"19163.5\",\"value\": \"-1299409\",\"type\": \"DELTA_ZS_MIN_PLUS\",\"avg\": \"0\",\"dir\": \"1\",\"open\": \"9161.0\",\"close\": \"9164.5\",\"high\": \"9167.0\",\"low\": \"9160.5\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"11\",\"time\": \"2020-06-16 07:19:00\",\"price\": \"19140.0\",\"value\": \"802311\",\"type\": \"OPEN_POS_BID_PLUS\",\"avg\": \"360048\",\"dir\": \"-1\",\"open\": \"9151.5\",\"close\": \"9139.5\",\"high\": \"9151.5\",\"low\": \"9139.0\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"11\",\"time\": \"2020-06-16 07:19:00\",\"price\": \"19140.0\",\"value\": \"611306\",\"type\": \"OPEN_POS_PLUS_HL\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9151.5\",\"close\": \"9139.5\",\"high\": \"9151.5\",\"low\": \"9139.0\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"11\",\"time\": \"2020-06-16 07:19:00\",\"price\": \"19140.0\",\"value\": \"1884288\",\"type\": \"OPEN_POS_PLUS\",\"avg\": \"607601\",\"dir\": \"-1\",\"open\": \"9151.5\",\"close\": \"9139.5\",\"high\": \"9151.5\",\"low\": \"9139.0\"}" );
        market.add("{\"period\": \"M15\",\"preview\": \"11\",\"time\": \"2020-06-16 07:19:00\",\"price\": \"19139.0\",\"value\": \"-1299409\",\"type\": \"DELTA_BID_HL\",\"avg\": \"0\",\"dir\": \"-1\",\"open\": \"9151.5\",\"close\": \"9139.5\",\"high\": \"9151.5\",\"low\": \"9139.0\"}" );


        new CompareAndMakeDecisionPro(market, pattern);
    }
}



//    0 {"period": "M5",
//    1 "preview": "1",
//    2 "time": "2020-05-27 12:28:00",
//    3 "price": "9175.0",
//    4 "value": "2920763",
//    5 "type": "ASK",
//    6 "avg": "2871888",
//    7 "dir": "1",
//    8 "open": "9167.5",
//    9 "close": "9178.5",
//    10 "high": "9183.0",
//    11 "low": "9167.0"}
//
//
//    0 period
//    1 period.toString()
//    2 ===preview=== +
//    3 preview +
//    4 "===time===" +
//    5 dateFormat.format(time)
//    6 "===price===" +
//    7 price
//    8 "===value===" +
//    9 value +
//    10 "===type===" +
//    11 type.toString() +
//    12 "===avg===" +
//    13 avg
//    14 "===dir===" +
//    15 dir + "
//    16 ===open===" +
//    17 open + "
//    18 ===close===" +
//    19 close + "
//    20 ===high===" +
//    21 high
//    22 ===low===" +
//    23 low

