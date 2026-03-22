package com.example.nutikas_restorani_serveerimissysteem;

import com.example.nutikas_restorani_serveerimissysteem.logic.ServeClient;
import com.example.nutikas_restorani_serveerimissysteem.logic.RestaurantTables;
import com.example.nutikas_restorani_serveerimissysteem.FormInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

// All logic to control url paths
@Controller
public class PathController {
	private final ServeClient sc; // Handles all the logic for changing fields in tables to style-display properly

	public PathController(ServeClient sc) { this.sc = sc; }

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
		List<RestaurantTables> tables = sc.getTables();
		for( int i = 0; i < tables.size(); i++ ) {
			model.addAttribute("table"+ (i+1), tables.get(i));
		}

		return "reservation";
	}
}
