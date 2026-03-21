package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.TableAsClass;
import java.lang.Math;
import java.util.List;
import java.lang.Integer;

import org.springframework.stereotype.Component;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

// Used by ServeClient to handle logic with specific tables
@Component
public class Tables{
	private final GenAllTablesArray mGen; // Generator object that reads in mTables array

	@PersistenceContext
	private EntityManager mEM;

	private TableAsClass[] mTables;

	// Returns tables that aren't reserved during date-time of start, end
	// longs are used to store date-time by yyyymmddhhmm (year, month, day, hour, minute)
	private List<Integer> getFreeTablesIndexesDuring(long start, long end) {

		// Query the id WHERE ...
		return mEM.createNativeQuery(
			"SELECT t.id "
			+ "FROM tables AS t "
			+ "JOIN reservations AS r "
			+ "ON t.id=r.table_id "
			+ "WHERE NOT (:end < r.start OR :start > r.end);", Integer.class)   // Specify that Integer should be returned
			.setParameter("start", start)
			.setParameter("end", end)
			.getResultList();
	}
	private int getMaxIdFromTables() {
		return (int) mEM.createNativeQuery(
			"SELECT MAX(id) FROM tables;").getSingleResult();
	}


	public Tables(GenAllTablesArray generated) { // Used by springboot to automatically read in tables array
		mGen = generated;
	}
	public void setTablesArray(TableAsClass[] arr) { this.mTables = arr; }
	public List<TableAsClass> getTables() { 
		
		return mEM.createQuery(
			"SELECT * from tables"
			+ "ORDER BY id;", TableAsClass.class)
		.getResultList();
		
		};
	public int getHowManyTables() { 
		return getMaxIdFromTables(); // Since id indexing starts with 1 instead of 0, this is the correct int.
		}

	/**
	 * If possible, a table is found and reserved
	 * long date-times are in form yyyymmddhhmm (year, month, day, hour, minute)
	 * tableType is a specifier - outside/inside table or other specifiers such as a quiet corner
	 * Return value indicates if a suitable table was found and reserved
	 */
	public boolean reserveTable(long startDateTime, long endDateTime, int guests, String tableType) {
		
		// Qurety tables join reservations
		// WHERE ......
		// -> only the id attribute into array.

		// Set displayAsReserved to true for all rows
		// Set displayAsSuggested to false for all rows

		// For loop
		// -> set displayasreserved to false where id=i

		// Scoring is the same

		// set displayAsSuggested to true where id=finalIndex

		// The arrays hold indexes to tables in mTables array
		
		List<Integer> freeTablesIndexes = getFreeTablesIndexesDuring(startDateTime, endDateTime);
		int[] tableScores = new int[freeTablesIndexes.size()];

		mEM.createNativeQuery(
			"UPDATE tables"
			+ "SET m_display_as_reserved = true;");
		mEM.createNativeQuery(
			"UPDATE tables"
			+ "SET m_display_as_suggested = false;");

		for(Integer i : freeTablesIndexes) {
			mEM.createNativeQuery(
				"UPDATE tables AS t"
				+ "SET m_display_as_reserved = false"
				+ "WHERE t.id = :i;")
			.setParameter("i", i);
		}
		

		// TODO - SCORING LOGIC DOESN'T WORK CURRENTLY!!!!

		// Give scores to all the free tables
		/*for(int i = 0; i < freeTablesIndexes.length; i++) {
			int index = freeTablesIndexes[i]; // I is the index to the array that has indexes for tables
			if (mTables[index].getMaxSeats() < guests) {
				tableScores[i] = -1; // Mark as not a valid option
				continue;
			}
			int tableScore = 100 - Math.abs(guests - mTables[index].getMaxSeats());
			if(mTables[index].hasType(tableType)) { tableScore += 3; } // Max 3 seats bigger for preferred table type
			
			//* If a different layout is used and it might be an issue, an extra check can be added:
			//* -1 to score if the table also has some other type from required.
			//*
			//* For example if inside has all tables of size 4 and two of them
			//* are type quiet, recommend the ones that are just regular inside tables.
			//*
			//* Current implementation does not have such cases.
			
			
			tableScores[i] = tableScore;
		}*/

		// After setting all scores, find the best
		int bestScoreIndex = 0;
		for(int i = 0; i < tableScores.length; i++) {
			if(tableScores[i] > tableScores[bestScoreIndex]) { bestScoreIndex = i; }
		}

		if (tableScores[bestScoreIndex] == -1) { return false; } // No table found!

		int index = freeTablesIndexes.get(bestScoreIndex);

		mEM.createNativeQuery(
			"UPDATE reservations AS r"
			+ "SET m_display_as_suggested = true;");
		mEM.createNativeQuery(
			"INSERT INTO reservations(starttime, endtime, table_id)"
			+ "VALUES(:start, :end, :index);")
		.setParameter("start", startDateTime)
		.setParameter("end", endDateTime)
		.setParameter("index", index);

		return true;
	}
	/**
	 * Used to randomly reserve some tables when starting application.
	 * Checks for suitable times and indexes are outside the scope of Tables class.
	 * That means this is a dummy setter-type method. No return type and the only checks are just in case (although shouldn't be needed at all)
	 */
	public void reserveForRandReservation(long startDateTime, long endDateTime, int index) {
		if ( endDateTime < startDateTime ) { return; }

		int maxId = getMaxIdFromTables();
		if (index < 0 || index > maxId) { return; }


		boolean isReserved = (boolean) mEM.createNativeQuery(
			"SELECT EXISTS(" 
			+ "SELECT starttime, endtime " 
			+ "FROM reservations "
			+ "WHERE table_id = :id "
			+ "AND NOT (starttime > :e OR endtime < :s)" 
			+ ");"
			//+ "THEN 'TRUE';"
		)
		.setParameter("id", index)
		.setParameter("s", startDateTime)
		.setParameter("e", endDateTime)
		.getSingleResult();
		if ( isReserved ) { return; }

		mEM.createNativeQuery(
			"INSERT INTO reservations(starttime, endtime, table_id)"
			+ "VALUES(:start, :end, :index);")
			.setParameter("start", startDateTime)
			.setParameter("end", endDateTime)
			.setParameter("index", index);
	}


	// Testing - to simplify debugging. Not in the final application. Should be removed.
	public void printAllTables() {
		for (TableAsClass t : mTables) {
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
