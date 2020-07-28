package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


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
                String out;

                int len = Integer.parseInt(contentLength.replaceAll("Content-Length: ", ""));
                bytes = new byte[len];
                inputStream.read(bytes, 0, len);
                String coding = contentType.replaceAll("Content-Type: application/json; charset=", "");
                out = new String(bytes, coding);
                parserString.parserStringJson(out);

                if (Gasket.isBroadcastSignalsFurther()) {
                    new Repeater(out);
                }

                inputStream.close();
                socket.close();
                in.close();
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Что-то навернулось в методе RUN класса serverAndParser.MyServerSocket.");
            ConsoleHelper.writeMessage(e.getStackTrace().toString());
        }
    }
}
