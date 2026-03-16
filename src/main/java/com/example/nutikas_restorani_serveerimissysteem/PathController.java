package com.example.nutikas_restorani_serveerimissysteem;

import com.example.nutikas_restorani_serveerimissysteem.logic.ServeClient;
import com.example.nutikas_restorani_serveerimissysteem.logic.Table;
import com.example.nutikas_restorani_serveerimissysteem.FormInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import java.util.Calendar;

// All logic to control url paths
// Extra responsibility is to generate random reservations when application starts.
@Controller
public class PathController {
	private final ServeClient sc; // Handles all the logic for changing fields in tables to style-display properly

	/** 
	 * Reserves tables randomly. Called from constructor.
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
		for( int i = 0; i < n; i++ ) {
			if (Math.random() < 0.5) {
				// Reserve table at index i
				
				// Construct new values for date-time from the template
				long startDateTime = dateTime_template * 100;
				long endDateTime = dateTime_template * 100;

				long randStart = (int)(Math.random() * 3) + 1;
				long randEnd = (int)(Math.random() * 4) + 1;

				// Add random hour values by specified rules
				startDateTime += now.get(now.HOUR_OF_DAY) - randStart;
				endDateTime += now.get(now.HOUR_OF_DAY) + randEnd;

				startDateTime *= 100;
				endDateTime *= 100;

				// Minutes can remain the same - could be added to template
				startDateTime += now.get(now.MINUTE);
				endDateTime += now.get(now.MINUTE);

				sc.reserveForRandReservation(startDateTime, endDateTime, i);
			}
		}
	}

	public PathController(ServeClient sc) {
		this.sc = sc;
		this.reserveRandomTables(); // When the application starts, randomly reserve some tables for today
	}

	@GetMapping("/")
	public String indexPage() {
		return "index";
	}
	@GetMapping("/admin")
	public String adminPage() {
		return "admin";
	}

	@PostMapping("/reservation") 
	public String reservationPage(FormInfo form, Model model) {

		sc.setForm(form);
		boolean foundTable = sc.reserveTable();

		// Styling that tells user if a suitable table was found
		model.addAttribute("reservationInfo", foundTable ? "Your reservation" : "No suitable table found");
		
		// Since the different tables are styled differently, inject fields individually, by the index.
		Table[] tables = sc.getTables();
		for( int i = 0; i < tables.length; i++ ) {
			model.addAttribute("table"+ (i+1), tables[i]);
		}

		return "reservation";
	}
}
