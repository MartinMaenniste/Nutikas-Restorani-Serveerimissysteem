package com.example.nutikas_restorani_serveerimissysteem;

// Holds values from submitted HTML form
public class FormInfo {
    private String name;
    private int guests;
    private String date;
    private String startTime;
    private String endTime;
    private String tableType;

    public String getName() { return this.name; }
    public int getGuests() { return this.guests; }
    public String getDate() { return this.date; }
    public String getStartTime() { return this.startTime; }
    public String getEndTime() { return this.endTime; }
    public String getTableType() { return this.tableType; }

    public void setName(String name) { this.name = name; }
    public void setGuests(int guests) { this.guests = guests; }
    public void setDate(String date) { this.date = date; }
    public void setStartTime(String time) { this.startTime = time; }
    public void setEndTime(String time) { this.endTime = time; }
    public void setTableType(String tableType) { this.tableType = tableType; }
}