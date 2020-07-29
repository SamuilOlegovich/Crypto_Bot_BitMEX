package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.view.ConsoleHelper;
import bitmex.Bot.model.DatesTimes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.net.Socket;




public class RepeaterSocket extends Thread {
    private ArrayList<ArrayList<String>> arrayLists;
    private ArrayList<String> strings;
    private BufferedWriter outBuff;
    private static Socket clientSocket = null;

    private static String contentLength = "6";
    private static String contentType = "6";
    private static String conection = "6";
    private static String postGet = "6";
    private static String expect = "6";
    private static String host = "6";
    private static String out = "6";
    private static String ip;

    private int port;


    public RepeaterSocket(String ip, String port, ArrayList in) {
//        this.arrayLists = new ArrayList<>(in);
        this.strings = new ArrayList<>(in);
        this.port = Integer.parseInt(port);
        this.ip = ip;
        start();
    }

    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("68gihj");
        arrayList.add("68gihj");
        arrayList.add("68gihj");
        arrayList.add("68gihj");
        arrayList.add("68gihj");
        arrayList.add("68gihj");
        arrayList.add("68gihj");
        arrayList.add("68gihj");
        arrayList.add("68gihj");
        new Server().start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new RepeaterSocket("localhost", "5555", arrayList);
    }



    @Override
    public void run() {
        getStringsOut();
        try {
            try {
                clientSocket = new Socket("localhost", 5555);
                outBuff = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                outBuff.write(postGet);
                outBuff.write(contentType);
                outBuff.write(host);
                outBuff.write(contentLength);
                outBuff.write(expect);
                outBuff.write(conection);
//                outBuff.write("" + "\n");
//                JsonObject jsonObject = new JsonObject();


//                outBuff.write();
                outBuff.write(out);

                outBuff.flush();
//
            } finally {
                // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (outBuff != null) {
                    outBuff.close();
                }
            }
            return;
        } catch (IOException e) {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (outBuff != null) {
                    outBuff.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            ConsoleHelper.writeMessage("Что-то не то с RepeaterSocket.");
        }
    }

    private void getStringsOut() {
//        ArrayList<String> stringArrayList = new ArrayList<>(arrayLists.get(arrayLists.size() - 1));
//        arrayLists.remove(arrayLists.size() - 1);
//
//        postGet = stringArrayList.get(0);
//        contentType = stringArrayList.get(1);
//        host = "Host: localhost:" + port + "\n";    //stringArrayList.get(2);
//        contentLength = stringArrayList.get(3);
//        expect = stringArrayList.get(4);
//        conection = stringArrayList.get(5);
//        out = stringArrayList.get(6);

        postGet = strings.get(0);
        contentType = strings.get(1);
        host = "Host: localhost:" + port + "\n";    //stringArrayList.get(2);
        contentLength = strings.get(3);
        expect = strings.get(4);
        conection = strings.get(5);
        out = strings.get(6);
    }


//    public synchronized void addSignals(ArrayList<String> in) {
//       arrayLists.add(new ArrayList<>(in));
////       initSocket();
//    }

    private void initSocket() {
        if (clientSocket == null) {
            try {
                clientSocket = new Socket(ip, port);
                outBuff = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                ConsoleHelper.writeMessage("Что-то не то с RepeaterSocket - initSocket");
                try {
                    clientSocket.close();
                    outBuff.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    ConsoleHelper.writeMessage("Что-то не то с RepeaterSocket - initSocket");
                }
            }
        }
    }
}

