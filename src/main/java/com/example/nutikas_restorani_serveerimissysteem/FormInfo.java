package com.example.nutikas_restorani_serveerimissysteem;

public class FormInfo {
    private String name;
    private int guests;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;

    public String getName() { return this.name; }
    public int getGuests() { return this.guests; }
    public String getStartDate() { return this.startDate; }
    public String getStartTime() { return this.startTime; }
    public String getEndDate() { return this.endDate; }
    public String getEndTime() { return this.endTime; }

    public void setName(String name) { this.name = name; }
    public void setGuests(int guests) { this.guests = guests; }
    public void setStartDate(String date) { this.startDate = date; }
    public void setStartTime(String time) { this.startTime = time; }
    public void setEndDate(String date) { this.endDate = date; }
    public void setEndTime(String time) { this.endTime = time; }

    @Override
    public String toString() { // Not used in the final app, used for testing.
        return "Name: " + this.name + ", Guests: " + this.guests + ", Start date/time: " + this.startDate + " " + this.startTime + ", End date/time: " + this.endDate + " " + this.endTime;
    }
}