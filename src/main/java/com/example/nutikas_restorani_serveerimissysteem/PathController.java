package com.example.nutikas_restorani_serveerimissysteem;

import com.example.nutikas_restorani_serveerimissysteem.logic.ServeClient;
import com.example.nutikas_restorani_serveerimissysteem.logic.Table;
import com.example.nutikas_restorani_serveerimissysteem.FormInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class PathController {
	private final ServeClient sc;

	public PathController(ServeClient sc) {
		this.sc = sc;
	}

	@GetMapping("/")
	public String displayIndex() {
		return "index.html";
	}

	@PostMapping("/reservation") 
	public String displayReservation(FormInfo form, Model model) {
		//sc.printAllTables(); // Testing

		sc.setForm(form);
		sc.reserveTable();

		// Since the tables are styled, inject fields individually, by the index.
		Table[] tables = sc.getTables();
		for( int i = 0; i < tables.length; i++ ) {
			model.addAttribute("table"+ (i+1), tables[i]);
		}

		return "reservation";
	}
}
