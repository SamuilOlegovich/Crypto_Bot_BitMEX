package bitmex.Bot.model.serverAndParser;

import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.WriterAndReadFile;




public class WriterString extends Thread {
    private String string;

    public WriterString(String s) {
        this.string = s;
        start();
    }

    @Override
    public void run() {
        WriterAndReadFile.writerFile(string + "\n", Gasket.getFilesAndPathCreator()
                .getPathFullHistory(), true);
    }
}
