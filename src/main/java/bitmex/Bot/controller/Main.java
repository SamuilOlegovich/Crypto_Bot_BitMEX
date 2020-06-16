package bitmex.Bot.controller;


import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.View;




public class Main {

    public static void main(String[] args) {
        View view = new View();
        Gasket.setViewThread(view);
        view.start();
    }
}
