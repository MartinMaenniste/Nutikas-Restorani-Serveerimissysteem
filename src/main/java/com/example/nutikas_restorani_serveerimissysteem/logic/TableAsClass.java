package com.example.nutikas_restorani_serveerimissysteem.logic;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Calendar;
import java.util.Comparator;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;

/**
 * Defines the "restaurant_tables" table
 * Holds most of the information this application needs to run
 * id - program uses the default id that is generated for indexing
 * m_max_seats - max amount of seats for the table in the restaurant it represents
 * m_size_name - used to style the table on webpage. This is the class of the dif that represents that restaurant table in webpage.
 * m_display_as_suggested and m_display_as_reserved are id's that can be given for styling webpage (otherwise default styling)
 * 
 * restaurant_tables.id is used as foreign key in reservations and tabletypes tables.
 */

@Entity
@Table(name = "restaurant_tables")
public class TableAsClass {
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Integer id;

	private int mMaxSeats;
	private String mSizeName; // Name of the size of the table - smallTable/mediumTable/mediumTable2/bigTable

	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean mDisplayAsSuggested;	 // Id-s that are added for styling of webpage
	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean mDisplayAsReserved;

	@OneToMany(mappedBy="table")
	private List<Reservations> mReservations;
	@OneToMany(mappedBy="table")
	private Set<TableTypes> mTabletypes;
	

	public TableAsClass() {}
}
