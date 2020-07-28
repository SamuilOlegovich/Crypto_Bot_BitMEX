package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.model.enums.TypeData;
import bitmex.Bot.model.StringHelper;
import bitmex.Bot.model.Gasket;

import java.net.Socket;
import java.io.*;



public class Repeater extends Thread {
    private String inString;

    public Repeater(String in) {
        this.inString = in;
        start();
    }

    @Override
    public void run() {
        String[] ipPort = Gasket.getBroadcastAddresses().split("\\*\\*\\*");
        String port;
        String ip;

        for (String s : ipPort) {
            port = StringHelper.giveData(TypeData.PORT, s);
            ip = StringHelper.giveData(TypeData.IP, s);
            new SocketThreads(ip, port, inString);
        }
    }

    private class SocketThreads extends Thread {
        private Socket clientSocket;
        private BufferedWriter out;
        private String ip;
        private String in;
        private int port;

        public SocketThreads(String ip, String port, String in) {
            this.port = Integer.parseInt(port);
            this.in = in;
            this.ip = ip;
            start();
        }

        @Override
        public void run() {
//            try {
//                try {
//                    //clientSocket = new Socket("localhost", 5555); // этой строкой мы запрашиваем
//                    clientSocket = new Socket(ip, port); // этой строкой мы запрашиваем
////                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//
//                    out.write(in + "\n"); // отправляем сообщение на сервер
//                    out.flush();
//                } finally { // в любом случае необходимо закрыть сокет и потоки
//                    ConsoleHelper.writeMessage("Клиент был закрыт...");
//                    clientSocket.close();
//                    out.close();
//                }
//            } catch (IOException e) {
//                try {
//                    clientSocket.close();
//                    out.close();
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
//                ConsoleHelper.writeMessage("Что-то не то с репитером.");
//            }

        }
    }
}


