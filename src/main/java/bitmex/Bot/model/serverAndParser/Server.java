package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    // список всех нитей
    private static ParserString parserString;
    private static int PORT;

    public Server() {
        this.PORT = Gasket.getPORT();
    }

    public static void main(String[] args) throws IOException {
        parserString = new ParserString();
        ServerSocket server = null;
        Socket socket = null;
        try {
            server = new ServerSocket(PORT);
            while (true) {
                // Блокируется до возникновения нового соединения:
                socket = server.accept();
                new SocketThread(socket, parserString);
            }
        } catch (IOException e) {
            server.close();
            socket.close();
            ConsoleHelper.writeMessage("Что-то навернулось в методе MAIN класса serverAndParser.Server.");
        }
    }

    @Override
    public void run() {
        parserString = new ParserString();
        ServerSocket server = null;
        Socket socket = null;
        try {
            server = new ServerSocket(PORT);
            while (true) {
                // Блокируется до возникновения нового соединения:
                socket = server.accept();
                new SocketThread(socket, parserString);
            }
        } catch (IOException e) {
            try {
                server.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ConsoleHelper.writeMessage("Что-то навернулось в методе MAIN класса serverAndParser.Server.");
        }
    }
}