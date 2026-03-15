package com.example.nutikas_restorani_serveerimissysteem.logic;

import java.util.List;
import java.util.ArrayList;

public class Table {
	private int mMaxSeats;
	private String mSizeName; // Name of the size of the table - small/medium/big
	private boolean mDisplayAsSuggested;
	private boolean mDisplayAsReserved;

	private List<long[]> mReservationsArray; // array that holds long[2] - start and end of reservation
					   // yyyymmddhhmm - as long
	private int mTotalReservations;
	// String type - out/in/...

	private void sortReservations() {
		// Delete outdated
		// sort by start - smaller numbers first (dates that come sooner)
	}

	public Table() {
		this.mMaxSeats = 0;
		this.mTotalReservations = 0;
		this.mDisplayAsSuggested = false;
		this.mReservationsArray = new ArrayList<long[]>();
	}
	public Table(int maxSeats, String sizeName) { 
		this.mMaxSeats = maxSeats;
		this.mTotalReservations = 0;
		this.mSizeName = sizeName;
		this.mDisplayAsSuggested = false;
		this.mReservationsArray = new ArrayList<long[]>();
		}
	
	public void setMaxSeats(int max) { this.mMaxSeats = max; }
	public void setSizeName(String sizeName) { this.mSizeName = sizeName; }
	public void setDisplayAsSuggested(boolean isSuggested) { this.mDisplayAsSuggested = isSuggested; }
	public void setDisplayAsReserved(boolean isReserved) { this.mDisplayAsReserved = isReserved; }

	public int getMaxSeats() { return this.mMaxSeats; }
	public String getSizeName() { return this.mSizeName; }
	public boolean getDisplayAsSuggested() { return this.mDisplayAsSuggested; }
	public boolean getDisplayAsReserved() { return this.mDisplayAsReserved; }
	

	public boolean isFreeDuring(long start, long end) {
			this.sortReservations(); // Sort every time so it's more predictable - and so outdated info is removed

			for(long[] r : mReservationsArray) {
				if( !(end < r[0] || start > r[1]) ) {
					return false;
				}
			}
			return true;
	}
	public void addReservation(long start, long end) {
		long[] reservation = new long[2];

		reservation[0] = start;
		reservation[1] = end;

		mReservationsArray.add(reservation);
	}

	// Testing
	public void printAllReservations() {
		for (long[] r : mReservationsArray) {
			System.out.println("-- Start: " + r[0]);
			System.out.println("-- End: " + r[1]);
		}
	}
}
