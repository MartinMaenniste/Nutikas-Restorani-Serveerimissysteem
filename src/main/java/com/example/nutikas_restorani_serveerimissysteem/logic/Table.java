package com.example.nutikas_restorani_serveerimissysteem.logic;

public class Table {
	private int mMaxSeats;
	private String mSizeName; // Name of the size of the table - small/medium/big
	private boolean mDisplayAsSuggested;

	private long[][] mReservationsArray; // array that holds long[2] - start and end of reservation
					   // yyyymmddhhmm - as long
	private int mTotalReservations;
	// String type - out/in/...

	private void sortReservations() {
		// Delete outdated
		// sort by start - smaller numbers first (dates that come sooner)
	}
	private void increaseArraySize() {
		long[][] newArray = new long[mReservationsArray.length * 2][2];
		for(int i = 0; i < newArray.length; i++) {
			newArray[i] = mReservationsArray[i];
		}
		mReservationsArray = newArray;
	}

	public Table() {
		this.mMaxSeats = 0;
		this.mDisplayAsSuggested = false;
		this.mReservationsArray = new long[2][2];
	}
	public Table(int maxSeats, String sizeName) { 
		this.mMaxSeats = maxSeats;
		this.mSizeName = sizeName;
		this.mDisplayAsSuggested = false;
		this.mReservationsArray = new long[2][2];
		}
	
	public void setMaxSeats(int max) { this.mMaxSeats = max; }
	public void setSizeName(String sizeName) { this.mSizeName = sizeName; }
	public void setDisplayAsSuggested(boolean isSuggested) { mDisplayAsSuggested = isSuggested; }

	public int getMaxSeats() { return this.mMaxSeats; }
	public String getSizeName() { return this.mSizeName; }
	public boolean getDisplayAsSuggested() { return this.mDisplayAsSuggested; }
	

	public boolean isFreeDuring(long start, long end) {
			this.sortReservations(); // Sort every time so it's more predictable - and so outdated info is removed

			for(long[] r : mReservationsArray) {
				if( !(end < r[0] || start > r[0]) )
					return false;
			}
			return true;
	}
	public void addReservation(long start, long end) {
		if (mReservationsArray.length == mTotalReservations)
			this.increaseArraySize();

		mReservationsArray[mTotalReservations][0] = start;
		mReservationsArray[mTotalReservations][1] = end;
		mTotalReservations++;
	}
}
