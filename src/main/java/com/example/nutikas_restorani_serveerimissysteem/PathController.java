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

@Controller
public class PathController {
	private final ServeClient sc;

	private void reserveRandomTables() {
		Calendar now = Calendar.getInstance();

		// The reservations are only for +- a few hours of time right now. Year, month and day remain the same
		// This is the template that is used to construct start/end date-times
		long dateTime_template = now.get(now.YEAR);
		dateTime_template *= 100;
		dateTime_template += now.get(now.MONTH) + 1; // In Calendar, months start from 0
		dateTime_template *= 100;
		dateTime_template += now.get(now.DAY_OF_MONTH);

		int n = sc.getHowManyTables();
		for( int i = 0; i < n; i++ ) {
			if (Math.random() < 0.5) {
				// Reserve table at index i
				long startDateTime = dateTime_template * 100;
				long endDateTime = dateTime_template * 100;

				// Random reservations are up to -3h and +4h
				// Minimum is +-1h
				// Table must be reserved right now
				long randStart = (int)(Math.random() * 3) + 1;
				long randEnd = (int)(Math.random() * 4) + 1;

				startDateTime += now.get(now.HOUR_OF_DAY) - randStart;
				endDateTime += now.get(now.HOUR_OF_DAY) + randEnd;

				startDateTime *= 100;
				endDateTime *= 100;

				startDateTime += now.get(now.MINUTE);
				endDateTime += now.get(now.MINUTE);

				sc.reserveForRandReservation(startDateTime, endDateTime, i);
			}
		}
	}

	public PathController(ServeClient sc) {
		this.sc = sc;
		this.reserveRandomTables(); // When running the application, randomly reserve some tables for today
	}

	@GetMapping("/")
	public String displayIndex() {
		return "index.html";
	}

	@PostMapping("/reservation") 
	public String displayReservation(FormInfo form, Model model) {
		//sc.printAllTables(); // Testing

		sc.setForm(form);
		boolean foundTable = sc.reserveTable();
		model.addAttribute("reservationInfo", foundTable ? "Your reservation" : "No suitable table found");
		
		// Since the tables are styled, inject fields individually, by the index.
		Table[] tables = sc.getTables();
		for( int i = 0; i < tables.length; i++ ) {
			model.addAttribute("table"+ (i+1), tables[i]);
		}

		return "reservation";
	}
}
