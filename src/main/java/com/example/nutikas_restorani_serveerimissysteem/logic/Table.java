package com.example.nutikas_restorani_serveerimissysteem.logic;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class Table {
	private int mMaxSeats;
	private String mSizeName; // Name of the size of the table - smallTable/mediumTable/mediumTable2/bigTable
							// Used for styling the webpage. This is the class name of the div that represents the table
	
	private boolean mDisplayAsSuggested; // Id-s that are added for styling of webpage
	private boolean mDisplayAsReserved;

	private List<long[]> mReservationsArray; // array that holds long[2] - start and end of reservation
					   // yyyymmddhhmm - as long

	private String[] mTableTypes; // Specifies more information about the table - outside/inside/quiet (corner) etc.

	private void sortReservations() {
		// Construct long for current time-date to remove expired reservations
		Calendar now = Calendar.getInstance();

		long dateTimeNow = now.get(now.YEAR);
		dateTimeNow *= 100;
		dateTimeNow += now.get(now.MONTH) + 1; // In Calendar, months start from 0
		dateTimeNow *= 100;
		dateTimeNow += now.get(now.DAY_OF_MONTH);
		dateTimeNow *= 100;
		dateTimeNow += now.get(now.HOUR_OF_DAY);
		dateTimeNow *= 100;
		dateTimeNow += now.get(now.MINUTE);
		final long final_dateTimeNow = dateTimeNow;

		mReservationsArray.sort(Comparator.comparingLong( l -> l[0])); // Sort by the start datetime value

		mReservationsArray.removeIf(el -> el[1] <= final_dateTimeNow);
	}

	public Table() {
		this.mMaxSeats = 0;
		this.mDisplayAsSuggested = false;
		this.mReservationsArray = new ArrayList<long[]>();
	}
	public Table(int maxSeats, String sizeName, String[] tableTypes) { 
		this.mMaxSeats = maxSeats;
		this.mSizeName = sizeName;
		this.mDisplayAsSuggested = false;
		this.mReservationsArray = new ArrayList<long[]>();
		this.mTableTypes = tableTypes;
		}
	
	public void setMaxSeats(int max) { this.mMaxSeats = max; }
	public void setSizeName(String sizeName) { this.mSizeName = sizeName; }
	public void setDisplayAsSuggested(boolean isSuggested) { this.mDisplayAsSuggested = isSuggested; }
	public void setDisplayAsReserved(boolean isReserved) { this.mDisplayAsReserved = isReserved; }

	public int getMaxSeats() { return this.mMaxSeats; }
	public String getSizeName() { return this.mSizeName; }
	public boolean getDisplayAsSuggested() { return this.mDisplayAsSuggested; }
	public boolean getDisplayAsReserved() { return this.mDisplayAsReserved; }

	public boolean hasType(String type) {
		for(String str : mTableTypes) {
			if (str.equals(type)) { return true; }
		}
		return false;
	}
	public boolean isFreeDuring(long start, long end) {
			this.sortReservations(); // Sort every time so it's more predictable - and so outdated info is removed

			for(long[] r : mReservationsArray) {
				if( !(end < r[0] || start > r[1]) ) {
					return false;
				}
			}
			return true;
	}
	/**
	 * Adds a new reservation - all necessary checks are done in the Tables class that wraps around this class.
	 */
	public void addReservation(long start, long end) {
		long[] reservation = new long[2];

		reservation[0] = start;
		reservation[1] = end;

		mReservationsArray.add(reservation);
	}

	// Testing - used to make debugging easier. Remove from final application!
	public void printAllReservations() {
		for (long[] r : mReservationsArray) {
			System.out.println("-- Start: " + r[0]);
			System.out.println("-- End: " + r[1]);
		}
	}
}
