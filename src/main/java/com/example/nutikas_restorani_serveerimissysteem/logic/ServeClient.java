package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.Tables;
import com.example.nutikas_restorani_serveerimissysteem.FormInfo;

import org.springframework.stereotype.Component;

@Component
public class ServeClient {
    private final Tables mTables;
    private FormInfo mForm;

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

//Name: John, Guests: 10, Date: 2026-03-11, Start time: 10:30, End time: 11:45
        return dateTime;
    }

    public ServeClient(Tables tables) {
        this. mTables = tables;
    }
    public void setForm(FormInfo form) {
        this.mForm = form;
    }
    public Table[] getTables() { return mTables.getTables(); };
    public int getHowManyTables() {return mTables.getHowManyTables(); }

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