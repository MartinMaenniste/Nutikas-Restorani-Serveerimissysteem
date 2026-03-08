package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.Table;

import org.springframework.stereotype.Component;

@Component
public class Tables{
	private final GenAllTablesArray mGen;

	private int mTotalTables;
	private Table[] mTables;

	private Table[] getFreeTablesDuring(int start, int end) {

		Table[] freeTables = new Table[mTotalTables];
		int index = 0;
		for(Table t : mTables) {
			if (t.isFreeDuring(start, end)) {
				freeTables[index] = t;
				index++;
			}
		}
		Table[] trimmed = new Table[index];
		for(int i = 0; i < index; i++) {
			trimmed[i] = freeTables[i];
		}

		return trimmed;
	}


	public Tables(GenAllTablesArray generated) {
		mGen = generated;
		this.mTables = generated.getArray();
		mTotalTables = generated.arraySize();
	}
	public void setTablesArray(Table[] arr) { this.mTables = arr; }
	public Table[] getTables() { return this.mTables; };

	public void reserveTable(int index) {
		mTables[index].setDisplayAsSuggested(true);
	}

	public void printAllTables() {
		for (Table t : mTables) {
			System.out.println("Seats: " + t.getMaxSeats() + ", Type: " + t.getSizeName() + ", Is suggested: " + t.getDisplayAsSuggested());
		}
	}
}
