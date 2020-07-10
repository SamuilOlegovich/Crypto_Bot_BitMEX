package bitmex.Bot.view;


import java.util.ArrayList;
import java.io.*;



public class WriterAndReadFile {

    public static void writerFile(String string, String path, boolean reWrite) {
        File file = new File(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, reWrite))) {
            writer.write(string);
            writer.flush();
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Ошибка в ЗАПИСИ файла - " + string + " === " + path);
        }
    }



    public static ArrayList<String> readFile(String path) throws Exception {
        File file = new File(path);
        ArrayList<String> arrayList = new ArrayList<>();


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                arrayList.add(reader.readLine());
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Ошибка в ЧТЕНИИ файла - " + path);
            throw new Exception();
        }
        return arrayList;
    }
}
