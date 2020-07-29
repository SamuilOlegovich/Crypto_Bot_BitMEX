package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;


public class SocketThread extends Thread {

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
            String contentType = in.readLine();
            String host = in.readLine();
            String contentLength = in.readLine();
            String expect = in.readLine();
            String conection = in.readLine();
            String space = in.readLine();
            String out = null;


            // POST / HTTP/1.1
            // Content-Type: application/json; charset=utf-8
            // Host: localhost:4444
            // Content-Length: 216
            // Expect: 100-continue
            // Connection: Keep-Alive
            // {"period": "M5","preview": "0","time": "28.07.2020 21:15:00","price": "11057,5","value": "-1410387",
            // "type": "DeltaBidHL","avg": "0","dir": "-1","open": "11057,5",
            // "close": "11057,0","high": "11058,0","low": "11057,0"}



            String coding;
            int len = Integer.parseInt(contentLength.replaceAll("Content-Length: ", ""));
            if (space.length() < 10) {
                bytes = new byte[len];
                inputStream.read(bytes, 0, len);
                coding = contentType.replaceAll("Content-Type: application/json; charset=", "");
                out = new String(bytes, coding);
            }

//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("\n\n");
//            stringBuilder.append(postGet + "\n");
//            stringBuilder.append(contentType + "\n");
//            stringBuilder.append(host + "\n");
//            stringBuilder.append(contentLength + "\n");
//            stringBuilder.append(expect + "\n");
//            stringBuilder.append(conection + "\n");
//            stringBuilder.append(space + "\n");
//            stringBuilder.append(out == null ? "" + "\n" : out + "\n");
//            stringBuilder.append("\n\n");
//            ConsoleHelper.writeMessage(stringBuilder.toString());

            if (space.length() > 10) {
                parserString.parserStringJson(space);
            } else {
                parserString.parserStringJson(out);
            }


            if (Gasket.isBroadcastSignalsFurther()) {
                ArrayList<String> arrayList = new ArrayList<>();

                arrayList.add(postGet + "\n");
                arrayList.add(contentType + "\n");
                arrayList.add(host + "\n");
                arrayList.add(contentLength + "\n");
                arrayList.add(expect + "\n");
                arrayList.add(conection + "\n");
                arrayList.add(out);

                Gasket.getRepeater().transferTo(arrayList);
            }

            inputStream.close();
            socket.close();
            in.close();
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Что-то навернулось в методе RUN класса SocketThread");
            ConsoleHelper.writeMessage(e.getStackTrace().toString());
        }
    }
}
