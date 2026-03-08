package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.Tables;
import com.example.nutikas_restorani_serveerimissysteem.FormInfo;

import org.springframework.stereotype.Component;

@Component
public class ServeClient {
    private final Tables mTables;
    private FormInfo mForm;

    public ServeClient(Tables tables) {
        this. mTables = tables;
    }
    public void setForm(FormInfo form) {
        this.mForm = form;
        System.out.println(this.mForm.toString());
    }
    public Table[] getTables() { return mTables.getTables(); };

    public void printAllTables() {
        mTables.printAllTables();
    }
    public void reserveTable() {
        // Currently just tests if tables are displayed correctly
        int n = 5;
        mTables.reserveTable(n);
    }

    
    // Default reservation option - amount of people, from now + 1...3h?
}