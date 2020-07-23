package bitmex.Bot.model;


import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.view.ConsoleHelper;


public class StringHelper {


    public static synchronized String giveData(TypeData key, String in) {

        if (in.startsWith("{\"" + TypeData.period.toString() + "\":")) {
            String[] strings = in.split("\",\"");
            strings[0] = strings[0].replaceAll("\\{", "");
            strings[strings.length - 1] = strings[strings.length - 1].replaceAll("}", "");

            for (int i = 0; i < strings.length; i++) {
                strings[i] = strings[i].replaceAll("\"", "");
                strings[i] = strings[i].replaceAll(": ", "");
            }

            for (String s : strings) {
                if (s.startsWith(key.toString())) {
                    return s.replaceAll(key.toString(), "").replaceAll(",", ".");
                }
            }
        } else {
            String[] strings = in.split("===");

            for (int i = 0; i < strings.length; i++) {
                if (strings[i].equalsIgnoreCase(key.toString())) {
                    return strings[i + 1];
                }
            }
        }

        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " === " + key.toString() + " --- "
                + in + "=============================================================================================");

        return null;
    }



    public static synchronized String setData(TypeData key, String data, String in) {
        StringBuilder stringBuilder = new StringBuilder();

//        if (in.startsWith("{\"" + TypeData.period.toString() + "\":")) {
//            String[] strings = in.split("\",\"");
//            strings[0] = strings[0].replaceAll("\\{", "");
//            strings[strings.length - 1] = strings[strings.length - 1].replaceAll("}", "");
//
//            for (int i = 0; i < strings.length; i++) {
//                strings[i] = strings[i].replaceAll("\"", "");
//                strings[i] = strings[i].replaceAll(": ", "");
//            }
//
//            for (String s : strings) {
//                if (s.startsWith(key.toString())) {
//                    return s.replaceAll(key.toString(), "").replaceAll(",", ".");
//                }
//            }
//        } else {
            String[] strings = in.split("===");

            for (int i = 0; i < strings.length; i++) {
                if (strings[i].equalsIgnoreCase(key.toString())) {
                    strings[i + 1] = data;
                    break;
                }
            }

            for (int i = 0; i < strings.length; i++) {
                stringBuilder.append(strings[i]);

                if (i != strings.length - 1) {
                    stringBuilder.append("===");
                }
            }
//        }

//        ConsoleHelper.writeMessage(DatesTimes.getDateTerminal() + " === " + key.toString() + " --- "
//                + in + "=============================================================================================");

        return stringBuilder.toString();
    }



    public static synchronized String convertStringForUser(String string) {

        String period = giveData(TypeData.period, string);
        String preview = giveData(TypeData.preview, string);
        String time = giveData(TypeData.time, string);
        String price = giveData(TypeData.price, string);
        String value = giveData(TypeData.value, string);
        String type = giveData(TypeData.type, string);
        String avg = giveData(TypeData.avg, string);
        String dir = giveData(TypeData.dir, string);
        String open = giveData(TypeData.open, string);
        String close = giveData(TypeData.close, string);
        String high = giveData(TypeData.high, string);
        String low = giveData(TypeData.low, string);

        return "period===" + period + "===preview===" + preview + "===time===" + time + "===price===" + price
                + "===value===" + value + "===type===" + type + "===avg===" + avg + "===dir===" + dir
                + "===open===" + open + "===close===" + close + "===high===" + high
                + "===low===" + low;
    }



    public static synchronized String convertStringForUserInsertNulls(String string) {
        String period = Gasket.getPeriodNULL().equalsIgnoreCase(TypeData.period.toString())
                ? giveData(TypeData.period, string) : Gasket.getPeriodNULL();
        String preview = Gasket.getPreviewNULL().equalsIgnoreCase(TypeData.preview.toString())
                ? giveData(TypeData.preview, string) : Gasket.getPreviewNULL();
        String time = Gasket.getTimeNULL().equalsIgnoreCase(TypeData.time.toString())
                ? giveData(TypeData.time, string) : Gasket.getTimeNULL();
        String price = Gasket.getPriceNULL().equalsIgnoreCase(TypeData.price.toString())
                ? giveData(TypeData.price, string) : Gasket.getPriceNULL();
        String value = Gasket.getValueNULL().equalsIgnoreCase(TypeData.value.toString())
                ? giveData(TypeData.value, string) : Gasket.getValueNULL();
        String type = Gasket.getTypeNULL().equalsIgnoreCase(TypeData.type.toString())
                ? giveData(TypeData.type, string) : Gasket.getTypeNULL();
        String avg = Gasket.getAvgNULL().equalsIgnoreCase(TypeData.avg.toString())
                ? giveData(TypeData.avg, string) : Gasket.getAvgNULL();
        String dir = Gasket.getDirNULL().equalsIgnoreCase(TypeData.dir.toString())
                ? giveData(TypeData.dir, string) : Gasket.getDirNULL();
        String open = Gasket.getOpenNULL().equalsIgnoreCase(TypeData.open.toString())
                ? giveData(TypeData.open, string) : Gasket.getOpenNULL();
        String close = Gasket.getCloseNULL().equalsIgnoreCase(TypeData.close.toString())
                ? giveData(TypeData.close, string) : Gasket.getCloseNULL();
        String high = Gasket.getHighNULL().equalsIgnoreCase(TypeData.high.toString())
                ? giveData(TypeData.high, string) : Gasket.getHighNULL();
        String low = Gasket.getLowNULL().equalsIgnoreCase(TypeData.low.toString())
                ? giveData(TypeData.low, string) : Gasket.getLowNULL();

        return "period===" +  period + "===preview===" + preview + "===time===" + time + "===price===" + price
                + "===value===" + value + "===type===" + type + "===avg===" + avg + "===dir===" + dir
                + "===open===" + open + "===close===" + close + "===high===" + high
                + "===low===" + low;
    }


    // in
    // BUY===2===SELL===0===AVERAGE===0.5===MAX===0.5===SIZE===27===ID===2964
    // out
    // BUY===1===SELL===1===AVERAGE===3.28===MAX===5.0===SIZE===220===BLOCK===1===TYPE===VOLUME===ID===400
    public static String insertTheMissingDataInTheZeroLine(String in) {
        String[] strings = in.split("===");
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(strings[0]);
        stringBuilder.append("===");
        stringBuilder.append(strings[1]);
        stringBuilder.append("===");
        stringBuilder.append(strings[2]);
        stringBuilder.append("===");
        stringBuilder.append(strings[3]);
        stringBuilder.append("===");
        stringBuilder.append(strings[4]);
        stringBuilder.append("===");
        stringBuilder.append(strings[5]);
        stringBuilder.append("===");
        stringBuilder.append(strings[6]);
        stringBuilder.append("===");
        stringBuilder.append(strings[7]);
        stringBuilder.append("===");
        stringBuilder.append(strings[8]);
        stringBuilder.append("===");
        stringBuilder.append(strings[9]);
        stringBuilder.append("===");

        stringBuilder.append(TypeData.BLOCK.toString());
        stringBuilder.append("===");
        stringBuilder.append(TypeData.NULL.toString());
        stringBuilder.append("===");
        stringBuilder.append(TypeData.TYPE.toString());
        stringBuilder.append("===");
        stringBuilder.append(TypeData.NULL.toString());
        stringBuilder.append("===");
        stringBuilder.append(TypeData.PREDICTOR.toString());
        stringBuilder.append("===");
        stringBuilder.append(TypeData.NULL.toString());
        stringBuilder.append("===");

        stringBuilder.append(strings[10]);
        stringBuilder.append("===");
        stringBuilder.append(strings[11]);

        if (Gasket.isAddOrTESTatTheEndOfTheLine()) {
            stringBuilder.append(" --- " + TypeData.TEST.toString());
        }

        //stringBuilder.append("\n");

        return stringBuilder.toString();
    }
}
