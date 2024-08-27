package saveData;

import java.io.File;

public class DataFileManager {

    private String pcFile;
    private String consoleFile;
    private String mobileFile;

    public DataFileManager(String username) {
        this.pcFile = "userData/" + username + "_PCsGame.csv";
        this.consoleFile = "userData/" + username + "_ConsolesGame.csv";
        this.mobileFile = "userData/" + username + "_MobileGame.csv";

        new File("userData").mkdirs();
    }
    // Getter methods for file paths
    public String getPcFile() {
        return pcFile;
    }

    public String getConsoleFile() {
        return consoleFile;
    }

    public String getMobileFile() {
        return mobileFile;
    }
}