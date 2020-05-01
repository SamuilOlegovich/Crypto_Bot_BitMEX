package bitmex.Bot.model;

public class FilesAndPathCreator {
    private String pathSettings;
    private String pathPatterns;
    private String pathLogs;

    public FilesAndPathCreator() {
        Gasket.setFilesAndPathCreator(this);
        createdPath();
        isTheFileInPlace();
    }

    private void createdPath() {
        pathSettings = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/Settings.txt";

        pathPatterns = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/Patterns.txt";

        pathLogs = getClass().getResource("").getPath()
                .replaceAll("target/classes", "src/main/java")
                .replaceAll("model/", "") + "Logs/Log.txt";
    }

    private void isTheFileInPlace() {

    }














    public String getPathSettings() {
        return pathSettings;
    }

    public String getPathPatterns() {
        return pathPatterns;
    }

    public String getPathLogs() {
        return pathLogs;
    }
}
