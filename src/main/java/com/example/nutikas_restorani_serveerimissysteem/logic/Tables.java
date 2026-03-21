package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.TableAsClass;
import java.lang.Math;
import java.util.List;
import java.lang.Integer;

import org.springframework.stereotype.Component;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

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

		return mEM.createNativeQuery( // Get all id's minus ones where reservation time is matching
			"SELECT t.id "			  // Reservation time is not matching if end < r.starttime or start > r.endtime (the 2 can't overlap since end > start everywhere)
			+ "FROM tables AS t "
			+ "WHERE NOT EXISTS ( "
			+ "    SELECT r.id "
			+ "    FROM reservations AS r "
			+ "    WHERE r.table_id=t.id "
			+ "    AND NOT (:end < r.starttime OR :start > r.endtime) "
			+ ");", Integer.class)   // Specify that Integer should be returned
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
	public void fillDatabase() { this.mGen.populateTables(); }

	public void setTablesArray(TableAsClass[] arr) { this.mTables = arr; }
	public List<TableAsClass> getTables() { 
		
		return mEM.createNativeQuery(
			"SELECT * from tables "
			+ "ORDER BY id ASC;", TableAsClass.class)
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
	@Transactional
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

		setReservedByList(freeTablesIndexes);
		setAllSuggestedToFalse();
		

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

		setSuggestedToTrueById(index);
		insertReservationToTable(startDateTime, endDateTime, index);

		return true;
	}
	/**
	 * Used to randomly reserve some tables when starting application.
	 * Checks for suitable times and indexes are outside the scope of Tables class.
	 * That means this is a dummy setter-type method. No return type and the only checks are just in case (although shouldn't be needed at all)
	 */
	@Transactional
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
		)
		.setParameter("id", index+1)
		.setParameter("s", startDateTime)
		.setParameter("e", endDateTime)
		.getSingleResult();
		if ( isReserved ) { return; }

		insertReservationToTable(startDateTime, endDateTime, index+1);

	}

	public void setReservedByList(List<Integer> freeTablesIndexes) {
		mEM.createNativeQuery(
			"UPDATE tables "
			+ "SET m_display_as_reserved = 'TRUE';")
			.executeUpdate();

		for(Integer i : freeTablesIndexes) {
			mEM.createNativeQuery(
				"UPDATE tables "
				+ "SET m_display_as_reserved = 'FALSE'"
				+ "WHERE id = :i;")
			.setParameter("i", i)
			.executeUpdate();
		}
	}
	public void setAllSuggestedToFalse() {
		mEM.createNativeQuery(
			"UPDATE tables "
			+ "SET m_display_as_suggested = 'FALSE';")
			.executeUpdate();
	}
	public void setSuggestedToTrueById(int id) {
		mEM.createNativeQuery(
			"UPDATE tables "
			+ "SET m_display_as_suggested = 'TRUE' "
			+ "WHERE id=:id")
			.setParameter("id", id)
			.executeUpdate();
	}
	public void insertReservationToTable(long startDateTime, long endDateTime, int id) {
		mEM.createNativeQuery(
			"INSERT INTO reservations(starttime, endtime, table_id) "
			+ "VALUES(:start, :end, :id);")
		.setParameter("start", startDateTime)
		.setParameter("end", endDateTime)
		.setParameter("id", id)
		.executeUpdate();
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
