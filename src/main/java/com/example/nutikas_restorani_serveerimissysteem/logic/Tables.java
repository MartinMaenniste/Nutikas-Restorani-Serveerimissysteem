package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.RestaurantTables;
import java.lang.Math;
import java.util.List;
import java.lang.Integer;
import java.util.Calendar;

import org.springframework.stereotype.Component;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

// Used by ServeClient to handle all logic with specific tables
@Component
public class Tables{

	@PersistenceContext
	private EntityManager mEM;

	// Returns tables that aren't reserved during date-time of start, end
	// longs are used to store date-time by yyyymmddhhmm (year, month, day, hour, minute)
	private List<Integer> getFreeTablesIdsDuring(long start, long end) {

		return mEM.createNativeQuery( // Get all id's minus ones where reservation time is matching
			"SELECT t.id "			  // Reservation time is not matching if end < r.starttime or start > r.endtime (the 2 can't overlap since end > start everywhere)
			+ "FROM restaurant_tables AS t "
			+ "WHERE NOT EXISTS ( "
			+ "    SELECT r.id "
			+ "    FROM reservations AS r "
			+ "    WHERE r.table_id=t.id "
			+ "    AND NOT (:end < r.starttime OR :start > r.endtime) "
			+ ") "
			+ "ORDER BY t.id ASC;", Integer.class)   // Specify that Integer should be returned
			// Remove ordering for random table out of suitable group, instead of lowest id
			.setParameter("start", start)
			.setParameter("end", end)
			.getResultList();
	}
	private int getMaxIdFromTables() {
		return (int) mEM.createNativeQuery(
			"SELECT MAX(id) FROM restaurant_tables;").getSingleResult();
	}
	/**
	 * Method makes sure only the restaurant tables with id's in the list given as parameter are not shown as reserved.
	 * All other restaurant tables are shown to be reserved.
	 * 
	 * freeTablesIds - List that holds restaurant_tables.id values for which there is no reservation that overlaps the time right now.
	 */
	private void setReservedByList(List<Integer> freeTablesIds) {
		mEM.createNativeQuery(
			"UPDATE restaurant_tables "
			+ "SET m_display_as_reserved = 'TRUE';")
			.executeUpdate();

		for(Integer i : freeTablesIds) {
			mEM.createNativeQuery(
				"UPDATE restaurant_tables "
				+ "SET m_display_as_reserved = 'FALSE'"
				+ "WHERE id = :i;")
			.setParameter("i", i)
			.executeUpdate();
		}
	}
	// Following 2 methods make sure only the suggested table gets shown as suggested - styled accordingly.
	private void setAllSuggestedToFalse() {
		mEM.createNativeQuery(
			"UPDATE restaurant_tables "
			+ "SET m_display_as_suggested = 'FALSE';")
			.executeUpdate();
	}
	private void setSuggestedToTrueById(int id) {
		mEM.createNativeQuery(
			"UPDATE restaurant_tables "
			+ "SET m_display_as_suggested = 'TRUE' "
			+ "WHERE id=:id")
			.setParameter("id", id)
			.executeUpdate();
	}
	/**
	 * startDateTime - Date + Time in the form of yymmddhhmm (year, month, day, hour, minute)
	 * endDateTime - - Date + Time in the form of yymmddhhmm (year, month, day, hour, minute)
	 * id - restaurant_tables.id for which this reservation is made
	 */
	private void insertReservationToTable(long startDateTime, long endDateTime, int id) {
		// reservations table holds the reservations that are for rows in restarurant_tables
		// foreign key id makes it specific to one table.

		mEM.createNativeQuery(
			"INSERT INTO reservations(starttime, endtime, table_id) "
			+ "VALUES(:start, :end, :id);")
		.setParameter("start", startDateTime)
		.setParameter("end", endDateTime)
		.setParameter("id", id)
		.executeUpdate();
	}
	private int getMaxSeatsById(int id) {
		return (int) mEM.createNativeQuery(
			"SELECT m_max_seats "
			+ "FROM restaurant_tables "
			+ "WHERE id=:id;"
		)
		.setParameter("id", id)
		.getSingleResult();
	}
	/**
	 * Method checks if a restaurant table has type given by parameter. (if there is an according row in tabletypes table)
	 * 
	 * id - restaurant_tables.id for which the type is checked
	 * type - the type to be checked for a restaurant table
	 * 
	 * returns - if the restaurant table has specified type (more specifically if there exists such a row in tabletypes table)
	 */
	private boolean tableByIdHasType(int id, String type) {
		return (boolean) mEM.createNativeQuery(
			"SELECT EXISTS ( "
			+ "SELECT * FROM restaurant_tables AS ta "
			+ "JOIN tabletypes AS ty "
			+ "ON ta.id=ty.table_id "
			+ "WHERE ta.id=:id "
			+ "AND ty.type=:type"
			+ ");"
		)
		.setParameter("id", id)
		.setParameter("type", type)
		.getSingleResult();
	}
	/**
	 * Method is called before every reservation to remove expired reservations.
	 */
	private void removeExpiredReservations() {
		// Create a (long) datetime variable and use a simple query

		Calendar now = Calendar.getInstance();

		long dateTime = now.get(now.YEAR);
		dateTime *= 100;
		dateTime += now.get(now.MONTH) + 1; // In Calendar, months start from 0
		dateTime *= 100;
		dateTime += now.get(now.DAY_OF_MONTH);
		dateTime *= 100;
		dateTime += now.get(now.HOUR_OF_DAY) + 2; // Timezone is off by 2h. Temporary solution.
		dateTime *= 100;
		dateTime += now.get(now.MINUTE);

		mEM.createNativeQuery(
			"DELETE FROM reservations "
			+ "WHERE endtime < :datetimenow;"
		)
		.setParameter("datetimenow", dateTime)
		.executeUpdate();
	}
	public List<RestaurantTables> getTables() { 
		return mEM.createNativeQuery(
			"SELECT * from restaurant_tables "
			+ "ORDER BY id ASC;", RestaurantTables.class)
		.getResultList();
	};
	public int getHowManyTables() { 
		return getMaxIdFromTables(); // Since id indexing starts with 1 instead of 0, this is the correct int.
	}
	public Tables() {}

