package sample;

import java.io.File;

public class Data {
    String clientName;
    String batteryName;
    File fileData;

    public Data(String clientName, String batteryName, File fileData) {
        this.clientName = clientName;
        this.batteryName = batteryName;
        this.fileData = fileData;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBatteryName() {
        return batteryName;
    }

    public void setBatteryName(String batteryName) {
        this.batteryName = batteryName;
    }

    public File getFileData() {
        return fileData;
    }

    public void setFileData(File fileData) {
        this.fileData = fileData;
    }
}
