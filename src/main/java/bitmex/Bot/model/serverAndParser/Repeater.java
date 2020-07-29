package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.Gasket;

import java.util.ArrayList;





public class Repeater {
    private ArrayList<RepeaterSocket> repeaterSockets;

    public Repeater() {
        this.repeaterSockets = new ArrayList<>();
//        init();
    }

    private void init() {
        String[] ipPort = Gasket.getBroadcastAddresses().split("\\*\\*\\*");
        String port;
        String ip;

        for (String s : ipPort) {
            port = StringHelper.giveData(TypeData.PORT, s);
            ip = StringHelper.giveData(TypeData.IP, s);
//            repeaterSockets.add(new RepeaterSocket(ip, port));
        }
    }

    public synchronized void transferTo(ArrayList<String> in) {
        ArrayList<String> arrayList = new ArrayList<>(in);
//        for (RepeaterSocket repeaterSocket : repeaterSockets) {
//            repeaterSocket.addSignals(arrayList);
//        }

        String[] ipPort = Gasket.getBroadcastAddresses().split("\\*\\*\\*");
        String port;
        String ip;

        for (String s : ipPort) {
            port = StringHelper.giveData(TypeData.PORT, s);
            ip = StringHelper.giveData(TypeData.IP, s);
//            repeaterSockets.add(new RepeaterSocket(ip, port));
            new RepeaterSocket(ip, port, arrayList);
        }
    }
}


