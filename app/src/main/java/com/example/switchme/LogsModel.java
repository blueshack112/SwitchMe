package com.example.switchme;

public class LogsModel {
    public String roomID;
    public String startedAt;
    public String endedAt;
    public String energyUsed;

    public LogsModel(String roomID, String startedAt, String endedAt, String energyUsed) {
        this.roomID = roomID;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.energyUsed = energyUsed;
    }
}
