package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.Tables;
import com.example.nutikas_restorani_serveerimissysteem.FormInfo;

import java.util.List;

import org.springframework.stereotype.Component;

// ServeClient is used at PathController.java
// Sort of a wrapper class to get Tables class and controller to exchange information
@Component
public class ServeClient {
    private final Tables mTables; // Wrapper to deal with Table[]
    private FormInfo mForm; // The form submitted from index.html

    /**
     * Application works with times in the form of longs with value yymmddmmhh (year, month, day, hour, minute)
     * Form gives them as strings - example Date: 2026-03-23, Start time: 12:30, End time: 15:45
     */
    private long stringDateTimetoInt(String date, String time) {
        long dateTime = 0;

        String[] dateParts = date.split("-");
        String[] timeParts = time.split(":");

        for(int i = 0; i < dateParts.length; i++) {
            dateTime += Long.parseLong(dateParts[i]);
            dateTime *= 100;
        }
        
        dateTime += Integer.parseInt(timeParts[0]);
        dateTime *= 100;
        dateTime += Integer.parseInt(timeParts[1]);

        return dateTime;
    }

    public ServeClient(Tables tables) { // Used by Spring boot to automatically set and populate Tables class
        this. mTables = tables;
    }
    public void fillDatabase() { this.mTables.fillDatabase(); }

    public void setForm(FormInfo form) {
        this.mForm = form;
    }
    public List<TableAsClass> getTables() {  return mTables.getTables(); }
    public int getHowManyTables() {return mTables.getHowManyTables(); }
    /**
     * Used by PathController to try to reserve table before switching to the view that displays the reserved table.
     * Returns if a suitable table was found and reserved
     */
    public boolean reserveTable() {
        long start = stringDateTimetoInt(mForm.getDate(), mForm.getStartTime());
        long end = stringDateTimetoInt(mForm.getDate(), mForm.getEndTime());

        return mTables.reserveTable(start, end, mForm.getGuests(), mForm.getTableType());
    }
    /*
     * Only used to reserve tables randomly when program starts. 
     */
    public void reserveForRandReservation(long start, long end, int index) {
        mTables.reserveForRandReservation(start, end, index);
    }

    // Testing
    public void printAllTables() { mTables.printAllTables(); }
    public void printAllReservations() { mTables.printAllReservations(); }
}