package bitmex.Bot.controller;


public class Test {



    // AdditionalTrading1 = [«1.00@P*1.2@P*0.01@V», «2.00@P*1.2@P*0.01@V», «3.00@P*1.2@P*0.01@V»,
    // «4.00@P*1.2@P*0.01@V»]
    public static void main(String[] args) {

        StringBuilder stringBuilder = new StringBuilder("AdditionalTrading1 = [");
        double value = 0.001;
        double steep = 0.1;
        double plus = 0.10;
        double take = 0.6;
        int block = 100;

        for (int i = 0; i < block; i++) {
            String[] strings = ("" + steep).split("\\.");
            String string = strings[0] + "." + strings[1].substring(0, strings[1].length() == 1 ? 1 : 2);

            stringBuilder.append("\"").append(string)
                    .append("@P*").append(take)
                    .append("@P*").append(value)
                    .append("@V\"");
            if (i != block - 1) {
                stringBuilder.append(", ");
            } else {
                stringBuilder.append("]");
            }
            steep = Double.parseDouble(string) + plus;

        }
        System.out.println(stringBuilder.toString());

    }
}

