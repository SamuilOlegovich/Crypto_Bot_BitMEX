package bitmex.Bot.controller;


import bitmex.Bot.model.serverAndParser.ParserString;
import bitmex.Bot.model.DatesTimes;
import bitmex.Bot.model.Gasket;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;



public class Test_2 {

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
        try {
            Thread.sleep(1000*3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new SocketThreads("localhost", "5555", "000999888777");
    }





////////////////
    public static class SocketThreads extends Thread {
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
            try {
                try {
                    //clientSocket = new Socket("localhost", 5555); // этой строкой мы запрашиваем
                    clientSocket = new Socket(ip, port); // этой строкой мы запрашиваем
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    out.write("POST / HTTP/1.1\n"); // отправляем сообщение на сервер
                    out.write("Content-Type: application/json; charset=utf-8\n"); // отправляем сообщение на сервер
                    out.write("Host: localhost: 555\n"); // отправляем сообщение на сервер
                    out.write("Content-Length: 216\n"); // отправляем сообщение на сервер
                    out.write("Expect: 100-continue\n"); // отправляем сообщение на сервер
                    out.write("Connection: Keep-Alive\n"); // отправляем сообщение на сервер
//                    out.write(" " + "\n"); // отправляем сообщение на сервер
                    out.write("{\"period\": \"M5\",\"preview\": \"0\",\"time\": \"28.07.2020 21:15:00\"" +
                            ",\"price\": \"11057,5\",\"value\": \"-1410387\",\"type\": \"DeltaBidHL\",\"avg\": \"0\"" +
                            ",\"dir\": \"-1\",\"open\": \"11057,5\",\"close\": \"11057,0\",\"high\": \"11058,0\",\"low\": \"11057,0\"}"); // отправляем сообщение на сервер
                    out.flush();
                } finally { // в любом случае необходимо закрыть сокет и потоки
                    System.out.println("Клиент был закрыт...");
                    clientSocket.close();
                    out.close();
                }
            } catch (IOException e) {
                try {
                    clientSocket.close();
                    out.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println("Что-то не то с репитером.");
            }
        }
    }









    public static class Server extends Thread {

        public Server() {
        }


        @Override
        public void run() {
            System.out.println(DatesTimes.getDateTerminal() + " --- СЕРВЕР ЗАПУЩЕН" + "\n");
//            ParserString parserString = new ParserString();
            ServerSocket server = null;
            Socket socket = null;

            try {
                server = new ServerSocket(5555);

                while (true) {
                    // Блокируется до возникновения нового соединения:
                    if (!isInterrupted()) {
                        socket = server.accept();
                        new SocketThread(socket, null);
                    } else {
                        break;
                    }
                }
                server.close();
                socket.close();
            } catch (IOException e) {
                try {
                    server.close();
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println(DatesTimes.getDateTerminal()
                        + "Что-то навернулось в методе MAIN класса serverAndParser.Server.");
            }
        }
    }


    public static class SocketThread extends Thread {

        private ParserString parserString;
        private Socket socket;


        public SocketThread(Socket socket, ParserString parserString) {
            this.parserString = parserString;
            this.socket = socket;
            start();
        }


        @Override
        public void run() {
            InputStream inputStream = null;
            BufferedReader in = null;
            byte[] bytes;

            try {
                //читаем сообщение
                inputStream = socket.getInputStream();
                in = new BufferedReader(new InputStreamReader(inputStream));

                String postGet = in.readLine();
                System.out.println(postGet);
                String contentType = in.readLine();
                System.out.println(contentType);
                String host = in.readLine();
                System.out.println(host);
                String contentLength = in.readLine();
                System.out.println(contentLength);
                String expect = in.readLine();
                System.out.println(expect);
                String conection = in.readLine();
                System.out.println(conection);
                String space = in.readLine();
                System.out.println(space);
                String out;

                int len = Integer.parseInt(contentLength.replaceAll("Content-Length: ", ""));
                bytes = new byte[len];
                inputStream.read(bytes, 0, len);
                String coding = contentType.replaceAll("Content-Type: application/json; charset=", "");
                out = new String(bytes, coding);
                System.out.println(out);
                //parserString.parserStringJson(out);

                if (Gasket.isBroadcastSignalsFurther()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(postGet + "\n");
                    stringBuilder.append(contentType + "\n");
                    stringBuilder.append(host + "\n");
                    stringBuilder.append(contentLength + "\n");
                    stringBuilder.append(expect + "\n");
                    stringBuilder.append(conection + "\n");
//                    stringBuilder.append("\n");
                    stringBuilder.append(out);
                    //new Repeater(stringBuilder.toString());
                }

                inputStream.close();
                socket.close();
                in.close();
            } catch (IOException e) {
                System.out.println("Что-то навернулось в методе RUN класса serverAndParser.MyServerSocket.");
                System.out.println(e.getStackTrace().toString());
            }
        }
    }

}




