package com.example.nutikas_restorani_serveerimissysteem;

import com.example.nutikas_restorani_serveerimissysteem.logic.PopulateDB;
import com.example.nutikas_restorani_serveerimissysteem.logic.ServeClient;

import java.util.Calendar;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;


/**
 * This class is responsible for filling the database with initial values.
 * implements CommandLineRunner to make sure database is accessed after the Spring app has started up
 * 
 * Using PopulateDB the class fills restaurant_tables and generates "random" reservations.
 */
@Component
public class StartupRunner implements CommandLineRunner {
    private final PopulateDB gen;
    private final ServeClient sc;

    /** 
	 * Reserves tables randomly.
	 * Each table has a 50% chance to be reserved at the time of right now (reservations that don't overlap current time aren't added).
	 * When a table is reserved, it is randomly reserved with start/end times:
	 * - start time is from -3h to -1h from right now
	 * - end time is from +1h to +4h from right now
	*/
	private void reserveRandomTables() {

		Calendar now = Calendar.getInstance();

		// The reservations are only for +- a few hours of time right now. Year, month and day remain the same
		// This is the template that is used to construct final start/end date-times
		long dateTime_template = now.get(now.YEAR);
		dateTime_template *= 100;
		dateTime_template += now.get(now.MONTH) + 1; // In Calendar, months start from 0
		dateTime_template *= 100;
		dateTime_template += now.get(now.DAY_OF_MONTH);

		int n = sc.getHowManyTables();
		for( int i = 1; i < n+1; i++ ) {
			if (Math.random() < 0.5) {
				// Reserve restaurant table with id = i
				
				// Construct new values for date-time from the template
				long startDateTime = dateTime_template * 100;
				long endDateTime = dateTime_template * 100;

				long randStart = (int)(Math.random() * 3) + 1;
				long randEnd = (int)(Math.random() * 4) + 1;

				// Add random hour values by specified rules - timezone is off by 2 hours, temporary solution.
				startDateTime += now.get(now.HOUR_OF_DAY) + 2 - randStart;
				endDateTime += now.get(now.HOUR_OF_DAY) + 2 + randEnd;

				startDateTime *= 100;
				endDateTime *= 100;

				// Minutes can remain the same - could be added to template
				startDateTime += now.get(now.MINUTE);
				endDateTime += now.get(now.MINUTE);

				sc.reserveForRandReservation(startDateTime, endDateTime, i);
			}
		}
	}

    public StartupRunner(PopulateDB gen, ServeClient sc) {this.gen = gen; this.sc=sc;}

    @Override
    public void run(String... args) { // After Spring has been initialised, fill the database.
        gen.populateTables(); // Populates the "tables" and "tabletypes" tables (via tables.txt)
        reserveRandomTables(); // Populates the reservations table
    }
}