	/**
	 * If possible, a table is found and reserved
	 * long date-times are in form yyyymmddhhmm (year, month, day, hour, minute)
	 * tableType is a specifier - outside/inside table or other specifiers such as a quiet corner
	 * Return value indicates if a suitable table was found and reserved
	 */
	@Transactional
	public boolean reserveTable(long startDateTime, long endDateTime, int guests, String tableType) {
		/**
		 * Method first removes old reservations
		 * Then resets the styling fields that indicate reserved and suggested table by getting a list of restaurant_table.id that are free during wanted reservation
		 * 
		 * Then all the restaurant tables are scored
		 * Finally, a reservation is made (or early return in the case of no free tables.)
		 */

		// Every reservation, remove expired reservations
		removeExpiredReservations();
		
		List<Integer> freeTablesIds = getFreeTablesIdsDuring(startDateTime, endDateTime);
		int[] tableScores = new int[freeTablesIds.size()];

		setReservedByList(freeTablesIds);
		setAllSuggestedToFalse();

		// Give scores to all the free tables
		for(int i = 0; i < freeTablesIds.size(); i++) {
			int id = freeTablesIds.get(i); // I is the index to the array that has id's for tables

			int maxSeats = getMaxSeatsById(id);

			if (maxSeats < guests) {
				tableScores[i] = -1; // Mark as not a valid option
				continue;
			}
			int tableScore = 100 - Math.abs(guests - maxSeats);
			if(tableByIdHasType(id, tableType)) { tableScore += 3; } // Max 3 seats bigger for preferred table type
			
			//* If a different layout is used and it might be an issue, an extra check can be added:
			//* -1 to score if the table also has some other type from required.
			//*
			//* For example if inside has all tables of size 4 and two of them
			//* are type quiet, recommend the ones that are just regular inside tables.
			//*
			//* Current implementation does not have such cases.
			
			tableScores[i] = tableScore;
		}

		// After setting all scores, find the best
		int bestScoreIndexInList = 0;
		for(int i = 0; i < tableScores.length; i++) {
			if(tableScores[i] > tableScores[bestScoreIndexInList]) { bestScoreIndexInList = i; }
		}

		if (tableScores[bestScoreIndexInList] == -1) { return false; } // No table found!

		int bestId = freeTablesIds.get(bestScoreIndexInList);

		setSuggestedToTrueById(bestId);
		insertReservationToTable(startDateTime, endDateTime, bestId);

		return true;
	}
	/**
	 * Used to randomly reserve some tables when starting application.
	 * The checks for suitable times and id validity are outside the scope of Tables class.
	 * That means this is a dummy setter-type method. No return type and the only checks are just in case (although shouldn't be needed at all)
	 */
	@Transactional
	public void reserveForRandReservation(long startDateTime, long endDateTime, int id) {
		if ( endDateTime < startDateTime ) { return; }

		int maxId = getMaxIdFromTables();
		if (id <= 0 || id > maxId) { return; }

		boolean isReserved = (boolean) mEM.createNativeQuery(
			"SELECT EXISTS(" 
			+ "SELECT starttime, endtime " 
			+ "FROM reservations "
			+ "WHERE table_id = :id "
			+ "AND NOT (starttime > :e OR endtime < :s)" 
			+ ");"
		)
		.setParameter("id", id)
		.setParameter("s", startDateTime)
		.setParameter("e", endDateTime)
		.getSingleResult();
		if ( isReserved ) { return; }

		insertReservationToTable(startDateTime, endDateTime, id);

	}
}