package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.Table;
import java.lang.Math;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Tables{
	private final GenAllTablesArray mGen;

	private int mTotalTables;
	private Table[] mTables;

	private int[] getFreeTablesIndexesDuring(long start, long end) {

		int[] freeTables = new int[mTotalTables];
		int index = 0;
		for(int i = 0; i < mTables.length; i++) {
			if (mTables[i].isFreeDuring(start, end)) {
				freeTables[index] = i;
				index++; 
			}
		}

		int[] trimmed = new int[index];
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
		int[] freeTables = getFreeTablesIndexesDuring(startDateTime, endDateTime);
		
		
		// Choose the closest max seats compared to amount of guests
		int closestIndex = -1;
		int smallestDiff = 100; // Random large enough value
		for (int i = 0; i < freeTables.length; i++) {
			int index = freeTables[i];
			if (guests <= mTables[index].getMaxSeats() && Math.abs(guests - mTables[index].getMaxSeats()) < smallestDiff) {
				closestIndex = index;
				smallestDiff = Math.abs(guests - mTables[index].getMaxSeats());
			}
		}
		
		// Reset other tables first
		for(Table t : mTables) { t.setDisplayAsSuggested(false); }
		if (closestIndex == -1) { return false; }

		mTables[closestIndex].setDisplayAsSuggested(true);
		mTables[closestIndex].addReservation(startDateTime, endDateTime);

		/*System.out.println("Reservations:");
		List<long[]> reservations = mTables[closestIndex].getReservationsArray();
		for(long[] r : reservations) {
			System.out.println("Start: " + r[0] + ", End: " + r[1]);
		}*/

		return true;
	}

	public void printAllTables() {
		for (Table t : mTables) {
			System.out.println("Seats: " + t.getMaxSeats() + ", Type: " + t.getSizeName() + ", Is suggested: " + t.getDisplayAsSuggested());
		}
	}
}
