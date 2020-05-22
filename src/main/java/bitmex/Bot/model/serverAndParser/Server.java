package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    public Server() {}


    @Override
    public void run() {
        ConsoleHelper.writeMessage("СЕРВЕР ЗАПУЩЕН");
        ParserString parserString = new ParserString();
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(Gasket.getPORT());

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







    // TEST
    public static void main(String[] args) throws IOException {
        ParserString parser = new ParserString();
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(Gasket.getPORT());
            while (true) {
                // Блокируется до возникновения нового соединения:
                socket = server.accept();
                new SocketThread(socket, parser);
            }
        } catch (IOException e) {
            server.close();
            socket.close();
            ConsoleHelper.writeMessage("Что-то навернулось в методе MAIN класса serverAndParser.Server.");
        }
    }
}