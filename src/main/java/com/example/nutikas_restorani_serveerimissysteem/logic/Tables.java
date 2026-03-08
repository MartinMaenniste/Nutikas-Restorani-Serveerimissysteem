package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.Table;
import java.lang.Math;

import org.springframework.stereotype.Component;

@Component
public class Tables{
	private final GenAllTablesArray mGen;

	private int mTotalTables;
	private Table[] mTables;

	private Table[] getFreeTablesDuring(long start, long end) {

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

	public boolean reserveTable(long startDateTime, long endDateTime, int guests) {
		Table[] freeTables = getFreeTablesDuring(startDateTime, endDateTime);
		
		// Choose the closest max seats compared to amount of guests
		int closestIndex = -1;
		int smallestDiff = 100; // Random large enough value
		for (int i = 0; i < freeTables.length; i++) {
			if (guests <= freeTables[i].getMaxSeats() && Math.abs(guests - freeTables[i].getMaxSeats()) < smallestDiff) {
				closestIndex = i;
				smallestDiff = Math.abs(guests - freeTables[i].getMaxSeats());
			}
		}
		
		// Reset other tables first
		for(Table t : mTables) { t.setDisplayAsSuggested(false); }
		if (closestIndex == -1) { return false; }

		mTables[closestIndex].setDisplayAsSuggested(true);

		return true;
	}

	public void printAllTables() {
		for (Table t : mTables) {
			System.out.println("Seats: " + t.getMaxSeats() + ", Type: " + t.getSizeName() + ", Is suggested: " + t.getDisplayAsSuggested());
		}
	}
}
