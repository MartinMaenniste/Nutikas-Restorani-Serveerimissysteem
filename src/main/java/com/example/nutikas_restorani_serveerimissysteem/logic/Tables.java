package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.Table;
import java.lang.Math;
import java.util.List;

import org.springframework.stereotype.Component;

// Used by ServeClient to handle logic with specific tables
@Component
public class Tables{
	private final GenAllTablesArray mGen; // Generator object that reads in mTables array

	private Table[] mTables;

	// Returns tables that aren't reserved during date-time of start, end
	// longs are used to store date-time by yyyymmddhhmm (year, month, day, hour, minute)
	private int[] getFreeTablesIndexesDuring(long start, long end) {

		int[] freeTables = new int[mTables.length]; // It is not known how many will be free, allocate space for all tables
		int index = 0;
		for(int i = 0; i < mTables.length; i++) {
			if (mTables[i].isFreeDuring(start, end)) {
				freeTables[index] = i;
				index++; 
			}
		}

		// Return only the free tables.
		// New array to not return unused space in array that was allocated.
		int[] trimmed = new int[index];
		for(int i = 0; i < index; i++) {
			trimmed[i] = freeTables[i];
		}

		return trimmed;
	}


	public Tables(GenAllTablesArray generated) { // Used by springboot to automatically read in tables array
		mGen = generated;
		this.mTables = generated.getArray();
	}
	public void setTablesArray(Table[] arr) { this.mTables = arr; }
	public Table[] getTables() { return this.mTables; };
	public int getHowManyTables() { return this.mTables.length; }

	/**
	 * If possible, a table is found and reserved
	 * long date-times are in form yyyymmddhhmm (year, month, day, hour, minute)
	 * tableType is a specifier - outside/inside table or other specifiers such as a quiet corner
	 * Return value indicates if a suitable table was found and reserved
	 */
	public boolean reserveTable(long startDateTime, long endDateTime, int guests, String tableType) {
		
		// The arrays hold indexes to tables in mTables array
		int[] freeTablesIndexes = getFreeTablesIndexesDuring(startDateTime, endDateTime);
		int[] tableScores = new int[freeTablesIndexes.length];

		// Only set the tables that are free - in the freeTablesIndexes array - as not reserved
		for(Table t : mTables) {
			t.setDisplayAsReserved(true);
		}
		for(int i : freeTablesIndexes) {
			mTables[i].setDisplayAsReserved(false);
		}
		
		// Give scores to all the free tables
		for(int i = 0; i < freeTablesIndexes.length; i++) {
			int index = freeTablesIndexes[i]; // I is the index to the array that has indexes for tables
			if (mTables[index].getMaxSeats() < guests) {
				tableScores[i] = -1; // Mark as not a valid option
				continue;
			}
			int tableScore = 100 - Math.abs(guests - mTables[index].getMaxSeats());
			if(mTables[index].hasType(tableType)) { tableScore += 3; } // Max 3 seats bigger for preferred table type
			/*
			* If a different layout is used and it might be an issue, an extra check can be added:
			* -1 to score if the table also has some other type from required.
			*
			* For example if inside has all tables of size 4 and two of them
			* are type quiet, recommend the ones that are just regular inside tables.
			*
			* Current implementation does not have such cases.
			*/
			
			tableScores[i] = tableScore;
		}

		// After setting all scores, find the best
		int bestScoreIndex = 0;
		for(int i = 0; i < tableScores.length; i++) {
			if(tableScores[i] > tableScores[bestScoreIndex]) { bestScoreIndex = i; }
		}
		
		// Reset all the tables to not show as displayed
		for(Table t : mTables) { t.setDisplayAsSuggested(false); }

		if (tableScores[bestScoreIndex] == -1) { return false; } // No table found!

		int index = freeTablesIndexes[bestScoreIndex];
		mTables[index].setDisplayAsSuggested(true);
		mTables[index].addReservation(startDateTime, endDateTime);

		return true;
	}
	/**
	 * Used to randomly reserve some tables when starting application.
	 * Checks for suitable times and indexes are outside the scope of Tables class.
	 * That means this is a dummy setter-type method. No return type and the only checks are just in case (although shouldn't be needed at all)
	 */
	public void reserveForRandReservation(long startDateTime, long endDateTime, int index) {
		if (index < 0 || index >= mTables.length) { return; }
		if ( endDateTime < startDateTime ) { return; }
		if ( !mTables[index].isFreeDuring(startDateTime, endDateTime) ) { return; }

		mTables[index].addReservation(startDateTime, endDateTime);
	}


	// Testing - to simplify debugging. Not in the final application. Should be removed.
	public void printAllTables() {
		for (Table t : mTables) {
			System.out.println("Seats: " + t.getMaxSeats() + ", Type: " + t.getSizeName() + ", Is suggested: " + t.getDisplayAsSuggested());
		}
	}
	public void printAllReservations() {
		for (int i = 0; i < mTables.length; i++) {
			System.out.println("Table " + i + ":");
			mTables[i].printAllReservations();
		}
	}
}
