package bitmex.Bot.model.bitMEX.enums;

public enum ChartDataBinSize {
    ONE_MINUTE("1m"),
    FIVE_MINUTES("5m"),
    ONE_HOUR("1h"),
    ONE_DAY("1d");

    private String bin;

    private ChartDataBinSize(String bin) {
        this.bin = bin;
    }

    public String getBin() {
        return bin;
    }
}
